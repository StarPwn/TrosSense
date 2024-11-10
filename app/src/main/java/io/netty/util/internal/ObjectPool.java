package io.netty.util.internal;

import io.netty.util.Recycler;

/* loaded from: classes4.dex */
public abstract class ObjectPool<T> {

    /* loaded from: classes4.dex */
    public interface Handle<T> {
        void recycle(T t);
    }

    /* loaded from: classes4.dex */
    public interface ObjectCreator<T> {
        T newObject(Handle<T> handle);
    }

    public abstract T get();

    ObjectPool() {
    }

    public static <T> ObjectPool<T> newPool(ObjectCreator<T> creator) {
        return new RecyclerObjectPool((ObjectCreator) ObjectUtil.checkNotNull(creator, "creator"));
    }

    /* loaded from: classes4.dex */
    private static final class RecyclerObjectPool<T> extends ObjectPool<T> {
        private final Recycler<T> recycler;

        RecyclerObjectPool(final ObjectCreator<T> creator) {
            this.recycler = new Recycler<T>() { // from class: io.netty.util.internal.ObjectPool.RecyclerObjectPool.1
                @Override // io.netty.util.Recycler
                protected T newObject(Recycler.Handle<T> handle) {
                    return (T) creator.newObject(handle);
                }
            };
        }

        @Override // io.netty.util.internal.ObjectPool
        public T get() {
            return this.recycler.get();
        }
    }
}
