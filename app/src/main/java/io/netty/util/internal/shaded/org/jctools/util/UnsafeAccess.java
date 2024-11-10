package io.netty.util.internal.shaded.org.jctools.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

/* loaded from: classes4.dex */
public class UnsafeAccess {
    public static final Unsafe UNSAFE = getUnsafe();
    public static final boolean SUPPORTS_GET_AND_SET_REF = hasGetAndSetSupport();
    public static final boolean SUPPORTS_GET_AND_ADD_LONG = hasGetAndAddLongSupport();

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe instance = (Unsafe) field.get(null);
            return instance;
        } catch (Exception e) {
            try {
                Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor(new Class[0]);
                c.setAccessible(true);
                Unsafe instance2 = c.newInstance(new Object[0]);
                return instance2;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private static boolean hasGetAndSetSupport() {
        try {
            Unsafe.class.getMethod("getAndSetObject", Object.class, Long.TYPE, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean hasGetAndAddLongSupport() {
        try {
            Unsafe.class.getMethod("getAndAddLong", Object.class, Long.TYPE, Long.TYPE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static long fieldOffset(Class clz, String fieldName) throws RuntimeException {
        try {
            return UNSAFE.objectFieldOffset(clz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
