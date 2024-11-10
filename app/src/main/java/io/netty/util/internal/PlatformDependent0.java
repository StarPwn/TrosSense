package io.netty.util.internal;

import com.google.common.base.Ascii;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.Unsafe;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class PlatformDependent0 {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long ADDRESS_FIELD_OFFSET;
    private static final Method ALIGN_SLICE;
    private static final Method ALLOCATE_ARRAY_METHOD;
    private static final long BITS_MAX_DIRECT_MEMORY;
    private static final long BYTE_ARRAY_BASE_OFFSET;
    private static final Constructor<?> DIRECT_BUFFER_CONSTRUCTOR;
    static final int HASH_CODE_ASCII_SEED = -1028477387;
    static final int HASH_CODE_C1 = -862048943;
    static final int HASH_CODE_C2 = 461845907;
    private static final Object INTERNAL_UNSAFE;
    private static final long INT_ARRAY_BASE_OFFSET;
    private static final long INT_ARRAY_INDEX_SCALE;
    private static final long LONG_ARRAY_BASE_OFFSET;
    private static final long LONG_ARRAY_INDEX_SCALE;
    private static final boolean STORE_FENCE_AVAILABLE;
    private static final boolean UNALIGNED;
    static final Unsafe UNSAFE;
    private static final long UNSAFE_COPY_THRESHOLD = 1048576;
    private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PlatformDependent0.class);
    private static final Throwable EXPLICIT_NO_UNSAFE_CAUSE = explicitNoUnsafeCause0();
    private static final int JAVA_VERSION = javaVersion0();
    private static final boolean IS_ANDROID = isAndroid0();
    private static final boolean RUNNING_IN_NATIVE_IMAGE = SystemPropertyUtil.contains("org.graalvm.nativeimage.imagecode");
    private static final boolean IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE = explicitTryReflectionSetAccessible0();

    /* JADX WARN: Removed duplicated region for block: B:12:0x038b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0380  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0355  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0372  */
    static {
        /*
            Method dump skipped, instructions count: 938
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.PlatformDependent0.<clinit>():void");
    }

    static /* synthetic */ boolean access$000() {
        return unsafeStaticFieldOffsetSupported();
    }

    private static boolean unsafeStaticFieldOffsetSupported() {
        return !RUNNING_IN_NATIVE_IMAGE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isExplicitNoUnsafe() {
        return EXPLICIT_NO_UNSAFE_CAUSE != null;
    }

    private static Throwable explicitNoUnsafeCause0() {
        String unsafePropName;
        boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        logger.debug("-Dio.netty.noUnsafe: {}", Boolean.valueOf(noUnsafe));
        if (noUnsafe) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
        }
        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
            unsafePropName = "io.netty.tryUnsafe";
        } else {
            unsafePropName = "org.jboss.netty.tryUnsafe";
        }
        if (!SystemPropertyUtil.getBoolean(unsafePropName, true)) {
            String msg = "sun.misc.Unsafe: unavailable (" + unsafePropName + ")";
            logger.debug(msg);
            return new UnsupportedOperationException(msg);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isUnaligned() {
        return UNALIGNED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long bitsMaxDirectMemory() {
        return BITS_MAX_DIRECT_MEMORY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Throwable getUnsafeUnavailabilityCause() {
        return UNSAFE_UNAVAILABILITY_CAUSE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean unalignedAccess() {
        return UNALIGNED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void throwException(Throwable cause) {
        UNSAFE.throwException((Throwable) ObjectUtil.checkNotNull(cause, "cause"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasDirectBufferNoCleanerConstructor() {
        return DIRECT_BUFFER_CONSTRUCTOR != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity) {
        return newDirectBuffer(UNSAFE.reallocateMemory(directBufferAddress(buffer), capacity), capacity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer allocateDirectNoCleaner(int capacity) {
        return newDirectBuffer(UNSAFE.allocateMemory(Math.max(1, capacity)), capacity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasAlignSliceMethod() {
        return ALIGN_SLICE != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer alignSlice(ByteBuffer buffer, int alignment) {
        try {
            return (ByteBuffer) ALIGN_SLICE.invoke(buffer, Integer.valueOf(alignment));
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e2) {
            throw new Error(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean hasAllocateArrayMethod() {
        return ALLOCATE_ARRAY_METHOD != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] allocateUninitializedArray(int size) {
        try {
            return (byte[]) ALLOCATE_ARRAY_METHOD.invoke(INTERNAL_UNSAFE, Byte.TYPE, Integer.valueOf(size));
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e2) {
            throw new Error(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer newDirectBuffer(long address, int capacity) {
        ObjectUtil.checkPositiveOrZero(capacity, "capacity");
        try {
            return (ByteBuffer) DIRECT_BUFFER_CONSTRUCTOR.newInstance(Long.valueOf(address), Integer.valueOf(capacity));
        } catch (Throwable cause) {
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new Error(cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long directBufferAddress(ByteBuffer buffer) {
        return getLong(buffer, ADDRESS_FIELD_OFFSET);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long byteArrayBaseOffset() {
        return BYTE_ARRAY_BASE_OFFSET;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object getObject(Object object, long fieldOffset) {
        return UNSAFE.getObject(object, fieldOffset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(Object object, long fieldOffset) {
        return UNSAFE.getInt(object, fieldOffset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void safeConstructPutInt(Object object, long fieldOffset, int value) {
        if (STORE_FENCE_AVAILABLE) {
            UNSAFE.putInt(object, fieldOffset, value);
            UNSAFE.storeFence();
        } else {
            UNSAFE.putIntVolatile(object, fieldOffset, value);
        }
    }

    private static long getLong(Object object, long fieldOffset) {
        return UNSAFE.getLong(object, fieldOffset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long objectFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte getByte(long address) {
        return UNSAFE.getByte(address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte getByte(byte[] data, int index) {
        return UNSAFE.getByte(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte getByte(byte[] data, long index) {
        return UNSAFE.getByte(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static short getShort(byte[] data, int index) {
        return UNSAFE.getShort(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(byte[] data, int index) {
        return UNSAFE.getInt(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getInt(int[] data, long index) {
        return UNSAFE.getInt(data, INT_ARRAY_BASE_OFFSET + (INT_ARRAY_INDEX_SCALE * index));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getIntVolatile(long address) {
        return UNSAFE.getIntVolatile((Object) null, address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putIntOrdered(long adddress, int newValue) {
        UNSAFE.putOrderedInt((Object) null, adddress, newValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLong(byte[] data, int index) {
        return UNSAFE.getLong(data, BYTE_ARRAY_BASE_OFFSET + index);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getLong(long[] data, long index) {
        return UNSAFE.getLong(data, LONG_ARRAY_BASE_OFFSET + (LONG_ARRAY_INDEX_SCALE * index));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putByte(long address, byte value) {
        UNSAFE.putByte(address, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putShort(long address, short value) {
        UNSAFE.putShort(address, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putByte(byte[] data, int index, byte value) {
        UNSAFE.putByte(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putByte(Object data, long offset, byte value) {
        UNSAFE.putByte(data, offset, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putShort(byte[] data, int index, short value) {
        UNSAFE.putShort(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putInt(byte[] data, int index, int value) {
        UNSAFE.putInt(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putLong(byte[] data, int index, long value) {
        UNSAFE.putLong(data, BYTE_ARRAY_BASE_OFFSET + index, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void putObject(Object o, long offset, Object x) {
        UNSAFE.putObject(o, offset, x);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void copyMemory(long srcAddr, long dstAddr, long length) {
        if (javaVersion() <= 8) {
            copyMemoryWithSafePointPolling(srcAddr, dstAddr, length);
        } else {
            UNSAFE.copyMemory(srcAddr, dstAddr, length);
        }
    }

    private static void copyMemoryWithSafePointPolling(long srcAddr, long dstAddr, long length) {
        while (length > 0) {
            long size = Math.min(length, UNSAFE_COPY_THRESHOLD);
            UNSAFE.copyMemory(srcAddr, dstAddr, size);
            length -= size;
            srcAddr += size;
            dstAddr += size;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void copyMemory(Object src, long srcOffset, Object dst, long dstOffset, long length) {
        if (javaVersion() <= 8) {
            copyMemoryWithSafePointPolling(src, srcOffset, dst, dstOffset, length);
        } else {
            UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, length);
        }
    }

    private static void copyMemoryWithSafePointPolling(Object src, long srcOffset, Object dst, long dstOffset, long length) {
        long srcOffset2 = srcOffset;
        long dstOffset2 = dstOffset;
        long length2 = length;
        while (length2 > 0) {
            long size = Math.min(length2, UNSAFE_COPY_THRESHOLD);
            UNSAFE.copyMemory(src, srcOffset2, dst, dstOffset2, size);
            length2 -= size;
            srcOffset2 += size;
            dstOffset2 += size;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMemory(long address, long bytes, byte value) {
        UNSAFE.setMemory(address, bytes, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setMemory(Object o, long offset, long bytes, byte value) {
        UNSAFE.setMemory(o, offset, bytes, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        int remainingBytes = length & 7;
        long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
        long diff = startPos2 - startPos1;
        if (length >= 8) {
            long end = remainingBytes + baseOffset1;
            long i = (baseOffset1 - 8) + length;
            while (i >= end) {
                long end2 = end;
                if (UNSAFE.getLong(bytes1, i) == UNSAFE.getLong(bytes2, i + diff)) {
                    i -= 8;
                    end = end2;
                } else {
                    return false;
                }
            }
        }
        if (remainingBytes >= 4) {
            remainingBytes -= 4;
            long pos = remainingBytes + baseOffset1;
            if (UNSAFE.getInt(bytes1, pos) != UNSAFE.getInt(bytes2, pos + diff)) {
                return false;
            }
        }
        long baseOffset2 = baseOffset1 + diff;
        return remainingBytes >= 2 ? UNSAFE.getChar(bytes1, baseOffset1) == UNSAFE.getChar(bytes2, baseOffset2) && (remainingBytes == 2 || UNSAFE.getByte(bytes1, baseOffset1 + 2) == UNSAFE.getByte(bytes2, 2 + baseOffset2)) : remainingBytes == 0 || UNSAFE.getByte(bytes1, baseOffset1) == UNSAFE.getByte(bytes2, baseOffset2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        long result = 0;
        long remainingBytes = length & 7;
        long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
        long end = baseOffset1 + remainingBytes;
        long diff = startPos2 - startPos1;
        long i = (baseOffset1 - 8) + length;
        while (i >= end) {
            long end2 = end;
            long end3 = i + diff;
            result |= UNSAFE.getLong(bytes1, i) ^ UNSAFE.getLong(bytes2, end3);
            i -= 8;
            end = end2;
        }
        long end4 = end;
        if (remainingBytes >= 4) {
            result |= UNSAFE.getInt(bytes1, baseOffset1) ^ UNSAFE.getInt(bytes2, baseOffset1 + diff);
            remainingBytes -= 4;
        }
        if (remainingBytes >= 2) {
            long pos = end4 - remainingBytes;
            result |= UNSAFE.getChar(bytes1, pos) ^ UNSAFE.getChar(bytes2, pos + diff);
            remainingBytes -= 2;
        }
        if (remainingBytes == 1) {
            long pos2 = end4 - 1;
            result |= UNSAFE.getByte(bytes1, pos2) ^ UNSAFE.getByte(bytes2, pos2 + diff);
        }
        return ConstantTimeUtils.equalsConstantTime(result, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isZero(byte[] bytes, int startPos, int length) {
        if (length <= 0) {
            return true;
        }
        long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
        int remainingBytes = length & 7;
        long end = remainingBytes + baseOffset;
        for (long i = (baseOffset - 8) + length; i >= end; i -= 8) {
            if (UNSAFE.getLong(bytes, i) != 0) {
                return false;
            }
        }
        if (remainingBytes >= 4) {
            remainingBytes -= 4;
            if (UNSAFE.getInt(bytes, remainingBytes + baseOffset) != 0) {
                return false;
            }
        }
        return remainingBytes >= 2 ? UNSAFE.getChar(bytes, baseOffset) == 0 && (remainingBytes == 2 || bytes[startPos + 2] == 0) : bytes[startPos] == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hashCodeAscii(byte[] bytes, int startPos, int length) {
        int hash = HASH_CODE_ASCII_SEED;
        long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
        int remainingBytes = length & 7;
        long end = remainingBytes + baseOffset;
        for (long i = (baseOffset - 8) + length; i >= end; i -= 8) {
            hash = hashCodeAsciiCompute(UNSAFE.getLong(bytes, i), hash);
        }
        if (remainingBytes == 0) {
            return hash;
        }
        int hcConst = HASH_CODE_C1;
        boolean z = (remainingBytes != 2) & (remainingBytes != 4) & (remainingBytes != 6);
        int i2 = HASH_CODE_C1;
        if (z) {
            hash = (hash * HASH_CODE_C1) + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset));
            hcConst = HASH_CODE_C2;
            baseOffset++;
        }
        if ((remainingBytes != 1) & (remainingBytes != 4) & (remainingBytes != 5)) {
            hash = (hash * hcConst) + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset));
            if (hcConst == HASH_CODE_C1) {
                i2 = HASH_CODE_C2;
            }
            hcConst = i2;
            baseOffset += 2;
        }
        if (remainingBytes >= 4) {
            return (hash * hcConst) + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset));
        }
        return hash;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hashCodeAsciiCompute(long value, int hash) {
        return (HASH_CODE_C1 * hash) + (hashCodeAsciiSanitize((int) value) * HASH_CODE_C2) + ((int) ((2242545357458243584L & value) >>> 32));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hashCodeAsciiSanitize(int value) {
        return 522133279 & value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hashCodeAsciiSanitize(short value) {
        return value & 7967;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hashCodeAsciiSanitize(byte value) {
        return value & Ascii.US;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: io.netty.util.internal.PlatformDependent0.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: io.netty.util.internal.PlatformDependent0.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: io.netty.util.internal.PlatformDependent0.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int addressSize() {
        return UNSAFE.addressSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long allocateMemory(long size) {
        return UNSAFE.allocateMemory(size);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void freeMemory(long address) {
        UNSAFE.freeMemory(address);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long reallocateMemory(long address, long newSize) {
        return UNSAFE.reallocateMemory(address, newSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAndroid() {
        return IS_ANDROID;
    }

    private static boolean isAndroid0() {
        String vmName = SystemPropertyUtil.get("java.vm.name");
        boolean isAndroid = "Dalvik".equals(vmName);
        if (isAndroid) {
            logger.debug("Platform: Android");
        }
        return isAndroid;
    }

    private static boolean explicitTryReflectionSetAccessible0() {
        return SystemPropertyUtil.getBoolean("io.netty.tryReflectionSetAccessible", javaVersion() < 9 || RUNNING_IN_NATIVE_IMAGE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isExplicitTryReflectionSetAccessible() {
        return IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int javaVersion() {
        return JAVA_VERSION;
    }

    private static int javaVersion0() {
        int majorVersion;
        if (isAndroid0()) {
            majorVersion = 6;
        } else {
            majorVersion = majorVersionFromJavaSpecificationVersion();
        }
        logger.debug("Java version: {}", Integer.valueOf(majorVersion));
        return majorVersion;
    }

    static int majorVersionFromJavaSpecificationVersion() {
        return majorVersion(SystemPropertyUtil.get("java.specification.version", "1.6"));
    }

    static int majorVersion(String javaSpecVersion) {
        String[] components = javaSpecVersion.split("\\.");
        int[] version = new int[components.length];
        for (int i = 0; i < components.length; i++) {
            version[i] = Integer.parseInt(components[i]);
        }
        if (version[0] == 1) {
            if (version[1] < 6) {
                throw new AssertionError();
            }
            return version[1];
        }
        return version[0];
    }

    private PlatformDependent0() {
    }
}
