package io.airlift.compress.snappy;

import io.airlift.compress.IncompatibleJvmException;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteOrder;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
final class UnsafeUtil {
    private static final long ADDRESS_OFFSET;
    public static final Unsafe UNSAFE;

    private UnsafeUtil() {
    }

    static {
        ByteOrder order = ByteOrder.nativeOrder();
        if (!order.equals(ByteOrder.LITTLE_ENDIAN)) {
            throw new IncompatibleJvmException(String.format("Snappy requires a little endian platform (found %s)", order));
        }
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
            try {
                ADDRESS_OFFSET = UNSAFE.objectFieldOffset(Buffer.class.getDeclaredField("address"));
            } catch (NoSuchFieldException e) {
                throw new IncompatibleJvmException("Snappy requires access to java.nio.Buffer raw address field");
            }
        } catch (Exception e2) {
            throw new IncompatibleJvmException("Snappy requires access to sun.misc.Unsafe");
        }
    }

    public static long getAddress(Buffer buffer) {
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("buffer is not direct");
        }
        return UNSAFE.getLong(buffer, ADDRESS_OFFSET);
    }
}
