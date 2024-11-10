package io.netty.handler.codec.haproxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class HAProxyTLV extends DefaultByteBufHolder {
    private final Type type;
    private final byte typeByteValue;

    /* JADX INFO: Access modifiers changed from: package-private */
    public int totalNumBytes() {
        return contentNumBytes() + 3;
    }

    int contentNumBytes() {
        return content().readableBytes();
    }

    /* loaded from: classes4.dex */
    public enum Type {
        PP2_TYPE_ALPN,
        PP2_TYPE_AUTHORITY,
        PP2_TYPE_SSL,
        PP2_TYPE_SSL_VERSION,
        PP2_TYPE_SSL_CN,
        PP2_TYPE_NETNS,
        OTHER;

        public static Type typeForByteValue(byte byteValue) {
            switch (byteValue) {
                case 1:
                    return PP2_TYPE_ALPN;
                case 2:
                    return PP2_TYPE_AUTHORITY;
                case 32:
                    return PP2_TYPE_SSL;
                case 33:
                    return PP2_TYPE_SSL_VERSION;
                case 34:
                    return PP2_TYPE_SSL_CN;
                case 48:
                    return PP2_TYPE_NETNS;
                default:
                    return OTHER;
            }
        }

        public static byte byteValueForType(Type type) {
            switch (type) {
                case PP2_TYPE_ALPN:
                    return (byte) 1;
                case PP2_TYPE_AUTHORITY:
                    return (byte) 2;
                case PP2_TYPE_SSL:
                    return (byte) 32;
                case PP2_TYPE_SSL_VERSION:
                    return BinaryMemcacheOpcodes.SASL_AUTH;
                case PP2_TYPE_SSL_CN:
                    return (byte) 34;
                case PP2_TYPE_NETNS:
                    return (byte) 48;
                default:
                    throw new IllegalArgumentException("unknown type: " + type);
            }
        }
    }

    public HAProxyTLV(byte typeByteValue, ByteBuf content) {
        this(Type.typeForByteValue(typeByteValue), typeByteValue, content);
    }

    public HAProxyTLV(Type type, ByteBuf content) {
        this(type, Type.byteValueForType(type), content);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HAProxyTLV(Type type, byte typeByteValue, ByteBuf content) {
        super(content);
        this.type = (Type) ObjectUtil.checkNotNull(type, "type");
        this.typeByteValue = typeByteValue;
    }

    public Type type() {
        return this.type;
    }

    public byte typeByteValue() {
        return this.typeByteValue;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public HAProxyTLV copy() {
        return replace(content().copy());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public HAProxyTLV duplicate() {
        return replace(content().duplicate());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public HAProxyTLV retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.buffer.ByteBufHolder
    public HAProxyTLV replace(ByteBuf content) {
        return new HAProxyTLV(this.type, this.typeByteValue, content);
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public HAProxyTLV retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public HAProxyTLV retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public HAProxyTLV touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder, io.netty.util.ReferenceCounted
    public HAProxyTLV touch(Object hint) {
        super.touch(hint);
        return this;
    }

    @Override // io.netty.buffer.DefaultByteBufHolder
    public String toString() {
        return StringUtil.simpleClassName(this) + "(type: " + type() + ", typeByteValue: " + ((int) typeByteValue()) + ", content: " + contentToString() + ')';
    }
}
