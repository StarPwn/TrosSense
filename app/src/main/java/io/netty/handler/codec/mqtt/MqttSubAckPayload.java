package io.netty.handler.codec.mqtt;

import io.netty.handler.codec.mqtt.MqttReasonCodes;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes4.dex */
public class MqttSubAckPayload {
    private final List<MqttReasonCodes.SubAck> reasonCodes;

    public MqttSubAckPayload(int... reasonCodes) {
        ObjectUtil.checkNotNull(reasonCodes, "reasonCodes");
        List<MqttReasonCodes.SubAck> list = new ArrayList<>(reasonCodes.length);
        for (int v : reasonCodes) {
            list.add(MqttReasonCodes.SubAck.valueOf((byte) (v & 255)));
        }
        this.reasonCodes = Collections.unmodifiableList(list);
    }

    public MqttSubAckPayload(MqttReasonCodes.SubAck... reasonCodes) {
        ObjectUtil.checkNotNull(reasonCodes, "reasonCodes");
        List<MqttReasonCodes.SubAck> list = new ArrayList<>(reasonCodes.length);
        list.addAll(Arrays.asList(reasonCodes));
        this.reasonCodes = Collections.unmodifiableList(list);
    }

    public MqttSubAckPayload(Iterable<Integer> reasonCodes) {
        Integer v;
        ObjectUtil.checkNotNull(reasonCodes, "reasonCodes");
        List<MqttReasonCodes.SubAck> list = new ArrayList<>();
        Iterator<Integer> it2 = reasonCodes.iterator();
        while (it2.hasNext() && (v = it2.next()) != null) {
            list.add(MqttReasonCodes.SubAck.valueOf(v.byteValue()));
        }
        this.reasonCodes = Collections.unmodifiableList(list);
    }

    public List<Integer> grantedQoSLevels() {
        List<Integer> qosLevels = new ArrayList<>(this.reasonCodes.size());
        for (MqttReasonCodes.SubAck code : this.reasonCodes) {
            if ((code.byteValue() & 255) > MqttQoS.EXACTLY_ONCE.value()) {
                qosLevels.add(Integer.valueOf(MqttQoS.FAILURE.value()));
            } else {
                qosLevels.add(Integer.valueOf(code.byteValue() & 255));
            }
        }
        return qosLevels;
    }

    public List<Integer> reasonCodes() {
        return typedReasonCodesToOrdinal();
    }

    private List<Integer> typedReasonCodesToOrdinal() {
        List<Integer> intCodes = new ArrayList<>(this.reasonCodes.size());
        for (MqttReasonCodes.SubAck code : this.reasonCodes) {
            intCodes.add(Integer.valueOf(code.byteValue() & 255));
        }
        return intCodes;
    }

    public List<MqttReasonCodes.SubAck> typedReasonCodes() {
        return this.reasonCodes;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[reasonCodes=" + this.reasonCodes + ']';
    }
}
