package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import java.util.List;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
public final class RedisDecoder extends ByteToMessageDecoder {
    private final boolean decodeInlineCommands;
    private final int maxInlineMessageLength;
    private final RedisMessagePool messagePool;
    private int remainingBulkLength;
    private State state;
    private final ToPositiveLongProcessor toPositiveLongProcessor;
    private RedisMessageType type;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum State {
        DECODE_TYPE,
        DECODE_INLINE,
        DECODE_LENGTH,
        DECODE_BULK_STRING_EOL,
        DECODE_BULK_STRING_CONTENT
    }

    public RedisDecoder() {
        this(false);
    }

    public RedisDecoder(boolean decodeInlineCommands) {
        this(65536, FixedRedisMessagePool.INSTANCE, decodeInlineCommands);
    }

    public RedisDecoder(int maxInlineMessageLength, RedisMessagePool messagePool) {
        this(maxInlineMessageLength, messagePool, false);
    }

    public RedisDecoder(int maxInlineMessageLength, RedisMessagePool messagePool, boolean decodeInlineCommands) {
        this.toPositiveLongProcessor = new ToPositiveLongProcessor();
        this.state = State.DECODE_TYPE;
        if (maxInlineMessageLength <= 0 || maxInlineMessageLength > 536870912) {
            throw new RedisCodecException("maxInlineMessageLength: " + maxInlineMessageLength + " (expected: <= 536870912)");
        }
        this.maxInlineMessageLength = maxInlineMessageLength;
        this.messagePool = messagePool;
        this.decodeInlineCommands = decodeInlineCommands;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:3:0x000b. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (true) {
            try {
                switch (this.state) {
                    case DECODE_TYPE:
                        if (!decodeType(in)) {
                            return;
                        }
                    case DECODE_INLINE:
                        if (!decodeInline(in, out)) {
                            return;
                        }
                    case DECODE_LENGTH:
                        if (!decodeLength(in, out)) {
                            return;
                        }
                    case DECODE_BULK_STRING_EOL:
                        if (!decodeBulkStringEndOfLine(in, out)) {
                            return;
                        }
                    case DECODE_BULK_STRING_CONTENT:
                        if (!decodeBulkStringContent(in, out)) {
                            return;
                        }
                    default:
                        throw new RedisCodecException("Unknown state: " + this.state);
                }
            } catch (RedisCodecException e) {
                resetDecoder();
                throw e;
            } catch (Exception e2) {
                resetDecoder();
                throw new RedisCodecException(e2);
            }
        }
    }

    private void resetDecoder() {
        this.state = State.DECODE_TYPE;
        this.remainingBulkLength = 0;
    }

    private boolean decodeType(ByteBuf in) throws Exception {
        if (!in.isReadable()) {
            return false;
        }
        this.type = RedisMessageType.readFrom(in, this.decodeInlineCommands);
        this.state = this.type.isInline() ? State.DECODE_INLINE : State.DECODE_LENGTH;
        return true;
    }

    private boolean decodeInline(ByteBuf in, List<Object> out) throws Exception {
        ByteBuf lineBytes = readLine(in);
        if (lineBytes == null) {
            if (in.readableBytes() > this.maxInlineMessageLength) {
                throw new RedisCodecException("length: " + in.readableBytes() + " (expected: <= " + this.maxInlineMessageLength + ")");
            }
            return false;
        }
        out.add(newInlineRedisMessage(this.type, lineBytes));
        resetDecoder();
        return true;
    }

    private boolean decodeLength(ByteBuf in, List<Object> out) throws Exception {
        ByteBuf lineByteBuf = readLine(in);
        if (lineByteBuf == null) {
            return false;
        }
        long length = parseRedisNumber(lineByteBuf);
        if (length < -1) {
            throw new RedisCodecException("length: " + length + " (expected: >= -1)");
        }
        switch (this.type) {
            case ARRAY_HEADER:
                out.add(new ArrayHeaderRedisMessage(length));
                resetDecoder();
                return true;
            case BULK_STRING:
                if (length > 536870912) {
                    throw new RedisCodecException("length: " + length + " (expected: <= 536870912)");
                }
                this.remainingBulkLength = (int) length;
                return decodeBulkString(in, out);
            default:
                throw new RedisCodecException("bad type: " + this.type);
        }
    }

    private boolean decodeBulkString(ByteBuf in, List<Object> out) throws Exception {
        switch (this.remainingBulkLength) {
            case -1:
                out.add(FullBulkStringRedisMessage.NULL_INSTANCE);
                resetDecoder();
                return true;
            case 0:
                this.state = State.DECODE_BULK_STRING_EOL;
                return decodeBulkStringEndOfLine(in, out);
            default:
                out.add(new BulkStringHeaderRedisMessage(this.remainingBulkLength));
                this.state = State.DECODE_BULK_STRING_CONTENT;
                return decodeBulkStringContent(in, out);
        }
    }

    private boolean decodeBulkStringEndOfLine(ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 2) {
            return false;
        }
        readEndOfLine(in);
        out.add(FullBulkStringRedisMessage.EMPTY_INSTANCE);
        resetDecoder();
        return true;
    }

    private boolean decodeBulkStringContent(ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes == 0) {
            return false;
        }
        if (this.remainingBulkLength == 0 && readableBytes < 2) {
            return false;
        }
        if (readableBytes >= this.remainingBulkLength + 2) {
            ByteBuf content = in.readSlice(this.remainingBulkLength);
            readEndOfLine(in);
            out.add(new DefaultLastBulkStringRedisContent(content.retain()));
            resetDecoder();
            return true;
        }
        int toRead = Math.min(this.remainingBulkLength, readableBytes);
        this.remainingBulkLength -= toRead;
        out.add(new DefaultBulkStringRedisContent(in.readSlice(toRead).retain()));
        return true;
    }

    private static void readEndOfLine(ByteBuf in) {
        short delim = in.readShort();
        if (RedisConstants.EOL_SHORT == delim) {
            return;
        }
        byte[] bytes = RedisCodecUtil.shortToBytes(delim);
        throw new RedisCodecException("delimiter: [" + ((int) bytes[0]) + "," + ((int) bytes[1]) + "] (expected: \\r\\n)");
    }

    private RedisMessage newInlineRedisMessage(RedisMessageType messageType, ByteBuf content) {
        switch (messageType) {
            case INLINE_COMMAND:
                return new InlineCommandRedisMessage(content.toString(CharsetUtil.UTF_8));
            case SIMPLE_STRING:
                SimpleStringRedisMessage cached = this.messagePool.getSimpleString(content);
                return cached != null ? cached : new SimpleStringRedisMessage(content.toString(CharsetUtil.UTF_8));
            case ERROR:
                ErrorRedisMessage cached2 = this.messagePool.getError(content);
                return cached2 != null ? cached2 : new ErrorRedisMessage(content.toString(CharsetUtil.UTF_8));
            case INTEGER:
                IntegerRedisMessage cached3 = this.messagePool.getInteger(content);
                return cached3 != null ? cached3 : new IntegerRedisMessage(parseRedisNumber(content));
            default:
                throw new RedisCodecException("bad type: " + messageType);
        }
    }

    private static ByteBuf readLine(ByteBuf in) {
        int lfIndex;
        if (!in.isReadable(2) || (lfIndex = in.indexOf(in.readerIndex(), in.writerIndex(), (byte) 10)) < 0) {
            return null;
        }
        ByteBuf data = in.readSlice((lfIndex - in.readerIndex()) - 1);
        readEndOfLine(in);
        return data;
    }

    private long parseRedisNumber(ByteBuf byteBuf) {
        int readableBytes = byteBuf.readableBytes();
        boolean negative = readableBytes > 0 && byteBuf.getByte(byteBuf.readerIndex()) == 45;
        int extraOneByteForNegative = negative ? 1 : 0;
        if (readableBytes <= extraOneByteForNegative) {
            throw new RedisCodecException("no number to parse: " + byteBuf.toString(CharsetUtil.US_ASCII));
        }
        if (readableBytes > extraOneByteForNegative + 19) {
            throw new RedisCodecException("too many characters to be a valid RESP Integer: " + byteBuf.toString(CharsetUtil.US_ASCII));
        }
        if (negative) {
            return -parsePositiveNumber(byteBuf.skipBytes(extraOneByteForNegative));
        }
        return parsePositiveNumber(byteBuf);
    }

    private long parsePositiveNumber(ByteBuf byteBuf) {
        this.toPositiveLongProcessor.reset();
        byteBuf.forEachByte(this.toPositiveLongProcessor);
        return this.toPositiveLongProcessor.content();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class ToPositiveLongProcessor implements ByteProcessor {
        private long result;

        private ToPositiveLongProcessor() {
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            if (value < 48 || value > 57) {
                throw new RedisCodecException("bad byte in number: " + ((int) value));
            }
            this.result = (this.result * 10) + value + MessagePack.Code.INT8;
            return true;
        }

        public long content() {
            return this.result;
        }

        public void reset() {
            this.result = 0L;
        }
    }
}
