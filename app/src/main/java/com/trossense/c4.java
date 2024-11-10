package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.List;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes3.dex */
public class c4 {
    private static final short a = 0;
    private static final short b = 9;
    private static final short c = 15;
    private static final short d = 19;
    private static final long e = dj.a(386300223760347559L, 1193622949570149861L, MethodHandles.lookup().lookupClass()).a(133346887430777L);
    private static final String[] f;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0046. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[30];
        String str = "\ngYT\u000f\u0011d\u0001z\rX\u001f<v\u000faAT\u0000\u0013\ngYT\u000f\u0011d\u0001z\rX\u001f<m\u0002bZT\u0018\u0002\u000ej\u0010\ngYT\u000f\u0011d\u0001z\rX\u001f<m\bk\u0003\u000bx[\u0013\ngYT\u000f\u0011d\u0001z\rX\u001e\fk8z^T\u001e\u0019\ngYT\u000f\u0011d\u0001z\rW\u0000\nk\u0013QV_\b<v\u0013kR]\u0010\ngYT\u000f\u0011d\u0001z\rX\u001f<d\u001fk\u0015\ngYT\u000f\u0011d\u0001z\rT\u0002\u0007`\u0015QGT\r\u0011i\u0018\ngYT\u000f\u0011d\u0001z\r_\t\u0017m\u0002|^E\t<q\u000ekE\u0018\ngYT\u000f\u0011d\u0001z\rR\u0004\u0002l\tcVX\u0000<q\u000ekE\u001a\ngYT\u000f\u0011d\u0001z\rE\u0003\u0017`\nQXW3\u0016k\u0003w^_\u000b\u000f\ngYT\u000f\u0011d\u0001z\rP\u001e\u0011j\u0010\u0017\ngYT\u000f\u0011d\u0001z\rX\u001f<f\u000fkDE\u001c\u000fd\u0013k\u0010\ngYT\u000f\u0011d\u0001z\rB\u0004\n`\u000bj\u0015\ngYT\u000f\u0011d\u0001z\rX\u001f<i\u0002iPX\u0002\u0004v\u0014\ngYT\u000f\u0011d\u0001z\rX\u001f<u\u000em\\P\u0014\u0006\u0015\ngYT\u000f\u0011d\u0001z\rV\u0003\u000fa\u0002`hE\u0005\u0006w\u0012\ngYT\u000f\u0011d\u0001z\rR\u001e\fv\u0014lXF\u0015\ngYT\u000f\u0011d\u0001z\rT\u0002\u0007Z\u0004|NB\u0018\u0002i\u0011\ngYT\u000f\u0011d\u0001z\rX\u001f<c\baS\u0011\ngYT\u000f\u0011d\u0001z\rE\u001e\na\u0002`C\u0012\ngYT\u000f\u0011d\u0001z\rX\u001f<v\u0010aEU\u0014\ngYT\u000f\u0011d\u0001z\rB\u0018\fk\u0002QCX\t\u0011\u0016\ngYT\u000f\u0011d\u0001z\r]\t\u0002q\u000fkEn\u0018\n`\u0015\u0015\ngYT\u000f\u0011d\u0001z\rF\u0003\fa\u0002`hE\u0005\u0006w\u0010\ngYT\u000f\u0011d\u0001z\rX\u001f<g\by\u0016\ngYT\u000f\u0011d\u0001z\rU\u0005\u0002h\b`Sn\u0018\n`\u0015";
        int length = "\ngYT\u000f\u0011d\u0001z\rX\u001f<v\u000faAT\u0000\u0013\ngYT\u000f\u0011d\u0001z\rX\u001f<m\u0002bZT\u0018\u0002\u000ej\u0010\ngYT\u000f\u0011d\u0001z\rX\u001f<m\bk\u0003\u000bx[\u0013\ngYT\u000f\u0011d\u0001z\rX\u001e\fk8z^T\u001e\u0019\ngYT\u000f\u0011d\u0001z\rW\u0000\nk\u0013QV_\b<v\u0013kR]\u0010\ngYT\u000f\u0011d\u0001z\rX\u001f<d\u001fk\u0015\ngYT\u000f\u0011d\u0001z\rT\u0002\u0007`\u0015QGT\r\u0011i\u0018\ngYT\u000f\u0011d\u0001z\r_\t\u0017m\u0002|^E\t<q\u000ekE\u0018\ngYT\u000f\u0011d\u0001z\rR\u0004\u0002l\tcVX\u0000<q\u000ekE\u001a\ngYT\u000f\u0011d\u0001z\rE\u0003\u0017`\nQXW3\u0016k\u0003w^_\u000b\u000f\ngYT\u000f\u0011d\u0001z\rP\u001e\u0011j\u0010\u0017\ngYT\u000f\u0011d\u0001z\rX\u001f<f\u000fkDE\u001c\u000fd\u0013k\u0010\ngYT\u000f\u0011d\u0001z\rB\u0004\n`\u000bj\u0015\ngYT\u000f\u0011d\u0001z\rX\u001f<i\u0002iPX\u0002\u0004v\u0014\ngYT\u000f\u0011d\u0001z\rX\u001f<u\u000em\\P\u0014\u0006\u0015\ngYT\u000f\u0011d\u0001z\rV\u0003\u000fa\u0002`hE\u0005\u0006w\u0012\ngYT\u000f\u0011d\u0001z\rR\u001e\fv\u0014lXF\u0015\ngYT\u000f\u0011d\u0001z\rT\u0002\u0007Z\u0004|NB\u0018\u0002i\u0011\ngYT\u000f\u0011d\u0001z\rX\u001f<c\baS\u0011\ngYT\u000f\u0011d\u0001z\rE\u001e\na\u0002`C\u0012\ngYT\u000f\u0011d\u0001z\rX\u001f<v\u0010aEU\u0014\ngYT\u000f\u0011d\u0001z\rB\u0018\fk\u0002QCX\t\u0011\u0016\ngYT\u000f\u0011d\u0001z\r]\t\u0002q\u000fkEn\u0018\n`\u0015\u0015\ngYT\u000f\u0011d\u0001z\rF\u0003\fa\u0002`hE\u0005\u0006w\u0010\ngYT\u000f\u0011d\u0001z\rX\u001f<g\by\u0016\ngYT\u000f\u0011d\u0001z\rU\u0005\u0002h\b`Sn\u0018\n`\u0015".length();
        char c2 = 19;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 119;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a2;
                        i4 = i6 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "Y4\n\u0007\\B7R)^\u000bLo4[2\u0010\u0011\u0004Q3\u0007\n";
                        length = "Y4\n\u0007\\B7R)^\u000bLo4[2\u0010\u0011\u0004Q3\u0007\n".length();
                        c2 = 18;
                        i2 = -1;
                        i3 = i;
                        i5 = 36;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a2;
                i2 = i6 + c2;
                if (i2 >= length) {
                    f = strArr;
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 36;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0088, code lost:            if (r2 != null) goto L39;     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static float a(long r11, org.cloudburstmc.protocol.bedrock.data.inventory.ItemData r13, com.trossense.sdk.f r14) {
        /*
            Method dump skipped, instructions count: 261
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.c4.a(long, org.cloudburstmc.protocol.bedrock.data.inventory.ItemData, com.trossense.sdk.f):float");
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 16;
                    break;
                case 1:
                    i2 = 121;
                    break;
                case 2:
                    i2 = 64;
                    break;
                case 3:
                    i2 = 70;
                    break;
                case 4:
                    i2 = 27;
                    break;
                case 5:
                    i2 = 20;
                    break;
                default:
                    i2 = 114;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    public static short a(ItemData itemData, short s, long j) {
        if (itemData.getTag() != null) {
            List list = itemData.getTag().getList(f[29], NbtType.COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                NbtMap nbtMap = (NbtMap) list.get(i);
                String[] strArr = f;
                if (nbtMap.getShort(strArr[2]) == s) {
                    return nbtMap.getShort(strArr[4]);
                }
            }
        }
        return (short) 0;
    }

    public static boolean a(long j, ItemData itemData) {
        return (itemData.getBlockDefinition() == null || itemData.getBlockDefinition().getRuntimeId() == 0) ? false : true;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'r');
        }
        return charArray;
    }

    public static boolean b(long j, ItemData itemData, com.trossense.sdk.f fVar) {
        long j2 = (j ^ e) ^ 109477997109959L;
        if (itemData == ItemData.AIR) {
            return false;
        }
        if (a(j2, itemData)) {
            return true;
        }
        List<String> list = fVar.c;
        String identifier = fVar.getIdentifier();
        String[] strArr = f;
        return list.contains(strArr[1]) || list.contains(strArr[13]) || list.contains(strArr[15]) || list.contains(strArr[28]) || list.contains(strArr[22]) || list.contains(strArr[16]) || list.contains(strArr[7]) || list.contains(strArr[3]) || list.contains(strArr[0]) || list.contains(strArr[26]) || list.contains(strArr[20]) || identifier.equalsIgnoreCase(strArr[14]) || identifier.equalsIgnoreCase(strArr[21]) || identifier.equalsIgnoreCase(strArr[6]) || identifier.equalsIgnoreCase(strArr[11]) || identifier.equalsIgnoreCase(strArr[19]) || identifier.equalsIgnoreCase(strArr[8]) || identifier.equalsIgnoreCase(strArr[12]);
    }
}
