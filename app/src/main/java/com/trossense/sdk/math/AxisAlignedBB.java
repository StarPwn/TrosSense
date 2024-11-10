package com.trossense.sdk.math;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class AxisAlignedBB {
    private static final long a = dj.a(-5870887208175271404L, 2442362445040464618L, MethodHandles.lookup().lookupClass()).a(153948986474464L);
    private static final String[] b;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    public final double minX;
    public final double minY;
    public final double minZ;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "H~+1dK4\u0013%&/#]~`\u00030#4^Pr\t7(\b!\u0007H~+9rK4\u0007H~+1dJ4";
        int length = "H~+1dK4\u0013%&/#]~`\u00030#4^Pr\t7(\b!\u0007H~+9rK4\u0007H~+1dJ4".length();
        char c = 7;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 88;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a2;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "lZ\u000f\u0015@l\u0010\u0007lZ\u000f\u001dVl\u0010";
                        length = "lZ\u000f\u0015@l\u0010\u0007lZ\u000f\u001dVl\u0010".length();
                        c = 7;
                        i2 = -1;
                        i3 = i;
                        i5 = 124;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a2;
                i2 = i6 + c;
                if (i2 >= length) {
                    b = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 124;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    public AxisAlignedBB(double d, double d2, double d3, double d4, double d5, double d6) {
        this.minX = Math.min(d, d4);
        this.minY = Math.min(d2, d5);
        this.minZ = Math.min(d3, d6);
        this.maxX = Math.max(d, d4);
        this.maxY = Math.max(d2, d5);
        this.maxZ = Math.max(d3, d6);
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 60;
                    break;
                case 1:
                    i2 = 6;
                    break;
                case 2:
                    i2 = 30;
                    break;
                case 3:
                    i2 = 8;
                    break;
                case 4:
                    i2 = 68;
                    break;
                case 5:
                    i2 = 74;
                    break;
                default:
                    i2 = 81;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'Q');
        }
        return charArray;
    }

    public static AxisAlignedBB fromBounds(double d, double d2, double d3, double d4, double d5, double d6) {
        return new AxisAlignedBB(Math.min(d, d4), Math.min(d2, d5), Math.min(d3, d6), Math.max(d, d4), Math.max(d2, d5), Math.max(d3, d6));
    }

    private boolean isVecInXY(Vec3f vec3f) {
        return vec3f != null && ((double) vec3f.x) >= this.minX && ((double) vec3f.x) <= this.maxX && ((double) vec3f.y) >= this.minY && ((double) vec3f.y) <= this.maxY;
    }

    private boolean isVecInXZ(Vec3f vec3f) {
        return vec3f != null && ((double) vec3f.x) >= this.minX && ((double) vec3f.x) <= this.maxX && ((double) vec3f.z) >= this.minZ && ((double) vec3f.z) <= this.maxZ;
    }

    private boolean isVecInYZ(Vec3f vec3f) {
        return vec3f != null && ((double) vec3f.y) >= this.minY && ((double) vec3f.y) <= this.maxY && ((double) vec3f.z) >= this.minZ && ((double) vec3f.z) <= this.maxZ;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0046, code lost:            if (r3 == null) goto L21;     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0034, code lost:            if (r3 == null) goto L14;     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.trossense.sdk.math.AxisAlignedBB addCoord(double r30, double r32, double r34) {
        /*
            r29 = this;
            r0 = r29
            double r1 = r0.minX
            int[] r3 = com.trossense.sdk.math.Vec3f.b()
            double r4 = r0.minY
            double r6 = r0.minZ
            double r8 = r0.maxX
            double r10 = r0.maxY
            double r12 = r0.maxZ
            r14 = 0
            int r16 = (r30 > r14 ? 1 : (r30 == r14 ? 0 : -1))
            if (r16 >= 0) goto L24
            double r1 = r1 + r30
            if (r3 != 0) goto L2a
            r14 = 1
            int[] r14 = new int[r14]
            com.trossense.sdk.PointerHolder.b(r14)
            r14 = 0
        L24:
            int r16 = (r30 > r14 ? 1 : (r30 == r14 ? 0 : -1))
            if (r16 <= 0) goto L2a
            double r8 = r8 + r30
        L2a:
            r17 = r1
            r23 = r8
            int r1 = (r32 > r14 ? 1 : (r32 == r14 ? 0 : -1))
            if (r1 >= 0) goto L36
            double r4 = r4 + r32
            if (r3 != 0) goto L3c
        L36:
            int r1 = (r32 > r14 ? 1 : (r32 == r14 ? 0 : -1))
            if (r1 <= 0) goto L3c
            double r10 = r10 + r32
        L3c:
            r19 = r4
            r25 = r10
            int r1 = (r34 > r14 ? 1 : (r34 == r14 ? 0 : -1))
            if (r1 >= 0) goto L48
            double r6 = r6 + r34
            if (r3 != 0) goto L4e
        L48:
            int r1 = (r34 > r14 ? 1 : (r34 == r14 ? 0 : -1))
            if (r1 <= 0) goto L4e
            double r12 = r12 + r34
        L4e:
            r21 = r6
            r27 = r12
            com.trossense.sdk.math.AxisAlignedBB r1 = new com.trossense.sdk.math.AxisAlignedBB
            r16 = r1
            r16.<init>(r17, r19, r21, r23, r25, r27)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.math.AxisAlignedBB.addCoord(double, double, double):com.trossense.sdk.math.AxisAlignedBB");
    }

    public double calculateXOffset(AxisAlignedBB axisAlignedBB, double d) {
        if (axisAlignedBB.maxY <= this.minY || axisAlignedBB.minY >= this.maxY || axisAlignedBB.maxZ <= this.minZ || axisAlignedBB.minZ >= this.maxZ) {
            return d;
        }
        if (d > 0.0d) {
            double d2 = axisAlignedBB.maxX;
            double d3 = this.minX;
            if (d2 <= d3) {
                double d4 = d3 - d2;
                return d4 < d ? d4 : d;
            }
        }
        if (d >= 0.0d) {
            return d;
        }
        double d5 = axisAlignedBB.minX;
        double d6 = this.maxX;
        if (d5 < d6) {
            return d;
        }
        double d7 = d6 - d5;
        return d7 > d ? d7 : d;
    }

    public double calculateYOffset(AxisAlignedBB axisAlignedBB, double d) {
        if (axisAlignedBB.maxX <= this.minX || axisAlignedBB.minX >= this.maxX || axisAlignedBB.maxZ <= this.minZ || axisAlignedBB.minZ >= this.maxZ) {
            return d;
        }
        if (d > 0.0d) {
            double d2 = axisAlignedBB.maxY;
            double d3 = this.minY;
            if (d2 <= d3) {
                double d4 = d3 - d2;
                return d4 < d ? d4 : d;
            }
        }
        if (d >= 0.0d) {
            return d;
        }
        double d5 = axisAlignedBB.minY;
        double d6 = this.maxY;
        if (d5 < d6) {
            return d;
        }
        double d7 = d6 - d5;
        return d7 > d ? d7 : d;
    }

    public double calculateZOffset(AxisAlignedBB axisAlignedBB, double d) {
        if (axisAlignedBB.maxX <= this.minX || axisAlignedBB.minX >= this.maxX || axisAlignedBB.maxY <= this.minY || axisAlignedBB.minY >= this.maxY) {
            return d;
        }
        if (d > 0.0d) {
            double d2 = axisAlignedBB.maxZ;
            double d3 = this.minZ;
            if (d2 <= d3) {
                double d4 = d3 - d2;
                return d4 < d ? d4 : d;
            }
        }
        if (d >= 0.0d) {
            return d;
        }
        double d5 = axisAlignedBB.minZ;
        double d6 = this.maxZ;
        if (d5 < d6) {
            return d;
        }
        double d7 = d6 - d5;
        return d7 > d ? d7 : d;
    }

    public AxisAlignedBB contract(double d, double d2, double d3) {
        return new AxisAlignedBB(this.minX + d, this.minY + d2, this.minZ + d3, this.maxX - d, this.maxY - d2, this.maxZ - d3);
    }

    public AxisAlignedBB expand(double d, double d2, double d3) {
        return new AxisAlignedBB(this.minX - d, this.minY - d2, this.minZ - d3, this.maxX + d, this.maxY + d2, this.maxZ + d3);
    }

    public double getAverageEdgeLength() {
        return (((this.maxX - this.minX) + (this.maxY - this.minY)) + (this.maxZ - this.minZ)) / 3.0d;
    }

    public boolean intersectsWith(AxisAlignedBB axisAlignedBB) {
        return axisAlignedBB.maxX > this.minX && axisAlignedBB.minX < this.maxX && axisAlignedBB.maxY > this.minY && axisAlignedBB.minY < this.maxY && axisAlignedBB.maxZ > this.minZ && axisAlignedBB.minZ < this.maxZ;
    }

    public boolean isVecInside(Vec3f vec3f) {
        return ((double) vec3f.x) > this.minX && ((double) vec3f.x) < this.maxX && ((double) vec3f.y) > this.minY && ((double) vec3f.y) < this.maxY && ((double) vec3f.z) > this.minZ && ((double) vec3f.z) < this.maxZ;
    }

    public AxisAlignedBB offset(double d, double d2, double d3) {
        return new AxisAlignedBB(this.minX + d, this.minY + d2, this.minZ + d3, this.maxX + d, this.maxY + d2, this.maxZ + d3);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] strArr = b;
        return sb.append(strArr[1]).append(this.minX).append(strArr[2]).append(this.minY).append(strArr[5]).append(this.minZ).append(strArr[3]).append(this.maxX).append(strArr[0]).append(this.maxY).append(strArr[4]).append(this.maxZ).append('}').toString();
    }

    public AxisAlignedBB union(AxisAlignedBB axisAlignedBB) {
        return new AxisAlignedBB(Math.min(this.minX, axisAlignedBB.minX), Math.min(this.minY, axisAlignedBB.minY), Math.min(this.minZ, axisAlignedBB.minZ), Math.max(this.maxX, axisAlignedBB.maxX), Math.max(this.maxY, axisAlignedBB.maxY), Math.max(this.maxZ, axisAlignedBB.maxZ));
    }
}
