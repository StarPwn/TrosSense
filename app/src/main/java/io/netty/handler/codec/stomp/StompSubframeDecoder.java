package io.netty.handler.codec.stomp;

import com.google.common.base.Ascii;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import okio.Utf8;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
public class StompSubframeDecoder extends ReplayingDecoder<State> {
    private static final int DEFAULT_CHUNK_SIZE = 8132;
    private static final int DEFAULT_MAX_LINE_LENGTH = 1024;
    private int alreadyReadChunkSize;
    private final Utf8LineParser commandParser;
    private long contentLength;
    private final HeaderParser headerParser;
    private LastStompContentSubframe lastContent;
    private final int maxChunkSize;

    /* loaded from: classes4.dex */
    public enum State {
        SKIP_CONTROL_CHARACTERS,
        READ_HEADERS,
        READ_CONTENT,
        FINALIZE_FRAME_READ,
        BAD_FRAME,
        INVALID_CHUNK
    }

    public StompSubframeDecoder() {
        this(1024, DEFAULT_CHUNK_SIZE);
    }

    public StompSubframeDecoder(boolean validateHeaders) {
        this(1024, DEFAULT_CHUNK_SIZE, validateHeaders);
    }

    public StompSubframeDecoder(int maxLineLength, int maxChunkSize) {
        this(maxLineLength, maxChunkSize, false);
    }

    public StompSubframeDecoder(int maxLineLength, int maxChunkSize, boolean validateHeaders) {
        super(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        ObjectUtil.checkPositive(maxLineLength, "maxLineLength");
        ObjectUtil.checkPositive(maxChunkSize, "maxChunkSize");
        this.maxChunkSize = maxChunkSize;
        this.commandParser = new Utf8LineParser(new AppendableCharSequence(16), maxLineLength);
        this.headerParser = new HeaderParser(new AppendableCharSequence(128), maxLineLength, validateHeaders);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0067 A[Catch: Exception -> 0x0116, TryCatch #1 {Exception -> 0x0116, blocks: (B:17:0x0054, B:18:0x0062, B:21:0x0067, B:24:0x006e, B:26:0x0072, B:27:0x0075, B:29:0x007d, B:32:0x0087, B:34:0x009b, B:35:0x00a8, B:37:0x00b1, B:39:0x00c4, B:41:0x00cc, B:42:0x00dd, B:44:0x00ec, B:45:0x010c, B:47:0x00d3, B:48:0x00f8, B:50:0x00ff, B:51:0x0103), top: B:16:0x0054 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ff A[Catch: Exception -> 0x0116, TryCatch #1 {Exception -> 0x0116, blocks: (B:17:0x0054, B:18:0x0062, B:21:0x0067, B:24:0x006e, B:26:0x0072, B:27:0x0075, B:29:0x007d, B:32:0x0087, B:34:0x009b, B:35:0x00a8, B:37:0x00b1, B:39:0x00c4, B:41:0x00cc, B:42:0x00dd, B:44:0x00ec, B:45:0x010c, B:47:0x00d3, B:48:0x00f8, B:50:0x00ff, B:51:0x0103), top: B:16:0x0054 }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r8, io.netty.buffer.ByteBuf r9, java.util.List<java.lang.Object> r10) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 332
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.stomp.StompSubframeDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    private StompCommand readCommand(ByteBuf in) {
        CharSequence commandSequence = this.commandParser.parse(in);
        if (commandSequence == null) {
            throw new DecoderException("Failed to read command from channel");
        }
        String commandStr = commandSequence.toString();
        try {
            return StompCommand.valueOf(commandStr);
        } catch (IllegalArgumentException e) {
            throw new DecoderException("Cannot to parse command " + commandStr);
        }
    }

    private State readHeaders(ByteBuf buffer, StompHeadersSubframe headersSubframe) {
        boolean headerRead;
        StompHeaders headers = headersSubframe.headers();
        do {
            headerRead = this.headerParser.parseHeader(headersSubframe, buffer);
        } while (headerRead);
        if (headers.contains(StompHeaders.CONTENT_LENGTH)) {
            this.contentLength = getContentLength(headers);
            if (this.contentLength == 0) {
                return State.FINALIZE_FRAME_READ;
            }
        }
        return State.READ_CONTENT;
    }

    private static long getContentLength(StompHeaders headers) {
        long contentLength = headers.getLong(StompHeaders.CONTENT_LENGTH, 0L);
        if (contentLength < 0) {
            throw new DecoderException(((Object) StompHeaders.CONTENT_LENGTH) + " must be non-negative");
        }
        return contentLength;
    }

    private static void skipNullCharacter(ByteBuf buffer) {
        byte b = buffer.readByte();
        if (b != 0) {
            throw new IllegalStateException("unexpected byte in buffer " + ((int) b) + " while expecting NULL byte");
        }
    }

    private static void skipControlCharacters(ByteBuf buffer) {
        while (true) {
            byte b = buffer.readByte();
            if (b != 13 && b != 10) {
                buffer.readerIndex(buffer.readerIndex() - 1);
                return;
            }
        }
    }

    private void resetDecoder() {
        checkpoint(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        this.alreadyReadChunkSize = 0;
        this.lastContent = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class Utf8LineParser implements ByteProcessor {
        private final AppendableCharSequence charSeq;
        private char interim;
        private int lineLength;
        private final int maxLineLength;
        private boolean nextRead;

        Utf8LineParser(AppendableCharSequence charSeq, int maxLineLength) {
            this.charSeq = (AppendableCharSequence) ObjectUtil.checkNotNull(charSeq, "charSeq");
            this.maxLineLength = maxLineLength;
        }

        AppendableCharSequence parse(ByteBuf byteBuf) {
            reset();
            int offset = byteBuf.forEachByte(this);
            if (offset == -1) {
                return null;
            }
            byteBuf.readerIndex(offset + 1);
            return this.charSeq;
        }

        AppendableCharSequence charSequence() {
            return this.charSeq;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte nextByte) throws Exception {
            if (nextByte == 13) {
                this.lineLength++;
                return true;
            }
            if (nextByte == 10) {
                return false;
            }
            int i = this.lineLength + 1;
            this.lineLength = i;
            if (i > this.maxLineLength) {
                throw new TooLongFrameException("An STOMP line is larger than " + this.maxLineLength + " bytes.");
            }
            if (this.nextRead) {
                this.interim = (char) (this.interim | ((nextByte & Utf8.REPLACEMENT_BYTE) << 6));
                this.nextRead = false;
            } else if (this.interim != 0) {
                appendTo(this.charSeq, (char) (this.interim | (nextByte & Utf8.REPLACEMENT_BYTE)));
                this.interim = (char) 0;
            } else if (nextByte >= 0) {
                appendTo(this.charSeq, (char) nextByte);
            } else if ((nextByte & MessagePack.Code.NEGFIXINT_PREFIX) == 192) {
                this.interim = (char) ((nextByte & Ascii.US) << 6);
            } else {
                this.interim = (char) ((nextByte & 15) << 12);
                this.nextRead = true;
            }
            return true;
        }

        protected void appendTo(AppendableCharSequence charSeq, char chr) {
            charSeq.append(chr);
        }

        protected void reset() {
            this.charSeq.reset();
            this.lineLength = 0;
            this.interim = (char) 0;
            this.nextRead = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class HeaderParser extends Utf8LineParser {
        private String name;
        private boolean shouldUnescape;
        private boolean unescapeInProgress;
        private boolean valid;
        private final boolean validateHeaders;

        HeaderParser(AppendableCharSequence charSeq, int maxLineLength, boolean validateHeaders) {
            super(charSeq, maxLineLength);
            this.validateHeaders = validateHeaders;
        }

        boolean parseHeader(StompHeadersSubframe headersSubframe, ByteBuf buf) {
            this.shouldUnescape = shouldUnescape(headersSubframe.command());
            AppendableCharSequence value = super.parse(buf);
            if (value == null) {
                return false;
            }
            if (this.name == null && value.length() == 0) {
                return false;
            }
            if (this.valid) {
                headersSubframe.headers().add((StompHeaders) this.name, value.toString());
                return true;
            }
            if (this.validateHeaders) {
                if (StringUtil.isNullOrEmpty(this.name)) {
                    throw new IllegalArgumentException("received an invalid header line '" + ((Object) value) + '\'');
                }
                String line = this.name + ':' + ((Object) value);
                throw new IllegalArgumentException("a header value or name contains a prohibited character ':', " + line);
            }
            return true;
        }

        @Override // io.netty.handler.codec.stomp.StompSubframeDecoder.Utf8LineParser, io.netty.util.ByteProcessor
        public boolean process(byte nextByte) throws Exception {
            if (nextByte == 58) {
                if (this.name == null) {
                    AppendableCharSequence charSeq = charSequence();
                    if (charSeq.length() != 0) {
                        this.name = charSeq.substring(0, charSeq.length());
                        charSeq.reset();
                        this.valid = true;
                        return true;
                    }
                    this.name = "";
                } else {
                    this.valid = false;
                }
            }
            return super.process(nextByte);
        }

        @Override // io.netty.handler.codec.stomp.StompSubframeDecoder.Utf8LineParser
        protected void appendTo(AppendableCharSequence charSeq, char chr) {
            if (!this.shouldUnescape) {
                super.appendTo(charSeq, chr);
                return;
            }
            if (chr == '\\') {
                if (this.unescapeInProgress) {
                    super.appendTo(charSeq, chr);
                    this.unescapeInProgress = false;
                    return;
                } else {
                    this.unescapeInProgress = true;
                    return;
                }
            }
            if (this.unescapeInProgress) {
                if (chr == 'c') {
                    charSeq.append(':');
                } else if (chr == 'r') {
                    charSeq.append(StringUtil.CARRIAGE_RETURN);
                } else if (chr == 'n') {
                    charSeq.append('\n');
                } else {
                    charSeq.append('\\').append(chr);
                    throw new IllegalArgumentException("received an invalid escape header sequence '" + ((Object) charSeq) + '\'');
                }
                this.unescapeInProgress = false;
                return;
            }
            super.appendTo(charSeq, chr);
        }

        @Override // io.netty.handler.codec.stomp.StompSubframeDecoder.Utf8LineParser
        protected void reset() {
            this.name = null;
            this.valid = false;
            this.unescapeInProgress = false;
            super.reset();
        }

        private static boolean shouldUnescape(StompCommand command) {
            return (command == StompCommand.CONNECT || command == StompCommand.CONNECTED) ? false : true;
        }
    }
}
