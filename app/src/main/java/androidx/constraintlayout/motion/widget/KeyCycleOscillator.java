package androidx.constraintlayout.motion.widget;

import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.core.app.NotificationCompat;
import io.netty.util.internal.StringUtil;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class KeyCycleOscillator {
    private static final String TAG = "KeyCycleOscillator";
    private CurveFit mCurveFit;
    protected ConstraintAttribute mCustom;
    private CycleOscillator mCycleOscillator;
    private String mType;
    private int mWaveShape = 0;
    public int mVariesBy = 0;
    ArrayList<WavePoint> mWavePoints = new ArrayList<>();

    public abstract void setProperty(View view, float f);

    public boolean variesByPath() {
        return this.mVariesBy == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class WavePoint {
        float mOffset;
        float mPeriod;
        int mPosition;
        float mValue;

        public WavePoint(int position, float period, float offset, float value) {
            this.mPosition = position;
            this.mValue = value;
            this.mOffset = offset;
            this.mPeriod = period;
        }
    }

    public String toString() {
        String str = this.mType;
        DecimalFormat df = new DecimalFormat("##.##");
        Iterator<WavePoint> it2 = this.mWavePoints.iterator();
        while (it2.hasNext()) {
            WavePoint wp = it2.next();
            str = str + "[" + wp.mPosition + " , " + df.format(wp.mValue) + "] ";
        }
        return str;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public float get(float t) {
        return (float) this.mCycleOscillator.getValues(t);
    }

    public float getSlope(float position) {
        return (float) this.mCycleOscillator.getSlope(position);
    }

    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static KeyCycleOscillator makeSpline(String str) {
        char c;
        if (str.startsWith("CUSTOM")) {
            return new CustomSet();
        }
        switch (str.hashCode()) {
            case -1249320806:
                if (str.equals("rotationX")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1249320805:
                if (str.equals("rotationY")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1225497657:
                if (str.equals("translationX")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1225497656:
                if (str.equals("translationY")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1225497655:
                if (str.equals("translationZ")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1001078227:
                if (str.equals(NotificationCompat.CATEGORY_PROGRESS)) {
                    c = StringUtil.CARRIAGE_RETURN;
                    break;
                }
                c = 65535;
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -797520672:
                if (str.equals("waveVariesBy")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 156108012:
                if (str.equals("waveOffset")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return new AlphaSet();
            case 1:
                return new ElevationSet();
            case 2:
                return new RotationSet();
            case 3:
                return new RotationXset();
            case 4:
                return new RotationYset();
            case 5:
                return new PathRotateSet();
            case 6:
                return new ScaleXset();
            case 7:
                return new ScaleYset();
            case '\b':
                return new AlphaSet();
            case '\t':
                return new AlphaSet();
            case '\n':
                return new TranslationXset();
            case 11:
                return new TranslationYset();
            case '\f':
                return new TranslationZset();
            case '\r':
                return new ProgressSet();
            default:
                return null;
        }
    }

    public void setPoint(int framePosition, int shape, int variesBy, float period, float offset, float value, ConstraintAttribute custom) {
        this.mWavePoints.add(new WavePoint(framePosition, period, offset, value));
        if (variesBy != -1) {
            this.mVariesBy = variesBy;
        }
        this.mWaveShape = shape;
        this.mCustom = custom;
    }

    public void setPoint(int framePosition, int shape, int variesBy, float period, float offset, float value) {
        this.mWavePoints.add(new WavePoint(framePosition, period, offset, value));
        if (variesBy != -1) {
            this.mVariesBy = variesBy;
        }
        this.mWaveShape = shape;
    }

    public void setup(float pathLength) {
        int count = this.mWavePoints.size();
        if (count == 0) {
            return;
        }
        Collections.sort(this.mWavePoints, new Comparator<WavePoint>() { // from class: androidx.constraintlayout.motion.widget.KeyCycleOscillator.1
            @Override // java.util.Comparator
            public int compare(WavePoint lhs, WavePoint rhs) {
                return Integer.compare(lhs.mPosition, rhs.mPosition);
            }
        });
        double[] time = new double[count];
        double[][] values = (double[][]) Array.newInstance((Class<?>) Double.TYPE, count, 2);
        this.mCycleOscillator = new CycleOscillator(this.mWaveShape, this.mVariesBy, count);
        int i = 0;
        Iterator<WavePoint> it2 = this.mWavePoints.iterator();
        while (it2.hasNext()) {
            WavePoint wp = it2.next();
            time[i] = wp.mPeriod * 0.01d;
            values[i][0] = wp.mValue;
            values[i][1] = wp.mOffset;
            this.mCycleOscillator.setPoint(i, wp.mPosition, wp.mPeriod, wp.mOffset, wp.mValue);
            i++;
        }
        this.mCycleOscillator.setup(pathLength);
        this.mCurveFit = CurveFit.get(0, time, values);
    }

    /* loaded from: classes.dex */
    static class ElevationSet extends KeyCycleOscillator {
        ElevationSet() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setElevation(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class AlphaSet extends KeyCycleOscillator {
        AlphaSet() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setAlpha(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class RotationSet extends KeyCycleOscillator {
        RotationSet() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setRotation(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class RotationXset extends KeyCycleOscillator {
        RotationXset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setRotationX(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class RotationYset extends KeyCycleOscillator {
        RotationYset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setRotationY(get(t));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PathRotateSet extends KeyCycleOscillator {
        PathRotateSet() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
        }

        public void setPathRotate(View view, float t, double dx, double dy) {
            view.setRotation(get(t) + ((float) Math.toDegrees(Math.atan2(dy, dx))));
        }
    }

    /* loaded from: classes.dex */
    static class ScaleXset extends KeyCycleOscillator {
        ScaleXset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setScaleX(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class ScaleYset extends KeyCycleOscillator {
        ScaleYset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setScaleY(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class TranslationXset extends KeyCycleOscillator {
        TranslationXset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setTranslationX(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class TranslationYset extends KeyCycleOscillator {
        TranslationYset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setTranslationY(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class TranslationZset extends KeyCycleOscillator {
        TranslationZset() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            view.setTranslationZ(get(t));
        }
    }

    /* loaded from: classes.dex */
    static class CustomSet extends KeyCycleOscillator {
        float[] value = new float[1];

        CustomSet() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            this.value[0] = get(t);
            this.mCustom.setInterpolatedValue(view, this.value);
        }
    }

    /* loaded from: classes.dex */
    static class ProgressSet extends KeyCycleOscillator {
        boolean mNoMethod = false;

        ProgressSet() {
        }

        @Override // androidx.constraintlayout.motion.widget.KeyCycleOscillator
        public void setProperty(View view, float t) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(get(t));
                return;
            }
            if (this.mNoMethod) {
                return;
            }
            Method method = null;
            try {
                method = view.getClass().getMethod("setProgress", Float.TYPE);
            } catch (NoSuchMethodException e) {
                this.mNoMethod = true;
            }
            if (method != null) {
                try {
                    method.invoke(view, Float.valueOf(get(t)));
                } catch (IllegalAccessException e2) {
                    Log.e(KeyCycleOscillator.TAG, "unable to setProgress", e2);
                } catch (InvocationTargetException e3) {
                    Log.e(KeyCycleOscillator.TAG, "unable to setProgress", e3);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static class IntDoubleSort {
        private IntDoubleSort() {
        }

        static void sort(int[] key, float[] value, int low, int hi) {
            int[] stack = new int[key.length + 10];
            int count = 0 + 1;
            stack[0] = hi;
            int count2 = count + 1;
            stack[count] = low;
            while (count2 > 0) {
                int count3 = count2 - 1;
                int low2 = stack[count3];
                count2 = count3 - 1;
                int hi2 = stack[count2];
                if (low2 < hi2) {
                    int p = partition(key, value, low2, hi2);
                    int count4 = count2 + 1;
                    stack[count2] = p - 1;
                    int count5 = count4 + 1;
                    stack[count4] = low2;
                    int count6 = count5 + 1;
                    stack[count5] = hi2;
                    count2 = count6 + 1;
                    stack[count6] = p + 1;
                }
            }
        }

        private static int partition(int[] array, float[] value, int low, int hi) {
            int pivot = array[hi];
            int i = low;
            for (int j = low; j < hi; j++) {
                if (array[j] <= pivot) {
                    swap(array, value, i, j);
                    i++;
                }
            }
            swap(array, value, i, hi);
            return i;
        }

        private static void swap(int[] array, float[] value, int a, int b) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            float tmpv = value[a];
            value[a] = value[b];
            value[b] = tmpv;
        }
    }

    /* loaded from: classes.dex */
    private static class IntFloatFloatSort {
        private IntFloatFloatSort() {
        }

        static void sort(int[] key, float[] value1, float[] value2, int low, int hi) {
            int[] stack = new int[key.length + 10];
            int count = 0 + 1;
            stack[0] = hi;
            int count2 = count + 1;
            stack[count] = low;
            while (count2 > 0) {
                int count3 = count2 - 1;
                int low2 = stack[count3];
                count2 = count3 - 1;
                int hi2 = stack[count2];
                if (low2 < hi2) {
                    int p = partition(key, value1, value2, low2, hi2);
                    int count4 = count2 + 1;
                    stack[count2] = p - 1;
                    int count5 = count4 + 1;
                    stack[count4] = low2;
                    int count6 = count5 + 1;
                    stack[count5] = hi2;
                    count2 = count6 + 1;
                    stack[count6] = p + 1;
                }
            }
        }

        private static int partition(int[] array, float[] value1, float[] value2, int low, int hi) {
            int pivot = array[hi];
            int i = low;
            for (int j = low; j < hi; j++) {
                if (array[j] <= pivot) {
                    swap(array, value1, value2, i, j);
                    i++;
                }
            }
            swap(array, value1, value2, i, hi);
            return i;
        }

        private static void swap(int[] array, float[] value1, float[] value2, int a, int b) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
            float tmpFloat = value1[a];
            value1[a] = value1[b];
            value1[b] = tmpFloat;
            float tmpFloat2 = value2[a];
            value2[a] = value2[b];
            value2[b] = tmpFloat2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class CycleOscillator {
        private static final String TAG = "CycleOscillator";
        static final int UNSET = -1;
        CurveFit mCurveFit;
        float[] mOffset;
        float mPathLength;
        float[] mPeriod;
        double[] mPosition;
        float[] mScale;
        double[] mSplineSlopeCache;
        double[] mSplineValueCache;
        float[] mValues;
        private final int mVariesBy;
        int mWaveShape;
        Oscillator mOscillator = new Oscillator();
        public HashMap<String, ConstraintAttribute> mCustomConstraints = new HashMap<>();

        CycleOscillator(int waveShape, int variesBy, int steps) {
            this.mWaveShape = waveShape;
            this.mVariesBy = variesBy;
            this.mOscillator.setType(waveShape);
            this.mValues = new float[steps];
            this.mPosition = new double[steps];
            this.mPeriod = new float[steps];
            this.mOffset = new float[steps];
            this.mScale = new float[steps];
        }

        public double getValues(float time) {
            if (this.mCurveFit != null) {
                this.mCurveFit.getPos(time, this.mSplineValueCache);
            } else {
                this.mSplineValueCache[0] = this.mOffset[0];
                this.mSplineValueCache[1] = this.mValues[0];
            }
            double offset = this.mSplineValueCache[0];
            double waveValue = this.mOscillator.getValue(time);
            return (this.mSplineValueCache[1] * waveValue) + offset;
        }

        public double getSlope(float time) {
            if (this.mCurveFit != null) {
                this.mCurveFit.getSlope(time, this.mSplineSlopeCache);
                this.mCurveFit.getPos(time, this.mSplineValueCache);
            } else {
                this.mSplineSlopeCache[0] = 0.0d;
                this.mSplineSlopeCache[1] = 0.0d;
            }
            double waveValue = this.mOscillator.getValue(time);
            double waveSlope = this.mOscillator.getSlope(time);
            return this.mSplineSlopeCache[0] + (this.mSplineSlopeCache[1] * waveValue) + (this.mSplineValueCache[1] * waveSlope);
        }

        private ConstraintAttribute get(String attributeName, ConstraintAttribute.AttributeType attributeType) {
            if (this.mCustomConstraints.containsKey(attributeName)) {
                ConstraintAttribute ret = this.mCustomConstraints.get(attributeName);
                if (ret.getType() != attributeType) {
                    throw new IllegalArgumentException("ConstraintAttribute is already a " + ret.getType().name());
                }
                return ret;
            }
            ConstraintAttribute ret2 = new ConstraintAttribute(attributeName, attributeType);
            this.mCustomConstraints.put(attributeName, ret2);
            return ret2;
        }

        public void setPoint(int index, int framePosition, float wavePeriod, float offset, float values) {
            this.mPosition[index] = framePosition / 100.0d;
            this.mPeriod[index] = wavePeriod;
            this.mOffset[index] = offset;
            this.mValues[index] = values;
        }

        public void setup(float pathLength) {
            this.mPathLength = pathLength;
            double[][] splineValues = (double[][]) Array.newInstance((Class<?>) Double.TYPE, this.mPosition.length, 2);
            this.mSplineValueCache = new double[this.mValues.length + 1];
            this.mSplineSlopeCache = new double[this.mValues.length + 1];
            if (this.mPosition[0] > 0.0d) {
                this.mOscillator.addPoint(0.0d, this.mPeriod[0]);
            }
            int last = this.mPosition.length - 1;
            if (this.mPosition[last] < 1.0d) {
                this.mOscillator.addPoint(1.0d, this.mPeriod[last]);
            }
            for (int i = 0; i < splineValues.length; i++) {
                splineValues[i][0] = this.mOffset[i];
                for (int j = 0; j < this.mValues.length; j++) {
                    splineValues[j][1] = this.mValues[j];
                }
                this.mOscillator.addPoint(this.mPosition[i], this.mPeriod[i]);
            }
            this.mOscillator.normalize();
            if (this.mPosition.length > 1) {
                this.mCurveFit = CurveFit.get(0, this.mPosition, splineValues);
            } else {
                this.mCurveFit = null;
            }
        }
    }
}
