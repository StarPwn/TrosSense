package io.netty.handler.codec;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import java.util.AbstractList;
import java.util.RandomAccess;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class CodecOutputList extends AbstractList<Object> implements RandomAccess {
    private Object[] array;
    private boolean insertSinceRecycled;
    private final CodecOutputListRecycler recycler;
    private int size;
    private static final CodecOutputListRecycler NOOP_RECYCLER = new CodecOutputListRecycler() { // from class: io.netty.handler.codec.CodecOutputList.1
        @Override // io.netty.handler.codec.CodecOutputList.CodecOutputListRecycler
        public void recycle(CodecOutputList object) {
        }
    };
    private static final FastThreadLocal<CodecOutputLists> CODEC_OUTPUT_LISTS_POOL = new FastThreadLocal<CodecOutputLists>() { // from class: io.netty.handler.codec.CodecOutputList.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public CodecOutputLists initialValue() throws Exception {
            return new CodecOutputLists(16);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface CodecOutputListRecycler {
        void recycle(CodecOutputList codecOutputList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class CodecOutputLists implements CodecOutputListRecycler {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private int count;
        private int currentIdx;
        private final CodecOutputList[] elements;
        private final int mask;

        CodecOutputLists(int numElements) {
            this.elements = new CodecOutputList[MathUtil.safeFindNextPositivePowerOfTwo(numElements)];
            for (int i = 0; i < this.elements.length; i++) {
                this.elements[i] = new CodecOutputList(this, 16);
            }
            this.count = this.elements.length;
            this.currentIdx = this.elements.length;
            this.mask = this.elements.length - 1;
        }

        public CodecOutputList getOrCreate() {
            if (this.count == 0) {
                return new CodecOutputList(CodecOutputList.NOOP_RECYCLER, 4);
            }
            this.count--;
            int idx = (this.currentIdx - 1) & this.mask;
            CodecOutputList list = this.elements[idx];
            this.currentIdx = idx;
            return list;
        }

        @Override // io.netty.handler.codec.CodecOutputList.CodecOutputListRecycler
        public void recycle(CodecOutputList codecOutputList) {
            int idx = this.currentIdx;
            this.elements[idx] = codecOutputList;
            this.currentIdx = (idx + 1) & this.mask;
            this.count++;
            if (this.count > this.elements.length) {
                throw new AssertionError();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CodecOutputList newInstance() {
        return CODEC_OUTPUT_LISTS_POOL.get().getOrCreate();
    }

    private CodecOutputList(CodecOutputListRecycler recycler, int size) {
        this.recycler = recycler;
        this.array = new Object[size];
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        checkIndex(index);
        return this.array[index];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object element) {
        ObjectUtil.checkNotNull(element, "element");
        try {
            insert(this.size, element);
        } catch (IndexOutOfBoundsException e) {
            expandArray();
            insert(this.size, element);
        }
        this.size++;
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public Object set(int index, Object element) {
        ObjectUtil.checkNotNull(element, "element");
        checkIndex(index);
        Object old = this.array[index];
        insert(index, element);
        return old;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Object element) {
        ObjectUtil.checkNotNull(element, "element");
        checkIndex(index);
        if (this.size == this.array.length) {
            expandArray();
        }
        if (index != this.size) {
            System.arraycopy(this.array, index, this.array, index + 1, this.size - index);
        }
        insert(index, element);
        this.size++;
    }

    @Override // java.util.AbstractList, java.util.List
    public Object remove(int index) {
        checkIndex(index);
        Object old = this.array[index];
        int len = (this.size - index) - 1;
        if (len > 0) {
            System.arraycopy(this.array, index + 1, this.array, index, len);
        }
        Object[] objArr = this.array;
        int i = this.size - 1;
        this.size = i;
        objArr[i] = null;
        return old;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.size = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean insertSinceRecycled() {
        return this.insertSinceRecycled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recycle() {
        for (int i = 0; i < this.size; i++) {
            this.array[i] = null;
        }
        this.size = 0;
        this.insertSinceRecycled = false;
        this.recycler.recycle(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getUnsafe(int index) {
        return this.array[index];
    }

    private void checkIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("expected: index < (" + this.size + "),but actual is (" + this.size + ")");
        }
    }

    private void insert(int index, Object element) {
        this.array[index] = element;
        this.insertSinceRecycled = true;
    }

    private void expandArray() {
        int newCapacity = this.array.length << 1;
        if (newCapacity < 0) {
            throw new OutOfMemoryError();
        }
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(this.array, 0, newArray, 0, this.array.length);
        this.array = newArray;
    }
}
