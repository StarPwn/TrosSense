package org.cloudburstmc.protocol.bedrock.codec.v575.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.awt.Color;
import java.util.List;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraEase;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraFadeInstruction;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraSetInstruction;
import org.cloudburstmc.protocol.bedrock.packet.CameraInstructionPacket;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.util.DefinitionUtils;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes5.dex */
public class CameraInstructionSerializer_v575 implements BedrockPacketSerializer<CameraInstructionPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CameraInstructionPacket packet) {
        NbtMapBuilder tag = NbtMap.builder();
        if (packet.getSetInstruction() != null) {
            CameraSetInstruction set = packet.getSetInstruction();
            DefinitionUtils.checkDefinition(helper.getCameraPresetDefinitions(), set.getPreset());
            NbtMapBuilder builder = NbtMap.builder().putInt("preset", set.getPreset().getRuntimeId());
            if (set.getEase() != null) {
                builder.putCompound("ease", NbtMap.builder().putString("type", set.getEase().getEaseType().getSerializeName()).putFloat(RtspHeaders.Values.TIME, set.getEase().getTime()).build());
            }
            if (set.getPos() != null) {
                builder.putCompound("pos", NbtMap.builder().putList("pos", NbtType.FLOAT, Float.valueOf(set.getPos().getX()), Float.valueOf(set.getPos().getY()), Float.valueOf(set.getPos().getZ())).build());
            }
            if (set.getRot() != null) {
                builder.putCompound("rot", NbtMap.builder().putFloat("x", set.getRot().getX()).putFloat(EllipticCurveJsonWebKey.Y_MEMBER_NAME, set.getRot().getY()).build());
            }
            if (set.getDefaultPreset().isPresent()) {
                builder.putBoolean("default", set.getDefaultPreset().getAsBoolean());
            }
            tag.put("set", (Object) builder.build());
        }
        if (packet.getClear().isPresent()) {
            tag.putBoolean("clear", packet.getClear().getAsBoolean());
        }
        if (packet.getFadeInstruction() != null) {
            CameraFadeInstruction fade = packet.getFadeInstruction();
            NbtMapBuilder builder2 = NbtMap.builder();
            if (fade.getTimeData() != null) {
                builder2.putCompound(RtspHeaders.Values.TIME, NbtMap.builder().putFloat("fadeIn", fade.getTimeData().getFadeInTime()).putFloat("hold", fade.getTimeData().getWaitTime()).putFloat("fadeOut", fade.getTimeData().getFadeOutTime()).build());
            }
            if (fade.getColor() != null) {
                builder2.putCompound("color", NbtMap.builder().putFloat(RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME, fade.getColor().getRed() / 255.0f).putFloat("g", fade.getColor().getBlue() / 255.0f).putFloat("b", fade.getColor().getGreen() / 255.0f).build());
            }
            tag.put("fade", (Object) builder2.build());
        }
        helper.writeTag(buffer, tag.build());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CameraInstructionPacket packet) {
        NbtMap tag = (NbtMap) helper.readTag(buffer, NbtMap.class);
        if (tag.containsKey("set", NbtType.COMPOUND)) {
            CameraSetInstruction set = new CameraSetInstruction();
            NbtMap setTag = tag.getCompound("set");
            int runtimeId = setTag.getInt("preset");
            NamedDefinition definition = helper.getCameraPresetDefinitions().getDefinition(runtimeId);
            Preconditions.checkNotNull(definition, "Unknown camera preset " + runtimeId);
            set.setPreset(definition);
            if (setTag.containsKey("ease", NbtType.COMPOUND)) {
                NbtMap easeTag = setTag.getCompound("ease");
                CameraEase type = CameraEase.fromName(easeTag.getString("type"));
                float time = easeTag.getFloat(RtspHeaders.Values.TIME);
                set.setEase(new CameraSetInstruction.EaseData(type, time));
            }
            if (setTag.containsKey("pos", NbtType.COMPOUND)) {
                List<Float> floats = setTag.getCompound("pos").getList("pos", NbtType.FLOAT);
                float x = floats.size() > 0 ? floats.get(0).floatValue() : 0.0f;
                float y = floats.size() > 1 ? floats.get(1).floatValue() : 0.0f;
                float z = floats.size() > 2 ? floats.get(2).floatValue() : 0.0f;
                set.setPos(Vector3f.from(x, y, z));
            }
            if (setTag.containsKey("rot", NbtType.COMPOUND)) {
                NbtMap rot = setTag.getCompound("rot");
                float pitch = rot.containsKey("x", NbtType.FLOAT) ? rot.getFloat("x") : 0.0f;
                float yaw = rot.containsKey(EllipticCurveJsonWebKey.Y_MEMBER_NAME, NbtType.FLOAT) ? rot.getFloat(EllipticCurveJsonWebKey.Y_MEMBER_NAME) : 0.0f;
                set.setRot(Vector2f.from(pitch, yaw));
            }
            if (setTag.containsKey("default", NbtType.BYTE)) {
                set.setDefaultPreset(OptionalBoolean.of(setTag.getBoolean("default")));
            }
            packet.setSetInstruction(set);
        }
        if (tag.containsKey("clear", NbtType.BYTE)) {
            packet.setClear(OptionalBoolean.of(tag.getBoolean("clear")));
        }
        if (tag.containsKey("fade", NbtType.COMPOUND)) {
            CameraFadeInstruction fade = new CameraFadeInstruction();
            NbtMap fadeTag = tag.getCompound("fade");
            if (fadeTag.containsKey(RtspHeaders.Values.TIME, NbtType.COMPOUND)) {
                NbtMap timeTag = fadeTag.getCompound(RtspHeaders.Values.TIME);
                float fadeIn = timeTag.getFloat("fadeIn");
                float wait = timeTag.getFloat("hold");
                float fadeout = timeTag.getFloat("fadeOut");
                fade.setTimeData(new CameraFadeInstruction.TimeData(fadeIn, wait, fadeout));
            }
            if (fadeTag.containsKey("color", NbtType.COMPOUND)) {
                NbtMap colorTag = tag.getCompound("color");
                fade.setColor(new Color((int) (colorTag.getFloat(RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME) * 255.0f), (int) (colorTag.getFloat("b") * 255.0f), (int) (colorTag.getFloat("g") * 255.0f)));
            }
            packet.setFadeInstruction(fade);
        }
    }
}
