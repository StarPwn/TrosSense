package org.cloudburstmc.protocol.bedrock.codec.v568;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v557.BedrockCodecHelper_v557;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationData;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.bedrock.data.skin.PersonaPieceData;
import org.cloudburstmc.protocol.bedrock.data.skin.PersonaPieceTintData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v568 extends BedrockCodecHelper_v557 {
    public BedrockCodecHelper_v568(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes, TypeMap<Ability> abilities, TypeMap<TextProcessingEventOrigin> textProcessingEventOrigins) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes, abilities, textProcessingEventOrigins);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v465.BedrockCodecHelper_v465, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v390.BedrockCodecHelper_v390, org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public SerializedSkin readSkin(ByteBuf buffer) {
        String skinId = readString(buffer);
        String playFabId = readString(buffer);
        String skinResourcePatch = readString(buffer);
        ImageData skinData = readImage(buffer);
        int animationCount = buffer.readIntLE();
        List<AnimationData> animations = new ObjectArrayList<>();
        for (int i = 0; i < animationCount; i++) {
            animations.add(readAnimationData(buffer));
        }
        ImageData capeData = readImage(buffer);
        String geometryData = readString(buffer);
        String geometryDataEngineVersion = readString(buffer);
        String animationData = readString(buffer);
        String capeId = readString(buffer);
        String fullSkinId = readString(buffer);
        String armSize = readString(buffer);
        String skinColor = readString(buffer);
        List<PersonaPieceData> personaPieces = new ObjectArrayList<>();
        int piecesLength = buffer.readIntLE();
        for (int i2 = 0; i2 < piecesLength; i2++) {
            String pieceId = readString(buffer);
            String pieceType = readString(buffer);
            String packId = readString(buffer);
            boolean isDefault = buffer.readBoolean();
            String productId = readString(buffer);
            personaPieces.add(new PersonaPieceData(pieceId, pieceType, packId, isDefault, productId));
        }
        List<PersonaPieceTintData> tintColors = new ObjectArrayList<>();
        int tintsLength = buffer.readIntLE();
        for (int i3 = 0; i3 < tintsLength; i3++) {
            String pieceType2 = readString(buffer);
            List<String> colors = new ObjectArrayList<>();
            int colorsLength = buffer.readIntLE();
            for (int i22 = 0; i22 < colorsLength; i22++) {
                colors.add(readString(buffer));
            }
            tintColors.add(new PersonaPieceTintData(pieceType2, colors));
        }
        boolean premium = buffer.readBoolean();
        boolean persona = buffer.readBoolean();
        boolean capeOnClassic = buffer.readBoolean();
        boolean primaryUser = buffer.readBoolean();
        boolean overridingPlayerAppearance = buffer.readBoolean();
        return SerializedSkin.of(skinId, playFabId, skinResourcePatch, skinData, animations, capeData, geometryData, geometryDataEngineVersion, animationData, premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors, overridingPlayerAppearance);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v465.BedrockCodecHelper_v465, org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v390.BedrockCodecHelper_v390, org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeSkin(ByteBuf buffer, SerializedSkin skin) {
        super.writeSkin(buffer, skin);
        buffer.writeBoolean(skin.isOverridingPlayerAppearance());
    }
}
