package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public final class MqttTopicSubscription {
    private final MqttSubscriptionOption option;
    private String topicFilter;

    public MqttTopicSubscription(String topicFilter, MqttQoS qualityOfService) {
        this.topicFilter = topicFilter;
        this.option = MqttSubscriptionOption.onlyFromQos(qualityOfService);
    }

    public MqttTopicSubscription(String topicFilter, MqttSubscriptionOption option) {
        this.topicFilter = topicFilter;
        this.option = option;
    }

    @Deprecated
    public String topicName() {
        return this.topicFilter;
    }

    public String topicFilter() {
        return this.topicFilter;
    }

    public void setTopicFilter(String topicFilter) {
        this.topicFilter = topicFilter;
    }

    public MqttQoS qualityOfService() {
        return this.option.qos();
    }

    public MqttSubscriptionOption option() {
        return this.option;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[topicFilter=" + this.topicFilter + ", option=" + this.option + ']';
    }
}
