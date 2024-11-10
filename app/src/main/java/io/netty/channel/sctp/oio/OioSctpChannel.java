package io.netty.channel.sctp.oio;

import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.NotificationHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.oio.AbstractOioMessageChannel;
import io.netty.channel.sctp.DefaultSctpChannelConfig;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelConfig;
import io.netty.channel.sctp.SctpMessage;
import io.netty.channel.sctp.SctpNotificationHandler;
import io.netty.channel.sctp.SctpServerChannel;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Deprecated
/* loaded from: classes4.dex */
public class OioSctpChannel extends AbstractOioMessageChannel implements SctpChannel {
    private final com.sun.nio.sctp.SctpChannel ch;
    private final SctpChannelConfig config;
    private final Selector connectSelector;
    private final NotificationHandler<?> notificationHandler;
    private final Selector readSelector;
    private final Selector writeSelector;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OioSctpChannel.class);
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private static final String EXPECTED_TYPE = " (expected: " + StringUtil.simpleClassName((Class<?>) SctpMessage.class) + ')';

    private static com.sun.nio.sctp.SctpChannel openChannel() {
        try {
            return com.sun.nio.sctp.SctpChannel.open();
        } catch (IOException e) {
            throw new ChannelException("Failed to open a sctp channel.", e);
        }
    }

    public OioSctpChannel() {
        this(openChannel());
    }

    public OioSctpChannel(com.sun.nio.sctp.SctpChannel ch) {
        this(null, ch);
    }

    public OioSctpChannel(Channel parent, com.sun.nio.sctp.SctpChannel ch) {
        super(parent);
        this.ch = ch;
        try {
            try {
                ch.configureBlocking(false);
                this.readSelector = Selector.open();
                this.writeSelector = Selector.open();
                this.connectSelector = Selector.open();
                ch.register(this.readSelector, 1);
                ch.register(this.writeSelector, 4);
                ch.register(this.connectSelector, 8);
                this.config = new OioSctpChannelConfig(this, ch);
                this.notificationHandler = new SctpNotificationHandler(this);
                if (1 == 0) {
                    try {
                        ch.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close a sctp channel.", (Throwable) e);
                    }
                }
            } catch (Exception e2) {
                throw new ChannelException("failed to initialize a sctp channel", e2);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                try {
                    ch.close();
                } catch (IOException e3) {
                    logger.warn("Failed to close a sctp channel.", (Throwable) e3);
                }
            }
            throw th;
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

    @Override // io.netty.channel.AbstractChannel, io.netty.channel.Channel
    public SctpServerChannel parent() {
        return (SctpServerChannel) super.parent();
    }

    @Override // io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.Channel
    public SctpChannelConfig config() {
        return this.config;
    }

    @Override // io.netty.channel.Channel
    public boolean isOpen() {
        return this.ch.isOpen();
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0078, code lost:            if (0 != 0) goto L19;     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007a, code lost:            r4.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0085, code lost:            return r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0082, code lost:            if (1 == 0) goto L24;     */
    @Override // io.netty.channel.oio.AbstractOioMessageChannel
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected int doReadMessages(java.util.List<java.lang.Object> r12) throws java.lang.Exception {
        /*
            r11 = this;
            java.nio.channels.Selector r0 = r11.readSelector
            boolean r0 = r0.isOpen()
            r1 = 0
            if (r0 != 0) goto La
            return r1
        La:
            r0 = 0
            java.nio.channels.Selector r2 = r11.readSelector
            r3 = 1000(0x3e8, double:4.94E-321)
            int r2 = r2.select(r3)
            if (r2 <= 0) goto L16
            r1 = 1
        L16:
            if (r1 != 0) goto L19
            return r0
        L19:
            java.nio.channels.Selector r3 = r11.readSelector
            java.util.Set r3 = r3.selectedKeys()
            r3.clear()
            io.netty.channel.Channel$Unsafe r3 = r11.unsafe()
            io.netty.channel.RecvByteBufAllocator$Handle r3 = r3.recvBufAllocHandle()
            io.netty.channel.sctp.SctpChannelConfig r4 = r11.config()
            io.netty.buffer.ByteBufAllocator r4 = r4.getAllocator()
            io.netty.buffer.ByteBuf r4 = r3.allocate(r4)
            r5 = 1
            int r6 = r4.writerIndex()     // Catch: java.lang.Throwable -> L7e
            int r7 = r4.writableBytes()     // Catch: java.lang.Throwable -> L7e
            java.nio.ByteBuffer r6 = r4.nioBuffer(r6, r7)     // Catch: java.lang.Throwable -> L7e
            com.sun.nio.sctp.SctpChannel r7 = r11.ch     // Catch: java.lang.Throwable -> L7e
            com.sun.nio.sctp.NotificationHandler<?> r8 = r11.notificationHandler     // Catch: java.lang.Throwable -> L7e
            r9 = 0
            com.sun.nio.sctp.MessageInfo r7 = r7.receive(r6, r9, r8)     // Catch: java.lang.Throwable -> L7e
            if (r7 != 0) goto L55
        L4f:
            if (r5 == 0) goto L54
            r4.release()
        L54:
            return r0
        L55:
            r6.flip()     // Catch: java.lang.Throwable -> L7e
            int r8 = r6.remaining()     // Catch: java.lang.Throwable -> L7e
            r3.lastBytesRead(r8)     // Catch: java.lang.Throwable -> L7e
            io.netty.channel.sctp.SctpMessage r8 = new io.netty.channel.sctp.SctpMessage     // Catch: java.lang.Throwable -> L7e
            int r9 = r4.writerIndex()     // Catch: java.lang.Throwable -> L7e
            int r10 = r3.lastBytesRead()     // Catch: java.lang.Throwable -> L7e
            int r9 = r9 + r10
            io.netty.buffer.ByteBuf r9 = r4.writerIndex(r9)     // Catch: java.lang.Throwable -> L7e
            r8.<init>(r7, r9)     // Catch: java.lang.Throwable -> L7e
            r12.add(r8)     // Catch: java.lang.Throwable -> L7e
            r5 = 0
            int r0 = r0 + 1
            if (r5 == 0) goto L85
        L7a:
            r4.release()
            goto L85
        L7e:
            r6 = move-exception
            io.netty.util.internal.PlatformDependent.throwException(r6)     // Catch: java.lang.Throwable -> L86
            if (r5 == 0) goto L85
            goto L7a
        L85:
            return r0
        L86:
            r6 = move-exception
            if (r5 == 0) goto L8c
            r4.release()
        L8c:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.channel.sctp.oio.OioSctpChannel.doReadMessages(java.util.List):int");
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        ByteBuffer nioData;
        if (!this.writeSelector.isOpen()) {
            return;
        }
        int size = in.size();
        int selectedKeys = this.writeSelector.select(1000L);
        if (selectedKeys > 0) {
            Set<SelectionKey> writableKeys = this.writeSelector.selectedKeys();
            if (writableKeys.isEmpty()) {
                return;
            }
            Iterator<SelectionKey> writableKeysIt = writableKeys.iterator();
            int written = 0;
            while (written != size) {
                writableKeysIt.next();
                writableKeysIt.remove();
                SctpMessage packet = (SctpMessage) in.current();
                if (packet == null) {
                    return;
                }
                ByteBuf data = packet.content();
                int dataLen = data.readableBytes();
                if (data.nioBufferCount() != -1) {
                    nioData = data.nioBuffer();
                } else {
                    nioData = ByteBuffer.allocate(dataLen);
                    data.getBytes(data.readerIndex(), nioData);
                    nioData.flip();
                }
                MessageInfo mi = MessageInfo.createOutgoing(association(), (SocketAddress) null, packet.streamIdentifier());
                mi.payloadProtocolID(packet.protocolIdentifier());
                mi.streamNumber(packet.streamIdentifier());
                mi.unordered(packet.isUnordered());
                this.ch.send(nioData, mi);
                written++;
                in.remove();
                if (!writableKeysIt.hasNext()) {
                    return;
                }
            }
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected Object filterOutboundMessage(Object msg) throws Exception {
        if (msg instanceof SctpMessage) {
            return msg;
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EXPECTED_TYPE);
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Association association() {
        try {
            return this.ch.association();
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.Channel
    public boolean isActive() {
        return isOpen() && association() != null;
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress localAddress0() {
        try {
            Iterator<SocketAddress> i = this.ch.getAllLocalAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Set<InetSocketAddress> allLocalAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = this.ch.getAllLocalAddresses();
            Set<InetSocketAddress> addresses = new LinkedHashSet<>(allLocalAddresses.size());
            for (SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress) socketAddress);
            }
            return addresses;
        } catch (Throwable th) {
            return Collections.emptySet();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected SocketAddress remoteAddress0() {
        try {
            Iterator<SocketAddress> i = this.ch.getRemoteAddresses().iterator();
            if (i.hasNext()) {
                return i.next();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public Set<InetSocketAddress> allRemoteAddresses() {
        try {
            Set<SocketAddress> allLocalAddresses = this.ch.getRemoteAddresses();
            Set<InetSocketAddress> addresses = new LinkedHashSet<>(allLocalAddresses.size());
            for (SocketAddress socketAddress : allLocalAddresses) {
                addresses.add((InetSocketAddress) socketAddress);
            }
            return addresses;
        } catch (Throwable th) {
            return Collections.emptySet();
        }
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doBind(SocketAddress localAddress) throws Exception {
        this.ch.bind(localAddress);
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.ch.bind(localAddress);
        }
        boolean success = false;
        try {
            this.ch.connect(remoteAddress);
            boolean finishConnect = false;
            while (!finishConnect) {
                if (this.connectSelector.select(1000L) >= 0) {
                    Set<SelectionKey> selectionKeys = this.connectSelector.selectedKeys();
                    Iterator<SelectionKey> it2 = selectionKeys.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        SelectionKey key = it2.next();
                        if (key.isConnectable()) {
                            selectionKeys.clear();
                            finishConnect = true;
                            break;
                        }
                    }
                    selectionKeys.clear();
                }
            }
            success = this.ch.finishConnect();
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

    @Override // io.netty.channel.AbstractChannel
    protected void doClose() throws Exception {
        closeSelector("read", this.readSelector);
        closeSelector("write", this.writeSelector);
        closeSelector("connect", this.connectSelector);
        this.ch.close();
    }

    private static void closeSelector(String selectorName, Selector selector) {
        try {
            selector.close();
        } catch (IOException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to close a " + selectorName + " selector.", (Throwable) e);
            }
        }
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture bindAddress(InetAddress localAddress) {
        return bindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture bindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                this.ch.bindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.oio.OioSctpChannel.1
                @Override // java.lang.Runnable
                public void run() {
                    OioSctpChannel.this.bindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture unbindAddress(InetAddress localAddress) {
        return unbindAddress(localAddress, newPromise());
    }

    @Override // io.netty.channel.sctp.SctpChannel
    public ChannelFuture unbindAddress(final InetAddress localAddress, final ChannelPromise promise) {
        if (eventLoop().inEventLoop()) {
            try {
                this.ch.unbindAddress(localAddress);
                promise.setSuccess();
            } catch (Throwable t) {
                promise.setFailure(t);
            }
        } else {
            eventLoop().execute(new Runnable() { // from class: io.netty.channel.sctp.oio.OioSctpChannel.2
                @Override // java.lang.Runnable
                public void run() {
                    OioSctpChannel.this.unbindAddress(localAddress, promise);
                }
            });
        }
        return promise;
    }

    /* loaded from: classes4.dex */
    private final class OioSctpChannelConfig extends DefaultSctpChannelConfig {
        private OioSctpChannelConfig(OioSctpChannel channel, com.sun.nio.sctp.SctpChannel javaChannel) {
            super(channel, javaChannel);
        }

        @Override // io.netty.channel.DefaultChannelConfig
        protected void autoReadCleared() {
            OioSctpChannel.this.clearReadPending();
        }
    }
}
