package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Function;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntFunction;

/* loaded from: classes4.dex */
public final class Int2ObjectFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Int2ObjectFunctions() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyFunction<V> extends AbstractInt2ObjectFunction<V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V get(int k) {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int k, V defaultValue) {
            return defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean containsKey(int k) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V defaultReturnValue() {
            return null;
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public void defaultReturnValue(V defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
        }

        public Object clone() {
            return Int2ObjectFunctions.EMPTY_FUNCTION;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            return (o instanceof Function) && ((Function) o).size() == 0;
        }

        public String toString() {
            return "{}";
        }

        private Object readResolve() {
            return Int2ObjectFunctions.EMPTY_FUNCTION;
        }
    }

    /* loaded from: classes4.dex */
    public static class Singleton<V> extends AbstractInt2ObjectFunction<V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final V value;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(int key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean containsKey(int k) {
            return this.key == k;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V get(int k) {
            return this.key == k ? this.value : this.defRetValue;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int k, V defaultValue) {
            return this.key == k ? this.value : defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            return 1;
        }

        public Object clone() {
            return this;
        }
    }

    public static <V> Int2ObjectFunction<V> singleton(int key, V value) {
        return new Singleton(key, value);
    }

    public static <V> Int2ObjectFunction<V> singleton(Integer key, V value) {
        return new Singleton(key.intValue(), value);
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedFunction<V> implements Int2ObjectFunction<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectFunction<V> function;
        protected final Object sync;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Object put(Integer num, Object obj) {
            return put(num, (Integer) obj);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedFunction(Int2ObjectFunction<V> f, Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedFunction(Int2ObjectFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, java.util.function.IntFunction
        public V apply(int operand) {
            V apply;
            synchronized (this.sync) {
                apply = this.function.apply(operand);
            }
            return apply;
        }

        @Override // it.unimi.dsi.fastutil.Function, java.util.function.Function
        @Deprecated
        public V apply(Integer key) {
            V apply;
            synchronized (this.sync) {
                apply = this.function.apply((Int2ObjectFunction<V>) key);
            }
            return apply;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            int size;
            synchronized (this.sync) {
                size = this.function.size();
            }
            return size;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V defaultReturnValue() {
            V defaultReturnValue;
            synchronized (this.sync) {
                defaultReturnValue = this.function.defaultReturnValue();
            }
            return defaultReturnValue;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public void defaultReturnValue(V defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean containsKey(int k) {
            boolean containsKey;
            synchronized (this.sync) {
                containsKey = this.function.containsKey(k);
            }
            return containsKey;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public boolean containsKey(Object k) {
            boolean containsKey;
            synchronized (this.sync) {
                containsKey = this.function.containsKey(k);
            }
            return containsKey;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V put(int k, V v) {
            V put;
            synchronized (this.sync) {
                put = this.function.put(k, (int) v);
            }
            return put;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V get(int k) {
            V v;
            synchronized (this.sync) {
                v = this.function.get(k);
            }
            return v;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int k, V defaultValue) {
            V orDefault;
            synchronized (this.sync) {
                orDefault = this.function.getOrDefault(k, (int) defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V remove(int k) {
            V remove;
            synchronized (this.sync) {
                remove = this.function.remove(k);
            }
            return remove;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        @Deprecated
        public V put(Integer k, V v) {
            V put2;
            synchronized (this.sync) {
                put2 = this.function.put2(k, (Integer) v);
            }
            return put2;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V get(Object k) {
            V v;
            synchronized (this.sync) {
                v = this.function.get(k);
            }
            return v;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object k, V defaultValue) {
            V orDefault;
            synchronized (this.sync) {
                orDefault = this.function.getOrDefault(k, defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V remove(Object k) {
            V remove;
            synchronized (this.sync) {
                remove = this.function.remove(k);
            }
            return remove;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.sync) {
                hashCode = this.function.hashCode();
            }
            return hashCode;
        }

        public boolean equals(Object o) {
            boolean equals;
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                equals = this.function.equals(o);
            }
            return equals;
        }

        public String toString() {
            String obj;
            synchronized (this.sync) {
                obj = this.function.toString();
            }
            return obj;
        }

        private void writeObject(ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }

    public static <V> Int2ObjectFunction<V> synchronize(Int2ObjectFunction<V> f) {
        return new SynchronizedFunction(f);
    }

    public static <V> Int2ObjectFunction<V> synchronize(Int2ObjectFunction<V> f, Object sync) {
        return new SynchronizedFunction(f, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableFunction<V> extends AbstractInt2ObjectFunction<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectFunction<? extends V> function;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Object put(Integer num, Object obj) {
            return put(num, (Integer) obj);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableFunction(Int2ObjectFunction<? extends V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            return this.function.size();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V defaultReturnValue() {
            return this.function.defaultReturnValue();
        }

        @Override // it.unimi.dsi.fastutil.ints.AbstractInt2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public void defaultReturnValue(V defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean containsKey(int k) {
            return this.function.containsKey(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V put(int k, V v) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V get(int k) {
            return this.function.get(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int k, V defaultValue) {
            return this.function.getOrDefault(k, (int) defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V remove(int k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        @Deprecated
        public V put(Integer k, V v) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V get(Object k) {
            return this.function.get(k);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object k, V defaultValue) {
            return this.function.getOrDefault(k, defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V remove(Object k) {
            throw new UnsupportedOperationException();
        }

        public int hashCode() {
            return this.function.hashCode();
        }

        public boolean equals(Object o) {
            return o == this || this.function.equals(o);
        }

        public String toString() {
            return this.function.toString();
        }
    }

    public static <V> Int2ObjectFunction<V> unmodifiable(Int2ObjectFunction<? extends V> f) {
        return new UnmodifiableFunction(f);
    }

    /* loaded from: classes4.dex */
    public static class PrimitiveFunction<V> implements Int2ObjectFunction<V> {
        protected final java.util.function.Function<? super Integer, ? extends V> function;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Object put(Integer num, Object obj) {
            return put(num, (Integer) obj);
        }

        protected PrimitiveFunction(java.util.function.Function<? super Integer, ? extends V> function) {
            this.function = function;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.ints.Int2ObjectMap
        public boolean containsKey(int key) {
            return this.function.apply(Integer.valueOf(key)) != null;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public boolean containsKey(Object key) {
            return (key == null || this.function.apply((Integer) key) == null) ? false : true;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V get(int key) {
            V v = this.function.apply(Integer.valueOf(key));
            if (v == null) {
                return null;
            }
            return v;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        public V getOrDefault(int key, V defaultValue) {
            V v = this.function.apply(Integer.valueOf(key));
            return v == null ? defaultValue : v;
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V get(Object key) {
            if (key == null) {
                return null;
            }
            return this.function.apply((Integer) key);
        }

        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public V getOrDefault(Object key, V defaultValue) {
            V v;
            if (key != null && (v = this.function.apply((Integer) key)) != null) {
                return v;
            }
            return defaultValue;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
        @Deprecated
        public V put(Integer key, V value) {
            throw new UnsupportedOperationException();
        }
    }

    public static <V> Int2ObjectFunction<V> primitive(java.util.function.Function<? super Integer, ? extends V> f) {
        Objects.requireNonNull(f);
        if (f instanceof Int2ObjectFunction) {
            return (Int2ObjectFunction) f;
        }
        if (f instanceof IntFunction) {
            final IntFunction intFunction = (IntFunction) f;
            intFunction.getClass();
            return new Int2ObjectFunction() { // from class: it.unimi.dsi.fastutil.ints.Int2ObjectFunctions$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
                public final Object get(int i) {
                    return intFunction.apply(i);
                }
            };
        }
        return new PrimitiveFunction(f);
    }
}
