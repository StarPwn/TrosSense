package org.cloudburstmc.protocol.bedrock.codec;

/* loaded from: classes5.dex */
public class PacketSerializeException extends RuntimeException {
    public PacketSerializeException(Throwable e) {
        super(e);
    }

    public PacketSerializeException(String message, Throwable e) {
        super(message, e);
    }
}
