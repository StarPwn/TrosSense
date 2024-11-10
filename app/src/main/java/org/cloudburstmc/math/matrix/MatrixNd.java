package org.cloudburstmc.math.matrix;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.imaginary.Complexd;
import org.cloudburstmc.math.imaginary.Quaterniond;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.VectorNd;
import org.cloudburstmc.math.vector.Vectord;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class MatrixNd implements Matrixd, Serializable, Cloneable {
    public static final MatrixNd IDENTITY_2 = new ImmutableIdentityMatrixN(2);
    public static final MatrixNd IDENTITY_3 = new ImmutableIdentityMatrixN(3);
    public static final MatrixNd IDENTITY_4 = new ImmutableIdentityMatrixN(4);
    private static final long serialVersionUID = 1;
    private final double[][] mat;

    private MatrixNd(double[][] mat) {
        this.mat = mat;
    }

    public int size() {
        return this.mat.length;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double get(int row, int col) {
        return this.mat[row][col];
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public VectorNd getRow(int row) {
        int size = size();
        VectorNd d = VectorNd.from(size);
        for (int col = 0; col < size; col++) {
            d.set(col, get(row, col));
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public VectorNd getColumn(int col) {
        int size = size();
        VectorNd d = VectorNd.from(size);
        for (int row = 0; row < size; row++) {
            d.set(row, get(row, col));
        }
        return d;
    }

    public void set(int row, int col, float val) {
        set(row, col, val);
    }

    public void set(int row, int col, double val) {
        this.mat[row][col] = val;
    }

    public final void setIdentity() {
        int size = size();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == col) {
                    this.mat[row][col] = 1.0d;
                } else {
                    this.mat[row][col] = 0.0d;
                }
            }
        }
    }

    public void setZero() {
        int size = size();
        for (int row = 0; row < size; row++) {
            Arrays.fill(this.mat[row], 0.0d);
        }
    }

    @Nonnull
    public MatrixNd resize(int size) {
        MatrixNd d = from(size);
        for (int rowCol = size(); rowCol < size; rowCol++) {
            d.set(rowCol, rowCol, 0.0f);
        }
        int rowCol2 = size();
        int size2 = Math.min(size, rowCol2);
        for (int row = 0; row < size2; row++) {
            System.arraycopy(this.mat[row], 0, d.mat[row], 0, size2);
        }
        return d;
    }

    @Nonnull
    public MatrixNd add(MatrixNd m) {
        int size = size();
        if (size != m.size()) {
            throw new IllegalArgumentException("Matrix sizes must be the same");
        }
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] + m.mat[row][col];
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNd sub(MatrixNd m) {
        int size = size();
        if (size != m.size()) {
            throw new IllegalArgumentException("Matrix sizes must be the same");
        }
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] - m.mat[row][col];
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNd mul(float a) {
        return mul(a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd mul(double a) {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] * a;
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNd mul(MatrixNd m) {
        int size = size();
        if (size != m.size()) {
            throw new IllegalArgumentException("Matrix sizes must be the same");
        }
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                double dot = 0.0d;
                for (int i = 0; i < size; i++) {
                    dot += this.mat[row][i] * m.mat[i][col];
                }
                d.mat[row][col] = dot;
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNd div(float a) {
        return div(a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd div(double a) {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] / a;
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNd div(MatrixNd m) {
        return mul(m.invert());
    }

    @Nonnull
    public MatrixNd pow(float pow) {
        return pow(pow);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd pow(double pow) {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = Math.pow(this.mat[row][col], pow);
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNd translate(VectorNd v) {
        return translate(v.toArray());
    }

    @Nonnull
    public MatrixNd translate(double... v) {
        return createTranslation(v).mul(this);
    }

    @Nonnull
    public MatrixNd scale(VectorNd v) {
        return scale(v.toArray());
    }

    @Nonnull
    public MatrixNd scale(double... v) {
        return createScaling(v).mul(this);
    }

    @Nonnull
    public MatrixNd rotate(Complexd rot) {
        return createRotation(size(), rot).mul(this);
    }

    @Nonnull
    public MatrixNd rotate(Quaterniond rot) {
        return createRotation(size(), rot).mul(this);
    }

    @Nonnull
    public VectorNd transform(VectorNd v) {
        return transform(v.toArray());
    }

    @Nonnull
    public VectorNd transform(double... vec) {
        int size = size();
        if (size != vec.length) {
            throw new IllegalArgumentException("Matrix and vector sizes must be the same");
        }
        VectorNd d = VectorNd.from(size);
        for (int row = 0; row < size; row++) {
            double dot = 0.0d;
            for (int col = 0; col < size; col++) {
                dot += this.mat[row][col] * vec[col];
            }
            d.set(row, dot);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd floor() {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = GenericMath.floor(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd ceil() {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = Math.ceil(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd round() {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = Math.round(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd abs() {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = Math.abs(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd negate() {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = -this.mat[row][col];
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd transpose() {
        int size = size();
        MatrixNd d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[col][row];
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double trace() {
        int size = size();
        double trace = 0.0d;
        for (int rowCol = 0; rowCol < size; rowCol++) {
            trace += this.mat[rowCol][rowCol];
        }
        return trace;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    public double determinant() {
        int size = size();
        double[][] m = deepClone(this.mat);
        for (int i = 0; i < size - 1; i++) {
            for (int col = i + 1; col < size; col++) {
                double det = m[i][i] < GenericMath.DBL_EPSILON ? 0.0d : m[i][col] / m[i][i];
                for (int row = i; row < size; row++) {
                    double[] dArr = m[row];
                    dArr[col] = dArr[col] - (m[row][i] * det);
                }
            }
        }
        double det2 = 1.0d;
        for (int i2 = 0; i2 < size; i2++) {
            det2 *= m[i2][i2];
        }
        return det2;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd invert() {
        if (Math.abs(determinant()) < GenericMath.DBL_EPSILON) {
            throw new ArithmeticException("Cannot inverse a matrix with a zero determinant");
        }
        int size = size();
        AugmentedMatrixN augMat = new AugmentedMatrixN();
        int augmentedSize = augMat.getAugmentedSize();
        for (int i = 0; i < size; i++) {
            for (int row = 0; row < size; row++) {
                if (i != row) {
                    double ratio = augMat.get(row, i) / augMat.get(i, i);
                    for (int col = 0; col < augmentedSize; col++) {
                        augMat.set(row, col, augMat.get(row, col) - (augMat.get(i, col) * ratio));
                    }
                }
            }
        }
        for (int row2 = 0; row2 < size; row2++) {
            double div = augMat.get(row2, row2);
            for (int col2 = 0; col2 < augmentedSize; col2++) {
                augMat.set(row2, col2, augMat.get(row2, col2) / div);
            }
        }
        return augMat.getAugmentation();
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
    public Matrix4d toMatrix4() {
        return Matrix4d.from(this);
    }

    @Nonnull
    public double[] toArray() {
        return toArray(false);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNf toFloat() {
        int size = size();
        float[] m = new float[size * size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                m[(row * size) + col] = (float) get(row, col);
            }
        }
        return MatrixNf.from(m);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public MatrixNd toDouble() {
        int size = size();
        double[] m = new double[size * size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                m[(row * size) + col] = get(row, col);
            }
        }
        return from(m);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixd
    @Nonnull
    public double[] toArray(boolean columnMajor) {
        int size = size();
        double[] array = new double[size * size];
        if (columnMajor) {
            for (int col = 0; col < size; col++) {
                for (int row = 0; row < size; row++) {
                    array[(col * size) + row] = this.mat[row][col];
                }
            }
        } else {
            for (int row2 = 0; row2 < size; row2++) {
                System.arraycopy(this.mat[row2], 0, array, row2 * size, size);
            }
        }
        return array;
    }

    @Nonnull
    public String toString() {
        int size = size();
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                builder.append(this.mat[row][col]);
                if (col < size - 1) {
                    builder.append(' ');
                }
            }
            int col2 = size - 1;
            if (row < col2) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MatrixNd)) {
            return false;
        }
        return Arrays.deepEquals(this.mat, ((MatrixNd) obj).mat);
    }

    public int hashCode() {
        return Arrays.deepHashCode(this.mat) + 395;
    }

    @Override // 
    @Nonnull
    public MatrixNd clone() {
        return from(this);
    }

    @Nonnull
    public static MatrixNd createScaling(VectorNd v) {
        return createScaling(v.toArray());
    }

    @Nonnull
    public static MatrixNd createScaling(double... vec) {
        int size = vec.length;
        MatrixNd m = from(size);
        for (int rowCol = 0; rowCol < size; rowCol++) {
            m.set(rowCol, rowCol, vec[rowCol]);
        }
        return m;
    }

    @Nonnull
    public static MatrixNd createTranslation(VectorNd v) {
        return createTranslation(v.toArray());
    }

    @Nonnull
    public static MatrixNd createTranslation(double... vec) {
        int size = vec.length;
        MatrixNd m = from(size + 1);
        for (int row = 0; row < size; row++) {
            m.set(row, size, vec[row]);
        }
        return m;
    }

    @Nonnull
    public static MatrixNd createRotation(int size, Complexd rot) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum matrix size is 2");
        }
        MatrixNd m = from(size);
        Complexd rot2 = rot.normalize();
        m.set(0, 0, rot2.getX());
        m.set(0, 1, -rot2.getY());
        m.set(1, 0, rot2.getY());
        m.set(1, 1, rot2.getX());
        return m;
    }

    @Nonnull
    public static MatrixNd createRotation(int size, Quaterniond rot) {
        if (size < 3) {
            throw new IllegalArgumentException("Minimum matrix size is 3");
        }
        MatrixNd m = from(size);
        Quaterniond rot2 = rot.normalize();
        m.set(0, 0, (1.0d - ((rot2.getY() * 2.0d) * rot2.getY())) - ((rot2.getZ() * 2.0d) * rot2.getZ()));
        m.set(0, 1, ((rot2.getX() * 2.0d) * rot2.getY()) - ((rot2.getW() * 2.0d) * rot2.getZ()));
        m.set(0, 2, (rot2.getX() * 2.0d * rot2.getZ()) + (rot2.getW() * 2.0d * rot2.getY()));
        m.set(1, 0, (rot2.getX() * 2.0d * rot2.getY()) + (rot2.getW() * 2.0d * rot2.getZ()));
        m.set(1, 1, (1.0d - ((rot2.getX() * 2.0d) * rot2.getX())) - ((rot2.getZ() * 2.0d) * rot2.getZ()));
        m.set(1, 2, ((rot2.getY() * 2.0d) * rot2.getZ()) - ((rot2.getW() * 2.0d) * rot2.getX()));
        m.set(2, 0, ((rot2.getX() * 2.0d) * rot2.getZ()) - ((rot2.getW() * 2.0d) * rot2.getY()));
        m.set(2, 1, (rot2.getY() * 2.0d * rot2.getZ()) + (rot2.getX() * 2.0d * rot2.getW()));
        m.set(2, 2, (1.0d - ((rot2.getX() * 2.0d) * rot2.getX())) - ((rot2.getY() * 2.0d) * rot2.getY()));
        return m;
    }

    @Nonnull
    public static MatrixNd createLookAt(int size, Vector3d eye, Vector3d at, Vector3d up) {
        if (size < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 4");
        }
        Vector3d f = at.sub(eye).normalize();
        Vector3d s = f.cross(up.normalize()).normalize();
        Vector3d u = s.cross(f).normalize();
        MatrixNd mat = from(size);
        mat.set(0, 0, s.getX());
        mat.set(0, 1, s.getY());
        mat.set(0, 2, s.getZ());
        mat.set(1, 0, u.getX());
        mat.set(1, 1, u.getY());
        mat.set(1, 2, u.getZ());
        mat.set(2, 0, -f.getX());
        mat.set(2, 1, -f.getY());
        mat.set(2, 2, -f.getZ());
        return mat.translate(eye.mul(-1.0f).toVectorN());
    }

    @Nonnull
    public static MatrixNd createPerspective(int size, float fov, float aspect, float near, float far) {
        return createPerspective(size, fov, aspect, near, far);
    }

    @Nonnull
    public static MatrixNd createPerspective(int size, double fov, double aspect, double near, double far) {
        if (size < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 4");
        }
        MatrixNd perspective = from(size);
        double scale = 1.0f / TrigMath.tan(0.008726646259971648d * fov);
        perspective.set(0, 0, scale / aspect);
        perspective.set(1, 1, scale);
        perspective.set(2, 2, (far + near) / (near - far));
        perspective.set(2, 3, ((2.0d * far) * near) / (near - far));
        perspective.set(3, 2, -1.0f);
        perspective.set(3, 3, 0.0f);
        return perspective;
    }

    @Nonnull
    public static MatrixNd createOrthographic(int size, float right, float left, float top, float bottom, float near, float far) {
        return createOrthographic(size, right, left, top, bottom, near, far);
    }

    @Nonnull
    public static MatrixNd createOrthographic(int size, double right, double left, double top, double bottom, double near, double far) {
        if (size < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 4");
        }
        MatrixNd orthographic = from(size);
        orthographic.set(0, 0, 2.0d / (right - left));
        orthographic.set(1, 1, 2.0d / (top - bottom));
        orthographic.set(2, 2, (-2.0d) / (far - near));
        orthographic.set(0, 3, (-(right + left)) / (right - left));
        orthographic.set(1, 3, (-(top + bottom)) / (top - bottom));
        orthographic.set(2, 3, (-(far + near)) / (far - near));
        return orthographic;
    }

    @Nonnull
    private static double[][] deepClone(double[][] array) {
        int size = array.length;
        double[][] clone = (double[][]) array.clone();
        for (int i = 0; i < size; i++) {
            clone[i] = (double[]) array[i].clone();
        }
        return clone;
    }

    /* loaded from: classes5.dex */
    private static class ImmutableIdentityMatrixN extends MatrixNd {
        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd abs() {
            return super.abs();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd ceil() {
            return super.ceil();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd
        @Nonnull
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd div(double d) {
            return super.div(d);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd floor() {
            return super.floor();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord getColumn(int i) {
            return super.getColumn(i);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Vectord getRow(int i) {
            return super.getRow(i);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd invert() {
            return super.invert();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd mul(double d) {
            return super.mul(d);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd negate() {
            return super.negate();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd pow(double d) {
            return super.pow(d);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd round() {
            return super.round();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd toDouble() {
            return super.toDouble();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf toFloat() {
            return super.toFloat();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd, org.cloudburstmc.math.matrix.Matrixd
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd transpose() {
            return super.transpose();
        }

        public ImmutableIdentityMatrixN(int size) {
            super((double[][]) Array.newInstance((Class<?>) Double.TYPE, size, size));
            setIdentity();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd
        public void set(int row, int col, double val) {
            throw new UnsupportedOperationException("You may not alter this matrix");
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNd
        public void setZero() {
            throw new UnsupportedOperationException("You may not alter this matrix");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class AugmentedMatrixN {
        private final MatrixNd aug;
        private final MatrixNd mat;
        private final int size;

        private AugmentedMatrixN(@Nonnull MatrixNd mat) {
            this.mat = mat.clone();
            this.size = mat.size();
            this.aug = MatrixNd.from(this.size);
        }

        /* JADX INFO: Access modifiers changed from: private */
        @Nonnull
        public MatrixNd getAugmentation() {
            return this.aug;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getAugmentedSize() {
            return this.size * 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double get(int row, int col) {
            if (col < this.size) {
                return this.mat.get(row, col);
            }
            return this.aug.get(row, col - this.size);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(int row, int col, double val) {
            if (col < this.size) {
                this.mat.set(row, col, val);
            } else {
                this.aug.set(row, col - this.size, val);
            }
        }
    }

    @Nonnull
    public static MatrixNd from(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum matrix size is 2");
        }
        MatrixNd mat = new MatrixNd((double[][]) Array.newInstance((Class<?>) Double.TYPE, size, size));
        mat.setIdentity();
        return mat;
    }

    @Nonnull
    public static MatrixNd from(Matrix2d m) {
        return new MatrixNd(new double[][]{new double[]{m.get(0, 0), m.get(0, 1)}, new double[]{m.get(1, 0), m.get(1, 1)}});
    }

    @Nonnull
    public static MatrixNd from(Matrix3d m) {
        return new MatrixNd(new double[][]{new double[]{m.get(0, 0), m.get(0, 1), m.get(0, 2)}, new double[]{m.get(1, 0), m.get(1, 1), m.get(1, 2)}, new double[]{m.get(2, 0), m.get(2, 1), m.get(2, 2)}});
    }

    @Nonnull
    public static MatrixNd from(Matrix4d m) {
        return new MatrixNd(new double[][]{new double[]{m.get(0, 0), m.get(0, 1), m.get(0, 2), m.get(0, 3)}, new double[]{m.get(1, 0), m.get(1, 1), m.get(1, 2), m.get(1, 3)}, new double[]{m.get(2, 0), m.get(2, 1), m.get(2, 2), m.get(2, 3)}, new double[]{m.get(3, 0), m.get(3, 1), m.get(3, 2), m.get(3, 3)}});
    }

    @Nonnull
    public static MatrixNd from(double... m) {
        if (m.length < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 2");
        }
        int size = (int) Math.ceil(Math.sqrt(m.length));
        double[][] mat = (double[][]) Array.newInstance((Class<?>) Double.TYPE, size, size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int index = (row * size) + col;
                if (index < m.length) {
                    mat[row][col] = m[index];
                } else {
                    mat[row][col] = 0.0d;
                }
            }
        }
        return new MatrixNd(mat);
    }

    @Nonnull
    public static MatrixNd from(MatrixNd m) {
        return new MatrixNd(deepClone(m.mat));
    }
}
