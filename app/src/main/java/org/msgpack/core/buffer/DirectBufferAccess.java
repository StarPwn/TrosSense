package org.msgpack.core.buffer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.nio.ch.DirectBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes5.dex */
public class DirectBufferAccess {
    static Constructor<?> byteBufferConstructor;
    static DirectBufferConstructorType directBufferConstructorType;
    static Class<?> directByteBufferClass;
    static Method mClean;
    static Method mCleaner;
    static Method mInvokeCleaner;
    static Method memoryBlockWrapFromJni;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes5.dex */
    public enum DirectBufferConstructorType {
        ARGS_LONG_LONG,
        ARGS_LONG_INT_REF,
        ARGS_LONG_INT,
        ARGS_INT_INT,
        ARGS_MB_INT_INT
    }

    private DirectBufferAccess() {
    }

    static {
        Method declaredMethod;
        Constructor<?> declaredConstructor;
        DirectBufferConstructorType directBufferConstructorType2;
        try {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1);
            directByteBufferClass = allocateDirect.getClass();
            try {
                declaredConstructor = directByteBufferClass.getDeclaredConstructor(Long.TYPE, Long.TYPE);
                directBufferConstructorType2 = DirectBufferConstructorType.ARGS_LONG_LONG;
                declaredMethod = null;
            } catch (NoSuchMethodException e) {
                try {
                    Constructor<?> declaredConstructor2 = directByteBufferClass.getDeclaredConstructor(Long.TYPE, Integer.TYPE, Object.class);
                    directBufferConstructorType2 = DirectBufferConstructorType.ARGS_LONG_INT_REF;
                    declaredMethod = null;
                    declaredConstructor = declaredConstructor2;
                } catch (NoSuchMethodException e2) {
                    try {
                        Constructor<?> declaredConstructor3 = directByteBufferClass.getDeclaredConstructor(Long.TYPE, Integer.TYPE);
                        directBufferConstructorType2 = DirectBufferConstructorType.ARGS_LONG_INT;
                        declaredMethod = null;
                        declaredConstructor = declaredConstructor3;
                    } catch (NoSuchMethodException e3) {
                        try {
                            Constructor<?> declaredConstructor4 = directByteBufferClass.getDeclaredConstructor(Integer.TYPE, Integer.TYPE);
                            directBufferConstructorType2 = DirectBufferConstructorType.ARGS_INT_INT;
                            declaredMethod = null;
                            declaredConstructor = declaredConstructor4;
                        } catch (NoSuchMethodException e4) {
                            Class<?> cls = Class.forName("java.nio.MemoryBlock");
                            declaredMethod = cls.getDeclaredMethod("wrapFromJni", Integer.TYPE, Long.TYPE);
                            declaredMethod.setAccessible(true);
                            declaredConstructor = directByteBufferClass.getDeclaredConstructor(cls, Integer.TYPE, Integer.TYPE);
                            directBufferConstructorType2 = DirectBufferConstructorType.ARGS_MB_INT_INT;
                        }
                    }
                }
            }
            byteBufferConstructor = declaredConstructor;
            directBufferConstructorType = directBufferConstructorType2;
            memoryBlockWrapFromJni = declaredMethod;
            if (byteBufferConstructor != null) {
                try {
                    byteBufferConstructor.setAccessible(true);
                } catch (RuntimeException e5) {
                    if ("java.lang.reflect.InaccessibleObjectException".equals(e5.getClass().getName())) {
                        byteBufferConstructor = null;
                    } else {
                        throw e5;
                    }
                }
                if (MessageBuffer.javaVersion <= 8) {
                    setupCleanerJava6(allocateDirect);
                    return;
                } else {
                    setupCleanerJava9(allocateDirect);
                    return;
                }
            }
            throw new RuntimeException("Constructor of DirectByteBuffer is not found");
        } catch (Exception e6) {
            throw new RuntimeException(e6);
        }
    }

    private static void setupCleanerJava6(final ByteBuffer byteBuffer) {
        Object doPrivileged = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.msgpack.core.buffer.DirectBufferAccess.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return DirectBufferAccess.getCleanerMethod(byteBuffer);
            }
        });
        if (doPrivileged instanceof Throwable) {
            throw new RuntimeException((Throwable) doPrivileged);
        }
        mCleaner = (Method) doPrivileged;
        Object doPrivileged2 = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.msgpack.core.buffer.DirectBufferAccess.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                return DirectBufferAccess.getCleanMethod(byteBuffer, DirectBufferAccess.mCleaner);
            }
        });
        if (doPrivileged2 instanceof Throwable) {
            throw new RuntimeException((Throwable) doPrivileged2);
        }
        mClean = (Method) doPrivileged2;
    }

    private static void setupCleanerJava9(final ByteBuffer byteBuffer) {
        Object doPrivileged = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.msgpack.core.buffer.DirectBufferAccess.3
            @Override // java.security.PrivilegedAction
            public Object run() {
                return DirectBufferAccess.getInvokeCleanerMethod(byteBuffer);
            }
        });
        if (doPrivileged instanceof Throwable) {
            throw new RuntimeException((Throwable) doPrivileged);
        }
        mInvokeCleaner = (Method) doPrivileged;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getCleanerMethod(ByteBuffer byteBuffer) {
        try {
            Method declaredMethod = byteBuffer.getClass().getDeclaredMethod("cleaner", new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(byteBuffer, new Object[0]);
            return declaredMethod;
        } catch (IllegalAccessException e) {
            return e;
        } catch (NoSuchMethodException e2) {
            return e2;
        } catch (InvocationTargetException e3) {
            return e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getCleanMethod(ByteBuffer byteBuffer, Method method) {
        try {
            Method declaredMethod = method.getReturnType().getDeclaredMethod("clean", new Class[0]);
            Object invoke = method.invoke(byteBuffer, new Object[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(invoke, new Object[0]);
            return declaredMethod;
        } catch (IllegalAccessException e) {
            return e;
        } catch (NoSuchMethodException e2) {
            return e2;
        } catch (InvocationTargetException e3) {
            return e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object getInvokeCleanerMethod(ByteBuffer byteBuffer) {
        try {
            Method declaredMethod = MessageBuffer.unsafe.getClass().getDeclaredMethod("invokeCleaner", ByteBuffer.class);
            declaredMethod.invoke(MessageBuffer.unsafe, byteBuffer);
            return declaredMethod;
        } catch (IllegalAccessException e) {
            return e;
        } catch (NoSuchMethodException e2) {
            return e2;
        } catch (InvocationTargetException e3) {
            return e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getAddress(Buffer buffer) {
        return ((DirectBuffer) buffer).address();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clean(Object obj) {
        try {
            if (MessageBuffer.javaVersion <= 8) {
                mClean.invoke(mCleaner.invoke(obj, new Object[0]), new Object[0]);
            } else {
                mInvokeCleaner.invoke(MessageBuffer.unsafe, obj);
            }
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isDirectByteBufferInstance(Object obj) {
        return directByteBufferClass.isInstance(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer newByteBuffer(long j, int i, int i2, ByteBuffer byteBuffer) {
        if (byteBufferConstructor == null) {
            throw new IllegalStateException("Can't create a new DirectByteBuffer. In JDK17+, two JVM options needs to be set: --add-opens=java.base/java.nio=ALL-UNNAMED and --add-opens=java.base/sun.nio.ch=ALL-UNNAMED");
        }
        try {
            switch (directBufferConstructorType) {
                case ARGS_LONG_LONG:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Long.valueOf(j + i), Long.valueOf(i2));
                case ARGS_LONG_INT_REF:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Long.valueOf(j + i), Integer.valueOf(i2), byteBuffer);
                case ARGS_LONG_INT:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Long.valueOf(j + i), Integer.valueOf(i2));
                case ARGS_INT_INT:
                    return (ByteBuffer) byteBufferConstructor.newInstance(Integer.valueOf(((int) j) + i), Integer.valueOf(i2));
                case ARGS_MB_INT_INT:
                    return (ByteBuffer) byteBufferConstructor.newInstance(memoryBlockWrapFromJni.invoke(null, Long.valueOf(j + i), Integer.valueOf(i2)), Integer.valueOf(i2), 0);
                default:
                    throw new IllegalStateException("Unexpected value");
            }
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }
}
