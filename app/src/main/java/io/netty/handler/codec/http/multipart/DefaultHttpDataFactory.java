package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class DefaultHttpDataFactory implements HttpDataFactory {
    public static final long MAXSIZE = -1;
    public static final long MINSIZE = 16384;
    private String baseDir;
    private Charset charset;
    private final boolean checkSize;
    private boolean deleteOnExit;
    private long maxSize;
    private long minSize;
    private final Map<HttpRequest, List<HttpData>> requestFileDeleteMap;
    private final boolean useDisk;

    public DefaultHttpDataFactory() {
        this.maxSize = -1L;
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.requestFileDeleteMap = Collections.synchronizedMap(new IdentityHashMap());
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = 16384L;
    }

    public DefaultHttpDataFactory(Charset charset) {
        this();
        this.charset = charset;
    }

    public DefaultHttpDataFactory(boolean useDisk) {
        this.maxSize = -1L;
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.requestFileDeleteMap = Collections.synchronizedMap(new IdentityHashMap());
        this.useDisk = useDisk;
        this.checkSize = false;
    }

    public DefaultHttpDataFactory(boolean useDisk, Charset charset) {
        this(useDisk);
        this.charset = charset;
    }

    public DefaultHttpDataFactory(long minSize) {
        this.maxSize = -1L;
        this.charset = HttpConstants.DEFAULT_CHARSET;
        this.requestFileDeleteMap = Collections.synchronizedMap(new IdentityHashMap());
        this.useDisk = false;
        this.checkSize = true;
        this.minSize = minSize;
    }

    public DefaultHttpDataFactory(long minSize, Charset charset) {
        this(minSize);
        this.charset = charset;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setDeleteOnExit(boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public void setMaxLimit(long maxSize) {
        this.maxSize = maxSize;
    }

    private List<HttpData> getList(HttpRequest request) {
        List<HttpData> list = this.requestFileDeleteMap.get(request);
        if (list == null) {
            List<HttpData> list2 = new ArrayList<>();
            this.requestFileDeleteMap.put(request, list2);
            return list2;
        }
        return list;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public Attribute createAttribute(HttpRequest request, String name) {
        if (this.useDisk) {
            Attribute attribute = new DiskAttribute(name, this.charset, this.baseDir, this.deleteOnExit);
            attribute.setMaxSize(this.maxSize);
            List<HttpData> list = getList(request);
            list.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            Attribute attribute2 = new MixedAttribute(name, this.minSize, this.charset, this.baseDir, this.deleteOnExit);
            attribute2.setMaxSize(this.maxSize);
            List<HttpData> list2 = getList(request);
            list2.add(attribute2);
            return attribute2;
        }
        MemoryAttribute attribute3 = new MemoryAttribute(name);
        attribute3.setMaxSize(this.maxSize);
        return attribute3;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public Attribute createAttribute(HttpRequest request, String name, long definedSize) {
        if (this.useDisk) {
            Attribute attribute = new DiskAttribute(name, definedSize, this.charset, this.baseDir, this.deleteOnExit);
            attribute.setMaxSize(this.maxSize);
            List<HttpData> list = getList(request);
            list.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            Attribute attribute2 = new MixedAttribute(name, definedSize, this.minSize, this.charset, this.baseDir, this.deleteOnExit);
            attribute2.setMaxSize(this.maxSize);
            List<HttpData> list2 = getList(request);
            list2.add(attribute2);
            return attribute2;
        }
        MemoryAttribute attribute3 = new MemoryAttribute(name, definedSize);
        attribute3.setMaxSize(this.maxSize);
        return attribute3;
    }

    private static void checkHttpDataSize(HttpData data) {
        try {
            data.checkSize(data.length());
        } catch (IOException e) {
            throw new IllegalArgumentException("Attribute bigger than maxSize allowed");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public Attribute createAttribute(HttpRequest request, String name, String value) {
        Attribute attribute;
        if (this.useDisk) {
            try {
                attribute = new DiskAttribute(name, value, this.charset, this.baseDir, this.deleteOnExit);
                attribute.setMaxSize(this.maxSize);
            } catch (IOException e) {
                Attribute attribute2 = new MixedAttribute(name, value, this.minSize, this.charset, this.baseDir, this.deleteOnExit);
                attribute2.setMaxSize(this.maxSize);
                attribute = attribute2;
            }
            checkHttpDataSize(attribute);
            List<HttpData> list = getList(request);
            list.add(attribute);
            return attribute;
        }
        if (this.checkSize) {
            Attribute attribute3 = new MixedAttribute(name, value, this.minSize, this.charset, this.baseDir, this.deleteOnExit);
            attribute3.setMaxSize(this.maxSize);
            checkHttpDataSize(attribute3);
            List<HttpData> list2 = getList(request);
            list2.add(attribute3);
            return attribute3;
        }
        try {
            MemoryAttribute attribute4 = new MemoryAttribute(name, value, this.charset);
            attribute4.setMaxSize(this.maxSize);
            checkHttpDataSize(attribute4);
            return attribute4;
        } catch (IOException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public FileUpload createFileUpload(HttpRequest request, String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
        if (this.useDisk) {
            FileUpload fileUpload = new DiskFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.baseDir, this.deleteOnExit);
            fileUpload.setMaxSize(this.maxSize);
            checkHttpDataSize(fileUpload);
            List<HttpData> list = getList(request);
            list.add(fileUpload);
            return fileUpload;
        }
        if (this.checkSize) {
            FileUpload fileUpload2 = new MixedFileUpload(name, filename, contentType, contentTransferEncoding, charset, size, this.minSize, this.baseDir, this.deleteOnExit);
            fileUpload2.setMaxSize(this.maxSize);
            checkHttpDataSize(fileUpload2);
            List<HttpData> list2 = getList(request);
            list2.add(fileUpload2);
            return fileUpload2;
        }
        MemoryFileUpload fileUpload3 = new MemoryFileUpload(name, filename, contentType, contentTransferEncoding, charset, size);
        fileUpload3.setMaxSize(this.maxSize);
        checkHttpDataSize(fileUpload3);
        return fileUpload3;
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public void removeHttpDataFromClean(HttpRequest request, InterfaceHttpData data) {
        List<HttpData> list;
        if (!(data instanceof HttpData) || (list = this.requestFileDeleteMap.get(request)) == null) {
            return;
        }
        Iterator<HttpData> i = list.iterator();
        while (i.hasNext()) {
            HttpData n = i.next();
            if (n == data) {
                i.remove();
                if (list.isEmpty()) {
                    this.requestFileDeleteMap.remove(request);
                    return;
                }
                return;
            }
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public void cleanRequestHttpData(HttpRequest request) {
        List<HttpData> list = this.requestFileDeleteMap.remove(request);
        if (list != null) {
            for (HttpData data : list) {
                data.release();
            }
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public void cleanAllHttpData() {
        Iterator<Map.Entry<HttpRequest, List<HttpData>>> i = this.requestFileDeleteMap.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<HttpRequest, List<HttpData>> e = i.next();
            List<HttpData> list = e.getValue();
            for (HttpData data : list) {
                data.release();
            }
            i.remove();
        }
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public void cleanRequestHttpDatas(HttpRequest request) {
        cleanRequestHttpData(request);
    }

    @Override // io.netty.handler.codec.http.multipart.HttpDataFactory
    public void cleanAllHttpDatas() {
        cleanAllHttpData();
    }
}
