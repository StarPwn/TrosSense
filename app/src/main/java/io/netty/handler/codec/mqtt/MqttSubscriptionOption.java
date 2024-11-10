package io.netty.handler.codec.mqtt;

/* loaded from: classes4.dex */
public final class MqttSubscriptionOption {
    private final boolean noLocal;
    private final MqttQoS qos;
    private final boolean retainAsPublished;
    private final RetainedHandlingPolicy retainHandling;

    /* loaded from: classes4.dex */
    public enum RetainedHandlingPolicy {
        SEND_AT_SUBSCRIBE(0),
        SEND_AT_SUBSCRIBE_IF_NOT_YET_EXISTS(1),
        DONT_SEND_AT_SUBSCRIBE(2);

        private final int value;

        RetainedHandlingPolicy(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static RetainedHandlingPolicy valueOf(int value) {
            switch (value) {
                case 0:
                    return SEND_AT_SUBSCRIBE;
                case 1:
                    return SEND_AT_SUBSCRIBE_IF_NOT_YET_EXISTS;
                case 2:
                    return DONT_SEND_AT_SUBSCRIBE;
                default:
                    throw new IllegalArgumentException("invalid RetainedHandlingPolicy: " + value);
            }
        }
    }

    public static MqttSubscriptionOption onlyFromQos(MqttQoS qos) {
        return new MqttSubscriptionOption(qos, false, false, RetainedHandlingPolicy.SEND_AT_SUBSCRIBE);
    }

    public MqttSubscriptionOption(MqttQoS qos, boolean noLocal, boolean retainAsPublished, RetainedHandlingPolicy retainHandling) {
        this.qos = qos;
        this.noLocal = noLocal;
        this.retainAsPublished = retainAsPublished;
        this.retainHandling = retainHandling;
    }

    public MqttQoS qos() {
        return this.qos;
    }

    public boolean isNoLocal() {
        return this.noLocal;
    }

    public boolean isRetainAsPublished() {
        return this.retainAsPublished;
    }

    public RetainedHandlingPolicy retainHandling() {
        return this.retainHandling;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MqttSubscriptionOption that = (MqttSubscriptionOption) o;
        if (this.noLocal == that.noLocal && this.retainAsPublished == that.retainAsPublished && this.qos == that.qos && this.retainHandling == that.retainHandling) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (((((this.qos.hashCode() * 31) + (this.noLocal ? 1 : 0)) * 31) + (this.retainAsPublished ? 1 : 0)) * 31) + this.retainHandling.hashCode();
    }

    public String toString() {
        return "SubscriptionOption[qos=" + this.qos + ", noLocal=" + this.noLocal + ", retainAsPublished=" + this.retainAsPublished + ", retainHandling=" + this.retainHandling + ']';
    }
}
