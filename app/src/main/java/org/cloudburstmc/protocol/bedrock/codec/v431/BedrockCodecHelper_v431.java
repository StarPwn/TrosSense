package org.cloudburstmc.protocol.bedrock.codec.v431;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftResultsDeprecatedAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventoryActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.InventorySource;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufInputStream;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;

/* loaded from: classes5.dex */
public class BedrockCodecHelper_v431 extends BedrockCodecHelper_v428 {
    public BedrockCodecHelper_v431(EntityDataTypeMap entityData, TypeMap<Class<?>> gameRulesTypes, TypeMap<ItemStackRequestActionType> stackRequestActionTypes, TypeMap<ContainerSlotType> containerSlotTypes) {
        super(entityData, gameRulesTypes, stackRequestActionTypes, containerSlotTypes);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0093 A[Catch: all -> 0x0118, LOOP:0: B:22:0x0090->B:24:0x0093, LOOP_END, TryCatch #3 {all -> 0x0118, blocks: (B:21:0x0089, B:22:0x0090, B:24:0x0093, B:26:0x009c, B:27:0x00a3, B:29:0x00a6, B:32:0x00b1, B:34:0x00bd, B:53:0x0073, B:54:0x0084), top: B:16:0x003e }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a6 A[Catch: all -> 0x0118, LOOP:1: B:27:0x00a3->B:29:0x00a6, LOOP_END, TryCatch #3 {all -> 0x0118, blocks: (B:21:0x0089, B:22:0x0090, B:24:0x0093, B:26:0x009c, B:27:0x00a3, B:29:0x00a6, B:32:0x00b1, B:34:0x00bd, B:53:0x0073, B:54:0x0084), top: B:16:0x003e }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00cf  */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.cloudburstmc.protocol.bedrock.data.inventory.ItemData readItemInstance(io.netty.buffer.ByteBuf r19) {
        /*
            Method dump skipped, instructions count: 333
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431.readItemInstance(io.netty.buffer.ByteBuf):org.cloudburstmc.protocol.bedrock.data.inventory.ItemData");
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v340.BedrockCodecHelper_v340, org.cloudburstmc.protocol.bedrock.codec.v332.BedrockCodecHelper_v332, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readItem(ByteBuf buffer) {
        int netId;
        Throwable th;
        NBTInputStream nbtStream;
        long blockingTicks;
        Throwable th2;
        int nbtSize;
        int runtimeId = VarInts.readInt(buffer);
        if (runtimeId == 0) {
            return ItemData.AIR;
        }
        ItemDefinition definition = this.itemDefinitions.getDefinition(runtimeId);
        int count = buffer.readUnsignedShortLE();
        int damage = VarInts.readUnsignedInt(buffer);
        boolean hasNetId = buffer.readBoolean();
        if (hasNetId) {
            int netId2 = VarInts.readInt(buffer);
            netId = netId2;
        } else {
            netId = 0;
        }
        int blockRuntimeId = VarInts.readInt(buffer);
        NbtMap compoundTag = null;
        ByteBuf buf = buffer.readSlice(VarInts.readUnsignedInt(buffer));
        try {
            LittleEndianByteBufInputStream stream = new LittleEndianByteBufInputStream(buf);
            try {
                nbtStream = new NBTInputStream(stream);
                try {
                    nbtSize = stream.readShort();
                } catch (Throwable th3) {
                    blockingTicks = 0;
                    th2 = th3;
                }
                if (nbtSize > 0) {
                    try {
                        compoundTag = (NbtMap) nbtStream.readTag();
                        blockingTicks = 0;
                    } catch (Throwable th4) {
                        blockingTicks = 0;
                        th2 = th4;
                    }
                } else {
                    if (nbtSize == -1) {
                        try {
                            int tagCount = stream.readUnsignedByte();
                            if (tagCount == 1) {
                                try {
                                    compoundTag = (NbtMap) nbtStream.readTag();
                                    blockingTicks = 0;
                                } catch (Throwable th5) {
                                    th2 = th5;
                                    blockingTicks = 0;
                                }
                            } else {
                                try {
                                    blockingTicks = 0;
                                    try {
                                        throw new IllegalArgumentException("Expected 1 tag but got " + tagCount);
                                    } catch (Throwable th6) {
                                        th2 = th6;
                                    }
                                } catch (Throwable th7) {
                                    blockingTicks = 0;
                                    th2 = th7;
                                }
                            }
                        } catch (Throwable th8) {
                            blockingTicks = 0;
                            th2 = th8;
                        }
                        try {
                            throw th2;
                        } catch (Throwable th9) {
                            try {
                                try {
                                    nbtStream.close();
                                    throw th9;
                                } catch (Throwable th10) {
                                    th = th10;
                                    try {
                                        throw th;
                                    } catch (Throwable th11) {
                                        try {
                                            try {
                                                stream.close();
                                                throw th11;
                                            } catch (IOException e) {
                                                e = e;
                                                throw new IllegalStateException("Unable to read item user data", e);
                                            }
                                        } catch (Throwable th12) {
                                            th.addSuppressed(th12);
                                            throw th11;
                                        }
                                    }
                                }
                            } catch (Throwable th13) {
                                th2.addSuppressed(th13);
                                throw th9;
                            }
                        }
                    }
                    blockingTicks = 0;
                }
            } catch (Throwable th14) {
                th = th14;
            }
            try {
                String[] canPlace = new String[stream.readInt()];
                for (int i = 0; i < canPlace.length; i++) {
                    try {
                        canPlace[i] = stream.readUTF();
                    } catch (Throwable th15) {
                        th2 = th15;
                    }
                }
                String[] canBreak = new String[stream.readInt()];
                for (int i2 = 0; i2 < canBreak.length; i2++) {
                    canBreak[i2] = stream.readUTF();
                }
                long blockingTicks2 = (definition == null || !"minecraft:shield".equals(definition.getIdentifier())) ? blockingTicks : stream.readLong();
                try {
                    nbtStream.close();
                    try {
                        stream.close();
                        if (buf.isReadable()) {
                            log.info("Item user data has {} readable bytes left\n{}", Integer.valueOf(buf.readableBytes()), ByteBufUtil.prettyHexDump(buf.readerIndex(0)));
                        }
                        return ItemData.builder().definition(definition).damage(damage).count(count).tag(compoundTag).canPlace(canPlace).canBreak(canBreak).blockingTicks(blockingTicks2).blockDefinition(this.blockDefinitions.getDefinition(blockRuntimeId)).usingNetId(hasNetId).netId(netId).build();
                    } catch (IOException e2) {
                        e = e2;
                        throw new IllegalStateException("Unable to read item user data", e);
                    }
                } catch (Throwable th16) {
                    th = th16;
                    throw th;
                }
            } catch (Throwable th17) {
                th2 = th17;
            }
        } catch (IOException e3) {
            e = e3;
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public ItemData readNetItem(ByteBuf buffer) {
        return readItem(buffer);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItemInstance(ByteBuf buffer, ItemData item) {
        Objects.requireNonNull(item, "item is null!");
        ItemDefinition definition = item.getDefinition();
        if (isAir(definition)) {
            buffer.writeByte(0);
            return;
        }
        VarInts.writeInt(buffer, definition.getRuntimeId());
        buffer.writeShortLE(item.getCount());
        VarInts.writeUnsignedInt(buffer, item.getDamage());
        VarInts.writeInt(buffer, item.getBlockDefinition() == null ? 0 : item.getBlockDefinition().getRuntimeId());
        ByteBuf userDataBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            try {
                LittleEndianByteBufOutputStream stream = new LittleEndianByteBufOutputStream(userDataBuf);
                try {
                    NBTOutputStream nbtStream = new NBTOutputStream(stream);
                    try {
                        if (item.getTag() != null) {
                            stream.writeShort(-1);
                            stream.writeByte(1);
                            nbtStream.writeTag(item.getTag());
                        } else {
                            userDataBuf.writeShortLE(0);
                        }
                        String[] canPlace = item.getCanPlace();
                        stream.writeInt(canPlace.length);
                        for (String aCanPlace : canPlace) {
                            stream.writeUTF(aCanPlace);
                        }
                        String[] canBreak = item.getCanBreak();
                        stream.writeInt(canBreak.length);
                        for (String aCanBreak : canBreak) {
                            stream.writeUTF(aCanBreak);
                        }
                        if ("minecraft:shield".equals(definition.getIdentifier())) {
                            stream.writeLong(item.getBlockingTicks());
                        }
                        VarInts.writeUnsignedInt(buffer, userDataBuf.readableBytes());
                        buffer.writeBytes(userDataBuf);
                        nbtStream.close();
                        stream.close();
                    } finally {
                    }
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        try {
                            stream.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                        throw th2;
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to write item user data", e);
            }
        } finally {
            userDataBuf.release();
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v340.BedrockCodecHelper_v340, org.cloudburstmc.protocol.bedrock.codec.v332.BedrockCodecHelper_v332, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeItem(ByteBuf buffer, ItemData item) {
        Objects.requireNonNull(item, "item is null!");
        ItemDefinition definition = item.getDefinition();
        if (isAir(definition)) {
            buffer.writeByte(0);
            return;
        }
        VarInts.writeInt(buffer, definition.getRuntimeId());
        buffer.writeShortLE(item.getCount());
        VarInts.writeUnsignedInt(buffer, item.getDamage());
        buffer.writeBoolean(item.isUsingNetId());
        if (item.isUsingNetId()) {
            VarInts.writeInt(buffer, item.getNetId());
        }
        VarInts.writeInt(buffer, item.getBlockDefinition() == null ? 0 : item.getBlockDefinition().getRuntimeId());
        ByteBuf userDataBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            try {
                LittleEndianByteBufOutputStream stream = new LittleEndianByteBufOutputStream(userDataBuf);
                try {
                    NBTOutputStream nbtStream = new NBTOutputStream(stream);
                    try {
                        if (item.getTag() != null) {
                            stream.writeShort(-1);
                            stream.writeByte(1);
                            nbtStream.writeTag(item.getTag());
                        } else {
                            userDataBuf.writeShortLE(0);
                        }
                        String[] canPlace = item.getCanPlace();
                        stream.writeInt(canPlace.length);
                        for (String aCanPlace : canPlace) {
                            stream.writeUTF(aCanPlace);
                        }
                        String[] canBreak = item.getCanBreak();
                        stream.writeInt(canBreak.length);
                        for (String aCanBreak : canBreak) {
                            stream.writeUTF(aCanBreak);
                        }
                        if ("minecraft:shield".equals(definition.getIdentifier())) {
                            stream.writeLong(item.getBlockingTicks());
                        }
                        VarInts.writeUnsignedInt(buffer, userDataBuf.readableBytes());
                        buffer.writeBytes(userDataBuf);
                        nbtStream.close();
                        stream.close();
                    } finally {
                    }
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        try {
                            stream.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                        throw th2;
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to write item user data", e);
            }
        } finally {
            userDataBuf.release();
        }
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeNetItem(ByteBuf buffer, ItemData item) {
        writeItem(buffer, item);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public boolean readInventoryActions(ByteBuf buffer, List<InventoryActionData> actions) {
        readArray(buffer, actions, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431$$ExternalSyntheticLambda2
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return BedrockCodecHelper_v431.this.m2086x6725a4c0((ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readInventoryActions$0$org-cloudburstmc-protocol-bedrock-codec-v431-BedrockCodecHelper_v431, reason: not valid java name */
    public /* synthetic */ InventoryActionData m2086x6725a4c0(ByteBuf buf, BedrockCodecHelper helper) {
        InventorySource source = readSource(buf);
        int slot = VarInts.readUnsignedInt(buf);
        ItemData fromItem = helper.readItem(buf);
        ItemData toItem = helper.readItem(buf);
        return new InventoryActionData(source, slot, fromItem, toItem);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeInventoryActions(ByteBuf buffer, List<InventoryActionData> actions, boolean hasNetworkIds) {
        writeArray(buffer, actions, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431$$ExternalSyntheticLambda1
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                BedrockCodecHelper_v431.this.m2087xd399810((ByteBuf) obj, (BedrockCodecHelper) obj2, (InventoryActionData) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$writeInventoryActions$1$org-cloudburstmc-protocol-bedrock-codec-v431-BedrockCodecHelper_v431, reason: not valid java name */
    public /* synthetic */ void m2087xd399810(ByteBuf buf, BedrockCodecHelper helper, InventoryActionData action) {
        writeSource(buf, action.getSource());
        VarInts.writeUnsignedInt(buf, action.getSlot());
        helper.writeItem(buf, action.getFromItem());
        helper.writeItem(buf, action.getToItem());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public ItemStackRequestAction readRequestActionData(ByteBuf byteBuf, ItemStackRequestActionType type) {
        if (type == ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED) {
            return new CraftResultsDeprecatedAction((ItemData[]) readArray(byteBuf, new ItemData[0], new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return BedrockCodecHelper_v431.this.readItemInstance((ByteBuf) obj);
                }
            }), byteBuf.readUnsignedByte());
        }
        return super.readRequestActionData(byteBuf, type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.BedrockCodecHelper_v428, org.cloudburstmc.protocol.bedrock.codec.v422.BedrockCodecHelper_v422, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407
    public void writeRequestActionData(ByteBuf byteBuf, ItemStackRequestAction action) {
        if (action.getType() == ItemStackRequestActionType.CRAFT_RESULTS_DEPRECATED) {
            writeArray(byteBuf, ((CraftResultsDeprecatedAction) action).getResultItems(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    BedrockCodecHelper_v431.this.writeItemInstance((ByteBuf) obj, (ItemData) obj2);
                }
            });
            byteBuf.writeByte(((CraftResultsDeprecatedAction) action).getTimesCrafted());
        } else {
            super.writeRequestActionData(byteBuf, action);
        }
    }
}
