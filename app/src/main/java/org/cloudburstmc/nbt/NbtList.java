package org.cloudburstmc.nbt;

import io.netty.util.internal.StringUtil;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import okhttp3.HttpUrl;

/* loaded from: classes5.dex */
public class NbtList<E> extends AbstractList<E> {
    public static final NbtList<Void> EMPTY = new NbtList<>(NbtType.END, new Void[0]);
    private final E[] array;
    private transient int hashCode;
    private transient boolean hashCodeGenerated;
    private final NbtType<E> type;

    public NbtList(NbtType<E> nbtType, Collection<E> collection) {
        this.type = (NbtType) Objects.requireNonNull(nbtType, "tagClass");
        this.array = (E[]) collection.toArray((Object[]) Array.newInstance((Class<?>) nbtType.getTagClass(), collection.size()));
    }

    @SafeVarargs
    public NbtList(NbtType<E> nbtType, E... eArr) {
        this.type = (NbtType) Objects.requireNonNull(nbtType, "tagClass");
        this.array = (E[]) Arrays.copyOf(eArr, eArr.length);
    }

    public NbtType<E> getType() {
        return this.type;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i) {
        if (i < 0 || i >= this.array.length) {
            throw new ArrayIndexOutOfBoundsException("Expected 0-" + (this.array.length - 1) + ". Got " + i);
        }
        return (E) NbtUtils.copy(this.array[i]);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.array.length;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        ListIterator<E> e1 = listIterator();
        ListIterator<E> listIterator = ((List) o).listIterator();
        while (e1.hasNext() && listIterator.hasNext()) {
            E o1 = e1.next();
            Object o2 = listIterator.next();
            if (!Objects.deepEquals(o1, o2)) {
                return false;
            }
        }
        return (e1.hasNext() || listIterator.hasNext()) ? false : true;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        if (this.hashCodeGenerated) {
            return this.hashCode;
        }
        int result = (Objects.hash(Integer.valueOf(super.hashCode()), this.type) * 31) + Arrays.deepHashCode(this.array);
        this.hashCode = result;
        this.hashCodeGenerated = true;
        return result;
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        Iterator<E> it2 = iterator();
        if (!it2.hasNext()) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[').append('\n');
        while (true) {
            String string = NbtUtils.toString(it2.next());
            sb.append(NbtUtils.indent(string));
            if (!it2.hasNext()) {
                return sb.append('\n').append(']').toString();
            }
            sb.append(StringUtil.COMMA).append('\n');
        }
    }
}
