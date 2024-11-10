package org.cloudburstmc.protocol.common.util;

import io.netty.util.concurrent.FastThreadLocal;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/* loaded from: classes5.dex */
public class Zlib {
    private static final int CHUNK = 8192;
    public static final Zlib DEFAULT = new Zlib(false);
    public static final Zlib RAW = new Zlib(true);
    private final FastThreadLocal<byte[]> chunkBytes = new FastThreadLocal<byte[]>() { // from class: org.cloudburstmc.protocol.common.util.Zlib.3
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public byte[] initialValue() {
            return new byte[8192];
        }
    };
    private final FastThreadLocal<Deflater> deflaterLocal;
    private final FastThreadLocal<Inflater> inflaterLocal;

    private Zlib(final boolean raw) {
        this.inflaterLocal = new FastThreadLocal<Inflater>() { // from class: org.cloudburstmc.protocol.common.util.Zlib.1
            @Override // io.netty.util.concurrent.FastThreadLocal
            public Inflater initialValue() {
                return new Inflater(raw);
            }
        };
        this.deflaterLocal = new FastThreadLocal<Deflater>() { // from class: org.cloudburstmc.protocol.common.util.Zlib.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.util.concurrent.FastThreadLocal
            public Deflater initialValue() {
                return new Deflater(7, raw);
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0046 A[Catch: all -> 0x00a4, DataFormatException -> 0x00a6, TryCatch #1 {DataFormatException -> 0x00a6, blocks: (B:3:0x0007, B:5:0x000e, B:7:0x0012, B:11:0x0029, B:13:0x0046, B:14:0x0067, B:15:0x006a, B:17:0x0070, B:19:0x0084, B:22:0x008b, B:28:0x0092, B:29:0x0099, B:40:0x005b, B:41:0x001e), top: B:2:0x0007, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0070 A[Catch: all -> 0x00a4, DataFormatException -> 0x00a6, TryCatch #1 {DataFormatException -> 0x00a6, blocks: (B:3:0x0007, B:5:0x000e, B:7:0x0012, B:11:0x0029, B:13:0x0046, B:14:0x0067, B:15:0x006a, B:17:0x0070, B:19:0x0084, B:22:0x008b, B:28:0x0092, B:29:0x0099, B:40:0x005b, B:41:0x001e), top: B:2:0x0007, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x005b A[Catch: all -> 0x00a4, DataFormatException -> 0x00a6, TryCatch #1 {DataFormatException -> 0x00a6, blocks: (B:3:0x0007, B:5:0x000e, B:7:0x0012, B:11:0x0029, B:13:0x0046, B:14:0x0067, B:15:0x006a, B:17:0x0070, B:19:0x0084, B:22:0x008b, B:28:0x0092, B:29:0x0099, B:40:0x005b, B:41:0x001e), top: B:2:0x0007, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public io.netty.buffer.ByteBuf inflate(io.netty.buffer.ByteBuf r9, int r10) throws java.util.zip.DataFormatException {
        /*
            r8 = this;
            r0 = 0
            io.netty.buffer.ByteBufAllocator r1 = io.netty.buffer.ByteBufAllocator.DEFAULT
            io.netty.buffer.ByteBuf r1 = r1.ioBuffer()
            boolean r2 = r9.isDirect()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r3 = 1
            if (r2 == 0) goto L1e
            boolean r2 = r9 instanceof io.netty.buffer.CompositeByteBuf     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r2 == 0) goto L1c
            r2 = r9
            io.netty.buffer.CompositeByteBuf r2 = (io.netty.buffer.CompositeByteBuf) r2     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r2 = r2.numComponents()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r2 <= r3) goto L1c
            goto L1e
        L1c:
            r0 = r9
            goto L29
        L1e:
            io.netty.buffer.ByteBufAllocator r2 = io.netty.buffer.ByteBufAllocator.DEFAULT     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            io.netty.buffer.ByteBuf r2 = r2.ioBuffer()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r2.writeBytes(r9)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r0 = r2
        L29:
            io.netty.util.concurrent.FastThreadLocal<java.util.zip.Inflater> r2 = r8.inflaterLocal     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            java.lang.Object r2 = r2.get()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            java.util.zip.Inflater r2 = (java.util.zip.Inflater) r2     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r2.reset()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r4 = r0.readerIndex()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r5 = r0.readableBytes()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            java.nio.ByteBuffer r4 = r0.internalNioBuffer(r4, r5)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            boolean r5 = r4.hasArray()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r5 == 0) goto L5b
            byte[] r5 = r4.array()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r6 = r4.arrayOffset()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r7 = r4.position()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r6 = r6 + r7
            int r7 = r4.remaining()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r2.setInput(r5, r6, r7)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            goto L67
        L5b:
            int r5 = r4.remaining()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            byte[] r5 = new byte[r5]     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r4.get(r5)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            r2.setInput(r5)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
        L67:
            r2.finished()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
        L6a:
            boolean r5 = r2.finished()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r5 != 0) goto L9b
            r5 = 8192(0x2000, float:1.148E-41)
            r1.ensureWritable(r5)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r6 = r1.writerIndex()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            java.nio.ByteBuffer r5 = r1.internalNioBuffer(r6, r5)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            int r5 = r8.inflate(r2, r5)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r5 >= r3) goto L84
            goto L9b
        L84:
            int r7 = r6 + r5
            r1.writerIndex(r7)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r10 <= 0) goto L9a
            int r7 = r1.writerIndex()     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            if (r7 >= r10) goto L92
            goto L9a
        L92:
            java.util.zip.DataFormatException r3 = new java.util.zip.DataFormatException     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            java.lang.String r7 = "Inflated data exceeds maximum size"
            r3.<init>(r7)     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
            throw r3     // Catch: java.lang.Throwable -> La4 java.util.zip.DataFormatException -> La6
        L9a:
            goto L6a
        L9b:
            if (r0 == 0) goto La3
            if (r0 == r9) goto La3
            r0.release()
        La3:
            return r1
        La4:
            r2 = move-exception
            goto Lac
        La6:
            r2 = move-exception
            r1.release()     // Catch: java.lang.Throwable -> La4
            throw r2     // Catch: java.lang.Throwable -> La4
        Lac:
            if (r0 == 0) goto Lb3
            if (r0 == r9) goto Lb3
            r0.release()
        Lb3:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.protocol.common.util.Zlib.inflate(io.netty.buffer.ByteBuf, int):io.netty.buffer.ByteBuf");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0029 A[Catch: all -> 0x00a5, TryCatch #0 {all -> 0x00a5, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x000c, B:11:0x0023, B:13:0x0029, B:14:0x0032, B:16:0x0052, B:18:0x0073, B:20:0x0079, B:23:0x0093, B:35:0x0067, B:37:0x0019), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0052 A[Catch: all -> 0x00a5, TryCatch #0 {all -> 0x00a5, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x000c, B:11:0x0023, B:13:0x0029, B:14:0x0032, B:16:0x0052, B:18:0x0073, B:20:0x0079, B:23:0x0093, B:35:0x0067, B:37:0x0019), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0079 A[Catch: all -> 0x00a5, LOOP:0: B:18:0x0073->B:20:0x0079, LOOP_END, TryCatch #0 {all -> 0x00a5, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x000c, B:11:0x0023, B:13:0x0029, B:14:0x0032, B:16:0x0052, B:18:0x0073, B:20:0x0079, B:23:0x0093, B:35:0x0067, B:37:0x0019), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0093 A[Catch: all -> 0x00a5, TRY_LEAVE, TryCatch #0 {all -> 0x00a5, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x000c, B:11:0x0023, B:13:0x0029, B:14:0x0032, B:16:0x0052, B:18:0x0073, B:20:0x0079, B:23:0x0093, B:35:0x0067, B:37:0x0019), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0067 A[Catch: all -> 0x00a5, TryCatch #0 {all -> 0x00a5, blocks: (B:3:0x0002, B:5:0x0008, B:7:0x000c, B:11:0x0023, B:13:0x0029, B:14:0x0032, B:16:0x0052, B:18:0x0073, B:20:0x0079, B:23:0x0093, B:35:0x0067, B:37:0x0019), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void deflate(io.netty.buffer.ByteBuf r8, io.netty.buffer.ByteBuf r9, int r10) throws java.util.zip.DataFormatException {
        /*
            r7 = this;
            r0 = 0
            r1 = 0
            boolean r2 = r8.isDirect()     // Catch: java.lang.Throwable -> La5
            if (r2 == 0) goto L19
            boolean r2 = r8 instanceof io.netty.buffer.CompositeByteBuf     // Catch: java.lang.Throwable -> La5
            if (r2 == 0) goto L17
            r2 = r8
            io.netty.buffer.CompositeByteBuf r2 = (io.netty.buffer.CompositeByteBuf) r2     // Catch: java.lang.Throwable -> La5
            int r2 = r2.numComponents()     // Catch: java.lang.Throwable -> La5
            r3 = 1
            if (r2 <= r3) goto L17
            goto L19
        L17:
            r1 = r8
            goto L23
        L19:
            io.netty.buffer.ByteBufAllocator r2 = io.netty.buffer.ByteBufAllocator.DEFAULT     // Catch: java.lang.Throwable -> La5
            io.netty.buffer.ByteBuf r2 = r2.ioBuffer()     // Catch: java.lang.Throwable -> La5
            r1 = r2
            r1.writeBytes(r8)     // Catch: java.lang.Throwable -> La5
        L23:
            boolean r2 = r9.isDirect()     // Catch: java.lang.Throwable -> La5
            if (r2 != 0) goto L31
            io.netty.buffer.ByteBufAllocator r2 = io.netty.buffer.ByteBufAllocator.DEFAULT     // Catch: java.lang.Throwable -> La5
            io.netty.buffer.ByteBuf r2 = r2.ioBuffer()     // Catch: java.lang.Throwable -> La5
            r0 = r2
            goto L32
        L31:
            r0 = r9
        L32:
            io.netty.util.concurrent.FastThreadLocal<java.util.zip.Deflater> r2 = r7.deflaterLocal     // Catch: java.lang.Throwable -> La5
            java.lang.Object r2 = r2.get()     // Catch: java.lang.Throwable -> La5
            java.util.zip.Deflater r2 = (java.util.zip.Deflater) r2     // Catch: java.lang.Throwable -> La5
            r2.reset()     // Catch: java.lang.Throwable -> La5
            r2.setLevel(r10)     // Catch: java.lang.Throwable -> La5
            int r3 = r1.readerIndex()     // Catch: java.lang.Throwable -> La5
            int r4 = r1.readableBytes()     // Catch: java.lang.Throwable -> La5
            java.nio.ByteBuffer r3 = r1.internalNioBuffer(r3, r4)     // Catch: java.lang.Throwable -> La5
            boolean r4 = r3.hasArray()     // Catch: java.lang.Throwable -> La5
            if (r4 == 0) goto L67
            byte[] r4 = r3.array()     // Catch: java.lang.Throwable -> La5
            int r5 = r3.arrayOffset()     // Catch: java.lang.Throwable -> La5
            int r6 = r3.position()     // Catch: java.lang.Throwable -> La5
            int r5 = r5 + r6
            int r6 = r3.remaining()     // Catch: java.lang.Throwable -> La5
            r2.setInput(r4, r5, r6)     // Catch: java.lang.Throwable -> La5
            goto L73
        L67:
            int r4 = r3.remaining()     // Catch: java.lang.Throwable -> La5
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> La5
            r3.get(r4)     // Catch: java.lang.Throwable -> La5
            r2.setInput(r4)     // Catch: java.lang.Throwable -> La5
        L73:
            boolean r4 = r2.finished()     // Catch: java.lang.Throwable -> La5
            if (r4 != 0) goto L91
            int r4 = r0.writerIndex()     // Catch: java.lang.Throwable -> La5
            r5 = 8192(0x2000, float:1.148E-41)
            r0.ensureWritable(r5)     // Catch: java.lang.Throwable -> La5
            java.nio.ByteBuffer r5 = r0.internalNioBuffer(r4, r5)     // Catch: java.lang.Throwable -> La5
            int r5 = r7.deflate(r2, r5)     // Catch: java.lang.Throwable -> La5
            int r6 = r4 + r5
            r0.writerIndex(r6)     // Catch: java.lang.Throwable -> La5
            goto L73
        L91:
            if (r0 == r9) goto L96
            r9.writeBytes(r0)     // Catch: java.lang.Throwable -> La5
        L96:
            if (r1 == 0) goto L9d
            if (r1 == r8) goto L9d
            r1.release()
        L9d:
            if (r0 == 0) goto La4
            if (r0 == r9) goto La4
            r0.release()
        La4:
            return
        La5:
            r2 = move-exception
            if (r1 == 0) goto Lad
            if (r1 == r8) goto Lad
            r1.release()
        Lad:
            if (r0 == 0) goto Lb4
            if (r0 == r9) goto Lb4
            r0.release()
        Lb4:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.protocol.common.util.Zlib.deflate(io.netty.buffer.ByteBuf, io.netty.buffer.ByteBuf, int):void");
    }

    private int inflate(Inflater inflater, ByteBuffer output) throws DataFormatException {
        if (output.hasArray()) {
            return inflater.inflate(output.array(), output.arrayOffset() + output.position(), output.remaining());
        }
        int startPos = output.position();
        byte[] chunkBytes = this.chunkBytes.get();
        while (output.remaining() > 0 && !inflater.finished()) {
            int length = Math.min(output.remaining(), 8192);
            int result = inflater.inflate(chunkBytes, 0, length);
            output.put(chunkBytes, 0, result);
        }
        return output.position() - startPos;
    }

    private int deflate(Deflater deflater, ByteBuffer output) {
        deflater.finish();
        if (output.hasArray()) {
            return deflater.deflate(output.array(), output.arrayOffset() + output.position(), output.remaining());
        }
        int startPos = output.position();
        byte[] chunkBytes = this.chunkBytes.get();
        while (output.remaining() > 0 && !deflater.finished()) {
            int length = Math.min(output.remaining(), 8192);
            int result = deflater.deflate(chunkBytes, 0, length);
            output.put(chunkBytes, 0, result);
        }
        return output.position() - startPos;
    }
}
