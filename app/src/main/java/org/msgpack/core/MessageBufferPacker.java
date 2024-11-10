package org.msgpack.core;

import java.io.IOException;
import java.util.List;
import org.msgpack.core.MessagePack;
import org.msgpack.core.buffer.ArrayBufferOutput;
import org.msgpack.core.buffer.MessageBuffer;
import org.msgpack.core.buffer.MessageBufferOutput;

/* loaded from: classes5.dex */
public class MessageBufferPacker extends MessagePacker {
    /* JADX INFO: Access modifiers changed from: protected */
    public MessageBufferPacker(MessagePack.PackerConfig packerConfig) {
        this(new ArrayBufferOutput(packerConfig.getBufferSize()), packerConfig);
    }

    protected MessageBufferPacker(ArrayBufferOutput arrayBufferOutput, MessagePack.PackerConfig packerConfig) {
        super(arrayBufferOutput, packerConfig);
    }

    @Override // org.msgpack.core.MessagePacker
    public MessageBufferOutput reset(MessageBufferOutput messageBufferOutput) throws IOException {
        if (!(messageBufferOutput instanceof ArrayBufferOutput)) {
            throw new IllegalArgumentException("MessageBufferPacker accepts only ArrayBufferOutput");
        }
        return super.reset(messageBufferOutput);
    }

    private ArrayBufferOutput getArrayBufferOut() {
        return (ArrayBufferOutput) this.out;
    }

    @Override // org.msgpack.core.MessagePacker
    public void clear() {
        super.clear();
        getArrayBufferOut().clear();
    }

    public byte[] toByteArray() {
        try {
            flush();
            return getArrayBufferOut().toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageBuffer toMessageBuffer() {
        try {
            flush();
            return getArrayBufferOut().toMessageBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MessageBuffer> toBufferList() {
        try {
            flush();
            return getArrayBufferOut().toBufferList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBufferSize() {
        return getArrayBufferOut().getSize();
    }
}
