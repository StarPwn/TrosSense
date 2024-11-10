package io.netty.handler.ssl;

import java.security.cert.Certificate;
import java.util.Map;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public interface OpenSslSession extends SSLSession {
    OpenSslSessionContext getSessionContext();

    void handshakeFinished(byte[] bArr, String str, String str2, byte[] bArr2, byte[][] bArr3, long j, long j2) throws SSLException;

    Map<String, Object> keyValueStorage();

    void prepareHandshake();

    OpenSslSessionId sessionId();

    void setLastAccessedTime(long j);

    void setLocalCertificate(Certificate[] certificateArr);

    void setSessionDetails(long j, long j2, OpenSslSessionId openSslSessionId, Map<String, Object> map);

    void tryExpandApplicationBufferSize(int i);
}
