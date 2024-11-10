package com.google.common.base;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Suppliers {

    /* loaded from: classes.dex */
    private interface SupplierFunction<T> extends Function<Supplier<T>, T> {
    }

    private Suppliers() {
    }

    public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(supplier);
        return new SupplierComposition(function, supplier);
    }

    /* loaded from: classes.dex */
    private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Function<? super F, T> function;
        final Supplier<F> supplier;

        SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
            this.function = function;
            this.supplier = supplier;
        }

        @Override // com.google.common.base.Supplier, java.util.function.Supplier
        public T get() {
            return this.function.apply(this.supplier.get());
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof SupplierComposition)) {
                return false;
            }
            SupplierComposition<?, ?> that = (SupplierComposition) obj;
            return this.function.equals(that.function) && this.supplier.equals(that.supplier);
        }

        public int hashCode() {
            return Objects.hashCode(this.function, this.supplier);
        }

        public String toString() {
            return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
        }
    }

    public static <T> Supplier<T> memoize(Supplier<T> delegate) {
        if ((delegate instanceof NonSerializableMemoizingSupplier) || (delegate instanceof MemoizingSupplier)) {
            return delegate;
        }
        return delegate instanceof Serializable ? new MemoizingSupplier(delegate) : new NonSerializableMemoizingSupplier(delegate);
    }

    /* loaded from: classes.dex */
    static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;
        volatile transient boolean initialized;
        transient T value;

        MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.base.Supplier, java.util.function.Supplier
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        return t;
                    }
                }
            }
            return this.value;
        }

        public String toString() {
            return "Suppliers.memoize(" + this.delegate + ")";
        }
    }

    /* loaded from: classes.dex */
    static class NonSerializableMemoizingSupplier<T> implements Supplier<T> {
        volatile Supplier<T> delegate;
        volatile boolean initialized;
        T value;

        NonSerializableMemoizingSupplier(Supplier<T> delegate) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
        }

        @Override // com.google.common.base.Supplier, java.util.function.Supplier
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        this.delegate = null;
                        return t;
                    }
                }
            }
            return this.value;
        }

        public String toString() {
            return "Suppliers.memoize(" + this.delegate + ")";
        }
    }

    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
        return new ExpiringMemoizingSupplier(delegate, duration, unit);
    }

    /* loaded from: classes.dex */
    static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;
        final long durationNanos;
        volatile transient long expirationNanos;
        volatile transient T value;

        ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
            this.delegate = (Supplier) Preconditions.checkNotNull(delegate);
            this.durationNanos = unit.toNanos(duration);
            Preconditions.checkArgument(duration > 0);
        }

        @Override // com.google.common.base.Supplier, java.util.function.Supplier
        public T get() {
            long nanos = this.expirationNanos;
            long now = Platform.systemNanoTime();
            if (nanos == 0 || now - nanos >= 0) {
                synchronized (this) {
                    if (nanos == this.expirationNanos) {
                        T t = this.delegate.get();
                        this.value = t;
                        long nanos2 = now + this.durationNanos;
                        this.expirationNanos = nanos2 == 0 ? 1L : nanos2;
                        return t;
                    }
                }
            }
            return this.value;
        }

        public String toString() {
            return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
        }
    }

    public static <T> Supplier<T> ofInstance(@Nullable T instance) {
        return new SupplierOfInstance(instance);
    }

    /* loaded from: classes.dex */
    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final T instance;

        SupplierOfInstance(@Nullable T instance) {
            this.instance = instance;
        }

        @Override // com.google.common.base.Supplier, java.util.function.Supplier
        public T get() {
            return this.instance;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof SupplierOfInstance) {
                SupplierOfInstance<?> that = (SupplierOfInstance) obj;
                return Objects.equal(this.instance, that.instance);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(this.instance);
        }

        public String toString() {
            return "Suppliers.ofInstance(" + this.instance + ")";
        }
    }

    public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
        return new ThreadSafeSupplier((Supplier) Preconditions.checkNotNull(delegate));
    }

    /* loaded from: classes.dex */
    private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0;
        final Supplier<T> delegate;

        ThreadSafeSupplier(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        @Override // com.google.common.base.Supplier, java.util.function.Supplier
        public T get() {
            T t;
            synchronized (this.delegate) {
                t = this.delegate.get();
            }
            return t;
        }

        public String toString() {
            return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
        }
    }

    public static <T> Function<Supplier<T>, T> supplierFunction() {
        SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
        return sf;
    }

    /* loaded from: classes.dex */
    private enum SupplierFunctionImpl implements SupplierFunction<Object> {
        INSTANCE;

        @Override // com.google.common.base.Function, java.util.function.Function
        public Object apply(Supplier<Object> input) {
            return input.get();
        }

        @Override // java.lang.Enum
        public String toString() {
            return "Suppliers.supplierFunction()";
        }
    }
}
