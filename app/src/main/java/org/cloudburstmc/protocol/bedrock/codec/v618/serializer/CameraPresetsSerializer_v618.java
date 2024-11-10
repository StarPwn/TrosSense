package org.cloudburstmc.protocol.bedrock.codec.v618.serializer;

import io.netty.buffer.ByteBuf;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.codec.v407.serializer.StartGameSerializer_v407$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraAudioListener;
import org.cloudburstmc.protocol.bedrock.data.camera.CameraPreset;
import org.cloudburstmc.protocol.bedrock.packet.CameraPresetsPacket;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import org.cloudburstmc.protocol.common.util.TriConsumer;

/* loaded from: classes5.dex */
public class CameraPresetsSerializer_v618 implements BedrockPacketSerializer<CameraPresetsPacket> {
    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, CameraPresetsPacket packet) {
        helper.writeArray(buffer, packet.getPresets(), new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda3
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                CameraPresetsSerializer_v618.this.writePreset((ByteBuf) obj, (BedrockCodecHelper) obj2, (CameraPreset) obj3);
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, CameraPresetsPacket packet) {
        helper.readArray(buffer, packet.getPresets(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return CameraPresetsSerializer_v618.this.readPreset((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
    }

    public void writePreset(ByteBuf buffer, BedrockCodecHelper helper, CameraPreset preset) {
        helper.writeString(buffer, preset.getIdentifier());
        helper.writeString(buffer, preset.getParentPreset());
        helper.writeOptionalNull(buffer, preset.getPos(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeFloatLE(((Vector3f) obj2).getX());
            }
        });
        helper.writeOptionalNull(buffer, preset.getPos(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeFloatLE(((Vector3f) obj2).getY());
            }
        });
        helper.writeOptionalNull(buffer, preset.getPos(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda6
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeFloatLE(((Vector3f) obj2).getZ());
            }
        });
        helper.writeOptionalNull(buffer, preset.getPitch(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeFloatLE(((Float) obj2).floatValue());
            }
        });
        helper.writeOptionalNull(buffer, preset.getYaw(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeFloatLE(((Float) obj2).floatValue());
            }
        });
        helper.writeOptionalNull(buffer, preset.getListener(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeByte(((CameraAudioListener) obj2).ordinal());
            }
        });
        helper.writeOptional(buffer, new StartGameSerializer_v407$$ExternalSyntheticLambda1(), preset.getPlayEffect(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeBoolean(((OptionalBoolean) obj2).getAsBoolean());
            }
        });
    }

    public CameraPreset readPreset(ByteBuf buffer, BedrockCodecHelper helper) {
        String identifier = helper.readString(buffer);
        String parentPreset = helper.readString(buffer);
        Float x = (Float) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                float readFloatLE;
                readFloatLE = ((ByteBuf) obj).readFloatLE();
                return Float.valueOf(readFloatLE);
            }
        });
        Float y = (Float) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                float readFloatLE;
                readFloatLE = ((ByteBuf) obj).readFloatLE();
                return Float.valueOf(readFloatLE);
            }
        });
        Float z = (Float) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                float readFloatLE;
                readFloatLE = ((ByteBuf) obj).readFloatLE();
                return Float.valueOf(readFloatLE);
            }
        });
        Vector3f pos = (x == null || y == null || z == null) ? null : Vector3f.from(x.floatValue(), y.floatValue(), z.floatValue());
        Float pitch = (Float) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                float readFloatLE;
                readFloatLE = ((ByteBuf) obj).readFloatLE();
                return Float.valueOf(readFloatLE);
            }
        });
        Float yaw = (Float) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                float readFloatLE;
                readFloatLE = ((ByteBuf) obj).readFloatLE();
                return Float.valueOf(readFloatLE);
            }
        });
        CameraAudioListener listener = (CameraAudioListener) helper.readOptional(buffer, null, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CameraPresetsSerializer_v618.lambda$readPreset$5((ByteBuf) obj);
            }
        });
        OptionalBoolean effects = (OptionalBoolean) helper.readOptional(buffer, OptionalBoolean.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v618.serializer.CameraPresetsSerializer_v618$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OptionalBoolean of;
                of = OptionalBoolean.of(((ByteBuf) obj).readBoolean());
                return of;
            }
        });
        return new CameraPreset(identifier, parentPreset, pos, yaw, pitch, listener, effects);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ CameraAudioListener lambda$readPreset$5(ByteBuf buf) {
        return CameraAudioListener.values()[buf.readUnsignedByte()];
    }
}
