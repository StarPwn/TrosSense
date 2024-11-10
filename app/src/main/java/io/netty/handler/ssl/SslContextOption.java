package io.netty.handler.ssl;

import io.netty.util.AbstractConstant;
import io.netty.util.ConstantPool;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class SslContextOption<T> extends AbstractConstant<SslContextOption<T>> {
    private static final ConstantPool<SslContextOption<Object>> pool = new ConstantPool<SslContextOption<Object>>() { // from class: io.netty.handler.ssl.SslContextOption.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.ConstantPool
        public SslContextOption<Object> newConstant(int id, String name) {
            return new SslContextOption<>(id, name);
        }
    };

    public static <T> SslContextOption<T> valueOf(String name) {
        return (SslContextOption) pool.valueOf(name);
    }

    public static <T> SslContextOption<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (SslContextOption) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    public static boolean exists(String name) {
        return pool.exists(name);
    }

    private SslContextOption(int id, String name) {
        super(id, name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SslContextOption(String name) {
        this(pool.nextId(), name);
    }

    public void validate(T value) {
        ObjectUtil.checkNotNull(value, "value");
    }
}
