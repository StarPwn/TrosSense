package org.cloudburstmc.protocol.bedrock.codec.v544.serializer;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormResponseSerializer_v291;
import org.cloudburstmc.protocol.bedrock.data.ModalFormCancelReason;
import org.cloudburstmc.protocol.bedrock.packet.ModalFormResponsePacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class ModalFormResponseSerializer_v544 extends ModalFormResponseSerializer_v291 {
    protected static final ModalFormCancelReason[] VALUES = ModalFormCancelReason.values();

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormResponseSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, ModalFormResponsePacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getFormId());
        Predicate predicate = new Predicate() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ModalFormResponseSerializer_v544$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean nonNull;
                nonNull = Objects.nonNull((String) obj);
                return nonNull;
            }
        };
        String formData = packet.getFormData();
        helper.getClass();
        helper.writeOptional(buffer, predicate, formData, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        helper.writeOptional(buffer, new Predicate() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ModalFormResponseSerializer_v544$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isPresent;
                isPresent = ((Optional) obj).isPresent();
                return isPresent;
            }
        }, packet.getCancelReason(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ModalFormResponseSerializer_v544$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeByte(((ModalFormCancelReason) ((Optional) obj2).get()).ordinal());
            }
        });
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.ModalFormResponseSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, ModalFormResponsePacket packet) {
        packet.setFormId(VarInts.readUnsignedInt(buffer));
        helper.getClass();
        packet.setFormData((String) helper.readOptional(buffer, null, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper)));
        packet.setCancelReason((Optional) helper.readOptional(buffer, Optional.empty(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v544.serializer.ModalFormResponseSerializer_v544$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Optional of;
                of = Optional.of(ModalFormResponseSerializer_v544.VALUES[((ByteBuf) obj).readByte()]);
                return of;
            }
        }));
    }
}
