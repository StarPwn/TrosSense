package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.internal.ObjectUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class DiskFileUpload extends AbstractDiskHttpData implements FileUpload {
    public static String baseDirectory = null;
    public static boolean deleteOnExitTemporaryFile = true;
    public static final String postfix = ".tmp";
    public static final String prefix = "FUp_";
    private final String baseDir;
    private String contentTransferEncoding;
    private String contentType;
    private final boolean deleteOnExit;
    private String filename;

    public DiskFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size, String baseDir, boolean deleteOnExit) {
        super(name, charset, size);
        setFilename(filename);
        setContentType(contentType);
        setContentTransferEncoding(contentTransferEncoding);
        this.baseDir = baseDir == null ? baseDirectory : baseDir;
        this.deleteOnExit = deleteOnExit;
    }

    public DiskFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
        this(name, filename, contentType, contentTransferEncoding, charset, size, baseDirectory, deleteOnExitTemporaryFile);
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
        File file = null;
        try {
            file = getFile();
        } catch (IOException e) {
        }
        return ((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.FORM_DATA) + "; " + ((Object) HttpHeaderValues.NAME) + "=\"" + getName() + "\"; " + ((Object) HttpHeaderValues.FILENAME) + "=\"" + this.filename + "\"\r\n" + ((Object) HttpHeaderNames.CONTENT_TYPE) + ": " + this.contentType + (getCharset() != null ? "; " + ((Object) HttpHeaderValues.CHARSET) + '=' + getCharset().name() + "\r\n" : "\r\n") + ((Object) HttpHeaderNames.CONTENT_LENGTH) + ": " + length() + "\r\nCompleted: " + isCompleted() + "\r\nIsInMemory: " + isInMemory() + "\r\nRealFile: " + (file != null ? file.getAbsolutePath() : "null") + " DeleteAfter: " + this.deleteOnExit;
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
        return "upload";
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
    public FileUpload copy() {
        ByteBuf content = content();
        return replace(content != null ? content.copy() : null);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload duplicate() {
        ByteBuf content = content();
        return replace(content != null ? content.duplicate() : null);
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
        DiskFileUpload upload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size, this.baseDir, this.deleteOnExit);
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
    public FileUpload retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload retain() {
        super.retain();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload touch() {
        super.touch();
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractDiskHttpData, io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.ReferenceCounted
    public FileUpload touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
