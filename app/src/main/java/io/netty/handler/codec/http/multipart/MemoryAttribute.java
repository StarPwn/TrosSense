package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class MemoryAttribute extends AbstractMemoryHttpData implements Attribute {
    public MemoryAttribute(String name) {
        this(name, HttpConstants.DEFAULT_CHARSET);
    }

    public MemoryAttribute(String name, long definedSize) {
        this(name, definedSize, HttpConstants.DEFAULT_CHARSET);
    }

    public MemoryAttribute(String name, Charset charset) {
        super(name, charset, 0L);
    }

    public MemoryAttribute(String name, long definedSize, Charset charset) {
        super(name, charset, definedSize);
    }

    public MemoryAttribute(String name, String value) throws IOException {
        this(name, value, HttpConstants.DEFAULT_CHARSET);
    }

    public MemoryAttribute(String name, String value, Charset charset) throws IOException {
        super(name, charset, 0L);
        setValue(value);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.Attribute;
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public String getValue() {
        return getByteBuf().toString(getCharset());
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public void setValue(String value) throws IOException {
        ObjectUtil.checkNotNull(value, "value");
        byte[] bytes = value.getBytes(getCharset());
        checkSize(bytes.length);
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        if (this.definedSize > 0) {
            this.definedSize = buffer.readableBytes();
        }
        setContent(buffer);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMemoryHttpData, io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        int localsize = buffer.readableBytes();
        try {
            checkSize(this.size + localsize);
            if (this.definedSize > 0 && this.definedSize < this.size + localsize) {
                this.definedSize = this.size + localsize;
            }
            super.addContent(buffer, last);
        } catch (IOException e) {
            buffer.release();
            throw e;
        }
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute) o;
        return getName().equalsIgnoreCase(attribute.getName());
    }

    @Override // java.lang.Comparable
    public int compareTo(InterfaceHttpData other) {
        if (!(other instanceof Attribute)) {
            throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + other.getHttpDataType());
        }
        return compareTo((Attribute) other);
    }

    public int compareTo(Attribute o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    public String toString() {
        return getName() + '=' + getValue();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute copy() {
        ByteBuf content = content();
        return replace(content != null ? content.copy() : null);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute duplicate() {
        ByteBuf content = content();
        return replace(content != null ? content.duplicate() : null);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute retainedDuplicate() {
        ByteBuf content = content();
        if (content != null) {
            ByteBuf content2 = content.retainedDuplicate();
            boolean success = false;
            try {
                Attribute duplicate = replace(content2);
                success = true;
                return duplicate;
            } finally {
                if (!success) {
                    content2.release();
                }
            }
        }
        return replace((ByteBuf) null);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute replace(ByteBuf content) {
        MemoryAttribute attr = new MemoryAttribute(getName());
        attr.setCharset(getCharset());
        if (content != null) {
            try {
                attr.setContent(content);
            } catch (IOException e) {
                throw new ChannelException(e);
            }
        }
        attr.setCompleted(isCompleted());
        return attr;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMemoryHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMemoryHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.ReferenceCounted
    public Attribute touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
