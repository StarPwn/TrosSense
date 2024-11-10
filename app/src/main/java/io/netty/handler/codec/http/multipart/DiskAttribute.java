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
public class DiskAttribute extends AbstractDiskHttpData implements Attribute {
    public static String baseDirectory = null;
    public static boolean deleteOnExitTemporaryFile = true;
    public static final String postfix = ".att";
    public static final String prefix = "Attr_";
    private String baseDir;
    private boolean deleteOnExit;

    public DiskAttribute(String name) {
        this(name, HttpConstants.DEFAULT_CHARSET);
    }

    public DiskAttribute(String name, String baseDir, boolean deleteOnExit) {
        this(name, HttpConstants.DEFAULT_CHARSET);
        this.baseDir = baseDir == null ? baseDirectory : baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public DiskAttribute(String name, long definedSize) {
        this(name, definedSize, HttpConstants.DEFAULT_CHARSET, baseDirectory, deleteOnExitTemporaryFile);
    }

    public DiskAttribute(String name, long definedSize, String baseDir, boolean deleteOnExit) {
        this(name, definedSize, HttpConstants.DEFAULT_CHARSET);
        this.baseDir = baseDir == null ? baseDirectory : baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public DiskAttribute(String name, Charset charset) {
        this(name, charset, baseDirectory, deleteOnExitTemporaryFile);
    }

    public DiskAttribute(String name, Charset charset, String baseDir, boolean deleteOnExit) {
        super(name, charset, 0L);
        this.baseDir = baseDir == null ? baseDirectory : baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public DiskAttribute(String name, long definedSize, Charset charset) {
        this(name, definedSize, charset, baseDirectory, deleteOnExitTemporaryFile);
    }

    public DiskAttribute(String name, long definedSize, Charset charset, String baseDir, boolean deleteOnExit) {
        super(name, charset, definedSize);
        this.baseDir = baseDir == null ? baseDirectory : baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public DiskAttribute(String name, String value) throws IOException {
        this(name, value, HttpConstants.DEFAULT_CHARSET);
    }

    public DiskAttribute(String name, String value, Charset charset) throws IOException {
        this(name, value, charset, baseDirectory, deleteOnExitTemporaryFile);
    }

    public DiskAttribute(String name, String value, Charset charset, String baseDir, boolean deleteOnExit) throws IOException {
        super(name, charset, 0L);
        setValue(value);
        this.baseDir = baseDir == null ? baseDirectory : baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.Attribute;
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public String getValue() throws IOException {
        byte[] bytes = get();
        return new String(bytes, getCharset());
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

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData, io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        long newDefinedSize = this.size + buffer.readableBytes();
        try {
            checkSize(newDefinedSize);
            if (this.definedSize > 0 && this.definedSize < newDefinedSize) {
                this.definedSize = newDefinedSize;
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
    public int compareTo(InterfaceHttpData o) {
        if (!(o instanceof Attribute)) {
            throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
        }
        return compareTo((Attribute) o);
    }

    public int compareTo(Attribute o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    public String toString() {
        try {
            return getName() + '=' + getValue();
        } catch (IOException e) {
            return getName() + '=' + e;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData
    protected boolean deleteOnExit() {
        return this.deleteOnExit;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData
    protected String getBaseDirectory() {
        return this.baseDir;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData
    protected String getDiskFilename() {
        return getName() + postfix;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData
    protected String getPostfix() {
        return postfix;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData
    protected String getPrefix() {
        return prefix;
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
        DiskAttribute attr = new DiskAttribute(getName(), this.baseDir, this.deleteOnExit);
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
    public Attribute retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.ReferenceCounted
    public Attribute touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
