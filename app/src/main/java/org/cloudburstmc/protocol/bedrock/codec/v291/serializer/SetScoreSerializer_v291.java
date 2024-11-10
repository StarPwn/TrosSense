package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.ScoreInfo;
import org.cloudburstmc.protocol.bedrock.packet.SetScorePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class SetScoreSerializer_v291 implements BedrockPacketSerializer<SetScorePacket> {
    public static final SetScoreSerializer_v291 INSTANCE = new SetScoreSerializer_v291();

    protected SetScoreSerializer_v291() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, final BedrockCodecHelper helper, SetScorePacket packet) {
        final SetScorePacket.Action action = packet.getAction();
        buffer.writeByte(action.ordinal());
        helper.writeArray(buffer, packet.getInfos(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetScoreSerializer_v291$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                SetScoreSerializer_v291.lambda$serialize$0(BedrockCodecHelper.this, action, (ByteBuf) obj, (ScoreInfo) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(BedrockCodecHelper helper, SetScorePacket.Action action, ByteBuf buf, ScoreInfo scoreInfo) {
        VarInts.writeLong(buf, scoreInfo.getScoreboardId());
        helper.writeString(buf, scoreInfo.getObjectiveId());
        buf.writeIntLE(scoreInfo.getScore());
        if (action == SetScorePacket.Action.SET) {
            buf.writeByte(scoreInfo.getType().ordinal());
            switch (scoreInfo.getType()) {
                case ENTITY:
                case PLAYER:
                    VarInts.writeLong(buf, scoreInfo.getEntityId());
                    return;
                case FAKE:
                    helper.writeString(buf, scoreInfo.getName());
                    return;
                default:
                    throw new IllegalArgumentException("Invalid score info received");
            }
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, final BedrockCodecHelper helper, SetScorePacket packet) {
        final SetScorePacket.Action action = SetScorePacket.Action.values()[buffer.readUnsignedByte()];
        packet.setAction(action);
        helper.readArray(buffer, packet.getInfos(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.SetScoreSerializer_v291$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SetScoreSerializer_v291.lambda$deserialize$1(BedrockCodecHelper.this, action, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ScoreInfo lambda$deserialize$1(BedrockCodecHelper helper, SetScorePacket.Action action, ByteBuf buf) {
        long scoreboardId = VarInts.readLong(buf);
        String objectiveId = helper.readString(buf);
        int score = buf.readIntLE();
        if (action == SetScorePacket.Action.SET) {
            ScoreInfo.ScorerType type = ScoreInfo.ScorerType.values()[buf.readUnsignedByte()];
            switch (type) {
                case ENTITY:
                case PLAYER:
                    long entityId = VarInts.readLong(buf);
                    return new ScoreInfo(scoreboardId, objectiveId, score, type, entityId);
                case FAKE:
                    String name = helper.readString(buf);
                    return new ScoreInfo(scoreboardId, objectiveId, score, name);
                default:
                    throw new IllegalArgumentException("Invalid score info received");
            }
        }
        return new ScoreInfo(scoreboardId, objectiveId, score);
    }
}
