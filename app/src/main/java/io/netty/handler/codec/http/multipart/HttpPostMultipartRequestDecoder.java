package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes4.dex */
public class HttpPostMultipartRequestDecoder implements InterfaceHttpPostRequestDecoder {
    private static final String FILENAME_ENCODED = HttpHeaderValues.FILENAME.toString() + '*';
    private final List<InterfaceHttpData> bodyListHttpData;
    private int bodyListHttpDataRank;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private Charset charset;
    private Attribute currentAttribute;
    private Map<CharSequence, Attribute> currentFieldAttributes;
    private FileUpload currentFileUpload;
    private HttpPostRequestDecoder.MultiPartStatus currentStatus;
    private boolean destroyed;
    private int discardThreshold;
    private final HttpDataFactory factory;
    private boolean isLastChunk;
    private final int maxBufferedBytes;
    private final int maxFields;
    private final String multipartDataBoundary;
    private String multipartMixedBoundary;
    private final HttpRequest request;
    private ByteBuf undecodedChunk;

    public HttpPostMultipartRequestDecoder(HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) {
        this(factory, request, charset, 128, 1024);
    }

    public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset, int maxFields, int maxBufferedBytes) {
        this.bodyListHttpData = new ArrayList();
        this.bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
        this.discardThreshold = 10485760;
        this.request = (HttpRequest) ObjectUtil.checkNotNull(request, "request");
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
        this.factory = (HttpDataFactory) ObjectUtil.checkNotNull(factory, "factory");
        this.maxFields = maxFields;
        this.maxBufferedBytes = maxBufferedBytes;
        String contentTypeValue = this.request.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue == null) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException("No '" + ((Object) HttpHeaderNames.CONTENT_TYPE) + "' header present.");
        }
        String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentTypeValue);
        if (dataBoundary != null) {
            this.multipartDataBoundary = dataBoundary[0];
            if (dataBoundary.length > 1 && dataBoundary[1] != null) {
                try {
                    this.charset = Charset.forName(dataBoundary[1]);
                } catch (IllegalCharsetNameException e) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                }
            }
        } else {
            this.multipartDataBoundary = null;
        }
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
        try {
            if (request instanceof HttpContent) {
                offer((HttpContent) request);
            } else {
                parseBody();
            }
        } catch (Throwable e2) {
            destroy();
            PlatformDependent.throwException(e2);
        }
    }

    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostMultipartRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean isMultipart() {
        checkDestroyed();
        return true;
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
    public HttpPostMultipartRequestDecoder offer(HttpContent content) {
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
        if (this.currentFileUpload != null) {
            return this.currentFileUpload;
        }
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
        parseBodyMultipart();
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

    private void parseBodyMultipart() {
        if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
            return;
        }
        InterfaceHttpData data = decodeMultipart(this.currentStatus);
        while (data != null) {
            addHttpData(data);
            if (this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE && this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
                data = decodeMultipart(this.currentStatus);
            } else {
                return;
            }
        }
    }

    private InterfaceHttpData decodeMultipart(HttpPostRequestDecoder.MultiPartStatus state) {
        long size;
        long parseLong;
        switch (state) {
            case NOTSTARTED:
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            case PREAMBLE:
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            case HEADERDELIMITER:
                return findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
            case DISPOSITION:
                return findMultipartDisposition();
            case FIELD:
                Charset localCharset = null;
                Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
                if (charsetAttribute != null) {
                    try {
                        localCharset = Charset.forName(charsetAttribute.getValue());
                    } catch (IOException e) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                    } catch (UnsupportedCharsetException e2) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
                    }
                }
                Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
                if (this.currentAttribute == null) {
                    Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
                    if (lengthAttribute != null) {
                        try {
                            parseLong = Long.parseLong(lengthAttribute.getValue());
                        } catch (IOException e3) {
                            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                        } catch (NumberFormatException e4) {
                            size = 0;
                        }
                    } else {
                        parseLong = 0;
                    }
                    size = parseLong;
                    try {
                        if (size > 0) {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()), size);
                        } else {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
                        }
                        if (localCharset != null) {
                            this.currentAttribute.setCharset(localCharset);
                        }
                    } catch (IOException e5) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
                    } catch (IllegalArgumentException e6) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                    } catch (NullPointerException e7) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                    }
                }
                if (!loadDataMultipartOptimized(this.undecodedChunk, this.multipartDataBoundary, this.currentAttribute)) {
                    return null;
                }
                Attribute finalAttribute = this.currentAttribute;
                this.currentAttribute = null;
                this.currentFieldAttributes = null;
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                return finalAttribute;
            case FILEUPLOAD:
                return getFileUpload(this.multipartDataBoundary);
            case MIXEDDELIMITER:
                return findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
            case MIXEDDISPOSITION:
                return findMultipartDisposition();
            case MIXEDFILEUPLOAD:
                return getFileUpload(this.multipartMixedBoundary);
            case PREEPILOGUE:
                return null;
            case EPILOGUE:
                return null;
            default:
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn't reach here.");
        }
    }

    private static void skipControlCharacters(ByteBuf undecodedChunk) {
        if (!undecodedChunk.hasArray()) {
            try {
                skipControlCharactersStandard(undecodedChunk);
                return;
            } catch (IndexOutOfBoundsException e1) {
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e1);
            }
        }
        HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        while (sao.pos < sao.limit) {
            byte[] bArr = sao.bytes;
            int i = sao.pos;
            sao.pos = i + 1;
            char c = (char) (bArr[i] & 255);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                sao.setReadPosition(1);
                return;
            }
        }
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
    }

    private static void skipControlCharactersStandard(ByteBuf undecodedChunk) {
        while (true) {
            char c = (char) undecodedChunk.readUnsignedByte();
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
                return;
            }
        }
    }

    private InterfaceHttpData findMultipartDelimiter(String delimiter, HttpPostRequestDecoder.MultiPartStatus dispositionStatus, HttpPostRequestDecoder.MultiPartStatus closeDelimiterStatus) {
        int readerIndex = this.undecodedChunk.readerIndex();
        try {
            skipControlCharacters(this.undecodedChunk);
            skipOneLine();
            try {
                String newline = readDelimiterOptimized(this.undecodedChunk, delimiter, this.charset);
                if (newline.equals(delimiter)) {
                    this.currentStatus = dispositionStatus;
                    return decodeMultipart(dispositionStatus);
                }
                if (newline.equals(delimiter + "--")) {
                    this.currentStatus = closeDelimiterStatus;
                    if (this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER) {
                        return null;
                    }
                    this.currentFieldAttributes = null;
                    return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
                }
                this.undecodedChunk.readerIndex(readerIndex);
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
            } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e) {
                this.undecodedChunk.readerIndex(readerIndex);
                return null;
            }
        } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
    }

    private InterfaceHttpData findMultipartDisposition() {
        boolean checkSecondArg;
        int readerIndex = this.undecodedChunk.readerIndex();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
            this.currentFieldAttributes = new TreeMap(CaseIgnoringComparator.INSTANCE);
        }
        while (!skipOneLine()) {
            try {
                skipControlCharacters(this.undecodedChunk);
                String newline = readLineOptimized(this.undecodedChunk, this.charset);
                String[] contents = splitMultipartHeader(newline);
                if (HttpHeaderNames.CONTENT_DISPOSITION.contentEqualsIgnoreCase(contents[0])) {
                    if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                        checkSecondArg = HttpHeaderValues.FORM_DATA.contentEqualsIgnoreCase(contents[1]);
                    } else {
                        checkSecondArg = HttpHeaderValues.ATTACHMENT.contentEqualsIgnoreCase(contents[1]) || HttpHeaderValues.FILE.contentEqualsIgnoreCase(contents[1]);
                    }
                    if (checkSecondArg) {
                        for (int i = 2; i < contents.length; i++) {
                            String[] values = contents[i].split("=", 2);
                            try {
                                Attribute attribute = getContentDispositionAttribute(values);
                                this.currentFieldAttributes.put(attribute.getName(), attribute);
                            } catch (IllegalArgumentException e) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                            } catch (NullPointerException e2) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
                            }
                        }
                    } else {
                        continue;
                    }
                } else if (HttpHeaderNames.CONTENT_TRANSFER_ENCODING.contentEqualsIgnoreCase(contents[0])) {
                    try {
                        this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_TRANSFER_ENCODING, this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString(), cleanString(contents[1])));
                    } catch (IllegalArgumentException e3) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                    } catch (NullPointerException e4) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
                    }
                } else if (HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(contents[0])) {
                    try {
                        this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_LENGTH, this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_LENGTH.toString(), cleanString(contents[1])));
                    } catch (IllegalArgumentException e5) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
                    } catch (NullPointerException e6) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                    }
                } else if (!HttpHeaderNames.CONTENT_TYPE.contentEqualsIgnoreCase(contents[0])) {
                    continue;
                } else {
                    if (HttpHeaderValues.MULTIPART_MIXED.contentEqualsIgnoreCase(contents[1])) {
                        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                            String values2 = StringUtil.substringAfter(contents[2], '=');
                            this.multipartMixedBoundary = "--" + values2;
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                            return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER);
                        }
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
                    }
                    for (int i2 = 1; i2 < contents.length; i2++) {
                        String charsetHeader = HttpHeaderValues.CHARSET.toString();
                        if (contents[i2].regionMatches(true, 0, charsetHeader, 0, charsetHeader.length())) {
                            String values3 = StringUtil.substringAfter(contents[i2], '=');
                            try {
                                this.currentFieldAttributes.put(HttpHeaderValues.CHARSET, this.factory.createAttribute(this.request, charsetHeader, cleanString(values3)));
                            } catch (IllegalArgumentException e7) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                            } catch (NullPointerException e8) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e8);
                            }
                        } else {
                            String values4 = contents[i2];
                            if (values4.contains("=")) {
                                String name = StringUtil.substringBefore(contents[i2], '=');
                                String values5 = StringUtil.substringAfter(contents[i2], '=');
                                try {
                                    this.currentFieldAttributes.put(name, this.factory.createAttribute(this.request, cleanString(name), values5));
                                } catch (IllegalArgumentException e9) {
                                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e9);
                                } catch (NullPointerException e10) {
                                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e10);
                                }
                            } else {
                                try {
                                    Attribute attribute2 = this.factory.createAttribute(this.request, cleanString(contents[0]), contents[i2]);
                                    this.currentFieldAttributes.put(attribute2.getName(), attribute2);
                                } catch (IllegalArgumentException e11) {
                                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e11);
                                } catch (NullPointerException e12) {
                                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e12);
                                }
                            }
                        }
                    }
                }
            } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e13) {
                this.undecodedChunk.readerIndex(readerIndex);
                return null;
            }
        }
        Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
            if (filenameAttribute != null) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD;
                return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD);
            }
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
            return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FIELD);
        }
        if (filenameAttribute != null) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD;
            return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD);
        }
        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Filename not found");
    }

    private Attribute getContentDispositionAttribute(String... values) {
        String name = cleanString(values[0]);
        String value = values[1];
        if (HttpHeaderValues.FILENAME.contentEquals(name)) {
            int last = value.length() - 1;
            if (last > 0 && value.charAt(0) == '\"' && value.charAt(last) == '\"') {
                value = value.substring(1, last);
            }
        } else if (FILENAME_ENCODED.equals(name)) {
            try {
                name = HttpHeaderValues.FILENAME.toString();
                String[] split = cleanString(value).split("'", 3);
                value = QueryStringDecoder.decodeComponent(split[2], Charset.forName(split[0]));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            } catch (UnsupportedCharsetException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            }
        } else {
            value = cleanString(value);
        }
        return this.factory.createAttribute(this.request, name, value);
    }

    protected InterfaceHttpData getFileUpload(String delimiter) {
        long size;
        long size2;
        String contentType;
        Attribute encoding = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        Charset localCharset = this.charset;
        HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
        if (encoding != null) {
            try {
                String code = encoding.getValue().toLowerCase();
                if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
                    localCharset = CharsetUtil.US_ASCII;
                } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
                    localCharset = CharsetUtil.ISO_8859_1;
                    mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
                } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                    mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
                } else {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + code);
                }
            } catch (IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
        }
        Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
        if (charsetAttribute != null) {
            try {
                localCharset = Charset.forName(charsetAttribute.getValue());
            } catch (IOException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            } catch (UnsupportedCharsetException e3) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
            }
        }
        if (this.currentFileUpload == null) {
            Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
            Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
            Attribute contentTypeAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TYPE);
            Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
            if (lengthAttribute == null) {
                size2 = 0;
            } else {
                try {
                    size2 = Long.parseLong(lengthAttribute.getValue());
                } catch (IOException e4) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
                } catch (NumberFormatException e5) {
                    size = 0;
                }
            }
            size = size2;
            if (contentTypeAttribute != null) {
                try {
                    contentType = contentTypeAttribute.getValue();
                } catch (IOException e6) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                } catch (IllegalArgumentException e7) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                } catch (NullPointerException e8) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e8);
                }
            } else {
                contentType = HttpPostBodyUtil.DEFAULT_BINARY_CONTENT_TYPE;
            }
            this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentType, mechanism.value(), localCharset, size);
        }
        if (!loadDataMultipartOptimized(this.undecodedChunk, delimiter, this.currentFileUpload) || !this.currentFileUpload.isCompleted()) {
            return null;
        }
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
            this.currentFieldAttributes = null;
        } else {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
            cleanMixedAttributes();
        }
        FileUpload fileUpload = this.currentFileUpload;
        this.currentFileUpload = null;
        return fileUpload;
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

    private void cleanMixedAttributes() {
        this.currentFieldAttributes.remove(HttpHeaderValues.CHARSET);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_LENGTH);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TYPE);
        this.currentFieldAttributes.remove(HttpHeaderValues.FILENAME);
    }

    private static String readLineOptimized(ByteBuf undecodedChunk, Charset charset) {
        int readerIndex = undecodedChunk.readerIndex();
        ByteBuf line = null;
        try {
            if (undecodedChunk.isReadable()) {
                int posLfOrCrLf = HttpPostBodyUtil.findLineBreak(undecodedChunk, undecodedChunk.readerIndex());
                if (posLfOrCrLf <= 0) {
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                try {
                    line = undecodedChunk.alloc().heapBuffer(posLfOrCrLf);
                    line.writeBytes(undecodedChunk, posLfOrCrLf);
                    byte nextByte = undecodedChunk.readByte();
                    if (nextByte == 13) {
                        undecodedChunk.readByte();
                    }
                    return line.toString(charset);
                } finally {
                    line.release();
                }
            }
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        } catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
    }

    private static String readDelimiterOptimized(ByteBuf undecodedChunk, String delimiter, Charset charset) {
        int readerIndex = undecodedChunk.readerIndex();
        byte[] bdelimiter = delimiter.getBytes(charset);
        int delimiterLength = bdelimiter.length;
        try {
            int delimiterPos = HttpPostBodyUtil.findDelimiter(undecodedChunk, readerIndex, bdelimiter, false);
            if (delimiterPos < 0) {
                undecodedChunk.readerIndex(readerIndex);
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
            }
            StringBuilder sb = new StringBuilder(delimiter);
            undecodedChunk.readerIndex(readerIndex + delimiterPos + delimiterLength);
            if (undecodedChunk.isReadable()) {
                byte nextByte = undecodedChunk.readByte();
                if (nextByte == 13) {
                    if (undecodedChunk.readByte() == 10) {
                        return sb.toString();
                    }
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                if (nextByte == 10) {
                    return sb.toString();
                }
                if (nextByte == 45) {
                    sb.append('-');
                    if (undecodedChunk.readByte() == 45) {
                        sb.append('-');
                        if (undecodedChunk.isReadable()) {
                            byte nextByte2 = undecodedChunk.readByte();
                            if (nextByte2 == 13) {
                                if (undecodedChunk.readByte() == 10) {
                                    return sb.toString();
                                }
                                undecodedChunk.readerIndex(readerIndex);
                                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                            }
                            if (nextByte2 == 10) {
                                return sb.toString();
                            }
                            undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
                            return sb.toString();
                        }
                        return sb.toString();
                    }
                }
            }
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        } catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
    }

    private static void rewriteCurrentBuffer(ByteBuf buffer, int lengthToSkip) {
        if (lengthToSkip == 0) {
            return;
        }
        int readerIndex = buffer.readerIndex();
        int readableBytes = buffer.readableBytes();
        if (readableBytes == lengthToSkip) {
            buffer.readerIndex(readerIndex);
            buffer.writerIndex(readerIndex);
        } else {
            buffer.setBytes(readerIndex, buffer, readerIndex + lengthToSkip, readableBytes - lengthToSkip);
            buffer.readerIndex(readerIndex);
            buffer.writerIndex((readerIndex + readableBytes) - lengthToSkip);
        }
    }

    private static boolean loadDataMultipartOptimized(ByteBuf undecodedChunk, String delimiter, HttpData httpData) {
        if (!undecodedChunk.isReadable()) {
            return false;
        }
        int startReaderIndex = undecodedChunk.readerIndex();
        byte[] bdelimiter = delimiter.getBytes(httpData.getCharset());
        int posDelimiter = HttpPostBodyUtil.findDelimiter(undecodedChunk, startReaderIndex, bdelimiter, true);
        if (posDelimiter < 0) {
            int readableBytes = undecodedChunk.readableBytes();
            int lastPosition = (readableBytes - bdelimiter.length) - 1;
            if (lastPosition < 0) {
                lastPosition = 0;
            }
            int posDelimiter2 = HttpPostBodyUtil.findLastLineBreak(undecodedChunk, startReaderIndex + lastPosition);
            if (posDelimiter2 < 0 && httpData.definedLength() == (httpData.length() + readableBytes) - 1 && undecodedChunk.getByte((readableBytes + startReaderIndex) - 1) == 13) {
                lastPosition = 0;
                posDelimiter2 = readableBytes - 1;
            }
            if (posDelimiter2 < 0) {
                ByteBuf content = undecodedChunk.copy();
                try {
                    httpData.addContent(content, false);
                    undecodedChunk.readerIndex(startReaderIndex);
                    undecodedChunk.writerIndex(startReaderIndex);
                    return false;
                } catch (IOException e) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                }
            }
            int posDelimiter3 = posDelimiter2 + lastPosition;
            if (posDelimiter3 == 0) {
                return false;
            }
            ByteBuf content2 = undecodedChunk.copy(startReaderIndex, posDelimiter3);
            try {
                httpData.addContent(content2, false);
                rewriteCurrentBuffer(undecodedChunk, posDelimiter3);
                return false;
            } catch (IOException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            }
        }
        ByteBuf content3 = undecodedChunk.copy(startReaderIndex, posDelimiter);
        try {
            httpData.addContent(content3, true);
            rewriteCurrentBuffer(undecodedChunk, posDelimiter);
            return true;
        } catch (IOException e3) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
        }
    }

    private static String cleanString(String field) {
        int size = field.length();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            char nextChar = field.charAt(i);
            switch (nextChar) {
                case '\t':
                case ',':
                case ':':
                case ';':
                case '=':
                    sb.append(' ');
                    break;
                case '\"':
                    break;
                default:
                    sb.append(nextChar);
                    break;
            }
        }
        return sb.toString().trim();
    }

    private boolean skipOneLine() {
        if (!this.undecodedChunk.isReadable()) {
            return false;
        }
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
            if (!this.undecodedChunk.isReadable()) {
                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                return false;
            }
            if (this.undecodedChunk.readByte() == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
            return false;
        }
        if (nextByte == 10) {
            return true;
        }
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
        return false;
    }

    private static String[] splitMultipartHeader(String sb) {
        String[] values;
        char ch;
        ArrayList<String> headers = new ArrayList<>(1);
        int nameStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
        int nameEnd = nameStart;
        while (nameEnd < sb.length() && (ch = sb.charAt(nameEnd)) != ':' && !Character.isWhitespace(ch)) {
            nameEnd++;
        }
        int colonEnd = nameEnd;
        while (true) {
            if (colonEnd >= sb.length()) {
                break;
            }
            if (sb.charAt(colonEnd) != ':') {
                colonEnd++;
            } else {
                colonEnd++;
                break;
            }
        }
        int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
        int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
        headers.add(sb.substring(nameStart, nameEnd));
        String svalue = valueStart >= valueEnd ? "" : sb.substring(valueStart, valueEnd);
        if (svalue.indexOf(59) >= 0) {
            values = splitMultipartHeaderValues(svalue);
        } else {
            values = svalue.split(",");
        }
        for (String value : values) {
            headers.add(value.trim());
        }
        String[] array = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            array[i] = headers.get(i);
        }
        return array;
    }

    private static String[] splitMultipartHeaderValues(String svalue) {
        List<String> values = InternalThreadLocalMap.get().arrayList(1);
        boolean inQuote = false;
        boolean escapeNext = false;
        int start = 0;
        for (int i = 0; i < svalue.length(); i++) {
            char c = svalue.charAt(i);
            if (inQuote) {
                if (escapeNext) {
                    escapeNext = false;
                } else if (c == '\\') {
                    escapeNext = true;
                } else if (c == '\"') {
                    inQuote = false;
                }
            } else if (c == '\"') {
                inQuote = true;
            } else if (c == ';') {
                values.add(svalue.substring(start, i));
                start = i + 1;
            }
        }
        values.add(svalue.substring(start));
        return (String[]) values.toArray(EmptyArrays.EMPTY_STRINGS);
    }

    int getCurrentAllocatedCapacity() {
        return this.undecodedChunk.capacity();
    }
}
