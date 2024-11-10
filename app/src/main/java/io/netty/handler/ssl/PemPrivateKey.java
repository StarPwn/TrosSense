package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.CharsetUtil;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import java.security.PrivateKey;

/* loaded from: classes4.dex */
public final class PemPrivateKey extends AbstractReferenceCounted implements PrivateKey, PemEncoded {
    private static final byte[] BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
    private static final byte[] END_PRIVATE_KEY = "\n-----END PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
    private static final String PKCS8_FORMAT = "PKCS#8";
    private static final long serialVersionUID = 7978017465645018936L;
    private final ByteBuf content;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PemEncoded toPEM(ByteBufAllocator allocator, boolean useDirect, PrivateKey key) {
        if (key instanceof PemEncoded) {
            return ((PemEncoded) key).retain();
        }
        byte[] bytes = key.getEncoded();
        if (bytes == null) {
            throw new IllegalArgumentException(key.getClass().getName() + " does not support encoding");
        }
        return toPEM(allocator, useDirect, bytes);
    }

    static PemEncoded toPEM(ByteBufAllocator allocator, boolean useDirect, byte[] bytes) {
        ByteBuf encoded = Unpooled.wrappedBuffer(bytes);
        try {
            ByteBuf base64 = SslUtils.toBase64(allocator, encoded);
            try {
                int size = BEGIN_PRIVATE_KEY.length + base64.readableBytes() + END_PRIVATE_KEY.length;
                boolean success = false;
                ByteBuf pem = useDirect ? allocator.directBuffer(size) : allocator.buffer(size);
                try {
                    pem.writeBytes(BEGIN_PRIVATE_KEY);
                    pem.writeBytes(base64);
                    pem.writeBytes(END_PRIVATE_KEY);
                    PemValue value = new PemValue(pem, true);
                    success = true;
                    return value;
                } finally {
                    if (!success) {
                        SslUtils.zerooutAndRelease(pem);
                    }
                }
            } finally {
                SslUtils.zerooutAndRelease(base64);
            }
        } finally {
            SslUtils.zerooutAndRelease(encoded);
        }
    }

    public static PemPrivateKey valueOf(byte[] key) {
        return valueOf(Unpooled.wrappedBuffer(key));
    }

    public static PemPrivateKey valueOf(ByteBuf key) {
        return new PemPrivateKey(key);
    }

    private PemPrivateKey(ByteBuf content) {
        this.content = (ByteBuf) ObjectUtil.checkNotNull(content, "content");
    }

    @Override // io.netty.handler.ssl.PemEncoded
    public boolean isSensitive() {
        return true;
    }

    @Override // io.netty.buffer.ByteBufHolder
    public ByteBuf content() {
        int count = refCnt();
        if (count <= 0) {
            throw new IllegalReferenceCountException(count);
        }
        return this.content;
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemPrivateKey copy() {
        return replace(this.content.copy());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemPrivateKey duplicate() {
        return replace(this.content.duplicate());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemPrivateKey retainedDuplicate() {
        return replace(this.content.retainedDuplicate());
    }

    @Override // io.netty.handler.ssl.PemEncoded, io.netty.buffer.ByteBufHolder
    public PemPrivateKey replace(ByteBuf content) {
        return new PemPrivateKey(content);
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public PemPrivateKey touch() {
        this.content.touch();
        return this;
    }

    @Override // io.netty.util.ReferenceCounted
    public PemPrivateKey touch(Object hint) {
        this.content.touch(hint);
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public PemPrivateKey retain() {
        return (PemPrivateKey) super.retain();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public PemPrivateKey retain(int increment) {
        return (PemPrivateKey) super.retain(increment);
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        SslUtils.zerooutAndRelease(this.content);
    }

    @Override // java.security.Key
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        throw new UnsupportedOperationException();
    }

    @Override // java.security.Key
    public String getFormat() {
        return PKCS8_FORMAT;
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() {
        release(refCnt());
    }

    @Override // javax.security.auth.Destroyable
    public boolean isDestroyed() {
        return refCnt() == 0;
    }
}
