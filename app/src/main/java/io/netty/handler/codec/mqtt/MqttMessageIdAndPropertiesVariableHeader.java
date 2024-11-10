package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class MqttMessageIdAndPropertiesVariableHeader extends MqttMessageIdVariableHeader {
    private final MqttProperties properties;

    public MqttMessageIdAndPropertiesVariableHeader(int messageId, MqttProperties properties) {
        super(messageId);
        if (messageId < 1 || messageId > 65535) {
            throw new IllegalArgumentException("messageId: " + messageId + " (expected: 1 ~ 65535)");
        }
        this.properties = MqttProperties.withEmptyDefaults(properties);
    }

    public MqttProperties properties() {
        return this.properties;
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader
    public String toString() {
        return StringUtil.simpleClassName(this) + "[messageId=" + messageId() + ", properties=" + this.properties + ']';
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader
    MqttMessageIdAndPropertiesVariableHeader withDefaultEmptyProperties() {
        return this;
    }
}
