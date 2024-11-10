package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public abstract class AbstractHttpData extends AbstractReferenceCounted implements HttpData {
    private boolean completed;
    protected long definedSize;
    private final String name;
    protected long size;
    private static final Pattern STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
    private static final Pattern REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
    private Charset charset = HttpConstants.DEFAULT_CHARSET;
    private long maxSize = -1;

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public abstract HttpData touch();

    @Override // io.netty.util.ReferenceCounted
    public abstract HttpData touch(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractHttpData(String name, Charset charset, long size) {
        ObjectUtil.checkNotNull(name, "name");
        this.name = ObjectUtil.checkNonEmpty(STRIP_PATTERN.matcher(REPLACE_PATTERN.matcher(name).replaceAll(" ")).replaceAll(""), "name");
        if (charset != null) {
            setCharset(charset);
        }
        this.definedSize = size;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long getMaxSize() {
        return this.maxSize;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void checkSize(long newSize) throws IOException {
        if (this.maxSize >= 0 && newSize > this.maxSize) {
            throw new IOException("Size exceed allowed maximum capacity");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public String getName() {
        return this.name;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isCompleted() {
        return this.completed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCompleted() {
        setCompleted(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public Charset getCharset() {
        return this.charset;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setCharset(Charset charset) {
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long length() {
        return this.size;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long definedLength() {
        return this.definedSize;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        try {
            return getByteBuf();
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        delete();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HttpData retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HttpData retain(int increment) {
        super.retain(increment);
        return this;
    }
}
