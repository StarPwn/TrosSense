package io.netty.handler.ssl;

import io.netty.handler.ssl.JdkAlpnSslEngine;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngine;

/* loaded from: classes4.dex */
final class BouncyCastleAlpnSslEngine extends JdkAlpnSslEngine {
    /* JADX INFO: Access modifiers changed from: package-private */
    public BouncyCastleAlpnSslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
        super(engine, applicationNegotiator, isServer, new BiConsumer<SSLEngine, JdkAlpnSslEngine.AlpnSelector>() { // from class: io.netty.handler.ssl.BouncyCastleAlpnSslEngine.1
            @Override // java.util.function.BiConsumer
            public void accept(SSLEngine e, JdkAlpnSslEngine.AlpnSelector s) {
                BouncyCastleAlpnSslUtils.setHandshakeApplicationProtocolSelector(e, s);
            }
        }, new BiConsumer<SSLEngine, List<String>>() { // from class: io.netty.handler.ssl.BouncyCastleAlpnSslEngine.2
            @Override // java.util.function.BiConsumer
            public void accept(SSLEngine e, List<String> p) {
                BouncyCastleAlpnSslUtils.setApplicationProtocols(e, p);
            }
        });
    }

    @Override // io.netty.handler.ssl.JdkAlpnSslEngine, javax.net.ssl.SSLEngine
    public String getApplicationProtocol() {
        return BouncyCastleAlpnSslUtils.getApplicationProtocol(getWrappedEngine());
    }

    @Override // io.netty.handler.ssl.JdkAlpnSslEngine, javax.net.ssl.SSLEngine
    public String getHandshakeApplicationProtocol() {
        return BouncyCastleAlpnSslUtils.getHandshakeApplicationProtocol(getWrappedEngine());
    }

    @Override // io.netty.handler.ssl.JdkAlpnSslEngine, javax.net.ssl.SSLEngine
    public void setHandshakeApplicationProtocolSelector(BiFunction<SSLEngine, List<String>, String> selector) {
        BouncyCastleAlpnSslUtils.setHandshakeApplicationProtocolSelector(getWrappedEngine(), selector);
    }

    @Override // io.netty.handler.ssl.JdkAlpnSslEngine, javax.net.ssl.SSLEngine
    public BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
        return BouncyCastleAlpnSslUtils.getHandshakeApplicationProtocolSelector(getWrappedEngine());
    }
}
