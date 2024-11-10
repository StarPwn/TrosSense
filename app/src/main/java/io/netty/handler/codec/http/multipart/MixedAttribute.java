package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* loaded from: classes4.dex */
public class MixedAttribute extends AbstractMixedHttpData<Attribute> implements Attribute {
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

    public MixedAttribute(String name, long limitSize) {
        this(name, limitSize, HttpConstants.DEFAULT_CHARSET);
    }

    public MixedAttribute(String name, long definedSize, long limitSize) {
        this(name, definedSize, limitSize, HttpConstants.DEFAULT_CHARSET);
    }

    public MixedAttribute(String name, long limitSize, Charset charset) {
        this(name, limitSize, charset, DiskAttribute.baseDirectory, DiskAttribute.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        this(name, 0L, limitSize, charset, baseDir, deleteOnExit);
    }

    public MixedAttribute(String name, long definedSize, long limitSize, Charset charset) {
        this(name, definedSize, limitSize, charset, DiskAttribute.baseDirectory, DiskAttribute.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, long definedSize, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        super(limitSize, baseDir, deleteOnExit, new MemoryAttribute(name, definedSize, charset));
    }

    public MixedAttribute(String name, String value, long limitSize) {
        this(name, value, limitSize, HttpConstants.DEFAULT_CHARSET, DiskAttribute.baseDirectory, DiskFileUpload.deleteOnExitTemporaryFile);
    }

    public MixedAttribute(String name, String value, long limitSize, Charset charset) {
        this(name, value, limitSize, charset, DiskAttribute.baseDirectory, DiskFileUpload.deleteOnExitTemporaryFile);
    }

    private static Attribute makeInitialAttributeFromValue(String name, String value, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        if (value.length() > limitSize) {
            try {
                return new DiskAttribute(name, value, charset, baseDir, deleteOnExit);
            } catch (IOException e) {
                try {
                    return new MemoryAttribute(name, value, charset);
                } catch (IOException e2) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        try {
            return new MemoryAttribute(name, value, charset);
        } catch (IOException e3) {
            throw new IllegalArgumentException(e3);
        }
    }

    public MixedAttribute(String name, String value, long limitSize, Charset charset, String baseDir, boolean deleteOnExit) {
        super(limitSize, baseDir, deleteOnExit, makeInitialAttributeFromValue(name, value, limitSize, charset, baseDir, deleteOnExit));
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public String getValue() throws IOException {
        return ((Attribute) this.wrapped).getValue();
    }

    @Override // io.netty.handler.codec.http.multipart.Attribute
    public void setValue(String value) throws IOException {
        ((Attribute) this.wrapped).setValue(value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData
    public Attribute makeDiskData() {
        DiskAttribute diskAttribute = new DiskAttribute(getName(), definedLength(), this.baseDir, this.deleteOnExit);
        diskAttribute.setMaxSize(getMaxSize());
        return diskAttribute;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute copy() {
        return (Attribute) super.copy();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute duplicate() {
        return (Attribute) super.duplicate();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute replace(ByteBuf content) {
        return (Attribute) super.replace(content);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute retain() {
        return (Attribute) super.retain();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute retain(int increment) {
        return (Attribute) super.retain(increment);
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.handler.codec.http.multipart.HttpData, io.netty.buffer.ByteBufHolder
    public Attribute retainedDuplicate() {
        return (Attribute) super.retainedDuplicate();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public Attribute touch() {
        return (Attribute) super.touch();
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractMixedHttpData, io.netty.util.ReferenceCounted
    public Attribute touch(Object hint) {
        return (Attribute) super.touch(hint);
    }
}
