package org.cloudburstmc.protocol.bedrock.codec.v618.serializer;

import io.netty.buffer.ByteBuf;
import java.awt.Color;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.StartGameSerializer_v407$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraEase;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraFadeInstruction;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraSetInstruction;
import org.cloudburstmc.protocol.bedrock.packet.CameraInstructionPacket;
import org.cloudburstmc.protocol.common.NamedDefinition;
import org.cloudburstmc.protocol.common.util.DefinitionUtils;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class CameraInstructionSerializer_618 implements BedrockPacketSerializer<CameraInstructionPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, CameraInstructionPacket packet) {
        helper.writeOptionalNull(buffer, packet.getSetInstruction(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CameraInstructionSerializer_618.this.m2100x882efe0(helper, buffer, (ByteBuf) obj, (CameraSetInstruction) obj2);
            }
        });
        helper.writeOptional(buffer, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), packet.getClear(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
        helper.writeOptionalNull(buffer, packet.getFadeInstruction(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CameraInstructionSerializer_618.this.m2101xda9279e2(helper, (ByteBuf) obj, (CameraFadeInstruction) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$1$org-cloudburstmc-protocol-bedrock-codec-v618-serializer-CameraInstructionSerializer_618, reason: not valid java name */
    public /* synthetic */ void m2100x882efe0(final BedrockCodecHelper helper, ByteBuf buffer, ByteBuf buf, CameraSetInstruction set) {
        DefinitionUtils.checkDefinition(helper.getCameraPresetDefinitions(), set.getPreset());
        buffer.writeIntLE(set.getPreset().getRuntimeId());
        helper.writeOptionalNull(buf, set.getEase(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CameraInstructionSerializer_618.this.writeEase((ByteBuf) obj, (CameraSetInstruction.EaseData) obj2);
            }
        });
        Vector3f pos = set.getPos();
        helper.getClass();
        helper.writeOptionalNull(buf, pos, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda10
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeVector3f((ByteBuf) obj, (Vector3f) obj2);
            }
        });
        Vector2f rot = set.getRot();
        helper.getClass();
        helper.writeOptionalNull(buf, rot, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda11
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeVector2f((ByteBuf) obj, (Vector2f) obj2);
            }
        });
        Vector3f facing = set.getFacing();
        helper.getClass();
        helper.writeOptionalNull(buf, facing, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda10
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                BedrockCodecHelper.this.writeVector3f((ByteBuf) obj, (Vector3f) obj2);
            }
        });
        helper.writeOptional(buf, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), set.getDefaultPreset(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda12
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$3$org-cloudburstmc-protocol-bedrock-codec-v618-serializer-CameraInstructionSerializer_618, reason: not valid java name */
    public /* synthetic */ void m2101xda9279e2(BedrockCodecHelper helper, ByteBuf buf, CameraFadeInstruction fade) {
        helper.writeOptionalNull(buf, fade.getTimeData(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CameraInstructionSerializer_618.this.writeTimeData((ByteBuf) obj, (CameraFadeInstruction.TimeData) obj2);
            }
        });
        helper.writeOptionalNull(buf, fade.getColor(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CameraInstructionSerializer_618.this.writeColor((ByteBuf) obj, (Color) obj2);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(final ByteBuf buffer, final BedrockCodecHelper helper, CameraInstructionPacket packet) {
        CameraSetInstruction set = (CameraSetInstruction) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CameraInstructionSerializer_618.this.m2098xb6358b03(helper, buffer, (ByteBuf) obj);
            }
        });
        packet.setSetInstruction(set);
        packet.setClear((OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(((ByteBuf) obj).readBoolean());
                return of;
            }
        }));
        CameraFadeInstruction fade = (CameraFadeInstruction) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CameraInstructionSerializer_618.this.m2099x88451505(helper, (ByteBuf) obj);
            }
        });
        packet.setFadeInstruction(fade);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$deserialize$5$org-cloudburstmc-protocol-bedrock-codec-v618-serializer-CameraInstructionSerializer_618, reason: not valid java name */
    public /* synthetic */ CameraSetInstruction m2098xb6358b03(final BedrockCodecHelper helper, ByteBuf buffer, ByteBuf buf) {
        int runtimeId = buf.readIntLE();
        NamedDefinition definition = helper.getCameraPresetDefinitions().getDefinition(runtimeId);
        Preconditions.checkNotNull(definition, "Unknown camera preset " + runtimeId);
        CameraSetInstruction.EaseData ease = (CameraSetInstruction.EaseData) helper.readOptional(buf, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda15
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CameraInstructionSerializer_618.this.readEase((ByteBuf) obj);
            }
        });
        helper.getClass();
        Vector3f pos = (Vector3f) helper.readOptional(buf, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readVector3f((ByteBuf) obj);
            }
        });
        helper.getClass();
        Vector2f rot = (Vector2f) helper.readOptional(buf, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda17
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readVector2f((ByteBuf) obj);
            }
        });
        helper.getClass();
        Vector3f facing = (Vector3f) helper.readOptional(buf, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda16
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BedrockCodecHelper.this.readVector3f((ByteBuf) obj);
            }
        });
        OptionalBoolean defaultPreset = (OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(((ByteBuf) obj).readBoolean());
                return of;
            }
        });
        return new CameraSetInstruction(definition, ease, pos, rot, facing, defaultPreset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$deserialize$7$org-cloudburstmc-protocol-bedrock-codec-v618-serializer-CameraInstructionSerializer_618, reason: not valid java name */
    public /* synthetic */ CameraFadeInstruction m2099x88451505(BedrockCodecHelper helper, ByteBuf buf) {
        CameraFadeInstruction.TimeData time = (CameraFadeInstruction.TimeData) helper.readOptional(buf, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CameraInstructionSerializer_618.this.readTimeData((ByteBuf) obj);
            }
        });
        Color color = (Color) helper.readOptional(buf, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraInstructionSerializer_618$$ExternalSyntheticLambda14
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CameraInstructionSerializer_618.this.readColor((ByteBuf) obj);
            }
        });
        return new CameraFadeInstruction(time, color);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEase(ByteBuf buffer, CameraSetInstruction.EaseData ease) {
        buffer.writeByte(ease.getEaseType().ordinal());
        buffer.writeFloatLE(ease.getTime());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CameraSetInstruction.EaseData readEase(ByteBuf buffer) {
        CameraEase type = CameraEase.values()[buffer.readUnsignedByte()];
        float time = buffer.readFloatLE();
        return new CameraSetInstruction.EaseData(type, time);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeTimeData(ByteBuf buffer, CameraFadeInstruction.TimeData timeData) {
        buffer.writeFloatLE(timeData.getFadeInTime());
        buffer.writeFloatLE(timeData.getWaitTime());
        buffer.writeFloatLE(timeData.getFadeOutTime());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CameraFadeInstruction.TimeData readTimeData(ByteBuf buffer) {
        float fadeIn = buffer.readFloatLE();
        float wait = buffer.readFloatLE();
        float fadeOut = buffer.readFloatLE();
        return new CameraFadeInstruction.TimeData(fadeIn, wait, fadeOut);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeColor(ByteBuf buffer, Color color) {
        buffer.writeFloatLE(color.getRed() / 255.0f);
        buffer.writeFloatLE(color.getGreen() / 255.0f);
        buffer.writeFloatLE(color.getBlue() / 255.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Color readColor(ByteBuf buffer) {
        return new Color((int) (buffer.readFloatLE() * 255.0f), (int) (buffer.readFloatLE() * 255.0f), (int) (buffer.readFloatLE() * 255.0f));
    }
}
