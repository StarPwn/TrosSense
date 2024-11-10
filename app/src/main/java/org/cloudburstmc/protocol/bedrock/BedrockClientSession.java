package org.cloudburstmc.protocol.bedrock;

/* loaded from: classes5.dex */
public class BedrockClientSession extends BedrockSession {
    public BedrockClientSession(BedrockPeer peer, int subClientId) {
        super(peer, subClientId);
    }

    @Override // org.cloudburstmc.protocol.bedrock.BedrockSession
    public void disconnect(String reason, boolean hideReason) {
        close(reason);
    }
}
