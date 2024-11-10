package io.netty.channel.udt.nio;

import com.barchart.udt.StatusUDT;
import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.FileRegion;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

@Deprecated
/* loaded from: classes4.dex */
public class NioUdtByteConnectorChannel extends AbstractNioByteChannel implements UdtChannel {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) NioUdtByteConnectorChannel.class);
    private final UdtChannelConfig config;

    public NioUdtByteConnectorChannel() {
        this(TypeUDT.STREAM);
    }

    public NioUdtByteConnectorChannel(Channel parent, SocketChannelUDT channelUDT) {
        super(parent, channelUDT);
        try {
            channelUDT.configureBlocking(false);
            switch (AnonymousClass2.$SwitchMap$com$barchart$udt$StatusUDT[channelUDT.socketUDT().status().ordinal()]) {
                case 1:
                case 2:
                    this.config = new DefaultUdtChannelConfig(this, channelUDT, true);
                    return;
                default:
                    this.config = new DefaultUdtChannelConfig(this, channelUDT, false);
                    return;
            }
        } catch (Exception e) {
            try {
                channelUDT.close();
            } catch (Exception e2) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Failed to close channel.", (Throwable) e2);
                }
            }
            throw new ChannelException("Failed to configure channel.", e);
        }
    }

    /* renamed from: io.netty.channel.udt.nio.NioUdtByteConnectorChannel$2, reason: invalid class name */
    /* loaded from: classes4.dex */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$barchart$udt$StatusUDT = new int[StatusUDT.values().length];

        static {
            try {
                $SwitchMap$com$barchart$udt$StatusUDT[StatusUDT.INIT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$barchart$udt$StatusUDT[StatusUDT.OPENED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public NioUdtByteConnectorChannel(SocketChannelUDT channelUDT) {
        this(null, channelUDT);
    }

    public NioUdtByteConnectorChannel(TypeUDT type) {
        this(NioUdtProvider.newConnectorChannelUDT(type));
    }

    @Override // io.netty.channel.Channel
    public UdtChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        privilegedBind(mo214javaChannel(), localAddress);
    }

    @Override // io.netty.channel.nio.AbstractNioChannel, io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        mo214javaChannel().close();
    }

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected boolean doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        doBind(localAddress != null ? localAddress : new InetSocketAddress(0));
        boolean success = false;
        try {
            boolean connected = SocketUtils.connect(mo214javaChannel(), remoteAddress);
            if (!connected) {
                selectionKey().interestOps(selectionKey().interestOps() | 8);
            }
            success = true;
            return connected;
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

    @Override // io.netty.channel.nio.AbstractNioChannel
    protected void doFinishConnect() throws Exception {
        if (mo214javaChannel().finishConnect()) {
            selectionKey().interestOps(selectionKey().interestOps() & (-9));
            return;
        }
        throw new Error("Provider error: failed to finish connect. Provider library should be upgraded.");
    }

    @Override // io.netty.channel.nio.AbstractNioByteChannel
    protected int doReadBytes(ByteBuf byteBuf) throws Exception {
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        allocHandle.attemptedBytesRead(byteBuf.writableBytes());
        return byteBuf.writeBytes((ScatteringByteChannel) mo214javaChannel(), allocHandle.attemptedBytesRead());
    }

    @Override // io.netty.channel.nio.AbstractNioByteChannel
    protected int doWriteBytes(ByteBuf byteBuf) throws Exception {
        int expectedWrittenBytes = byteBuf.readableBytes();
        return byteBuf.readBytes((GatheringByteChannel) mo214javaChannel(), expectedWrittenBytes);
    }

    @Override // io.netty.channel.nio.AbstractNioByteChannel, io.netty.channel.socket.DuplexChannel
    protected ChannelFuture shutdownInput() {
        return newFailedFuture(new UnsupportedOperationException("shutdownInput"));
    }

    @Override // io.netty.channel.nio.AbstractNioByteChannel
    protected long doWriteFileRegion(FileRegion region) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        SocketChannelUDT channelUDT = mo214javaChannel();
        return channelUDT.isOpen() && channelUDT.isConnectFinished();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.nio.AbstractNioChannel
    /* renamed from: javaChannel, reason: merged with bridge method [inline-methods] */
    public SocketChannelUDT mo214javaChannel() {
        return super.mo214javaChannel();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        return mo214javaChannel().socket().getLocalSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        return mo214javaChannel().socket().getRemoteSocketAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress localAddress() {
        return (InetSocketAddress) super.localAddress();
    }

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress) super.remoteAddress();
    }

    private static void privilegedBind(final SocketChannelUDT socketChannel, final SocketAddress localAddress) throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: io.netty.channel.udt.nio.NioUdtByteConnectorChannel.1
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws IOException {
                    socketChannel.bind(localAddress);
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getCause());
        }
    }
}
