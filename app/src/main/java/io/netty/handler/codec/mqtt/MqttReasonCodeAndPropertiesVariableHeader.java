package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class MqttReasonCodeAndPropertiesVariableHeader {
    public static final byte REASON_CODE_OK = 0;
    private final MqttProperties properties;
    private final byte reasonCode;

    public MqttReasonCodeAndPropertiesVariableHeader(byte reasonCode, MqttProperties properties) {
        this.reasonCode = reasonCode;
        this.properties = MqttProperties.withEmptyDefaults(properties);
    }

    public byte reasonCode() {
        return this.reasonCode;
    }

    public MqttProperties properties() {
        return this.properties;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[reasonCode=" + ((int) this.reasonCode) + ", properties=" + this.properties + ']';
    }
}
