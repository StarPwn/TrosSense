package org.cloudburstmc.protocol.bedrock.codec.compat;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class NoopBedrockCodecHelper extends BaseBedrockCodecHelper {
    public static final NoopBedrockCodecHelper INSTANCE = new NoopBedrockCodecHelper();

    private NoopBedrockCodecHelper() {
        super(EntityDataTypeMap.builder().build(), TypeMap.empty("GameRule"));
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public EntityLinkData readEntityLink(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityLink(ByteBuf buffer, EntityLinkData link) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readNetItem(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeNetItem(ByteBuf buffer, ItemData item) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readItem(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItem(ByteBuf buffer, ItemData item) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readItemInstance(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemInstance(ByteBuf buffer, ItemData item) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public CommandOriginData readCommandOrigin(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeCommandOrigin(ByteBuf buffer, CommandOriginData commandOrigin) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public GameRuleData<?> readGameRule(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeGameRule(ByteBuf buffer, GameRuleData<?> gameRule) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readEntityData(ByteBuf buffer, EntityDataMap entityData) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeEntityData(ByteBuf buffer, EntityDataMap entityData) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public CommandEnumData readCommandEnum(ByteBuf buffer, boolean soft) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeCommandEnum(ByteBuf buffer, CommandEnumData commandEnum) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public SerializedSkin readSkin(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeSkin(ByteBuf buffer, SerializedSkin skin) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <O> O readOptional(ByteBuf buffer, O emptyValue, Function<ByteBuf, O> function) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeOptional(ByteBuf buffer, Predicate<T> isPresent, T object, BiConsumer<ByteBuf, T> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public <T> void writeOptionalNull(ByteBuf buffer, T object, BiConsumer<ByteBuf, T> consumer) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writePlayerAbilities(ByteBuf buffer, PlayerAbilityHolder abilityHolder) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void readPlayerAbilities(ByteBuf buffer, PlayerAbilityHolder abilityHolder) {
        throw new UnsupportedOperationException();
    }
}
