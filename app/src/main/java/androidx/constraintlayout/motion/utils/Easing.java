package androidx.constraintlayout.motion.utils;

import android.util.Log;
import java.util.Arrays;

/* loaded from: classes.dex */
public class Easing {
    private static final String ACCELERATE = "cubic(0.4, 0.05, 0.8, 0.7)";
    private static final String DECELERATE = "cubic(0.0, 0.0, 0.2, 0.95)";
    private static final String LINEAR = "cubic(1, 1, 0, 0)";
    private static final String STANDARD = "cubic(0.4, 0.0, 0.2, 1)";
    String str = "identity";
    static Easing sDefault = new Easing();
    private static final String STANDARD_NAME = "standard";
    private static final String ACCELERATE_NAME = "accelerate";
    private static final String DECELERATE_NAME = "decelerate";
    private static final String LINEAR_NAME = "linear";
    public static String[] NAMED_EASING = {STANDARD_NAME, ACCELERATE_NAME, DECELERATE_NAME, LINEAR_NAME};

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static Easing getInterpolator(String configString) {
        char c;
        if (configString == null) {
            return null;
        }
        if (configString.startsWith("cubic")) {
            return new CubicEasing(configString);
        }
        switch (configString.hashCode()) {
            case -1354466595:
                if (configString.equals(ACCELERATE_NAME)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1263948740:
                if (configString.equals(DECELERATE_NAME)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1102672091:
                if (configString.equals(LINEAR_NAME)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1312628413:
                if (configString.equals(STANDARD_NAME)) {
                    c = 0;
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
                return new CubicEasing(STANDARD);
            case 1:
                return new CubicEasing(ACCELERATE);
            case 2:
                return new CubicEasing(DECELERATE);
            case 3:
                return new CubicEasing(LINEAR);
            default:
                Log.e("ConstraintSet", "transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or " + Arrays.toString(NAMED_EASING));
                return sDefault;
        }
    }

    public double get(double x) {
        return x;
    }

    public String toString() {
        return this.str;
    }

    public double getDiff(double x) {
        return 1.0d;
    }

    /* loaded from: classes.dex */
    static class CubicEasing extends Easing {
        double x1;
        double x2;
        double y1;
        double y2;
        private static double error = 0.01d;
        private static double d_error = 1.0E-4d;

        CubicEasing(String configString) {
            this.str = configString;
            int start = configString.indexOf(40);
            int off1 = configString.indexOf(44, start);
            this.x1 = Double.parseDouble(configString.substring(start + 1, off1).trim());
            int off2 = configString.indexOf(44, off1 + 1);
            this.y1 = Double.parseDouble(configString.substring(off1 + 1, off2).trim());
            int off3 = configString.indexOf(44, off2 + 1);
            this.x2 = Double.parseDouble(configString.substring(off2 + 1, off3).trim());
            int end = configString.indexOf(41, off3 + 1);
            this.y2 = Double.parseDouble(configString.substring(off3 + 1, end).trim());
        }

        public CubicEasing(double x1, double y1, double x2, double y2) {
            setup(x1, y1, x2, y2);
        }

        void setup(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        private double getX(double t) {
            double t1 = 1.0d - t;
            double f1 = t1 * 3.0d * t1 * t;
            double f2 = 3.0d * t1 * t * t;
            double f3 = t * t * t;
            return (this.x1 * f1) + (this.x2 * f2) + f3;
        }

        private double getY(double t) {
            double t1 = 1.0d - t;
            double f1 = t1 * 3.0d * t1 * t;
            double f2 = 3.0d * t1 * t * t;
            double f3 = t * t * t;
            return (this.y1 * f1) + (this.y2 * f2) + f3;
        }

        private double getDiffX(double t) {
            double t1 = 1.0d - t;
            return (t1 * 3.0d * t1 * this.x1) + (6.0d * t1 * t * (this.x2 - this.x1)) + (3.0d * t * t * (1.0d - this.x2));
        }

        private double getDiffY(double t) {
            double t1 = 1.0d - t;
            return (t1 * 3.0d * t1 * this.y1) + (6.0d * t1 * t * (this.y2 - this.y1)) + (3.0d * t * t * (1.0d - this.y2));
        }

        @Override // androidx.constraintlayout.motion.utils.Easing
        public double getDiff(double x) {
            double t = 0.5d;
            double range = 0.5d;
            while (range > d_error) {
                double tx = getX(t);
                range *= 0.5d;
                if (tx < x) {
                    t += range;
                } else {
                    t -= range;
                }
            }
            double x1 = getX(t - range);
            double x2 = getX(t + range);
            double y1 = getY(t - range);
            double y2 = getY(t + range);
            return (y2 - y1) / (x2 - x1);
        }

        @Override // androidx.constraintlayout.motion.utils.Easing
        public double get(double x) {
            if (x <= 0.0d) {
                return 0.0d;
            }
            if (x >= 1.0d) {
                return 1.0d;
            }
            double t = 0.5d;
            double range = 0.5d;
            while (range > error) {
                double tx = getX(t);
                range *= 0.5d;
                if (tx < x) {
                    t += range;
                } else {
                    t -= range;
                }
            }
            double x1 = getX(t - range);
            double x2 = getX(t + range);
            double y1 = getY(t - range);
            double y2 = getY(t + range);
            return (((y2 - y1) * (x - x1)) / (x2 - x1)) + y1;
        }
    }
}
