package org.jose4j.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes5.dex */
public class Response implements SimpleResponse {
    private String body;
    private Map<String, List<String>> headers = new HashMap();
    private int statusCode;
    private String statusMessage;

    public Response(int statusCode, String statusMessage, Map<String, List<String>> headers, String body) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            String name = normalizeHeaderName(header.getKey());
            this.headers.put(name, header.getValue());
        }
        this.body = body;
    }

    @Override // org.jose4j.http.SimpleResponse
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override // org.jose4j.http.SimpleResponse
    public String getStatusMessage() {
        return this.statusMessage;
    }

    @Override // org.jose4j.http.SimpleResponse
    public Collection<String> getHeaderNames() {
        return this.headers.keySet();
    }

    @Override // org.jose4j.http.SimpleResponse
    public List<String> getHeaderValues(String name) {
        return this.headers.get(normalizeHeaderName(name));
    }

    @Override // org.jose4j.http.SimpleResponse
    public String getBody() {
        return this.body;
    }

    private String normalizeHeaderName(String name) {
        if (name != null) {
            return name.toLowerCase().trim();
        }
        return null;
    }

    public String toString() {
        return "SimpleResponse{statusCode=" + this.statusCode + ", statusMessage='" + this.statusMessage + "', headers=" + this.headers + ", body='" + this.body + "'}";
    }
}
