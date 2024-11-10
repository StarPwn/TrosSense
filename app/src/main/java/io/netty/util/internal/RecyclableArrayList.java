package io.netty.util.internal;

import io.netty.util.internal.ObjectPool;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: classes4.dex */
public final class RecyclableArrayList extends ArrayList<Object> {
    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final ObjectPool<RecyclableArrayList> RECYCLER = ObjectPool.newPool(new ObjectPool.ObjectCreator<RecyclableArrayList>() { // from class: io.netty.util.internal.RecyclableArrayList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.internal.ObjectPool.ObjectCreator
        public RecyclableArrayList newObject(ObjectPool.Handle<RecyclableArrayList> handle) {
            return new RecyclableArrayList(handle);
        }
    });
    private static final long serialVersionUID = -8605125654176467947L;
    private final ObjectPool.Handle<RecyclableArrayList> handle;
    private boolean insertSinceRecycled;

    public static RecyclableArrayList newInstance() {
        return newInstance(8);
    }

    public static RecyclableArrayList newInstance(int minCapacity) {
        RecyclableArrayList ret = RECYCLER.get();
        ret.ensureCapacity(minCapacity);
        return ret;
    }

    private RecyclableArrayList(ObjectPool.Handle<RecyclableArrayList> handle) {
        this(handle, 8);
    }

    private RecyclableArrayList(ObjectPool.Handle<RecyclableArrayList> handle, int initialCapacity) {
        super(initialCapacity);
        this.handle = handle;
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<?> c) {
        checkNullElements(c);
        if (super.addAll(c)) {
            this.insertSinceRecycled = true;
            return true;
        }
        return false;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public boolean addAll(int index, Collection<?> c) {
        checkNullElements(c);
        if (super.addAll(index, c)) {
            this.insertSinceRecycled = true;
            return true;
        }
        return false;
    }

    private static void checkNullElements(Collection<?> c) {
        if ((c instanceof RandomAccess) && (c instanceof List)) {
            List<?> list = (List) c;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (list.get(i) == null) {
                    throw new IllegalArgumentException("c contains null values");
                }
            }
            return;
        }
        for (Object element : c) {
            if (element == null) {
                throw new IllegalArgumentException("c contains null values");
            }
        }
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object element) {
        if (super.add(ObjectUtil.checkNotNull(element, "element"))) {
            this.insertSinceRecycled = true;
            return true;
        }
        return false;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public void add(int index, Object element) {
        super.add(index, ObjectUtil.checkNotNull(element, "element"));
        this.insertSinceRecycled = true;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Object set(int index, Object element) {
        Object old = super.set(index, ObjectUtil.checkNotNull(element, "element"));
        this.insertSinceRecycled = true;
        return old;
    }

    public boolean insertSinceRecycled() {
        return this.insertSinceRecycled;
    }

    public boolean recycle() {
        clear();
        this.insertSinceRecycled = false;
        this.handle.recycle(this);
        return true;
    }
}
