package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.AbstractReferenceCounted;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
abstract class AbstractMixedHttpData<D extends HttpData> extends AbstractReferenceCounted implements HttpData {
    final String baseDir;
    final boolean deleteOnExit;
    private final long limitSize;
    D wrapped;

    abstract D makeDiskData();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractMixedHttpData(long limitSize, String baseDir, boolean deleteOnExit, D initial) {
        this.limitSize = limitSize;
        this.wrapped = initial;
        this.baseDir = baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long getMaxSize() {
        return this.wrapped.getMaxSize();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setMaxSize(long maxSize) {
        this.wrapped.setMaxSize(maxSize);
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        return this.wrapped.content();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void checkSize(long newSize) throws IOException {
        this.wrapped.checkSize(newSize);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long definedLength() {
        return this.wrapped.definedLength();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public Charset getCharset() {
        return this.wrapped.getCharset();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public String getName() {
        return this.wrapped.getName();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        if (this.wrapped instanceof AbstractMemoryHttpData) {
            try {
                checkSize(this.wrapped.length() + buffer.readableBytes());
                if (this.wrapped.length() + buffer.readableBytes() > this.limitSize) {
                    D diskData = makeDiskData();
                    ByteBuf data = ((AbstractMemoryHttpData) this.wrapped).getByteBuf();
                    if (data != null && data.isReadable()) {
                        diskData.addContent(data.retain(), false);
                    }
                    this.wrapped.release();
                    this.wrapped = diskData;
                }
            } catch (IOException e) {
                buffer.release();
                throw e;
            }
        }
        this.wrapped.addContent(buffer, last);
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        delete();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void delete() {
        this.wrapped.delete();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public byte[] get() throws IOException {
        return this.wrapped.get();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getByteBuf() throws IOException {
        return this.wrapped.getByteBuf();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString() throws IOException {
        return this.wrapped.getString();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString(Charset encoding) throws IOException {
        return this.wrapped.getString(encoding);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isInMemory() {
        return this.wrapped.isInMemory();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public long length() {
        return this.wrapped.length();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean renameTo(File dest) throws IOException {
        return this.wrapped.renameTo(dest);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setCharset(Charset charset) {
        this.wrapped.setCharset(charset);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(ByteBuf buffer) throws IOException {
        try {
            checkSize(buffer.readableBytes());
            if (buffer.readableBytes() > this.limitSize && (this.wrapped instanceof AbstractMemoryHttpData)) {
                this.wrapped.release();
                this.wrapped = makeDiskData();
            }
            this.wrapped.setContent(buffer);
        } catch (IOException e) {
            buffer.release();
            throw e;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(File file) throws IOException {
        checkSize(file.length());
        if (file.length() > this.limitSize && (this.wrapped instanceof AbstractMemoryHttpData)) {
            this.wrapped.release();
            this.wrapped = makeDiskData();
        }
        this.wrapped.setContent(file);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(InputStream inputStream) throws IOException {
        if (this.wrapped instanceof AbstractMemoryHttpData) {
            this.wrapped.release();
            this.wrapped = makeDiskData();
        }
        this.wrapped.setContent(inputStream);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isCompleted() {
        return this.wrapped.isCompleted();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.wrapped.getHttpDataType();
    }

    public int hashCode() {
        return this.wrapped.hashCode();
    }

    public boolean equals(Object obj) {
        return this.wrapped.equals(obj);
    }

    @Override // java.lang.Comparable
    public int compareTo(InterfaceHttpData o) {
        return this.wrapped.compareTo(o);
    }

    public String toString() {
        return "Mixed: " + this.wrapped;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getChunk(int length) throws IOException {
        return this.wrapped.getChunk(length);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public File getFile() throws IOException {
        return this.wrapped.getFile();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public D copy() {
        return (D) this.wrapped.copy();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public D duplicate() {
        return (D) this.wrapped.duplicate();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public D retainedDuplicate() {
        return (D) this.wrapped.retainedDuplicate();
    }

    @Override // io.netty.buffer.ByteBufHolder
    public D replace(ByteBuf byteBuf) {
        return (D) this.wrapped.replace(byteBuf);
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public D touch() {
        this.wrapped.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public D touch(Object hint) {
        this.wrapped.touch(hint);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public D retain() {
        return (D) super.retain();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public D retain(int increment) {
        return (D) super.retain(increment);
    }
}
