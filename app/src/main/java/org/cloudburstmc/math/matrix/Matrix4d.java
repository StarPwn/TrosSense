package org.cloudburstmc.math.matrix;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.imaginary.Complexd;
import org.cloudburstmc.math.imaginary.Quaterniond;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector4d;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class Matrix4d implements Matrixd, Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    private final double m00;
    private final double m01;
    private final double m02;
    private final double m03;
    private final double m10;
    private final double m11;
    private final double m12;
    private final double m13;
    private final double m20;
    private final double m21;
    private final double m22;
    private final double m23;
    private final double m30;
    private final double m31;
    private final double m32;
    private final double m33;
    public static final Matrix4d ZERO = new Matrix4d(0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
    public static final Matrix4d IDENTITY = new Matrix4d(1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d);
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    private Matrix4d(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
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
                case 1: goto L14;
                case 2: goto L24;
                case 3: goto L34;
                default: goto L3;
            }
        L3:
            goto L44
        L4:
            switch(r7) {
                case 0: goto L11;
                case 1: goto Le;
                case 2: goto Lb;
                case 3: goto L8;
                default: goto L7;
            }
        L7:
            goto L14
        L8:
            double r0 = r5.m03
            return r0
        Lb:
            double r0 = r5.m02
            return r0
        Le:
            double r0 = r5.m01
            return r0
        L11:
            double r0 = r5.m00
            return r0
        L14:
            switch(r7) {
                case 0: goto L21;
                case 1: goto L1e;
                case 2: goto L1b;
                case 3: goto L18;
                default: goto L17;
            }
        L17:
            goto L24
        L18:
            double r0 = r5.m13
            return r0
        L1b:
            double r0 = r5.m12
            return r0
        L1e:
            double r0 = r5.m11
            return r0
        L21:
            double r0 = r5.m10
            return r0
        L24:
            switch(r7) {
                case 0: goto L31;
                case 1: goto L2e;
                case 2: goto L2b;
                case 3: goto L28;
                default: goto L27;
            }
        L27:
            goto L34
        L28:
            double r0 = r5.m23
            return r0
        L2b:
            double r0 = r5.m22
            return r0
        L2e:
            double r0 = r5.m21
            return r0
        L31:
            double r0 = r5.m20
            return r0
        L34:
            switch(r7) {
                case 0: goto L41;
                case 1: goto L3e;
                case 2: goto L3b;
                case 3: goto L38;
                default: goto L37;
            }
        L37:
            goto L44
        L38:
            double r0 = r5.m33
            return r0
        L3b:
            double r0 = r5.m32
            return r0
        L3e:
            double r0 = r5.m31
            return r0
        L41:
            double r0 = r5.m30
            return r0
        L44:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r3 = 2
            if (r6 < 0) goto L55
            if (r6 <= r3) goto L53
            goto L55
        L53:
            r4 = r2
            goto L57
        L55:
            java.lang.String r4 = "row must be greater than zero and smaller than 3. "
        L57:
            java.lang.StringBuilder r1 = r1.append(r4)
            if (r7 < 0) goto L5f
            if (r7 <= r3) goto L61
        L5f:
            java.lang.String r2 = "col must be greater than zero and smaller than 3."
        L61:
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.cloudburstmc.math.matrix.Matrix4d.get(int, int):double");
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Vector4d getRow(int row) {
        return Vector4d.from(get(row, 0), get(row, 1), get(row, 2), get(row, 3));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Vector4d getColumn(int col) {
        return Vector4d.from(get(0, col), get(1, col), get(2, col), get(3, col));
    }

    @Nonnull
    public Matrix4d add(Matrix4d m) {
        return from(this.m00 + m.m00, this.m01 + m.m01, this.m02 + m.m02, this.m03 + m.m03, this.m10 + m.m10, this.m11 + m.m11, this.m12 + m.m12, this.m13 + m.m13, this.m20 + m.m20, this.m21 + m.m21, this.m22 + m.m22, this.m23 + m.m23, this.m30 + m.m30, this.m31 + m.m31, this.m32 + m.m32, this.m33 + m.m33);
    }

    @Nonnull
    public Matrix4d sub(Matrix4d m) {
        return from(this.m00 - m.m00, this.m01 - m.m01, this.m02 - m.m02, this.m03 - m.m03, this.m10 - m.m10, this.m11 - m.m11, this.m12 - m.m12, this.m13 - m.m13, this.m20 - m.m20, this.m21 - m.m21, this.m22 - m.m22, this.m23 - m.m23, this.m30 - m.m30, this.m31 - m.m31, this.m32 - m.m32, this.m33 - m.m33);
    }

    @Nonnull
    public Matrix4d mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d mul(double a) {
        return from(this.m00 * a, this.m01 * a, this.m02 * a, this.m03 * a, this.m10 * a, this.m11 * a, this.m12 * a, this.m13 * a, this.m20 * a, this.m21 * a, this.m22 * a, this.m23 * a, this.m30 * a, this.m31 * a, this.m32 * a, this.m33 * a);
    }

    @Nonnull
    public Matrix4d mul(Matrix4d m) {
        return from((this.m00 * m.m00) + (this.m01 * m.m10) + (this.m02 * m.m20) + (this.m03 * m.m30), (this.m00 * m.m01) + (this.m01 * m.m11) + (this.m02 * m.m21) + (this.m03 * m.m31), (this.m00 * m.m02) + (this.m01 * m.m12) + (this.m02 * m.m22) + (this.m03 * m.m32), (this.m00 * m.m03) + (this.m01 * m.m13) + (this.m02 * m.m23) + (this.m03 * m.m33), (this.m10 * m.m00) + (this.m11 * m.m10) + (this.m12 * m.m20) + (this.m13 * m.m30), (this.m10 * m.m01) + (this.m11 * m.m11) + (this.m12 * m.m21) + (this.m13 * m.m31), (this.m10 * m.m02) + (this.m11 * m.m12) + (this.m12 * m.m22) + (this.m13 * m.m32), (this.m10 * m.m03) + (this.m11 * m.m13) + (this.m12 * m.m23) + (this.m13 * m.m33), (this.m20 * m.m00) + (this.m21 * m.m10) + (this.m22 * m.m20) + (this.m23 * m.m30), (this.m20 * m.m01) + (this.m21 * m.m11) + (this.m22 * m.m21) + (this.m23 * m.m31), (this.m20 * m.m02) + (this.m21 * m.m12) + (this.m22 * m.m22) + (this.m23 * m.m32), (this.m20 * m.m03) + (this.m21 * m.m13) + (this.m22 * m.m23) + (this.m23 * m.m33), (this.m30 * m.m00) + (this.m31 * m.m10) + (this.m32 * m.m20) + (this.m33 * m.m30), (this.m30 * m.m01) + (this.m31 * m.m11) + (this.m32 * m.m21) + (this.m33 * m.m31), (this.m30 * m.m02) + (this.m31 * m.m12) + (this.m32 * m.m22) + (this.m33 * m.m32), (this.m30 * m.m03) + (this.m31 * m.m13) + (this.m32 * m.m23) + (this.m33 * m.m33));
    }

    @Nonnull
    public Matrix4d div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d div(double a) {
        return from(this.m00 / a, this.m01 / a, this.m02 / a, this.m03 / a, this.m10 / a, this.m11 / a, this.m12 / a, this.m13 / a, this.m20 / a, this.m21 / a, this.m22 / a, this.m23 / a, this.m30 / a, this.m31 / a, this.m32 / a, this.m33 / a);
    }

    @Nonnull
    public Matrix4d div(Matrix4d m) {
        return mul(m.invert());
    }

    @Nonnull
    public Matrix4d pow(float pow) {
        return pow(pow);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d pow(double pow) {
        return from(Math.pow(this.m00, pow), Math.pow(this.m01, pow), Math.pow(this.m02, pow), Math.pow(this.m03, pow), Math.pow(this.m10, pow), Math.pow(this.m11, pow), Math.pow(this.m12, pow), Math.pow(this.m13, pow), Math.pow(this.m20, pow), Math.pow(this.m21, pow), Math.pow(this.m22, pow), Math.pow(this.m23, pow), Math.pow(this.m30, pow), Math.pow(this.m31, pow), Math.pow(this.m32, pow), Math.pow(this.m33, pow));
    }

    @Nonnull
    public Matrix4d translate(Vector3d v) {
        return translate(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public Matrix4d translate(float x, float y, float z) {
        return translate(x, y, z);
    }

    @Nonnull
    public Matrix4d translate(double x, double y, double z) {
        return createTranslation(x, y, z).mul(this);
    }

    @Nonnull
    public Matrix4d scale(float scale) {
        return scale(scale);
    }

    @Nonnull
    public Matrix4d scale(double scale) {
        return scale(scale, scale, scale, scale);
    }

    @Nonnull
    public Matrix4d scale(Vector4d v) {
        return scale(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Matrix4d scale(float x, float y, float z, float w) {
        return scale(x, y, z, w);
    }

    @Nonnull
    public Matrix4d scale(double x, double y, double z, double w) {
        return createScaling(x, y, z, w).mul(this);
    }

    @Nonnull
    public Matrix4d rotate(Complexd rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Matrix4d rotate(Quaterniond rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Vector4d transform(Vector4d v) {
        return transform(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public Vector4d transform(float x, float y, float z, float w) {
        return transform(x, y, z, w);
    }

    @Nonnull
    public Vector4d transform(double x, double y, double z, double w) {
        return Vector4d.from((this.m00 * x) + (this.m01 * y) + (this.m02 * z) + (this.m03 * w), (this.m10 * x) + (this.m11 * y) + (this.m12 * z) + (this.m13 * w), (this.m20 * x) + (this.m21 * y) + (this.m22 * z) + (this.m23 * w), (this.m30 * x) + (this.m31 * y) + (this.m32 * z) + (this.m33 * w));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d floor() {
        return from(GenericMath.floor(this.m00), GenericMath.floor(this.m01), GenericMath.floor(this.m02), GenericMath.floor(this.m03), GenericMath.floor(this.m10), GenericMath.floor(this.m11), GenericMath.floor(this.m12), GenericMath.floor(this.m13), GenericMath.floor(this.m20), GenericMath.floor(this.m21), GenericMath.floor(this.m22), GenericMath.floor(this.m23), GenericMath.floor(this.m30), GenericMath.floor(this.m31), GenericMath.floor(this.m32), GenericMath.floor(this.m33));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d ceil() {
        return from(Math.ceil(this.m00), Math.ceil(this.m01), Math.ceil(this.m02), Math.ceil(this.m03), Math.ceil(this.m10), Math.ceil(this.m11), Math.ceil(this.m12), Math.ceil(this.m13), Math.ceil(this.m20), Math.ceil(this.m21), Math.ceil(this.m22), Math.ceil(this.m23), Math.ceil(this.m30), Math.ceil(this.m31), Math.ceil(this.m32), Math.ceil(this.m33));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d round() {
        return from((float) Math.round(this.m00), (float) Math.round(this.m01), (float) Math.round(this.m02), (float) Math.round(this.m03), (float) Math.round(this.m10), (float) Math.round(this.m11), (float) Math.round(this.m12), (float) Math.round(this.m13), (float) Math.round(this.m20), (float) Math.round(this.m21), (float) Math.round(this.m22), (float) Math.round(this.m23), (float) Math.round(this.m30), (float) Math.round(this.m31), (float) Math.round(this.m32), (float) Math.round(this.m33));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d abs() {
        return from(Math.abs(this.m00), Math.abs(this.m01), Math.abs(this.m02), Math.abs(this.m03), Math.abs(this.m10), Math.abs(this.m11), Math.abs(this.m12), Math.abs(this.m13), Math.abs(this.m20), Math.abs(this.m21), Math.abs(this.m22), Math.abs(this.m23), Math.abs(this.m30), Math.abs(this.m31), Math.abs(this.m32), Math.abs(this.m33));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d negate() {
        return from(-this.m00, -this.m01, -this.m02, -this.m03, -this.m10, -this.m11, -this.m12, -this.m13, -this.m20, -this.m21, -this.m22, -this.m23, -this.m30, -this.m31, -this.m32, -this.m33);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d transpose() {
        return from(this.m00, this.m10, this.m20, this.m30, this.m01, this.m11, this.m21, this.m31, this.m02, this.m12, this.m22, this.m32, this.m03, this.m13, this.m23, this.m33);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double trace() {
        return this.m00 + this.m11 + this.m22 + this.m33;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double determinant() {
        return (((this.m00 * (((((((this.m11 * this.m22) * this.m33) + ((this.m21 * this.m32) * this.m13)) + ((this.m31 * this.m12) * this.m23)) - ((this.m31 * this.m22) * this.m13)) - ((this.m11 * this.m32) * this.m23)) - ((this.m21 * this.m12) * this.m33))) - (this.m10 * (((((((this.m01 * this.m22) * this.m33) + ((this.m21 * this.m32) * this.m03)) + ((this.m31 * this.m02) * this.m23)) - ((this.m31 * this.m22) * this.m03)) - ((this.m01 * this.m32) * this.m23)) - ((this.m21 * this.m02) * this.m33)))) + (this.m20 * (((((((this.m01 * this.m12) * this.m33) + ((this.m11 * this.m32) * this.m03)) + ((this.m31 * this.m02) * this.m13)) - ((this.m31 * this.m12) * this.m03)) - ((this.m01 * this.m32) * this.m13)) - ((this.m11 * this.m02) * this.m33)))) - (this.m30 * (((((((this.m01 * this.m12) * this.m23) + ((this.m11 * this.m22) * this.m03)) + ((this.m21 * this.m02) * this.m13)) - ((this.m21 * this.m12) * this.m03)) - ((this.m01 * this.m22) * this.m13)) - ((this.m11 * this.m02) * this.m23)));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d invert() {
        double det = determinant();
        if (Math.abs(det) >= GenericMath.DBL_EPSILON) {
            return from(det3(this.m11, this.m21, this.m31, this.m12, this.m22, this.m32, this.m13, this.m23, this.m33) / det, (-det3(this.m01, this.m21, this.m31, this.m02, this.m22, this.m32, this.m03, this.m23, this.m33)) / det, det3(this.m01, this.m11, this.m31, this.m02, this.m12, this.m32, this.m03, this.m13, this.m33) / det, (-det3(this.m01, this.m11, this.m21, this.m02, this.m12, this.m22, this.m03, this.m13, this.m23)) / det, (-det3(this.m10, this.m20, this.m30, this.m12, this.m22, this.m32, this.m13, this.m23, this.m33)) / det, det3(this.m00, this.m20, this.m30, this.m02, this.m22, this.m32, this.m03, this.m23, this.m33) / det, (-det3(this.m00, this.m10, this.m30, this.m02, this.m12, this.m32, this.m03, this.m13, this.m33)) / det, det3(this.m00, this.m10, this.m20, this.m02, this.m12, this.m22, this.m03, this.m13, this.m23) / det, det3(this.m10, this.m20, this.m30, this.m11, this.m21, this.m31, this.m13, this.m23, this.m33) / det, (-det3(this.m00, this.m20, this.m30, this.m01, this.m21, this.m31, this.m03, this.m23, this.m33)) / det, det3(this.m00, this.m10, this.m30, this.m01, this.m11, this.m31, this.m03, this.m13, this.m33) / det, (-det3(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m03, this.m13, this.m23)) / det, (-det3(this.m10, this.m20, this.m30, this.m11, this.m21, this.m31, this.m12, this.m22, this.m32)) / det, det3(this.m00, this.m20, this.m30, this.m01, this.m21, this.m31, this.m02, this.m22, this.m32) / det, (-det3(this.m00, this.m10, this.m30, this.m01, this.m11, this.m31, this.m02, this.m12, this.m32)) / det, det3(this.m00, this.m10, this.m20, this.m01, this.m11, this.m21, this.m02, this.m12, this.m22) / det);
        }
        throw new ArithmeticException("Cannot inverse a matrix with a zero determinant");
    }

    @Nonnull
    public Matrix2d toMatrix2() {
        return Matrix2d.from(this);
    }

    @Nonnull
    public Matrix3d toMatrix3() {
        return Matrix3d.from(this);
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
            return new double[]{this.m00, this.m10, this.m20, this.m30, this.m01, this.m11, this.m21, this.m31, this.m02, this.m12, this.m22, this.m32, this.m03, this.m13, this.m23, this.m33};
        }
        return new double[]{this.m00, this.m01, this.m02, this.m03, this.m10, this.m11, this.m12, this.m13, this.m20, this.m21, this.m22, this.m23, this.m30, this.m31, this.m32, this.m33};
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4f toFloat() {
        return Matrix4f.from(this.m00, this.m01, this.m02, this.m03, this.m10, this.m11, this.m12, this.m13, this.m20, this.m21, this.m22, this.m23, this.m30, this.m31, this.m32, this.m33);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public Matrix4d toDouble() {
        return from(this.m00, this.m01, this.m02, this.m03, this.m10, this.m11, this.m12, this.m13, this.m20, this.m21, this.m22, this.m23, this.m30, this.m31, this.m32, this.m33);
    }

    @Nonnull
    public String toString() {
        return this.m00 + " " + this.m01 + " " + this.m02 + " " + this.m03 + "\n" + this.m10 + " " + this.m11 + " " + this.m12 + " " + this.m13 + "\n" + this.m20 + " " + this.m21 + " " + this.m22 + " " + this.m23 + "\n" + this.m30 + " " + this.m31 + " " + this.m32 + " " + this.m33 + "\n";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix4d)) {
            return false;
        }
        Matrix4d matrix4 = (Matrix4d) o;
        return Double.compare(matrix4.m00, this.m00) == 0 && Double.compare(matrix4.m01, this.m01) == 0 && Double.compare(matrix4.m02, this.m02) == 0 && Double.compare(matrix4.m03, this.m03) == 0 && Double.compare(matrix4.m10, this.m10) == 0 && Double.compare(matrix4.m11, this.m11) == 0 && Double.compare(matrix4.m12, this.m12) == 0 && Double.compare(matrix4.m13, this.m13) == 0 && Double.compare(matrix4.m20, this.m20) == 0 && Double.compare(matrix4.m21, this.m21) == 0 && Double.compare(matrix4.m22, this.m22) == 0 && Double.compare(matrix4.m23, this.m23) == 0 && Double.compare(matrix4.m30, this.m30) == 0 && Double.compare(matrix4.m31, this.m31) == 0 && Double.compare(matrix4.m32, this.m32) == 0 && Double.compare(matrix4.m33, this.m33) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.m00 != 0.0d ? Double.hashCode(this.m00) : 0;
            this.hashCode = (((((((((((((((((((((((((((((result * 31) + (this.m01 != 0.0d ? Double.hashCode(this.m01) : 0)) * 31) + (this.m02 != 0.0d ? Double.hashCode(this.m02) : 0)) * 31) + (this.m03 != 0.0d ? Double.hashCode(this.m03) : 0)) * 31) + (this.m10 != 0.0d ? Double.hashCode(this.m10) : 0)) * 31) + (this.m11 != 0.0d ? Double.hashCode(this.m11) : 0)) * 31) + (this.m12 != 0.0d ? Double.hashCode(this.m12) : 0)) * 31) + (this.m13 != 0.0d ? Double.hashCode(this.m13) : 0)) * 31) + (this.m20 != 0.0d ? Double.hashCode(this.m20) : 0)) * 31) + (this.m21 != 0.0d ? Double.hashCode(this.m21) : 0)) * 31) + (this.m22 != 0.0d ? Double.hashCode(this.m22) : 0)) * 31) + (this.m23 != 0.0d ? Double.hashCode(this.m23) : 0)) * 31) + (this.m30 != 0.0d ? Double.hashCode(this.m30) : 0)) * 31) + (this.m31 != 0.0d ? Double.hashCode(this.m31) : 0)) * 31) + (this.m32 != 0.0d ? Double.hashCode(this.m32) : 0)) * 31) + (this.m33 != 0.0d ? Double.hashCode(this.m33) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }

    @Nonnull
    public Matrix4d clone() {
        return from(this);
    }

    @Nonnull
    public static Matrix4d from(double n) {
        return n == 0.0d ? ZERO : new Matrix4d(n, n, n, n, n, n, n, n, n, n, n, n, n, n, n, n);
    }

    @Nonnull
    public static Matrix4d from(Matrix2d m) {
        return from(m.get(0, 0), m.get(0, 1), 0.0d, 0.0d, m.get(1, 0), m.get(1, 1), 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    @Nonnull
    public static Matrix4d from(Matrix3d m) {
        return from(m.get(0, 0), m.get(0, 1), m.get(0, 2), 0.0d, m.get(1, 0), m.get(1, 1), m.get(1, 2), 0.0d, m.get(2, 0), m.get(2, 1), m.get(2, 2), 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
    }

    @Nonnull
    public static Matrix4d from(Matrix4d m) {
        return from(m.m00, m.m01, m.m02, m.m03, m.m10, m.m11, m.m12, m.m13, m.m20, m.m21, m.m22, m.m23, m.m30, m.m31, m.m32, m.m33);
    }

    @Nonnull
    public static Matrix4d from(MatrixNd m) {
        double m31;
        double m32;
        double m02;
        double m12;
        double m20;
        double m21;
        double m22;
        double m03;
        double m13;
        double m23;
        double m30;
        double m33;
        double m00 = m.get(0, 0);
        double m01 = m.get(0, 1);
        double m10 = m.get(1, 0);
        double m11 = m.get(1, 1);
        if (m.size() > 2) {
            double m022 = m.get(0, 2);
            double m122 = m.get(1, 2);
            double m202 = m.get(2, 0);
            double m212 = m.get(2, 1);
            double m222 = m.get(2, 2);
            if (m.size() > 3) {
                double m032 = m.get(0, 3);
                double m132 = m.get(1, 3);
                double m232 = m.get(2, 3);
                double m302 = m.get(3, 0);
                double m312 = m.get(3, 1);
                double m322 = m.get(3, 2);
                m31 = m312;
                m32 = m322;
                m02 = m022;
                m12 = m122;
                m20 = m202;
                m21 = m212;
                m22 = m222;
                m03 = m032;
                m13 = m132;
                m23 = m232;
                m30 = m302;
                m33 = m.get(3, 3);
            } else {
                m31 = 0.0d;
                m32 = 0.0d;
                m02 = m022;
                m12 = m122;
                m20 = m202;
                m21 = m212;
                m22 = m222;
                m03 = 0.0d;
                m13 = 0.0d;
                m23 = 0.0d;
                m30 = 0.0d;
                m33 = 0.0d;
            }
        } else {
            m31 = 0.0d;
            m32 = 0.0d;
            m02 = 0.0d;
            m12 = 0.0d;
            m20 = 0.0d;
            m21 = 0.0d;
            m22 = 0.0d;
            m03 = 0.0d;
            m13 = 0.0d;
            m23 = 0.0d;
            m30 = 0.0d;
            m33 = 0.0d;
        }
        return from(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Nonnull
    public static Matrix4d from(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        return from(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Nonnull
    public static Matrix4d from(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
        return (m00 == 0.0d && m01 == 0.0d && m02 == 0.0d && m03 == 0.0d && m10 == 0.0d && m11 == 0.0d && m12 == 0.0d && m13 == 0.0d && m20 == 0.0d && m21 == 0.0d && m22 == 0.0d && m23 == 0.0d && m30 == 0.0d && m31 == 0.0d && m32 == 0.0d && m33 == 0.0d) ? ZERO : new Matrix4d(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Nonnull
    public static Matrix4d fromDiagonal(double m00, double m11, double m22, double m33) {
        return (m00 == 0.0d && m11 == 0.0d && m22 == 0.0d && m33 == 0.0d) ? ZERO : new Matrix4d(m00, 0.0d, 0.0d, 0.0d, 0.0d, m11, 0.0d, 0.0d, 0.0d, 0.0d, m22, 0.0d, 0.0d, 0.0d, 0.0d, m33);
    }

    @Nonnull
    public static Matrix4d createScaling(float scale) {
        return createScaling(scale);
    }

    @Nonnull
    public static Matrix4d createScaling(double scale) {
        return createScaling(scale, scale, scale, scale);
    }

    @Nonnull
    public static Matrix4d createScaling(Vector4d v) {
        return createScaling(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    @Nonnull
    public static Matrix4d createScaling(float x, float y, float z, float w) {
        return createScaling(x, y, z, w);
    }

    @Nonnull
    public static Matrix4d createScaling(double x, double y, double z, double w) {
        return from(x, 0.0d, 0.0d, 0.0d, 0.0d, y, 0.0d, 0.0d, 0.0d, 0.0d, z, 0.0d, 0.0d, 0.0d, 0.0d, w);
    }

    @Nonnull
    public static Matrix4d createTranslation(Vector3d v) {
        return createTranslation(v.getX(), v.getY(), v.getZ());
    }

    @Nonnull
    public static Matrix4d createTranslation(float x, float y, float z) {
        return createTranslation(x, y, z);
    }

    @Nonnull
    public static Matrix4d createTranslation(double x, double y, double z) {
        return from(1.0d, 0.0d, 0.0d, x, 0.0d, 1.0d, 0.0d, y, 0.0d, 0.0d, 1.0d, z, 0.0d, 0.0d, 0.0d, 1.0d);
    }

    @Nonnull
    public static Matrix4d createRotation(Complexd rot) {
        Complexd rot2 = rot.normalize();
        return from(rot2.getX(), -rot2.getY(), 0.0d, 0.0d, rot2.getY(), rot2.getX(), 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d);
    }

    @Nonnull
    public static Matrix4d createRotation(Quaterniond rot) {
        Quaterniond rot2 = rot.normalize();
        return from((1.0d - ((rot2.getY() * 2.0d) * rot2.getY())) - ((rot2.getZ() * 2.0d) * rot2.getZ()), ((rot2.getX() * 2.0d) * rot2.getY()) - ((rot2.getW() * 2.0d) * rot2.getZ()), (rot2.getX() * 2.0d * rot2.getZ()) + (rot2.getW() * 2.0d * rot2.getY()), 0.0d, (rot2.getX() * 2.0d * rot2.getY()) + (rot2.getW() * 2.0d * rot2.getZ()), (1.0d - ((rot2.getX() * 2.0d) * rot2.getX())) - ((rot2.getZ() * 2.0d) * rot2.getZ()), ((rot2.getY() * 2.0d) * rot2.getZ()) - ((rot2.getW() * 2.0d) * rot2.getX()), 0.0d, ((rot2.getX() * 2.0d) * rot2.getZ()) - ((rot2.getW() * 2.0d) * rot2.getY()), (rot2.getY() * 2.0d * rot2.getZ()) + (rot2.getX() * 2.0d * rot2.getW()), (1.0d - ((rot2.getX() * 2.0d) * rot2.getX())) - ((rot2.getY() * 2.0d) * rot2.getY()), 0.0d, 0.0d, 0.0d, 0.0d, 1.0d);
    }

    @Nonnull
    public static Matrix4d createLookAt(Vector3d eye, Vector3d at, Vector3d up) {
        Vector3d f = at.sub(eye).normalize();
        Vector3d s = f.cross(up).normalize();
        Vector3d u = s.cross(f);
        Matrix4d mat = from(s.getX(), s.getY(), s.getZ(), 0.0d, u.getX(), u.getY(), u.getZ(), 0.0d, -f.getX(), -f.getY(), -f.getZ(), 0.0d, 0.0d, 0.0d, 0.0d, 1.0d);
        return mat.translate(eye.negate());
    }

    @Nonnull
    public static Matrix4d createPerspective(float fov, float aspect, float near, float far) {
        return createPerspective(fov, aspect, near, far);
    }

    @Nonnull
    public static Matrix4d createPerspective(double fov, double aspect, double near, double far) {
        double scale = 1.0f / TrigMath.tan(0.008726646259971648d * fov);
        return from(scale / aspect, 0.0d, 0.0d, 0.0d, 0.0d, scale, 0.0d, 0.0d, 0.0d, 0.0d, (far + near) / (near - far), ((2.0d * far) * near) / (near - far), 0.0d, 0.0d, -1.0d, 0.0d);
    }

    @Nonnull
    public static Matrix4d createOrthographic(float right, float left, float top, float bottom, float near, float far) {
        return createOrthographic(right, left, top, bottom, near, far);
    }

    @Nonnull
    public static Matrix4d createOrthographic(double right, double left, double top, double bottom, double near, double far) {
        return from(2.0d / (right - left), 0.0d, 0.0d, (-(right + left)) / (right - left), 0.0d, 2.0d / (top - bottom), 0.0d, (-(top + bottom)) / (top - bottom), 0.0d, 0.0d, (-2.0d) / (far - near), (-(far + near)) / (far - near), 0.0d, 0.0d, 0.0d, 1.0d);
    }

    private static double det3(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        return ((((m11 * m22) - (m12 * m21)) * m00) - (((m10 * m22) - (m12 * m20)) * m01)) + (((m10 * m21) - (m11 * m20)) * m02);
    }
}
