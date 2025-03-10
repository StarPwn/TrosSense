package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.JdkApplicationProtocolNegotiator;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import org.conscrypt.AllocatedBuffer;
import org.conscrypt.BufferAllocator;
import org.conscrypt.HandshakeListener;

/* loaded from: classes4.dex */
abstract class ConscryptAlpnSslEngine extends JdkSslEngine {
    private static final boolean USE_BUFFER_ALLOCATOR = SystemPropertyUtil.getBoolean("io.netty.handler.ssl.conscrypt.useBufferAllocator", true);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConscryptAlpnSslEngine newClientEngine(SSLEngine engine, ByteBufAllocator alloc, JdkApplicationProtocolNegotiator applicationNegotiator) {
        return new ClientEngine(engine, alloc, applicationNegotiator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConscryptAlpnSslEngine newServerEngine(SSLEngine engine, ByteBufAllocator alloc, JdkApplicationProtocolNegotiator applicationNegotiator) {
        return new ServerEngine(engine, alloc, applicationNegotiator);
    }

    private ConscryptAlpnSslEngine(SSLEngine engine, ByteBufAllocator alloc, List<String> protocols) {
        super(engine);
        if (USE_BUFFER_ALLOCATOR) {
            org.conscrypt.Conscrypt.setBufferAllocator(engine, new BufferAllocatorAdapter(alloc));
        }
        org.conscrypt.Conscrypt.setApplicationProtocols(engine, (String[]) protocols.toArray(EmptyArrays.EMPTY_STRINGS));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int calculateOutNetBufSize(int plaintextBytes, int numBuffers) {
        return calculateSpace(plaintextBytes, numBuffers, 2147483647L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int calculateRequiredOutBufSpace(int plaintextBytes, int numBuffers) {
        return calculateSpace(plaintextBytes, numBuffers, org.conscrypt.Conscrypt.maxEncryptedPacketLength());
    }

    private int calculateSpace(int plaintextBytes, int numBuffers, long maxPacketLength) {
        long maxOverhead = org.conscrypt.Conscrypt.maxSealOverhead(getWrappedEngine()) * numBuffers;
        return (int) Math.min(maxPacketLength, plaintextBytes + maxOverhead);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SSLEngineResult unwrap(ByteBuffer[] srcs, ByteBuffer[] dests) throws SSLException {
        return org.conscrypt.Conscrypt.unwrap(getWrappedEngine(), srcs, dests);
    }

    /* loaded from: classes4.dex */
    private static final class ClientEngine extends ConscryptAlpnSslEngine {
        private final JdkApplicationProtocolNegotiator.ProtocolSelectionListener protocolListener;

        ClientEngine(SSLEngine engine, ByteBufAllocator alloc, JdkApplicationProtocolNegotiator applicationNegotiator) {
            super(engine, alloc, applicationNegotiator.protocols());
            org.conscrypt.Conscrypt.setHandshakeListener(engine, new HandshakeListener() { // from class: io.netty.handler.ssl.ConscryptAlpnSslEngine.ClientEngine.1
                public void onHandshakeFinished() throws SSLException {
                    ClientEngine.this.selectProtocol();
                }
            });
            this.protocolListener = (JdkApplicationProtocolNegotiator.ProtocolSelectionListener) ObjectUtil.checkNotNull(applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols()), "protocolListener");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void selectProtocol() throws SSLException {
            String protocol = org.conscrypt.Conscrypt.getApplicationProtocol(getWrappedEngine());
            try {
                this.protocolListener.selected(protocol);
            } catch (Throwable e) {
                throw SslUtils.toSSLHandshakeException(e);
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class ServerEngine extends ConscryptAlpnSslEngine {
        private final JdkApplicationProtocolNegotiator.ProtocolSelector protocolSelector;

        ServerEngine(SSLEngine engine, ByteBufAllocator alloc, JdkApplicationProtocolNegotiator applicationNegotiator) {
            super(engine, alloc, applicationNegotiator.protocols());
            org.conscrypt.Conscrypt.setHandshakeListener(engine, new HandshakeListener() { // from class: io.netty.handler.ssl.ConscryptAlpnSslEngine.ServerEngine.1
                public void onHandshakeFinished() throws SSLException {
                    ServerEngine.this.selectProtocol();
                }
            });
            this.protocolSelector = (JdkApplicationProtocolNegotiator.ProtocolSelector) ObjectUtil.checkNotNull(applicationNegotiator.protocolSelectorFactory().newSelector(this, new LinkedHashSet(applicationNegotiator.protocols())), "protocolSelector");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void selectProtocol() throws SSLException {
            try {
                String protocol = org.conscrypt.Conscrypt.getApplicationProtocol(getWrappedEngine());
                this.protocolSelector.select(protocol != null ? Collections.singletonList(protocol) : Collections.emptyList());
            } catch (Throwable e) {
                throw SslUtils.toSSLHandshakeException(e);
            }
        }
    }

    /* loaded from: classes4.dex */
    private static final class BufferAllocatorAdapter extends BufferAllocator {
        private final ByteBufAllocator alloc;

        BufferAllocatorAdapter(ByteBufAllocator alloc) {
            this.alloc = alloc;
        }

        public AllocatedBuffer allocateDirectBuffer(int capacity) {
            return new BufferAdapter(this.alloc.directBuffer(capacity));
        }
    }

    /* loaded from: classes4.dex */
    private static final class BufferAdapter extends AllocatedBuffer {
        private final ByteBuffer buffer;
        private final ByteBuf nettyBuffer;

        BufferAdapter(ByteBuf nettyBuffer) {
            this.nettyBuffer = nettyBuffer;
            this.buffer = nettyBuffer.nioBuffer(0, nettyBuffer.capacity());
        }

        public ByteBuffer nioBuffer() {
            return this.buffer;
        }

        public AllocatedBuffer retain() {
            this.nettyBuffer.retain();
            return this;
        }

        public AllocatedBuffer release() {
            this.nettyBuffer.release();
            return this;
        }
    }
}
