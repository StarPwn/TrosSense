package io.netty.handler.ssl;

import io.netty.handler.ssl.JdkApplicationProtocolNegotiator;
import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLHandshakeException;

/* loaded from: classes4.dex */
class JdkBaseApplicationProtocolNegotiator implements JdkApplicationProtocolNegotiator {
    private final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory;
    private final List<String> protocols;
    private final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory;
    private final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory;
    static final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory FAIL_SELECTOR_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectorFactory() { // from class: io.netty.handler.ssl.JdkBaseApplicationProtocolNegotiator.1
        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelectorFactory
        public JdkApplicationProtocolNegotiator.ProtocolSelector newSelector(SSLEngine engine, Set<String> supportedProtocols) {
            return new FailProtocolSelector((JdkSslEngine) engine, supportedProtocols);
        }
    };
    static final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory NO_FAIL_SELECTOR_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectorFactory() { // from class: io.netty.handler.ssl.JdkBaseApplicationProtocolNegotiator.2
        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelectorFactory
        public JdkApplicationProtocolNegotiator.ProtocolSelector newSelector(SSLEngine engine, Set<String> supportedProtocols) {
            return new NoFailProtocolSelector((JdkSslEngine) engine, supportedProtocols);
        }
    };
    static final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory FAIL_SELECTION_LISTENER_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory() { // from class: io.netty.handler.ssl.JdkBaseApplicationProtocolNegotiator.3
        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory
        public JdkApplicationProtocolNegotiator.ProtocolSelectionListener newListener(SSLEngine engine, List<String> supportedProtocols) {
            return new FailProtocolSelectionListener((JdkSslEngine) engine, supportedProtocols);
        }
    };
    static final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory NO_FAIL_SELECTION_LISTENER_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory() { // from class: io.netty.handler.ssl.JdkBaseApplicationProtocolNegotiator.4
        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory
        public JdkApplicationProtocolNegotiator.ProtocolSelectionListener newListener(SSLEngine engine, List<String> supportedProtocols) {
            return new NoFailProtocolSelectionListener((JdkSslEngine) engine, supportedProtocols);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkBaseApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory, JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, Iterable<String> protocols) {
        this(wrapperFactory, selectorFactory, listenerFactory, ApplicationProtocolUtil.toList(protocols));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkBaseApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory, JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, String... protocols) {
        this(wrapperFactory, selectorFactory, listenerFactory, ApplicationProtocolUtil.toList(protocols));
    }

    private JdkBaseApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory, JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, List<String> protocols) {
        this.wrapperFactory = (JdkApplicationProtocolNegotiator.SslEngineWrapperFactory) ObjectUtil.checkNotNull(wrapperFactory, "wrapperFactory");
        this.selectorFactory = (JdkApplicationProtocolNegotiator.ProtocolSelectorFactory) ObjectUtil.checkNotNull(selectorFactory, "selectorFactory");
        this.listenerFactory = (JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory) ObjectUtil.checkNotNull(listenerFactory, "listenerFactory");
        this.protocols = Collections.unmodifiableList((List) ObjectUtil.checkNotNull(protocols, "protocols"));
    }

    @Override // io.netty.handler.ssl.ApplicationProtocolNegotiator
    public List<String> protocols() {
        return this.protocols;
    }

    @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator
    public JdkApplicationProtocolNegotiator.ProtocolSelectorFactory protocolSelectorFactory() {
        return this.selectorFactory;
    }

    @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator
    public JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory protocolListenerFactory() {
        return this.listenerFactory;
    }

    @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator
    public JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory() {
        return this.wrapperFactory;
    }

    /* loaded from: classes4.dex */
    static class NoFailProtocolSelector implements JdkApplicationProtocolNegotiator.ProtocolSelector {
        private final JdkSslEngine engineWrapper;
        private final Set<String> supportedProtocols;

        NoFailProtocolSelector(JdkSslEngine engineWrapper, Set<String> supportedProtocols) {
            this.engineWrapper = engineWrapper;
            this.supportedProtocols = supportedProtocols;
        }

        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelector
        public void unsupported() {
            this.engineWrapper.setNegotiatedApplicationProtocol(null);
        }

        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelector
        public String select(List<String> protocols) throws Exception {
            for (String p : this.supportedProtocols) {
                if (protocols.contains(p)) {
                    this.engineWrapper.setNegotiatedApplicationProtocol(p);
                    return p;
                }
            }
            return noSelectMatchFound();
        }

        public String noSelectMatchFound() throws Exception {
            this.engineWrapper.setNegotiatedApplicationProtocol(null);
            return null;
        }
    }

    /* loaded from: classes4.dex */
    private static final class FailProtocolSelector extends NoFailProtocolSelector {
        FailProtocolSelector(JdkSslEngine engineWrapper, Set<String> supportedProtocols) {
            super(engineWrapper, supportedProtocols);
        }

        @Override // io.netty.handler.ssl.JdkBaseApplicationProtocolNegotiator.NoFailProtocolSelector
        public String noSelectMatchFound() throws Exception {
            throw new SSLHandshakeException("Selected protocol is not supported");
        }
    }

    /* loaded from: classes4.dex */
    private static class NoFailProtocolSelectionListener implements JdkApplicationProtocolNegotiator.ProtocolSelectionListener {
        private final JdkSslEngine engineWrapper;
        private final List<String> supportedProtocols;

        NoFailProtocolSelectionListener(JdkSslEngine engineWrapper, List<String> supportedProtocols) {
            this.engineWrapper = engineWrapper;
            this.supportedProtocols = supportedProtocols;
        }

        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelectionListener
        public void unsupported() {
            this.engineWrapper.setNegotiatedApplicationProtocol(null);
        }

        @Override // io.netty.handler.ssl.JdkApplicationProtocolNegotiator.ProtocolSelectionListener
        public void selected(String protocol) throws Exception {
            if (this.supportedProtocols.contains(protocol)) {
                this.engineWrapper.setNegotiatedApplicationProtocol(protocol);
            } else {
                noSelectedMatchFound(protocol);
            }
        }

        protected void noSelectedMatchFound(String protocol) throws Exception {
        }
    }

    /* loaded from: classes4.dex */
    private static final class FailProtocolSelectionListener extends NoFailProtocolSelectionListener {
        FailProtocolSelectionListener(JdkSslEngine engineWrapper, List<String> supportedProtocols) {
            super(engineWrapper, supportedProtocols);
        }

        @Override // io.netty.handler.ssl.JdkBaseApplicationProtocolNegotiator.NoFailProtocolSelectionListener
        protected void noSelectedMatchFound(String protocol) throws Exception {
            throw new SSLHandshakeException("No compatible protocols found");
        }
    }
}
