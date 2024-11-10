package org.cloudburstmc.protocol.common.util;

/* loaded from: classes5.dex */
public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, char p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Character.valueOf(p1)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, int p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Integer.valueOf(p1)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, long p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Long.valueOf(p1)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, char p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Character.valueOf(p1), Character.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, char p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Character.valueOf(p1), Integer.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, char p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Character.valueOf(p1), Long.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, char p1, Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Character.valueOf(p1), p2));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, int p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Integer.valueOf(p1), Character.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, int p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Integer.valueOf(p1), Integer.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, int p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Integer.valueOf(p1), Long.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, int p1, Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Integer.valueOf(p1), p2));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Long.valueOf(p1), Character.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Long.valueOf(p1), Integer.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Long.valueOf(p1), Long.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, long p1, Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, Long.valueOf(p1), p2));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, Character.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, Integer.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, Long.valueOf(p2)));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3));
        }
    }

    public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, char p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Character.valueOf(p1)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, int p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Integer.valueOf(p1)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, long p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Long.valueOf(p1)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, char p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Character.valueOf(p1), Character.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, char p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Character.valueOf(p1), Integer.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, char p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Character.valueOf(p1), Long.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, char p1, Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Character.valueOf(p1), p2));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, int p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Integer.valueOf(p1), Character.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, int p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Integer.valueOf(p1), Integer.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, int p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Integer.valueOf(p1), Long.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, int p1, Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Integer.valueOf(p1), p2));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, long p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Long.valueOf(p1), Character.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, long p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Long.valueOf(p1), Integer.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Long.valueOf(p1), Long.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, long p1, Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, Long.valueOf(p1), p2));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, Character.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, Integer.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, Long.valueOf(p2)));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2, p3));
        }
    }

    public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Character.valueOf(p1)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Integer.valueOf(p1)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Long.valueOf(p1)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Character.valueOf(p1), Character.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Character.valueOf(p1), Integer.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Character.valueOf(p1), Long.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Character.valueOf(p1), p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Integer.valueOf(p1), Character.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Integer.valueOf(p1), Integer.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Integer.valueOf(p1), Long.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Integer.valueOf(p1), p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Long.valueOf(p1), Character.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Long.valueOf(p1), Integer.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Long.valueOf(p1), Long.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, Long.valueOf(p1), p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, Character.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, Integer.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, Long.valueOf(p2)));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2, Object p3) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
        return obj;
    }

    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }

    public static int checkElementIndex(int index, int size, String desc) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
        return index;
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, Integer.valueOf(index));
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return format("%s (%s) must be less than size (%s)", desc, Integer.valueOf(index), Integer.valueOf(size));
    }

    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "index");
    }

    public static int checkPositionIndex(int index, int size, String desc) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, Integer.valueOf(index));
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return format("%s (%s) must not be greater than size (%s)", desc, Integer.valueOf(index), Integer.valueOf(size));
    }

    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }
        return format("end index (%s) must not be less than start index (%s)", Integer.valueOf(end), Integer.valueOf(start));
    }

    static String format(String template, Object... args) {
        int placeholderStart;
        String template2 = String.valueOf(template);
        Object[] args2 = args == null ? new Object[]{"(Object[])null"} : args;
        StringBuilder builder = new StringBuilder(template2.length() + (args2.length * 16));
        int templateStart = 0;
        int i = 0;
        while (i < args2.length && (placeholderStart = template2.indexOf("%s", templateStart)) != -1) {
            builder.append((CharSequence) template2, templateStart, placeholderStart);
            builder.append(args2[i]);
            templateStart = placeholderStart + 2;
            i++;
        }
        builder.append((CharSequence) template2, templateStart, template2.length());
        if (i < args2.length) {
            builder.append(" [");
            int i2 = i + 1;
            builder.append(args2[i]);
            while (true) {
                int i3 = i2;
                int i4 = args2.length;
                if (i3 >= i4) {
                    break;
                }
                builder.append(", ");
                i2 = i3 + 1;
                builder.append(args2[i3]);
            }
            builder.append(']');
        }
        return builder.toString();
    }
}
