package org.cloudburstmc.nbt.util.stream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.cloudburstmc.nbt.util.VarInts;

/* loaded from: classes5.dex */
public class NetworkDataOutputStream extends LittleEndianDataOutputStream {
    public NetworkDataOutputStream(OutputStream stream) {
        super(stream);
    }

    public NetworkDataOutputStream(DataOutputStream stream) {
        super(stream);
    }

    @Override // org.cloudburstmc.nbt.util.stream.LittleEndianDataOutputStream, java.io.DataOutput
    public void writeInt(int value) throws IOException {
        VarInts.writeInt(this.stream, value);
    }

    @Override // org.cloudburstmc.nbt.util.stream.LittleEndianDataOutputStream, java.io.DataOutput
    public void writeLong(long value) throws IOException {
        VarInts.writeLong(this.stream, value);
    }

    @Override // org.cloudburstmc.nbt.util.stream.LittleEndianDataOutputStream, java.io.DataOutput
    public void writeUTF(String string) throws IOException {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        VarInts.writeUnsignedInt(this.stream, bytes.length);
        write(bytes);
    }
}
