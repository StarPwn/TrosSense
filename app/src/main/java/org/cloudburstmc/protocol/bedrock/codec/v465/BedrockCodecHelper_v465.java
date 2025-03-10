package org.cloudburstmc.protocol.bedrock.codec.v465;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v448.BedrockCodecHelper_v448;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationData;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.bedrock.data.skin.PersonaPieceData;
import org.cloudburstmc.protocol.bedrock.data.skin.PersonaPieceTintData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v465 extends BedrockCodecHelper_v448 {
    public BedrockCodecHelper_v465(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v390.BedrockCodecHelper_v390, org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
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
        return SerializedSkin.of(skinId, playFabId, skinResourcePatch, skinData, animations, capeData, geometryData, geometryDataEngineVersion, animationData, premium, persona, capeOnClassic, primaryUser, capeId, fullSkinId, armSize, skinColor, personaPieces, tintColors);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v390.BedrockCodecHelper_v390, org.cloudburstmc.protocol.bedrock.codec.v388.BedrockCodecHelper_v388, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeSkin(ByteBuf buffer, SerializedSkin skin) {
        Objects.requireNonNull(skin, "Skin is null");
        writeString(buffer, skin.getSkinId());
        writeString(buffer, skin.getPlayFabId());
        writeString(buffer, skin.getSkinResourcePatch());
        writeImage(buffer, skin.getSkinData());
        List<AnimationData> animations = skin.getAnimations();
        buffer.writeIntLE(animations.size());
        for (AnimationData animation : animations) {
            writeAnimationData(buffer, animation);
        }
        writeImage(buffer, skin.getCapeData());
        writeString(buffer, skin.getGeometryData());
        writeString(buffer, skin.getGeometryDataEngineVersion());
        writeString(buffer, skin.getAnimationData());
        writeString(buffer, skin.getCapeId());
        writeString(buffer, skin.getFullSkinId());
        writeString(buffer, skin.getArmSize());
        writeString(buffer, skin.getSkinColor());
        List<PersonaPieceData> pieces = skin.getPersonaPieces();
        buffer.writeIntLE(pieces.size());
        for (PersonaPieceData piece : pieces) {
            writeString(buffer, piece.getId());
            writeString(buffer, piece.getType());
            writeString(buffer, piece.getPackId());
            buffer.writeBoolean(piece.isDefault());
            writeString(buffer, piece.getProductId());
        }
        List<PersonaPieceTintData> tints = skin.getTintColors();
        buffer.writeIntLE(tints.size());
        for (PersonaPieceTintData tint : tints) {
            writeString(buffer, tint.getType());
            List<String> colors = tint.getColors();
            buffer.writeIntLE(colors.size());
            for (String color : colors) {
                writeString(buffer, color);
            }
        }
        buffer.writeBoolean(skin.isPremium());
        buffer.writeBoolean(skin.isPersona());
        buffer.writeBoolean(skin.isCapeOnClassic());
        buffer.writeBoolean(skin.isPrimaryUser());
    }
}
