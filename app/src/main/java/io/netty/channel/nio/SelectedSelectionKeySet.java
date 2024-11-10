package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class SelectedSelectionKeySet extends AbstractSet<SelectionKey> {
    SelectionKey[] keys = new SelectionKey[1024];
    int size;

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(SelectionKey o) {
        if (o == null) {
            return false;
        }
        if (this.size == this.keys.length) {
            increaseCapacity();
        }
        SelectionKey[] selectionKeyArr = this.keys;
        int i = this.size;
        this.size = i + 1;
        selectionKeyArr[i] = o;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        SelectionKey[] array = this.keys;
        int s = this.size;
        for (int i = 0; i < s; i++) {
            SelectionKey k = array[i];
            if (k.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<SelectionKey> iterator() {
        return new Iterator<SelectionKey>() { // from class: io.netty.channel.nio.SelectedSelectionKeySet.1
            private int idx;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.idx < SelectedSelectionKeySet.this.size;
            }

            @Override // java.util.Iterator
            public SelectionKey next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                SelectionKey[] selectionKeyArr = SelectedSelectionKeySet.this.keys;
                int i = this.idx;
                this.idx = i + 1;
                return selectionKeyArr[i];
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        reset(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset(int start) {
        Arrays.fill(this.keys, start, this.size, (Object) null);
        this.size = 0;
    }

    private void increaseCapacity() {
        SelectionKey[] newKeys = new SelectionKey[this.keys.length << 1];
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        this.keys = newKeys;
    }
}
