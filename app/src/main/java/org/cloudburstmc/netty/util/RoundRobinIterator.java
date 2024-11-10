package org.cloudburstmc.netty.util;

import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes5.dex */
public class RoundRobinIterator<E> implements Iterator<E> {
    private final Collection<E> collection;
    private Iterator<E> iterator;

    public RoundRobinIterator(Collection<E> collection) {
        this.collection = collection;
        this.iterator = this.collection.iterator();
    }

    @Override // java.util.Iterator
    public synchronized boolean hasNext() {
        return !this.collection.isEmpty();
    }

    @Override // java.util.Iterator
    public synchronized E next() {
        if (!this.iterator.hasNext()) {
            this.iterator = this.collection.iterator();
        }
        return this.iterator.next();
    }
}
