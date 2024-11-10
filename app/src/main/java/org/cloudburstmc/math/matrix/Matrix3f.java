package org.cloudburstmc.math.matrix;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Complexf;
import org.cloudburstmc.math.imaginary.Quaternionf;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class Matrix3f implements Matrixf, Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    private final float m00;
    private final float m01;
    private final float m02;
    private final float m10;
    private final float m11;
    private final float m12;
    private final float m20;
    private final float m21;
    private final float m22;
    public static final Matrix3f ZERO = new Matrix3f(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    public static final Matrix3f IDENTITY = new Matrix3f(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    private Matrix3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.RegionMaker.calcSwitchOut(RegionMaker.java:923)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:797)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processFallThroughCases(RegionMaker.java:841)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:800)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    /* JADX WARN: Failed to find 'out' block for switch in B:1:0x0000. Please report as an issue. */
    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float get(int r6, int r7) {
        /*
            r5 = this;
            switch(r6) {
                case 0: goto L4;
                case 1: goto L11;
                case 2: goto L1e;
                default: goto L3;
            }
        L3:
            goto L2b
        L4:
            switch(r7) {
                case 0: goto Le;
                case 1: goto Lb;
                case 2: goto L8;
                default: goto L7;
            }
        L7:
            goto L11
        L8:
            float r0 = r5.m02
            return r0
        Lb:
            float r0 = r5.m01
            return r0
        Le:
            float r0 = r5.m00
            return r0
        L11:
            switch(r7) {
                case 0: goto L1b;
                case 1: goto L18;
                case 2: goto L15;
                default: goto L14;
            }
        L14:
            goto L1e
        L15:
            float r0 = r5.m12
            return r0
        L18:
            float r0 = r5.m11
            return r0
        L1b:
            float r0 = r5.m10
            return r0
        L1e:
            switch(r7) {
                case 0: goto L28;
                case 1: goto L25;
                case 2: goto L22;
                default: goto L21;
            }
        L21:
            goto L2b
        L22:
            float r0 = r5.m22
            return r0
        L25:
            float r0 = r5.m21
            return r0
        L28:
            float r0 = r5.m20
            return r0
        L2b:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r3 = 2
            if (r6 < 0) goto L3c
            if (r6 <= r3) goto L3a
            goto L3c
        L3a:
            r4 = r2
            goto L3e
        L3c:
            java.lang.String r4 = "row must be greater than zero and smaller than 3. "
        L3e:
            java.lang.StringBuilder r1 = r1.append(r4)
            if (r7 < 0) goto L46
            if (r7 <= r3) goto L48
        L46:
            java.lang.String r2 = "col must be greater than zero and smaller than 3."
        L48:
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.math.matrix.Matrix3f.get(int, int):float");
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Vector3f getRow(int row) {
        return Vector3f.from(get(row, 0), get(row, 1), get(row, 2));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Vector3f getColumn(int col) {
        return Vector3f.from(get(0, col), get(1, col), get(2, col));
    }

    @Nonnull
    public Matrix3f add(Matrix3f m) {
        return from(this.m00 + m.m00, this.m01 + m.m01, this.m02 + m.m02, this.m10 + m.m10, this.m11 + m.m11, this.m12 + m.m12, this.m20 + m.m20, this.m21 + m.m21, this.m22 + m.m22);
    }

    @Nonnull
    public Matrix3f sub(Matrix3f m) {
        return from(this.m00 - m.m00, this.m01 - m.m01, this.m02 - m.m02, this.m10 - m.m10, this.m11 - m.m11, this.m12 - m.m12, this.m20 - m.m20, this.m21 - m.m21, this.m22 - m.m22);
    }

    @Nonnull
    public Matrix3f mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f mul(float a) {
        return from(this.m00 * a, this.m01 * a, this.m02 * a, this.m10 * a, this.m11 * a, this.m12 * a, this.m20 * a, this.m21 * a, this.m22 * a);
    }

    @Nonnull
    public Matrix3f mul(Matrix3f m) {
        return from((this.m00 * m.m00) + (this.m01 * m.m10) + (this.m02 * m.m20), (this.m00 * m.m01) + (this.m01 * m.m11) + (this.m02 * m.m21), (this.m00 * m.m02) + (this.m01 * m.m12) + (this.m02 * m.m22), (this.m10 * m.m00) + (this.m11 * m.m10) + (this.m12 * m.m20), (this.m10 * m.m01) + (this.m11 * m.m11) + (this.m12 * m.m21), (this.m10 * m.m02) + (this.m11 * m.m12) + (this.m12 * m.m22), (this.m20 * m.m00) + (this.m21 * m.m10) + (this.m22 * m.m20), (this.m20 * m.m01) + (this.m21 * m.m11) + (this.m22 * m.m21), (this.m20 * m.m02) + (this.m21 * m.m12) + (this.m22 * m.m22));
    }

    @Nonnull
    public Matrix3f div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f div(float a) {
        return from(this.m00 / a, this.m01 / a, this.m02 / a, this.m10 / a, this.m11 / a, this.m12 / a, this.m20 / a, this.m21 / a, this.m22 / a);
    }

    @Nonnull
    public Matrix3f div(Matrix3f m) {
        return mul(m.invert());
    }

    @Nonnull
    public Matrix3f pow(double pow) {
        return pow((float) pow);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f pow(float pow) {
        return from(Math.pow(this.m00, pow), Math.pow(this.m01, pow), Math.pow(this.m02, pow), Math.pow(this.m10, pow), Math.pow(this.m11, pow), Math.pow(this.m12, pow), Math.pow(this.m20, pow), Math.pow(this.m21, pow), Math.pow(this.m22, pow));
    }

    @Nonnull
    public Matrix3f translate(Vector2f v) {
        return translate(v.getX(), v.getY());
    }

    @Nonnull
    public Matrix3f translate(double x, double y) {
        return translate((float) x, (float) y);
    }

    @Nonnull
    public Matrix3f translate(float x, float y) {
        return createTranslation(x, y).mul(this);
    }

    @Nonnull
    public Matrix3f scale(double scale) {
        return scale((float) scale);
    }

    @Nonnull
    public Matrix3f scale(float scale) {
        return scale(scale, scale, scale);
    }

    @Nonnull
    public Matrix3f scale(Vector3f v) {
        return scale(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Matrix3f scale(double x, double y, double z) {
        return scale((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Matrix3f scale(float x, float y, float z) {
        return createScaling(x, y, z).mul(this);
    }

    @Nonnull
    public Matrix3f rotate(Complexf rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Matrix3f rotate(Quaternionf rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Vector3f transform(Vector3f v) {
        return transform(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3f transform(double x, double y, double z) {
        return transform((float) x, (float) y, (float) z);
    }

    @Nonnull
    public Vector3f transform(float x, float y, float z) {
        return Vector3f.from((this.m00 * x) + (this.m01 * y) + (this.m02 * z), (this.m10 * x) + (this.m11 * y) + (this.m12 * z), (this.m20 * x) + (this.m21 * y) + (this.m22 * z));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f floor() {
        return from(GenericMath.floor(this.m00), GenericMath.floor(this.m01), GenericMath.floor(this.m02), GenericMath.floor(this.m10), GenericMath.floor(this.m11), GenericMath.floor(this.m12), GenericMath.floor(this.m20), GenericMath.floor(this.m21), GenericMath.floor(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f ceil() {
        return from(Math.ceil(this.m00), Math.ceil(this.m01), Math.ceil(this.m02), Math.ceil(this.m10), Math.ceil(this.m11), Math.ceil(this.m12), Math.ceil(this.m20), Math.ceil(this.m21), Math.ceil(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f round() {
        return from(Math.round(this.m00), Math.round(this.m01), Math.round(this.m02), Math.round(this.m10), Math.round(this.m11), Math.round(this.m12), Math.round(this.m20), Math.round(this.m21), Math.round(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f abs() {
        return from(Math.abs(this.m00), Math.abs(this.m01), Math.abs(this.m02), Math.abs(this.m10), Math.abs(this.m11), Math.abs(this.m12), Math.abs(this.m20), Math.abs(this.m21), Math.abs(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f negate() {
        return from(-this.m00, -this.m01, -this.m02, -this.m10, -this.m11, -this.m12, -this.m20, -this.m21, -this.m22);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f transpose() {
        return from(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float trace() {
        return this.m00 + this.m11 + this.m22;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float determinant() {
        return ((this.m00 * ((this.m11 * this.m22) - (this.m12 * this.m21))) - (this.m01 * ((this.m10 * this.m22) - (this.m12 * this.m20)))) + (this.m02 * ((this.m10 * this.m21) - (this.m11 * this.m20)));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f invert() {
        float det = determinant();
        if (Math.abs(det) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot inverse a matrix with a zero determinant");
        }
        return from(((this.m11 * this.m22) - (this.m21 * this.m12)) / det, (-((this.m01 * this.m22) - (this.m21 * this.m02))) / det, ((this.m01 * this.m12) - (this.m02 * this.m11)) / det, (-((this.m10 * this.m22) - (this.m20 * this.m12))) / det, ((this.m00 * this.m22) - (this.m20 * this.m02)) / det, (-((this.m00 * this.m12) - (this.m10 * this.m02))) / det, ((this.m10 * this.m21) - (this.m20 * this.m11)) / det, (-((this.m00 * this.m21) - (this.m20 * this.m01))) / det, ((this.m00 * this.m11) - (this.m01 * this.m10)) / det);
    }

    @Nonnull
    public Matrix2f toMatrix2() {
        return Matrix2f.from(this);
    }

    @Nonnull
    public Matrix4f toMatrix4() {
        return Matrix4f.from(this);
    }

    @Nonnull
    public MatrixNf toMatrixN() {
        return MatrixNf.from(this);
    }

    @Nonnull
    public float[] toArray() {
        return toArray(false);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public float[] toArray(boolean columnMajor) {
        if (columnMajor) {
            return new float[]{this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22};
        }
        return new float[]{this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22};
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3f toFloat() {
        return from(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix3d toDouble() {
        return Matrix3d.from(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
    }

    @Nonnull
    public String toString() {
        return this.m00 + " " + this.m01 + " " + this.m02 + "\n" + this.m10 + " " + this.m11 + " " + this.m12 + "\n" + this.m20 + " " + this.m21 + " " + this.m22 + "\n";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix3f)) {
            return false;
        }
        Matrix3f matrix3 = (Matrix3f) o;
        return Float.compare(matrix3.m00, this.m00) == 0 && Float.compare(matrix3.m01, this.m01) == 0 && Float.compare(matrix3.m02, this.m02) == 0 && Float.compare(matrix3.m10, this.m10) == 0 && Float.compare(matrix3.m11, this.m11) == 0 && Float.compare(matrix3.m12, this.m12) == 0 && Float.compare(matrix3.m20, this.m20) == 0 && Float.compare(matrix3.m21, this.m21) == 0 && Float.compare(matrix3.m22, this.m22) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.m00 != 0.0f ? Float.hashCode(this.m00) : 0;
            this.hashCode = (((((((((((((((result * 31) + (this.m01 != 0.0f ? Float.hashCode(this.m01) : 0)) * 31) + (this.m02 != 0.0f ? Float.hashCode(this.m02) : 0)) * 31) + (this.m10 != 0.0f ? Float.hashCode(this.m10) : 0)) * 31) + (this.m11 != 0.0f ? Float.hashCode(this.m11) : 0)) * 31) + (this.m12 != 0.0f ? Float.hashCode(this.m12) : 0)) * 31) + (this.m20 != 0.0f ? Float.hashCode(this.m20) : 0)) * 31) + (this.m21 != 0.0f ? Float.hashCode(this.m21) : 0)) * 31) + (this.m22 != 0.0f ? Float.hashCode(this.m22) : 0);
            this.hashed = true;
        }
        return this.hashCode;
    }

    @Nonnull
    public Matrix3f clone() {
        return from(this);
    }

    @Nonnull
    public static Matrix3f from(float n) {
        return n == 0.0f ? ZERO : new Matrix3f(n, n, n, n, n, n, n, n, n);
    }

    @Nonnull
    public static Matrix3f from(Matrix2f m) {
        return from(m.get(0, 0), m.get(0, 1), 0.0f, m.get(1, 0), m.get(1, 1), 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Nonnull
    public static Matrix3f from(Matrix3f m) {
        return from(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
    }

    @Nonnull
    public static Matrix3f from(Matrix4f m) {
        return from(m.get(0, 0), m.get(0, 1), m.get(0, 2), m.get(1, 0), m.get(1, 1), m.get(1, 2), m.get(2, 0), m.get(2, 1), m.get(2, 2));
    }

    @Nonnull
    public static Matrix3f from(MatrixNf m) {
        float m20;
        float m21;
        float m02;
        float m22;
        float m12;
        float m00 = m.get(0, 0);
        float m01 = m.get(0, 1);
        float m10 = m.get(1, 0);
        float m11 = m.get(1, 1);
        if (m.size() > 2) {
            float m022 = m.get(0, 2);
            float m122 = m.get(1, 2);
            m20 = m.get(2, 0);
            float m212 = m.get(2, 1);
            m21 = m212;
            m02 = m022;
            m22 = m.get(2, 2);
            m12 = m122;
        } else {
            m20 = 0.0f;
            m21 = 0.0f;
            m02 = 0.0f;
            m22 = 0.0f;
            m12 = 0.0f;
        }
        return from(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Nonnull
    public static Matrix3f from(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        return from((float) m00, (float) m01, (float) m02, (float) m10, (float) m11, (float) m12, (float) m20, (float) m21, (float) m22);
    }

    @Nonnull
    public static Matrix3f from(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        return (m00 == 0.0f && m01 == 0.0f && m02 == 0.0f && m10 == 0.0f && m11 == 0.0f && m12 == 0.0f && m20 == 0.0f && m21 == 0.0f && m22 == 0.0f) ? ZERO : new Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Nonnull
    public static Matrix3f fromDiagonal(float m00, float m11, float m22) {
        return (m00 == 0.0f && m11 == 0.0f && m22 == 0.0f) ? ZERO : new Matrix3f(m00, 0.0f, 0.0f, 0.0f, m11, 0.0f, 0.0f, 0.0f, m22);
    }

    @Nonnull
    public static Matrix3f createScaling(double scale) {
        return createScaling((float) scale);
    }

    @Nonnull
    public static Matrix3f createScaling(float scale) {
        return createScaling(scale, scale, scale);
    }

    @Nonnull
    public static Matrix3f createScaling(Vector3f v) {
        return createScaling(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Matrix3f createScaling(double x, double y, double z) {
        return createScaling((float) x, (float) y, (float) z);
    }

    @Nonnull
    public static Matrix3f createScaling(float x, float y, float z) {
        return from(x, 0.0f, 0.0f, 0.0f, y, 0.0f, 0.0f, 0.0f, z);
    }

    @Nonnull
    public static Matrix3f createTranslation(Vector2f v) {
        return createTranslation(v.getX(), v.getY());
    }

    @Nonnull
    public static Matrix3f createTranslation(double x, double y) {
        return createTranslation((float) x, (float) y);
    }

    @Nonnull
    public static Matrix3f createTranslation(float x, float y) {
        return from(1.0f, 0.0f, x, 0.0f, 1.0f, y, 0.0f, 0.0f, 1.0f);
    }

    @Nonnull
    public static Matrix3f createRotation(Complexf rot) {
        Complexf rot2 = rot.normalize();
        return from(rot2.getX(), -rot2.getY(), 0.0f, rot2.getY(), rot2.getX(), 0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Nonnull
    public static Matrix3f createRotation(Quaternionf rot) {
        Quaternionf rot2 = rot.normalize();
        return from((1.0f - ((rot2.getY() * 2.0f) * rot2.getY())) - ((rot2.getZ() * 2.0f) * rot2.getZ()), ((rot2.getX() * 2.0f) * rot2.getY()) - ((rot2.getW() * 2.0f) * rot2.getZ()), (rot2.getX() * 2.0f * rot2.getZ()) + (rot2.getW() * 2.0f * rot2.getY()), (rot2.getX() * 2.0f * rot2.getY()) + (rot2.getW() * 2.0f * rot2.getZ()), (1.0f - ((rot2.getX() * 2.0f) * rot2.getX())) - ((rot2.getZ() * 2.0f) * rot2.getZ()), ((rot2.getY() * 2.0f) * rot2.getZ()) - ((rot2.getW() * 2.0f) * rot2.getX()), ((rot2.getX() * 2.0f) * rot2.getZ()) - ((rot2.getW() * 2.0f) * rot2.getY()), (rot2.getY() * 2.0f * rot2.getZ()) + (rot2.getX() * 2.0f * rot2.getW()), (1.0f - ((rot2.getX() * 2.0f) * rot2.getX())) - ((rot2.getY() * 2.0f) * rot2.getY()));
    }
}
