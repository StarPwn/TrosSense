package io.netty.util.internal.logging;

import java.util.HashSet;
import java.util.Set;

/* loaded from: classes4.dex */
public final class MessageFormatter {
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    public static FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    public static FormattingTuple format(String messagePattern, Object argA, Object argB) {
        return arrayFormat(messagePattern, new Object[]{argA, argB});
    }

    public static FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return new FormattingTuple(messagePattern, null);
        }
        boolean z = true;
        int lastArrIdx = argArray.length - 1;
        Object lastEntry = argArray[lastArrIdx];
        Throwable throwable = lastEntry instanceof Throwable ? (Throwable) lastEntry : null;
        if (messagePattern != null) {
            int j = messagePattern.indexOf(DELIM_STR);
            if (j == -1) {
                return new FormattingTuple(messagePattern, throwable);
            }
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
            int i = 0;
            int L = 0;
            while (true) {
                boolean z2 = false;
                boolean notEscaped = (j == 0 || messagePattern.charAt(j + (-1)) != '\\') ? z : false;
                if (!notEscaped) {
                    sbuf.append((CharSequence) messagePattern, i, j - 1);
                    if (j >= 2 && messagePattern.charAt(j - 2) == '\\') {
                        z2 = true;
                    }
                    notEscaped = z2;
                } else {
                    sbuf.append((CharSequence) messagePattern, i, j);
                }
                i = j + 2;
                if (notEscaped) {
                    deeplyAppendParameter(sbuf, argArray[L], null);
                    L++;
                    if (L > lastArrIdx) {
                        break;
                    }
                } else {
                    sbuf.append(DELIM_STR);
                }
                j = messagePattern.indexOf(DELIM_STR, i);
                if (j == -1) {
                    break;
                }
                z = true;
            }
            sbuf.append((CharSequence) messagePattern, i, messagePattern.length());
            return new FormattingTuple(sbuf.toString(), L <= lastArrIdx ? throwable : null);
        }
        return new FormattingTuple(null, throwable);
    }

    private static void deeplyAppendParameter(StringBuilder sbuf, Object o, Set<Object[]> seenSet) {
        if (o == null) {
            sbuf.append("null");
            return;
        }
        Class<?> objClass = o.getClass();
        if (!objClass.isArray()) {
            if (Number.class.isAssignableFrom(objClass)) {
                if (objClass == Long.class) {
                    sbuf.append(((Long) o).longValue());
                    return;
                }
                if (objClass == Integer.class || objClass == Short.class || objClass == Byte.class) {
                    sbuf.append(((Number) o).intValue());
                    return;
                }
                if (objClass == Double.class) {
                    sbuf.append(((Double) o).doubleValue());
                    return;
                } else if (objClass == Float.class) {
                    sbuf.append(((Float) o).floatValue());
                    return;
                } else {
                    safeObjectAppend(sbuf, o);
                    return;
                }
            }
            safeObjectAppend(sbuf, o);
            return;
        }
        sbuf.append('[');
        if (objClass == boolean[].class) {
            booleanArrayAppend(sbuf, (boolean[]) o);
        } else if (objClass == byte[].class) {
            byteArrayAppend(sbuf, (byte[]) o);
        } else if (objClass == char[].class) {
            charArrayAppend(sbuf, (char[]) o);
        } else if (objClass == short[].class) {
            shortArrayAppend(sbuf, (short[]) o);
        } else if (objClass == int[].class) {
            intArrayAppend(sbuf, (int[]) o);
        } else if (objClass == long[].class) {
            longArrayAppend(sbuf, (long[]) o);
        } else if (objClass == float[].class) {
            floatArrayAppend(sbuf, (float[]) o);
        } else if (objClass == double[].class) {
            doubleArrayAppend(sbuf, (double[]) o);
        } else {
            objectArrayAppend(sbuf, (Object[]) o, seenSet);
        }
        sbuf.append(']');
    }

    private static void safeObjectAppend(StringBuilder sbuf, Object o) {
        try {
            String oAsString = o.toString();
            sbuf.append(oAsString);
        } catch (Throwable t) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + ']');
            t.printStackTrace();
            sbuf.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sbuf, Object[] a, Set<Object[]> seenSet) {
        if (a.length == 0) {
            return;
        }
        if (seenSet == null) {
            seenSet = new HashSet(a.length);
        }
        if (seenSet.add(a)) {
            deeplyAppendParameter(sbuf, a[0], seenSet);
            for (int i = 1; i < a.length; i++) {
                sbuf.append(", ");
                deeplyAppendParameter(sbuf, a[i], seenSet);
            }
            seenSet.remove(a);
            return;
        }
        sbuf.append("...");
    }

    private static void booleanArrayAppend(StringBuilder sbuf, boolean[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append(a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append(a[i]);
        }
    }

    private static void byteArrayAppend(StringBuilder sbuf, byte[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append((int) a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append((int) a[i]);
        }
    }

    private static void charArrayAppend(StringBuilder sbuf, char[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append(a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append(a[i]);
        }
    }

    private static void shortArrayAppend(StringBuilder sbuf, short[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append((int) a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append((int) a[i]);
        }
    }

    private static void intArrayAppend(StringBuilder sbuf, int[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append(a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append(a[i]);
        }
    }

    private static void longArrayAppend(StringBuilder sbuf, long[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append(a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append(a[i]);
        }
    }

    private static void floatArrayAppend(StringBuilder sbuf, float[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append(a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append(a[i]);
        }
    }

    private static void doubleArrayAppend(StringBuilder sbuf, double[] a) {
        if (a.length == 0) {
            return;
        }
        sbuf.append(a[0]);
        for (int i = 1; i < a.length; i++) {
            sbuf.append(", ");
            sbuf.append(a[i]);
        }
    }

    private MessageFormatter() {
    }
}
