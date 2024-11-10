package io.netty.handler.ssl;

import io.netty.handler.ssl.JdkApplicationProtocolNegotiator;
import java.nio.ByteBuffer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class JdkAlpnSslEngine extends JdkSslEngine {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AlpnSelector alpnSelector;
    private final JdkApplicationProtocolNegotiator.ProtocolSelectionListener selectionListener;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class AlpnSelector implements BiFunction<SSLEngine, List<String>, String> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private boolean called;
        private final JdkApplicationProtocolNegotiator.ProtocolSelector selector;

        AlpnSelector(JdkApplicationProtocolNegotiator.ProtocolSelector selector) {
            this.selector = selector;
        }

        @Override // java.util.function.BiFunction
        public String apply(SSLEngine sslEngine, List<String> strings) {
            if (this.called) {
                throw new AssertionError();
            }
            this.called = true;
            try {
                String selected = this.selector.select(strings);
                return selected == null ? "" : selected;
            } catch (Exception e) {
                return null;
            }
        }

        void checkUnsupported() {
            if (this.called) {
                return;
            }
            String protocol = JdkAlpnSslEngine.this.getApplicationProtocol();
            if (protocol == null) {
                throw new AssertionError();
            }
            if (protocol.isEmpty()) {
                this.selector.unsupported();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkAlpnSslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer, BiConsumer<SSLEngine, AlpnSelector> setHandshakeApplicationProtocolSelector, BiConsumer<SSLEngine, List<String>> setApplicationProtocols) {
        super(engine);
        if (isServer) {
            this.selectionListener = null;
            this.alpnSelector = new AlpnSelector(applicationNegotiator.protocolSelectorFactory().newSelector(this, new LinkedHashSet(applicationNegotiator.protocols())));
            setHandshakeApplicationProtocolSelector.accept(engine, this.alpnSelector);
        } else {
            this.selectionListener = applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols());
            this.alpnSelector = null;
            setApplicationProtocols.accept(engine, applicationNegotiator.protocols());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkAlpnSslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer) {
        this(engine, applicationNegotiator, isServer, new BiConsumer<SSLEngine, AlpnSelector>() { // from class: io.netty.handler.ssl.JdkAlpnSslEngine.1
            @Override // java.util.function.BiConsumer
            public void accept(SSLEngine e, AlpnSelector s) {
                JdkAlpnSslUtils.setHandshakeApplicationProtocolSelector(e, s);
            }
        }, new BiConsumer<SSLEngine, List<String>>() { // from class: io.netty.handler.ssl.JdkAlpnSslEngine.2
            @Override // java.util.function.BiConsumer
            public void accept(SSLEngine e, List<String> p) {
                JdkAlpnSslUtils.setApplicationProtocols(e, p);
            }
        });
    }

    private SSLEngineResult verifyProtocolSelection(SSLEngineResult result) throws SSLException {
        if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
            if (this.alpnSelector == null) {
                try {
                    String protocol = getApplicationProtocol();
                    if (protocol == null) {
                        throw new AssertionError();
                    }
                    if (protocol.isEmpty()) {
                        this.selectionListener.unsupported();
                    } else {
                        this.selectionListener.selected(protocol);
                    }
                } catch (Throwable e) {
                    throw SslUtils.toSSLHandshakeException(e);
                }
            } else {
                if (this.selectionListener != null) {
                    throw new AssertionError();
                }
                this.alpnSelector.checkUnsupported();
            }
        }
        return result;
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
    public SSLEngineResult wrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
        return verifyProtocolSelection(super.wrap(src, dst));
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
    public SSLEngineResult wrap(ByteBuffer[] srcs, ByteBuffer dst) throws SSLException {
        return verifyProtocolSelection(super.wrap(srcs, dst));
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
    public SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int len, ByteBuffer dst) throws SSLException {
        return verifyProtocolSelection(super.wrap(srcs, offset, len, dst));
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
    public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
        return verifyProtocolSelection(super.unwrap(src, dst));
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
    public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts) throws SSLException {
        return verifyProtocolSelection(super.unwrap(src, dsts));
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, javax.net.ssl.SSLEngine
    public SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dst, int offset, int len) throws SSLException {
        return verifyProtocolSelection(super.unwrap(src, dst, offset, len));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.ssl.JdkSslEngine
    public void setNegotiatedApplicationProtocol(String applicationProtocol) {
    }

    @Override // io.netty.handler.ssl.JdkSslEngine, io.netty.handler.ssl.ApplicationProtocolAccessor
    public String getNegotiatedApplicationProtocol() {
        String protocol = getApplicationProtocol();
        if (protocol == null || protocol.isEmpty()) {
            return null;
        }
        return protocol;
    }

    @Override // javax.net.ssl.SSLEngine
    public String getApplicationProtocol() {
        return JdkAlpnSslUtils.getApplicationProtocol(getWrappedEngine());
    }

    @Override // javax.net.ssl.SSLEngine
    public String getHandshakeApplicationProtocol() {
        return JdkAlpnSslUtils.getHandshakeApplicationProtocol(getWrappedEngine());
    }

    @Override // javax.net.ssl.SSLEngine
    public void setHandshakeApplicationProtocolSelector(BiFunction<SSLEngine, List<String>, String> selector) {
        JdkAlpnSslUtils.setHandshakeApplicationProtocolSelector(getWrappedEngine(), selector);
    }

    @Override // javax.net.ssl.SSLEngine
    public BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
        return JdkAlpnSslUtils.getHandshakeApplicationProtocolSelector(getWrappedEngine());
    }
}
