package org.cloudburstmc.math.matrix;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.cloudburstmc.math.GenericMath;
import org.cloudburstmc.math.TrigMath;
import org.cloudburstmc.math.imaginary.Complexf;
import org.cloudburstmc.math.imaginary.Quaternionf;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.VectorNf;
import org.cloudburstmc.math.vector.Vectorf;

@ParametersAreNonnullByDefault
/* loaded from: classes5.dex */
public class MatrixNf implements Matrixf, Serializable, Cloneable {
    public static final MatrixNf IDENTITY_2 = new ImmutableIdentityMatrixN(2);
    public static final MatrixNf IDENTITY_3 = new ImmutableIdentityMatrixN(3);
    public static final MatrixNf IDENTITY_4 = new ImmutableIdentityMatrixN(4);
    private static final long serialVersionUID = 1;
    private final float[][] mat;

    private MatrixNf(float[][] mat) {
        this.mat = mat;
    }

    public int size() {
        return this.mat.length;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float get(int row, int col) {
        return this.mat[row][col];
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public VectorNf getRow(int row) {
        int size = size();
        VectorNf d = VectorNf.from(size);
        for (int col = 0; col < size; col++) {
            d.set(col, get(row, col));
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public VectorNf getColumn(int col) {
        int size = size();
        VectorNf d = VectorNf.from(size);
        for (int row = 0; row < size; row++) {
            d.set(row, get(row, col));
        }
        return d;
    }

    public void set(int row, int col, double val) {
        set(row, col, (float) val);
    }

    public void set(int row, int col, float val) {
        this.mat[row][col] = val;
    }

    public final void setIdentity() {
        int size = size();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == col) {
                    this.mat[row][col] = 1.0f;
                } else {
                    this.mat[row][col] = 0.0f;
                }
            }
        }
    }

    public void setZero() {
        int size = size();
        for (int row = 0; row < size; row++) {
            Arrays.fill(this.mat[row], 0.0f);
        }
    }

    @Nonnull
    public MatrixNf resize(int size) {
        MatrixNf d = from(size);
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
    public MatrixNf add(MatrixNf m) {
        int size = size();
        if (size != m.size()) {
            throw new IllegalArgumentException("Matrix sizes must be the same");
        }
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] + m.mat[row][col];
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNf sub(MatrixNf m) {
        int size = size();
        if (size != m.size()) {
            throw new IllegalArgumentException("Matrix sizes must be the same");
        }
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] - m.mat[row][col];
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNf mul(double a) {
        return mul((float) a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf mul(float a) {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] * a;
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNf mul(MatrixNf m) {
        int size = size();
        if (size != m.size()) {
            throw new IllegalArgumentException("Matrix sizes must be the same");
        }
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                float dot = 0.0f;
                for (int i = 0; i < size; i++) {
                    dot += this.mat[row][i] * m.mat[i][col];
                }
                d.mat[row][col] = dot;
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNf div(double a) {
        return div((float) a);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf div(float a) {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[row][col] / a;
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNf div(MatrixNf m) {
        return mul(m.invert());
    }

    @Nonnull
    public MatrixNf pow(double pow) {
        return pow((float) pow);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf pow(float pow) {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = (float) Math.pow(this.mat[row][col], pow);
            }
        }
        return d;
    }

    @Nonnull
    public MatrixNf translate(VectorNf v) {
        return translate(v.toArray());
    }

    @Nonnull
    public MatrixNf translate(float... v) {
        return createTranslation(v).mul(this);
    }

    @Nonnull
    public MatrixNf scale(VectorNf v) {
        return scale(v.toArray());
    }

    @Nonnull
    public MatrixNf scale(float... v) {
        return createScaling(v).mul(this);
    }

    @Nonnull
    public MatrixNf rotate(Complexf rot) {
        return createRotation(size(), rot).mul(this);
    }

    @Nonnull
    public MatrixNf rotate(Quaternionf rot) {
        return createRotation(size(), rot).mul(this);
    }

    @Nonnull
    public VectorNf transform(VectorNf v) {
        return transform(v.toArray());
    }

    @Nonnull
    public VectorNf transform(float... vec) {
        int size = size();
        if (size != vec.length) {
            throw new IllegalArgumentException("Matrix and vector sizes must be the same");
        }
        VectorNf d = VectorNf.from(size);
        for (int row = 0; row < size; row++) {
            float dot = 0.0f;
            for (int col = 0; col < size; col++) {
                dot += this.mat[row][col] * vec[col];
            }
            d.set(row, dot);
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf floor() {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = GenericMath.floor(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf ceil() {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = (float) Math.ceil(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf round() {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = Math.round(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf abs() {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = Math.abs(this.mat[row][col]);
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf negate() {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = -this.mat[row][col];
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf transpose() {
        int size = size();
        MatrixNf d = from(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                d.mat[row][col] = this.mat[col][row];
            }
        }
        return d;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float trace() {
        int size = size();
        float trace = 0.0f;
        for (int rowCol = 0; rowCol < size; rowCol++) {
            trace += this.mat[rowCol][rowCol];
        }
        return trace;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    public float determinant() {
        int size = size();
        float[][] m = deepClone(this.mat);
        for (int i = 0; i < size - 1; i++) {
            for (int col = i + 1; col < size; col++) {
                float det = m[i][i] < GenericMath.FLT_EPSILON ? 0.0f : m[i][col] / m[i][i];
                for (int row = i; row < size; row++) {
                    float[] fArr = m[row];
                    fArr[col] = fArr[col] - (m[row][i] * det);
                }
            }
        }
        float det2 = 1.0f;
        for (int i2 = 0; i2 < size; i2++) {
            det2 *= m[i2][i2];
        }
        return det2;
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf invert() {
        if (Math.abs(determinant()) < GenericMath.FLT_EPSILON) {
            throw new ArithmeticException("Cannot inverse a matrix with a zero determinant");
        }
        int size = size();
        AugmentedMatrixN augMat = new AugmentedMatrixN();
        int augmentedSize = augMat.getAugmentedSize();
        for (int i = 0; i < size; i++) {
            for (int row = 0; row < size; row++) {
                if (i != row) {
                    float ratio = augMat.get(row, i) / augMat.get(i, i);
                    for (int col = 0; col < augmentedSize; col++) {
                        augMat.set(row, col, augMat.get(row, col) - (augMat.get(i, col) * ratio));
                    }
                }
            }
        }
        for (int row2 = 0; row2 < size; row2++) {
            float div = augMat.get(row2, row2);
            for (int col2 = 0; col2 < augmentedSize; col2++) {
                augMat.set(row2, col2, augMat.get(row2, col2) / div);
            }
        }
        return augMat.getAugmentation();
    }

    @Nonnull
    public Matrix2f toMatrix2() {
        return Matrix2f.from(this);
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
    public float[] toArray() {
        return toArray(false);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNf toFloat() {
        int size = size();
        float[] m = new float[size * size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                m[(row * size) + col] = get(row, col);
            }
        }
        return from(m);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public MatrixNd toDouble() {
        int size = size();
        double[] m = new double[size * size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                m[(row * size) + col] = get(row, col);
            }
        }
        return MatrixNd.from(m);
    }

    @Override // org.cloudburstmc.math.matrix.Matrixf
    @Nonnull
    public float[] toArray(boolean columnMajor) {
        int size = size();
        float[] array = new float[size * size];
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
        if (!(obj instanceof MatrixNf)) {
            return false;
        }
        return Arrays.deepEquals(this.mat, ((MatrixNf) obj).mat);
    }

    public int hashCode() {
        return Arrays.deepHashCode(this.mat) + 395;
    }

    @Override // 
    @Nonnull
    public MatrixNf clone() {
        return from(this);
    }

    @Nonnull
    public static MatrixNf createScaling(VectorNf v) {
        return createScaling(v.toArray());
    }

    @Nonnull
    public static MatrixNf createScaling(float... vec) {
        int size = vec.length;
        MatrixNf m = from(size);
        for (int rowCol = 0; rowCol < size; rowCol++) {
            m.set(rowCol, rowCol, vec[rowCol]);
        }
        return m;
    }

    @Nonnull
    public static MatrixNf createTranslation(VectorNf v) {
        return createTranslation(v.toArray());
    }

    @Nonnull
    public static MatrixNf createTranslation(float... vec) {
        int size = vec.length;
        MatrixNf m = from(size + 1);
        for (int row = 0; row < size; row++) {
            m.set(row, size, vec[row]);
        }
        return m;
    }

    @Nonnull
    public static MatrixNf createRotation(int size, Complexf rot) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum matrix size is 2");
        }
        MatrixNf m = from(size);
        Complexf rot2 = rot.normalize();
        m.set(0, 0, rot2.getX());
        m.set(0, 1, -rot2.getY());
        m.set(1, 0, rot2.getY());
        m.set(1, 1, rot2.getX());
        return m;
    }

    @Nonnull
    public static MatrixNf createRotation(int size, Quaternionf rot) {
        if (size < 3) {
            throw new IllegalArgumentException("Minimum matrix size is 3");
        }
        MatrixNf m = from(size);
        Quaternionf rot2 = rot.normalize();
        m.set(0, 0, (1.0f - ((rot2.getY() * 2.0f) * rot2.getY())) - ((rot2.getZ() * 2.0f) * rot2.getZ()));
        m.set(0, 1, ((rot2.getX() * 2.0f) * rot2.getY()) - ((rot2.getW() * 2.0f) * rot2.getZ()));
        m.set(0, 2, (rot2.getX() * 2.0f * rot2.getZ()) + (rot2.getW() * 2.0f * rot2.getY()));
        m.set(1, 0, (rot2.getX() * 2.0f * rot2.getY()) + (rot2.getW() * 2.0f * rot2.getZ()));
        m.set(1, 1, (1.0f - ((rot2.getX() * 2.0f) * rot2.getX())) - ((rot2.getZ() * 2.0f) * rot2.getZ()));
        m.set(1, 2, ((rot2.getY() * 2.0f) * rot2.getZ()) - ((rot2.getW() * 2.0f) * rot2.getX()));
        m.set(2, 0, ((rot2.getX() * 2.0f) * rot2.getZ()) - ((rot2.getW() * 2.0f) * rot2.getY()));
        m.set(2, 1, (rot2.getY() * 2.0f * rot2.getZ()) + (rot2.getX() * 2.0f * rot2.getW()));
        m.set(2, 2, (1.0f - ((rot2.getX() * 2.0f) * rot2.getX())) - ((rot2.getY() * 2.0f) * rot2.getY()));
        return m;
    }

    @Nonnull
    public static MatrixNf createLookAt(int size, Vector3f eye, Vector3f at, Vector3f up) {
        if (size < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 4");
        }
        Vector3f f = at.sub(eye).normalize();
        Vector3f s = f.cross(up.normalize()).normalize();
        Vector3f u = s.cross(f).normalize();
        MatrixNf mat = from(size);
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
    public static MatrixNf createPerspective(int size, double fov, double aspect, double near, double far) {
        return createPerspective(size, (float) fov, (float) aspect, (float) near, (float) far);
    }

    @Nonnull
    public static MatrixNf createPerspective(int size, float fov, float aspect, float near, float far) {
        if (size < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 4");
        }
        MatrixNf perspective = from(size);
        float scale = 1.0f / TrigMath.tan(0.008726646f * fov);
        perspective.set(0, 0, scale / aspect);
        perspective.set(1, 1, scale);
        perspective.set(2, 2, (far + near) / (near - far));
        perspective.set(2, 3, ((2.0f * far) * near) / (near - far));
        perspective.set(3, 2, -1.0f);
        perspective.set(3, 3, 0.0f);
        return perspective;
    }

    @Nonnull
    public static MatrixNf createOrthographic(int size, double right, double left, double top, double bottom, double near, double far) {
        return createOrthographic(size, (float) right, (float) left, (float) top, (float) bottom, (float) near, (float) far);
    }

    @Nonnull
    public static MatrixNf createOrthographic(int size, float right, float left, float top, float bottom, float near, float far) {
        if (size < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 4");
        }
        MatrixNf orthographic = from(size);
        orthographic.set(0, 0, 2.0f / (right - left));
        orthographic.set(1, 1, 2.0f / (top - bottom));
        orthographic.set(2, 2, (-2.0f) / (far - near));
        orthographic.set(0, 3, (-(right + left)) / (right - left));
        orthographic.set(1, 3, (-(top + bottom)) / (top - bottom));
        orthographic.set(2, 3, (-(far + near)) / (far - near));
        return orthographic;
    }

    @Nonnull
    private static float[][] deepClone(float[][] array) {
        int size = array.length;
        float[][] clone = (float[][]) array.clone();
        for (int i = 0; i < size; i++) {
            clone[i] = (float[]) array[i].clone();
        }
        return clone;
    }

    /* loaded from: classes5.dex */
    private static class ImmutableIdentityMatrixN extends MatrixNf {
        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf abs() {
            return super.abs();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf ceil() {
            return super.ceil();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf
        @Nonnull
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf div(float f) {
            return super.div(f);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf floor() {
            return super.floor();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf getColumn(int i) {
            return super.getColumn(i);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Vectorf getRow(int i) {
            return super.getRow(i);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf invert() {
            return super.invert();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf mul(float f) {
            return super.mul(f);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf negate() {
            return super.negate();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf pow(float f) {
            return super.pow(f);
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf round() {
            return super.round();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixd toDouble() {
            return super.toDouble();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf toFloat() {
            return super.toFloat();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf, org.cloudburstmc.math.matrix.Matrixf
        @Nonnull
        public /* bridge */ /* synthetic */ Matrixf transpose() {
            return super.transpose();
        }

        public ImmutableIdentityMatrixN(int size) {
            super((float[][]) Array.newInstance((Class<?>) Float.TYPE, size, size));
            setIdentity();
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf
        public void set(int row, int col, float val) {
            throw new UnsupportedOperationException("You may not alter this matrix");
        }

        @Override // org.cloudburstmc.math.matrix.MatrixNf
        public void setZero() {
            throw new UnsupportedOperationException("You may not alter this matrix");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class AugmentedMatrixN {
        private final MatrixNf aug;
        private final MatrixNf mat;
        private final int size;

        private AugmentedMatrixN(@Nonnull MatrixNf mat) {
            this.mat = mat.clone();
            this.size = mat.size();
            this.aug = MatrixNf.from(this.size);
        }

        /* JADX INFO: Access modifiers changed from: private */
        @Nonnull
        public MatrixNf getAugmentation() {
            return this.aug;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getAugmentedSize() {
            return this.size * 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public float get(int row, int col) {
            if (col < this.size) {
                return this.mat.get(row, col);
            }
            return this.aug.get(row, col - this.size);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(int row, int col, float val) {
            if (col < this.size) {
                this.mat.set(row, col, val);
            } else {
                this.aug.set(row, col - this.size, val);
            }
        }
    }

    @Nonnull
    public static MatrixNf from(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Minimum matrix size is 2");
        }
        MatrixNf mat = new MatrixNf((float[][]) Array.newInstance((Class<?>) Float.TYPE, size, size));
        mat.setIdentity();
        return mat;
    }

    @Nonnull
    public static MatrixNf from(Matrix2f m) {
        return new MatrixNf(new float[][]{new float[]{m.get(0, 0), m.get(0, 1)}, new float[]{m.get(1, 0), m.get(1, 1)}});
    }

    @Nonnull
    public static MatrixNf from(Matrix3f m) {
        return new MatrixNf(new float[][]{new float[]{m.get(0, 0), m.get(0, 1), m.get(0, 2)}, new float[]{m.get(1, 0), m.get(1, 1), m.get(1, 2)}, new float[]{m.get(2, 0), m.get(2, 1), m.get(2, 2)}});
    }

    @Nonnull
    public static MatrixNf from(Matrix4f m) {
        return new MatrixNf(new float[][]{new float[]{m.get(0, 0), m.get(0, 1), m.get(0, 2), m.get(0, 3)}, new float[]{m.get(1, 0), m.get(1, 1), m.get(1, 2), m.get(1, 3)}, new float[]{m.get(2, 0), m.get(2, 1), m.get(2, 2), m.get(2, 3)}, new float[]{m.get(3, 0), m.get(3, 1), m.get(3, 2), m.get(3, 3)}});
    }

    @Nonnull
    public static MatrixNf from(float... m) {
        if (m.length < 4) {
            throw new IllegalArgumentException("Minimum matrix size is 2");
        }
        int size = (int) Math.ceil(Math.sqrt(m.length));
        float[][] mat = (float[][]) Array.newInstance((Class<?>) Float.TYPE, size, size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int index = (row * size) + col;
                if (index < m.length) {
                    mat[row][col] = m[index];
                } else {
                    mat[row][col] = 0.0f;
                }
            }
        }
        return new MatrixNf(mat);
    }

    @Nonnull
    public static MatrixNf from(MatrixNf m) {
        return new MatrixNf(deepClone(m.mat));
    }
}
