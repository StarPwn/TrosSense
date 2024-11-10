package io.airlift.compress.snappy;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
final class SnappyInternalUtils {
    private SnappyInternalUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(String.format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    static String badPositionIndexes(int start, int end, int size) {
        if (start < 0 || start > size) {
            return badPositionIndex(start, size, "start index");
        }
        if (end < 0 || end > size) {
            return badPositionIndex(end, size, "end index");
        }
        return String.format("end index (%s) must not be less than start index (%s)", Integer.valueOf(end), Integer.valueOf(start));
    }

    static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return String.format("%s (%s) must not be negative", desc, Integer.valueOf(index));
        }
        if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        }
        return String.format("%s (%s) must not be greater than size (%s)", desc, Integer.valueOf(index), Integer.valueOf(size));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int readBytes(InputStream source, byte[] dest, int offset, int length) throws IOException {
        checkNotNull(source, "source is null", new Object[0]);
        checkNotNull(dest, "dest is null", new Object[0]);
        int lastRead = source.read(dest, offset, length);
        int totalRead = lastRead;
        if (lastRead < length) {
            while (totalRead < length && lastRead != -1) {
                lastRead = source.read(dest, offset + totalRead, length - totalRead);
                if (lastRead != -1) {
                    totalRead += lastRead;
                }
            }
        }
        return totalRead;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int skip(InputStream source, int skip) throws IOException {
        if (skip <= 0) {
            return 0;
        }
        int toSkip = skip - ((int) source.skip(skip));
        boolean more = true;
        while (toSkip > 0 && more) {
            int read = source.read();
            if (read == -1) {
                more = false;
            } else {
                int toSkip2 = toSkip - 1;
                toSkip = (int) (toSkip2 - source.skip(toSkip2));
            }
        }
        int skipped = skip - toSkip;
        return skipped;
    }
}
