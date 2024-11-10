package org.cloudburstmc.protocol.bedrock;

@FunctionalInterface
/* loaded from: classes5.dex */
public interface BedrockSessionFactory {
    BedrockSession createSession(BedrockPeer bedrockPeer, int i);
}
