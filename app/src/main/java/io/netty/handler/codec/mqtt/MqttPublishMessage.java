package io.netty.handler.codec.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;

/* loaded from: classes4.dex */
public class MqttPublishMessage extends MqttMessage implements ByteBufHolder {
    public MqttPublishMessage(MqttFixedHeader mqttFixedHeader, MqttPublishVariableHeader variableHeader, ByteBuf payload) {
        super(mqttFixedHeader, variableHeader, payload);
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public MqttPublishVariableHeader variableHeader() {
        return (MqttPublishVariableHeader) super.variableHeader();
    }

    @Override // io.netty.handler.codec.mqtt.MqttMessage
    public ByteBuf payload() {
        return content();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible((ByteBuf) super.payload());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage copy() {
        return replace(content().copy());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.buffer.ByteBufHolder
    public MqttPublishMessage replace(ByteBuf content) {
        return new MqttPublishMessage(fixedHeader(), variableHeader(), content);
    }

    @Override // io.netty.util.ReferenceCounted
    public int refCnt() {
        return content().refCnt();
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage retain() {
        content().retain();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage retain(int increment) {
        content().retain(increment);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage touch() {
        content().touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public MqttPublishMessage touch(Object hint) {
        content().touch(hint);
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release() {
        return content().release();
    }

    @Override // io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        return content().release(decrement);
    }
}
