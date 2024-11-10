package org.cloudburstmc.protocol.bedrock.codec.v594.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongObjectPair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
import org.cloudburstmc.protocol.bedrock.codec.v448.serializer.AvailableCommandsSerializer_v448;
import org.cloudburstmc.protocol.bedrock.data.command.ChainedSubCommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumConstraint;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOverloadData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.common.util.LongKeys;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.SequencedHashSet;
import org.cloudburstmc.protocol.common.util.TriConsumer;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AvailableCommandsSerializer_v594 extends AvailableCommandsSerializer_v448 {
    public AvailableCommandsSerializer_v594(TypeMap<CommandParam> paramTypeMap) {
        super(paramTypeMap);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, AvailableCommandsPacket packet) {
        int i;
        CommandParamData[] commandParamDataArr;
        final SequencedHashSet<String> enumValues = new SequencedHashSet<>();
        final SequencedHashSet<String> subCommandValues = new SequencedHashSet<>();
        final SequencedHashSet<String> postFixes = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> enums = new SequencedHashSet<>();
        final SequencedHashSet<ChainedSubCommandData> subCommandData = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> softEnums = new SequencedHashSet<>();
        final SequencedHashSet<LongObjectPair<Set<CommandEnumConstraint>>> enumConstraints = new SequencedHashSet<>();
        Iterator<CommandData> it2 = packet.getCommands().iterator();
        while (it2.hasNext()) {
            CommandData data = it2.next();
            if (data.getAliases() != null) {
                enumValues.addAll(data.getAliases().getValues().keySet());
                enums.add(data.getAliases());
            }
            for (ChainedSubCommandData subcommand : data.getSubcommands()) {
                if (!subCommandData.contains(subcommand)) {
                    subCommandData.add(subcommand);
                    for (ChainedSubCommandData.Value value : subcommand.getValues()) {
                        Iterator<CommandData> it3 = it2;
                        if (subCommandValues.contains(value.getFirst())) {
                            subCommandValues.add(value.getFirst());
                        }
                        if (subCommandValues.contains(value.getSecond())) {
                            subCommandValues.add(value.getSecond());
                        }
                        it2 = it3;
                    }
                }
            }
            Iterator<CommandData> it4 = it2;
            CommandOverloadData[] overloads = data.getOverloads();
            int length = overloads.length;
            int i2 = 0;
            while (i2 < length) {
                CommandOverloadData overload = overloads[i2];
                CommandParamData[] overloads2 = overload.getOverloads();
                CommandOverloadData[] commandOverloadDataArr = overloads;
                int length2 = overloads2.length;
                CommandData data2 = data;
                int i3 = 0;
                while (i3 < length2) {
                    CommandParamData parameter = overloads2[i3];
                    int i4 = length2;
                    CommandEnumData commandEnumData = parameter.getEnumData();
                    if (commandEnumData == null) {
                        i = length;
                        commandParamDataArr = overloads2;
                    } else if (commandEnumData.isSoft()) {
                        softEnums.add(commandEnumData);
                        i = length;
                        commandParamDataArr = overloads2;
                    } else {
                        enums.add(commandEnumData);
                        i = length;
                        final int enumIndex = enums.indexOf(commandEnumData);
                        commandParamDataArr = overloads2;
                        commandEnumData.getValues().forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda11
                            @Override // java.util.function.BiConsumer
                            public final void accept(Object obj, Object obj2) {
                                AvailableCommandsSerializer_v594.lambda$serialize$0(SequencedHashSet.this, enumConstraints, enumIndex, (String) obj, (Set) obj2);
                            }
                        });
                    }
                    String postfix = parameter.getPostfix();
                    if (postfix != null) {
                        postFixes.add(postfix);
                    }
                    i3++;
                    length2 = i4;
                    length = i;
                    overloads2 = commandParamDataArr;
                }
                i2++;
                overloads = commandOverloadDataArr;
                data = data2;
            }
            it2 = it4;
        }
        helper.getClass();
        helper.writeArray(buffer, enumValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        helper.getClass();
        helper.writeArray(buffer, subCommandValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        helper.getClass();
        helper.writeArray(buffer, postFixes, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        writeEnums(buffer, helper, enumValues, enums);
        helper.writeArray(buffer, subCommandData, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v594.this.m2096x54af8ea2(buffer, helper, subCommandValues, (ByteBuf) obj, (ChainedSubCommandData) obj2);
            }
        });
        helper.writeArray(buffer, packet.getCommands(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v594.this.m2097x8ca069c1(buffer, helper, enums, softEnums, postFixes, subCommandData, (ByteBuf) obj, (CommandData) obj2);
            }
        });
        helper.getClass();
        helper.writeArray(buffer, softEnums, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda1(helper));
        helper.writeArray(buffer, enumConstraints, new TriConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda2
            @Override // org.cloudburstmc.protocol.common.util.TriConsumer
            public final void accept(Object obj, Object obj2, Object obj3) {
                AvailableCommandsSerializer_v594.this.writeEnumConstraint((ByteBuf) obj, (BedrockCodecHelper) obj2, (LongObjectPair) obj3);
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
    /* renamed from: lambda$serialize$1$org-cloudburstmc-protocol-bedrock-codec-v594-serializer-AvailableCommandsSerializer_v594, reason: not valid java name */
    public /* synthetic */ void m2096x54af8ea2(ByteBuf buffer, BedrockCodecHelper helper, SequencedHashSet subCommandValues, ByteBuf buf, ChainedSubCommandData value) {
        writeSubCommand(buffer, helper, subCommandValues, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$2$org-cloudburstmc-protocol-bedrock-codec-v594-serializer-AvailableCommandsSerializer_v594, reason: not valid java name */
    public /* synthetic */ void m2097x8ca069c1(ByteBuf buffer, BedrockCodecHelper helper, SequencedHashSet enums, SequencedHashSet softEnums, SequencedHashSet postFixes, SequencedHashSet subCommandData, ByteBuf buf, CommandData command) {
        writeCommand(buffer, helper, command, enums, softEnums, postFixes, subCommandData);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(final ByteBuf buffer, final BedrockCodecHelper helper, AvailableCommandsPacket packet) {
        SequencedHashSet<String> enumValues = new SequencedHashSet<>();
        final SequencedHashSet<String> subCommandValues = new SequencedHashSet<>();
        final SequencedHashSet<String> postFixes = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> enums = new SequencedHashSet<>();
        final SequencedHashSet<ChainedSubCommandData> subCommandData = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> softEnums = new SequencedHashSet<>();
        final Set<Consumer<List<CommandEnumData>>> softEnumParameters = new HashSet<>();
        helper.getClass();
        helper.readArray(buffer, enumValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
        helper.getClass();
        helper.readArray(buffer, subCommandValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
        helper.getClass();
        helper.readArray(buffer, postFixes, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda3(helper));
        readEnums(buffer, helper, enumValues, enums);
        helper.readArray(buffer, subCommandData, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda3
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return AvailableCommandsSerializer_v594.this.m2094xed6ea1a1(subCommandValues, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, packet.getCommands(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return AvailableCommandsSerializer_v594.this.m2095x255f7cc0(enums, postFixes, softEnumParameters, subCommandData, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, softEnums, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CommandEnumData readCommandEnum;
                readCommandEnum = BedrockCodecHelper.this.readCommandEnum(buffer, true);
                return readCommandEnum;
            }
        });
        readConstraints(buffer, helper, enums, enumValues);
        softEnumParameters.forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Consumer) obj).accept(SequencedHashSet.this);
            }
        });
    }

    protected void writeCommand(ByteBuf buffer, BedrockCodecHelper helper, CommandData commandData, List<CommandEnumData> enums, List<CommandEnumData> softEnums, List<String> postFixes, final List<ChainedSubCommandData> subCommands) {
        helper.writeString(buffer, commandData.getName());
        helper.writeString(buffer, commandData.getDescription());
        writeFlags(buffer, commandData.getFlags());
        CommandPermission permission = commandData.getPermission() == null ? CommandPermission.ANY : commandData.getPermission();
        buffer.writeByte(permission.ordinal());
        CommandEnumData aliases = commandData.getAliases();
        buffer.writeIntLE(aliases == null ? -1 : enums.indexOf(aliases));
        helper.writeArray(buffer, commandData.getSubcommands(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v594.lambda$writeCommand$7(subCommands, (ByteBuf) obj, (ChainedSubCommandData) obj2);
            }
        });
        CommandOverloadData[] overloads = commandData.getOverloads();
        VarInts.writeUnsignedInt(buffer, overloads.length);
        int length = overloads.length;
        int i = 0;
        while (i < length) {
            CommandOverloadData overload = overloads[i];
            buffer.writeBoolean(overload.isChaining());
            VarInts.writeUnsignedInt(buffer, overload.getOverloads().length);
            CommandParamData[] overloads2 = overload.getOverloads();
            int length2 = overloads2.length;
            int i2 = 0;
            while (i2 < length2) {
                CommandParamData param = overloads2[i2];
                writeParameter(buffer, helper, param, enums, softEnums, postFixes);
                i2++;
                length2 = length2;
                overloads2 = overloads2;
                i = i;
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeCommand$7(List subCommands, ByteBuf buf, ChainedSubCommandData subcommand) {
        int index = subCommands.indexOf(subcommand);
        Preconditions.checkArgument(index > -1, "Invalid subcommand index: " + subcommand);
        buf.writeShortLE(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: readCommand, reason: merged with bridge method [inline-methods] */
    public CommandData m2095x255f7cc0(ByteBuf buffer, BedrockCodecHelper helper, List<CommandEnumData> enums, List<String> postfixes, Set<Consumer<List<CommandEnumData>>> softEnumParameters, final List<ChainedSubCommandData> subCommandsList) {
        String name = helper.readString(buffer);
        String description = helper.readString(buffer);
        Set<CommandData.Flag> flags = readFlags(buffer);
        CommandPermission permissions = PERMISSIONS[buffer.readUnsignedByte()];
        int aliasIndex = buffer.readIntLE();
        CommandEnumData aliases = aliasIndex == -1 ? null : enums.get(aliasIndex);
        ObjectArrayList objectArrayList = new ObjectArrayList();
        helper.readArray(buffer, objectArrayList, new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda7
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return AvailableCommandsSerializer_v594.lambda$readCommand$8(subCommandsList, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        CommandOverloadData[] overloads = new CommandOverloadData[VarInts.readUnsignedInt(buffer)];
        for (int i = 0; i < overloads.length; i++) {
            boolean chaining = buffer.readBoolean();
            CommandParamData[] params = new CommandParamData[VarInts.readUnsignedInt(buffer)];
            overloads[i] = new CommandOverloadData(chaining, params);
            for (int i2 = 0; i2 < params.length; i2++) {
                params[i2] = readParameter(buffer, helper, enums, postfixes, softEnumParameters);
            }
        }
        return new CommandData(name, description, flags, permissions, aliases, objectArrayList, overloads);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ChainedSubCommandData lambda$readCommand$8(List subCommandsList, ByteBuf buf, BedrockCodecHelper help) {
        int index = buf.readUnsignedShortLE();
        return (ChainedSubCommandData) subCommandsList.get(index);
    }

    protected void writeSubCommand(ByteBuf buffer, BedrockCodecHelper helper, final List<String> values, ChainedSubCommandData data) {
        helper.writeString(buffer, data.getName());
        helper.writeArray(buffer, data.getValues(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v594.lambda$writeSubCommand$9(values, (ByteBuf) obj, (ChainedSubCommandData.Value) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeSubCommand$9(List values, ByteBuf buf, ChainedSubCommandData.Value val) {
        int first = values.indexOf(val.getFirst());
        Preconditions.checkArgument(first > -1, "Invalid enum value detected: " + val.getFirst());
        int second = values.indexOf(val.getSecond());
        Preconditions.checkArgument(second > -1, "Invalid enum value detected: " + val.getSecond());
        buf.writeShortLE(first);
        buf.writeShortLE(second);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: readSubCommand, reason: merged with bridge method [inline-methods] */
    public ChainedSubCommandData m2094xed6ea1a1(ByteBuf buffer, BedrockCodecHelper helper, final List<String> values) {
        String name = helper.readString(buffer);
        ChainedSubCommandData data = new ChainedSubCommandData(name);
        helper.readArray(buffer, data.getValues(), new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v594.serializer.AvailableCommandsSerializer_v594$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return AvailableCommandsSerializer_v594.lambda$readSubCommand$10(values, (ByteBuf) obj);
            }
        });
        return data;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ChainedSubCommandData.Value lambda$readSubCommand$10(List values, ByteBuf buf) {
        int first = buf.readUnsignedShortLE();
        int second = buf.readUnsignedShortLE();
        return new ChainedSubCommandData.Value((String) values.get(first), (String) values.get(second));
    }
}
