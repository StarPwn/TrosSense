package io.netty.util;

import io.netty.util.AbstractConstant;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes4.dex */
public abstract class AbstractConstant<T extends AbstractConstant<T>> implements Constant<T> {
    private static final AtomicLong uniqueIdGenerator = new AtomicLong();
    private final int id;
    private final String name;
    private final long uniquifier = uniqueIdGenerator.getAndIncrement();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractConstant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override // io.netty.util.Constant
    public final String name() {
        return this.name;
    }

    @Override // io.netty.util.Constant
    public final int id() {
        return this.id;
    }

    public final String toString() {
        return name();
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // java.lang.Comparable
    public final int compareTo(T o) {
        if (this == o) {
            return 0;
        }
        int returnCode = hashCode() - o.hashCode();
        if (returnCode != 0) {
            return returnCode;
        }
        if (this.uniquifier < o.uniquifier) {
            return -1;
        }
        if (this.uniquifier > o.uniquifier) {
            return 1;
        }
        throw new Error("failed to compare two different constants");
    }
}
