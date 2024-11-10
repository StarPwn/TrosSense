package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class MqttPubReplyMessageVariableHeader extends MqttMessageIdVariableHeader {
    public static final byte REASON_CODE_OK = 0;
    private final MqttProperties properties;
    private final byte reasonCode;

    public MqttPubReplyMessageVariableHeader(int messageId, byte reasonCode, MqttProperties properties) {
        super(messageId);
        if (messageId < 1 || messageId > 65535) {
            throw new IllegalArgumentException("messageId: " + messageId + " (expected: 1 ~ 65535)");
        }
        this.reasonCode = reasonCode;
        this.properties = MqttProperties.withEmptyDefaults(properties);
    }

    public byte reasonCode() {
        return this.reasonCode;
    }

    public MqttProperties properties() {
        return this.properties;
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader
    public String toString() {
        return StringUtil.simpleClassName(this) + "[messageId=" + messageId() + ", reasonCode=" + ((int) this.reasonCode) + ", properties=" + this.properties + ']';
    }
}
