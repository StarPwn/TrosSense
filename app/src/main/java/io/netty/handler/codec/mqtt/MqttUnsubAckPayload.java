package io.netty.handler.codec.mqtt;

import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.codec.mqtt.MqttReasonCodes;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public final class MqttUnsubAckPayload {
    private static final MqttUnsubAckPayload EMPTY = new MqttUnsubAckPayload(new short[0]);
    private final List<MqttReasonCodes.UnsubAck> unsubscribeReasonCodes;

    public static MqttUnsubAckPayload withEmptyDefaults(MqttUnsubAckPayload payload) {
        if (payload == null) {
            return EMPTY;
        }
        return payload;
    }

    public MqttUnsubAckPayload(short... unsubscribeReasonCodes) {
        ObjectUtil.checkNotNull(unsubscribeReasonCodes, "unsubscribeReasonCodes");
        List<MqttReasonCodes.UnsubAck> list = new ArrayList<>(unsubscribeReasonCodes.length);
        for (short s : unsubscribeReasonCodes) {
            Short v = Short.valueOf(s);
            list.add(MqttReasonCodes.UnsubAck.valueOf((byte) (v.shortValue() & Http2CodecUtil.MAX_UNSIGNED_BYTE)));
        }
        this.unsubscribeReasonCodes = Collections.unmodifiableList(list);
    }

    public MqttUnsubAckPayload(Iterable<Short> unsubscribeReasonCodes) {
        ObjectUtil.checkNotNull(unsubscribeReasonCodes, "unsubscribeReasonCodes");
        List<MqttReasonCodes.UnsubAck> list = new ArrayList<>();
        for (Short v : unsubscribeReasonCodes) {
            ObjectUtil.checkNotNull(v, "unsubscribeReasonCode");
            list.add(MqttReasonCodes.UnsubAck.valueOf(v.byteValue()));
        }
        this.unsubscribeReasonCodes = Collections.unmodifiableList(list);
    }

    public List<Short> unsubscribeReasonCodes() {
        return typedReasonCodesToOrdinal();
    }

    private List<Short> typedReasonCodesToOrdinal() {
        List<Short> codes = new ArrayList<>(this.unsubscribeReasonCodes.size());
        for (MqttReasonCodes.UnsubAck code : this.unsubscribeReasonCodes) {
            codes.add(Short.valueOf((short) (code.byteValue() & 255)));
        }
        return codes;
    }

    public List<MqttReasonCodes.UnsubAck> typedReasonCodes() {
        return this.unsubscribeReasonCodes;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[unsubscribeReasonCodes=" + this.unsubscribeReasonCodes + ']';
    }
}
