package kotlin;

import io.netty.handler.codec.http2.Http2CodecUtil;
import it.unimi.dsi.fastutil.BigArrays$$ExternalSyntheticBackport0;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.URangesKt;

/* compiled from: UByte.kt */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0005\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 v2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001vB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\nø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u0000H\u0087\nø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001c\u0010\u000fJ\u001b\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u0012J\u001b\u0010\u001b\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b \u0010\u0018J\u001a\u0010!\u001a\u00020\"2\b\u0010\t\u001a\u0004\u0018\u00010#HÖ\u0003¢\u0006\u0004\b$\u0010%J\u001b\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b'\u0010\u000fJ\u001b\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\bø\u0001\u0000¢\u0006\u0004\b(\u0010\u0012J\u001b\u0010&\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b)\u0010\u001fJ\u001b\u0010&\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\b*\u0010\u0018J\u0010\u0010+\u001a\u00020\rHÖ\u0001¢\u0006\u0004\b,\u0010-J\u0016\u0010.\u001a\u00020\u0000H\u0087\nø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0005J\u0016\u00100\u001a\u00020\u0000H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0005J\u001b\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b3\u0010\u000fJ\u001b\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\b4\u0010\u0012J\u001b\u00102\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\b5\u0010\u001fJ\u001b\u00102\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\b6\u0010\u0018J\u001b\u00107\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\bø\u0001\u0000¢\u0006\u0004\b8\u0010\u000bJ\u001b\u00107\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\bø\u0001\u0000¢\u0006\u0004\b9\u0010\u0012J\u001b\u00107\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\bø\u0001\u0000¢\u0006\u0004\b:\u0010\u001fJ\u001b\u00107\u001a\u00020\u00162\u0006\u0010\t\u001a\u00020\u0016H\u0087\bø\u0001\u0000¢\u0006\u0004\b;\u0010<J\u001b\u0010=\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\b>\u0010\u000bJ\u001b\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\b@\u0010\u000fJ\u001b\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\bA\u0010\u0012J\u001b\u0010?\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\bB\u0010\u001fJ\u001b\u0010?\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bC\u0010\u0018J\u001b\u0010D\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bF\u0010GJ\u001b\u0010H\u001a\u00020E2\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bI\u0010GJ\u001b\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bK\u0010\u000fJ\u001b\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\bL\u0010\u0012J\u001b\u0010J\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\bM\u0010\u001fJ\u001b\u0010J\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bN\u0010\u0018J\u001b\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0000H\u0087\nø\u0001\u0000¢\u0006\u0004\bP\u0010\u000fJ\u001b\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0010H\u0087\nø\u0001\u0000¢\u0006\u0004\bQ\u0010\u0012J\u001b\u0010O\u001a\u00020\u00132\u0006\u0010\t\u001a\u00020\u0013H\u0087\nø\u0001\u0000¢\u0006\u0004\bR\u0010\u001fJ\u001b\u0010O\u001a\u00020\u00102\u0006\u0010\t\u001a\u00020\u0016H\u0087\nø\u0001\u0000¢\u0006\u0004\bS\u0010\u0018J\u0010\u0010T\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bU\u0010\u0005J\u0010\u0010V\u001a\u00020WH\u0087\b¢\u0006\u0004\bX\u0010YJ\u0010\u0010Z\u001a\u00020[H\u0087\b¢\u0006\u0004\b\\\u0010]J\u0010\u0010^\u001a\u00020\rH\u0087\b¢\u0006\u0004\b_\u0010-J\u0010\u0010`\u001a\u00020aH\u0087\b¢\u0006\u0004\bb\u0010cJ\u0010\u0010d\u001a\u00020eH\u0087\b¢\u0006\u0004\bf\u0010gJ\u000f\u0010h\u001a\u00020iH\u0016¢\u0006\u0004\bj\u0010kJ\u0016\u0010l\u001a\u00020\u0000H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bm\u0010\u0005J\u0016\u0010n\u001a\u00020\u0010H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bo\u0010-J\u0016\u0010p\u001a\u00020\u0013H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bq\u0010cJ\u0016\u0010r\u001a\u00020\u0016H\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bs\u0010gJ\u001b\u0010t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\fø\u0001\u0000¢\u0006\u0004\bu\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003ø\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006w"}, d2 = {"Lkotlin/UByte;", "", "data", "", "constructor-impl", "(B)B", "getData$annotations", "()V", "and", "other", "and-7apg3OU", "(BB)B", "compareTo", "", "compareTo-7apg3OU", "(BB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(BI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(BJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(BS)I", "dec", "dec-w2LRezQ", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(BJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(BLjava/lang/Object;)Z", "floorDiv", "floorDiv-7apg3OU", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "floorDiv-xj2QHRw", "hashCode", "hashCode-impl", "(B)I", "inc", "inc-w2LRezQ", "inv", "inv-w2LRezQ", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "mod", "mod-7apg3OU", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "mod-xj2QHRw", "(BS)S", "or", "or-7apg3OU", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-7apg3OU", "(BB)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-7apg3OU", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "toByte-impl", "toDouble", "", "toDouble-impl", "(B)D", "toFloat", "", "toFloat-impl", "(B)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(B)J", "toShort", "", "toShort-impl", "(B)S", "toString", "", "toString-impl", "(B)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-7apg3OU", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 8, 0}, xi = 48)
@JvmInline
/* loaded from: classes4.dex */
public final class UByte implements Comparable<UByte> {
    public static final byte MAX_VALUE = -1;
    public static final byte MIN_VALUE = 0;
    public static final int SIZE_BITS = 8;
    public static final int SIZE_BYTES = 1;
    private final byte data;

    /* renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ UByte m292boximpl(byte b) {
        return new UByte(b);
    }

    /* renamed from: constructor-impl, reason: not valid java name */
    public static byte m298constructorimpl(byte b) {
        return b;
    }

    /* renamed from: equals-impl, reason: not valid java name */
    public static boolean m304equalsimpl(byte b, Object obj) {
        return (obj instanceof UByte) && b == ((UByte) obj).getData();
    }

    /* renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m305equalsimpl0(byte b, byte b2) {
        return b == b2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* renamed from: hashCode-impl, reason: not valid java name */
    public static int m310hashCodeimpl(byte b) {
        return Byte.hashCode(b);
    }

    public boolean equals(Object obj) {
        return m304equalsimpl(this.data, obj);
    }

    public int hashCode() {
        return m310hashCodeimpl(this.data);
    }

    /* renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ byte getData() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UByte uByte) {
        return Intrinsics.compare(getData() & 255, uByte.getData() & 255);
    }

    private /* synthetic */ UByte(byte data) {
        this.data = data;
    }

    /* renamed from: compareTo-7apg3OU, reason: not valid java name */
    private int m293compareTo7apg3OU(byte other) {
        return Intrinsics.compare(getData() & 255, other & 255);
    }

    /* renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static int m294compareTo7apg3OU(byte arg0, byte other) {
        return Intrinsics.compare(arg0 & 255, other & 255);
    }

    /* renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m297compareToxj2QHRw(byte arg0, short other) {
        return Intrinsics.compare(arg0 & 255, 65535 & other);
    }

    /* renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m296compareToWZ4Q5Ns(byte arg0, int other) {
        int compare;
        compare = Integer.compare(UInt.m375constructorimpl(arg0 & 255) ^ Integer.MIN_VALUE, other ^ Integer.MIN_VALUE);
        return compare;
    }

    /* renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m295compareToVKZWuLQ(byte arg0, long other) {
        int compare;
        compare = Long.compare(ULong.m454constructorimpl(arg0 & 255) ^ Long.MIN_VALUE, other ^ Long.MIN_VALUE);
        return compare;
    }

    /* renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m322plus7apg3OU(byte arg0, byte other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) + UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m325plusxj2QHRw(byte arg0, short other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) + UInt.m375constructorimpl(65535 & other));
    }

    /* renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m324plusWZ4Q5Ns(byte arg0, int other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) + other);
    }

    /* renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m323plusVKZWuLQ(byte arg0, long other) {
        return ULong.m454constructorimpl(ULong.m454constructorimpl(arg0 & 255) + other);
    }

    /* renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m313minus7apg3OU(byte arg0, byte other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) - UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m316minusxj2QHRw(byte arg0, short other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) - UInt.m375constructorimpl(65535 & other));
    }

    /* renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m315minusWZ4Q5Ns(byte arg0, int other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) - other);
    }

    /* renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m314minusVKZWuLQ(byte arg0, long other) {
        return ULong.m454constructorimpl(ULong.m454constructorimpl(arg0 & 255) - other);
    }

    /* renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m332times7apg3OU(byte arg0, byte other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) * UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m335timesxj2QHRw(byte arg0, short other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) * UInt.m375constructorimpl(65535 & other));
    }

    /* renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m334timesWZ4Q5Ns(byte arg0, int other) {
        return UInt.m375constructorimpl(UInt.m375constructorimpl(arg0 & 255) * other);
    }

    /* renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m333timesVKZWuLQ(byte arg0, long other) {
        return ULong.m454constructorimpl(ULong.m454constructorimpl(arg0 & 255) * other);
    }

    /* renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m300div7apg3OU(byte arg0, byte other) {
        return UByte$$ExternalSyntheticBackport1.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m303divxj2QHRw(byte arg0, short other) {
        return UByte$$ExternalSyntheticBackport1.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(65535 & other));
    }

    /* renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m302divWZ4Q5Ns(byte arg0, int other) {
        return UByte$$ExternalSyntheticBackport1.m(UInt.m375constructorimpl(arg0 & 255), other);
    }

    /* renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m301divVKZWuLQ(byte arg0, long other) {
        return BigArrays$$ExternalSyntheticBackport0.m(ULong.m454constructorimpl(arg0 & 255), other);
    }

    /* renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m328rem7apg3OU(byte arg0, byte other) {
        return UByte$$ExternalSyntheticBackport0.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m331remxj2QHRw(byte arg0, short other) {
        return UByte$$ExternalSyntheticBackport0.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(65535 & other));
    }

    /* renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m330remWZ4Q5Ns(byte arg0, int other) {
        return UByte$$ExternalSyntheticBackport0.m(UInt.m375constructorimpl(arg0 & 255), other);
    }

    /* renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m329remVKZWuLQ(byte arg0, long other) {
        return UByte$$ExternalSyntheticBackport3.m(ULong.m454constructorimpl(arg0 & 255), other);
    }

    /* renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m306floorDiv7apg3OU(byte arg0, byte other) {
        return UByte$$ExternalSyntheticBackport1.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m309floorDivxj2QHRw(byte arg0, short other) {
        return UByte$$ExternalSyntheticBackport1.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(65535 & other));
    }

    /* renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m308floorDivWZ4Q5Ns(byte arg0, int other) {
        return UByte$$ExternalSyntheticBackport1.m(UInt.m375constructorimpl(arg0 & 255), other);
    }

    /* renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m307floorDivVKZWuLQ(byte arg0, long other) {
        return BigArrays$$ExternalSyntheticBackport0.m(ULong.m454constructorimpl(arg0 & 255), other);
    }

    /* renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m317mod7apg3OU(byte arg0, byte other) {
        return m298constructorimpl((byte) UByte$$ExternalSyntheticBackport0.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(other & 255)));
    }

    /* renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m320modxj2QHRw(byte arg0, short other) {
        return UShort.m561constructorimpl((short) UByte$$ExternalSyntheticBackport0.m(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(65535 & other)));
    }

    /* renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m319modWZ4Q5Ns(byte arg0, int other) {
        return UByte$$ExternalSyntheticBackport0.m(UInt.m375constructorimpl(arg0 & 255), other);
    }

    /* renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m318modVKZWuLQ(byte arg0, long other) {
        return UByte$$ExternalSyntheticBackport3.m(ULong.m454constructorimpl(arg0 & 255), other);
    }

    /* renamed from: inc-w2LRezQ, reason: not valid java name */
    private static final byte m311incw2LRezQ(byte arg0) {
        return m298constructorimpl((byte) (arg0 + 1));
    }

    /* renamed from: dec-w2LRezQ, reason: not valid java name */
    private static final byte m299decw2LRezQ(byte arg0) {
        return m298constructorimpl((byte) (arg0 - 1));
    }

    /* renamed from: rangeTo-7apg3OU, reason: not valid java name */
    private static final UIntRange m326rangeTo7apg3OU(byte arg0, byte other) {
        return new UIntRange(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(other & 255), null);
    }

    /* renamed from: rangeUntil-7apg3OU, reason: not valid java name */
    private static final UIntRange m327rangeUntil7apg3OU(byte arg0, byte other) {
        return URangesKt.m1560untilJ1ME1BU(UInt.m375constructorimpl(arg0 & 255), UInt.m375constructorimpl(other & 255));
    }

    /* renamed from: and-7apg3OU, reason: not valid java name */
    private static final byte m291and7apg3OU(byte arg0, byte other) {
        return m298constructorimpl((byte) (arg0 & other));
    }

    /* renamed from: or-7apg3OU, reason: not valid java name */
    private static final byte m321or7apg3OU(byte arg0, byte other) {
        return m298constructorimpl((byte) (arg0 | other));
    }

    /* renamed from: xor-7apg3OU, reason: not valid java name */
    private static final byte m347xor7apg3OU(byte arg0, byte other) {
        return m298constructorimpl((byte) (arg0 ^ other));
    }

    /* renamed from: inv-w2LRezQ, reason: not valid java name */
    private static final byte m312invw2LRezQ(byte arg0) {
        return m298constructorimpl((byte) (~arg0));
    }

    /* renamed from: toByte-impl, reason: not valid java name */
    private static final byte m336toByteimpl(byte arg0) {
        return arg0;
    }

    /* renamed from: toShort-impl, reason: not valid java name */
    private static final short m341toShortimpl(byte arg0) {
        return (short) (arg0 & Http2CodecUtil.MAX_UNSIGNED_BYTE);
    }

    /* renamed from: toInt-impl, reason: not valid java name */
    private static final int m339toIntimpl(byte arg0) {
        return arg0 & 255;
    }

    /* renamed from: toLong-impl, reason: not valid java name */
    private static final long m340toLongimpl(byte arg0) {
        return arg0 & 255;
    }

    /* renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m343toUBytew2LRezQ(byte arg0) {
        return arg0;
    }

    /* renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m346toUShortMh2AYeg(byte arg0) {
        return UShort.m561constructorimpl((short) (arg0 & Http2CodecUtil.MAX_UNSIGNED_BYTE));
    }

    /* renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m344toUIntpVg5ArA(byte arg0) {
        return UInt.m375constructorimpl(arg0 & 255);
    }

    /* renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m345toULongsVKNKU(byte arg0) {
        return ULong.m454constructorimpl(arg0 & 255);
    }

    /* renamed from: toFloat-impl, reason: not valid java name */
    private static final float m338toFloatimpl(byte arg0) {
        return arg0 & 255;
    }

    /* renamed from: toDouble-impl, reason: not valid java name */
    private static final double m337toDoubleimpl(byte arg0) {
        return arg0 & 255;
    }

    /* renamed from: toString-impl, reason: not valid java name */
    public static String m342toStringimpl(byte arg0) {
        return String.valueOf(arg0 & 255);
    }

    public String toString() {
        return m342toStringimpl(this.data);
    }
}
