package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.SetScoreboardIdentityPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetScoreboardIdentitySerializer_v291 implements BedrockPacketSerializer<SetScoreboardIdentityPacket> {
    public static final SetScoreboardIdentitySerializer_v291 INSTANCE = new SetScoreboardIdentitySerializer_v291();

    protected SetScoreboardIdentitySerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, SetScoreboardIdentityPacket packet) {
        final SetScoreboardIdentityPacket.Action action = packet.getAction();
        buffer.writeByte(action.ordinal());
        helper.writeArray(buffer, packet.getEntries(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetScoreboardIdentitySerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                SetScoreboardIdentitySerializer_v291.lambda$serialize$0(ByteBuf.this, action, helper, (ByteBuf) obj, (SetScoreboardIdentityPacket.Entry) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(ByteBuf buffer, SetScoreboardIdentityPacket.Action action, BedrockCodecHelper helper, ByteBuf buf, SetScoreboardIdentityPacket.Entry entry) {
        VarInts.writeLong(buffer, entry.getScoreboardId());
        if (action == SetScoreboardIdentityPacket.Action.ADD) {
            helper.writeUuid(buffer, entry.getUuid());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(final ByteBuf buffer, final BedrockCodecHelper helper, SetScoreboardIdentityPacket packet) {
        final SetScoreboardIdentityPacket.Action action = SetScoreboardIdentityPacket.Action.values()[buffer.readUnsignedByte()];
        packet.setAction(action);
        helper.readArray(buffer, packet.getEntries(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetScoreboardIdentitySerializer_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SetScoreboardIdentitySerializer_v291.lambda$deserialize$1(ByteBuf.this, action, helper, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ SetScoreboardIdentityPacket.Entry lambda$deserialize$1(ByteBuf buffer, SetScoreboardIdentityPacket.Action action, BedrockCodecHelper helper, ByteBuf buf) {
        long scoreboardId = VarInts.readLong(buffer);
        UUID uuid = null;
        if (action == SetScoreboardIdentityPacket.Action.ADD) {
            uuid = helper.readUuid(buffer);
        }
        return new SetScoreboardIdentityPacket.Entry(scoreboardId, uuid);
    }
}
