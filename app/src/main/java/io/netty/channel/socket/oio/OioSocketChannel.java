package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoop;
import io.netty.channel.oio.OioByteStreamChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

@Deprecated
/* loaded from: classes4.dex */
public class OioSocketChannel extends OioByteStreamChannel implements SocketChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OioSocketChannel.class);
    private final OioSocketChannelConfig config;
    private final Socket socket;

    public OioSocketChannel() {
        this(new Socket());
    }

    public OioSocketChannel(Socket socket) {
        this(null, socket);
    }

    public OioSocketChannel(Channel parent, Socket socket) {
        super(parent);
        this.socket = socket;
        this.config = new DefaultOioSocketChannelConfig(this, socket);
        try {
            try {
                if (socket.isConnected()) {
                    activate(socket.getInputStream(), socket.getOutputStream());
                }
                socket.setSoTimeout(1000);
                if (1 == 0) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close a socket.", (Throwable) e);
                    }
                }
            } catch (Exception e2) {
                throw new ChannelException("failed to initialize a socket", e2);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                try {
                    socket.close();
                } catch (IOException e3) {
                    logger.warn("Failed to close a socket.", (Throwable) e3);
                }
            }
            throw th;
        }
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public ServerSocketChannel parent() {
        return (ServerSocketChannel) super.parent();
    }

    @Override // io.netty.channel.Channel
    public OioSocketChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return !this.socket.isClosed();
    }

    @Override // io.netty.channel.oio.OioByteStreamChannel, io.netty.channel.Channel
    public boolean isActive() {
        return !this.socket.isClosed() && this.socket.isConnected();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isOutputShutdown() {
        return this.socket.isOutputShutdown() || !isActive();
    }

    @Override // io.netty.channel.oio.AbstractOioByteChannel
    public boolean isInputShutdown() {
        return this.socket.isInputShutdown() || !isActive();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public boolean isShutdown() {
        return (this.socket.isInputShutdown() && this.socket.isOutputShutdown()) || !isActive();
    }

    @Override // io.netty.channel.AbstractChannel
    protected final void doShutdownOutput() throws Exception {
        shutdownOutput0();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownOutput() {
        return shutdownOutput(newPromise());
    }

    @Override // io.netty.channel.oio.AbstractOioByteChannel
    public ChannelFuture shutdownInput() {
        return shutdownInput(newPromise());
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdown() {
        return shutdown(newPromise());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.oio.OioByteStreamChannel, io.netty.channel.oio.AbstractOioByteChannel
    public int doReadBytes(ByteBuf buf) throws Exception {
        if (this.socket.isClosed()) {
            return -1;
        }
        try {
            return super.doReadBytes(buf);
        } catch (SocketTimeoutException e) {
            return 0;
        }
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        EventLoop loop = eventLoop();
        if (loop.inEventLoop()) {
            shutdownOutput0(promise);
        } else {
            loop.execute(new Runnable() { // from class: io.netty.channel.socket.oio.OioSocketChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    OioSocketChannel.this.shutdownOutput0(promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownOutput0(ChannelPromise promise) {
        try {
            shutdownOutput0();
            promise.setSuccess();
        } catch (Throwable t) {
            promise.setFailure(t);
        }
    }

    private void shutdownOutput0() throws IOException {
        this.socket.shutdownOutput();
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdownInput(final ChannelPromise promise) {
        EventLoop loop = eventLoop();
        if (loop.inEventLoop()) {
            shutdownInput0(promise);
        } else {
            loop.execute(new Runnable() { // from class: io.netty.channel.socket.oio.OioSocketChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    OioSocketChannel.this.shutdownInput0(promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownInput0(ChannelPromise promise) {
        try {
            this.socket.shutdownInput();
            promise.setSuccess();
        } catch (Throwable t) {
            promise.setFailure(t);
        }
    }

    @Override // io.netty.channel.socket.DuplexChannel
    public ChannelFuture shutdown(final ChannelPromise promise) {
        ChannelFuture shutdownOutputFuture = shutdownOutput();
        if (shutdownOutputFuture.isDone()) {
            shutdownOutputDone(shutdownOutputFuture, promise);
        } else {
            shutdownOutputFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.socket.oio.OioSocketChannel.3
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownOutputFuture2) throws Exception {
                    OioSocketChannel.this.shutdownOutputDone(shutdownOutputFuture2, promise);
                }
            });
        }
        return promise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
        ChannelFuture shutdownInputFuture = shutdownInput();
        if (shutdownInputFuture.isDone()) {
            shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
        } else {
            shutdownInputFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.socket.oio.OioSocketChannel.4
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture shutdownInputFuture2) throws Exception {
                    OioSocketChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture2, promise);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
        Throwable shutdownOutputCause = shutdownOutputFuture.cause();
        Throwable shutdownInputCause = shutdownInputFuture.cause();
        if (shutdownOutputCause != null) {
            if (shutdownInputCause != null) {
                logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
            }
            promise.setFailure(shutdownOutputCause);
        } else if (shutdownInputCause != null) {
            promise.setFailure(shutdownInputCause);
        } else {
            promise.setSuccess();
        }
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return this.socket.getLocalSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return this.socket.getRemoteSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        SocketUtils.bind(this.socket, localAddress);
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            SocketUtils.bind(this.socket, localAddress);
        }
        int connectTimeoutMillis = config().getConnectTimeoutMillis();
        boolean success = false;
        try {
            try {
                SocketUtils.connect(this.socket, remoteAddress, connectTimeoutMillis);
                activate(this.socket.getInputStream(), this.socket.getOutputStream());
                success = true;
            } catch (SocketTimeoutException e) {
                ConnectTimeoutException cause = new ConnectTimeoutException("connection timed out after " + connectTimeoutMillis + " ms: " + remoteAddress);
                cause.setStackTrace(e.getStackTrace());
                throw cause;
            }
        } finally {
            if (!success) {
                doClose();
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doDisconnect() throws Exception {
        doClose();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.oio.OioByteStreamChannel, io.netty.channel.AbstractChannel
    public void doClose() throws Exception {
        this.socket.close();
    }

    protected boolean checkInputShutdown() {
        if (isInputShutdown()) {
            try {
                Thread.sleep(config().getSoTimeout());
                return true;
            } catch (Throwable th) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.oio.AbstractOioChannel
    @Deprecated
    public void setReadPending(boolean readPending) {
        super.setReadPending(readPending);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void clearReadPending0() {
        clearReadPending();
    }
}
