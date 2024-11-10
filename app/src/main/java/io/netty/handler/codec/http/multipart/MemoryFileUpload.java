package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class MemoryFileUpload extends AbstractMemoryHttpData implements FileUpload {
    private String contentTransferEncoding;
    private String contentType;
    private String filename;

    public MemoryFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
        super(name, charset, size);
        setFilename(filename);
        setContentType(contentType);
        setContentTransferEncoding(contentTransferEncoding);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpData
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.FileUpload;
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getFilename() {
        return this.filename;
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setFilename(String filename) {
        this.filename = (String) ObjectUtil.checkNotNull(filename, "filename");
    }

    public int hashCode() {
        return FileUploadUtil.hashCode(this);
    }

    public boolean equals(Object o) {
        return (o instanceof FileUpload) && FileUploadUtil.equals(this, (FileUpload) o);
    }

    @Override // java.lang.Comparable
    public int compareTo(InterfaceHttpData o) {
        if (!(o instanceof FileUpload)) {
            throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
        }
        return compareTo((FileUpload) o);
    }

    public int compareTo(FileUpload o) {
        return FileUploadUtil.compareTo(this, o);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setContentType(String contentType) {
        this.contentType = (String) ObjectUtil.checkNotNull(contentType, "contentType");
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getContentType() {
        return this.contentType;
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getContentTransferEncoding() {
        return this.contentTransferEncoding;
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String toString() {
        return ((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.FORM_DATA) + "; " + ((Object) HttpHeaderValues.NAME) + "=\"" + getName() + "\"; " + ((Object) HttpHeaderValues.FILENAME) + "=\"" + this.filename + "\"\r\n" + ((Object) HttpHeaderNames.CONTENT_TYPE) + ": " + this.contentType + (getCharset() != null ? "; " + ((Object) HttpHeaderValues.CHARSET) + '=' + getCharset().name() + "\r\n" : "\r\n") + ((Object) HttpHeaderNames.CONTENT_LENGTH) + ": " + length() + "\r\nCompleted: " + isCompleted() + "\r\nIsInMemory: " + isInMemory();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload copy() {
        ByteBuf content = content();
        return replace(content != null ? content.copy() : content);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload duplicate() {
        ByteBuf content = content();
        return replace(content != null ? content.duplicate() : content);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload retainedDuplicate() {
        ByteBuf content = content();
        if (content != null) {
            ByteBuf content2 = content.retainedDuplicate();
            boolean success = false;
            try {
                FileUpload duplicate = replace(content2);
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
    public FileUpload replace(ByteBuf content) {
        MemoryFileUpload upload = new MemoryFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
        if (content != null) {
            try {
                upload.setContent(content);
            } catch (IOException e) {
                throw new ChannelException(e);
            }
        }
        upload.setCompleted(isCompleted());
        return upload;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMemoryHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMemoryHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.ReferenceCounted
    public FileUpload touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
