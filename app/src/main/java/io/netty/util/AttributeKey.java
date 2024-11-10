package io.netty.util;

/* loaded from: classes4.dex */
public final class AttributeKey<T> extends AbstractConstant<AttributeKey<T>> {
    private static final ConstantPool<AttributeKey<Object>> pool = new ConstantPool<AttributeKey<Object>>() { // from class: io.netty.util.AttributeKey.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.ConstantPool
        public AttributeKey<Object> newConstant(int id, String name) {
            return new AttributeKey<>(id, name);
        }
    };

    public static <T> AttributeKey<T> valueOf(String name) {
        return (AttributeKey) pool.valueOf(name);
    }

    public static boolean exists(String name) {
        return pool.exists(name);
    }

    public static <T> AttributeKey<T> newInstance(String name) {
        return (AttributeKey) pool.newInstance(name);
    }

    public static <T> AttributeKey<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (AttributeKey) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    private AttributeKey(int id, String name) {
        super(id, name);
    }
}
