package com.trossense.sdk;

import com.trossense.bl;
import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v594.Bedrock_v594;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;
import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ContainerOpenPacket;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;

/* loaded from: classes3.dex */
public class ab extends Bedrock_v594 {
    public static final BedrockCodec a = Bedrock_v594.CODEC.toBuilder().registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return new l();
        }
    }, u.a, bl.cB).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final Object get() {
            return new j();
        }
    }, s.a, bl.c0).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final Object get() {
            return new m();
        }
    }, w.a, 200).deregisterPacket(CommandRequestPacket.class).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda3
        @Override // java.util.function.Supplier
        public final Object get() {
            return new o();
        }
    }, new r(), 77).deregisterPacket(AnimatePacket.class).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda4
        @Override // java.util.function.Supplier
        public final Object get() {
            return new n();
        }
    }, new q(), 44).deregisterPacket(ContainerOpenPacket.class).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda5
        @Override // java.util.function.Supplier
        public final Object get() {
            return new p();
        }
    }, new t(), 46).deregisterPacket(AddEntityPacket.class).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda6
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ac();
        }
    }, new y(), 13).deregisterPacket(PlayerAuthInputPacket.class).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda7
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ad();
        }
    }, new x(), bl.bF).deregisterPacket(StartGamePacket.class).registerPacket(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda8
        @Override // java.util.function.Supplier
        public final Object get() {
            return new ae();
        }
    }, new aa(), 11).helper(new Supplier() { // from class: com.trossense.sdk.ab$$ExternalSyntheticLambda9
        @Override // java.util.function.Supplier
        public final Object get() {
            return ab.lambda$static$0();
        }
    }).build();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BedrockCodecHelper lambda$static$0() {
        return new z(ENTITY_DATA, GAME_RULE_TYPES, ITEM_STACK_REQUEST_TYPES, CONTAINER_SLOT_TYPES, PLAYER_ABILITIES, TEXT_PROCESSING_ORIGINS);
    }
}
