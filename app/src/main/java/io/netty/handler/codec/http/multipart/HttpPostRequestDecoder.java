package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.nio.charset.Charset;
import java.util.List;

/* loaded from: classes4.dex */
public class HttpPostRequestDecoder implements InterfaceHttpPostRequestDecoder {
    static final int DEFAULT_DISCARD_THRESHOLD = 10485760;
    static final int DEFAULT_MAX_BUFFERED_BYTES = 1024;
    static final int DEFAULT_MAX_FIELDS = 128;
    private final InterfaceHttpPostRequestDecoder decoder;

    /* loaded from: classes4.dex */
    public static class EndOfDataDecoderException extends DecoderException {
        private static final long serialVersionUID = 1336267941020800769L;
    }

    /* loaded from: classes4.dex */
    protected enum MultiPartStatus {
        NOTSTARTED,
        PREAMBLE,
        HEADERDELIMITER,
        DISPOSITION,
        FIELD,
        FILEUPLOAD,
        MIXEDPREAMBLE,
        MIXEDDELIMITER,
        MIXEDDISPOSITION,
        MIXEDFILEUPLOAD,
        MIXEDCLOSEDELIMITER,
        CLOSEDELIMITER,
        PREEPILOGUE,
        EPILOGUE
    }

    /* loaded from: classes4.dex */
    public static final class TooLongFormFieldException extends DecoderException {
        private static final long serialVersionUID = 1336267941020800769L;
    }

    /* loaded from: classes4.dex */
    public static final class TooManyFormFieldsException extends DecoderException {
        private static final long serialVersionUID = 1336267941020800769L;
    }

    public HttpPostRequestDecoder(HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostRequestDecoder(HttpRequest request, int maxFields, int maxBufferedBytes) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET, maxFields, maxBufferedBytes);
    }

    public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) {
        ObjectUtil.checkNotNull(factory, "factory");
        ObjectUtil.checkNotNull(request, "request");
        ObjectUtil.checkNotNull(charset, "charset");
        if (isMultipart(request)) {
            this.decoder = new HttpPostMultipartRequestDecoder(factory, request, charset);
        } else {
            this.decoder = new HttpPostStandardRequestDecoder(factory, request, charset);
        }
    }

    public HttpPostRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset, int maxFields, int maxBufferedBytes) {
        ObjectUtil.checkNotNull(factory, "factory");
        ObjectUtil.checkNotNull(request, "request");
        ObjectUtil.checkNotNull(charset, "charset");
        if (isMultipart(request)) {
            this.decoder = new HttpPostMultipartRequestDecoder(factory, request, charset, maxFields, maxBufferedBytes);
        } else {
            this.decoder = new HttpPostStandardRequestDecoder(factory, request, charset, maxFields, maxBufferedBytes);
        }
    }

    public static boolean isMultipart(HttpRequest request) {
        String mimeType = request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        return (mimeType == null || !mimeType.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString()) || getMultipartDataBoundary(mimeType) == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String[] getMultipartDataBoundary(String contentType) {
        int mrank;
        int crank;
        String charset;
        String[] headerContentType = splitHeaderContentType(contentType);
        String multiPartHeader = HttpHeaderValues.MULTIPART_FORM_DATA.toString();
        if (!headerContentType[0].regionMatches(true, 0, multiPartHeader, 0, multiPartHeader.length())) {
            return null;
        }
        String boundaryHeader = HttpHeaderValues.BOUNDARY.toString();
        if (headerContentType[1].regionMatches(true, 0, boundaryHeader, 0, boundaryHeader.length())) {
            mrank = 1;
            crank = 2;
        } else {
            if (!headerContentType[2].regionMatches(true, 0, boundaryHeader, 0, boundaryHeader.length())) {
                return null;
            }
            mrank = 2;
            crank = 1;
        }
        String boundary = StringUtil.substringAfter(headerContentType[mrank], '=');
        if (boundary == null) {
            throw new ErrorDataDecoderException("Needs a boundary value");
        }
        if (boundary.charAt(0) == '\"') {
            String bound = boundary.trim();
            int index = bound.length() - 1;
            if (bound.charAt(index) == '\"') {
                boundary = bound.substring(1, index);
            }
        }
        String charsetHeader = HttpHeaderValues.CHARSET.toString();
        if (headerContentType[crank].regionMatches(true, 0, charsetHeader, 0, charsetHeader.length()) && (charset = StringUtil.substringAfter(headerContentType[crank], '=')) != null) {
            return new String[]{"--" + boundary, charset};
        }
        return new String[]{"--" + boundary};
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean isMultipart() {
        return this.decoder.isMultipart();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void setDiscardThreshold(int discardThreshold) {
        this.decoder.setDiscardThreshold(discardThreshold);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public int getDiscardThreshold() {
        return this.decoder.getDiscardThreshold();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public List<InterfaceHttpData> getBodyHttpDatas() {
        return this.decoder.getBodyHttpDatas();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public List<InterfaceHttpData> getBodyHttpDatas(String name) {
        return this.decoder.getBodyHttpDatas(name);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData getBodyHttpData(String name) {
        return this.decoder.getBodyHttpData(name);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpPostRequestDecoder offer(HttpContent content) {
        return this.decoder.offer(content);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean hasNext() {
        return this.decoder.hasNext();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData next() {
        return this.decoder.next();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData currentPartialHttpData() {
        return this.decoder.currentPartialHttpData();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void destroy() {
        this.decoder.destroy();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void cleanFiles() {
        this.decoder.cleanFiles();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void removeHttpDataFromClean(InterfaceHttpData data) {
        this.decoder.removeHttpDataFromClean(data);
    }

    private static String[] splitHeaderContentType(String sb) {
        int aStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
        int aEnd = sb.indexOf(59);
        if (aEnd == -1) {
            return new String[]{sb, "", ""};
        }
        int bStart = HttpPostBodyUtil.findNonWhitespace(sb, aEnd + 1);
        if (sb.charAt(aEnd - 1) == ' ') {
            aEnd--;
        }
        int bEnd = sb.indexOf(59, bStart);
        if (bEnd == -1) {
            return new String[]{sb.substring(aStart, aEnd), sb.substring(bStart, HttpPostBodyUtil.findEndOfString(sb)), ""};
        }
        int cStart = HttpPostBodyUtil.findNonWhitespace(sb, bEnd + 1);
        if (sb.charAt(bEnd - 1) == ' ') {
            bEnd--;
        }
        int cEnd = HttpPostBodyUtil.findEndOfString(sb);
        return new String[]{sb.substring(aStart, aEnd), sb.substring(bStart, bEnd), sb.substring(cStart, cEnd)};
    }

    /* loaded from: classes4.dex */
    public static class NotEnoughDataDecoderException extends DecoderException {
        private static final long serialVersionUID = -7846841864603865638L;

        public NotEnoughDataDecoderException() {
        }

        public NotEnoughDataDecoderException(String msg) {
            super(msg);
        }

        public NotEnoughDataDecoderException(Throwable cause) {
            super(cause);
        }

        public NotEnoughDataDecoderException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    /* loaded from: classes4.dex */
    public static class ErrorDataDecoderException extends DecoderException {
        private static final long serialVersionUID = 5020247425493164465L;

        public ErrorDataDecoderException() {
        }

        public ErrorDataDecoderException(String msg) {
            super(msg);
        }

        public ErrorDataDecoderException(Throwable cause) {
            super(cause);
        }

        public ErrorDataDecoderException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
