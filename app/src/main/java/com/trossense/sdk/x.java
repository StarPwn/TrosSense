package com.trossense.sdk;

import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.function.BiFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419;
import org.cloudburstmc.protocol.bedrock.data.InputInteractionModel;
import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.data.PlayerBlockActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.ItemUseTransaction;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.LegacySetItemSlotData;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class x extends PlayerAuthInputSerializer_v419 {
    private static String[] b;
    private static final long c = dj.a(876785681137187842L, 8584033365741743730L, MethodHandles.lookup().lookupClass()).a(98886952106029L);
    protected static final InputInteractionModel[] a = InputInteractionModel.values();

    static {
        b(null);
    }

    public static void b(String[] strArr) {
        b = strArr;
    }

    public static String[] b() {
        return b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LegacySetItemSlotData lambda$deserialize$1(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper) {
        return new LegacySetItemSlotData(byteBuf.readByte(), bedrockCodecHelper.readByteArray(byteBuf));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lambda$serialize$0(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, LegacySetItemSlotData legacySetItemSlotData) {
        byteBuf.writeByte(legacySetItemSlotData.getContainerId());
        bedrockCodecHelper.writeByteArray(byteBuf, legacySetItemSlotData.getSlots());
    }

    protected PlayerBlockActionData a(long j, ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper) {
        PlayerBlockActionData playerBlockActionData = new PlayerBlockActionData();
        playerBlockActionData.setAction(PlayerActionType.values()[VarInts.readInt(byteBuf)]);
        switch (v.a[playerBlockActionData.getAction().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                playerBlockActionData.setBlockPosition(bedrockCodecHelper.readVector3i(byteBuf));
                playerBlockActionData.setFace(VarInts.readInt(byteBuf));
            default:
                return playerBlockActionData;
        }
    }

    protected void a(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, long j, PlayerBlockActionData playerBlockActionData) {
        VarInts.writeInt(byteBuf, playerBlockActionData.getAction().ordinal());
        switch (v.a[playerBlockActionData.getAction().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                bedrockCodecHelper.writeVector3i(byteBuf, playerBlockActionData.getBlockPosition());
                VarInts.writeInt(byteBuf, playerBlockActionData.getFace());
                return;
            default:
                return;
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, PlayerAuthInputPacket playerAuthInputPacket) {
        long j = (c ^ 104758441033083L) ^ 16449152906193L;
        String[] b2 = b();
        super.deserialize(byteBuf, bedrockCodecHelper, playerAuthInputPacket);
        playerAuthInputPacket.setCameraDeparted(byteBuf.readBoolean());
        if (playerAuthInputPacket.getInputData().contains(PlayerAuthInputData.PERFORM_ITEM_INTERACTION)) {
            ItemUseTransaction itemUseTransaction = new ItemUseTransaction();
            int readInt = VarInts.readInt(byteBuf);
            itemUseTransaction.setLegacyRequestId(readInt);
            if (readInt < -1 && (readInt & 1) == 0) {
                bedrockCodecHelper.readArray(byteBuf, itemUseTransaction.getLegacySlots(), new BiFunction() { // from class: com.trossense.sdk.x$$ExternalSyntheticLambda1
                    @Override // java.util.function.BiFunction
                    public final Object apply(Object obj, Object obj2) {
                        return x.lambda$deserialize$1((ByteBuf) obj, (BedrockCodecHelper) obj2);
                    }
                });
            }
            bedrockCodecHelper.readInventoryActions(byteBuf, itemUseTransaction.getActions());
            itemUseTransaction.setActionType(VarInts.readUnsignedInt(byteBuf));
            itemUseTransaction.setBlockPosition(bedrockCodecHelper.readBlockPosition(byteBuf));
            itemUseTransaction.setBlockFace(VarInts.readInt(byteBuf));
            itemUseTransaction.setHotbarSlot(VarInts.readInt(byteBuf));
            itemUseTransaction.setItemInHand(bedrockCodecHelper.readItem(byteBuf));
            itemUseTransaction.setPlayerPosition(bedrockCodecHelper.readVector3f(byteBuf));
            itemUseTransaction.setClickPosition(bedrockCodecHelper.readVector3f(byteBuf));
            itemUseTransaction.setBlockDefinition(bedrockCodecHelper.getBlockDefinitions().getDefinition(VarInts.readUnsignedInt(byteBuf)));
            playerAuthInputPacket.setItemUseTransaction(itemUseTransaction);
        }
        if (playerAuthInputPacket.getInputData().contains(PlayerAuthInputData.PERFORM_ITEM_STACK_REQUEST)) {
            playerAuthInputPacket.setItemStackRequest(bedrockCodecHelper.readItemStackRequest(byteBuf));
        }
        if (playerAuthInputPacket.getInputData().contains(PlayerAuthInputData.PERFORM_BLOCK_ACTIONS)) {
            int readInt2 = VarInts.readInt(byteBuf);
            int i = 0;
            while (true) {
                if (i >= readInt2) {
                    break;
                }
                playerAuthInputPacket.getPlayerActions().add(a(j, byteBuf, bedrockCodecHelper));
                i++;
                if (b2 != null) {
                    break;
                } else if (b2 != null) {
                    PointerHolder.b(new int[4]);
                    break;
                }
            }
        }
        playerAuthInputPacket.setAnalogMoveVector(bedrockCodecHelper.readVector2f(byteBuf));
        ad adVar = (ad) playerAuthInputPacket;
        adVar.a(byteBuf.readableBytes() != 0);
        if (adVar.a()) {
            adVar.b(byteBuf.readBoolean());
            adVar.a(byteBuf.readFloat());
            adVar.b(byteBuf.readFloat());
            adVar.c(byteBuf.readBoolean());
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388
    protected void readInteractionModel(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, PlayerAuthInputPacket playerAuthInputPacket) {
        playerAuthInputPacket.setInputInteractionModel(a[VarInts.readUnsignedInt(byteBuf)]);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, PlayerAuthInputPacket playerAuthInputPacket) {
        long j = (c ^ 10047483485905L) ^ 35477288147789L;
        String[] b2 = b();
        super.serialize(byteBuf, bedrockCodecHelper, playerAuthInputPacket);
        byteBuf.writeBoolean(playerAuthInputPacket.isCameraDeparted());
        if (playerAuthInputPacket.getInputData().contains(PlayerAuthInputData.PERFORM_ITEM_INTERACTION)) {
            ItemUseTransaction itemUseTransaction = playerAuthInputPacket.getItemUseTransaction();
            int legacyRequestId = itemUseTransaction.getLegacyRequestId();
            VarInts.writeInt(byteBuf, legacyRequestId);
            if (legacyRequestId < -1 && (legacyRequestId & 1) == 0) {
                bedrockCodecHelper.writeArray(byteBuf, itemUseTransaction.getLegacySlots(), new TriConsumer() { // from class: com.trossense.sdk.x$$ExternalSyntheticLambda0
                    @Override // org.cloudburstmc.protocol.common.util.TriConsumer
                    public final void accept(Object obj, Object obj2, Object obj3) {
                        x.lambda$serialize$0((ByteBuf) obj, (BedrockCodecHelper) obj2, (LegacySetItemSlotData) obj3);
                    }
                });
            }
            bedrockCodecHelper.writeInventoryActions(byteBuf, itemUseTransaction.getActions(), itemUseTransaction.isUsingNetIds());
            VarInts.writeUnsignedInt(byteBuf, itemUseTransaction.getActionType());
            bedrockCodecHelper.writeBlockPosition(byteBuf, itemUseTransaction.getBlockPosition());
            VarInts.writeInt(byteBuf, itemUseTransaction.getBlockFace());
            VarInts.writeInt(byteBuf, itemUseTransaction.getHotbarSlot());
            bedrockCodecHelper.writeItem(byteBuf, itemUseTransaction.getItemInHand());
            bedrockCodecHelper.writeVector3f(byteBuf, itemUseTransaction.getPlayerPosition());
            bedrockCodecHelper.writeVector3f(byteBuf, itemUseTransaction.getClickPosition());
            VarInts.writeUnsignedInt(byteBuf, itemUseTransaction.getBlockDefinition().getRuntimeId());
        }
        if (playerAuthInputPacket.getInputData().contains(PlayerAuthInputData.PERFORM_ITEM_STACK_REQUEST)) {
            bedrockCodecHelper.writeItemStackRequest(byteBuf, playerAuthInputPacket.getItemStackRequest());
        }
        if (playerAuthInputPacket.getInputData().contains(PlayerAuthInputData.PERFORM_BLOCK_ACTIONS)) {
            VarInts.writeInt(byteBuf, playerAuthInputPacket.getPlayerActions().size());
            Iterator<PlayerBlockActionData> it2 = playerAuthInputPacket.getPlayerActions().iterator();
            while (it2.hasNext()) {
                a(byteBuf, bedrockCodecHelper, j, it2.next());
                if (b2 != null) {
                    break;
                } else if (b2 != null) {
                    break;
                }
            }
        }
        bedrockCodecHelper.writeVector2f(byteBuf, playerAuthInputPacket.getAnalogMoveVector());
        ad adVar = (ad) playerAuthInputPacket;
        if (adVar.a()) {
            byteBuf.writeBoolean(adVar.b());
            byteBuf.writeFloat(adVar.c());
            byteBuf.writeFloat(adVar.d());
            byteBuf.writeBoolean(adVar.e());
        }
        if (PointerHolder.s() == null) {
            b(new String[4]);
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388
    protected void writeInteractionModel(ByteBuf byteBuf, BedrockCodecHelper bedrockCodecHelper, PlayerAuthInputPacket playerAuthInputPacket) {
        VarInts.writeUnsignedInt(byteBuf, playerAuthInputPacket.getInputInteractionModel().ordinal());
    }
}
