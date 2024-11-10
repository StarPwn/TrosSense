package io.netty.handler.codec.mqtt;

/* loaded from: classes4.dex */
public final class MqttUnsubAckMessage extends MqttMessage {
    public MqttUnsubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdAndPropertiesVariableHeader variableHeader, MqttUnsubAckPayload payload) {
        super(mqttFixedHeader, variableHeader, MqttUnsubAckPayload.withEmptyDefaults(payload));
    }

    public MqttUnsubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader, MqttUnsubAckPayload payload) {
        this(mqttFixedHeader, fallbackVariableHeader(variableHeader), payload);
    }

    public MqttUnsubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader) {
        this(mqttFixedHeader, variableHeader, (MqttUnsubAckPayload) null);
    }

    private static MqttMessageIdAndPropertiesVariableHeader fallbackVariableHeader(MqttMessageIdVariableHeader variableHeader) {
        if (variableHeader instanceof MqttMessageIdAndPropertiesVariableHeader) {
            return (MqttMessageIdAndPropertiesVariableHeader) variableHeader;
        }
        return new MqttMessageIdAndPropertiesVariableHeader(variableHeader.messageId(), MqttProperties.NO_PROPERTIES);
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader) super.variableHeader();
    }

    public MqttMessageIdAndPropertiesVariableHeader idAndPropertiesVariableHeader() {
        return (MqttMessageIdAndPropertiesVariableHeader) super.variableHeader();
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttUnsubAckPayload payload() {
        return (MqttUnsubAckPayload) super.payload();
    }
}
