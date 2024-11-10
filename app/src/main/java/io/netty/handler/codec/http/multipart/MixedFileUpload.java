package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class MixedFileUpload extends AbstractMixedHttpData<FileUpload> implements FileUpload {
    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void addContent(ByteBuf byteBuf, boolean z) throws IOException {
        super.addContent(byteBuf, z);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void checkSize(long j) throws IOException {
        super.checkSize(j);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData
    public /* bridge */ /* synthetic */ int compareTo(InterfaceHttpData interfaceHttpData) {
        return super.compareTo(interfaceHttpData);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.buffer.ByteBufHolder
    public /* bridge */ /* synthetic */ ByteBuf content() {
        return super.content();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ long definedLength() {
        return super.definedLength();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void delete() {
        super.delete();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ byte[] get() throws IOException {
        return super.get();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ ByteBuf getByteBuf() throws IOException {
        return super.getByteBuf();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ Charset getCharset() {
        return super.getCharset();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ ByteBuf getChunk(int i) throws IOException {
        return super.getChunk(i);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ File getFile() throws IOException {
        return super.getFile();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.InterfaceHttpData
    public /* bridge */ /* synthetic */ InterfaceHttpData.HttpDataType getHttpDataType() {
        return super.getHttpDataType();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ long getMaxSize() {
        return super.getMaxSize();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.InterfaceHttpData
    public /* bridge */ /* synthetic */ String getName() {
        return super.getName();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ String getString() throws IOException {
        return super.getString();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ String getString(Charset charset) throws IOException {
        return super.getString(charset);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ boolean isCompleted() {
        return super.isCompleted();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ boolean isInMemory() {
        return super.isInMemory();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ long length() {
        return super.length();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ boolean renameTo(File file) throws IOException {
        return super.renameTo(file);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void setCharset(Charset charset) {
        super.setCharset(charset);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void setContent(ByteBuf byteBuf) throws IOException {
        super.setContent(byteBuf);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void setContent(File file) throws IOException {
        super.setContent(file);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void setContent(InputStream inputStream) throws IOException {
        super.setContent(inputStream);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData
    public /* bridge */ /* synthetic */ void setMaxSize(long j) {
        super.setMaxSize(j);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public MixedFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size, long limitSize) {
        this(name, filename, contentType, contentTransferEncoding, charset, size, limitSize, DiskFileUpload.baseDirectory, DiskFileUpload.deleteOnExitTemporaryFile);
    }

    public MixedFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size, long limitSize, String baseDir, boolean deleteOnExit) {
        super(limitSize, baseDir, deleteOnExit, size > limitSize ? new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, baseDir, deleteOnExit) : new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size));
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getContentTransferEncoding() {
        return ((FileUpload) this.wrapped).getContentTransferEncoding();
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getFilename() {
        return ((FileUpload) this.wrapped).getFilename();
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setContentTransferEncoding(String contentTransferEncoding) {
        ((FileUpload) this.wrapped).setContentTransferEncoding(contentTransferEncoding);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setFilename(String filename) {
        ((FileUpload) this.wrapped).setFilename(filename);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public void setContentType(String contentType) {
        ((FileUpload) this.wrapped).setContentType(contentType);
    }

    @Override // io.netty.handler.codec.http.multipart.FileUpload
    public String getContentType() {
        return ((FileUpload) this.wrapped).getContentType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData
    public FileUpload makeDiskData() {
        DiskFileUpload diskFileUpload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), definedLength(), this.baseDir, this.deleteOnExit);
        diskFileUpload.setMaxSize(getMaxSize());
        return diskFileUpload;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload copy() {
        return (FileUpload) super.copy();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload duplicate() {
        return (FileUpload) super.duplicate();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload retainedDuplicate() {
        return (FileUpload) super.retainedDuplicate();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public FileUpload replace(ByteBuf content) {
        return (FileUpload) super.replace(content);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload touch() {
        return (FileUpload) super.touch();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.ReferenceCounted
    public FileUpload touch(Object hint) {
        return (FileUpload) super.touch(hint);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload retain() {
        return (FileUpload) super.retain();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public FileUpload retain(int increment) {
        return (FileUpload) super.retain(increment);
    }
}
