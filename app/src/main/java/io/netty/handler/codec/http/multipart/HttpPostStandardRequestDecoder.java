package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes4.dex */
public class HttpPostStandardRequestDecoder implements InterfaceHttpPostRequestDecoder {
    private final List<InterfaceHttpData> bodyListHttpData;
    private int bodyListHttpDataRank;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private final Charset charset;
    private Attribute currentAttribute;
    private HttpPostRequestDecoder.MultiPartStatus currentStatus;
    private boolean destroyed;
    private int discardThreshold;
    private final HttpDataFactory factory;
    private boolean isLastChunk;
    private final int maxBufferedBytes;
    private final int maxFields;
    private final HttpRequest request;
    private ByteBuf undecodedChunk;

    public HttpPostStandardRequestDecoder(HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) {
        this(factory, request, charset, 128, 1024);
    }

    public HttpPostStandardRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset, int maxFields, int maxBufferedBytes) {
        this.bodyListHttpData = new ArrayList();
        this.bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
        this.discardThreshold = 10485760;
        this.request = (HttpRequest) ObjectUtil.checkNotNull(request, "request");
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
        this.factory = (HttpDataFactory) ObjectUtil.checkNotNull(factory, "factory");
        this.maxFields = maxFields;
        this.maxBufferedBytes = maxBufferedBytes;
        try {
            if (request instanceof HttpContent) {
                offer((HttpContent) request);
            } else {
                parseBody();
            }
        } catch (Throwable e) {
            destroy();
            PlatformDependent.throwException(e);
        }
    }

    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostStandardRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean isMultipart() {
        checkDestroyed();
        return false;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void setDiscardThreshold(int discardThreshold) {
        this.discardThreshold = ObjectUtil.checkPositiveOrZero(discardThreshold, "discardThreshold");
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public int getDiscardThreshold() {
        return this.discardThreshold;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public List<InterfaceHttpData> getBodyHttpDatas() {
        checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyListHttpData;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public List<InterfaceHttpData> getBodyHttpDatas(String name) {
        checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyMapHttpData.get(name);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData getBodyHttpData(String name) {
        checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
        if (list != null) {
            return list.get(0);
        }
        return null;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public HttpPostStandardRequestDecoder offer(HttpContent content) {
        checkDestroyed();
        if (content instanceof LastHttpContent) {
            this.isLastChunk = true;
        }
        ByteBuf buf = content.content();
        if (this.undecodedChunk == null) {
            this.undecodedChunk = buf.alloc().buffer(buf.readableBytes()).writeBytes(buf);
        } else {
            this.undecodedChunk.writeBytes(buf);
        }
        parseBody();
        if (this.maxBufferedBytes > 0 && this.undecodedChunk != null && this.undecodedChunk.readableBytes() > this.maxBufferedBytes) {
            throw new HttpPostRequestDecoder.TooLongFormFieldException();
        }
        if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
            if (this.undecodedChunk.refCnt() == 1) {
                this.undecodedChunk.discardReadBytes();
            } else {
                ByteBuf buffer = this.undecodedChunk.alloc().buffer(this.undecodedChunk.readableBytes());
                buffer.writeBytes(this.undecodedChunk);
                this.undecodedChunk.release();
                this.undecodedChunk = buffer;
            }
        }
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean hasNext() {
        checkDestroyed();
        if (this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.EPILOGUE || this.bodyListHttpDataRank < this.bodyListHttpData.size()) {
            return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
        }
        throw new HttpPostRequestDecoder.EndOfDataDecoderException();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData next() {
        checkDestroyed();
        if (hasNext()) {
            List<InterfaceHttpData> list = this.bodyListHttpData;
            int i = this.bodyListHttpDataRank;
            this.bodyListHttpDataRank = i + 1;
            return list.get(i);
        }
        return null;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData currentPartialHttpData() {
        return this.currentAttribute;
    }

    private void parseBody() {
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
                return;
            }
            return;
        }
        parseBodyAttributes();
    }

    protected void addHttpData(InterfaceHttpData data) {
        if (data == null) {
            return;
        }
        if (this.maxFields > 0 && this.bodyListHttpData.size() >= this.maxFields) {
            throw new HttpPostRequestDecoder.TooManyFormFieldsException();
        }
        List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
        if (datas == null) {
            datas = new ArrayList(1);
            this.bodyMapHttpData.put(data.getName(), datas);
        }
        datas.add(data);
        this.bodyListHttpData.add(data);
    }

    private void parseBodyAttributesStandard() {
        int firstpos = this.undecodedChunk.readerIndex();
        int currentpos = firstpos;
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
        }
        boolean contRead = true;
        while (this.undecodedChunk.isReadable() && contRead) {
            try {
                char read = (char) this.undecodedChunk.readUnsignedByte();
                currentpos++;
                switch (this.currentStatus) {
                    case DISPOSITION:
                        if (read != '=') {
                            if (read == '&' || (this.isLastChunk && !this.undecodedChunk.isReadable())) {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                                String key = decodeAttribute(this.undecodedChunk.toString(firstpos, (read == '&' ? currentpos - 1 : currentpos) - firstpos, this.charset), this.charset);
                                if (!key.isEmpty()) {
                                    this.currentAttribute = this.factory.createAttribute(this.request, key);
                                    this.currentAttribute.setValue("");
                                    addHttpData(this.currentAttribute);
                                }
                                this.currentAttribute = null;
                                firstpos = currentpos;
                                contRead = true;
                                break;
                            }
                        } else {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
                            int equalpos = currentpos - 1;
                            this.currentAttribute = this.factory.createAttribute(this.request, decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset));
                            firstpos = currentpos;
                            break;
                        }
                        break;
                    case FIELD:
                        if (read == '&') {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                            setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, (currentpos - 1) - firstpos));
                            firstpos = currentpos;
                            contRead = true;
                            break;
                        } else if (read == '\r') {
                            if (this.undecodedChunk.isReadable()) {
                                currentpos++;
                                if (((char) this.undecodedChunk.readUnsignedByte()) == '\n') {
                                    this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                    setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, (currentpos - 2) - firstpos));
                                    firstpos = currentpos;
                                    contRead = false;
                                    break;
                                } else {
                                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
                                }
                            } else {
                                currentpos--;
                                break;
                            }
                        } else if (read != '\n') {
                            break;
                        } else {
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                            setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, (currentpos - 1) - firstpos));
                            firstpos = currentpos;
                            contRead = false;
                            break;
                        }
                    default:
                        contRead = false;
                        break;
                }
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
                this.undecodedChunk.readerIndex(firstpos);
                throw e;
            } catch (IOException e2) {
                this.undecodedChunk.readerIndex(firstpos);
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            } catch (IllegalArgumentException e3) {
                this.undecodedChunk.readerIndex(firstpos);
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
            }
        }
        if (this.isLastChunk && this.currentAttribute != null) {
            int ampersandpos = currentpos;
            if (ampersandpos > firstpos) {
                setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, ampersandpos - firstpos));
            } else if (!this.currentAttribute.isCompleted()) {
                setFinalBuffer(Unpooled.EMPTY_BUFFER);
            }
            firstpos = currentpos;
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
        } else if (contRead && this.currentAttribute != null && this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
            this.currentAttribute.addContent(this.undecodedChunk.retainedSlice(firstpos, currentpos - firstpos), false);
            firstpos = currentpos;
        }
        this.undecodedChunk.readerIndex(firstpos);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:17:0x004c. Please report as an issue. */
    private void parseBodyAttributes() {
        if (this.undecodedChunk == null) {
            return;
        }
        if (!this.undecodedChunk.hasArray()) {
            parseBodyAttributesStandard();
            return;
        }
        HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(this.undecodedChunk);
        int firstpos = this.undecodedChunk.readerIndex();
        int currentpos = firstpos;
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
        }
        boolean contRead = true;
        while (true) {
            try {
                if (sao.pos < sao.limit) {
                    byte[] bArr = sao.bytes;
                    int i = sao.pos;
                    sao.pos = i + 1;
                    char read = (char) (bArr[i] & 255);
                    currentpos++;
                    switch (this.currentStatus) {
                        case DISPOSITION:
                            if (read == '=') {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
                                int equalpos = currentpos - 1;
                                this.currentAttribute = this.factory.createAttribute(this.request, decodeAttribute(this.undecodedChunk.toString(firstpos, equalpos - firstpos, this.charset), this.charset));
                                firstpos = currentpos;
                            } else if (read == '&' || (this.isLastChunk && !this.undecodedChunk.isReadable())) {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                                String key = decodeAttribute(this.undecodedChunk.toString(firstpos, (read == '&' ? currentpos - 1 : currentpos) - firstpos, this.charset), this.charset);
                                if (!key.isEmpty()) {
                                    this.currentAttribute = this.factory.createAttribute(this.request, key);
                                    this.currentAttribute.setValue("");
                                    addHttpData(this.currentAttribute);
                                }
                                this.currentAttribute = null;
                                firstpos = currentpos;
                                contRead = true;
                            }
                            break;
                        case FIELD:
                            if (read == '&') {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.DISPOSITION;
                                setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, (currentpos - 1) - firstpos));
                                firstpos = currentpos;
                                contRead = true;
                            } else if (read == '\r') {
                                if (sao.pos < sao.limit) {
                                    byte[] bArr2 = sao.bytes;
                                    int i2 = sao.pos;
                                    sao.pos = i2 + 1;
                                    currentpos++;
                                    if (((char) (bArr2[i2] & 255)) == '\n') {
                                        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                        sao.setReadPosition(0);
                                        setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, (currentpos - 2) - firstpos));
                                        firstpos = currentpos;
                                        contRead = false;
                                        break;
                                    } else {
                                        sao.setReadPosition(0);
                                        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad end of line");
                                    }
                                } else if (sao.limit > 0) {
                                    currentpos--;
                                }
                            } else if (read == '\n') {
                                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE;
                                sao.setReadPosition(0);
                                setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, (currentpos - 1) - firstpos));
                                firstpos = currentpos;
                                contRead = false;
                                break;
                            }
                        default:
                            sao.setReadPosition(0);
                            contRead = false;
                            break;
                    }
                }
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
                this.undecodedChunk.readerIndex(firstpos);
                throw e;
            } catch (IOException e2) {
                this.undecodedChunk.readerIndex(firstpos);
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            } catch (IllegalArgumentException e3) {
                this.undecodedChunk.readerIndex(firstpos);
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
            }
        }
        if (this.isLastChunk && this.currentAttribute != null) {
            int ampersandpos = currentpos;
            if (ampersandpos > firstpos) {
                setFinalBuffer(this.undecodedChunk.retainedSlice(firstpos, ampersandpos - firstpos));
            } else if (!this.currentAttribute.isCompleted()) {
                setFinalBuffer(Unpooled.EMPTY_BUFFER);
            }
            firstpos = currentpos;
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
        } else if (contRead && this.currentAttribute != null && this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FIELD) {
            this.currentAttribute.addContent(this.undecodedChunk.retainedSlice(firstpos, currentpos - firstpos), false);
            firstpos = currentpos;
        }
        this.undecodedChunk.readerIndex(firstpos);
    }

    private void setFinalBuffer(ByteBuf buffer) throws IOException {
        this.currentAttribute.addContent(buffer, true);
        ByteBuf decodedBuf = decodeAttribute(this.currentAttribute.getByteBuf(), this.charset);
        if (decodedBuf != null) {
            this.currentAttribute.setContent(decodedBuf);
        }
        addHttpData(this.currentAttribute);
        this.currentAttribute = null;
    }

    private static String decodeAttribute(String s, Charset charset) {
        try {
            return QueryStringDecoder.decodeComponent(s, charset);
        } catch (IllegalArgumentException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("Bad string: '" + s + '\'', e);
        }
    }

    private static ByteBuf decodeAttribute(ByteBuf b, Charset charset) {
        int firstEscaped = b.forEachByte(new UrlEncodedDetector());
        if (firstEscaped == -1) {
            return null;
        }
        ByteBuf buf = b.alloc().buffer(b.readableBytes());
        UrlDecoder urlDecode = new UrlDecoder(buf);
        int idx = b.forEachByte(urlDecode);
        if (urlDecode.nextEscapedIdx != 0) {
            if (idx == -1) {
                idx = b.readableBytes() - 1;
            }
            int idx2 = idx - (urlDecode.nextEscapedIdx - 1);
            buf.release();
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(String.format("Invalid hex byte at index '%d' in string: '%s'", Integer.valueOf(idx2), b.toString(charset)));
        }
        return buf;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void destroy() {
        cleanFiles();
        for (InterfaceHttpData httpData : this.bodyListHttpData) {
            if (httpData.refCnt() > 0) {
                httpData.release();
            }
        }
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void cleanFiles() {
        checkDestroyed();
        this.factory.cleanRequestHttpData(this.request);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void removeHttpDataFromClean(InterfaceHttpData data) {
        checkDestroyed();
        this.factory.removeHttpDataFromClean(this.request, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class UrlEncodedDetector implements ByteProcessor {
        private UrlEncodedDetector() {
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return (value == 37 || value == 43) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class UrlDecoder implements ByteProcessor {
        private byte hiByte;
        private int nextEscapedIdx;
        private final ByteBuf output;

        UrlDecoder(ByteBuf output) {
            this.output = output;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            if (this.nextEscapedIdx != 0) {
                if (this.nextEscapedIdx == 1) {
                    this.hiByte = value;
                    this.nextEscapedIdx++;
                } else {
                    int hi = StringUtil.decodeHexNibble((char) this.hiByte);
                    int lo = StringUtil.decodeHexNibble((char) value);
                    if (hi == -1 || lo == -1) {
                        this.nextEscapedIdx++;
                        return false;
                    }
                    this.output.writeByte((hi << 4) + lo);
                    this.nextEscapedIdx = 0;
                }
            } else if (value == 37) {
                this.nextEscapedIdx = 1;
            } else if (value == 43) {
                this.output.writeByte(32);
            } else {
                this.output.writeByte(value);
            }
            return true;
        }
    }
}
