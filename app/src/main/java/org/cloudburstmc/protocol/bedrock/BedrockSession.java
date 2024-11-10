package org.cloudburstmc.protocol.bedrock;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.SecretKey;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.netty.BedrockPacketWrapper;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public abstract class BedrockSession {
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) BedrockSession.class);
    private final AtomicBoolean closed = new AtomicBoolean();
    protected String disconnectReason = BedrockDisconnectReasons.UNKNOWN;
    protected boolean logging;
    protected BedrockPacketHandler packetHandler;
    protected final BedrockPeer peer;
    protected final int subClientId;

    public abstract void disconnect(String str, boolean z);

    public BedrockSession(BedrockPeer peer, int subClientId) {
        this.peer = peer;
        this.subClientId = subClientId;
    }

    public BedrockPacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public void setPacketHandler(BedrockPacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkForClosed() {
        if (this.closed.get()) {
            throw new IllegalStateException("Session has been closed");
        }
    }

    public void sendPacket(BedrockPacket packet) {
        this.peer.sendPacket(this.subClientId, 0, packet);
        logOutbound(packet);
    }

    public void sendPacketImmediately(BedrockPacket packet) {
        this.peer.sendPacketImmediately(this.subClientId, 0, packet);
        logOutbound(packet);
    }

    public BedrockPeer getPeer() {
        return this.peer;
    }

    public BedrockCodec getCodec() {
        return this.peer.getCodec();
    }

    public void setCodec(BedrockCodec codec) {
        ObjectUtil.checkNotNull(codec, "codec");
        if (this.subClientId != 0) {
            throw new IllegalStateException("The packet codec can only be set by the primary session");
        }
        this.peer.setCodec(codec);
    }

    public void setCompression(PacketCompressionAlgorithm algorithm) {
        if (isSubClient()) {
            throw new IllegalStateException("The compression algorithm can only be set by the primary session");
        }
        this.peer.setCompression(algorithm);
    }

    public void enableEncryption(SecretKey key) {
        if (isSubClient()) {
            throw new IllegalStateException("Encryption can only be enabled by the primary session");
        }
        this.peer.enableEncryption(key);
    }

    public void close(String reason) {
        checkForClosed();
        if (!isSubClient()) {
            this.peer.close(reason);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onClose() {
        if (!this.closed.compareAndSet(false, true)) {
            return;
        }
        if (this.packetHandler != null) {
            try {
                this.packetHandler.onDisconnect(this.disconnectReason);
            } catch (Exception e) {
                log.error("Exception thrown while handling disconnect", (Throwable) e);
            }
        }
        this.peer.removeSession(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPacket(BedrockPacketWrapper wrapper) {
        BedrockPacket packet = wrapper.getPacket();
        logInbound(packet);
        if (this.packetHandler == null) {
            log.warn("Received packet without a packet handler for {}:{}: {}", getSocketAddress(), Integer.valueOf(this.subClientId), packet);
        } else if (this.packetHandler.handlePacket(packet) == PacketSignal.UNHANDLED) {
            log.warn("Unhandled packet for {}:{}: {}", getSocketAddress(), Integer.valueOf(this.subClientId), packet);
        }
    }

    protected void logOutbound(BedrockPacket packet) {
        if (log.isTraceEnabled() && this.logging) {
            log.trace("Outbound {}{}: {}", getSocketAddress(), Integer.valueOf(this.subClientId), packet);
        }
    }

    protected void logInbound(BedrockPacket packet) {
        if (log.isTraceEnabled() && this.logging) {
            log.trace("Inbound {}{}: {}", getSocketAddress(), Integer.valueOf(this.subClientId), packet);
        }
    }

    public SocketAddress getSocketAddress() {
        return this.peer.getSocketAddress();
    }

    public boolean isSubClient() {
        return this.subClientId != 0;
    }

    public boolean isLogging() {
        return this.logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public String getDisconnectReason() {
        return this.disconnectReason;
    }

    public void setDisconnectReason(String disconnectReason) {
        this.disconnectReason = disconnectReason;
    }

    public final void disconnect() {
        disconnect(BedrockDisconnectReasons.DISCONNECTED);
    }

    public final void disconnect(String reason) {
        disconnect(reason, false);
    }

    public boolean isConnected() {
        return !this.closed.get();
    }
}
