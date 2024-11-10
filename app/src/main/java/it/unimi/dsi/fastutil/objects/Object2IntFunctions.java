package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.ToIntFunction;

/* loaded from: classes4.dex */
public final class Object2IntFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Object2IntFunctions() {
    }

    /* loaded from: classes4.dex */
    public static class EmptyFunction<K> extends AbstractObject2IntFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getInt(Object k) {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object k, int defaultValue) {
            return defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.Function
        public boolean containsKey(Object k) {
            return false;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int defaultReturnValue() {
            return 0;
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public void defaultReturnValue(int defRetValue) {
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
            return Object2IntFunctions.EMPTY_FUNCTION;
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
            return Object2IntFunctions.EMPTY_FUNCTION;
        }
    }

    /* loaded from: classes4.dex */
    public static class Singleton<K> extends AbstractObject2IntFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final int value;

        /* JADX INFO: Access modifiers changed from: protected */
        public Singleton(K key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override // it.unimi.dsi.fastutil.Function
        public boolean containsKey(Object k) {
            return Objects.equals(this.key, k);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getInt(Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object k, int defaultValue) {
            return Objects.equals(this.key, k) ? this.value : defaultValue;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            return 1;
        }

        public Object clone() {
            return this;
        }
    }

    public static <K> Object2IntFunction<K> singleton(K key, int value) {
        return new Singleton(key, value);
    }

    public static <K> Object2IntFunction<K> singleton(K key, Integer value) {
        return new Singleton(key, value.intValue());
    }

    /* loaded from: classes4.dex */
    public static class SynchronizedFunction<K> implements Object2IntFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2IntFunction<K> function;
        protected final Object sync;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.Function, java.util.function.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Object apply(Object obj) {
            return apply((SynchronizedFunction<K>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Integer put(Object obj, Integer num) {
            return put((SynchronizedFunction<K>) obj, num);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedFunction(Object2IntFunction<K> f, Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public SynchronizedFunction(Object2IntFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, java.util.function.ToIntFunction
        public int applyAsInt(K operand) {
            int applyAsInt;
            synchronized (this.sync) {
                applyAsInt = this.function.applyAsInt(operand);
            }
            return applyAsInt;
        }

        @Override // it.unimi.dsi.fastutil.Function, java.util.function.Function
        @Deprecated
        public Integer apply(K key) {
            Integer apply;
            synchronized (this.sync) {
                apply = this.function.apply(key);
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

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int defaultReturnValue() {
            int defaultReturnValue;
            synchronized (this.sync) {
                defaultReturnValue = this.function.defaultReturnValue();
            }
            return defaultReturnValue;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public void defaultReturnValue(int defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }

        @Override // it.unimi.dsi.fastutil.Function
        public boolean containsKey(Object k) {
            boolean containsKey;
            synchronized (this.sync) {
                containsKey = this.function.containsKey(k);
            }
            return containsKey;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int put(K k, int v) {
            int put;
            synchronized (this.sync) {
                put = this.function.put((Object2IntFunction<K>) k, v);
            }
            return put;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getInt(Object k) {
            int i;
            synchronized (this.sync) {
                i = this.function.getInt(k);
            }
            return i;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object k, int defaultValue) {
            int orDefault;
            synchronized (this.sync) {
                orDefault = this.function.getOrDefault(k, defaultValue);
            }
            return orDefault;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int removeInt(Object k) {
            int removeInt;
            synchronized (this.sync) {
                removeInt = this.function.removeInt(k);
            }
            return removeInt;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        @Deprecated
        public Integer put(K k, Integer v) {
            Integer put2;
            synchronized (this.sync) {
                put2 = this.function.put2((Object2IntFunction<K>) k, v);
            }
            return put2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer get(Object k) {
            Integer num;
            synchronized (this.sync) {
                num = this.function.get(k);
            }
            return num;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer getOrDefault(Object k, Integer defaultValue) {
            Integer orDefault;
            synchronized (this.sync) {
                orDefault = this.function.getOrDefault(k, defaultValue);
            }
            return orDefault;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer remove(Object k) {
            Integer remove;
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

    public static <K> Object2IntFunction<K> synchronize(Object2IntFunction<K> f) {
        return new SynchronizedFunction(f);
    }

    public static <K> Object2IntFunction<K> synchronize(Object2IntFunction<K> f, Object sync) {
        return new SynchronizedFunction(f, sync);
    }

    /* loaded from: classes4.dex */
    public static class UnmodifiableFunction<K> extends AbstractObject2IntFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2IntFunction<? extends K> function;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Integer put(Object obj, Integer num) {
            return put((UnmodifiableFunction<K>) obj, num);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public UnmodifiableFunction(Object2IntFunction<? extends K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public int size() {
            return this.function.size();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int defaultReturnValue() {
            return this.function.defaultReturnValue();
        }

        @Override // it.unimi.dsi.fastutil.objects.AbstractObject2IntFunction, it.unimi.dsi.fastutil.objects.Object2IntFunction
        public void defaultReturnValue(int defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.Function
        public boolean containsKey(Object k) {
            return this.function.containsKey(k);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int put(K k, int v) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getInt(Object k) {
            return this.function.getInt(k);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object k, int defaultValue) {
            return this.function.getOrDefault(k, defaultValue);
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int removeInt(Object k) {
            throw new UnsupportedOperationException();
        }

        @Override // it.unimi.dsi.fastutil.Function, it.unimi.dsi.fastutil.ints.Int2ObjectMap, java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        @Deprecated
        public Integer put(K k, Integer v) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer get(Object k) {
            return this.function.get(k);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer getOrDefault(Object k, Integer defaultValue) {
            return this.function.getOrDefault(k, defaultValue);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer remove(Object k) {
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

    public static <K> Object2IntFunction<K> unmodifiable(Object2IntFunction<? extends K> f) {
        return new UnmodifiableFunction(f);
    }

    /* loaded from: classes4.dex */
    public static class PrimitiveFunction<K> implements Object2IntFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Integer> function;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public /* bridge */ /* synthetic */ Integer put(Object obj, Integer num) {
            return put((PrimitiveFunction<K>) obj, num);
        }

        protected PrimitiveFunction(java.util.function.Function<? super K, ? extends Integer> function) {
            this.function = function;
        }

        @Override // it.unimi.dsi.fastutil.Function
        public boolean containsKey(Object key) {
            return this.function.apply(key) != null;
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getInt(Object key) {
            Integer v = this.function.apply(key);
            return v == null ? defaultReturnValue() : v.intValue();
        }

        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        public int getOrDefault(Object key, int defaultValue) {
            Integer v = this.function.apply(key);
            return v == null ? defaultValue : v.intValue();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer get(Object key) {
            return this.function.apply(key);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction, it.unimi.dsi.fastutil.Function
        @Deprecated
        public Integer getOrDefault(Object key, Integer defaultValue) {
            Integer v = this.function.apply(key);
            return v == null ? defaultValue : v;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
        @Deprecated
        public Integer put(K key, Integer value) {
            throw new UnsupportedOperationException();
        }
    }

    public static <K> Object2IntFunction<K> primitive(final java.util.function.Function<? super K, ? extends Integer> f) {
        Objects.requireNonNull(f);
        if (f instanceof Object2IntFunction) {
            return (Object2IntFunction) f;
        }
        if (f instanceof ToIntFunction) {
            return new Object2IntFunction() { // from class: it.unimi.dsi.fastutil.objects.Object2IntFunctions$$ExternalSyntheticLambda0
                @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
                public final int getInt(Object obj) {
                    int applyAsInt;
                    applyAsInt = ((ToIntFunction) f).applyAsInt(obj);
                    return applyAsInt;
                }
            };
        }
        return new PrimitiveFunction(f);
    }
}
