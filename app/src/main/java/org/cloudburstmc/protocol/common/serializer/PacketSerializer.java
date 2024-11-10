package org.cloudburstmc.protocol.common.serializer;

import io.netty.buffer.ByteBuf;

/* loaded from: classes5.dex */
public interface PacketSerializer<P, H> {
    void deserialize(ByteBuf byteBuf, H h, P p);

    void serialize(ByteBuf byteBuf, H h, P p);
}
