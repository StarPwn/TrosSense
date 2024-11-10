package org.cloudburstmc.protocol.bedrock.codec.v534;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v503.BedrockCodecHelper_v503;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.AbilityLayer;
import org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v534 extends BedrockCodecHelper_v503 {
    private final TypeMap<Ability> abilities;
    private final Object2IntMap<Ability> abilityFlagsToBits;

    public BedrockCodecHelper_v534(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes, TypeMap<Ability> abilities) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
        this.abilities = abilities;
        final Object2IntMap<Ability> flags = new Object2IntOpenHashMap<>();
        abilities.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.BedrockCodecHelper_v534$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                Object2IntMap.this.put((Object2IntMap) ((Ability) obj2), 1 << ((Integer) obj).intValue());
            }
        });
        this.abilityFlagsToBits = Object2IntMaps.unmodifiable(flags);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readPlayerAbilities(ByteBuf buffer, PlayerAbilityHolder abilityHolder) {
        abilityHolder.setUniqueEntityId(buffer.readLongLE());
        abilityHolder.setPlayerPermission(PlayerPermission.values()[buffer.readUnsignedByte()]);
        abilityHolder.setCommandPermission(CommandPermission.values()[buffer.readUnsignedByte()]);
        readArray(buffer, abilityHolder.getAbilityLayers(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.BedrockCodecHelper_v534$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper_v534.this.readAbilityLayer((ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbilityLayer readAbilityLayer(ByteBuf buffer) {
        AbilityLayer abilityLayer = new AbilityLayer();
        abilityLayer.setLayerType(AbilityLayer.Type.values()[buffer.readUnsignedShortLE()]);
        readAbilitiesFromNumber(buffer.readIntLE(), abilityLayer.getAbilitiesSet());
        readAbilitiesFromNumber(buffer.readIntLE(), abilityLayer.getAbilityValues());
        abilityLayer.setFlySpeed(buffer.readFloatLE());
        abilityLayer.setWalkSpeed(buffer.readFloatLE());
        return abilityLayer;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writePlayerAbilities(ByteBuf buffer, PlayerAbilityHolder abilityHolder) {
        buffer.writeLongLE(abilityHolder.getUniqueEntityId());
        buffer.writeByte(abilityHolder.getPlayerPermission().ordinal());
        buffer.writeByte(abilityHolder.getCommandPermission().ordinal());
        writeArray(buffer, abilityHolder.getAbilityLayers(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.BedrockCodecHelper_v534$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v534.this.writeAbilityLayer((ByteBuf) obj, (AbilityLayer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeAbilityLayer(ByteBuf buffer, AbilityLayer abilityLayer) {
        buffer.writeShortLE(abilityLayer.getLayerType().ordinal());
        buffer.writeIntLE(getAbilitiesNumber(abilityLayer.getAbilitiesSet()));
        buffer.writeIntLE(getAbilitiesNumber(abilityLayer.getAbilityValues()));
        buffer.writeFloatLE(abilityLayer.getFlySpeed());
        buffer.writeFloatLE(abilityLayer.getWalkSpeed());
    }

    protected int getAbilitiesNumber(Set<Ability> abilities) {
        int number = 0;
        for (Ability ability : abilities) {
            number |= this.abilityFlagsToBits.getInt(ability);
        }
        return number;
    }

    protected void readAbilitiesFromNumber(final int number, final Set<Ability> abilities) {
        this.abilityFlagsToBits.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v534.BedrockCodecHelper_v534$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper_v534.lambda$readAbilitiesFromNumber$1(number, abilities, (Ability) obj, (Integer) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$readAbilitiesFromNumber$1(int number, Set abilities, Ability ability, Integer index) {
        if ((index.intValue() & number) != 0) {
            abilities.add(ability);
        }
    }
}
