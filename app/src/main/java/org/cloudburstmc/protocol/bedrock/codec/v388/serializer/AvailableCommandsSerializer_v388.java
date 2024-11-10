package org.cloudburstmc.protocol.bedrock.codec.v388.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongObjectPair;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda1;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14;
import org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3;
import org.cloudburstmc.protocol.bedrock.codec.v340.serializer.AvailableCommandsSerializer_v340;
import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumConstraint;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOverloadData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.common.util.LongKeys;
import org.cloudburstmc.protocol.common.util.SequencedHashSet;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AvailableCommandsSerializer_v388 extends AvailableCommandsSerializer_v340 {
    private static final CommandEnumConstraint[] CONSTRAINTS = CommandEnumConstraint.values();

    public AvailableCommandsSerializer_v388(TypeMap<CommandParam> paramTypeMap) {
        super(paramTypeMap);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, AvailableCommandsPacket packet) {
        CommandOverloadData[] commandOverloadDataArr;
        int i;
        final SequencedHashSet<String> enumValues = new SequencedHashSet<>();
        final SequencedHashSet<String> postFixes = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> enums = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> softEnums = new SequencedHashSet<>();
        final SequencedHashSet<LongObjectPair<Set<CommandEnumConstraint>>> enumConstraints = new SequencedHashSet<>();
        Iterator<CommandData> it2 = packet.getCommands().iterator();
        while (it2.hasNext()) {
            CommandData data = it2.next();
            if (data.getAliases() != null) {
                enumValues.addAll(data.getAliases().getValues().keySet());
                enums.add(data.getAliases());
            }
            CommandOverloadData[] overloads = data.getOverloads();
            int length = overloads.length;
            int i2 = 0;
            while (i2 < length) {
                CommandOverloadData overload = overloads[i2];
                CommandParamData[] overloads2 = overload.getOverloads();
                int length2 = overloads2.length;
                Iterator<CommandData> it3 = it2;
                int i3 = 0;
                while (i3 < length2) {
                    CommandParamData parameter = overloads2[i3];
                    CommandData data2 = data;
                    CommandEnumData commandEnumData = parameter.getEnumData();
                    if (commandEnumData == null) {
                        commandOverloadDataArr = overloads;
                        i = length;
                    } else if (commandEnumData.isSoft()) {
                        softEnums.add(commandEnumData);
                        commandOverloadDataArr = overloads;
                        i = length;
                    } else {
                        enums.add(commandEnumData);
                        commandOverloadDataArr = overloads;
                        final int enumIndex = enums.indexOf(commandEnumData);
                        i = length;
                        commandEnumData.getValues().forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda2
                            @Override // java.util.function.BiConsumer
                            public final void accept(Object obj, Object obj2) {
                                AvailableCommandsSerializer_v388.lambda$serialize$0(SequencedHashSet.this, enumConstraints, enumIndex, (String) obj, (Set) obj2);
                            }
                        });
                    }
                    String postfix = parameter.getPostfix();
                    if (postfix != null) {
                        postFixes.add(postfix);
                    }
                    i3++;
                    data = data2;
                    overloads = commandOverloadDataArr;
                    length = i;
                }
                i2++;
                it2 = it3;
            }
        }
        helper.getClass();
        helper.writeArray(buffer, enumValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        helper.getClass();
        helper.writeArray(buffer, postFixes, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        writeEnums(buffer, helper, enumValues, enums);
        helper.writeArray(buffer, packet.getCommands(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v388.this.m2068x1ac6b6e8(buffer, helper, enums, softEnums, postFixes, (ByteBuf) obj, (CommandData) obj2);
            }
        });
        helper.getClass();
        helper.writeArray(buffer, softEnums, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda1(helper));
        helper.writeArray(buffer, enumConstraints, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda4
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                AvailableCommandsSerializer_v388.this.writeEnumConstraint((ByteBuf) obj, (BedrockCodecHelper) obj2, (LongObjectPair) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$serialize$0(SequencedHashSet enumValues, SequencedHashSet enumConstraints, int enumIndex, String key, Set constraints) {
        enumValues.add(key);
        if (!constraints.isEmpty()) {
            int valueIndex = enumValues.indexOf(key);
            enumConstraints.add(LongObjectPair.of(LongKeys.key(valueIndex, enumIndex), constraints));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$1$org-cloudburstmc-protocol-bedrock-codec-v388-serializer-AvailableCommandsSerializer_v388, reason: not valid java name */
    public /* synthetic */ void m2068x1ac6b6e8(ByteBuf buffer, BedrockCodecHelper helper, SequencedHashSet enums, SequencedHashSet softEnums, SequencedHashSet postFixes, ByteBuf buf, CommandData command) {
        writeCommand(buffer, helper, command, enums, softEnums, postFixes);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(final ByteBuf buffer, final BedrockCodecHelper helper, AvailableCommandsPacket packet) {
        SequencedHashSet<String> enumValues = new SequencedHashSet<>();
        final SequencedHashSet<String> postFixes = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> enums = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> softEnums = new SequencedHashSet<>();
        final Set<Consumer<List<CommandEnumData>>> softEnumParameters = new HashSet<>();
        helper.getClass();
        helper.readArray(buffer, enumValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
        helper.getClass();
        helper.readArray(buffer, postFixes, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
        readEnums(buffer, helper, enumValues, enums);
        helper.readArray(buffer, packet.getCommands(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return AvailableCommandsSerializer_v388.this.m2067x7b94eec8(enums, postFixes, softEnumParameters, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, softEnums, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda6
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CommandEnumData readCommandEnum;
                readCommandEnum = BedrockCodecHelper.this.readCommandEnum(buffer, true);
                return readCommandEnum;
            }
        });
        readConstraints(buffer, helper, enums, enumValues);
        softEnumParameters.forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Consumer) obj).accept(SequencedHashSet.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$deserialize$2$org-cloudburstmc-protocol-bedrock-codec-v388-serializer-AvailableCommandsSerializer_v388, reason: not valid java name */
    public /* synthetic */ CommandData m2067x7b94eec8(SequencedHashSet enums, SequencedHashSet postFixes, Set softEnumParameters, ByteBuf buf, BedrockCodecHelper aHelper) {
        return m2065x4d3c25d7(buf, aHelper, enums, postFixes, softEnumParameters);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEnumConstraint(ByteBuf buffer, BedrockCodecHelper helper, LongObjectPair<Set<CommandEnumConstraint>> pair) {
        buffer.writeIntLE(LongKeys.high(pair.keyLong()));
        buffer.writeIntLE(LongKeys.low(pair.keyLong()));
        helper.writeArray(buffer, pair.value(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((ByteBuf) obj).writeByte(((CommandEnumConstraint) obj2).ordinal());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readConstraints(ByteBuf buffer, BedrockCodecHelper helper, List<CommandEnumData> enums, List<String> enumValues) {
        int count = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < count; i++) {
            String key = enumValues.get(buffer.readIntLE());
            CommandEnumData enumData = enums.get(buffer.readIntLE());
            Set<CommandEnumConstraint> constraints = enumData.getValues().get(key);
            helper.readArray(buffer, constraints, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return AvailableCommandsSerializer_v388.lambda$readConstraints$6((ByteBuf) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ CommandEnumConstraint lambda$readConstraints$6(ByteBuf buf) {
        return CONSTRAINTS[buf.readUnsignedByte()];
    }
}
