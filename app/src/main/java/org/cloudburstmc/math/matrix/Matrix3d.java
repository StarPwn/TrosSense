package org.cloudburstmc.math.matrix;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Complexd;
import org.cloudburstmc.math.imaginary.Quaterniond;
import org.cloudburstmc.math.vector.Vector2d;
import org.cloudburstmc.math.vector.Vector3d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class Matrix3d implements Matrixd, Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    private final double m00;
    private final double m01;
    private final double m02;
    private final double m10;
    private final double m11;
    private final double m12;
    private final double m20;
    private final double m21;
    private final double m22;
    public static final Matrix3d ZERO = new Matrix3d(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
    public static final Matrix3d IDENTITY = new Matrix3d(1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d);
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    private Matrix3d(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
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
    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double get(int r6, int r7) {
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
            double r0 = r5.m02
            return r0
        Lb:
            double r0 = r5.m01
            return r0
        Le:
            double r0 = r5.m00
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
            double r0 = r5.m12
            return r0
        L18:
            double r0 = r5.m11
            return r0
        L1b:
            double r0 = r5.m10
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
            double r0 = r5.m22
            return r0
        L25:
            double r0 = r5.m21
            return r0
        L28:
            double r0 = r5.m20
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
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.math.matrix.Matrix3d.get(int, int):double");
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Vector3d getRow(int row) {
        return Vector3d.from(get(row, 0), get(row, 1), get(row, 2));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Vector3d getColumn(int col) {
        return Vector3d.from(get(0, col), get(1, col), get(2, col));
    }

    @Nonnull
    public Matrix3d add(Matrix3d m) {
        return from(this.m00 + m.m00, this.m01 + m.m01, this.m02 + m.m02, this.m10 + m.m10, this.m11 + m.m11, this.m12 + m.m12, this.m20 + m.m20, this.m21 + m.m21, this.m22 + m.m22);
    }

    @Nonnull
    public Matrix3d sub(Matrix3d m) {
        return from(this.m00 - m.m00, this.m01 - m.m01, this.m02 - m.m02, this.m10 - m.m10, this.m11 - m.m11, this.m12 - m.m12, this.m20 - m.m20, this.m21 - m.m21, this.m22 - m.m22);
    }

    @Nonnull
    public Matrix3d mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d mul(double a) {
        return from(this.m00 * a, this.m01 * a, this.m02 * a, this.m10 * a, this.m11 * a, this.m12 * a, this.m20 * a, this.m21 * a, this.m22 * a);
    }

    @Nonnull
    public Matrix3d mul(Matrix3d m) {
        return from((this.m00 * m.m00) + (this.m01 * m.m10) + (this.m02 * m.m20), (this.m00 * m.m01) + (this.m01 * m.m11) + (this.m02 * m.m21), (this.m00 * m.m02) + (this.m01 * m.m12) + (this.m02 * m.m22), (this.m10 * m.m00) + (this.m11 * m.m10) + (this.m12 * m.m20), (this.m10 * m.m01) + (this.m11 * m.m11) + (this.m12 * m.m21), (this.m10 * m.m02) + (this.m11 * m.m12) + (this.m12 * m.m22), (this.m20 * m.m00) + (this.m21 * m.m10) + (this.m22 * m.m20), (this.m20 * m.m01) + (this.m21 * m.m11) + (this.m22 * m.m21), (this.m20 * m.m02) + (this.m21 * m.m12) + (this.m22 * m.m22));
    }

    @Nonnull
    public Matrix3d div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d div(double a) {
        return from(this.m00 / a, this.m01 / a, this.m02 / a, this.m10 / a, this.m11 / a, this.m12 / a, this.m20 / a, this.m21 / a, this.m22 / a);
    }

    @Nonnull
    public Matrix3d div(Matrix3d m) {
        return mul(m.invert());
    }

    @Nonnull
    public Matrix3d pow(float pow) {
        return pow(pow);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d pow(double pow) {
        return from(Math.pow(this.m00, pow), Math.pow(this.m01, pow), Math.pow(this.m02, pow), Math.pow(this.m10, pow), Math.pow(this.m11, pow), Math.pow(this.m12, pow), Math.pow(this.m20, pow), Math.pow(this.m21, pow), Math.pow(this.m22, pow));
    }

    @Nonnull
    public Matrix3d translate(Vector2d v) {
        return translate(v.getX(), v.getY());
    }

    @Nonnull
    public Matrix3d translate(float x, float y) {
        return translate(x, y);
    }

    @Nonnull
    public Matrix3d translate(double x, double y) {
        return createTranslation(x, y).mul(this);
    }

    @Nonnull
    public Matrix3d scale(float scale) {
        return scale(scale);
    }

    @Nonnull
    public Matrix3d scale(double scale) {
        return scale(scale, scale, scale);
    }

    @Nonnull
    public Matrix3d scale(Vector3d v) {
        return scale(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Matrix3d scale(float x, float y, float z) {
        return scale(x, y, z);
    }

    @Nonnull
    public Matrix3d scale(double x, double y, double z) {
        return createScaling(x, y, z).mul(this);
    }

    @Nonnull
    public Matrix3d rotate(Complexd rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Matrix3d rotate(Quaterniond rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Vector3d transform(Vector3d v) {
        return transform(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Vector3d transform(float x, float y, float z) {
        return transform(x, y, z);
    }

    @Nonnull
    public Vector3d transform(double x, double y, double z) {
        return Vector3d.from((this.m00 * x) + (this.m01 * y) + (this.m02 * z), (this.m10 * x) + (this.m11 * y) + (this.m12 * z), (this.m20 * x) + (this.m21 * y) + (this.m22 * z));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d floor() {
        return from(GenericMath.floor(this.m00), GenericMath.floor(this.m01), GenericMath.floor(this.m02), GenericMath.floor(this.m10), GenericMath.floor(this.m11), GenericMath.floor(this.m12), GenericMath.floor(this.m20), GenericMath.floor(this.m21), GenericMath.floor(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d ceil() {
        return from(Math.ceil(this.m00), Math.ceil(this.m01), Math.ceil(this.m02), Math.ceil(this.m10), Math.ceil(this.m11), Math.ceil(this.m12), Math.ceil(this.m20), Math.ceil(this.m21), Math.ceil(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d round() {
        return from((float) Math.round(this.m00), (float) Math.round(this.m01), (float) Math.round(this.m02), (float) Math.round(this.m10), (float) Math.round(this.m11), (float) Math.round(this.m12), (float) Math.round(this.m20), (float) Math.round(this.m21), (float) Math.round(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d abs() {
        return from(Math.abs(this.m00), Math.abs(this.m01), Math.abs(this.m02), Math.abs(this.m10), Math.abs(this.m11), Math.abs(this.m12), Math.abs(this.m20), Math.abs(this.m21), Math.abs(this.m22));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d negate() {
        return from(-this.m00, -this.m01, -this.m02, -this.m10, -this.m11, -this.m12, -this.m20, -this.m21, -this.m22);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d transpose() {
        return from(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double trace() {
        return this.m00 + this.m11 + this.m22;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double determinant() {
        return ((this.m00 * ((this.m11 * this.m22) - (this.m12 * this.m21))) - (this.m01 * ((this.m10 * this.m22) - (this.m12 * this.m20)))) + (this.m02 * ((this.m10 * this.m21) - (this.m11 * this.m20)));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d invert() {
        double det = determinant();
        if (Math.abs(det) >= GenericMath.DBL_EPSILON) {
            return from(((this.m11 * this.m22) - (this.m21 * this.m12)) / det, (-((this.m01 * this.m22) - (this.m21 * this.m02))) / det, ((this.m01 * this.m12) - (this.m02 * this.m11)) / det, (-((this.m10 * this.m22) - (this.m20 * this.m12))) / det, ((this.m00 * this.m22) - (this.m20 * this.m02)) / det, (-((this.m00 * this.m12) - (this.m10 * this.m02))) / det, ((this.m10 * this.m21) - (this.m20 * this.m11)) / det, (-((this.m00 * this.m21) - (this.m20 * this.m01))) / det, ((this.m00 * this.m11) - (this.m01 * this.m10)) / det);
        }
        throw new ArithmeticException("Cannot inverse a matrix with a zero determinant");
    }

    @Nonnull
    public Matrix2d toMatrix2() {
        return Matrix2d.from(this);
    }

    @Nonnull
    public Matrix4d toMatrix4() {
        return Matrix4d.from(this);
    }

    @Nonnull
    public MatrixNd toMatrixN() {
        return MatrixNd.from(this);
    }

    @Nonnull
    public double[] toArray() {
        return toArray(false);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public double[] toArray(boolean columnMajor) {
        if (columnMajor) {
            return new double[]{this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22};
        }
        return new double[]{this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22};
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3f toFloat() {
        return Matrix3f.from(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix3d toDouble() {
        return from(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
    }

    @Nonnull
    public String toString() {
        return this.m00 + " " + this.m01 + " " + this.m02 + "\n" + this.m10 + " " + this.m11 + " " + this.m12 + "\n" + this.m20 + " " + this.m21 + " " + this.m22 + "\n";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix3d)) {
            return false;
        }
        Matrix3d matrix3 = (Matrix3d) o;
        return Double.compare(matrix3.m00, this.m00) == 0 && Double.compare(matrix3.m01, this.m01) == 0 && Double.compare(matrix3.m02, this.m02) == 0 && Double.compare(matrix3.m10, this.m10) == 0 && Double.compare(matrix3.m11, this.m11) == 0 && Double.compare(matrix3.m12, this.m12) == 0 && Double.compare(matrix3.m20, this.m20) == 0 && Double.compare(matrix3.m21, this.m21) == 0 && Double.compare(matrix3.m22, this.m22) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.m00 != 0.0d ? Double.hashCode(this.m00) : 0;
            this.hashCode = (((((((((((((((result * 31) + (this.m01 != 0.0d ? Double.hashCode(this.m01) : 0)) * 31) + (this.m02 != 0.0d ? Double.hashCode(this.m02) : 0)) * 31) + (this.m10 != 0.0d ? Double.hashCode(this.m10) : 0)) * 31) + (this.m11 != 0.0d ? Double.hashCode(this.m11) : 0)) * 31) + (this.m12 != 0.0d ? Double.hashCode(this.m12) : 0)) * 31) + (this.m20 != 0.0d ? Double.hashCode(this.m20) : 0)) * 31) + (this.m21 != 0.0d ? Double.hashCode(this.m21) : 0)) * 31) + (this.m22 != 0.0d ? Double.hashCode(this.m22) : 0);
            this.hashed = true;
        }
        return this.hashCode;
    }

    @Nonnull
    public Matrix3d clone() {
        return from(this);
    }

    @Nonnull
    public static Matrix3d from(double n) {
        return n == 0.0d ? ZERO : new Matrix3d(n, n, n, n, n, n, n, n, n);
    }

    @Nonnull
    public static Matrix3d from(Matrix2d m) {
        return from(m.get(0, 0), m.get(0, 1), 0.0d, m.get(1, 0), m.get(1, 1), 0.0d, 0.0d, 0.0d, 0.0d);
    }

    @Nonnull
    public static Matrix3d from(Matrix3d m) {
        return from(m.m00, m.m01, m.m02, m.m10, m.m11, m.m12, m.m20, m.m21, m.m22);
    }

    @Nonnull
    public static Matrix3d from(Matrix4d m) {
        return from(m.get(0, 0), m.get(0, 1), m.get(0, 2), m.get(1, 0), m.get(1, 1), m.get(1, 2), m.get(2, 0), m.get(2, 1), m.get(2, 2));
    }

    @Nonnull
    public static Matrix3d from(MatrixNd m) {
        double m21;
        double m22;
        double m02;
        double m12;
        double m20;
        double m00 = m.get(0, 0);
        double m01 = m.get(0, 1);
        double m10 = m.get(1, 0);
        double m11 = m.get(1, 1);
        if (m.size() > 2) {
            double m022 = m.get(0, 2);
            double m122 = m.get(1, 2);
            double m202 = m.get(2, 0);
            double m212 = m.get(2, 1);
            m21 = m212;
            m22 = m.get(2, 2);
            m02 = m022;
            m12 = m122;
            m20 = m202;
        } else {
            m21 = 0.0d;
            m22 = 0.0d;
            m02 = 0.0d;
            m12 = 0.0d;
            m20 = 0.0d;
        }
        return from(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Nonnull
    public static Matrix3d from(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        return from(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Nonnull
    public static Matrix3d from(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        return (m00 == 0.0d && m01 == 0.0d && m02 == 0.0d && m10 == 0.0d && m11 == 0.0d && m12 == 0.0d && m20 == 0.0d && m21 == 0.0d && m22 == 0.0d) ? ZERO : new Matrix3d(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Nonnull
    public static Matrix3d fromDiagonal(double m00, double m11, double m22) {
        return (m00 == 0.0d && m11 == 0.0d && m22 == 0.0d) ? ZERO : new Matrix3d(m00, 0.0d, 0.0d, 0.0d, m11, 0.0d, 0.0d, 0.0d, m22);
    }

    @Nonnull
    public static Matrix3d createScaling(float scale) {
        return createScaling(scale);
    }

    @Nonnull
    public static Matrix3d createScaling(double scale) {
        return createScaling(scale, scale, scale);
    }

    @Nonnull
    public static Matrix3d createScaling(Vector3d v) {
        return createScaling(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Matrix3d createScaling(float x, float y, float z) {
        return createScaling(x, y, z);
    }

    @Nonnull
    public static Matrix3d createScaling(double x, double y, double z) {
        return from(x, 0.0d, 0.0d, 0.0d, y, 0.0d, 0.0d, 0.0d, z);
    }

    @Nonnull
    public static Matrix3d createTranslation(Vector2d v) {
        return createTranslation(v.getX(), v.getY());
    }

    @Nonnull
    public static Matrix3d createTranslation(float x, float y) {
        return createTranslation(x, y);
    }

    @Nonnull
    public static Matrix3d createTranslation(double x, double y) {
        return from(1.0d, 0.0d, x, 0.0d, 1.0d, y, 0.0d, 0.0d, 1.0d);
    }

    @Nonnull
    public static Matrix3d createRotation(Complexd rot) {
        Complexd rot2 = rot.normalize();
        return from(rot2.getX(), -rot2.getY(), 0.0d, rot2.getY(), rot2.getX(), 0.0d, 0.0d, 0.0d, 1.0d);
    }

    @Nonnull
    public static Matrix3d createRotation(Quaterniond rot) {
        Quaterniond rot2 = rot.normalize();
        return from((1.0d - ((rot2.getY() * 2.0d) * rot2.getY())) - ((rot2.getZ() * 2.0d) * rot2.getZ()), ((rot2.getX() * 2.0d) * rot2.getY()) - ((rot2.getW() * 2.0d) * rot2.getZ()), (rot2.getX() * 2.0d * rot2.getZ()) + (rot2.getW() * 2.0d * rot2.getY()), (rot2.getX() * 2.0d * rot2.getY()) + (rot2.getW() * 2.0d * rot2.getZ()), (1.0d - ((rot2.getX() * 2.0d) * rot2.getX())) - ((rot2.getZ() * 2.0d) * rot2.getZ()), ((rot2.getY() * 2.0d) * rot2.getZ()) - ((rot2.getW() * 2.0d) * rot2.getX()), ((rot2.getX() * 2.0d) * rot2.getZ()) - ((rot2.getW() * 2.0d) * rot2.getY()), (rot2.getY() * 2.0d * rot2.getZ()) + (rot2.getX() * 2.0d * rot2.getW()), (1.0d - ((rot2.getX() * 2.0d) * rot2.getX())) - ((rot2.getY() * 2.0d) * rot2.getY()));
    }
}
