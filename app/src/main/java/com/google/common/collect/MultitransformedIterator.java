package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class MultitransformedIterator<F, T> implements Iterator<T> {
    final Iterator<? extends F> backingIterator;
    private Iterator<? extends T> current = Iterators.emptyIterator();
    private Iterator<? extends T> removeFrom;

    abstract Iterator<? extends T> transform(F f);

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultitransformedIterator(Iterator<? extends F> backingIterator) {
        this.backingIterator = (Iterator) Preconditions.checkNotNull(backingIterator);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        Preconditions.checkNotNull(this.current);
        if (this.current.hasNext()) {
            return true;
        }
        while (this.backingIterator.hasNext()) {
            Iterator<? extends T> transform = transform(this.backingIterator.next());
            this.current = transform;
            Preconditions.checkNotNull(transform);
            if (this.current.hasNext()) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Iterator
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        this.removeFrom = this.current;
        return this.current.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        CollectPreconditions.checkRemove(this.removeFrom != null);
        this.removeFrom.remove();
        this.removeFrom = null;
    }
}
