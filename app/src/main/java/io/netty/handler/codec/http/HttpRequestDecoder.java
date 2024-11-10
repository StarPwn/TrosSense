package io.netty.handler.codec.http;

import io.netty.buffer.Unpooled;
import io.netty.util.AsciiString;

/* loaded from: classes4.dex */
public class HttpRequestDecoder extends HttpObjectDecoder {
    private static final long ACCEPT_AS_LONG = 128026086171457L;
    private static final long CONNECTION_AS_LONG_0 = 7598807758576447299L;
    private static final short CONNECTION_AS_SHORT_1 = 28271;
    private static final long CONTENT_AS_LONG = 3275364211029339971L;
    private static final int GET_AS_INT = 5522759;
    private static final int HOST_AS_INT = 1953722184;
    private static final long HTTP_1_0_AS_LONG = 3471766442030158920L;
    private static final long HTTP_1_1_AS_LONG = 3543824036068086856L;
    private static final long LENGTH_AS_LONG = 114849160783180L;
    private static final int POST_AS_INT = 1414745936;
    private static final int TYPE_AS_INT = 1701869908;
    private static final AsciiString Accept = AsciiString.cached("Accept");
    private static final AsciiString Host = AsciiString.cached("Host");
    private static final AsciiString Connection = AsciiString.cached("Connection");
    private static final AsciiString ContentType = AsciiString.cached("Content-Type");
    private static final AsciiString ContentLength = AsciiString.cached("Content-Length");

    public HttpRequestDecoder() {
    }

    public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
        this(new HttpDecoderConfig().setMaxInitialLineLength(maxInitialLineLength).setMaxHeaderSize(maxHeaderSize).setMaxChunkSize(maxChunkSize));
    }

    @Deprecated
    public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true, validateHeaders);
    }

    @Deprecated
    public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true, validateHeaders, initialBufferSize);
    }

    @Deprecated
    public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true, validateHeaders, initialBufferSize, allowDuplicateContentLengths);
    }

    @Deprecated
    public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize, boolean allowDuplicateContentLengths, boolean allowPartialChunks) {
        super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true, validateHeaders, initialBufferSize, allowDuplicateContentLengths, allowPartialChunks);
    }

    public HttpRequestDecoder(HttpDecoderConfig config) {
        super(config);
    }

    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    protected HttpMessage createMessage(String[] initialLine) throws Exception {
        return new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]), HttpMethod.valueOf(initialLine[0]), initialLine[1], this.headersFactory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    public AsciiString splitHeaderName(byte[] sb, int start, int length) {
        byte firstChar = sb[start];
        if (firstChar == 72) {
            if (length == 4 && isHost(sb, start)) {
                return Host;
            }
        } else if (firstChar == 65) {
            if (length == 6 && isAccept(sb, start)) {
                return Accept;
            }
        } else if (firstChar == 67) {
            if (length == 10) {
                if (isConnection(sb, start)) {
                    return Connection;
                }
            } else if (length == 12) {
                if (isContentType(sb, start)) {
                    return ContentType;
                }
            } else if (length == 14 && isContentLength(sb, start)) {
                return ContentLength;
            }
        }
        return super.splitHeaderName(sb, start, length);
    }

    private static boolean isAccept(byte[] sb, int start) {
        long maybeAccept = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16) | (sb[start + 3] << 24) | (sb[start + 4] << 32) | (sb[start + 5] << 40);
        return maybeAccept == ACCEPT_AS_LONG;
    }

    private static boolean isHost(byte[] sb, int start) {
        int maybeHost = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16) | (sb[start + 3] << 24);
        return maybeHost == HOST_AS_INT;
    }

    private static boolean isConnection(byte[] sb, int start) {
        long maybeConnecti = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16) | (sb[start + 3] << 24) | (sb[start + 4] << 32) | (sb[start + 5] << 40) | (sb[start + 6] << 48) | (sb[start + 7] << 56);
        if (maybeConnecti != CONNECTION_AS_LONG_0) {
            return false;
        }
        short maybeOn = (short) (sb[start + 8] | (sb[start + 9] << 8));
        return maybeOn == 28271;
    }

    private static boolean isContentType(byte[] sb, int start) {
        long maybeContent = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16) | (sb[start + 3] << 24) | (sb[start + 4] << 32) | (sb[start + 5] << 40) | (sb[start + 6] << 48) | (sb[start + 7] << 56);
        if (maybeContent != CONTENT_AS_LONG) {
            return false;
        }
        int maybeType = sb[start + 8] | (sb[start + 9] << 8) | (sb[start + 10] << 16) | (sb[start + 11] << 24);
        return maybeType == TYPE_AS_INT;
    }

    private static boolean isContentLength(byte[] sb, int start) {
        long maybeContent = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16) | (sb[start + 3] << 24) | (sb[start + 4] << 32) | (sb[start + 5] << 40) | (sb[start + 6] << 48) | (sb[start + 7] << 56);
        if (maybeContent != CONTENT_AS_LONG) {
            return false;
        }
        long maybeLength = (sb[start + 13] << 40) | sb[start + 8] | (sb[start + 9] << 8) | (sb[start + 10] << 16) | (sb[start + 11] << 24) | (sb[start + 12] << 32);
        return maybeLength == LENGTH_AS_LONG;
    }

    private static boolean isGetMethod(byte[] sb, int start) {
        int maybeGet = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16);
        return maybeGet == GET_AS_INT;
    }

    private static boolean isPostMethod(byte[] sb, int start) {
        int maybePost = sb[start] | (sb[start + 1] << 8) | (sb[start + 2] << 16) | (sb[start + 3] << 24);
        return maybePost == POST_AS_INT;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    public String splitFirstWordInitialLine(byte[] sb, int start, int length) {
        if (length == 3) {
            if (isGetMethod(sb, start)) {
                return HttpMethod.GET.name();
            }
        } else if (length == 4 && isPostMethod(sb, start)) {
            return HttpMethod.POST.name();
        }
        return super.splitFirstWordInitialLine(sb, start, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    public String splitThirdWordInitialLine(byte[] sb, int start, int length) {
        if (length == 8) {
            long maybeHttp1_x = (sb[start + 1] << 8) | sb[start] | (sb[start + 2] << 16) | (sb[start + 3] << 24) | (sb[start + 4] << 32) | (sb[start + 5] << 40) | (sb[start + 6] << 48) | (sb[start + 7] << 56);
            if (maybeHttp1_x == HTTP_1_1_AS_LONG) {
                return "HTTP/1.1";
            }
            if (maybeHttp1_x == HTTP_1_0_AS_LONG) {
                return "HTTP/1.0";
            }
        }
        return super.splitThirdWordInitialLine(sb, start, length);
    }

    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    protected HttpMessage createInvalidMessage() {
        return new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/bad-request", Unpooled.buffer(0), this.headersFactory, this.trailersFactory);
    }

    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    protected boolean isDecodingRequest() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.http.HttpObjectDecoder
    public boolean isContentAlwaysEmpty(HttpMessage msg) {
        if (msg.getClass() == DefaultHttpRequest.class) {
            return false;
        }
        return super.isContentAlwaysEmpty(msg);
    }
}
