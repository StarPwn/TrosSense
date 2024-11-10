package org.cloudburstmc.protocol.bedrock.codec.v340.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.PetDiedEventData;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v340 extends EventSerializer_v332 {
    public static final EventSerializer_v340 INSTANCE = new EventSerializer_v340();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332
    public PetDiedEventData readPetDied(ByteBuf buffer, BedrockCodecHelper helper) {
        boolean killedByOwner = buffer.readBoolean();
        long killerUniqueEntityId = VarInts.readLong(buffer);
        long petUniqueEntityId = VarInts.readLong(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        int petEntityType = VarInts.readInt(buffer);
        return new PetDiedEventData(killedByOwner, killerUniqueEntityId, petUniqueEntityId, entityDamageCause, petEntityType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332
    public void writePetDied(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        super.writePetDied(buffer, helper, eventData);
        VarInts.writeInt(buffer, ((PetDiedEventData) eventData).getPetEntityType());
    }
}
