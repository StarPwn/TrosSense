package org.cloudburstmc.protocol.bedrock.netty.initializer;

import org.cloudburstmc.protocol.bedrock.BedrockClientSession;
import org.cloudburstmc.protocol.bedrock.BedrockPeer;

/* loaded from: classes5.dex */
public abstract class BedrockClientInitializer extends BedrockChannelInitializer<BedrockClientSession> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockChannelInitializer
    public BedrockClientSession createSession0(BedrockPeer peer, int subClientId) {
        return new BedrockClientSession(peer, subClientId);
    }
}
