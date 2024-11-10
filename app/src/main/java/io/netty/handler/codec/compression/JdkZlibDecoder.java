package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

/* loaded from: classes4.dex */
public class JdkZlibDecoder extends ZlibDecoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int FCOMMENT = 16;
    private static final int FEXTRA = 4;
    private static final int FHCRC = 2;
    private static final int FNAME = 8;
    private static final int FRESERVED = 224;
    private final ByteBufChecksum crc;
    private boolean decideZlibOrNone;
    private final boolean decompressConcatenated;
    private final byte[] dictionary;
    private volatile boolean finished;
    private int flags;
    private GzipState gzipState;
    private Inflater inflater;
    private int xlen;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum GzipState {
        HEADER_START,
        HEADER_END,
        FLG_READ,
        XLEN_READ,
        SKIP_FNAME,
        SKIP_COMMENT,
        PROCESS_FHCRC,
        FOOTER_START
    }

    public JdkZlibDecoder() {
        this(ZlibWrapper.ZLIB, null, false, 0);
    }

    public JdkZlibDecoder(int maxAllocation) {
        this(ZlibWrapper.ZLIB, null, false, maxAllocation);
    }

    public JdkZlibDecoder(byte[] dictionary) {
        this(ZlibWrapper.ZLIB, dictionary, false, 0);
    }

    public JdkZlibDecoder(byte[] dictionary, int maxAllocation) {
        this(ZlibWrapper.ZLIB, dictionary, false, maxAllocation);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper) {
        this(wrapper, null, false, 0);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper, int maxAllocation) {
        this(wrapper, null, false, maxAllocation);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper, boolean decompressConcatenated) {
        this(wrapper, null, decompressConcatenated, 0);
    }

    public JdkZlibDecoder(ZlibWrapper wrapper, boolean decompressConcatenated, int maxAllocation) {
        this(wrapper, null, decompressConcatenated, maxAllocation);
    }

    public JdkZlibDecoder(boolean decompressConcatenated) {
        this(ZlibWrapper.GZIP, null, decompressConcatenated, 0);
    }

    public JdkZlibDecoder(boolean decompressConcatenated, int maxAllocation) {
        this(ZlibWrapper.GZIP, null, decompressConcatenated, maxAllocation);
    }

    private JdkZlibDecoder(ZlibWrapper wrapper, byte[] dictionary, boolean decompressConcatenated, int maxAllocation) {
        super(maxAllocation);
        this.gzipState = GzipState.HEADER_START;
        this.flags = -1;
        this.xlen = -1;
        ObjectUtil.checkNotNull(wrapper, "wrapper");
        this.decompressConcatenated = decompressConcatenated;
        switch (wrapper) {
            case GZIP:
                this.inflater = new Inflater(true);
                this.crc = ByteBufChecksum.wrapChecksum(new CRC32());
                break;
            case NONE:
                this.inflater = new Inflater(true);
                this.crc = null;
                break;
            case ZLIB:
                this.inflater = new Inflater();
                this.crc = null;
                break;
            case ZLIB_OR_NONE:
                this.decideZlibOrNone = true;
                this.crc = null;
                break;
            default:
                throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + wrapper);
        }
        this.dictionary = dictionary;
    }

    @Override // io.netty.handler.codec.compression.ZlibDecoder
    public boolean isClosed() {
        return this.finished;
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x010b, code lost:            r13.skipBytes(r0 - r11.inflater.getRemaining());     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0116, code lost:            if (r3 == false) goto L66;     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0118, code lost:            r11.gzipState = io.netty.handler.codec.compression.JdkZlibDecoder.GzipState.FOOTER_START;        handleGzipFooter(r13);     */
    /* JADX WARN: Code restructure failed: missing block: B:62:?, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x012d, code lost:            return;     */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r12, io.netty.buffer.ByteBuf r13, java.util.List<java.lang.Object> r14) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 327
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.JdkZlibDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    private boolean handleGzipFooter(ByteBuf in) {
        if (readGZIPFooter(in)) {
            this.finished = !this.decompressConcatenated;
            if (!this.finished) {
                this.inflater.reset();
                this.crc.reset();
                this.gzipState = GzipState.HEADER_START;
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // io.netty.handler.codec.compression.ZlibDecoder
    protected void decompressionBufferExhausted(ByteBuf buffer) {
        this.finished = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved0(ctx);
        if (this.inflater != null) {
            this.inflater.end();
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000f. Please report as an issue. */
    private boolean readGZIPHeader(ByteBuf in) {
        switch (this.gzipState) {
            case HEADER_START:
                if (in.readableBytes() < 10) {
                    return false;
                }
                int magic0 = in.readByte();
                int magic1 = in.readByte();
                if (magic0 != 31) {
                    throw new DecompressionException("Input is not in the GZIP format");
                }
                this.crc.update(magic0);
                this.crc.update(magic1);
                int method = in.readUnsignedByte();
                if (method != 8) {
                    throw new DecompressionException("Unsupported compression method " + method + " in the GZIP header");
                }
                this.crc.update(method);
                this.flags = in.readUnsignedByte();
                this.crc.update(this.flags);
                if ((this.flags & 224) != 0) {
                    throw new DecompressionException("Reserved flags are set in the GZIP header");
                }
                this.crc.update(in, in.readerIndex(), 4);
                in.skipBytes(4);
                this.crc.update(in.readUnsignedByte());
                this.crc.update(in.readUnsignedByte());
                this.gzipState = GzipState.FLG_READ;
            case FLG_READ:
                if ((this.flags & 4) != 0) {
                    if (in.readableBytes() < 2) {
                        return false;
                    }
                    int xlen1 = in.readUnsignedByte();
                    int xlen2 = in.readUnsignedByte();
                    this.crc.update(xlen1);
                    this.crc.update(xlen2);
                    this.xlen |= (xlen1 << 8) | xlen2;
                }
                this.gzipState = GzipState.XLEN_READ;
            case XLEN_READ:
                if (this.xlen != -1) {
                    if (in.readableBytes() < this.xlen) {
                        return false;
                    }
                    this.crc.update(in, in.readerIndex(), this.xlen);
                    in.skipBytes(this.xlen);
                }
                this.gzipState = GzipState.SKIP_FNAME;
            case SKIP_FNAME:
                if (!skipIfNeeded(in, 8)) {
                    return false;
                }
                this.gzipState = GzipState.SKIP_COMMENT;
            case SKIP_COMMENT:
                if (!skipIfNeeded(in, 16)) {
                    return false;
                }
                this.gzipState = GzipState.PROCESS_FHCRC;
            case PROCESS_FHCRC:
                if ((this.flags & 2) != 0 && !verifyCrc16(in)) {
                    return false;
                }
                this.crc.reset();
                this.gzipState = GzipState.HEADER_END;
                return true;
            case HEADER_END:
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    private boolean skipIfNeeded(ByteBuf in, int flagMask) {
        if ((this.flags & flagMask) == 0) {
            return true;
        }
        while (in.isReadable()) {
            int b = in.readUnsignedByte();
            this.crc.update(b);
            if (b == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean readGZIPFooter(ByteBuf in) {
        if (in.readableBytes() < 8) {
            return false;
        }
        boolean enoughData = verifyCrc(in);
        if (!enoughData) {
            throw new AssertionError();
        }
        int dataLength = 0;
        for (int i = 0; i < 4; i++) {
            dataLength |= in.readUnsignedByte() << (i * 8);
        }
        int readLength = this.inflater.getTotalOut();
        if (dataLength != readLength) {
            throw new DecompressionException("Number of bytes mismatch. Expected: " + dataLength + ", Got: " + readLength);
        }
        return true;
    }

    private boolean verifyCrc(ByteBuf in) {
        if (in.readableBytes() < 4) {
            return false;
        }
        long crcValue = 0;
        for (int i = 0; i < 4; i++) {
            crcValue |= in.readUnsignedByte() << (i * 8);
        }
        long readCrc = this.crc.getValue();
        if (crcValue != readCrc) {
            throw new DecompressionException("CRC value mismatch. Expected: " + crcValue + ", Got: " + readCrc);
        }
        return true;
    }

    private boolean verifyCrc16(ByteBuf in) {
        if (in.readableBytes() < 2) {
            return false;
        }
        long readCrc32 = this.crc.getValue();
        long crc16Value = 0;
        long readCrc16 = 0;
        for (int i = 0; i < 2; i++) {
            crc16Value |= in.readUnsignedByte() << (i * 8);
            readCrc16 |= ((readCrc32 >> (i * 8)) & 255) << (i * 8);
        }
        if (crc16Value != readCrc16) {
            throw new DecompressionException("CRC16 value mismatch. Expected: " + crc16Value + ", Got: " + readCrc16);
        }
        return true;
    }

    private static boolean looksLikeZlib(short cmf_flg) {
        return (cmf_flg & 30720) == 30720 && cmf_flg % 31 == 0;
    }
}
