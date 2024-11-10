package org.cloudburstmc.math.matrix;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.imaginary.Complexf;
import org.cloudburstmc.math.vector.Vector2f;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class Matrix2f implements Matrixf, Serializable, Cloneable {
    private static final long serialVersionUID = 1;
    private final float m00;
    private final float m01;
    private final float m10;
    private final float m11;
    public static final Matrix2f ZERO = new Matrix2f(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Matrix2f IDENTITY = new Matrix2f(1.0f, 0.0f, 0.0f, 1.0f);
    private volatile transient boolean hashed = false;
    private volatile transient int hashCode = 0;

    private Matrix2f(float m00, float m01, float m10, float m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:1:0x0000. Please report as an issue. */
    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float get(int row, int col) {
        switch (row) {
            case 0:
                switch (col) {
                    case 0:
                        return this.m00;
                    case 1:
                        return this.m01;
                }
                throw new IllegalArgumentException(((row >= 0 || row > 1) ? "row must be greater than zero and smaller than 2. " : "") + ((col >= 0 || col > 1) ? "col must be greater than zero and smaller than 2." : ""));
            case 1:
                switch (col) {
                    case 0:
                        return this.m10;
                    case 1:
                        return this.m11;
                }
            default:
                throw new IllegalArgumentException(((row >= 0 || row > 1) ? "row must be greater than zero and smaller than 2. " : "") + ((col >= 0 || col > 1) ? "col must be greater than zero and smaller than 2." : ""));
        }
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Vector2f getRow(int row) {
        return Vector2f.from(get(row, 0), get(row, 1));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Vector2f getColumn(int col) {
        return Vector2f.from(get(0, col), get(1, col));
    }

    @Nonnull
    public Matrix2f add(Matrix2f m) {
        return from(this.m00 + m.m00, this.m01 + m.m01, this.m10 + m.m10, this.m11 + m.m11);
    }

    @Nonnull
    public Matrix2f sub(Matrix2f m) {
        return from(this.m00 - m.m00, this.m01 - m.m01, this.m10 - m.m10, this.m11 - m.m11);
    }

    @Nonnull
    public Matrix2f mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f mul(float a) {
        return from(this.m00 * a, this.m01 * a, this.m10 * a, this.m11 * a);
    }

    @Nonnull
    public Matrix2f mul(Matrix2f m) {
        return from((this.m00 * m.m00) + (this.m01 * m.m10), (this.m00 * m.m01) + (this.m01 * m.m11), (this.m10 * m.m00) + (this.m11 * m.m10), (this.m10 * m.m01) + (this.m11 * m.m11));
    }

    @Nonnull
    public Matrix2f div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f div(float a) {
        return from(this.m00 / a, this.m01 / a, this.m10 / a, this.m11 / a);
    }

    @Nonnull
    public Matrix2f div(Matrix2f m) {
        return mul(m.invert());
    }

    @Nonnull
    public Matrix2f pow(double pow) {
        return pow((float) pow);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f pow(float pow) {
        return from(Math.pow(this.m00, pow), Math.pow(this.m01, pow), Math.pow(this.m10, pow), Math.pow(this.m11, pow));
    }

    @Nonnull
    public Matrix2f translate(double x) {
        return translate((float) x);
    }

    @Nonnull
    public Matrix2f translate(float x) {
        return createTranslation(x).mul(this);
    }

    @Nonnull
    public Matrix2f scale(double scale) {
        return scale((float) scale);
    }

    @Nonnull
    public Matrix2f scale(float scale) {
        return scale(scale, scale);
    }

    @Nonnull
    public Matrix2f scale(Vector2f v) {
        return scale(v.getX(), v.getY());
    }

    @Nonnull
    public Matrix2f scale(double x, double y) {
        return scale((float) x, (float) y);
    }

    @Nonnull
    public Matrix2f scale(float x, float y) {
        return createScaling(x, y).mul(this);
    }

    @Nonnull
    public Matrix2f rotate(Complexf rot) {
        return createRotation(rot).mul(this);
    }

    @Nonnull
    public Vector2f transform(Vector2f v) {
        return transform(v.getX(), v.getY());
    }

    @Nonnull
    public Vector2f transform(double x, double y) {
        return transform((float) x, (float) y);
    }

    @Nonnull
    public Vector2f transform(float x, float y) {
        return Vector2f.from((this.m00 * x) + (this.m01 * y), (this.m10 * x) + (this.m11 * y));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f floor() {
        return from(GenericMath.floor(this.m00), GenericMath.floor(this.m01), GenericMath.floor(this.m10), GenericMath.floor(this.m11));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f ceil() {
        return from(Math.ceil(this.m00), Math.ceil(this.m01), Math.ceil(this.m10), Math.ceil(this.m11));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f round() {
        return from(Math.round(this.m00), Math.round(this.m01), Math.round(this.m10), Math.round(this.m11));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f abs() {
        return from(Math.abs(this.m00), Math.abs(this.m01), Math.abs(this.m10), Math.abs(this.m11));
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f negate() {
        return from(-this.m00, -this.m01, -this.m10, -this.m11);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f transpose() {
        return from(this.m00, this.m10, this.m01, this.m11);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float trace() {
        return this.m00 + this.m11;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float determinant() {
        return (this.m00 * this.m11) - (this.m01 * this.m10);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f invert() {
        float det = determinant();
        if (Math.abs(det) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot inverse a matrix with a zero determinant");
        }
        return from(this.m11 / det, (-this.m01) / det, (-this.m10) / det, this.m00 / det);
    }

    @Nonnull
    public Matrix3f toMatrix3() {
        return Matrix3f.from(this);
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
            return new float[]{this.m00, this.m10, this.m01, this.m11};
        }
        return new float[]{this.m00, this.m01, this.m10, this.m11};
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2f toFloat() {
        return from(this.m00, this.m01, this.m10, this.m11);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public Matrix2d toDouble() {
        return Matrix2d.from(this.m00, this.m01, this.m10, this.m11);
    }

    @Nonnull
    public String toString() {
        return this.m00 + " " + this.m01 + "\n" + this.m10 + " " + this.m11;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matrix2f)) {
            return false;
        }
        Matrix2f matrix2 = (Matrix2f) o;
        return Float.compare(matrix2.m00, this.m00) == 0 && Float.compare(matrix2.m01, this.m01) == 0 && Float.compare(matrix2.m10, this.m10) == 0 && Float.compare(matrix2.m11, this.m11) == 0;
    }

    public int hashCode() {
        if (!this.hashed) {
            int result = this.m00 != 0.0f ? Float.hashCode(this.m00) : 0;
            this.hashCode = (((((result * 31) + (this.m01 != 0.0f ? Float.hashCode(this.m01) : 0)) * 31) + (this.m10 != 0.0f ? Float.hashCode(this.m10) : 0)) * 31) + (this.m11 != 0.0f ? Float.hashCode(this.m11) : 0);
            this.hashed = true;
        }
        int result2 = this.hashCode;
        return result2;
    }

    @Nonnull
    public Matrix2f clone() {
        return from(this);
    }

    @Nonnull
    public static Matrix2f from(float n) {
        return n == 0.0f ? ZERO : new Matrix2f(n, n, n, n);
    }

    @Nonnull
    public static Matrix2f from(Matrix2f m) {
        return from(m.m00, m.m01, m.m10, m.m11);
    }

    @Nonnull
    public static Matrix2f from(Matrix3f m) {
        return from(m.get(0, 0), m.get(0, 1), m.get(1, 0), m.get(1, 1));
    }

    @Nonnull
    public static Matrix2f from(Matrix4f m) {
        return from(m.get(0, 0), m.get(0, 1), m.get(1, 0), m.get(1, 1));
    }

    @Nonnull
    public static Matrix2f from(MatrixNf m) {
        return from(m.get(0, 0), m.get(0, 1), m.get(1, 0), m.get(1, 1));
    }

    @Nonnull
    public static Matrix2f from(double m00, double m01, double m10, double m11) {
        return from((float) m00, (float) m01, (float) m10, (float) m11);
    }

    @Nonnull
    public static Matrix2f from(float m00, float m01, float m10, float m11) {
        return (m00 == 0.0f && m01 == 0.0f && m10 == 0.0f && m11 == 0.0f) ? ZERO : new Matrix2f(m00, m01, m10, m11);
    }

    @Nonnull
    public static Matrix2f fromDiagonal(float m00, float m11) {
        return (m00 == 0.0f && m11 == 0.0f) ? ZERO : new Matrix2f(m00, 0.0f, 0.0f, m11);
    }

    @Nonnull
    public static Matrix2f createScaling(double scale) {
        return createScaling((float) scale);
    }

    @Nonnull
    public static Matrix2f createScaling(float scale) {
        return createScaling(scale, scale);
    }

    @Nonnull
    public static Matrix2f createScaling(Vector2f v) {
        return createScaling(v.getX(), v.getY());
    }

    @Nonnull
    public static Matrix2f createScaling(double x, double y) {
        return createScaling((float) x, (float) y);
    }

    @Nonnull
    public static Matrix2f createScaling(float x, float y) {
        return from(x, 0.0f, 0.0f, y);
    }

    @Nonnull
    public static Matrix2f createTranslation(double x) {
        return createTranslation((float) x);
    }

    @Nonnull
    public static Matrix2f createTranslation(float x) {
        return from(1.0f, x, 0.0f, 1.0f);
    }

    @Nonnull
    public static Matrix2f createRotation(Complexf rot) {
        Complexf rot2 = rot.normalize();
        return from(rot2.getX(), -rot2.getY(), rot2.getY(), rot2.getX());
    }
}
