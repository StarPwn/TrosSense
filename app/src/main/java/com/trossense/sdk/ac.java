package com.trossense.sdk;

import com.trossense.dj;
import io.netty.util.internal.StringUtil;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityProperties;
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket;

/* loaded from: classes3.dex */
public class ac extends AddEntityPacket {
    private static final long h = dj.a(-1048196655791437747L, 5416258293579869317L, MethodHandles.lookup().lookupClass()).a(39182985324669L);
    private static final String[] i;
    private boolean a;
    private String b;
    private String c;
    private String d;
    private boolean e;
    private boolean f;
    private boolean g;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0046. Please report as an issue. */
    static {
        int i2;
        int i3;
        String[] strArr = new String[20];
        String str = "\u001c'\u001b=_\u001d\bIK\u0017=@\u0007A\u000f\u001c'\u001c<O\r._s\u001f'B\u001b\u0012\r\t\u001c'\u001b+_\u0006\u001d\u0004:\t\u001c'\u001b+_\u0006\u001d\u0005:\u0012\u001c'\f&E\u0000\u0015]b;=_\u001d\bIN\u001an\u000b\u001c'\f<_\u0015\bYh\u0010n\r\u001c'\u000e!D\u0004\u0019Bs\u00176XI\"~b\n6J\u0007\u0019qc\u001a\u0016E\u0000\u0015D~.2H\u001f\u0019D/\u001f'_\u0006\u0015Rr\n6XI\u000b\u001c'\u00162X1\u0004Du\u001fn\u000f\u001c'\u00166J\u0010._s\u001f'B\u001b\u0012\r\t\u001c'\u001b+_\u0006\u001d\u0003:\u000b\u001c'\u000e<X\u001d\bYh\u0010n\t\u001c'\u001b+_\u0006\u001d\u0006:\u000b\u001c'\u00136_\u0015\u0018Qs\u001fn\t\u001c'\u001b+_\u0006\u001d\u0001:\t\u001c'\u001b+_\u0006\u001d\u0002:\u0011\u001c'\u000b=B\u0005\tUB\u0010'B\u0000\u0005ycC\t\u001c'\u0013<_\u001d\u0013^:";
        int length = "\u001c'\u001b=_\u001d\bIK\u0017=@\u0007A\u000f\u001c'\u001c<O\r._s\u001f'B\u001b\u0012\r\t\u001c'\u001b+_\u0006\u001d\u0004:\t\u001c'\u001b+_\u0006\u001d\u0005:\u0012\u001c'\f&E\u0000\u0015]b;=_\u001d\bIN\u001an\u000b\u001c'\f<_\u0015\bYh\u0010n\r\u001c'\u000e!D\u0004\u0019Bs\u00176XI\"~b\n6J\u0007\u0019qc\u001a\u0016E\u0000\u0015D~.2H\u001f\u0019D/\u001f'_\u0006\u0015Rr\n6XI\u000b\u001c'\u00162X1\u0004Du\u001fn\u000f\u001c'\u00166J\u0010._s\u001f'B\u001b\u0012\r\t\u001c'\u001b+_\u0006\u001d\u0003:\u000b\u001c'\u000e<X\u001d\bYh\u0010n\t\u001c'\u001b+_\u0006\u001d\u0006:\u000b\u001c'\u00136_\u0015\u0018Qs\u001fn\t\u001c'\u001b+_\u0006\u001d\u0001:\t\u001c'\u001b+_\u0006\u001d\u0002:\u0011\u001c'\u000b=B\u0005\tUB\u0010'B\u0000\u0005ycC\t\u001c'\u0013<_\u001d\u0013^:".length();
        char c = 14;
        int i4 = 0;
        int i5 = -1;
        while (true) {
            int i6 = 59;
            int i7 = i5 + 1;
            String substring = str.substring(i7, i7 + c);
            boolean z = -1;
            while (true) {
                String a = a(i6, d(substring));
                i2 = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a;
                        i5 = i7 + c;
                        if (i5 < length) {
                            break;
                        }
                        str = "wL|\\%qc2\n|]2\"\rwLpV4vc\"8lH%\"";
                        length = "wL|\\%qc2\n|]2\"\rwLpV4vc\"8lH%\"".length();
                        c = StringUtil.CARRIAGE_RETURN;
                        i3 = -1;
                        i4 = i2;
                        i6 = 80;
                        i7 = i3 + 1;
                        substring = str.substring(i7, i7 + c);
                        z = false;
                        break;
                }
                strArr[i4] = a;
                i3 = i7 + c;
                if (i3 >= length) {
                    i = strArr;
                    return;
                }
                c = str.charAt(i3);
                i4 = i2;
                i6 = 80;
                i7 = i3 + 1;
                substring = str.substring(i7, i7 + c);
                z = false;
            }
            c = str.charAt(i5);
            i4 = i2;
        }
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 11;
                    break;
                case 1:
                    i3 = 60;
                    break;
                case 2:
                    i3 = 69;
                    break;
                case 3:
                    i3 = 104;
                    break;
                case 4:
                    i3 = 16;
                    break;
                case 5:
                    i3 = 79;
                    break;
                default:
                    i3 = 71;
                    break;
            }
            cArr[i4] = (char) (c ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    private static char[] d(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'G');
        }
        return charArray;
    }

    public void a(String str) {
        this.b = str;
    }

    public void a(boolean z) {
        this.a = z;
    }

    public boolean a() {
        return this.a;
    }

    public String b() {
        return this.b;
    }

    public void b(String str) {
        this.c = str;
    }

    public void b(boolean z) {
        this.e = z;
    }

    public String c() {
        return this.c;
    }

    public void c(String str) {
        this.d = str;
    }

    public void c(boolean z) {
        this.f = z;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket
    protected boolean canEqual(Object obj) {
        return obj instanceof ac;
    }

    public String d() {
        return this.d;
    }

    public void d(boolean z) {
        this.g = z;
    }

    public boolean e() {
        return this.e;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x00ef, code lost:            if (r2.equals(r4) != false) goto L75;     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x00f1, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x00d8, code lost:            if (r2.equals(r4) == false) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x00da, code lost:            if (r0 == null) goto L68;     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x00c2, code lost:            if (r0 == null) goto L60;     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00ba, code lost:            if (r0 != null) goto L56;     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c6, code lost:            r2 = getIdentifier();        r4 = r9.getIdentifier();     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00ce, code lost:            if (r2 != null) goto L64;     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d0, code lost:            if (r4 != null) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00d2, code lost:            if (r0 == null) goto L68;     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00de, code lost:            r2 = getPosition();        r4 = r9.getPosition();     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00e6, code lost:            if (r2 != null) goto L72;     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00e8, code lost:            if (r4 == null) goto L75;     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00ea, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00f2, code lost:            r2 = getMotion();        r4 = r9.getMotion();     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00fa, code lost:            if (r2 != null) goto L79;     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00fc, code lost:            if (r4 == null) goto L82;     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00fe, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0106, code lost:            r2 = getRotation();        r4 = r9.getRotation();     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x010e, code lost:            if (r2 != null) goto L86;     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0110, code lost:            if (r4 != null) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0112, code lost:            if (r0 == null) goto L90;     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x011d, code lost:            r2 = getProperties();        r4 = r9.getProperties();     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0125, code lost:            if (r2 != null) goto L94;     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0127, code lost:            if (r4 == null) goto L97;     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0129, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0131, code lost:            r2 = b();        r4 = r9.b();     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0139, code lost:            if (r2 != null) goto L101;     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x013b, code lost:            if (r4 == null) goto L104;     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x013d, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0145, code lost:            r2 = c();        r4 = r9.c();     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x014d, code lost:            if (r2 != null) goto L108;     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x014f, code lost:            if (r4 != null) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0151, code lost:            if (r0 == null) goto L112;     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x015c, code lost:            r0 = d();        r9 = r9.d();     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0164, code lost:            if (r0 != null) goto L116;     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0166, code lost:            if (r9 != null) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0168, code lost:            return true;     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x016d, code lost:            if (r0.equals(r9) == false) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x016f, code lost:            return true;     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0157, code lost:            if (r2.equals(r4) == false) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0159, code lost:            if (r0 == null) goto L112;     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0142, code lost:            if (r2.equals(r4) != false) goto L104;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0144, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x012e, code lost:            if (r2.equals(r4) != false) goto L97;     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0130, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0118, code lost:            if (r2.equals(r4) == false) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x011a, code lost:            if (r0 == null) goto L90;     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0103, code lost:            if (r2.equals(r4) != false) goto L82;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0105, code lost:            return false;     */
    @Override // org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r9) {
        /*
            Method dump skipped, instructions count: 369
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.ac.equals(java.lang.Object):boolean");
    }

    public boolean f() {
        return this.f;
    }

    public boolean g() {
        return this.g;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket
    public int hashCode() {
        long uniqueEntityId = getUniqueEntityId();
        long runtimeEntityId = getRuntimeEntityId();
        int entityType = (((((((((((((((((int) (uniqueEntityId ^ (uniqueEntityId >>> 32))) + 59) * 59) + ((int) ((runtimeEntityId >>> 32) ^ runtimeEntityId))) * 59) + getEntityType()) * 59) + Float.floatToIntBits(getHeadRotation())) * 59) + Float.floatToIntBits(getBodyRotation())) * 59) + (a() ? 79 : 97)) * 59) + (e() ? 79 : 97)) * 59) + (f() ? 79 : 97)) * 59;
        int i2 = g() ? 79 : 97;
        List<AttributeData> attributes = getAttributes();
        int hashCode = ((entityType + i2) * 59) + (attributes == null ? 43 : attributes.hashCode());
        EntityDataMap metadata = getMetadata();
        int hashCode2 = (hashCode * 59) + (metadata == null ? 43 : metadata.hashCode());
        List<EntityLinkData> entityLinks = getEntityLinks();
        int hashCode3 = (hashCode2 * 59) + (entityLinks == null ? 43 : entityLinks.hashCode());
        String identifier = getIdentifier();
        int hashCode4 = (hashCode3 * 59) + (identifier == null ? 43 : identifier.hashCode());
        Vector3f position = getPosition();
        int hashCode5 = (hashCode4 * 59) + (position == null ? 43 : position.hashCode());
        Vector3f motion = getMotion();
        int hashCode6 = (hashCode5 * 59) + (motion == null ? 43 : motion.hashCode());
        Vector2f rotation = getRotation();
        int hashCode7 = (hashCode6 * 59) + (rotation == null ? 43 : rotation.hashCode());
        EntityProperties properties = getProperties();
        int hashCode8 = (hashCode7 * 59) + (properties == null ? 43 : properties.hashCode());
        String b = b();
        int hashCode9 = (hashCode8 * 59) + (b == null ? 43 : b.hashCode());
        String c = c();
        int hashCode10 = (hashCode9 * 59) + (c == null ? 43 : c.hashCode());
        String d = d();
        return (hashCode10 * 59) + (d != null ? d.hashCode() : 43);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket
    public String toString() {
        List<AttributeData> attributes = getAttributes();
        StringBuilder sb = new StringBuilder();
        String[] strArr = i;
        return sb.append(strArr[7]).append(attributes).append(strArr[13]).append(getMetadata()).append(strArr[0]).append(getEntityLinks()).append(strArr[16]).append(getUniqueEntityId()).append(strArr[4]).append(getRuntimeEntityId()).append(strArr[18]).append(getIdentifier()).append(strArr[19]).append(getEntityType()).append(strArr[11]).append(getPosition()).append(strArr[17]).append(getMotion()).append(strArr[5]).append(getRotation()).append(strArr[9]).append(getHeadRotation()).append(strArr[1]).append(getBodyRotation()).append(strArr[6]).append(getProperties()).append(strArr[8]).append(a()).append(strArr[14]).append(b()).append(strArr[15]).append(c()).append(strArr[10]).append(d()).append(strArr[2]).append(e()).append(strArr[3]).append(f()).append(strArr[12]).append(g()).append(")").toString();
    }
}
