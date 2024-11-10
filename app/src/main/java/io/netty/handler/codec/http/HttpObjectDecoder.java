package io.netty.handler.codec.http;

import com.trossense.bl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes4.dex */
public abstract class HttpObjectDecoder extends ByteToMessageDecoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final boolean DEFAULT_ALLOW_DUPLICATE_CONTENT_LENGTHS = false;
    public static final boolean DEFAULT_ALLOW_PARTIAL_CHUNKS = true;
    public static final boolean DEFAULT_CHUNKED_SUPPORTED = true;
    public static final int DEFAULT_INITIAL_BUFFER_SIZE = 128;
    public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
    public static final int DEFAULT_MAX_HEADER_SIZE = 8192;
    public static final int DEFAULT_MAX_INITIAL_LINE_LENGTH = 4096;
    public static final boolean DEFAULT_VALIDATE_HEADERS = true;
    private static final boolean[] ISO_CONTROL_OR_WHITESPACE;
    private static final boolean[] LATIN_WHITESPACE;
    private static final ByteProcessor SKIP_CONTROL_CHARS_BYTES;
    private static final boolean[] SP_LENIENT_BYTES = new boolean[256];
    private final boolean allowDuplicateContentLengths;
    private final boolean allowPartialChunks;
    private long chunkSize;
    private boolean chunked;
    private final boolean chunkedSupported;
    private long contentLength;
    private State currentState;
    private final HeaderParser headerParser;
    protected final HttpHeadersFactory headersFactory;
    private boolean isSwitchingToNonHttp1Protocol;
    private final LineParser lineParser;
    private final int maxChunkSize;
    private HttpMessage message;
    private AsciiString name;
    private final ByteBuf parserScratchBuffer;
    private final AtomicBoolean resetRequested;
    private LastHttpContent trailer;
    protected final HttpHeadersFactory trailersFactory;

    @Deprecated
    protected final boolean validateHeaders;
    private String value;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum State {
        SKIP_CONTROL_CHARS,
        READ_INITIAL,
        READ_HEADER,
        READ_VARIABLE_LENGTH_CONTENT,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_DELIMITER,
        READ_CHUNK_FOOTER,
        BAD_MESSAGE,
        UPGRADED
    }

    protected abstract HttpMessage createInvalidMessage();

    protected abstract HttpMessage createMessage(String[] strArr) throws Exception;

    protected abstract boolean isDecodingRequest();

    static {
        SP_LENIENT_BYTES[160] = true;
        SP_LENIENT_BYTES[137] = true;
        SP_LENIENT_BYTES[139] = true;
        SP_LENIENT_BYTES[140] = true;
        SP_LENIENT_BYTES[141] = true;
        LATIN_WHITESPACE = new boolean[256];
        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b = (byte) (b + 1)) {
            LATIN_WHITESPACE[b + 128] = Character.isWhitespace(b);
        }
        ISO_CONTROL_OR_WHITESPACE = new boolean[256];
        for (byte b2 = Byte.MIN_VALUE; b2 < Byte.MAX_VALUE; b2 = (byte) (b2 + 1)) {
            ISO_CONTROL_OR_WHITESPACE[b2 + 128] = Character.isISOControl(b2) || isWhitespace(b2);
        }
        SKIP_CONTROL_CHARS_BYTES = new ByteProcessor() { // from class: io.netty.handler.codec.http.HttpObjectDecoder.1
            @Override // io.netty.util.ByteProcessor
            public boolean process(byte value) {
                return HttpObjectDecoder.ISO_CONTROL_OR_WHITESPACE[value + 128];
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        try {
            this.parserScratchBuffer.release();
        } finally {
            super.handlerRemoved0(ctx);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpObjectDecoder() {
        this(new HttpDecoderConfig());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setChunkedSupported(chunkedSupported));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setChunkedSupported(chunkedSupported).setValidateHeaders(validateHeaders));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setChunkedSupported(chunkedSupported).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setChunkedSupported(chunkedSupported).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize).setAllowDuplicateContentLengths(allowDuplicateContentLengths));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public HttpObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean chunkedSupported, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths, boolean allowPartialChunks) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize).setChunkedSupported(chunkedSupported).setValidateHeaders(validateHeaders).setInitialBufferSize(initialBufferSize).setAllowDuplicateContentLengths(allowDuplicateContentLengths).setAllowPartialChunks(allowPartialChunks));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HttpObjectDecoder(HttpDecoderConfig config) {
        this.contentLength = Long.MIN_VALUE;
        this.resetRequested = new AtomicBoolean();
        this.currentState = State.SKIP_CONTROL_CHARS;
        ObjectUtil.checkNotNull(config, "config");
        this.parserScratchBuffer = Unpooled.buffer(config.getInitialBufferSize());
        this.lineParser = new LineParser(this.parserScratchBuffer, config.getMaxInitialLineLength());
        this.headerParser = new HeaderParser(this.parserScratchBuffer, config.getMaxHeaderSize());
        this.maxChunkSize = config.getMaxChunkSize();
        this.chunkedSupported = config.isChunkedSupported();
        this.headersFactory = config.getHeadersFactory();
        this.trailersFactory = config.getTrailersFactory();
        this.validateHeaders = isValidating(this.headersFactory);
        this.allowDuplicateContentLengths = config.isAllowDuplicateContentLengths();
        this.allowPartialChunks = config.isAllowPartialChunks();
    }

    protected boolean isValidating(HttpHeadersFactory headersFactory) {
        if (!(headersFactory instanceof DefaultHttpHeadersFactory)) {
            return true;
        }
        DefaultHttpHeadersFactory builder = (DefaultHttpHeadersFactory) headersFactory;
        return builder.isValidatingHeaderNames() || builder.isValidatingHeaderValues();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0017. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d8  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0130 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0165 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0166 A[Catch: Exception -> 0x01cc, TryCatch #1 {Exception -> 0x01cc, blocks: (B:70:0x015f, B:73:0x0166, B:74:0x0170, B:75:0x0173, B:78:0x0196, B:80:0x019e, B:83:0x01a5, B:85:0x01a9, B:88:0x01ae, B:89:0x01b3, B:90:0x01b4, B:92:0x01bb, B:95:0x01c0, B:97:0x0176, B:99:0x017a, B:101:0x017e, B:102:0x0185, B:103:0x0186), top: B:69:0x015f }] */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void decode(io.netty.channel.ChannelHandlerContext r10, io.netty.buffer.ByteBuf r11, java.util.List<java.lang.Object> r12) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 526
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.HttpObjectDecoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    public void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        boolean prematureClosure;
        super.decodeLast(ctx, in, out);
        if (this.resetRequested.get()) {
            resetNow();
        }
        switch (this.currentState) {
            case SKIP_CONTROL_CHARS:
            case READ_INITIAL:
            case BAD_MESSAGE:
            case UPGRADED:
                return;
            case READ_CHUNK_SIZE:
            case READ_FIXED_LENGTH_CONTENT:
            case READ_CHUNKED_CONTENT:
            case READ_CHUNK_DELIMITER:
            case READ_CHUNK_FOOTER:
                if (isDecodingRequest() || this.chunked) {
                    prematureClosure = true;
                } else {
                    prematureClosure = this.contentLength > 0;
                }
                if (!prematureClosure) {
                    out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                }
                resetNow();
                return;
            case READ_HEADER:
                out.add(invalidMessage(this.message, Unpooled.EMPTY_BUFFER, new PrematureChannelClosureException("Connection closed before received headers")));
                resetNow();
                return;
            case READ_VARIABLE_LENGTH_CONTENT:
                if (!this.chunked && !in.isReadable()) {
                    out.add(LastHttpContent.EMPTY_LAST_CONTENT);
                    resetNow();
                    return;
                }
                return;
            default:
                throw new IllegalStateException("Unhandled state " + this.currentState);
        }
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof HttpExpectationFailedEvent) {
            switch (this.currentState) {
                case READ_CHUNK_SIZE:
                case READ_VARIABLE_LENGTH_CONTENT:
                case READ_FIXED_LENGTH_CONTENT:
                    reset();
                    break;
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    private void addCurrentMessage(List<Object> out) {
        HttpMessage message = this.message;
        if (message == null) {
            throw new AssertionError();
        }
        this.message = null;
        out.add(message);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isContentAlwaysEmpty(HttpMessage msg) {
        if (!(msg instanceof HttpResponse)) {
            return false;
        }
        HttpResponse res = (HttpResponse) msg;
        HttpResponseStatus status = res.status();
        int code = status.code();
        HttpStatusClass statusClass = status.codeClass();
        if (statusClass == HttpStatusClass.INFORMATIONAL) {
            return (code == 101 && !res.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_ACCEPT) && res.headers().contains((CharSequence) HttpHeaderNames.UPGRADE, (CharSequence) HttpHeaderValues.WEBSOCKET, true)) ? false : true;
        }
        switch (code) {
            case bl.cC /* 204 */:
            case bl.ed /* 304 */:
                return true;
            default:
                return false;
        }
    }

    protected boolean isSwitchingToNonHttp1Protocol(HttpResponse msg) {
        if (msg.status().code() != HttpResponseStatus.SWITCHING_PROTOCOLS.code()) {
            return false;
        }
        String newProtocol = msg.headers().get(HttpHeaderNames.UPGRADE);
        return newProtocol == null || !(newProtocol.contains(HttpVersion.HTTP_1_0.text()) || newProtocol.contains(HttpVersion.HTTP_1_1.text()));
    }

    public void reset() {
        this.resetRequested.lazySet(true);
    }

    private void resetNow() {
        this.message = null;
        this.name = null;
        this.value = null;
        this.contentLength = Long.MIN_VALUE;
        this.chunked = false;
        this.lineParser.reset();
        this.headerParser.reset();
        this.trailer = null;
        if (!this.isSwitchingToNonHttp1Protocol) {
            this.resetRequested.lazySet(false);
            this.currentState = State.SKIP_CONTROL_CHARS;
        } else {
            this.isSwitchingToNonHttp1Protocol = false;
            this.currentState = State.UPGRADED;
        }
    }

    private HttpMessage invalidMessage(HttpMessage current, ByteBuf in, Exception cause) {
        this.currentState = State.BAD_MESSAGE;
        this.message = null;
        this.trailer = null;
        in.skipBytes(in.readableBytes());
        if (current == null) {
            current = createInvalidMessage();
        }
        current.setDecoderResult(DecoderResult.failure(cause));
        return current;
    }

    private HttpContent invalidChunk(ByteBuf in, Exception cause) {
        this.currentState = State.BAD_MESSAGE;
        this.message = null;
        this.trailer = null;
        in.skipBytes(in.readableBytes());
        HttpContent chunk = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
        chunk.setDecoderResult(DecoderResult.failure(cause));
        return chunk;
    }

    private State readHeaders(ByteBuf buffer) {
        HttpMessage message = this.message;
        HttpHeaders headers = message.headers();
        HeaderParser headerParser = this.headerParser;
        ByteBuf line = headerParser.parse(buffer);
        if (line == null) {
            return null;
        }
        int lineLength = line.readableBytes();
        while (lineLength > 0) {
            byte[] lineContent = line.array();
            int startLine = line.arrayOffset() + line.readerIndex();
            byte firstChar = lineContent[startLine];
            if (this.name != null && (firstChar == 32 || firstChar == 9)) {
                String trimmedLine = langAsciiString(lineContent, startLine, lineLength).trim();
                String valueStr = this.value;
                this.value = valueStr + ' ' + trimmedLine;
            } else {
                if (this.name != null) {
                    headers.add(this.name, this.value);
                }
                splitHeader(lineContent, startLine, lineLength);
            }
            line = headerParser.parse(buffer);
            if (line == null) {
                return null;
            }
            lineLength = line.readableBytes();
        }
        if (this.name != null) {
            headers.add(this.name, this.value);
        }
        this.name = null;
        this.value = null;
        HttpMessageDecoderResult decoderResult = new HttpMessageDecoderResult(this.lineParser.size, headerParser.size);
        message.setDecoderResult(decoderResult);
        List<String> contentLengthFields = headers.getAll(HttpHeaderNames.CONTENT_LENGTH);
        if (contentLengthFields.isEmpty()) {
            this.contentLength = HttpUtil.getWebSocketContentLength(message);
        } else {
            HttpVersion version = message.protocolVersion();
            boolean isHttp10OrEarlier = version.majorVersion() < 1 || (version.majorVersion() == 1 && version.minorVersion() == 0);
            this.contentLength = HttpUtil.normalizeAndGetContentLength(contentLengthFields, isHttp10OrEarlier, this.allowDuplicateContentLengths);
            if (this.contentLength != -1) {
                String lengthValue = contentLengthFields.get(0).trim();
                if (contentLengthFields.size() > 1 || !lengthValue.equals(Long.toString(this.contentLength))) {
                    headers.set(HttpHeaderNames.CONTENT_LENGTH, Long.valueOf(this.contentLength));
                }
            }
        }
        if (!isDecodingRequest() && (message instanceof HttpResponse)) {
            HttpResponse res = (HttpResponse) message;
            this.isSwitchingToNonHttp1Protocol = isSwitchingToNonHttp1Protocol(res);
        }
        if (isContentAlwaysEmpty(message)) {
            HttpUtil.setTransferEncodingChunked(message, false);
            return State.SKIP_CONTROL_CHARS;
        }
        if (!HttpUtil.isTransferEncodingChunked(message)) {
            if (this.contentLength >= 0) {
                return State.READ_FIXED_LENGTH_CONTENT;
            }
            return State.READ_VARIABLE_LENGTH_CONTENT;
        }
        this.chunked = true;
        if (!contentLengthFields.isEmpty() && message.protocolVersion() == HttpVersion.HTTP_1_1) {
            handleTransferEncodingChunkedWithContentLength(message);
        }
        return State.READ_CHUNK_SIZE;
    }

    protected void handleTransferEncodingChunkedWithContentLength(HttpMessage message) {
        message.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
        this.contentLength = Long.MIN_VALUE;
    }

    private LastHttpContent readTrailingHeaders(ByteBuf buffer) {
        HeaderParser headerParser = this.headerParser;
        ByteBuf line = headerParser.parse(buffer);
        if (line == null) {
            return null;
        }
        LastHttpContent trailer = this.trailer;
        int lineLength = line.readableBytes();
        if (lineLength == 0 && trailer == null) {
            return LastHttpContent.EMPTY_LAST_CONTENT;
        }
        CharSequence lastHeader = null;
        if (trailer == null) {
            DefaultLastHttpContent defaultLastHttpContent = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.trailersFactory);
            this.trailer = defaultLastHttpContent;
            trailer = defaultLastHttpContent;
        }
        while (lineLength > 0) {
            byte[] lineContent = line.array();
            int startLine = line.arrayOffset() + line.readerIndex();
            byte firstChar = lineContent[startLine];
            if (lastHeader != null && (firstChar == 32 || firstChar == 9)) {
                List<String> current = trailer.trailingHeaders().getAll(lastHeader);
                if (!current.isEmpty()) {
                    int lastPos = current.size() - 1;
                    String lineTrimmed = langAsciiString(lineContent, startLine, line.readableBytes()).trim();
                    String currentLastPos = current.get(lastPos);
                    current.set(lastPos, currentLastPos + lineTrimmed);
                }
            } else {
                splitHeader(lineContent, startLine, lineLength);
                AsciiString headerName = this.name;
                if (!HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(headerName) && !HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(headerName)) {
                    trailer.trailingHeaders().add(headerName, this.value);
                }
                lastHeader = this.name;
                this.name = null;
                this.value = null;
            }
            line = headerParser.parse(buffer);
            if (line == null) {
                return null;
            }
            lineLength = line.readableBytes();
        }
        this.trailer = null;
        return trailer;
    }

    private static int skipWhiteSpaces(byte[] hex, int start, int length) {
        for (int i = 0; i < length; i++) {
            if (!isWhitespace(hex[start + i])) {
                return i;
            }
        }
        return length;
    }

    private static int getChunkSize(byte[] hex, int start, int length) {
        int skipped = skipWhiteSpaces(hex, start, length);
        if (skipped == length) {
            throw new NumberFormatException();
        }
        int start2 = start + skipped;
        int length2 = length - skipped;
        int result = 0;
        for (int i = 0; i < length2; i++) {
            int digit = StringUtil.decodeHexNibble(hex[start2 + i]);
            if (digit == -1) {
                byte b = hex[start2 + i];
                if (b == 59 || isControlOrWhitespaceAsciiChar(b)) {
                    if (i == 0) {
                        throw new NumberFormatException("Empty chunk size");
                    }
                    return result;
                }
                throw new NumberFormatException("Invalid character in chunk size");
            }
            result = (result * 16) + digit;
            if (result < 0) {
                throw new NumberFormatException("Chunk size overflow: " + result);
            }
        }
        return result;
    }

    private String[] splitInitialLine(ByteBuf asciiBuffer) {
        byte[] asciiBytes = asciiBuffer.array();
        int arrayOffset = asciiBuffer.arrayOffset();
        int startContent = asciiBuffer.readerIndex() + arrayOffset;
        int end = asciiBuffer.readableBytes() + startContent;
        int aStart = findNonSPLenient(asciiBytes, startContent, end);
        int aEnd = findSPLenient(asciiBytes, aStart, end);
        int bStart = findNonSPLenient(asciiBytes, aEnd, end);
        int bEnd = findSPLenient(asciiBytes, bStart, end);
        int cStart = findNonSPLenient(asciiBytes, bEnd, end);
        int cEnd = findEndOfString(asciiBytes, Math.max(cStart - 1, startContent), end);
        String[] strArr = new String[3];
        strArr[0] = splitFirstWordInitialLine(asciiBytes, aStart, aEnd - aStart);
        strArr[1] = splitSecondWordInitialLine(asciiBytes, bStart, bEnd - bStart);
        strArr[2] = cStart < cEnd ? splitThirdWordInitialLine(asciiBytes, cStart, cEnd - cStart) : "";
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String splitFirstWordInitialLine(byte[] asciiContent, int start, int length) {
        return langAsciiString(asciiContent, start, length);
    }

    protected String splitSecondWordInitialLine(byte[] asciiContent, int start, int length) {
        return langAsciiString(asciiContent, start, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String splitThirdWordInitialLine(byte[] asciiContent, int start, int length) {
        return langAsciiString(asciiContent, start, length);
    }

    private static String langAsciiString(byte[] asciiContent, int start, int length) {
        if (length == 0) {
            return "";
        }
        if (start == 0) {
            if (length == asciiContent.length) {
                return new String(asciiContent, 0, 0, asciiContent.length);
            }
            return new String(asciiContent, 0, 0, length);
        }
        return new String(asciiContent, 0, start, length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:            r5 = r3;     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0022, code lost:            if (r5 >= r0) goto L31;     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0026, code lost:            if (r9[r5] != 58) goto L18;     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002b, code lost:            r5 = r5 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0028, code lost:            r5 = r5 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x002e, code lost:            r8.name = splitHeaderName(r9, r1, r3 - r1);        r4 = findNonWhitespace(r9, r5, r0);     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003a, code lost:            if (r4 != r0) goto L22;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003c, code lost:            r8.value = "";     */
    /* JADX WARN: Code restructure failed: missing block: B:21:?, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0041, code lost:            r6 = findEndOfString(r9, r10, r0);        r8.value = langAsciiString(r9, r4, r6 - r4);     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x004d, code lost:            return;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void splitHeader(byte[] r9, int r10, int r11) {
        /*
            r8 = this;
            int r0 = r10 + r11
            int r1 = findNonWhitespace(r9, r10, r0)
            boolean r2 = r8.isDecodingRequest()
            r3 = r1
        Lb:
            r4 = 58
            if (r3 >= r0) goto L1f
            r5 = r9[r3]
            if (r5 == r4) goto L1f
            if (r2 != 0) goto L1c
            boolean r6 = isOWS(r5)
            if (r6 == 0) goto L1c
            goto L1f
        L1c:
            int r3 = r3 + 1
            goto Lb
        L1f:
            if (r3 == r0) goto L4e
            r5 = r3
        L22:
            if (r5 >= r0) goto L2e
            r6 = r9[r5]
            if (r6 != r4) goto L2b
            int r5 = r5 + 1
            goto L2e
        L2b:
            int r5 = r5 + 1
            goto L22
        L2e:
            int r4 = r3 - r1
            io.netty.util.AsciiString r4 = r8.splitHeaderName(r9, r1, r4)
            r8.name = r4
            int r4 = findNonWhitespace(r9, r5, r0)
            if (r4 != r0) goto L41
            java.lang.String r6 = ""
            r8.value = r6
            goto L4d
        L41:
            int r6 = findEndOfString(r9, r10, r0)
            int r7 = r6 - r4
            java.lang.String r7 = langAsciiString(r9, r4, r7)
            r8.value = r7
        L4d:
            return
        L4e:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "No colon found"
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http.HttpObjectDecoder.splitHeader(byte[], int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AsciiString splitHeaderName(byte[] sb, int start, int length) {
        return new AsciiString(sb, start, length, true);
    }

    private static int findNonSPLenient(byte[] sb, int offset, int end) {
        for (int result = offset; result < end; result++) {
            byte c = sb[result];
            if (!isSPLenient(c)) {
                if (isWhitespace(c)) {
                    throw new IllegalArgumentException("Invalid separator");
                }
                return result;
            }
        }
        return end;
    }

    private static int findSPLenient(byte[] sb, int offset, int end) {
        for (int result = offset; result < end; result++) {
            if (isSPLenient(sb[result])) {
                return result;
            }
        }
        return end;
    }

    private static boolean isSPLenient(byte c) {
        return SP_LENIENT_BYTES[c + 128];
    }

    private static boolean isWhitespace(byte b) {
        return LATIN_WHITESPACE[b + 128];
    }

    private static int findNonWhitespace(byte[] sb, int offset, int end) {
        for (int result = offset; result < end; result++) {
            byte c = sb[result];
            if (!isWhitespace(c)) {
                return result;
            }
            if (!isOWS(c)) {
                throw new IllegalArgumentException("Invalid separator, only a single space or horizontal tab allowed, but received a '" + ((int) c) + "' (0x" + Integer.toHexString(c) + ")");
            }
        }
        return end;
    }

    private static int findEndOfString(byte[] sb, int start, int end) {
        for (int result = end - 1; result > start; result--) {
            if (!isWhitespace(sb[result])) {
                return result + 1;
            }
        }
        return 0;
    }

    private static boolean isOWS(byte ch) {
        return ch == 32 || ch == 9;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class HeaderParser {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        protected final int maxLength;
        protected final ByteBuf seq;
        int size;

        HeaderParser(ByteBuf seq, int maxLength) {
            this.seq = seq;
            this.maxLength = maxLength;
        }

        public ByteBuf parse(ByteBuf buffer) {
            int endOfSeqIncluded;
            int readableBytes = buffer.readableBytes();
            int readerIndex = buffer.readerIndex();
            int maxBodySize = this.maxLength - this.size;
            if (maxBodySize < 0) {
                throw new AssertionError();
            }
            long maxBodySizeWithCRLF = maxBodySize + 2;
            int toProcess = (int) Math.min(maxBodySizeWithCRLF, readableBytes);
            int toIndexExclusive = readerIndex + toProcess;
            if (toIndexExclusive < readerIndex) {
                throw new AssertionError();
            }
            int indexOfLf = buffer.indexOf(readerIndex, toIndexExclusive, (byte) 10);
            if (indexOfLf == -1) {
                if (readableBytes > maxBodySize) {
                    throw newException(this.maxLength);
                }
                return null;
            }
            if (indexOfLf > readerIndex && buffer.getByte(indexOfLf - 1) == 13) {
                endOfSeqIncluded = indexOfLf - 1;
            } else {
                endOfSeqIncluded = indexOfLf;
            }
            int newSize = endOfSeqIncluded - readerIndex;
            if (newSize == 0) {
                this.seq.clear();
                buffer.readerIndex(indexOfLf + 1);
                return this.seq;
            }
            int size = this.size + newSize;
            if (size > this.maxLength) {
                throw newException(this.maxLength);
            }
            this.size = size;
            this.seq.clear();
            this.seq.writeBytes(buffer, readerIndex, newSize);
            buffer.readerIndex(indexOfLf + 1);
            return this.seq;
        }

        public void reset() {
            this.size = 0;
        }

        protected TooLongFrameException newException(int maxLength) {
            return new TooLongHttpHeaderException("HTTP header is larger than " + maxLength + " bytes.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public final class LineParser extends HeaderParser {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        LineParser(ByteBuf seq, int maxLength) {
            super(seq, maxLength);
        }

        @Override // io.netty.handler.codec.http.HttpObjectDecoder.HeaderParser
        public ByteBuf parse(ByteBuf buffer) {
            reset();
            int readableBytes = buffer.readableBytes();
            if (readableBytes == 0) {
                return null;
            }
            int readerIndex = buffer.readerIndex();
            if (HttpObjectDecoder.this.currentState == State.SKIP_CONTROL_CHARS && skipControlChars(buffer, readableBytes, readerIndex)) {
                return null;
            }
            return super.parse(buffer);
        }

        private boolean skipControlChars(ByteBuf buffer, int readableBytes, int readerIndex) {
            if (HttpObjectDecoder.this.currentState != State.SKIP_CONTROL_CHARS) {
                throw new AssertionError();
            }
            int maxToSkip = Math.min(this.maxLength, readableBytes);
            int firstNonControlIndex = buffer.forEachByte(readerIndex, maxToSkip, HttpObjectDecoder.SKIP_CONTROL_CHARS_BYTES);
            if (firstNonControlIndex == -1) {
                buffer.skipBytes(maxToSkip);
                if (readableBytes > this.maxLength) {
                    throw newException(this.maxLength);
                }
                return true;
            }
            buffer.readerIndex(firstNonControlIndex);
            HttpObjectDecoder.this.currentState = State.READ_INITIAL;
            return false;
        }

        @Override // io.netty.handler.codec.http.HttpObjectDecoder.HeaderParser
        protected TooLongFrameException newException(int maxLength) {
            return new TooLongHttpLineException("An HTTP line is larger than " + maxLength + " bytes.");
        }
    }

    private static boolean isControlOrWhitespaceAsciiChar(byte b) {
        return ISO_CONTROL_OR_WHITESPACE[b + 128];
    }
}
