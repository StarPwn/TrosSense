package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectUtil;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes4.dex */
final class InternalAttribute extends AbstractReferenceCounted implements InterfaceHttpData {
    private final Charset charset;
    private int size;
    private final List<ByteBuf> value = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public InternalAttribute(Charset charset) {
        this.charset = charset;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.InternalAttribute;
    }

    public void addValue(String value) {
        ObjectUtil.checkNotNull(value, "value");
        ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
        this.value.add(buf);
        this.size += buf.readableBytes();
    }

    public void addValue(String value, int rank) {
        ObjectUtil.checkNotNull(value, "value");
        ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
        this.value.add(rank, buf);
        this.size += buf.readableBytes();
    }

    public void setValue(String value, int rank) {
        ObjectUtil.checkNotNull(value, "value");
        ByteBuf buf = Unpooled.copiedBuffer(value, this.charset);
        ByteBuf old = this.value.set(rank, buf);
        if (old != null) {
            this.size -= old.readableBytes();
            old.release();
        }
        this.size += buf.readableBytes();
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof InternalAttribute)) {
            return false;
        }
        InternalAttribute attribute = (InternalAttribute) o;
        return getName().equalsIgnoreCase(attribute.getName());
    }

    @Override // java.lang.Comparable
    public int compareTo(InterfaceHttpData o) {
        if (!(o instanceof InternalAttribute)) {
            throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
        }
        return compareTo((InternalAttribute) o);
    }

    public int compareTo(InternalAttribute o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ByteBuf elt : this.value) {
            result.append(elt.toString(this.charset));
        }
        return result.toString();
    }

    public int size() {
        return this.size;
    }

    public ByteBuf toByteBuf() {
        return Unpooled.compositeBuffer().addComponents(this.value).writerIndex(size()).readerIndex(0);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public String getName() {
        return "InternalAttribute";
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public InterfaceHttpData retain() {
        for (ByteBuf buf : this.value) {
            buf.retain();
        }
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public InterfaceHttpData retain(int increment) {
        for (ByteBuf buf : this.value) {
            buf.retain(increment);
        }
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public InterfaceHttpData touch() {
        for (ByteBuf buf : this.value) {
            buf.touch();
        }
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public InterfaceHttpData touch(Object hint) {
        for (ByteBuf buf : this.value) {
            buf.touch(hint);
        }
        return this;
    }
}
