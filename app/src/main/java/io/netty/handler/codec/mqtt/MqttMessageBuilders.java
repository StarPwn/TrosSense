package io.netty.handler.codec.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes4.dex */
public final class MqttMessageBuilders {

    /* loaded from: classes4.dex */
    public interface PropertiesInitializer<T> {
        void apply(T t);
    }

    /* loaded from: classes4.dex */
    public static final class PublishBuilder {
        private int messageId;
        private MqttProperties mqttProperties;
        private ByteBuf payload;
        private MqttQoS qos;
        private boolean retained;
        private String topic;

        PublishBuilder() {
        }

        public PublishBuilder topicName(String topic) {
            this.topic = topic;
            return this;
        }

        public PublishBuilder retained(boolean retained) {
            this.retained = retained;
            return this;
        }

        public PublishBuilder qos(MqttQoS qos) {
            this.qos = qos;
            return this;
        }

        public PublishBuilder payload(ByteBuf payload) {
            this.payload = payload;
            return this;
        }

        public PublishBuilder messageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        public PublishBuilder properties(MqttProperties properties) {
            this.mqttProperties = properties;
            return this;
        }

        public MqttPublishMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, this.qos, this.retained, 0);
            MqttPublishVariableHeader mqttVariableHeader = new MqttPublishVariableHeader(this.topic, this.messageId, this.mqttProperties);
            return new MqttPublishMessage(mqttFixedHeader, mqttVariableHeader, Unpooled.buffer().writeBytes(this.payload));
        }
    }

    /* loaded from: classes4.dex */
    public static final class ConnectBuilder {
        private boolean cleanSession;
        private String clientId;
        private boolean hasPassword;
        private boolean hasUser;
        private int keepAliveSecs;
        private byte[] password;
        private String username;
        private boolean willFlag;
        private byte[] willMessage;
        private boolean willRetain;
        private String willTopic;
        private MqttVersion version = MqttVersion.MQTT_3_1_1;
        private MqttProperties willProperties = MqttProperties.NO_PROPERTIES;
        private MqttQoS willQos = MqttQoS.AT_MOST_ONCE;
        private MqttProperties properties = MqttProperties.NO_PROPERTIES;

        ConnectBuilder() {
        }

        public ConnectBuilder protocolVersion(MqttVersion version) {
            this.version = version;
            return this;
        }

        public ConnectBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ConnectBuilder cleanSession(boolean cleanSession) {
            this.cleanSession = cleanSession;
            return this;
        }

        public ConnectBuilder keepAlive(int keepAliveSecs) {
            this.keepAliveSecs = keepAliveSecs;
            return this;
        }

        public ConnectBuilder willFlag(boolean willFlag) {
            this.willFlag = willFlag;
            return this;
        }

        public ConnectBuilder willQoS(MqttQoS willQos) {
            this.willQos = willQos;
            return this;
        }

        public ConnectBuilder willTopic(String willTopic) {
            this.willTopic = willTopic;
            return this;
        }

        @Deprecated
        public ConnectBuilder willMessage(String willMessage) {
            willMessage(willMessage == null ? null : willMessage.getBytes(CharsetUtil.UTF_8));
            return this;
        }

        public ConnectBuilder willMessage(byte[] willMessage) {
            this.willMessage = willMessage;
            return this;
        }

        public ConnectBuilder willRetain(boolean willRetain) {
            this.willRetain = willRetain;
            return this;
        }

        public ConnectBuilder willProperties(MqttProperties willProperties) {
            this.willProperties = willProperties;
            return this;
        }

        public ConnectBuilder hasUser(boolean value) {
            this.hasUser = value;
            return this;
        }

        public ConnectBuilder hasPassword(boolean value) {
            this.hasPassword = value;
            return this;
        }

        public ConnectBuilder username(String username) {
            this.hasUser = username != null;
            this.username = username;
            return this;
        }

        @Deprecated
        public ConnectBuilder password(String password) {
            password(password == null ? null : password.getBytes(CharsetUtil.UTF_8));
            return this;
        }

        public ConnectBuilder password(byte[] password) {
            this.hasPassword = password != null;
            this.password = password;
            return this;
        }

        public ConnectBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public MqttConnectMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(this.version.protocolName(), this.version.protocolLevel(), this.hasUser, this.hasPassword, this.willRetain, this.willQos.value(), this.willFlag, this.cleanSession, this.keepAliveSecs, this.properties);
            MqttConnectPayload mqttConnectPayload = new MqttConnectPayload(this.clientId, this.willProperties, this.willTopic, this.willMessage, this.username, this.password);
            return new MqttConnectMessage(mqttFixedHeader, mqttConnectVariableHeader, mqttConnectPayload);
        }
    }

    /* loaded from: classes4.dex */
    public static final class SubscribeBuilder {
        private int messageId;
        private MqttProperties properties;
        private List<MqttTopicSubscription> subscriptions;

        SubscribeBuilder() {
        }

        public SubscribeBuilder addSubscription(MqttQoS qos, String topic) {
            ensureSubscriptionsExist();
            this.subscriptions.add(new MqttTopicSubscription(topic, qos));
            return this;
        }

        public SubscribeBuilder addSubscription(String topic, MqttSubscriptionOption option) {
            ensureSubscriptionsExist();
            this.subscriptions.add(new MqttTopicSubscription(topic, option));
            return this;
        }

        public SubscribeBuilder messageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        public SubscribeBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public MqttSubscribeMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
            MqttMessageIdAndPropertiesVariableHeader mqttVariableHeader = new MqttMessageIdAndPropertiesVariableHeader(this.messageId, this.properties);
            MqttSubscribePayload mqttSubscribePayload = new MqttSubscribePayload(this.subscriptions);
            return new MqttSubscribeMessage(mqttFixedHeader, mqttVariableHeader, mqttSubscribePayload);
        }

        private void ensureSubscriptionsExist() {
            if (this.subscriptions == null) {
                this.subscriptions = new ArrayList(5);
            }
        }
    }

    /* loaded from: classes4.dex */
    public static final class UnsubscribeBuilder {
        private int messageId;
        private MqttProperties properties;
        private List<String> topicFilters;

        UnsubscribeBuilder() {
        }

        public UnsubscribeBuilder addTopicFilter(String topic) {
            if (this.topicFilters == null) {
                this.topicFilters = new ArrayList(5);
            }
            this.topicFilters.add(topic);
            return this;
        }

        public UnsubscribeBuilder messageId(int messageId) {
            this.messageId = messageId;
            return this;
        }

        public UnsubscribeBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public MqttUnsubscribeMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0);
            MqttMessageIdAndPropertiesVariableHeader mqttVariableHeader = new MqttMessageIdAndPropertiesVariableHeader(this.messageId, this.properties);
            MqttUnsubscribePayload mqttSubscribePayload = new MqttUnsubscribePayload(this.topicFilters);
            return new MqttUnsubscribeMessage(mqttFixedHeader, mqttVariableHeader, mqttSubscribePayload);
        }
    }

    /* loaded from: classes4.dex */
    public static final class ConnAckBuilder {
        private MqttProperties properties;
        private ConnAckPropertiesBuilder propsBuilder;
        private MqttConnectReturnCode returnCode;
        private boolean sessionPresent;

        private ConnAckBuilder() {
            this.properties = MqttProperties.NO_PROPERTIES;
        }

        public ConnAckBuilder returnCode(MqttConnectReturnCode returnCode) {
            this.returnCode = returnCode;
            return this;
        }

        public ConnAckBuilder sessionPresent(boolean sessionPresent) {
            this.sessionPresent = sessionPresent;
            return this;
        }

        public ConnAckBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public ConnAckBuilder properties(PropertiesInitializer<ConnAckPropertiesBuilder> consumer) {
            if (this.propsBuilder == null) {
                this.propsBuilder = new ConnAckPropertiesBuilder();
            }
            consumer.apply(this.propsBuilder);
            return this;
        }

        public MqttConnAckMessage build() {
            if (this.propsBuilder != null) {
                this.properties = this.propsBuilder.build();
            }
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(this.returnCode, this.sessionPresent, this.properties);
            return new MqttConnAckMessage(mqttFixedHeader, mqttConnAckVariableHeader);
        }
    }

    /* loaded from: classes4.dex */
    public static final class ConnAckPropertiesBuilder {
        private byte[] authenticationData;
        private String authenticationMethod;
        private String clientId;
        private Long maximumPacketSize;
        private Byte maximumQos;
        private String reasonString;
        private int receiveMaximum;
        private String responseInformation;
        private boolean retain;
        private Integer serverKeepAlive;
        private String serverReference;
        private Long sessionExpiryInterval;
        private Boolean sharedSubscriptionAvailable;
        private Boolean subscriptionIdentifiersAvailable;
        private int topicAliasMaximum;
        private final MqttProperties.UserProperties userProperties = new MqttProperties.UserProperties();
        private Boolean wildcardSubscriptionAvailable;

        public MqttProperties build() {
            MqttProperties mqttProperties = new MqttProperties();
            if (this.clientId != null) {
                mqttProperties.add(new MqttProperties.StringProperty(MqttProperties.MqttPropertyType.ASSIGNED_CLIENT_IDENTIFIER.value(), this.clientId));
            }
            if (this.sessionExpiryInterval != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.SESSION_EXPIRY_INTERVAL.value(), Integer.valueOf(this.sessionExpiryInterval.intValue())));
            }
            if (this.receiveMaximum > 0) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.RECEIVE_MAXIMUM.value(), Integer.valueOf(this.receiveMaximum)));
            }
            if (this.maximumQos != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.MAXIMUM_QOS.value(), Integer.valueOf(this.receiveMaximum)));
            }
            mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.RETAIN_AVAILABLE.value(), Integer.valueOf(this.retain ? 1 : 0)));
            if (this.maximumPacketSize != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.MAXIMUM_PACKET_SIZE.value(), Integer.valueOf(this.maximumPacketSize.intValue())));
            }
            mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.TOPIC_ALIAS_MAXIMUM.value(), Integer.valueOf(this.topicAliasMaximum)));
            if (this.reasonString != null) {
                mqttProperties.add(new MqttProperties.StringProperty(MqttProperties.MqttPropertyType.REASON_STRING.value(), this.reasonString));
            }
            mqttProperties.add(this.userProperties);
            if (this.wildcardSubscriptionAvailable != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.WILDCARD_SUBSCRIPTION_AVAILABLE.value(), Integer.valueOf(this.wildcardSubscriptionAvailable.booleanValue() ? 1 : 0)));
            }
            if (this.subscriptionIdentifiersAvailable != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.SUBSCRIPTION_IDENTIFIER_AVAILABLE.value(), Integer.valueOf(this.subscriptionIdentifiersAvailable.booleanValue() ? 1 : 0)));
            }
            if (this.sharedSubscriptionAvailable != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.SHARED_SUBSCRIPTION_AVAILABLE.value(), Integer.valueOf(this.sharedSubscriptionAvailable.booleanValue() ? 1 : 0)));
            }
            if (this.serverKeepAlive != null) {
                mqttProperties.add(new MqttProperties.IntegerProperty(MqttProperties.MqttPropertyType.SERVER_KEEP_ALIVE.value(), this.serverKeepAlive));
            }
            if (this.responseInformation != null) {
                mqttProperties.add(new MqttProperties.StringProperty(MqttProperties.MqttPropertyType.RESPONSE_INFORMATION.value(), this.responseInformation));
            }
            if (this.serverReference != null) {
                mqttProperties.add(new MqttProperties.StringProperty(MqttProperties.MqttPropertyType.SERVER_REFERENCE.value(), this.serverReference));
            }
            if (this.authenticationMethod != null) {
                mqttProperties.add(new MqttProperties.StringProperty(MqttProperties.MqttPropertyType.AUTHENTICATION_METHOD.value(), this.authenticationMethod));
            }
            if (this.authenticationData != null) {
                mqttProperties.add(new MqttProperties.BinaryProperty(MqttProperties.MqttPropertyType.AUTHENTICATION_DATA.value(), this.authenticationData));
            }
            return mqttProperties;
        }

        public ConnAckPropertiesBuilder sessionExpiryInterval(long seconds) {
            this.sessionExpiryInterval = Long.valueOf(seconds);
            return this;
        }

        public ConnAckPropertiesBuilder receiveMaximum(int value) {
            this.receiveMaximum = ObjectUtil.checkPositive(value, "value");
            return this;
        }

        public ConnAckPropertiesBuilder maximumQos(byte value) {
            if (value != 0 && value != 1) {
                throw new IllegalArgumentException("maximum QoS property could be 0 or 1");
            }
            this.maximumQos = Byte.valueOf(value);
            return this;
        }

        public ConnAckPropertiesBuilder retainAvailable(boolean retain) {
            this.retain = retain;
            return this;
        }

        public ConnAckPropertiesBuilder maximumPacketSize(long size) {
            this.maximumPacketSize = Long.valueOf(ObjectUtil.checkPositive(size, "size"));
            return this;
        }

        public ConnAckPropertiesBuilder assignedClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ConnAckPropertiesBuilder topicAliasMaximum(int value) {
            this.topicAliasMaximum = value;
            return this;
        }

        public ConnAckPropertiesBuilder reasonString(String reason) {
            this.reasonString = reason;
            return this;
        }

        public ConnAckPropertiesBuilder userProperty(String name, String value) {
            this.userProperties.add(name, value);
            return this;
        }

        public ConnAckPropertiesBuilder wildcardSubscriptionAvailable(boolean value) {
            this.wildcardSubscriptionAvailable = Boolean.valueOf(value);
            return this;
        }

        public ConnAckPropertiesBuilder subscriptionIdentifiersAvailable(boolean value) {
            this.subscriptionIdentifiersAvailable = Boolean.valueOf(value);
            return this;
        }

        public ConnAckPropertiesBuilder sharedSubscriptionAvailable(boolean value) {
            this.sharedSubscriptionAvailable = Boolean.valueOf(value);
            return this;
        }

        public ConnAckPropertiesBuilder serverKeepAlive(int seconds) {
            this.serverKeepAlive = Integer.valueOf(seconds);
            return this;
        }

        public ConnAckPropertiesBuilder responseInformation(String value) {
            this.responseInformation = value;
            return this;
        }

        public ConnAckPropertiesBuilder serverReference(String host) {
            this.serverReference = host;
            return this;
        }

        public ConnAckPropertiesBuilder authenticationMethod(String methodName) {
            this.authenticationMethod = methodName;
            return this;
        }

        public ConnAckPropertiesBuilder authenticationData(byte[] rawData) {
            this.authenticationData = (byte[]) rawData.clone();
            return this;
        }
    }

    /* loaded from: classes4.dex */
    public static final class PubAckBuilder {
        private int packetId;
        private MqttProperties properties;
        private byte reasonCode;

        PubAckBuilder() {
        }

        public PubAckBuilder reasonCode(byte reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        public PubAckBuilder packetId(int packetId) {
            this.packetId = packetId;
            return this;
        }

        @Deprecated
        public PubAckBuilder packetId(short packetId) {
            return packetId(65535 & packetId);
        }

        public PubAckBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public MqttMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttPubReplyMessageVariableHeader mqttPubAckVariableHeader = new MqttPubReplyMessageVariableHeader(this.packetId, this.reasonCode, this.properties);
            return new MqttMessage(mqttFixedHeader, mqttPubAckVariableHeader);
        }
    }

    /* loaded from: classes4.dex */
    public static final class SubAckBuilder {
        private final List<MqttQoS> grantedQoses = new ArrayList();
        private int packetId;
        private MqttProperties properties;

        SubAckBuilder() {
        }

        public SubAckBuilder packetId(int packetId) {
            this.packetId = packetId;
            return this;
        }

        @Deprecated
        public SubAckBuilder packetId(short packetId) {
            return packetId(65535 & packetId);
        }

        public SubAckBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public SubAckBuilder addGrantedQos(MqttQoS qos) {
            this.grantedQoses.add(qos);
            return this;
        }

        public SubAckBuilder addGrantedQoses(MqttQoS... qoses) {
            this.grantedQoses.addAll(Arrays.asList(qoses));
            return this;
        }

        public MqttSubAckMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessageIdAndPropertiesVariableHeader mqttSubAckVariableHeader = new MqttMessageIdAndPropertiesVariableHeader(this.packetId, this.properties);
            int[] grantedQoses = new int[this.grantedQoses.size()];
            int i = 0;
            for (MqttQoS grantedQos : this.grantedQoses) {
                grantedQoses[i] = grantedQos.value();
                i++;
            }
            MqttSubAckPayload subAckPayload = new MqttSubAckPayload(grantedQoses);
            return new MqttSubAckMessage(mqttFixedHeader, mqttSubAckVariableHeader, subAckPayload);
        }
    }

    /* loaded from: classes4.dex */
    public static final class UnsubAckBuilder {
        private int packetId;
        private MqttProperties properties;
        private final List<Short> reasonCodes = new ArrayList();

        UnsubAckBuilder() {
        }

        public UnsubAckBuilder packetId(int packetId) {
            this.packetId = packetId;
            return this;
        }

        @Deprecated
        public UnsubAckBuilder packetId(short packetId) {
            return packetId(65535 & packetId);
        }

        public UnsubAckBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public UnsubAckBuilder addReasonCode(short reasonCode) {
            this.reasonCodes.add(Short.valueOf(reasonCode));
            return this;
        }

        public UnsubAckBuilder addReasonCodes(Short... reasonCodes) {
            this.reasonCodes.addAll(Arrays.asList(reasonCodes));
            return this;
        }

        public MqttUnsubAckMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessageIdAndPropertiesVariableHeader mqttSubAckVariableHeader = new MqttMessageIdAndPropertiesVariableHeader(this.packetId, this.properties);
            MqttUnsubAckPayload subAckPayload = new MqttUnsubAckPayload(this.reasonCodes);
            return new MqttUnsubAckMessage(mqttFixedHeader, mqttSubAckVariableHeader, subAckPayload);
        }
    }

    /* loaded from: classes4.dex */
    public static final class DisconnectBuilder {
        private MqttProperties properties;
        private byte reasonCode;

        DisconnectBuilder() {
        }

        public DisconnectBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public DisconnectBuilder reasonCode(byte reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        public MqttMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.DISCONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttReasonCodeAndPropertiesVariableHeader mqttDisconnectVariableHeader = new MqttReasonCodeAndPropertiesVariableHeader(this.reasonCode, this.properties);
            return new MqttMessage(mqttFixedHeader, mqttDisconnectVariableHeader);
        }
    }

    /* loaded from: classes4.dex */
    public static final class AuthBuilder {
        private MqttProperties properties;
        private byte reasonCode;

        AuthBuilder() {
        }

        public AuthBuilder properties(MqttProperties properties) {
            this.properties = properties;
            return this;
        }

        public AuthBuilder reasonCode(byte reasonCode) {
            this.reasonCode = reasonCode;
            return this;
        }

        public MqttMessage build() {
            MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.AUTH, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttReasonCodeAndPropertiesVariableHeader mqttAuthVariableHeader = new MqttReasonCodeAndPropertiesVariableHeader(this.reasonCode, this.properties);
            return new MqttMessage(mqttFixedHeader, mqttAuthVariableHeader);
        }
    }

    public static ConnectBuilder connect() {
        return new ConnectBuilder();
    }

    public static ConnAckBuilder connAck() {
        return new ConnAckBuilder();
    }

    public static PublishBuilder publish() {
        return new PublishBuilder();
    }

    public static SubscribeBuilder subscribe() {
        return new SubscribeBuilder();
    }

    public static UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder();
    }

    public static PubAckBuilder pubAck() {
        return new PubAckBuilder();
    }

    public static SubAckBuilder subAck() {
        return new SubAckBuilder();
    }

    public static UnsubAckBuilder unsubAck() {
        return new UnsubAckBuilder();
    }

    public static DisconnectBuilder disconnect() {
        return new DisconnectBuilder();
    }

    public static AuthBuilder auth() {
        return new AuthBuilder();
    }

    private MqttMessageBuilders() {
    }
}
