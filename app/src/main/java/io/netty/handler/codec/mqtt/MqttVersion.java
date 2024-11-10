package io.netty.handler.codec.mqtt;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public enum MqttVersion {
    MQTT_3_1("MQIsdp", (byte) 3),
    MQTT_3_1_1("MQTT", (byte) 4),
    MQTT_5("MQTT", (byte) 5);

    private final byte level;
    private final String name;

    MqttVersion(String protocolName, byte protocolLevel) {
        this.name = (String) ObjectUtil.checkNotNull(protocolName, "protocolName");
        this.level = protocolLevel;
    }

    public String protocolName() {
        return this.name;
    }

    public byte[] protocolNameBytes() {
        return this.name.getBytes(CharsetUtil.UTF_8);
    }

    public byte protocolLevel() {
        return this.level;
    }

    public static MqttVersion fromProtocolNameAndLevel(String protocolName, byte protocolLevel) {
        MqttVersion mv = null;
        switch (protocolLevel) {
            case 3:
                mv = MQTT_3_1;
                break;
            case 4:
                mv = MQTT_3_1_1;
                break;
            case 5:
                mv = MQTT_5;
                break;
        }
        if (mv == null) {
            throw new MqttUnacceptableProtocolVersionException(protocolName + " is an unknown protocol name");
        }
        if (mv.name.equals(protocolName)) {
            return mv;
        }
        throw new MqttUnacceptableProtocolVersionException(protocolName + " and " + ((int) protocolLevel) + " don't match");
    }
}
