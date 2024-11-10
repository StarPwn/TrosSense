package org.cloudburstmc.protocol.bedrock.codec.v448.serializer;

import io.netty.buffer.ByteBuf;
import java.util.EnumSet;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.codec.v388.serializer.AvailableCommandsSerializer_v388;
import org.cloudburstmc.protocol.bedrock.data.command.CommandData;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParam;
import org.cloudburstmc.protocol.common.util.TypeMap;

/* loaded from: classes5.dex */
public class AvailableCommandsSerializer_v448 extends AvailableCommandsSerializer_v388 {
    public AvailableCommandsSerializer_v448(TypeMap<CommandParam> paramTypeMap) {
        super(paramTypeMap);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291
    public void writeFlags(ByteBuf buffer, Set<CommandData.Flag> flags) {
        int flagBits = 0;
        for (CommandData.Flag flag : flags) {
            flagBits |= 1 << flag.ordinal();
        }
        buffer.writeShortLE(flagBits);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v291.serializer.AvailableCommandsSerializer_v291
    public Set<CommandData.Flag> readFlags(ByteBuf buffer) {
        int flagBits = buffer.readUnsignedShortLE();
        EnumSet<CommandData.Flag> flags = EnumSet.noneOf(CommandData.Flag.class);
        for (CommandData.Flag flag : CommandData.Flag.values()) {
            if (((1 << flag.ordinal()) & flagBits) != 0) {
                flags.add(flag);
            }
            flagBits |= 1 << flag.ordinal();
        }
        return flags;
    }
}
