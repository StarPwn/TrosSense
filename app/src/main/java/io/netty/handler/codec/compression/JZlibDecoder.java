package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerId;

/* loaded from: classes4.dex */
public class JZlibDecoder extends ZlibDecoder {
    private byte[] dictionary;
    private volatile boolean finished;
    private final Inflater z;

    public JZlibDecoder() {
        this(ZlibWrapper.ZLIB, 0);
    }

    public JZlibDecoder(int maxAllocation) {
        this(ZlibWrapper.ZLIB, maxAllocation);
    }

    public JZlibDecoder(ZlibWrapper wrapper) {
        this(wrapper, 0);
    }

    public JZlibDecoder(ZlibWrapper wrapper, int maxAllocation) {
        super(maxAllocation);
        this.z = new Inflater();
        ObjectUtil.checkNotNull(wrapper, "wrapper");
        int resultCode = this.z.init(ZlibUtil.convertWrapperType(wrapper));
        if (resultCode != 0) {
            ZlibUtil.fail(this.z, "initialization failure", resultCode);
        }
    }

    public JZlibDecoder(byte[] dictionary) {
        this(dictionary, 0);
    }

    public JZlibDecoder(byte[] dictionary, int maxAllocation) {
        super(maxAllocation);
        this.z = new Inflater();
        this.dictionary = (byte[]) ObjectUtil.checkNotNull(dictionary, "dictionary");
        int resultCode = this.z.inflateInit(JZlib.W_ZLIB);
        if (resultCode != 0) {
            ZlibUtil.fail(this.z, "initialization failure", resultCode);
        }
    }

    @Override // io.netty.handler.codec.compression.ZlibDecoder
    public boolean isClosed() {
        return this.finished;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:20:0x0094. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (this.finished) {
            in.skipBytes(in.readableBytes());
            return;
        }
        int inputLength = in.readableBytes();
        if (inputLength == 0) {
            return;
        }
        try {
            this.z.avail_in = inputLength;
            if (in.hasArray()) {
                this.z.next_in = in.array();
                this.z.next_in_index = in.arrayOffset() + in.readerIndex();
            } else {
                byte[] array = new byte[inputLength];
                in.getBytes(in.readerIndex(), array);
                this.z.next_in = array;
                this.z.next_in_index = 0;
            }
            int oldNextInIndex = this.z.next_in_index;
            ByteBuf decompressed = prepareDecompressBuffer(ctx, null, inputLength << 1);
            while (true) {
                try {
                    decompressed = prepareDecompressBuffer(ctx, decompressed, this.z.avail_in << 1);
                    this.z.avail_out = decompressed.writableBytes();
                    this.z.next_out = decompressed.array();
                    this.z.next_out_index = decompressed.arrayOffset() + decompressed.writerIndex();
                    int oldNextOutIndex = this.z.next_out_index;
                    int resultCode = this.z.inflate(2);
                    int outputLength = this.z.next_out_index - oldNextOutIndex;
                    if (outputLength > 0) {
                        decompressed.writerIndex(decompressed.writerIndex() + outputLength);
                    }
                    switch (resultCode) {
                        case ContainerId.CRAFTING_USE_INGREDIENT /* -5 */:
                            if (this.z.avail_in <= 0) {
                                break;
                            }
                        case 0:
                        case 1:
                            this.finished = true;
                            this.z.inflateEnd();
                            break;
                        case 2:
                            if (this.dictionary == null) {
                                ZlibUtil.fail(this.z, "decompression failure", resultCode);
                            } else {
                                int resultCode2 = this.z.inflateSetDictionary(this.dictionary, this.dictionary.length);
                                if (resultCode2 != 0) {
                                    ZlibUtil.fail(this.z, "failed to set the dictionary", resultCode2);
                                }
                            }
                        default:
                            ZlibUtil.fail(this.z, "decompression failure", resultCode);
                    }
                } finally {
                    in.skipBytes(this.z.next_in_index - oldNextInIndex);
                    if (decompressed.isReadable()) {
                        out.add(decompressed);
                    } else {
                        decompressed.release();
                    }
                }
            }
        } finally {
            this.z.next_in = null;
            this.z.next_out = null;
        }
    }

    @Override // io.netty.handler.codec.compression.ZlibDecoder
    protected void decompressionBufferExhausted(ByteBuf buffer) {
        this.finished = true;
    }
}
