package io.netty.handler.codec.http.multipart;

import com.trossense.bl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.stream.ChunkedInput;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public class HttpPostRequestEncoder implements ChunkedInput<HttpContent> {
    private static final Map.Entry[] percentEncodings = {new AbstractMap.SimpleImmutableEntry(Pattern.compile("\\*"), "%2A"), new AbstractMap.SimpleImmutableEntry(Pattern.compile("\\+"), "%20"), new AbstractMap.SimpleImmutableEntry(Pattern.compile("~"), "%7E")};
    private final List<InterfaceHttpData> bodyListDatas;
    private final Charset charset;
    private ByteBuf currentBuffer;
    private InterfaceHttpData currentData;
    private FileUpload currentFileUpload;
    private boolean duringMixedMode;
    private final EncoderMode encoderMode;
    private final HttpDataFactory factory;
    private long globalBodySize;
    private long globalProgress;
    private boolean headerFinalized;
    private boolean isChunked;
    private boolean isKey;
    private boolean isLastChunk;
    private boolean isLastChunkSent;
    private final boolean isMultipart;
    private ListIterator<InterfaceHttpData> iterator;
    String multipartDataBoundary;
    final List<InterfaceHttpData> multipartHttpDatas;
    String multipartMixedBoundary;
    private final HttpRequest request;

    /* loaded from: classes4.dex */
    public enum EncoderMode {
        RFC1738,
        RFC3986,
        HTML5
    }

    public HttpPostRequestEncoder(HttpRequest request, boolean multipart) throws ErrorDataEncoderException {
        this(new DefaultHttpDataFactory(16384L), request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
    }

    public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart) throws ErrorDataEncoderException {
        this(factory, request, multipart, HttpConstants.DEFAULT_CHARSET, EncoderMode.RFC1738);
    }

    public HttpPostRequestEncoder(HttpDataFactory factory, HttpRequest request, boolean multipart, Charset charset, EncoderMode encoderMode) throws ErrorDataEncoderException {
        this.isKey = true;
        this.request = (HttpRequest) ObjectUtil.checkNotNull(request, "request");
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
        this.factory = (HttpDataFactory) ObjectUtil.checkNotNull(factory, "factory");
        if (HttpMethod.TRACE.equals(request.method())) {
            throw new ErrorDataEncoderException("Cannot create a Encoder if request is a TRACE");
        }
        this.bodyListDatas = new ArrayList();
        this.isLastChunk = false;
        this.isLastChunkSent = false;
        this.isMultipart = multipart;
        this.multipartHttpDatas = new ArrayList();
        this.encoderMode = encoderMode;
        if (this.isMultipart) {
            initDataMultipart();
        }
    }

    public void cleanFiles() {
        this.factory.cleanRequestHttpData(this.request);
    }

    public boolean isMultipart() {
        return this.isMultipart;
    }

    private void initDataMultipart() {
        this.multipartDataBoundary = getNewMultipartDelimiter();
    }

    private void initMixedMultipart() {
        this.multipartMixedBoundary = getNewMultipartDelimiter();
    }

    private static String getNewMultipartDelimiter() {
        return Long.toHexString(PlatformDependent.threadLocalRandom().nextLong());
    }

    public List<InterfaceHttpData> getBodyListAttributes() {
        return this.bodyListDatas;
    }

    public void setBodyHttpDatas(List<InterfaceHttpData> datas) throws ErrorDataEncoderException {
        ObjectUtil.checkNotNull(datas, "datas");
        this.globalBodySize = 0L;
        this.bodyListDatas.clear();
        this.currentFileUpload = null;
        this.duringMixedMode = false;
        this.multipartHttpDatas.clear();
        for (InterfaceHttpData data : datas) {
            addBodyHttpData(data);
        }
    }

    public void addBodyAttribute(String name, String value) throws ErrorDataEncoderException {
        String svalue = value != null ? value : "";
        Attribute data = this.factory.createAttribute(this.request, (String) ObjectUtil.checkNotNull(name, "name"), svalue);
        addBodyHttpData(data);
    }

    public void addBodyFileUpload(String name, File file, String contentType, boolean isText) throws ErrorDataEncoderException {
        addBodyFileUpload(name, file.getName(), file, contentType, isText);
    }

    public void addBodyFileUpload(String name, String filename, File file, String contentType, boolean isText) throws ErrorDataEncoderException {
        String filename2;
        String scontentType;
        String contentTransferEncoding;
        ObjectUtil.checkNotNull(name, "name");
        ObjectUtil.checkNotNull(file, "file");
        if (filename != null) {
            filename2 = filename;
        } else {
            filename2 = "";
        }
        if (contentType != null) {
            scontentType = contentType;
        } else if (isText) {
            scontentType = HttpPostBodyUtil.DEFAULT_TEXT_CONTENT_TYPE;
        } else {
            scontentType = HttpPostBodyUtil.DEFAULT_BINARY_CONTENT_TYPE;
        }
        if (isText) {
            contentTransferEncoding = null;
        } else {
            String contentTransferEncoding2 = HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value();
            contentTransferEncoding = contentTransferEncoding2;
        }
        FileUpload fileUpload = this.factory.createFileUpload(this.request, name, filename2, scontentType, contentTransferEncoding, null, file.length());
        try {
            fileUpload.setContent(file);
            addBodyHttpData(fileUpload);
        } catch (IOException e) {
            throw new ErrorDataEncoderException(e);
        }
    }

    public void addBodyFileUploads(String name, File[] file, String[] contentType, boolean[] isText) throws ErrorDataEncoderException {
        if (file.length != contentType.length && file.length != isText.length) {
            throw new IllegalArgumentException("Different array length");
        }
        for (int i = 0; i < file.length; i++) {
            addBodyFileUpload(name, file[i], contentType[i], isText[i]);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addBodyHttpData(InterfaceHttpData data) throws ErrorDataEncoderException {
        String str;
        boolean localMixed;
        if (this.headerFinalized) {
            throw new ErrorDataEncoderException("Cannot add value once finalized");
        }
        this.bodyListDatas.add(ObjectUtil.checkNotNull(data, "data"));
        if (!this.isMultipart) {
            if (data instanceof Attribute) {
                Attribute attribute = (Attribute) data;
                try {
                    String key = encodeAttribute(attribute.getName(), this.charset);
                    String value = encodeAttribute(attribute.getValue(), this.charset);
                    Attribute newattribute = this.factory.createAttribute(this.request, key, value);
                    this.multipartHttpDatas.add(newattribute);
                    this.globalBodySize += newattribute.getName().length() + 1 + newattribute.length() + 1;
                    return;
                } catch (IOException e) {
                    throw new ErrorDataEncoderException(e);
                }
            }
            if (data instanceof FileUpload) {
                FileUpload fileUpload = (FileUpload) data;
                String key2 = encodeAttribute(fileUpload.getName(), this.charset);
                String value2 = encodeAttribute(fileUpload.getFilename(), this.charset);
                Attribute newattribute2 = this.factory.createAttribute(this.request, key2, value2);
                this.multipartHttpDatas.add(newattribute2);
                this.globalBodySize += newattribute2.getName().length() + 1 + newattribute2.length() + 1;
                return;
            }
            return;
        }
        if (data instanceof Attribute) {
            if (this.duringMixedMode) {
                InternalAttribute internal = new InternalAttribute(this.charset);
                internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");
                this.multipartHttpDatas.add(internal);
                this.multipartMixedBoundary = null;
                this.currentFileUpload = null;
                this.duringMixedMode = false;
            }
            InternalAttribute internal2 = new InternalAttribute(this.charset);
            if (!this.multipartHttpDatas.isEmpty()) {
                internal2.addValue("\r\n");
            }
            internal2.addValue("--" + this.multipartDataBoundary + "\r\n");
            Attribute attribute2 = (Attribute) data;
            internal2.addValue(((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.FORM_DATA) + "; " + ((Object) HttpHeaderValues.NAME) + "=\"" + attribute2.getName() + "\"\r\n");
            internal2.addValue(((Object) HttpHeaderNames.CONTENT_LENGTH) + ": " + attribute2.length() + "\r\n");
            Charset localcharset = attribute2.getCharset();
            if (localcharset != null) {
                internal2.addValue(((Object) HttpHeaderNames.CONTENT_TYPE) + ": " + HttpPostBodyUtil.DEFAULT_TEXT_CONTENT_TYPE + "; " + ((Object) HttpHeaderValues.CHARSET) + '=' + localcharset.name() + "\r\n");
            }
            internal2.addValue("\r\n");
            this.multipartHttpDatas.add(internal2);
            this.multipartHttpDatas.add(data);
            this.globalBodySize += attribute2.length() + internal2.size();
            return;
        }
        if (data instanceof FileUpload) {
            FileUpload fileUpload2 = (FileUpload) data;
            InternalAttribute internal3 = new InternalAttribute(this.charset);
            if (!this.multipartHttpDatas.isEmpty()) {
                internal3.addValue("\r\n");
            }
            if (this.duringMixedMode) {
                if (this.currentFileUpload == null || !this.currentFileUpload.getName().equals(fileUpload2.getName())) {
                    internal3.addValue("--" + this.multipartMixedBoundary + "--");
                    this.multipartHttpDatas.add(internal3);
                    this.multipartMixedBoundary = null;
                    internal3 = new InternalAttribute(this.charset);
                    internal3.addValue("\r\n");
                    localMixed = false;
                    this.currentFileUpload = fileUpload2;
                    this.duringMixedMode = false;
                    str = "\r\n\r\n";
                } else {
                    localMixed = true;
                    str = "\r\n\r\n";
                }
            } else {
                if (this.encoderMode == EncoderMode.HTML5 || this.currentFileUpload == null) {
                    str = "\r\n\r\n";
                } else if (!this.currentFileUpload.getName().equals(fileUpload2.getName())) {
                    str = "\r\n\r\n";
                } else {
                    initMixedMultipart();
                    InternalAttribute pastAttribute = (InternalAttribute) this.multipartHttpDatas.get(this.multipartHttpDatas.size() - 2);
                    this.globalBodySize -= pastAttribute.size();
                    str = "\r\n\r\n";
                    StringBuilder replacement = new StringBuilder(this.multipartDataBoundary.length() + bl.bA + (this.multipartMixedBoundary.length() * 2) + fileUpload2.getFilename().length() + fileUpload2.getName().length()).append("--").append(this.multipartDataBoundary).append("\r\n").append((CharSequence) HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append((CharSequence) HttpHeaderValues.FORM_DATA).append("; ").append((CharSequence) HttpHeaderValues.NAME).append("=\"").append(fileUpload2.getName()).append("\"\r\n").append((CharSequence) HttpHeaderNames.CONTENT_TYPE).append(": ").append((CharSequence) HttpHeaderValues.MULTIPART_MIXED).append("; ").append((CharSequence) HttpHeaderValues.BOUNDARY).append('=').append(this.multipartMixedBoundary).append(str).append("--").append(this.multipartMixedBoundary).append("\r\n").append((CharSequence) HttpHeaderNames.CONTENT_DISPOSITION).append(": ").append((CharSequence) HttpHeaderValues.ATTACHMENT);
                    if (!fileUpload2.getFilename().isEmpty()) {
                        replacement.append("; ").append((CharSequence) HttpHeaderValues.FILENAME).append("=\"").append(this.currentFileUpload.getFilename()).append('\"');
                    }
                    replacement.append("\r\n");
                    pastAttribute.setValue(replacement.toString(), 1);
                    pastAttribute.setValue("", 2);
                    this.globalBodySize += pastAttribute.size();
                    this.duringMixedMode = true;
                    localMixed = true;
                }
                localMixed = false;
                this.currentFileUpload = fileUpload2;
                this.duringMixedMode = false;
            }
            if (localMixed) {
                internal3.addValue("--" + this.multipartMixedBoundary + "\r\n");
                if (fileUpload2.getFilename().isEmpty()) {
                    internal3.addValue(((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.ATTACHMENT) + "\r\n");
                } else {
                    internal3.addValue(((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.ATTACHMENT) + "; " + ((Object) HttpHeaderValues.FILENAME) + "=\"" + fileUpload2.getFilename() + "\"\r\n");
                }
            } else {
                internal3.addValue("--" + this.multipartDataBoundary + "\r\n");
                if (fileUpload2.getFilename().isEmpty()) {
                    internal3.addValue(((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.FORM_DATA) + "; " + ((Object) HttpHeaderValues.NAME) + "=\"" + fileUpload2.getName() + "\"\r\n");
                } else {
                    internal3.addValue(((Object) HttpHeaderNames.CONTENT_DISPOSITION) + ": " + ((Object) HttpHeaderValues.FORM_DATA) + "; " + ((Object) HttpHeaderValues.NAME) + "=\"" + fileUpload2.getName() + "\"; " + ((Object) HttpHeaderValues.FILENAME) + "=\"" + fileUpload2.getFilename() + "\"\r\n");
                }
            }
            internal3.addValue(((Object) HttpHeaderNames.CONTENT_LENGTH) + ": " + fileUpload2.length() + "\r\n");
            internal3.addValue(((Object) HttpHeaderNames.CONTENT_TYPE) + ": " + fileUpload2.getContentType());
            String contentTransferEncoding = fileUpload2.getContentTransferEncoding();
            if (contentTransferEncoding != null && contentTransferEncoding.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                internal3.addValue("\r\n" + ((Object) HttpHeaderNames.CONTENT_TRANSFER_ENCODING) + ": " + HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value() + str);
            } else if (fileUpload2.getCharset() != null) {
                internal3.addValue("; " + ((Object) HttpHeaderValues.CHARSET) + '=' + fileUpload2.getCharset().name() + str);
            } else {
                internal3.addValue(str);
            }
            this.multipartHttpDatas.add(internal3);
            this.multipartHttpDatas.add(data);
            this.globalBodySize += fileUpload2.length() + internal3.size();
        }
    }

    public HttpRequest finalizeRequest() throws ErrorDataEncoderException {
        if (!this.headerFinalized) {
            if (this.isMultipart) {
                InternalAttribute internal = new InternalAttribute(this.charset);
                if (this.duringMixedMode) {
                    internal.addValue("\r\n--" + this.multipartMixedBoundary + "--");
                }
                internal.addValue("\r\n--" + this.multipartDataBoundary + "--\r\n");
                this.multipartHttpDatas.add(internal);
                this.multipartMixedBoundary = null;
                this.currentFileUpload = null;
                this.duringMixedMode = false;
                this.globalBodySize += internal.size();
            }
            this.headerFinalized = true;
            HttpHeaders headers = this.request.headers();
            List<String> contentTypes = headers.getAll(HttpHeaderNames.CONTENT_TYPE);
            List<String> transferEncoding = headers.getAll(HttpHeaderNames.TRANSFER_ENCODING);
            if (contentTypes != null) {
                headers.remove(HttpHeaderNames.CONTENT_TYPE);
                for (String contentType : contentTypes) {
                    String lowercased = contentType.toLowerCase();
                    if (!lowercased.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString()) && !lowercased.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())) {
                        headers.add(HttpHeaderNames.CONTENT_TYPE, contentType);
                    }
                }
            }
            if (this.isMultipart) {
                String value = ((Object) HttpHeaderValues.MULTIPART_FORM_DATA) + "; " + ((Object) HttpHeaderValues.BOUNDARY) + '=' + this.multipartDataBoundary;
                headers.add(HttpHeaderNames.CONTENT_TYPE, value);
            } else {
                headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
            }
            long realSize = this.globalBodySize;
            if (!this.isMultipart) {
                realSize--;
            }
            this.iterator = this.multipartHttpDatas.listIterator();
            headers.set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(realSize));
            if (realSize > 8096 || this.isMultipart) {
                this.isChunked = true;
                if (transferEncoding != null) {
                    headers.remove(HttpHeaderNames.TRANSFER_ENCODING);
                    for (CharSequence v : transferEncoding) {
                        if (!HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(v)) {
                            headers.add(HttpHeaderNames.TRANSFER_ENCODING, v);
                        }
                    }
                }
                HttpUtil.setTransferEncodingChunked(this.request, true);
                return new WrappedHttpRequest(this.request);
            }
            HttpContent chunk = nextChunk();
            if (this.request instanceof FullHttpRequest) {
                FullHttpRequest fullRequest = (FullHttpRequest) this.request;
                ByteBuf chunkContent = chunk.content();
                if (fullRequest.content() != chunkContent) {
                    fullRequest.content().clear().writeBytes(chunkContent);
                    chunkContent.release();
                }
                return fullRequest;
            }
            return new WrappedFullHttpRequest(this.request, chunk);
        }
        throw new ErrorDataEncoderException("Header already encoded");
    }

    public boolean isChunked() {
        return this.isChunked;
    }

    private String encodeAttribute(String s, Charset charset) throws ErrorDataEncoderException {
        if (s == null) {
            return "";
        }
        try {
            String encoded = URLEncoder.encode(s, charset.name());
            if (this.encoderMode == EncoderMode.RFC3986) {
                for (Map.Entry<Pattern, String> entry : percentEncodings) {
                    String replacement = entry.getValue();
                    encoded = entry.getKey().matcher(encoded).replaceAll(replacement);
                }
            }
            return encoded;
        } catch (UnsupportedEncodingException e) {
            throw new ErrorDataEncoderException(charset.name(), e);
        }
    }

    private ByteBuf fillByteBuf() {
        int length = this.currentBuffer.readableBytes();
        if (length > 8096) {
            return this.currentBuffer.readRetainedSlice(HttpPostBodyUtil.chunkSize);
        }
        ByteBuf slice = this.currentBuffer;
        this.currentBuffer = null;
        return slice;
    }

    private HttpContent encodeNextChunkMultipart(int sizeleft) throws ErrorDataEncoderException {
        ByteBuf buffer;
        if (this.currentData == null) {
            return null;
        }
        if (this.currentData instanceof InternalAttribute) {
            buffer = ((InternalAttribute) this.currentData).toByteBuf();
            this.currentData = null;
        } else {
            try {
                buffer = ((HttpData) this.currentData).getChunk(sizeleft);
                if (buffer.capacity() == 0) {
                    this.currentData = null;
                    return null;
                }
            } catch (IOException e) {
                throw new ErrorDataEncoderException(e);
            }
        }
        if (this.currentBuffer == null) {
            this.currentBuffer = buffer;
        } else {
            this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, buffer);
        }
        if (this.currentBuffer.readableBytes() < 8096) {
            this.currentData = null;
            return null;
        }
        return new DefaultHttpContent(fillByteBuf());
    }

    private HttpContent encodeNextChunkUrlEncoded(int sizeleft) throws ErrorDataEncoderException {
        if (this.currentData == null) {
            return null;
        }
        int size = sizeleft;
        if (this.isKey) {
            String key = this.currentData.getName();
            ByteBuf buffer = Unpooled.wrappedBuffer(key.getBytes(this.charset));
            this.isKey = false;
            if (this.currentBuffer == null) {
                this.currentBuffer = Unpooled.wrappedBuffer(buffer, Unpooled.wrappedBuffer("=".getBytes(this.charset)));
            } else {
                this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, buffer, Unpooled.wrappedBuffer("=".getBytes(this.charset)));
            }
            size -= buffer.readableBytes() + 1;
            if (this.currentBuffer.readableBytes() >= 8096) {
                return new DefaultHttpContent(fillByteBuf());
            }
        }
        try {
            ByteBuf buffer2 = ((HttpData) this.currentData).getChunk(size);
            ByteBuf delimiter = null;
            if (buffer2.readableBytes() < size) {
                this.isKey = true;
                delimiter = this.iterator.hasNext() ? Unpooled.wrappedBuffer("&".getBytes(this.charset)) : null;
            }
            if (buffer2.capacity() == 0) {
                this.currentData = null;
                if (this.currentBuffer == null) {
                    if (delimiter == null) {
                        return null;
                    }
                    this.currentBuffer = delimiter;
                } else if (delimiter != null) {
                    this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, delimiter);
                }
                if (this.currentBuffer.readableBytes() >= 8096) {
                    return new DefaultHttpContent(fillByteBuf());
                }
                return null;
            }
            if (this.currentBuffer == null) {
                if (delimiter != null) {
                    this.currentBuffer = Unpooled.wrappedBuffer(buffer2, delimiter);
                } else {
                    this.currentBuffer = buffer2;
                }
            } else if (delimiter != null) {
                this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, buffer2, delimiter);
            } else {
                this.currentBuffer = Unpooled.wrappedBuffer(this.currentBuffer, buffer2);
            }
            if (this.currentBuffer.readableBytes() < 8096) {
                this.currentData = null;
                this.isKey = true;
                return null;
            }
            return new DefaultHttpContent(fillByteBuf());
        } catch (IOException e) {
            throw new ErrorDataEncoderException(e);
        }
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public void close() throws Exception {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    @Deprecated
    public HttpContent readChunk(ChannelHandlerContext ctx) throws Exception {
        return readChunk(ctx.alloc());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.stream.ChunkedInput
    public HttpContent readChunk(ByteBufAllocator allocator) throws Exception {
        if (this.isLastChunkSent) {
            return null;
        }
        HttpContent nextChunk = nextChunk();
        this.globalProgress += nextChunk.content().readableBytes();
        return nextChunk;
    }

    private HttpContent nextChunk() throws ErrorDataEncoderException {
        HttpContent chunk;
        HttpContent chunk2;
        if (this.isLastChunk) {
            this.isLastChunkSent = true;
            return LastHttpContent.EMPTY_LAST_CONTENT;
        }
        int size = calculateRemainingSize();
        if (size <= 0) {
            ByteBuf buffer = fillByteBuf();
            return new DefaultHttpContent(buffer);
        }
        if (this.currentData != null) {
            if (this.isMultipart) {
                chunk2 = encodeNextChunkMultipart(size);
            } else {
                chunk2 = encodeNextChunkUrlEncoded(size);
            }
            if (chunk2 != null) {
                return chunk2;
            }
            size = calculateRemainingSize();
        }
        if (!this.iterator.hasNext()) {
            return lastChunk();
        }
        while (size > 0 && this.iterator.hasNext()) {
            this.currentData = this.iterator.next();
            if (this.isMultipart) {
                chunk = encodeNextChunkMultipart(size);
            } else {
                chunk = encodeNextChunkUrlEncoded(size);
            }
            if (chunk == null) {
                size = calculateRemainingSize();
            } else {
                return chunk;
            }
        }
        HttpContent chunk3 = lastChunk();
        return chunk3;
    }

    private int calculateRemainingSize() {
        if (this.currentBuffer != null) {
            int size = HttpPostBodyUtil.chunkSize - this.currentBuffer.readableBytes();
            return size;
        }
        return HttpPostBodyUtil.chunkSize;
    }

    private HttpContent lastChunk() {
        this.isLastChunk = true;
        if (this.currentBuffer == null) {
            this.isLastChunkSent = true;
            return LastHttpContent.EMPTY_LAST_CONTENT;
        }
        ByteBuf buffer = this.currentBuffer;
        this.currentBuffer = null;
        return new DefaultHttpContent(buffer);
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public boolean isEndOfInput() throws Exception {
        return this.isLastChunkSent;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long length() {
        return this.isMultipart ? this.globalBodySize : this.globalBodySize - 1;
    }

    @Override // io.netty.handler.stream.ChunkedInput
    public long progress() {
        return this.globalProgress;
    }

    /* loaded from: classes4.dex */
    public static class ErrorDataEncoderException extends Exception {
        private static final long serialVersionUID = 5020247425493164465L;

        public ErrorDataEncoderException() {
        }

        public ErrorDataEncoderException(String msg) {
            super(msg);
        }

        public ErrorDataEncoderException(Throwable cause) {
            super(cause);
        }

        public ErrorDataEncoderException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class WrappedHttpRequest implements HttpRequest {
        private final HttpRequest request;

        WrappedHttpRequest(HttpRequest request) {
            this.request = request;
        }

        @Override // io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public HttpRequest setProtocolVersion(HttpVersion version) {
            this.request.setProtocolVersion(version);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public HttpRequest setMethod(HttpMethod method) {
            this.request.setMethod(method);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public HttpRequest setUri(String uri) {
            this.request.setUri(uri);
            return this;
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public HttpMethod getMethod() {
            return this.request.method();
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public HttpMethod method() {
            return this.request.method();
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public String getUri() {
            return this.request.uri();
        }

        @Override // io.netty.handler.codec.http.HttpRequest
        public String uri() {
            return this.request.uri();
        }

        @Override // io.netty.handler.codec.http.HttpMessage
        public HttpVersion getProtocolVersion() {
            return this.request.protocolVersion();
        }

        @Override // io.netty.handler.codec.http.HttpMessage
        public HttpVersion protocolVersion() {
            return this.request.protocolVersion();
        }

        @Override // io.netty.handler.codec.http.HttpMessage
        public HttpHeaders headers() {
            return this.request.headers();
        }

        @Override // io.netty.handler.codec.DecoderResultProvider
        public DecoderResult decoderResult() {
            return this.request.decoderResult();
        }

        @Override // io.netty.handler.codec.http.HttpObject
        @Deprecated
        public DecoderResult getDecoderResult() {
            return this.request.getDecoderResult();
        }

        @Override // io.netty.handler.codec.DecoderResultProvider
        public void setDecoderResult(DecoderResult result) {
            this.request.setDecoderResult(result);
        }
    }

    /* loaded from: classes4.dex */
    private static final class WrappedFullHttpRequest extends WrappedHttpRequest implements FullHttpRequest {
        private final HttpContent content;

        private WrappedFullHttpRequest(HttpRequest request, HttpContent content) {
            super(request);
            this.content = content;
        }

        @Override // io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.WrappedHttpRequest, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpRequest setProtocolVersion(HttpVersion version) {
            super.setProtocolVersion(version);
            return this;
        }

        @Override // io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.WrappedHttpRequest, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpRequest setMethod(HttpMethod method) {
            super.setMethod(method);
            return this;
        }

        @Override // io.netty.handler.codec.http.multipart.HttpPostRequestEncoder.WrappedHttpRequest, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
        public FullHttpRequest setUri(String uri) {
            super.setUri(uri);
            return this;
        }

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest copy() {
            return replace(content().copy());
        }

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest duplicate() {
            return replace(content().duplicate());
        }

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest retainedDuplicate() {
            return replace(content().retainedDuplicate());
        }

        @Override // io.netty.handler.codec.http.LastHttpContent, io.netty.handler.codec.http.HttpContent, io.netty.buffer.ByteBufHolder
        public FullHttpRequest replace(ByteBuf content) {
            DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content);
            duplicate.headers().set(headers());
            duplicate.trailingHeaders().set(trailingHeaders());
            return duplicate;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpRequest retain(int increment) {
            this.content.retain(increment);
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpRequest retain() {
            this.content.retain();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpRequest touch() {
            this.content.touch();
            return this;
        }

        @Override // io.netty.util.ReferenceCounted
        public FullHttpRequest touch(Object hint) {
            this.content.touch(hint);
            return this;
        }

        @Override // io.netty.buffer.ByteBufHolder
        public ByteBuf content() {
            return this.content.content();
        }

        @Override // io.netty.handler.codec.http.LastHttpContent
        public HttpHeaders trailingHeaders() {
            if (this.content instanceof LastHttpContent) {
                return ((LastHttpContent) this.content).trailingHeaders();
            }
            return EmptyHttpHeaders.INSTANCE;
        }

        @Override // io.netty.util.ReferenceCounted
        public int refCnt() {
            return this.content.refCnt();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release() {
            return this.content.release();
        }

        @Override // io.netty.util.ReferenceCounted
        public boolean release(int decrement) {
            return this.content.release(decrement);
        }
    }
}
