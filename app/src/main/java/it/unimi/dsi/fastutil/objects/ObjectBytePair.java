package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Pair;
import java.util.Comparator;

/* loaded from: classes4.dex */
public interface ObjectBytePair<K> extends Pair<K, Byte> {
    byte rightByte();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte right() {
        return Byte.valueOf(rightByte());
    }

    default ObjectBytePair<K> right(byte r) {
        throw new UnsupportedOperationException();
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectBytePair<K> right(Byte l) {
        return right(l.byteValue());
    }

    default byte secondByte() {
        return rightByte();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte second() {
        return Byte.valueOf(secondByte());
    }

    default ObjectBytePair<K> second(byte r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectBytePair<K> second(Byte l) {
        return second(l.byteValue());
    }

    default byte valueByte() {
        return rightByte();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default Byte value() {
        return Byte.valueOf(valueByte());
    }

    default ObjectBytePair<K> value(byte r) {
        return right(r);
    }

    @Override // it.unimi.dsi.fastutil.Pair
    @Deprecated
    default ObjectBytePair<K> value(Byte l) {
        return value(l.byteValue());
    }

    static <K> ObjectBytePair<K> of(K left, byte right) {
        return new ObjectByteImmutablePair(left, right);
    }

    static <K> Comparator<ObjectBytePair<K>> lexComparator() {
        return new Comparator() { // from class: it.unimi.dsi.fastutil.objects.ObjectBytePair$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return ObjectBytePair.lambda$lexComparator$0((ObjectBytePair) obj, (ObjectBytePair) obj2);
            }
        };
    }

    static /* synthetic */ int lambda$lexComparator$0(ObjectBytePair x, ObjectBytePair y) {
        int t = ((Comparable) x.left()).compareTo(y.left());
        return t != 0 ? t : Byte.compare(x.rightByte(), y.rightByte());
    }
}
