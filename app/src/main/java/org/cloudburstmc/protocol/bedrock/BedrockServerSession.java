package org.cloudburstmc.protocol.bedrock;

import org.cloudburstmc.protocol.bedrock.packet.DisconnectPacket;

/* loaded from: classes5.dex */
public class BedrockServerSession extends BedrockSession {
    public BedrockServerSession(BedrockPeer peer, int subClientId) {
        super(peer, subClientId);
    }

    @Override // org.cloudburstmc.protocol.bedrock.BedrockSession
    public void disconnect(String reason, boolean hideReason) {
        checkForClosed();
        DisconnectPacket packet = new DisconnectPacket();
        if (reason == null || hideReason) {
            packet.setMessageSkipped(true);
            reason = BedrockDisconnectReasons.DISCONNECTED;
        }
        packet.setKickMessage(reason);
        sendPacketImmediately(packet);
    }
}
