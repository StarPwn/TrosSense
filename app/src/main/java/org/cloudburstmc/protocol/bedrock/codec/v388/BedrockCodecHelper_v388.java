package org.cloudburstmc.protocol.bedrock.codec.v388;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimatedTextureType;
import org.cloudburstmc.protocol.bedrock.data.skin.AnimationData;
import org.cloudburstmc.protocol.bedrock.data.skin.ImageData;
import org.cloudburstmc.protocol.bedrock.data.skin.SerializedSkin;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureAnimationMode;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureMirror;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureRotation;
import org.cloudburstmc.protocol.bedrock.data.structure.StructureSettings;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v388 extends BedrockCodecHelper_v361 {
    protected static final AnimatedTextureType[] TEXTURE_TYPES = AnimatedTextureType.values();

    public BedrockCodecHelper_v388(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes) {
        super(entityData, gameRulesTypes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public SerializedSkin readSkin(ByteBuf buffer) {
        String skinId = readString(buffer);
        String skinResourcePatch = readString(buffer);
        ImageData skinData = readImage(buffer);
        int animationCount = buffer.readIntLE();
        List<AnimationData> animations = new ObjectArrayList<>(animationCount);
        for (int i = 0; i < animationCount; i++) {
            animations.add(readAnimationData(buffer));
        }
        ImageData capeData = readImage(buffer);
        String geometryData = readString(buffer);
        String animationData = readString(buffer);
        boolean premium = buffer.readBoolean();
        boolean persona = buffer.readBoolean();
        boolean capeOnClassic = buffer.readBoolean();
        String capeId = readString(buffer);
        String fullSkinId = readString(buffer);
        return SerializedSkin.of(skinId, "", skinResourcePatch, skinData, animations, capeData, geometryData, animationData, premium, persona, capeOnClassic, capeId, fullSkinId);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeSkin(ByteBuf buffer, SerializedSkin skin) {
        Objects.requireNonNull(skin, "Skin is null");
        writeString(buffer, skin.getSkinId());
        writeString(buffer, skin.getSkinResourcePatch());
        writeImage(buffer, skin.getSkinData());
        List<AnimationData> animations = skin.getAnimations();
        buffer.writeIntLE(animations.size());
        for (AnimationData animation : animations) {
            writeAnimationData(buffer, animation);
        }
        writeImage(buffer, skin.getCapeData());
        writeString(buffer, skin.getGeometryData());
        writeString(buffer, skin.getAnimationData());
        buffer.writeBoolean(skin.isPremium());
        buffer.writeBoolean(skin.isPersona());
        buffer.writeBoolean(skin.isCapeOnClassic());
        writeString(buffer, skin.getCapeId());
        writeString(buffer, skin.getFullSkinId());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper
    public AnimationData readAnimationData(ByteBuf buffer) {
        ImageData image = readImage(buffer);
        AnimatedTextureType type = TEXTURE_TYPES[buffer.readIntLE()];
        float frames = buffer.readFloatLE();
        return new AnimationData(image, type, frames);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper
    public void writeAnimationData(ByteBuf buffer, AnimationData animation) {
        writeImage(buffer, animation.getImage());
        buffer.writeIntLE(animation.getTextureType().ordinal());
        buffer.writeFloatLE(animation.getFrames());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper
    public ImageData readImage(ByteBuf buffer) {
        int width = buffer.readIntLE();
        int height = buffer.readIntLE();
        byte[] image = readByteArray(buffer);
        return ImageData.of(width, height, image);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper
    public void writeImage(ByteBuf buffer, ImageData image) {
        Objects.requireNonNull(image, "image is null");
        buffer.writeIntLE(image.getWidth());
        buffer.writeIntLE(image.getHeight());
        writeByteArray(buffer, image.getImage());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        String paletteName = readString(buffer);
        boolean ignoringEntities = buffer.readBoolean();
        boolean ignoringBlocks = buffer.readBoolean();
        Vector3i size = readBlockPosition(buffer);
        Vector3i offset = readBlockPosition(buffer);
        long lastEditedByEntityId = VarInts.readLong(buffer);
        StructureRotation rotation = StructureRotation.from(buffer.readByte());
        StructureMirror mirror = StructureMirror.from(buffer.readByte());
        float integrityValue = buffer.readFloatLE();
        int integritySeed = buffer.readIntLE();
        Vector3f pivot = readVector3f(buffer);
        return new StructureSettings(paletteName, ignoringEntities, ignoringBlocks, true, size, offset, lastEditedByEntityId, rotation, mirror, StructureAnimationMode.NONE, 0.0f, integrityValue, integritySeed, pivot);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v361.BedrockCodecHelper_v361, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        super.writeStructureSettings(buffer, settings);
        writeVector3f(buffer, settings.getPivot());
    }
}
