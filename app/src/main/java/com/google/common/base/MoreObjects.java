package com.google.common.base;

import java.util.Arrays;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class MoreObjects {
    public static <T> T firstNonNull(@Nullable T t, @Nullable T t2) {
        return t != null ? t : (T) Preconditions.checkNotNull(t2);
    }

    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }

    public static ToStringHelper toStringHelper(Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }

    public static ToStringHelper toStringHelper(String className) {
        return new ToStringHelper(className);
    }

    /* loaded from: classes.dex */
    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;

        private ToStringHelper(String className) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = (String) Preconditions.checkNotNull(className);
        }

        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        public ToStringHelper add(String name, @Nullable Object value) {
            return addHolder(name, value);
        }

        public ToStringHelper add(String name, boolean value) {
            return addHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, char value) {
            return addHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, double value) {
            return addHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, float value) {
            return addHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, int value) {
            return addHolder(name, String.valueOf(value));
        }

        public ToStringHelper add(String name, long value) {
            return addHolder(name, String.valueOf(value));
        }

        public ToStringHelper addValue(@Nullable Object value) {
            return addHolder(value);
        }

        public ToStringHelper addValue(boolean value) {
            return addHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(char value) {
            return addHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(double value) {
            return addHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(float value) {
            return addHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(int value) {
            return addHolder(String.valueOf(value));
        }

        public ToStringHelper addValue(long value) {
            return addHolder(String.valueOf(value));
        }

        public String toString() {
            boolean omitNullValuesSnapshot = this.omitNullValues;
            String nextSeparator = "";
            StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
            for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                Object value = valueHolder.value;
                if (!omitNullValuesSnapshot || value != null) {
                    builder.append(nextSeparator);
                    nextSeparator = ", ";
                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=');
                    }
                    if (value != null && value.getClass().isArray()) {
                        Object[] objectArray = {value};
                        String arrayString = Arrays.deepToString(objectArray);
                        builder.append((CharSequence) arrayString, 1, arrayString.length() - 1);
                    } else {
                        builder.append(value);
                    }
                }
            }
            return builder.append('}').toString();
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            return valueHolder;
        }

        private ToStringHelper addHolder(@Nullable Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            return this;
        }

        private ToStringHelper addHolder(String name, @Nullable Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            valueHolder.name = (String) Preconditions.checkNotNull(name);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static final class ValueHolder {
            String name;
            ValueHolder next;
            Object value;

            private ValueHolder() {
            }
        }
    }

    private MoreObjects() {
    }
}
