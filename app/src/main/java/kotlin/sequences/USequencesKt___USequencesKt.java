package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: _USequences.kt */
@Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"sum", "Lkotlin/UInt;", "Lkotlin/sequences/Sequence;", "Lkotlin/UByte;", "sumOfUByte", "(Lkotlin/sequences/Sequence;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Lkotlin/sequences/Sequence;)J", "Lkotlin/UShort;", "sumOfUShort", "kotlin-stdlib"}, k = 5, mv = {1, 8, 0}, xi = 49, xs = "kotlin/sequences/USequencesKt")
/* loaded from: classes5.dex */
class USequencesKt___USequencesKt {
    public static final int sumOfUInt(Sequence<UInt> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        int sum = 0;
        Iterator<UInt> it2 = sequence.iterator();
        while (it2.hasNext()) {
            int element = it2.next().getData();
            sum = UInt.m375constructorimpl(sum + element);
        }
        return sum;
    }

    public static final long sumOfULong(Sequence<ULong> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        long sum = 0;
        Iterator<ULong> it2 = sequence.iterator();
        while (it2.hasNext()) {
            long element = it2.next().getData();
            sum = ULong.m454constructorimpl(sum + element);
        }
        return sum;
    }

    public static final int sumOfUByte(Sequence<UByte> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        int sum = 0;
        Iterator<UByte> it2 = sequence.iterator();
        while (it2.hasNext()) {
            byte element = it2.next().getData();
            sum = UInt.m375constructorimpl(UInt.m375constructorimpl(element & 255) + sum);
        }
        return sum;
    }

    public static final int sumOfUShort(Sequence<UShort> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        int sum = 0;
        Iterator<UShort> it2 = sequence.iterator();
        while (it2.hasNext()) {
            short element = it2.next().getData();
            sum = UInt.m375constructorimpl(UInt.m375constructorimpl(65535 & element) + sum);
        }
        return sum;
    }
}
