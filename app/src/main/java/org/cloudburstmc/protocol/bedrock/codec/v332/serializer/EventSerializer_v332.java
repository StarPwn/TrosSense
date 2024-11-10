package org.cloudburstmc.protocol.bedrock.codec.v332.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.EventSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.event.EventData;
import org.cloudburstmc.protocol.bedrock.data.event.EventDataType;
import org.cloudburstmc.protocol.bedrock.data.event.MobBornEventData;
import org.cloudburstmc.protocol.bedrock.data.event.PetDiedEventData;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class EventSerializer_v332 extends EventSerializer_v291 {
    public static final EventSerializer_v332 INSTANCE = new EventSerializer_v332();

    /* JADX INFO: Access modifiers changed from: protected */
    public EventSerializer_v332() {
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOB_BORN, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v332.this.readMobBorn((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.readers.put((EnumMap<EventDataType, BiFunction<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PET_DIED, (EventDataType) new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EventSerializer_v332.this.readPetDied((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.MOB_BORN, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v332.this.writeMobBorn((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
        this.writers.put((EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockCodecHelper, EventData>>) EventDataType.PET_DIED, (EventDataType) new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v332.serializer.EventSerializer_v332$$ExternalSyntheticLambda3
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                EventSerializer_v332.this.writePetDied((ByteBuf) obj, (BedrockCodecHelper) obj2, (EventData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public MobBornEventData readMobBorn(ByteBuf buffer, BedrockCodecHelper helper) {
        int entityType = VarInts.readInt(buffer);
        int variant = VarInts.readInt(buffer);
        int color = buffer.readUnsignedByte();
        return new MobBornEventData(entityType, variant, color);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeMobBorn(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        MobBornEventData event = (MobBornEventData) eventData;
        VarInts.writeInt(buffer, event.getEntityType());
        VarInts.writeInt(buffer, event.getVariant());
        buffer.writeByte(event.getColor());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PetDiedEventData readPetDied(ByteBuf buffer, BedrockCodecHelper helper) {
        boolean killedByOwner = buffer.readBoolean();
        long killerUniqueEntityId = VarInts.readLong(buffer);
        long petUniqueEntityId = VarInts.readLong(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        return new PetDiedEventData(killedByOwner, killerUniqueEntityId, petUniqueEntityId, entityDamageCause, -1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writePetDied(ByteBuf buffer, BedrockCodecHelper helper, EventData eventData) {
        PetDiedEventData event = (PetDiedEventData) eventData;
        buffer.writeBoolean(event.isOwnerKilled());
        VarInts.writeLong(buffer, event.getKillerUniqueEntityId());
        VarInts.writeLong(buffer, event.getPetUniqueEntityId());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
    }
}
