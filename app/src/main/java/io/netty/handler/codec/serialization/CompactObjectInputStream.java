package io.netty.handler.codec.serialization;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

/* loaded from: classes4.dex */
class CompactObjectInputStream extends ObjectInputStream {
    private final ClassResolver classResolver;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompactObjectInputStream(InputStream in, ClassResolver classResolver) throws IOException {
        super(in);
        this.classResolver = classResolver;
    }

    @Override // java.io.ObjectInputStream
    protected void readStreamHeader() throws IOException {
        int version = readByte() & 255;
        if (version != 5) {
            throw new StreamCorruptedException("Unsupported version: " + version);
        }
    }

    @Override // java.io.ObjectInputStream
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        int type = read();
        if (type < 0) {
            throw new EOFException();
        }
        switch (type) {
            case 0:
                return super.readClassDescriptor();
            case 1:
                String className = readUTF();
                Class<?> clazz = this.classResolver.resolve(className);
                return ObjectStreamClass.lookupAny(clazz);
            default:
                throw new StreamCorruptedException("Unexpected class descriptor type: " + type);
        }
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        try {
            Class<?> clazz = this.classResolver.resolve(desc.getName());
            return clazz;
        } catch (ClassNotFoundException e) {
            Class<?> clazz2 = super.resolveClass(desc);
            return clazz2;
        }
    }
}
