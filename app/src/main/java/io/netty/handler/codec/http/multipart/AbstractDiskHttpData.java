package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes4.dex */
public abstract class AbstractDiskHttpData extends AbstractHttpData {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) AbstractDiskHttpData.class);
    private File file;
    private FileChannel fileChannel;
    private boolean isRenamed;

    protected abstract boolean deleteOnExit();

    protected abstract String getBaseDirectory();

    protected abstract String getDiskFilename();

    protected abstract String getPostfix();

    protected abstract String getPrefix();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDiskHttpData(String name, Charset charset, long size) {
        super(name, charset, size);
    }

    private File tempFile() throws IOException {
        String newpostfix;
        File tmpFile;
        String diskFilename = getDiskFilename();
        if (diskFilename != null) {
            newpostfix = '_' + Integer.toString(diskFilename.hashCode());
        } else {
            newpostfix = getPostfix();
        }
        if (getBaseDirectory() == null) {
            tmpFile = PlatformDependent.createTempFile(getPrefix(), newpostfix, null);
        } else {
            tmpFile = PlatformDependent.createTempFile(getPrefix(), newpostfix, new File(getBaseDirectory()));
        }
        if (deleteOnExit()) {
            DeleteFileOnExitHook.add(tmpFile.getPath());
        }
        return tmpFile;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(ByteBuf buffer) throws IOException {
        ObjectUtil.checkNotNull(buffer, "buffer");
        try {
            this.size = buffer.readableBytes();
            checkSize(this.size);
            if (this.definedSize > 0 && this.definedSize < this.size) {
                throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
            }
            if (this.file == null) {
                this.file = tempFile();
            }
            if (buffer.readableBytes() == 0) {
                if (!this.file.createNewFile()) {
                    if (this.file.length() == 0) {
                        return;
                    }
                    if (!this.file.delete() || !this.file.createNewFile()) {
                        throw new IOException("file exists already: " + this.file);
                    }
                }
                return;
            }
            RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
            try {
                accessFile.setLength(0L);
                FileChannel localfileChannel = accessFile.getChannel();
                ByteBuffer byteBuffer = buffer.nioBuffer();
                int written = 0;
                while (written < this.size) {
                    written += localfileChannel.write(byteBuffer);
                }
                buffer.readerIndex(buffer.readerIndex() + written);
                localfileChannel.force(false);
                accessFile.close();
                setCompleted();
            } catch (Throwable th) {
                accessFile.close();
                throw th;
            }
        } finally {
            buffer.release();
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        if (buffer != null) {
            try {
                int localsize = buffer.readableBytes();
                checkSize(this.size + localsize);
                if (this.definedSize > 0 && this.definedSize < this.size + localsize) {
                    throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
                }
                if (this.file == null) {
                    this.file = tempFile();
                }
                if (this.fileChannel == null) {
                    RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
                    this.fileChannel = accessFile.getChannel();
                }
                int remaining = localsize;
                long position = this.fileChannel.position();
                int index = buffer.readerIndex();
                while (remaining > 0) {
                    int written = buffer.getBytes(index, this.fileChannel, position, remaining);
                    if (written < 0) {
                        break;
                    }
                    remaining -= written;
                    position += written;
                    index += written;
                }
                this.fileChannel.position(position);
                buffer.readerIndex(index);
                this.size += localsize - remaining;
            } finally {
                buffer.release();
            }
        }
        if (last) {
            if (this.file == null) {
                this.file = tempFile();
            }
            if (this.fileChannel == null) {
                RandomAccessFile accessFile2 = new RandomAccessFile(this.file, "rw");
                this.fileChannel = accessFile2.getChannel();
            }
            try {
                this.fileChannel.force(false);
                this.fileChannel.close();
                this.fileChannel = null;
                setCompleted();
                return;
            } catch (Throwable th) {
                this.fileChannel.close();
                throw th;
            }
        }
        ObjectUtil.checkNotNull(buffer, "buffer");
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(File file) throws IOException {
        long size = file.length();
        checkSize(size);
        this.size = size;
        if (this.file != null) {
            delete();
        }
        this.file = file;
        this.isRenamed = true;
        setCompleted();
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void setContent(InputStream inputStream) throws IOException {
        ObjectUtil.checkNotNull(inputStream, "inputStream");
        if (this.file != null) {
            delete();
        }
        this.file = tempFile();
        RandomAccessFile accessFile = new RandomAccessFile(this.file, "rw");
        int written = 0;
        try {
            accessFile.setLength(0L);
            FileChannel localfileChannel = accessFile.getChannel();
            byte[] bytes = new byte[16384];
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            int read = inputStream.read(bytes);
            while (read > 0) {
                byteBuffer.position(read).flip();
                written += localfileChannel.write(byteBuffer);
                checkSize(written);
                byteBuffer.clear();
                read = inputStream.read(bytes);
            }
            localfileChannel.force(false);
            accessFile.close();
            this.size = written;
            if (this.definedSize > 0 && this.definedSize < this.size) {
                if (!this.file.delete()) {
                    logger.warn("Failed to delete: {}", this.file);
                }
                this.file = null;
                throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
            }
            this.isRenamed = true;
            setCompleted();
        } catch (Throwable th) {
            accessFile.close();
            throw th;
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:30:0x0014 -> B:23:0x002a). Please report as a decompilation issue!!! */
    @Override // io.netty.handler.codec.http.multipart.HttpData
    public void delete() {
        try {
            try {
            } catch (Throwable th) {
                try {
                    this.fileChannel.close();
                } catch (IOException e) {
                    logger.warn("Failed to close a file.", (Throwable) e);
                }
                throw th;
            }
        } catch (IOException e2) {
            logger.warn("Failed to close a file.", (Throwable) e2);
        }
        if (this.fileChannel != null) {
            try {
                this.fileChannel.force(false);
                this.fileChannel.close();
            } catch (IOException e3) {
                logger.warn("Failed to force.", (Throwable) e3);
                this.fileChannel.close();
            }
            this.fileChannel = null;
        }
        if (!this.isRenamed) {
            String filePath = null;
            if (this.file != null && this.file.exists()) {
                filePath = this.file.getPath();
                if (!this.file.delete()) {
                    filePath = null;
                    logger.warn("Failed to delete: {}", this.file);
                }
            }
            if (deleteOnExit() && filePath != null) {
                DeleteFileOnExitHook.remove(filePath);
            }
            this.file = null;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public byte[] get() throws IOException {
        if (this.file == null) {
            return EmptyArrays.EMPTY_BYTES;
        }
        return readFrom(this.file);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public ByteBuf getByteBuf() throws IOException {
        if (this.file == null) {
            return Unpooled.EMPTY_BUFFER;
        }
        byte[] array = readFrom(this.file);
        return Unpooled.wrappedBuffer(array);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002b, code lost:            r5.fileChannel.close();        r5.fileChannel = null;     */
    @Override // io.netty.handler.codec.http.multipart.HttpData
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public io.netty.buffer.ByteBuf getChunk(int r6) throws java.io.IOException {
        /*
            r5 = this;
            java.io.File r0 = r5.file
            if (r0 == 0) goto L53
            if (r6 != 0) goto L7
            goto L53
        L7:
            java.nio.channels.FileChannel r0 = r5.fileChannel
            if (r0 != 0) goto L1a
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.io.File r1 = r5.file
            java.lang.String r2 = "r"
            r0.<init>(r1, r2)
            java.nio.channels.FileChannel r1 = r0.getChannel()
            r5.fileChannel = r1
        L1a:
            r0 = 0
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.allocate(r6)
        L1f:
            if (r0 >= r6) goto L3e
            r2 = 0
            java.nio.channels.FileChannel r3 = r5.fileChannel     // Catch: java.io.IOException -> L35
            int r3 = r3.read(r1)     // Catch: java.io.IOException -> L35
            r4 = -1
            if (r3 != r4) goto L33
            java.nio.channels.FileChannel r4 = r5.fileChannel     // Catch: java.io.IOException -> L35
            r4.close()     // Catch: java.io.IOException -> L35
            r5.fileChannel = r2     // Catch: java.io.IOException -> L35
            goto L3e
        L33:
            int r0 = r0 + r3
            goto L1f
        L35:
            r3 = move-exception
            java.nio.channels.FileChannel r4 = r5.fileChannel
            r4.close()
            r5.fileChannel = r2
            throw r3
        L3e:
            if (r0 != 0) goto L44
            io.netty.buffer.ByteBuf r2 = io.netty.buffer.Unpooled.EMPTY_BUFFER
            return r2
        L44:
            r1.flip()
            io.netty.buffer.ByteBuf r2 = io.netty.buffer.Unpooled.wrappedBuffer(r1)
            r3 = 0
            r2.readerIndex(r3)
            r2.writerIndex(r0)
            return r2
        L53:
            io.netty.buffer.ByteBuf r0 = io.netty.buffer.Unpooled.EMPTY_BUFFER
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.multipart.AbstractDiskHttpData.getChunk(int):io.netty.buffer.ByteBuf");
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString() throws IOException {
        return getString(HttpConstants.DEFAULT_CHARSET);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public String getString(Charset encoding) throws IOException {
        if (this.file == null) {
            return "";
        }
        if (encoding == null) {
            byte[] array = readFrom(this.file);
            return new String(array, HttpConstants.DEFAULT_CHARSET.name());
        }
        byte[] array2 = readFrom(this.file);
        return new String(array2, encoding.name());
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public boolean isInMemory() {
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00e5  */
    @Override // io.netty.handler.codec.http.multipart.HttpData
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean renameTo(java.io.File r19) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 243
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.multipart.AbstractDiskHttpData.renameTo(java.io.File):boolean");
    }

    private static byte[] readFrom(File src) throws IOException {
        long srcsize = src.length();
        if (srcsize > 2147483647L) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        RandomAccessFile accessFile = new RandomAccessFile(src, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
        byte[] array = new byte[(int) srcsize];
        try {
            FileChannel fileChannel = accessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(array);
            for (int read = 0; read < srcsize; read += fileChannel.read(byteBuffer)) {
            }
            return array;
        } finally {
            accessFile.close();
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpData
    public File getFile() throws IOException {
        return this.file;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HttpData touch() {
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.AbstractHttpData, io.netty.util.ReferenceCounted
    public HttpData touch(Object hint) {
        return this;
    }
}
