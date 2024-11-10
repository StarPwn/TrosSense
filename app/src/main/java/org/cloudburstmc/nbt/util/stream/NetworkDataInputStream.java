package org.cloudburstmc.nbt.util.stream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.cloudburstmc.nbt.util.VarInts;

/* loaded from: classes5.dex */
public class NetworkDataInputStream extends LittleEndianDataInputStream {
    public NetworkDataInputStream(InputStream stream) {
        super(stream);
    }

    public NetworkDataInputStream(DataInputStream stream) {
        super(stream);
    }

    @Override // org.cloudburstmc.nbt.util.stream.LittleEndianDataInputStream, java.io.DataInput
    public int readInt() throws IOException {
        return VarInts.readInt(this.stream);
    }

    @Override // org.cloudburstmc.nbt.util.stream.LittleEndianDataInputStream, java.io.DataInput
    public long readLong() throws IOException {
        return VarInts.readLong(this.stream);
    }

    @Override // org.cloudburstmc.nbt.util.stream.LittleEndianDataInputStream, java.io.DataInput
    public String readUTF() throws IOException {
        int length = VarInts.readUnsignedInt(this.stream);
        byte[] bytes = new byte[length];
        readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
