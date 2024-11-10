package io.netty.handler.codec.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.util.internal.EmptyArrays;
import java.util.Iterator;
import java.util.List;

@ChannelHandler.Sharable
/* loaded from: classes4.dex */
public final class MqttEncoder extends MessageToMessageEncoder<MqttMessage> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final MqttEncoder INSTANCE = new MqttEncoder();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage, List list) throws Exception {
        encode2(channelHandlerContext, mqttMessage, (List<Object>) list);
    }

    private MqttEncoder() {
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, MqttMessage msg, List<Object> out) throws Exception {
        out.add(doEncode(ctx, msg));
    }

    static ByteBuf doEncode(ChannelHandlerContext ctx, MqttMessage message) {
        switch (message.fixedHeader().messageType()) {
            case CONNECT:
                return encodeConnectMessage(ctx, (MqttConnectMessage) message);
            case CONNACK:
                return encodeConnAckMessage(ctx, (MqttConnAckMessage) message);
            case PUBLISH:
                return encodePublishMessage(ctx, (MqttPublishMessage) message);
            case SUBSCRIBE:
                return encodeSubscribeMessage(ctx, (MqttSubscribeMessage) message);
            case UNSUBSCRIBE:
                return encodeUnsubscribeMessage(ctx, (MqttUnsubscribeMessage) message);
            case SUBACK:
                return encodeSubAckMessage(ctx, (MqttSubAckMessage) message);
            case UNSUBACK:
                if (message instanceof MqttUnsubAckMessage) {
                    return encodeUnsubAckMessage(ctx, (MqttUnsubAckMessage) message);
                }
                return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ctx.alloc(), message);
            case PUBACK:
            case PUBREC:
            case PUBREL:
            case PUBCOMP:
                return encodePubReplyMessage(ctx, message);
            case DISCONNECT:
            case AUTH:
                return encodeReasonCodePlusPropertiesMessage(ctx, message);
            case PINGREQ:
            case PINGRESP:
                return encodeMessageWithOnlySingleByteFixedHeader(ctx.alloc(), message);
            default:
                throw new IllegalArgumentException("Unknown message type: " + message.fixedHeader().messageType().value());
        }
    }

    private static ByteBuf encodeConnectMessage(ChannelHandlerContext ctx, MqttConnectMessage message) {
        byte[] passwordBytes;
        int payloadBufferSize;
        ByteBuf willPropertiesBuf;
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttConnectVariableHeader variableHeader = message.variableHeader();
        MqttConnectPayload payload = message.payload();
        MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel(variableHeader.name(), (byte) variableHeader.version());
        MqttCodecUtil.setMqttVersion(ctx, mqttVersion);
        if (!variableHeader.hasUserName() && variableHeader.hasPassword()) {
            throw new EncoderException("Without a username, the password MUST be not set");
        }
        String clientIdentifier = payload.clientIdentifier();
        if (!MqttCodecUtil.isValidClientId(mqttVersion, 23, clientIdentifier)) {
            throw new MqttIdentifierRejectedException("invalid clientIdentifier: " + clientIdentifier);
        }
        int clientIdentifierBytes = ByteBufUtil.utf8Bytes(clientIdentifier);
        int payloadBufferSize2 = 0 + clientIdentifierBytes + 2;
        String willTopic = payload.willTopic();
        int willTopicBytes = nullableUtf8Bytes(willTopic);
        byte[] willMessage = payload.willMessageInBytes();
        byte[] willMessageBytes = willMessage != null ? willMessage : EmptyArrays.EMPTY_BYTES;
        if (variableHeader.isWillFlag()) {
            payloadBufferSize2 = payloadBufferSize2 + willTopicBytes + 2 + willMessageBytes.length + 2;
        }
        String userName = payload.userName();
        int userNameBytes = nullableUtf8Bytes(userName);
        if (variableHeader.hasUserName()) {
            payloadBufferSize2 += userNameBytes + 2;
        }
        byte[] password = payload.passwordInBytes();
        byte[] passwordBytes2 = password != null ? password : EmptyArrays.EMPTY_BYTES;
        int payloadBufferSize3 = variableHeader.hasPassword() ? payloadBufferSize2 + passwordBytes2.length + 2 : payloadBufferSize2;
        byte[] protocolNameBytes = mqttVersion.protocolNameBytes();
        ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.variableHeader().properties());
        try {
            if (variableHeader.isWillFlag()) {
                try {
                    passwordBytes = passwordBytes2;
                    try {
                        ByteBuf willPropertiesBuf2 = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), payload.willProperties());
                        payloadBufferSize = payloadBufferSize3 + willPropertiesBuf2.readableBytes();
                        willPropertiesBuf = willPropertiesBuf2;
                    } catch (Throwable th) {
                        th = th;
                        propertiesBuf.release();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } else {
                passwordBytes = passwordBytes2;
                try {
                    payloadBufferSize = payloadBufferSize3;
                    willPropertiesBuf = Unpooled.EMPTY_BUFFER;
                } catch (Throwable th3) {
                    th = th3;
                    propertiesBuf.release();
                    throw th;
                }
            }
            try {
                try {
                    int variableHeaderBufferSize = protocolNameBytes.length + 2 + 4 + propertiesBuf.readableBytes();
                    int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
                    try {
                        int fixedHeaderBufferSize = getVariableLengthInt(variablePartSize) + 1;
                        try {
                            ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
                            buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
                            writeVariableLengthInt(buf, variablePartSize);
                            buf.writeShort(protocolNameBytes.length);
                            buf.writeBytes(protocolNameBytes);
                            buf.writeByte(variableHeader.version());
                            buf.writeByte(getConnVariableHeaderFlag(variableHeader));
                            buf.writeShort(variableHeader.keepAliveTimeSeconds());
                            buf.writeBytes(propertiesBuf);
                            writeExactUTF8String(buf, clientIdentifier, clientIdentifierBytes);
                            if (variableHeader.isWillFlag()) {
                                try {
                                    buf.writeBytes(willPropertiesBuf);
                                    writeExactUTF8String(buf, willTopic, willTopicBytes);
                                    buf.writeShort(willMessageBytes.length);
                                    buf.writeBytes(willMessageBytes, 0, willMessageBytes.length);
                                } catch (Throwable th4) {
                                    th = th4;
                                    willPropertiesBuf.release();
                                    throw th;
                                }
                            }
                            try {
                                if (variableHeader.hasUserName()) {
                                    writeExactUTF8String(buf, userName, userNameBytes);
                                }
                                if (variableHeader.hasPassword()) {
                                    byte[] passwordBytes3 = passwordBytes;
                                    try {
                                        buf.writeShort(passwordBytes3.length);
                                        try {
                                            buf.writeBytes(passwordBytes3, 0, passwordBytes3.length);
                                        } catch (Throwable th5) {
                                            th = th5;
                                            willPropertiesBuf.release();
                                            throw th;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                    }
                                }
                                willPropertiesBuf.release();
                                propertiesBuf.release();
                                return buf;
                            } catch (Throwable th7) {
                                th = th7;
                            }
                        } catch (Throwable th8) {
                            th = th8;
                        }
                    } catch (Throwable th9) {
                        th = th9;
                    }
                } catch (Throwable th10) {
                    th = th10;
                }
            } catch (Throwable th11) {
                th = th11;
                propertiesBuf.release();
                throw th;
            }
        } catch (Throwable th12) {
            th = th12;
        }
    }

    private static int getConnVariableHeaderFlag(MqttConnectVariableHeader variableHeader) {
        int flagByte = 0;
        if (variableHeader.hasUserName()) {
            flagByte = 0 | 128;
        }
        if (variableHeader.hasPassword()) {
            flagByte |= 64;
        }
        if (variableHeader.isWillRetain()) {
            flagByte |= 32;
        }
        int flagByte2 = flagByte | ((variableHeader.willQos() & 3) << 3);
        if (variableHeader.isWillFlag()) {
            flagByte2 |= 4;
        }
        if (variableHeader.isCleanSession()) {
            return flagByte2 | 2;
        }
        return flagByte2;
    }

    private static ByteBuf encodeConnAckMessage(ChannelHandlerContext ctx, MqttConnAckMessage message) {
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.variableHeader().properties());
        try {
            ByteBuf buf = ctx.alloc().buffer(propertiesBuf.readableBytes() + 4);
            buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
            writeVariableLengthInt(buf, propertiesBuf.readableBytes() + 2);
            buf.writeByte(message.variableHeader().isSessionPresent() ? 1 : 0);
            buf.writeByte(message.variableHeader().connectReturnCode().byteValue());
            buf.writeBytes(propertiesBuf);
            return buf;
        } finally {
            propertiesBuf.release();
        }
    }

    private static ByteBuf encodeSubscribeMessage(ChannelHandlerContext ctx, MqttSubscribeMessage message) {
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.idAndPropertiesVariableHeader().properties());
        try {
            int variableHeaderBufferSize = propertiesBuf.readableBytes() + 2;
            int payloadBufferSize = 0;
            MqttFixedHeader mqttFixedHeader = message.fixedHeader();
            MqttMessageIdVariableHeader variableHeader = message.variableHeader();
            MqttSubscribePayload payload = message.payload();
            Iterator<MqttTopicSubscription> it2 = payload.topicSubscriptions().iterator();
            while (it2.hasNext()) {
                String topicName = it2.next().topicName();
                int topicNameBytes = ByteBufUtil.utf8Bytes(topicName);
                payloadBufferSize = payloadBufferSize + topicNameBytes + 2 + 1;
            }
            int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
            int fixedHeaderBufferSize = getVariableLengthInt(variablePartSize) + 1;
            ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
            buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
            writeVariableLengthInt(buf, variablePartSize);
            int messageId = variableHeader.messageId();
            buf.writeShort(messageId);
            buf.writeBytes(propertiesBuf);
            for (MqttTopicSubscription topic : payload.topicSubscriptions()) {
                writeUnsafeUTF8String(buf, topic.topicName());
                if (mqttVersion != MqttVersion.MQTT_3_1_1 && mqttVersion != MqttVersion.MQTT_3_1) {
                    MqttSubscriptionOption option = topic.option();
                    int optionEncoded = option.retainHandling().value() << 4;
                    if (option.isRetainAsPublished()) {
                        optionEncoded |= 8;
                    }
                    if (option.isNoLocal()) {
                        optionEncoded |= 4;
                    }
                    buf.writeByte(optionEncoded | option.qos().value());
                }
                buf.writeByte(topic.qualityOfService().value());
            }
            return buf;
        } finally {
            propertiesBuf.release();
        }
    }

    private static ByteBuf encodeUnsubscribeMessage(ChannelHandlerContext ctx, MqttUnsubscribeMessage message) {
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.idAndPropertiesVariableHeader().properties());
        try {
            int variableHeaderBufferSize = propertiesBuf.readableBytes() + 2;
            int payloadBufferSize = 0;
            MqttFixedHeader mqttFixedHeader = message.fixedHeader();
            MqttMessageIdVariableHeader variableHeader = message.variableHeader();
            MqttUnsubscribePayload payload = message.payload();
            for (String topicName : payload.topics()) {
                int topicNameBytes = ByteBufUtil.utf8Bytes(topicName);
                payloadBufferSize += topicNameBytes + 2;
            }
            int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
            int fixedHeaderBufferSize = getVariableLengthInt(variablePartSize) + 1;
            ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
            buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
            writeVariableLengthInt(buf, variablePartSize);
            int messageId = variableHeader.messageId();
            buf.writeShort(messageId);
            buf.writeBytes(propertiesBuf);
            for (String topicName2 : payload.topics()) {
                writeUnsafeUTF8String(buf, topicName2);
            }
            return buf;
        } finally {
            propertiesBuf.release();
        }
    }

    private static ByteBuf encodeSubAckMessage(ChannelHandlerContext ctx, MqttSubAckMessage message) {
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.idAndPropertiesVariableHeader().properties());
        try {
            int variableHeaderBufferSize = propertiesBuf.readableBytes() + 2;
            int payloadBufferSize = message.payload().grantedQoSLevels().size();
            int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
            int fixedHeaderBufferSize = getVariableLengthInt(variablePartSize) + 1;
            ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
            buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
            writeVariableLengthInt(buf, variablePartSize);
            buf.writeShort(message.variableHeader().messageId());
            buf.writeBytes(propertiesBuf);
            Iterator<Integer> it2 = message.payload().reasonCodes().iterator();
            while (it2.hasNext()) {
                int code = it2.next().intValue();
                buf.writeByte(code);
            }
            return buf;
        } finally {
            propertiesBuf.release();
        }
    }

    private static ByteBuf encodeUnsubAckMessage(ChannelHandlerContext ctx, MqttUnsubAckMessage message) {
        if (message.variableHeader() instanceof MqttMessageIdAndPropertiesVariableHeader) {
            MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
            ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.idAndPropertiesVariableHeader().properties());
            try {
                int variableHeaderBufferSize = propertiesBuf.readableBytes() + 2;
                MqttUnsubAckPayload payload = message.payload();
                int payloadBufferSize = payload == null ? 0 : payload.unsubscribeReasonCodes().size();
                int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
                int fixedHeaderBufferSize = getVariableLengthInt(variablePartSize) + 1;
                ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
                buf.writeByte(getFixedHeaderByte1(message.fixedHeader()));
                writeVariableLengthInt(buf, variablePartSize);
                buf.writeShort(message.variableHeader().messageId());
                buf.writeBytes(propertiesBuf);
                if (payload != null) {
                    for (Short reasonCode : payload.unsubscribeReasonCodes()) {
                        buf.writeByte(reasonCode.shortValue());
                    }
                }
                return buf;
            } finally {
                propertiesBuf.release();
            }
        }
        return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ctx.alloc(), message);
    }

    private static ByteBuf encodePublishMessage(ChannelHandlerContext ctx, MqttPublishMessage message) {
        MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttPublishVariableHeader variableHeader = message.variableHeader();
        ByteBuf payload = message.payload().duplicate();
        String topicName = variableHeader.topicName();
        int topicNameBytes = ByteBufUtil.utf8Bytes(topicName);
        ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion, ctx.alloc(), message.variableHeader().properties());
        try {
            int variableHeaderBufferSize = topicNameBytes + 2 + (mqttFixedHeader.qosLevel().value() > 0 ? 2 : 0) + propertiesBuf.readableBytes();
            int payloadBufferSize = payload.readableBytes();
            int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
            int fixedHeaderBufferSize = getVariableLengthInt(variablePartSize) + 1;
            ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
            buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
            writeVariableLengthInt(buf, variablePartSize);
            writeExactUTF8String(buf, topicName, topicNameBytes);
            if (mqttFixedHeader.qosLevel().value() > 0) {
                buf.writeShort(variableHeader.packetId());
            }
            buf.writeBytes(propertiesBuf);
            buf.writeBytes(payload);
            return buf;
        } finally {
            propertiesBuf.release();
        }
    }

    private static ByteBuf encodePubReplyMessage(ChannelHandlerContext ctx, MqttMessage message) {
        ByteBuf propertiesBuf;
        boolean includeReasonCode;
        int variableHeaderBufferSize;
        if (message.variableHeader() instanceof MqttPubReplyMessageVariableHeader) {
            MqttFixedHeader mqttFixedHeader = message.fixedHeader();
            MqttPubReplyMessageVariableHeader variableHeader = (MqttPubReplyMessageVariableHeader) message.variableHeader();
            int msgId = variableHeader.messageId();
            MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
            if (mqttVersion == MqttVersion.MQTT_5 && (variableHeader.reasonCode() != 0 || !variableHeader.properties().isEmpty())) {
                propertiesBuf = encodeProperties(ctx.alloc(), variableHeader.properties());
                includeReasonCode = true;
                variableHeaderBufferSize = propertiesBuf.readableBytes() + 3;
            } else {
                propertiesBuf = Unpooled.EMPTY_BUFFER;
                includeReasonCode = false;
                variableHeaderBufferSize = 2;
            }
            try {
                int fixedHeaderBufferSize = getVariableLengthInt(variableHeaderBufferSize) + 1;
                ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variableHeaderBufferSize);
                buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
                writeVariableLengthInt(buf, variableHeaderBufferSize);
                buf.writeShort(msgId);
                if (includeReasonCode) {
                    buf.writeByte(variableHeader.reasonCode());
                }
                buf.writeBytes(propertiesBuf);
                return buf;
            } finally {
                propertiesBuf.release();
            }
        }
        return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ctx.alloc(), message);
    }

    private static ByteBuf encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ByteBufAllocator byteBufAllocator, MqttMessage message) {
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader) message.variableHeader();
        int msgId = variableHeader.messageId();
        int fixedHeaderBufferSize = getVariableLengthInt(2) + 1;
        ByteBuf buf = byteBufAllocator.buffer(fixedHeaderBufferSize + 2);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        writeVariableLengthInt(buf, 2);
        buf.writeShort(msgId);
        return buf;
    }

    private static ByteBuf encodeReasonCodePlusPropertiesMessage(ChannelHandlerContext ctx, MqttMessage message) {
        ByteBuf propertiesBuf;
        boolean includeReasonCode;
        int variableHeaderBufferSize;
        if (message.variableHeader() instanceof MqttReasonCodeAndPropertiesVariableHeader) {
            MqttVersion mqttVersion = MqttCodecUtil.getMqttVersion(ctx);
            MqttFixedHeader mqttFixedHeader = message.fixedHeader();
            MqttReasonCodeAndPropertiesVariableHeader variableHeader = (MqttReasonCodeAndPropertiesVariableHeader) message.variableHeader();
            if (mqttVersion == MqttVersion.MQTT_5 && (variableHeader.reasonCode() != 0 || !variableHeader.properties().isEmpty())) {
                propertiesBuf = encodeProperties(ctx.alloc(), variableHeader.properties());
                includeReasonCode = true;
                variableHeaderBufferSize = propertiesBuf.readableBytes() + 1;
            } else {
                propertiesBuf = Unpooled.EMPTY_BUFFER;
                includeReasonCode = false;
                variableHeaderBufferSize = 0;
            }
            try {
                int fixedHeaderBufferSize = getVariableLengthInt(variableHeaderBufferSize) + 1;
                ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variableHeaderBufferSize);
                buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
                writeVariableLengthInt(buf, variableHeaderBufferSize);
                if (includeReasonCode) {
                    buf.writeByte(variableHeader.reasonCode());
                }
                buf.writeBytes(propertiesBuf);
                return buf;
            } finally {
                propertiesBuf.release();
            }
        }
        return encodeMessageWithOnlySingleByteFixedHeader(ctx.alloc(), message);
    }

    private static ByteBuf encodeMessageWithOnlySingleByteFixedHeader(ByteBufAllocator byteBufAllocator, MqttMessage message) {
        MqttFixedHeader mqttFixedHeader = message.fixedHeader();
        ByteBuf buf = byteBufAllocator.buffer(2);
        buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
        buf.writeByte(0);
        return buf;
    }

    private static ByteBuf encodePropertiesIfNeeded(MqttVersion mqttVersion, ByteBufAllocator byteBufAllocator, MqttProperties mqttProperties) {
        if (mqttVersion == MqttVersion.MQTT_5) {
            return encodeProperties(byteBufAllocator, mqttProperties);
        }
        return Unpooled.EMPTY_BUFFER;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static ByteBuf encodeProperties(ByteBufAllocator byteBufAllocator, MqttProperties mqttProperties) {
        ByteBuf propertiesBuf = byteBufAllocator.buffer();
        try {
            propertiesBuf = byteBufAllocator.buffer();
            try {
                for (MqttProperties.MqttProperty property : mqttProperties.listAll()) {
                    MqttProperties.MqttPropertyType propertyType = MqttProperties.MqttPropertyType.valueOf(property.propertyId);
                    switch (propertyType) {
                        case PAYLOAD_FORMAT_INDICATOR:
                        case REQUEST_PROBLEM_INFORMATION:
                        case REQUEST_RESPONSE_INFORMATION:
                        case MAXIMUM_QOS:
                        case RETAIN_AVAILABLE:
                        case WILDCARD_SUBSCRIPTION_AVAILABLE:
                        case SUBSCRIPTION_IDENTIFIER_AVAILABLE:
                        case SHARED_SUBSCRIPTION_AVAILABLE:
                            writeVariableLengthInt(propertiesBuf, property.propertyId);
                            byte bytePropValue = ((Integer) ((MqttProperties.IntegerProperty) property).value).byteValue();
                            propertiesBuf.writeByte(bytePropValue);
                            break;
                        case SERVER_KEEP_ALIVE:
                        case RECEIVE_MAXIMUM:
                        case TOPIC_ALIAS_MAXIMUM:
                        case TOPIC_ALIAS:
                            int fourBytesIntPropValue = property.propertyId;
                            writeVariableLengthInt(propertiesBuf, fourBytesIntPropValue);
                            short twoBytesInPropValue = ((Integer) ((MqttProperties.IntegerProperty) property).value).shortValue();
                            propertiesBuf.writeShort(twoBytesInPropValue);
                            break;
                        case PUBLICATION_EXPIRY_INTERVAL:
                        case SESSION_EXPIRY_INTERVAL:
                        case WILL_DELAY_INTERVAL:
                        case MAXIMUM_PACKET_SIZE:
                            int vbi = property.propertyId;
                            writeVariableLengthInt(propertiesBuf, vbi);
                            int fourBytesIntPropValue2 = ((Integer) ((MqttProperties.IntegerProperty) property).value).intValue();
                            propertiesBuf.writeInt(fourBytesIntPropValue2);
                            break;
                        case SUBSCRIPTION_IDENTIFIER:
                            writeVariableLengthInt(propertiesBuf, property.propertyId);
                            int vbi2 = ((Integer) ((MqttProperties.IntegerProperty) property).value).intValue();
                            writeVariableLengthInt(propertiesBuf, vbi2);
                            break;
                        case CONTENT_TYPE:
                        case RESPONSE_TOPIC:
                        case ASSIGNED_CLIENT_IDENTIFIER:
                        case AUTHENTICATION_METHOD:
                        case RESPONSE_INFORMATION:
                        case SERVER_REFERENCE:
                        case REASON_STRING:
                            writeVariableLengthInt(propertiesBuf, property.propertyId);
                            writeEagerUTF8String(propertiesBuf, (String) ((MqttProperties.StringProperty) property).value);
                            break;
                        case USER_PROPERTY:
                            List<MqttProperties.StringPair> pairs = (List) ((MqttProperties.UserProperties) property).value;
                            for (MqttProperties.StringPair pair : pairs) {
                                writeVariableLengthInt(propertiesBuf, property.propertyId);
                                writeEagerUTF8String(propertiesBuf, pair.key);
                                writeEagerUTF8String(propertiesBuf, pair.value);
                            }
                            break;
                        case CORRELATION_DATA:
                        case AUTHENTICATION_DATA:
                            writeVariableLengthInt(propertiesBuf, property.propertyId);
                            byte[] binaryPropValue = (byte[]) ((MqttProperties.BinaryProperty) property).value;
                            propertiesBuf.writeShort(binaryPropValue.length);
                            propertiesBuf.writeBytes(binaryPropValue, 0, binaryPropValue.length);
                            break;
                        default:
                            throw new EncoderException("Unknown property type: " + propertyType);
                    }
                }
                writeVariableLengthInt(propertiesBuf, propertiesBuf.readableBytes());
                propertiesBuf.writeBytes(propertiesBuf);
                return propertiesBuf;
            } finally {
                propertiesBuf.release();
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private static int getFixedHeaderByte1(MqttFixedHeader header) {
        int ret = 0 | (header.messageType().value() << 4);
        if (header.isDup()) {
            ret |= 8;
        }
        int ret2 = ret | (header.qosLevel().value() << 1);
        if (header.isRetain()) {
            return ret2 | 1;
        }
        return ret2;
    }

    private static void writeVariableLengthInt(ByteBuf buf, int num) {
        do {
            int digit = num % 128;
            num /= 128;
            if (num > 0) {
                digit |= 128;
            }
            buf.writeByte(digit);
        } while (num > 0);
    }

    private static int nullableUtf8Bytes(String s) {
        if (s == null) {
            return 0;
        }
        return ByteBufUtil.utf8Bytes(s);
    }

    private static int nullableMaxUtf8Bytes(String s) {
        if (s == null) {
            return 0;
        }
        return ByteBufUtil.utf8MaxBytes(s);
    }

    private static void writeExactUTF8String(ByteBuf buf, String s, int utf8Length) {
        buf.ensureWritable(utf8Length + 2);
        buf.writeShort(utf8Length);
        if (utf8Length > 0) {
            int writtenUtf8Length = ByteBufUtil.reserveAndWriteUtf8(buf, s, utf8Length);
            if (writtenUtf8Length != utf8Length) {
                throw new AssertionError();
            }
        }
    }

    private static void writeEagerUTF8String(ByteBuf buf, String s) {
        int maxUtf8Length = nullableMaxUtf8Bytes(s);
        buf.ensureWritable(maxUtf8Length + 2);
        int writerIndex = buf.writerIndex();
        int startUtf8String = writerIndex + 2;
        buf.writerIndex(startUtf8String);
        int utf8Length = s != null ? ByteBufUtil.reserveAndWriteUtf8(buf, s, maxUtf8Length) : 0;
        buf.setShort(writerIndex, utf8Length);
    }

    private static void writeUnsafeUTF8String(ByteBuf buf, String s) {
        int writerIndex = buf.writerIndex();
        int startUtf8String = writerIndex + 2;
        buf.writerIndex(startUtf8String);
        int utf8Length = s != null ? ByteBufUtil.reserveAndWriteUtf8(buf, s, 0) : 0;
        buf.setShort(writerIndex, utf8Length);
    }

    private static int getVariableLengthInt(int num) {
        int count = 0;
        do {
            num /= 128;
            count++;
        } while (num > 0);
        return count;
    }
}
