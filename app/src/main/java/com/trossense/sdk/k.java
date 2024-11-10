package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.UUID;

/* loaded from: classes3.dex */
public class k {
    private static final long f = dj.a(-3896814848403313645L, 5156735318396104463L, MethodHandles.lookup().lookupClass()).a(86577493207310L);
    private static final String[] g;
    private boolean a;
    private UUID b;
    private byte[] c;
    private String d;
    private String e;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0043. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[5];
        String str = "\u0016Y2\u0010ABF\"y\u0016)\u0003AT\u0016i\u0012.\u000bxG\u0018Q\u001c3K{M\u0012T<)\u0011Z_SL\u0018+\fL\u001b\t\u0016Y2\fLu\u000fHD";
        int length = "\u0016Y2\u0010ABF\"y\u0016)\u0003AT\u0016i\u0012.\u000bxG\u0018Q\u001c3K{M\u0012T<)\u0011Z_SL\u0018+\fL\u001b\t\u0016Y2\fLu\u000fHD".length();
        char c = 7;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 3;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, c(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "&i\u00100wE?xt\f&i\u0004>qx\ts=\u0012&%";
                        length = "&i\u00100wE?xt\f&i\u0004>qx\ts=\u0012&%".length();
                        c = '\t';
                        i2 = -1;
                        i3 = i;
                        i5 = 51;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    g = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 51;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 57;
                    break;
                case 1:
                    i2 = 122;
                    break;
                case 2:
                    i2 = 68;
                    break;
                case 3:
                    i2 = 102;
                    break;
                case 4:
                    i2 = 43;
                    break;
                case 5:
                    i2 = 37;
                    break;
                default:
                    i2 = 120;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] c(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'x');
        }
        return charArray;
    }

    public void a(String str) {
        this.d = str;
    }

    public void a(UUID uuid) {
        this.b = uuid;
    }

    public void a(boolean z) {
        this.a = z;
    }

    public void a(byte[] bArr) {
        this.c = bArr;
    }

    public boolean a() {
        return this.a;
    }

    protected boolean a(Object obj) {
        return obj instanceof k;
    }

    public UUID b() {
        return this.b;
    }

    public void b(String str) {
        this.e = str;
    }

    public byte[] c() {
        return this.c;
    }

    public String d() {
        return this.d;
    }

    public String e() {
        return this.e;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof k)) {
            return false;
        }
        k kVar = (k) obj;
        if (!kVar.a(this) || this.a != kVar.a) {
            return false;
        }
        UUID uuid = this.b;
        UUID uuid2 = kVar.b;
        if (uuid == null) {
            if (uuid2 != null) {
                return false;
            }
        } else if (!uuid.equals(uuid2)) {
            return false;
        }
        if (!Arrays.equals(this.c, kVar.c)) {
            return false;
        }
        String str = this.d;
        String str2 = kVar.d;
        if (str == null) {
            if (str2 != null) {
                return false;
            }
        } else if (!str.equals(str2)) {
            return false;
        }
        String str3 = this.e;
        String str4 = kVar.e;
        if (str3 == null) {
            if (str4 != null) {
                return false;
            }
        } else if (!str3.equals(str4)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        String[] d = m.d();
        int i = this.a ? 79 : 97;
        UUID uuid = this.b;
        int hashCode = ((((i + 59) * 59) + (uuid == null ? 43 : uuid.hashCode())) * 59) + Arrays.hashCode(this.c);
        String str = this.d;
        int hashCode2 = (hashCode * 59) + (str == null ? 43 : str.hashCode());
        String str2 = this.e;
        int hashCode3 = (hashCode2 * 59) + (str2 != null ? str2.hashCode() : 43);
        if (d == null) {
            PointerHolder.b(new int[2]);
        }
        return hashCode3;
    }

    public String toString() {
        m.d();
        boolean z = this.a;
        StringBuilder sb = new StringBuilder();
        String[] strArr = g;
        String sb2 = sb.append(strArr[1]).append(z).append(strArr[0]).append(this.b).append(strArr[4]).append(Arrays.toString(this.c)).append(strArr[2]).append(this.d).append(strArr[3]).append(this.e).append(")").toString();
        if (PointerHolder.s() == null) {
            m.b(new String[5]);
        }
        return sb2;
    }
}
