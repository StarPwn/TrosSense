package io.netty.handler.codec.mqtt;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class MqttMessage {
    private final DecoderResult decoderResult;
    private final MqttFixedHeader mqttFixedHeader;
    private final Object payload;
    private final Object variableHeader;
    public static final MqttMessage PINGREQ = new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0));
    public static final MqttMessage PINGRESP = new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0));
    public static final MqttMessage DISCONNECT = new MqttMessage(new MqttFixedHeader(MqttMessageType.DISCONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0));

    public MqttMessage(MqttFixedHeader mqttFixedHeader) {
        this(mqttFixedHeader, null, null);
    }

    public MqttMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
        this(mqttFixedHeader, variableHeader, null);
    }

    public MqttMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload) {
        this(mqttFixedHeader, variableHeader, payload, DecoderResult.SUCCESS);
    }

    public MqttMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload, DecoderResult decoderResult) {
        this.mqttFixedHeader = mqttFixedHeader;
        this.variableHeader = variableHeader;
        this.payload = payload;
        this.decoderResult = decoderResult;
    }

    public MqttFixedHeader fixedHeader() {
        return this.mqttFixedHeader;
    }

    public Object variableHeader() {
        return this.variableHeader;
    }

    public Object payload() {
        return this.payload;
    }

    public DecoderResult decoderResult() {
        return this.decoderResult;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[fixedHeader=" + (fixedHeader() != null ? fixedHeader().toString() : "") + ", variableHeader=" + (variableHeader() != null ? this.variableHeader.toString() : "") + ", payload=" + (payload() != null ? this.payload.toString() : "") + ']';
    }
}
