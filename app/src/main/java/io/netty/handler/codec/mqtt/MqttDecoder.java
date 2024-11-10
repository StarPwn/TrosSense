package io.netty.handler.codec.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttSubscriptionOption;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.netty.channel.raknet.RakConstants;

/* loaded from: classes4.dex */
public final class MqttDecoder extends ReplayingDecoder<DecoderState> {
    private int bytesRemainingInVariablePart;
    private final int maxBytesInMessage;
    private final int maxClientIdLength;
    private MqttFixedHeader mqttFixedHeader;
    private Object variableHeader;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public enum DecoderState {
        READ_FIXED_HEADER,
        READ_VARIABLE_HEADER,
        READ_PAYLOAD,
        BAD_MESSAGE
    }

    public MqttDecoder() {
        this(MqttConstant.DEFAULT_MAX_BYTES_IN_MESSAGE, 23);
    }

    public MqttDecoder(int maxBytesInMessage) {
        this(maxBytesInMessage, 23);
    }

    public MqttDecoder(int maxBytesInMessage, int maxClientIdLength) {
        super(DecoderState.READ_FIXED_HEADER);
        this.maxBytesInMessage = ObjectUtil.checkPositive(maxBytesInMessage, "maxBytesInMessage");
        this.maxClientIdLength = ObjectUtil.checkPositive(maxClientIdLength, "maxClientIdLength");
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        switch (state()) {
            case READ_FIXED_HEADER:
                try {
                    this.mqttFixedHeader = decodeFixedHeader(ctx, buffer);
                    this.bytesRemainingInVariablePart = this.mqttFixedHeader.remainingLength();
                    checkpoint(DecoderState.READ_VARIABLE_HEADER);
                } catch (Exception cause) {
                    out.add(invalidMessage(cause));
                    return;
                }
            case READ_VARIABLE_HEADER:
                try {
                    Result<?> decodedVariableHeader = decodeVariableHeader(ctx, buffer, this.mqttFixedHeader);
                    this.variableHeader = ((Result) decodedVariableHeader).value;
                    if (this.bytesRemainingInVariablePart > this.maxBytesInMessage) {
                        buffer.skipBytes(actualReadableBytes());
                        throw new TooLongFrameException("too large message: " + this.bytesRemainingInVariablePart + " bytes");
                    }
                    this.bytesRemainingInVariablePart -= ((Result) decodedVariableHeader).numberOfBytesConsumed;
                    checkpoint(DecoderState.READ_PAYLOAD);
                } catch (Exception cause2) {
                    out.add(invalidMessage(cause2));
                    return;
                }
            case READ_PAYLOAD:
                try {
                    Result<?> decodedPayload = decodePayload(ctx, buffer, this.mqttFixedHeader.messageType(), this.bytesRemainingInVariablePart, this.maxClientIdLength, this.variableHeader);
                    this.bytesRemainingInVariablePart -= ((Result) decodedPayload).numberOfBytesConsumed;
                    if (this.bytesRemainingInVariablePart != 0) {
                        throw new DecoderException("non-zero remaining payload bytes: " + this.bytesRemainingInVariablePart + " (" + this.mqttFixedHeader.messageType() + ')');
                    }
                    checkpoint(DecoderState.READ_FIXED_HEADER);
                    MqttMessage message = MqttMessageFactory.newMessage(this.mqttFixedHeader, this.variableHeader, ((Result) decodedPayload).value);
                    this.mqttFixedHeader = null;
                    this.variableHeader = null;
                    out.add(message);
                    return;
                } catch (Exception cause3) {
                    out.add(invalidMessage(cause3));
                    return;
                }
            case BAD_MESSAGE:
                buffer.skipBytes(actualReadableBytes());
                return;
            default:
                throw new Error();
        }
    }

    private MqttMessage invalidMessage(Throwable cause) {
        checkpoint(DecoderState.BAD_MESSAGE);
        return MqttMessageFactory.newInvalidMessage(this.mqttFixedHeader, this.variableHeader, cause);
    }

    private static MqttFixedHeader decodeFixedHeader(ChannelHandlerContext ctx, ByteBuf buffer) {
        short digit;
        int remainingLength;
        int loops;
        short b1 = buffer.readUnsignedByte();
        MqttMessageType messageType = MqttMessageType.valueOf(b1 >> 4);
        boolean dupFlag = (b1 & 8) == 8;
        int qosLevel = (b1 & 6) >> 1;
        boolean retain = (b1 & 1) != 0;
        switch (messageType) {
            case PUBLISH:
                if (qosLevel == 3) {
                    throw new DecoderException("Illegal QOS Level in fixed header of PUBLISH message (" + qosLevel + ')');
                }
                break;
            case PUBREL:
            case SUBSCRIBE:
            case UNSUBSCRIBE:
                if (dupFlag) {
                    throw new DecoderException("Illegal BIT 3 in fixed header of " + messageType + " message, must be 0, found 1");
                }
                if (qosLevel != 1) {
                    throw new DecoderException("Illegal QOS Level in fixed header of " + messageType + " message, must be 1, found " + qosLevel);
                }
                if (retain) {
                    throw new DecoderException("Illegal BIT 0 in fixed header of " + messageType + " message, must be 0, found 1");
                }
                break;
            case AUTH:
            case CONNACK:
            case CONNECT:
            case DISCONNECT:
            case PINGREQ:
            case PINGRESP:
            case PUBACK:
            case PUBCOMP:
            case PUBREC:
            case SUBACK:
            case UNSUBACK:
                if (dupFlag) {
                    throw new DecoderException("Illegal BIT 3 in fixed header of " + messageType + " message, must be 0, found 1");
                }
                if (qosLevel != 0) {
                    throw new DecoderException("Illegal BIT 2 or 1 in fixed header of " + messageType + " message, must be 0, found " + qosLevel);
                }
                if (retain) {
                    throw new DecoderException("Illegal BIT 0 in fixed header of " + messageType + " message, must be 0, found 1");
                }
                break;
            default:
                throw new DecoderException("Unknown message type, do not know how to validate fixed header");
        }
        int remainingLength2 = 0;
        int multiplier = 1;
        int loops2 = 0;
        while (true) {
            digit = buffer.readUnsignedByte();
            remainingLength = remainingLength2 + ((digit & 127) * multiplier);
            int multiplier2 = multiplier * 128;
            loops = loops2 + 1;
            if ((digit & RakConstants.ID_USER_PACKET_ENUM) != 0 && loops < 4) {
                remainingLength2 = remainingLength;
                multiplier = multiplier2;
                loops2 = loops;
            }
        }
        if (loops == 4 && (digit & RakConstants.ID_USER_PACKET_ENUM) != 0) {
            throw new DecoderException("remaining length exceeds 4 digits (" + messageType + ')');
        }
        MqttFixedHeader decodedFixedHeader = new MqttFixedHeader(messageType, dupFlag, MqttQoS.valueOf(qosLevel), retain, remainingLength);
        return MqttCodecUtil.validateFixedHeader(ctx, MqttCodecUtil.resetUnusedFields(decodedFixedHeader));
    }

    private Result<?> decodeVariableHeader(ChannelHandlerContext ctx, ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
        switch (mqttFixedHeader.messageType()) {
            case PUBLISH:
                return decodePublishVariableHeader(ctx, buffer, mqttFixedHeader);
            case PUBREL:
            case PUBACK:
            case PUBCOMP:
            case PUBREC:
                return decodePubReplyMessage(buffer);
            case SUBSCRIBE:
            case UNSUBSCRIBE:
            case SUBACK:
            case UNSUBACK:
                return decodeMessageIdAndPropertiesVariableHeader(ctx, buffer);
            case AUTH:
            case DISCONNECT:
                return decodeReasonCodeAndPropertiesVariableHeader(buffer);
            case CONNACK:
                return decodeConnAckVariableHeader(ctx, buffer);
            case CONNECT:
                return decodeConnectionVariableHeader(ctx, buffer);
            case PINGREQ:
            case PINGRESP:
                return new Result<>(null, 0);
            default:
                throw new DecoderException("Unknown message type: " + mqttFixedHeader.messageType());
        }
    }

    private static Result<MqttConnectVariableHeader> decodeConnectionVariableHeader(ChannelHandlerContext ctx, ByteBuf buffer) {
        MqttProperties properties;
        Result<String> protoString = decodeString(buffer);
        int numberOfBytesConsumed = ((Result) protoString).numberOfBytesConsumed;
        byte protocolLevel = buffer.readByte();
        MqttVersion version = MqttVersion.fromProtocolNameAndLevel((String) ((Result) protoString).value, protocolLevel);
        MqttCodecUtil.setMqttVersion(ctx, version);
        int b1 = buffer.readUnsignedByte();
        int keepAlive = decodeMsbLsb(buffer);
        int numberOfBytesConsumed2 = numberOfBytesConsumed + 1 + 1 + 2;
        boolean hasUserName = (b1 & 128) == 128;
        boolean hasPassword = (b1 & 64) == 64;
        boolean willRetain = (b1 & 32) == 32;
        int willQos = (b1 & 24) >> 3;
        boolean willFlag = (b1 & 4) == 4;
        boolean cleanSession = (b1 & 2) == 2;
        if (version == MqttVersion.MQTT_3_1_1 || version == MqttVersion.MQTT_5) {
            boolean zeroReservedFlag = (b1 & 1) == 0;
            if (!zeroReservedFlag) {
                throw new DecoderException("non-zero reserved flag");
            }
        }
        if (version == MqttVersion.MQTT_5) {
            Result<MqttProperties> propertiesResult = decodeProperties(buffer);
            MqttProperties properties2 = (MqttProperties) ((Result) propertiesResult).value;
            numberOfBytesConsumed2 += ((Result) propertiesResult).numberOfBytesConsumed;
            properties = properties2;
        } else {
            MqttProperties properties3 = MqttProperties.NO_PROPERTIES;
            properties = properties3;
        }
        MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(version.protocolName(), version.protocolLevel(), hasUserName, hasPassword, willRetain, willQos, willFlag, cleanSession, keepAlive, properties);
        return new Result<>(mqttConnectVariableHeader, numberOfBytesConsumed2);
    }

    private static Result<MqttConnAckVariableHeader> decodeConnAckVariableHeader(ChannelHandlerContext ctx, ByteBuf buffer) {
        MqttProperties properties;
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        boolean sessionPresent = (buffer.readUnsignedByte() & 1) == 1;
        byte returnCode = buffer.readByte();
        int numberOfBytesConsumed = 2;
        if (mqttVersion == MqttVersion.MQTT_5) {
            Result<MqttProperties> propertiesResult = decodeProperties(buffer);
            properties = (MqttProperties) ((Result) propertiesResult).value;
            numberOfBytesConsumed = 2 + ((Result) propertiesResult).numberOfBytesConsumed;
        } else {
            properties = MqttProperties.NO_PROPERTIES;
        }
        MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.valueOf(returnCode), sessionPresent, properties);
        return new Result<>(mqttConnAckVariableHeader, numberOfBytesConsumed);
    }

    private static Result<MqttMessageIdAndPropertiesVariableHeader> decodeMessageIdAndPropertiesVariableHeader(ChannelHandlerContext ctx, ByteBuf buffer) {
        MqttMessageIdAndPropertiesVariableHeader mqttVariableHeader;
        int mqtt5Consumed;
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        int packetId = decodeMessageId(buffer);
        if (mqttVersion == MqttVersion.MQTT_5) {
            Result<MqttProperties> properties = decodeProperties(buffer);
            mqttVariableHeader = new MqttMessageIdAndPropertiesVariableHeader(packetId, (MqttProperties) ((Result) properties).value);
            mqtt5Consumed = ((Result) properties).numberOfBytesConsumed;
        } else {
            mqttVariableHeader = new MqttMessageIdAndPropertiesVariableHeader(packetId, MqttProperties.NO_PROPERTIES);
            mqtt5Consumed = 0;
        }
        return new Result<>(mqttVariableHeader, mqtt5Consumed + 2);
    }

    private Result<MqttPubReplyMessageVariableHeader> decodePubReplyMessage(ByteBuf buffer) {
        MqttPubReplyMessageVariableHeader mqttPubAckVariableHeader;
        int consumed;
        int packetId = decodeMessageId(buffer);
        if (this.bytesRemainingInVariablePart > 3) {
            byte reasonCode = buffer.readByte();
            Result<MqttProperties> properties = decodeProperties(buffer);
            mqttPubAckVariableHeader = new MqttPubReplyMessageVariableHeader(packetId, reasonCode, (MqttProperties) ((Result) properties).value);
            consumed = ((Result) properties).numberOfBytesConsumed + 3;
        } else if (this.bytesRemainingInVariablePart > 2) {
            byte reasonCode2 = buffer.readByte();
            mqttPubAckVariableHeader = new MqttPubReplyMessageVariableHeader(packetId, reasonCode2, MqttProperties.NO_PROPERTIES);
            consumed = 3;
        } else {
            mqttPubAckVariableHeader = new MqttPubReplyMessageVariableHeader(packetId, (byte) 0, MqttProperties.NO_PROPERTIES);
            consumed = 2;
        }
        return new Result<>(mqttPubAckVariableHeader, consumed);
    }

    private Result<MqttReasonCodeAndPropertiesVariableHeader> decodeReasonCodeAndPropertiesVariableHeader(ByteBuf buffer) {
        byte reasonCode;
        MqttProperties properties;
        int consumed;
        if (this.bytesRemainingInVariablePart > 1) {
            reasonCode = buffer.readByte();
            Result<MqttProperties> propertiesResult = decodeProperties(buffer);
            properties = (MqttProperties) ((Result) propertiesResult).value;
            consumed = ((Result) propertiesResult).numberOfBytesConsumed + 1;
        } else if (this.bytesRemainingInVariablePart > 0) {
            reasonCode = buffer.readByte();
            properties = MqttProperties.NO_PROPERTIES;
            consumed = 1;
        } else {
            reasonCode = 0;
            properties = MqttProperties.NO_PROPERTIES;
            consumed = 0;
        }
        MqttReasonCodeAndPropertiesVariableHeader mqttReasonAndPropsVariableHeader = new MqttReasonCodeAndPropertiesVariableHeader(reasonCode, properties);
        return new Result<>(mqttReasonAndPropsVariableHeader, consumed);
    }

    private Result<MqttPublishVariableHeader> decodePublishVariableHeader(ChannelHandlerContext ctx, ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
        MqttProperties properties;
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        Result<String> decodedTopic = decodeString(buffer);
        if (!MqttCodecUtil.isValidPublishTopicName((String) ((Result) decodedTopic).value)) {
            throw new DecoderException("invalid publish topic name: " + ((String) ((Result) decodedTopic).value) + " (contains wildcards)");
        }
        int numberOfBytesConsumed = ((Result) decodedTopic).numberOfBytesConsumed;
        int messageId = -1;
        if (mqttFixedHeader.qosLevel().value() > 0) {
            messageId = decodeMessageId(buffer);
            numberOfBytesConsumed += 2;
        }
        if (mqttVersion == MqttVersion.MQTT_5) {
            Result<MqttProperties> propertiesResult = decodeProperties(buffer);
            properties = (MqttProperties) ((Result) propertiesResult).value;
            numberOfBytesConsumed += ((Result) propertiesResult).numberOfBytesConsumed;
        } else {
            properties = MqttProperties.NO_PROPERTIES;
        }
        MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader((String) ((Result) decodedTopic).value, messageId, properties);
        return new Result<>(mqttPublishVariableHeader, numberOfBytesConsumed);
    }

    private static int decodeMessageId(ByteBuf buffer) {
        int messageId = decodeMsbLsb(buffer);
        if (!MqttCodecUtil.isValidMessageId(messageId)) {
            throw new DecoderException("invalid messageId: " + messageId);
        }
        return messageId;
    }

    private static Result<?> decodePayload(ChannelHandlerContext ctx, ByteBuf buffer, MqttMessageType messageType, int bytesRemainingInVariablePart, int maxClientIdLength, Object variableHeader) {
        switch (messageType) {
            case PUBLISH:
                return decodePublishPayload(buffer, bytesRemainingInVariablePart);
            case SUBSCRIBE:
                return decodeSubscribePayload(buffer, bytesRemainingInVariablePart);
            case UNSUBSCRIBE:
                return decodeUnsubscribePayload(buffer, bytesRemainingInVariablePart);
            case CONNECT:
                return decodeConnectionPayload(buffer, maxClientIdLength, (MqttConnectVariableHeader) variableHeader);
            case SUBACK:
                return decodeSubackPayload(buffer, bytesRemainingInVariablePart);
            case UNSUBACK:
                return decodeUnsubAckPayload(ctx, buffer, bytesRemainingInVariablePart);
            default:
                return new Result<>(null, 0);
        }
    }

    private static Result<MqttConnectPayload> decodeConnectionPayload(ByteBuf buffer, int maxClientIdLength, MqttConnectVariableHeader mqttConnectVariableHeader) {
        MqttProperties willProperties;
        Result<String> decodedClientId = decodeString(buffer);
        String decodedClientIdValue = (String) ((Result) decodedClientId).value;
        MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(mqttConnectVariableHeader.name(), (byte) mqttConnectVariableHeader.version());
        if (!MqttCodecUtil.isValidClientId(mqttVersion, maxClientIdLength, decodedClientIdValue)) {
            throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + decodedClientIdValue);
        }
        int numberOfBytesConsumed = ((Result) decodedClientId).numberOfBytesConsumed;
        Result<String> decodedWillTopic = null;
        byte[] decodedWillMessage = null;
        if (mqttConnectVariableHeader.isWillFlag()) {
            if (mqttVersion == MqttVersion.MQTT_5) {
                Result<MqttProperties> propertiesResult = decodeProperties(buffer);
                willProperties = (MqttProperties) ((Result) propertiesResult).value;
                numberOfBytesConsumed += ((Result) propertiesResult).numberOfBytesConsumed;
            } else {
                willProperties = MqttProperties.NO_PROPERTIES;
            }
            decodedWillTopic = decodeString(buffer, 0, 32767);
            int numberOfBytesConsumed2 = numberOfBytesConsumed + ((Result) decodedWillTopic).numberOfBytesConsumed;
            decodedWillMessage = decodeByteArray(buffer);
            numberOfBytesConsumed = numberOfBytesConsumed2 + decodedWillMessage.length + 2;
        } else {
            willProperties = MqttProperties.NO_PROPERTIES;
        }
        Result<String> decodedUserName = null;
        byte[] decodedPassword = null;
        if (mqttConnectVariableHeader.hasUserName()) {
            decodedUserName = decodeString(buffer);
            numberOfBytesConsumed += ((Result) decodedUserName).numberOfBytesConsumed;
        }
        if (mqttConnectVariableHeader.hasPassword()) {
            decodedPassword = decodeByteArray(buffer);
            numberOfBytesConsumed += decodedPassword.length + 2;
        }
        MqttConnectPayload mqttConnectPayload = new MqttConnectPayload((String) ((Result) decodedClientId).value, willProperties, decodedWillTopic != null ? (String) ((Result) decodedWillTopic).value : null, decodedWillMessage, decodedUserName != null ? (String) ((Result) decodedUserName).value : null, decodedPassword);
        return new Result<>(mqttConnectPayload, numberOfBytesConsumed);
    }

    private static Result<MqttSubscribePayload> decodeSubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        List<MqttTopicSubscription> subscribeTopics = new ArrayList<>();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            Result<String> decodedTopicName = decodeString(buffer);
            int numberOfBytesConsumed2 = numberOfBytesConsumed + ((Result) decodedTopicName).numberOfBytesConsumed;
            short optionByte = buffer.readUnsignedByte();
            MqttQoS qos = MqttQoS.valueOf(optionByte & 3);
            boolean retainAsPublished = false;
            boolean noLocal = ((optionByte & 4) >> 2) == 1;
            if (((optionByte & 8) >> 3) == 1) {
                retainAsPublished = true;
            }
            MqttSubscriptionOption.RetainedHandlingPolicy retainHandling = MqttSubscriptionOption.RetainedHandlingPolicy.valueOf((optionByte & 48) >> 4);
            MqttSubscriptionOption subscriptionOption = new MqttSubscriptionOption(qos, noLocal, retainAsPublished, retainHandling);
            numberOfBytesConsumed = numberOfBytesConsumed2 + 1;
            subscribeTopics.add(new MqttTopicSubscription((String) ((Result) decodedTopicName).value, subscriptionOption));
        }
        return new Result<>(new MqttSubscribePayload(subscribeTopics), numberOfBytesConsumed);
    }

    private static Result<MqttSubAckPayload> decodeSubackPayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        List<Integer> grantedQos = new ArrayList<>(bytesRemainingInVariablePart);
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            int reasonCode = buffer.readUnsignedByte();
            numberOfBytesConsumed++;
            grantedQos.add(Integer.valueOf(reasonCode));
        }
        return new Result<>(new MqttSubAckPayload(grantedQos), numberOfBytesConsumed);
    }

    private static Result<MqttUnsubAckPayload> decodeUnsubAckPayload(ChannelHandlerContext ctx, ByteBuf buffer, int bytesRemainingInVariablePart) {
        List<Short> reasonCodes = new ArrayList<>(bytesRemainingInVariablePart);
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            short reasonCode = buffer.readUnsignedByte();
            numberOfBytesConsumed++;
            reasonCodes.add(Short.valueOf(reasonCode));
        }
        return new Result<>(new MqttUnsubAckPayload(reasonCodes), numberOfBytesConsumed);
    }

    private static Result<MqttUnsubscribePayload> decodeUnsubscribePayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        ArrayList arrayList = new ArrayList();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            Result<String> decodedTopicName = decodeString(buffer);
            numberOfBytesConsumed += ((Result) decodedTopicName).numberOfBytesConsumed;
            arrayList.add(((Result) decodedTopicName).value);
        }
        return new Result<>(new MqttUnsubscribePayload(arrayList), numberOfBytesConsumed);
    }

    private static Result<ByteBuf> decodePublishPayload(ByteBuf buffer, int bytesRemainingInVariablePart) {
        ByteBuf b = buffer.readRetainedSlice(bytesRemainingInVariablePart);
        return new Result<>(b, bytesRemainingInVariablePart);
    }

    private static Result<String> decodeString(ByteBuf buffer) {
        return decodeString(buffer, 0, Integer.MAX_VALUE);
    }

    private static Result<String> decodeString(ByteBuf buffer, int minBytes, int maxBytes) {
        int size = decodeMsbLsb(buffer);
        if (size < minBytes || size > maxBytes) {
            buffer.skipBytes(size);
            int numberOfBytesConsumed = 2 + size;
            return new Result<>(null, numberOfBytesConsumed);
        }
        String s = buffer.toString(buffer.readerIndex(), size, CharsetUtil.UTF_8);
        buffer.skipBytes(size);
        int numberOfBytesConsumed2 = 2 + size;
        return new Result<>(s, numberOfBytesConsumed2);
    }

    private static byte[] decodeByteArray(ByteBuf buffer) {
        int size = decodeMsbLsb(buffer);
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return bytes;
    }

    private static long packInts(int a, int b) {
        return (a << 32) | (b & 4294967295L);
    }

    private static int unpackA(long ints) {
        return (int) (ints >> 32);
    }

    private static int unpackB(long ints) {
        return (int) ints;
    }

    private static int decodeMsbLsb(ByteBuf buffer) {
        short msbSize = buffer.readUnsignedByte();
        short lsbSize = buffer.readUnsignedByte();
        int result = (msbSize << 8) | lsbSize;
        if (result < 0 || result > 65535) {
            return -1;
        }
        return result;
    }

    private static long decodeVariableByteInteger(ByteBuf buffer) {
        short digit;
        int remainingLength = 0;
        int multiplier = 1;
        int loops = 0;
        do {
            digit = buffer.readUnsignedByte();
            remainingLength += (digit & 127) * multiplier;
            multiplier *= 128;
            loops++;
            if ((digit & RakConstants.ID_USER_PACKET_ENUM) == 0) {
                break;
            }
        } while (loops < 4);
        if (loops == 4 && (digit & RakConstants.ID_USER_PACKET_ENUM) != 0) {
            throw new DecoderException("MQTT protocol limits Remaining Length to 4 bytes");
        }
        return packInts(remainingLength, loops);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Result<T> {
        private final int numberOfBytesConsumed;
        private final T value;

        Result(T value, int numberOfBytesConsumed) {
            this.value = value;
            this.numberOfBytesConsumed = numberOfBytesConsumed;
        }
    }

    private static Result<MqttProperties> decodeProperties(ByteBuf buffer) {
        long propertiesLength = decodeVariableByteInteger(buffer);
        int totalPropertiesLength = unpackA(propertiesLength);
        int numberOfBytesConsumed = unpackB(propertiesLength);
        MqttProperties decodedProperties = new MqttProperties();
        while (numberOfBytesConsumed < totalPropertiesLength) {
            long propertyId = decodeVariableByteInteger(buffer);
            int propertyIdValue = unpackA(propertyId);
            int numberOfBytesConsumed2 = numberOfBytesConsumed + unpackB(propertyId);
            MqttProperties.MqttPropertyType propertyType = MqttProperties.MqttPropertyType.valueOf(propertyIdValue);
            switch (propertyType) {
                case PAYLOAD_FORMAT_INDICATOR:
                case REQUEST_PROBLEM_INFORMATION:
                case REQUEST_RESPONSE_INFORMATION:
                case MAXIMUM_QOS:
                case RETAIN_AVAILABLE:
                case WILDCARD_SUBSCRIPTION_AVAILABLE:
                case SUBSCRIPTION_IDENTIFIER_AVAILABLE:
                case SHARED_SUBSCRIPTION_AVAILABLE:
                    int b1 = buffer.readUnsignedByte();
                    numberOfBytesConsumed = numberOfBytesConsumed2 + 1;
                    decodedProperties.add(new MqttProperties.IntegerProperty(propertyIdValue, Integer.valueOf(b1)));
                    break;
                case SERVER_KEEP_ALIVE:
                case RECEIVE_MAXIMUM:
                case TOPIC_ALIAS_MAXIMUM:
                case TOPIC_ALIAS:
                    int int2BytesResult = decodeMsbLsb(buffer);
                    numberOfBytesConsumed = numberOfBytesConsumed2 + 2;
                    decodedProperties.add(new MqttProperties.IntegerProperty(propertyIdValue, Integer.valueOf(int2BytesResult)));
                    break;
                case PUBLICATION_EXPIRY_INTERVAL:
                case SESSION_EXPIRY_INTERVAL:
                case WILL_DELAY_INTERVAL:
                case MAXIMUM_PACKET_SIZE:
                    int maxPacketSize = buffer.readInt();
                    numberOfBytesConsumed = numberOfBytesConsumed2 + 4;
                    decodedProperties.add(new MqttProperties.IntegerProperty(propertyIdValue, Integer.valueOf(maxPacketSize)));
                    break;
                case SUBSCRIPTION_IDENTIFIER:
                    long vbIntegerResult = decodeVariableByteInteger(buffer);
                    numberOfBytesConsumed = numberOfBytesConsumed2 + unpackB(vbIntegerResult);
                    decodedProperties.add(new MqttProperties.IntegerProperty(propertyIdValue, Integer.valueOf(unpackA(vbIntegerResult))));
                    break;
                case CONTENT_TYPE:
                case RESPONSE_TOPIC:
                case ASSIGNED_CLIENT_IDENTIFIER:
                case AUTHENTICATION_METHOD:
                case RESPONSE_INFORMATION:
                case SERVER_REFERENCE:
                case REASON_STRING:
                    Result<String> stringResult = decodeString(buffer);
                    numberOfBytesConsumed = numberOfBytesConsumed2 + ((Result) stringResult).numberOfBytesConsumed;
                    decodedProperties.add(new MqttProperties.StringProperty(propertyIdValue, (String) ((Result) stringResult).value));
                    break;
                case USER_PROPERTY:
                    Result<String> keyResult = decodeString(buffer);
                    Result<String> valueResult = decodeString(buffer);
                    numberOfBytesConsumed = numberOfBytesConsumed2 + ((Result) keyResult).numberOfBytesConsumed + ((Result) valueResult).numberOfBytesConsumed;
                    decodedProperties.add(new MqttProperties.UserProperty((String) ((Result) keyResult).value, (String) ((Result) valueResult).value));
                    break;
                case CORRELATION_DATA:
                case AUTHENTICATION_DATA:
                    byte[] binaryDataResult = decodeByteArray(buffer);
                    numberOfBytesConsumed = numberOfBytesConsumed2 + binaryDataResult.length + 2;
                    decodedProperties.add(new MqttProperties.BinaryProperty(propertyIdValue, binaryDataResult));
                    break;
                default:
                    throw new DecoderException("Unknown property type: " + propertyType);
            }
        }
        return new Result<>(decodedProperties, numberOfBytesConsumed);
    }
}
