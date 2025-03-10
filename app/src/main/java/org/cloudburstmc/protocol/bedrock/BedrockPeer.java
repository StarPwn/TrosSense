package org.cloudburstmc.protocol.bedrock;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.net.SocketAddress;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.SecretKey;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v428.Bedrock_v428;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.bedrock.netty.codec.FrameIdCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.batch.BedrockBatchDecoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionStrategy;
import org.cloudburstmc.protocol.bedrock.netty.codec.encryption.BedrockEncryptionDecoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.encryption.BedrockEncryptionEncoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec;
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockChannelInitializer;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils;
import org.jose4j.keys.AesKey;

/* loaded from: classes5.dex */
public class BedrockPeer extends ChannelInboundHandlerAdapter {
    public static final String NAME = "bedrock-peer";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) BedrockPeer.class);
    protected final Channel channel;
    protected final BedrockSessionFactory sessionFactory;
    protected ScheduledFuture<?> tickFuture;
    protected final Int2ObjectMap<BedrockSession> sessions = new Int2ObjectOpenHashMap();
    protected final Queue<BedrockPacketWrapper> packetQueue = PlatformDependent.newMpscQueue();
    protected AtomicBoolean closed = new AtomicBoolean();

    public BedrockPeer(Channel channel, BedrockSessionFactory sessionFactory) {
        this.channel = channel;
        this.sessionFactory = sessionFactory;
    }

    protected void onBedrockPacket(BedrockPacketWrapper wrapper) {
        int targetId = wrapper.getTargetSubClientId();
        BedrockSession session = this.sessions.computeIfAbsent(targetId, new Int2ObjectFunction() { // from class: org.cloudburstmc.protocol.bedrock.BedrockPeer$$ExternalSyntheticLambda0
            @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
            public final Object get(int i) {
                return BedrockPeer.this.onSessionCreated(i);
            }
        });
        session.onPacket(wrapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BedrockSession onSessionCreated(int sessionId) {
        return this.sessionFactory.createSession(this, sessionId);
    }

    protected void checkForClosed() {
        if (this.closed.get()) {
            throw new IllegalStateException("Peer has been closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeSession(BedrockSession session) {
        this.sessions.remove(session.subClientId, session);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onTick() {
        if (this.closed.get() || this.packetQueue.isEmpty()) {
            return;
        }
        while (true) {
            BedrockPacketWrapper packet = this.packetQueue.poll();
            if (packet != null) {
                this.channel.write(packet);
            } else {
                this.channel.flush();
                return;
            }
        }
    }

    private void onRakNetDisconnect(ChannelHandlerContext ctx, RakDisconnectReason reason) {
        String disconnectReason = BedrockDisconnectReasons.getReason(reason);
        ObjectIterator<BedrockSession> it2 = this.sessions.values().iterator();
        while (it2.hasNext()) {
            BedrockSession session = it2.next();
            session.disconnectReason = disconnectReason;
        }
    }

    private void free() {
        for (BedrockPacketWrapper wrapper : this.packetQueue) {
            ReferenceCountUtil.safeRelease(wrapper);
        }
    }

    public void sendPacket(int senderClientId, int targetClientId, BedrockPacket packet) {
        this.packetQueue.add(new BedrockPacketWrapper(0, senderClientId, targetClientId, packet, null));
    }

    public void sendPacketImmediately(int senderClientId, int targetClientId, BedrockPacket packet) {
        this.channel.writeAndFlush(new BedrockPacketWrapper(0, senderClientId, targetClientId, packet, null));
    }

    public void enableEncryption(SecretKey secretKey) {
        Objects.requireNonNull(secretKey, "secretKey");
        if (!secretKey.getAlgorithm().equals(AesKey.ALGORITHM)) {
            throw new IllegalArgumentException("Invalid key algorithm");
        }
        if (this.channel.pipeline().get(BedrockEncryptionEncoder.class) != null || this.channel.pipeline().get(BedrockEncryptionDecoder.class) != null) {
            throw new IllegalStateException("Encryption is already enabled");
        }
        int protocolVersion = getCodec().getProtocolVersion();
        boolean useCtr = protocolVersion >= Bedrock_v428.CODEC.getProtocolVersion();
        this.channel.pipeline().addAfter(FrameIdCodec.NAME, BedrockEncryptionEncoder.NAME, new BedrockEncryptionEncoder(secretKey, EncryptionUtils.createCipher(useCtr, true, secretKey)));
        this.channel.pipeline().addAfter(FrameIdCodec.NAME, BedrockEncryptionDecoder.NAME, new BedrockEncryptionDecoder(secretKey, EncryptionUtils.createCipher(useCtr, false, secretKey)));
        log.debug("Encryption enabled for {}", getSocketAddress());
    }

    public void setCompression(PacketCompressionAlgorithm algorithm) {
        Objects.requireNonNull(algorithm, "algorithm");
        setCompression(BedrockChannelInitializer.getCompression(algorithm, getRakVersion(), false));
    }

    public void setCompression(CompressionStrategy strategy) {
        Objects.requireNonNull(strategy, "strategy");
        boolean needsPrefix = getCodec().getProtocolVersion() >= 649;
        ChannelHandler handler = this.channel.pipeline().get(CompressionCodec.NAME);
        if (handler == null) {
            this.channel.pipeline().addBefore(BedrockBatchDecoder.NAME, CompressionCodec.NAME, new CompressionCodec(strategy, needsPrefix));
        } else {
            this.channel.pipeline().replace(CompressionCodec.NAME, CompressionCodec.NAME, new CompressionCodec(strategy, needsPrefix));
        }
    }

    public CompressionStrategy getCompressionStrategy() {
        ChannelHandler handler = this.channel.pipeline().get(CompressionCodec.NAME);
        if (!(handler instanceof CompressionCodec)) {
            return null;
        }
        return ((CompressionCodec) handler).getStrategy();
    }

    public BedrockCodec getCodec() {
        return ((BedrockPacketCodec) this.channel.pipeline().get(BedrockPacketCodec.class)).getCodec();
    }

    public BedrockCodecHelper getCodecHelper() {
        return ((BedrockPacketCodec) this.channel.pipeline().get(BedrockPacketCodec.class)).getHelper();
    }

    public void setCodec(BedrockCodec codec) {
        Objects.requireNonNull(codec, "codec");
        ((BedrockPacketCodec) this.channel.pipeline().get(BedrockPacketCodec.class)).setCodec(codec);
    }

    public void close(String reason) {
        ObjectIterator<BedrockSession> it2 = this.sessions.values().iterator();
        while (it2.hasNext()) {
            BedrockSession session = it2.next();
            session.disconnectReason = reason;
        }
        this.channel.disconnect();
    }

    protected void onClose() {
        if (this.channel.isOpen()) {
            log.warn("Tried to close peer, but channel is open!", new Throwable());
            return;
        }
        if (!this.closed.compareAndSet(false, true)) {
            return;
        }
        if (this.tickFuture != null) {
            this.tickFuture.cancel(false);
            this.tickFuture = null;
        }
        ObjectIterator<BedrockSession> it2 = this.sessions.values().iterator();
        while (it2.hasNext()) {
            BedrockSession session = it2.next();
            try {
                session.onClose();
            } catch (Exception e) {
                log.error("Exception whilst closing session", (Throwable) e);
            }
        }
        free();
    }

    public boolean isConnected() {
        return !this.closed.get() && this.channel.isOpen();
    }

    public boolean isConnecting() {
        return (this.channel.isActive() || this.closed.get()) ? false : true;
    }

    public SocketAddress getSocketAddress() {
        return this.channel.remoteAddress();
    }

    public Channel getChannel() {
        return this.channel;
    }

    public int getRakVersion() {
        return ((Integer) this.channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)).intValue();
    }

    @Override // io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerRemoved(ChannelHandlerContext ctx) {
        onClose();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.sessions.put(0, (int) this.sessionFactory.createSession(this, 0));
        this.tickFuture = this.channel.eventLoop().scheduleAtFixedRate(new Runnable() { // from class: org.cloudburstmc.protocol.bedrock.BedrockPeer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BedrockPeer.this.onTick();
            }
        }, 50L, 50L, TimeUnit.MILLISECONDS);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        onClose();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof BedrockPacketWrapper) {
                onBedrockPacket((BedrockPacketWrapper) msg);
                return;
            }
            throw new DecoderException("Unexpected message type: " + msg.getClass().getName());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof RakDisconnectReason) {
            onRakNetDisconnect(ctx, (RakDisconnectReason) evt);
        }
    }
}
