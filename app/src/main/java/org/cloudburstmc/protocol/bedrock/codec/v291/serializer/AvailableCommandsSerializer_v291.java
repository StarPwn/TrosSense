package org.cloudburstmc.protocol.bedrock.codec.v291.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumConstraint;
import org.cloudburstmc.protocol.bedrock.data.command.CommandEnumData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOverloadData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamOption;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.SequencedHashSet;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class AvailableCommandsSerializer_v291 implements BedrockPacketSerializer<AvailableCommandsPacket> {
    protected static final int ARG_FLAG_ENUM = 2097152;
    protected static final int ARG_FLAG_POSTFIX = 16777216;
    protected static final int ARG_FLAG_SOFT_ENUM = 67108864;
    protected static final int ARG_FLAG_VALID = 1048576;
    private final TypeMap<CommandParam> paramTypeMap;
    protected static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) AvailableCommandsSerializer_v291.class);
    protected static final CommandPermission[] PERMISSIONS = CommandPermission.values();
    protected static final ToIntFunction<ByteBuf> READ_BYTE = new ToIntFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda8
        @Override // java.util.function.ToIntFunction
        public final int applyAsInt(Object obj) {
            short readUnsignedByte;
            readUnsignedByte = ((ByteBuf) obj).readUnsignedByte();
            return readUnsignedByte;
        }
    };
    protected static final ToIntFunction<ByteBuf> READ_SHORT = new ToIntFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda9
        @Override // java.util.function.ToIntFunction
        public final int applyAsInt(Object obj) {
            int readUnsignedShortLE;
            readUnsignedShortLE = ((ByteBuf) obj).readUnsignedShortLE();
            return readUnsignedShortLE;
        }
    };
    protected static final ToIntFunction<ByteBuf> READ_INT = new ToIntFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda10
        @Override // java.util.function.ToIntFunction
        public final int applyAsInt(Object obj) {
            int readIntLE;
            readIntLE = ((ByteBuf) obj).readIntLE();
            return readIntLE;
        }
    };
    protected static final ObjIntConsumer<ByteBuf> WRITE_BYTE = new ObjIntConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda11
        @Override // java.util.function.ObjIntConsumer
        public final void accept(Object obj, int i) {
            ((ByteBuf) obj).writeByte(i);
        }
    };
    protected static final ObjIntConsumer<ByteBuf> WRITE_SHORT = new ObjIntConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda12
        @Override // java.util.function.ObjIntConsumer
        public final void accept(Object obj, int i) {
            ((ByteBuf) obj).writeShortLE(i);
        }
    };
    protected static final ObjIntConsumer<ByteBuf> WRITE_INT = new ObjIntConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda13
        @Override // java.util.function.ObjIntConsumer
        public final void accept(Object obj, int i) {
            ((ByteBuf) obj).writeIntLE(i);
        }
    };
    protected static final CommandData.Flag[] FLAGS = CommandData.Flag.values();
    protected static final CommandParamOption[] OPTIONS = CommandParamOption.values();

    public AvailableCommandsSerializer_v291(TypeMap<CommandParam> paramTypeMap) {
        this.paramTypeMap = paramTypeMap;
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(final ByteBuf buffer, final BedrockCodecHelper helper, AvailableCommandsPacket packet) {
        Iterator<CommandData> it2;
        SequencedHashSet<String> enumValues = new SequencedHashSet<>();
        final SequencedHashSet<String> postFixes = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> enums = new SequencedHashSet<>();
        final SequencedHashSet<CommandEnumData> softEnums = new SequencedHashSet<>();
        Iterator<CommandData> it3 = packet.getCommands().iterator();
        while (it3.hasNext()) {
            CommandData data = it3.next();
            if (data.getAliases() != null) {
                enumValues.addAll(data.getAliases().getValues().keySet());
                enums.add(data.getAliases());
            }
            for (CommandOverloadData overload : data.getOverloads()) {
                CommandParamData[] overloads = overload.getOverloads();
                int length = overloads.length;
                int i = 0;
                while (i < length) {
                    CommandParamData parameter = overloads[i];
                    CommandEnumData commandEnumData = parameter.getEnumData();
                    if (commandEnumData == null) {
                        it2 = it3;
                    } else if (commandEnumData.isSoft()) {
                        softEnums.add(commandEnumData);
                        it2 = it3;
                    } else {
                        it2 = it3;
                        enumValues.addAll(commandEnumData.getValues().keySet());
                        enums.add(commandEnumData);
                    }
                    String postfix = parameter.getPostfix();
                    if (postfix != null) {
                        postFixes.add(postfix);
                    }
                    i++;
                    it3 = it2;
                }
            }
        }
        helper.getClass();
        helper.writeArray(buffer, enumValues, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        helper.getClass();
        helper.writeArray(buffer, postFixes, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda14(helper));
        writeEnums(buffer, helper, enumValues, enums);
        helper.writeArray(buffer, packet.getCommands(), new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda15
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v291.this.m2066xec6dedf7(buffer, helper, enums, softEnums, postFixes, (ByteBuf) obj, (CommandData) obj2);
            }
        });
        helper.getClass();
        helper.writeArray(buffer, softEnums, new AvailableCommandsSerializer_v291$$ExternalSyntheticLambda1(helper));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$serialize$0$org-cloudburstmc-protocol-bedrock-codec-v291-serializer-AvailableCommandsSerializer_v291, reason: not valid java name */
    public /* synthetic */ void m2066xec6dedf7(ByteBuf buffer, BedrockCodecHelper helper, SequencedHashSet enums, SequencedHashSet softEnums, SequencedHashSet postFixes, ByteBuf buf, CommandData command) {
        writeCommand(buffer, helper, command, enums, softEnums, postFixes);
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
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
        helper.readArray(buffer, packet.getCommands(), new BiFunction() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda4
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return AvailableCommandsSerializer_v291.this.m2065x4d3c25d7(enums, postFixes, softEnumParameters, (ByteBuf) obj, (BedrockCodecHelper) obj2);
            }
        });
        helper.readArray(buffer, softEnums, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CommandEnumData readCommandEnum;
                readCommandEnum = BedrockCodecHelper.this.readCommandEnum(buffer, true);
                return readCommandEnum;
            }
        });
        softEnumParameters.forEach(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((Consumer) obj).accept(SequencedHashSet.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeEnums(final ByteBuf buffer, final BedrockCodecHelper helper, final List<String> values, List<CommandEnumData> enums) {
        final ObjIntConsumer<ByteBuf> indexWriter;
        int valuesSize = values.size();
        if (valuesSize <= 256) {
            indexWriter = WRITE_BYTE;
        } else if (valuesSize <= 65536) {
            indexWriter = WRITE_SHORT;
        } else {
            indexWriter = WRITE_INT;
        }
        helper.writeArray(buffer, enums, new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda7
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AvailableCommandsSerializer_v291.lambda$writeEnums$4(BedrockCodecHelper.this, buffer, values, indexWriter, (ByteBuf) obj, (CommandEnumData) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$writeEnums$4(BedrockCodecHelper helper, ByteBuf buffer, List values, ObjIntConsumer indexWriter, ByteBuf buf, CommandEnumData commandEnum) {
        helper.writeString(buf, commandEnum.getName());
        VarInts.writeUnsignedInt(buffer, commandEnum.getValues().size());
        for (String value : commandEnum.getValues().keySet()) {
            int index = values.indexOf(value);
            Preconditions.checkArgument(index > -1, "Invalid enum value detected: " + value);
            indexWriter.accept(buf, index);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readEnums(final ByteBuf buffer, final BedrockCodecHelper helper, final List<String> values, List<CommandEnumData> enums) {
        final ToIntFunction<ByteBuf> indexReader;
        int valuesSize = values.size();
        if (valuesSize <= 256) {
            indexReader = READ_BYTE;
        } else if (valuesSize <= 65536) {
            indexReader = READ_SHORT;
        } else {
            indexReader = READ_INT;
        }
        helper.readArray(buffer, enums, new Function() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return AvailableCommandsSerializer_v291.lambda$readEnums$5(BedrockCodecHelper.this, buffer, values, indexReader, (ByteBuf) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ CommandEnumData lambda$readEnums$5(BedrockCodecHelper helper, ByteBuf buffer, List values, ToIntFunction indexReader, ByteBuf buf) {
        String name = helper.readString(buf);
        int length = VarInts.readUnsignedInt(buffer);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < length; i++) {
            linkedHashMap.put(values.get(indexReader.applyAsInt(buf)), EnumSet.noneOf(CommandEnumConstraint.class));
        }
        return new CommandEnumData(name, linkedHashMap, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeCommand(ByteBuf buffer, BedrockCodecHelper helper, CommandData commandData, List<CommandEnumData> enums, List<CommandEnumData> softEnums, List<String> postFixes) {
        helper.writeString(buffer, commandData.getName());
        helper.writeString(buffer, commandData.getDescription());
        writeFlags(buffer, commandData.getFlags());
        CommandPermission permission = commandData.getPermission() == null ? CommandPermission.ANY : commandData.getPermission();
        buffer.writeByte(permission.ordinal());
        CommandEnumData aliases = commandData.getAliases();
        buffer.writeIntLE(aliases == null ? -1 : enums.indexOf(aliases));
        CommandOverloadData[] overloads = commandData.getOverloads();
        VarInts.writeUnsignedInt(buffer, overloads.length);
        int length = overloads.length;
        int i = 0;
        while (i < length) {
            CommandOverloadData overload = overloads[i];
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

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: readCommand, reason: merged with bridge method [inline-methods] */
    public CommandData m2065x4d3c25d7(ByteBuf buffer, BedrockCodecHelper helper, List<CommandEnumData> enums, List<String> postfixes, Set<Consumer<List<CommandEnumData>>> softEnumParameters) {
        String name = helper.readString(buffer);
        String description = helper.readString(buffer);
        Set<CommandData.Flag> flags = readFlags(buffer);
        CommandPermission permissions = PERMISSIONS[buffer.readUnsignedByte()];
        int aliasIndex = buffer.readIntLE();
        CommandEnumData aliases = aliasIndex == -1 ? null : enums.get(aliasIndex);
        CommandOverloadData[] overloads = new CommandOverloadData[VarInts.readUnsignedInt(buffer)];
        for (int i = 0; i < overloads.length; i++) {
            overloads[i] = new CommandOverloadData(false, new CommandParamData[VarInts.readUnsignedInt(buffer)]);
            for (int i2 = 0; i2 < overloads[i].getOverloads().length; i2++) {
                overloads[i].getOverloads()[i2] = readParameter(buffer, helper, enums, postfixes, softEnumParameters);
            }
        }
        return new CommandData(name, description, flags, permissions, aliases, Collections.emptyList(), overloads);
    }

    protected void writeFlags(ByteBuf buffer, Set<CommandData.Flag> flags) {
        int flagBits = 0;
        for (CommandData.Flag flag : flags) {
            flagBits |= 1 << flag.ordinal();
        }
        buffer.writeByte(flagBits);
    }

    protected Set<CommandData.Flag> readFlags(ByteBuf buffer) {
        int flagBits = buffer.readUnsignedByte();
        EnumSet<CommandData.Flag> flags = EnumSet.noneOf(CommandData.Flag.class);
        for (CommandData.Flag flag : CommandData.Flag.values()) {
            if (((1 << flag.ordinal()) & flagBits) != 0) {
                flags.add(flag);
            }
            flagBits |= 1 << flag.ordinal();
        }
        return flags;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeParameter(ByteBuf buffer, BedrockCodecHelper helper, CommandParamData param, List<CommandEnumData> enums, List<CommandEnumData> softEnums, List<String> postfixes) {
        int symbol;
        helper.writeString(buffer, param.getName());
        if (param.getPostfix() != null) {
            symbol = postfixes.indexOf(param.getPostfix()) | 16777216;
        } else if (param.getEnumData() != null) {
            if (param.getEnumData().isSoft()) {
                symbol = softEnums.indexOf(param.getEnumData()) | 67108864 | 1048576;
            } else {
                symbol = enums.indexOf(param.getEnumData()) | 2097152 | 1048576;
            }
        } else if (param.getType() != null) {
            symbol = this.paramTypeMap.getId(param.getType()) | 1048576;
        } else {
            throw new IllegalStateException("No param type specified: " + param);
        }
        buffer.writeIntLE(symbol);
        buffer.writeBoolean(param.isOptional());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CommandParamData readParameter(ByteBuf buffer, BedrockCodecHelper helper, List<CommandEnumData> enums, List<String> postfixes, Set<Consumer<List<CommandEnumData>>> softEnumParameters) {
        final CommandParamData param = new CommandParamData();
        param.setName(helper.readString(buffer));
        final int symbol = buffer.readIntLE();
        if ((16777216 & symbol) != 0) {
            param.setPostfix(postfixes.get((-16777217) & symbol));
        } else if ((1048576 & symbol) != 0) {
            if ((67108864 & symbol) != 0) {
                softEnumParameters.add(new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        CommandParamData.this.setEnumData((CommandEnumData) ((List) obj).get((-68157441) & symbol));
                    }
                });
            } else if ((2097152 & symbol) != 0) {
                param.setEnumData(enums.get((-3145729) & symbol));
            } else {
                int parameterTypeId = (-1048577) & symbol;
                CommandParam type = this.paramTypeMap.getTypeUnsafe(parameterTypeId);
                if (type == null) {
                    throw new IllegalStateException("Invalid parameter type: " + parameterTypeId + ", Symbol: " + symbol);
                }
                param.setType(type);
            }
        } else {
            throw new IllegalStateException("No param type specified: " + param.getName());
        }
        param.setOptional(buffer.readBoolean());
        return param;
    }
}
