package okhttp3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.text.StringsKt;
import okhttp3.internal.Util;
import okhttp3.internal.http.DatesKt;

/* compiled from: Headers.kt */
@Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010 \n\u0002\b\u0006\u0018\u0000 '2\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\u0001:\u0002&'B\u0015\b\u0002\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0012\u001a\u00020\u0003H\u0086\u0002J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0012\u001a\u00020\u0003J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0012\u001a\u00020\u0003H\u0007J\b\u0010\u0017\u001a\u00020\tH\u0016J\u001b\u0010\u0018\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00020\u0019H\u0096\u0002J\u000e\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\tJ\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00030\u001cJ\u0006\u0010\u001d\u001a\u00020\u001eJ\r\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0002\b\u001fJ\u0018\u0010 \u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\"0!J\b\u0010#\u001a\u00020\u0003H\u0016J\u000e\u0010$\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\tJ\u0014\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00030\"2\u0006\u0010\u0012\u001a\u00020\u0003R\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u0011\u0010\b\u001a\u00020\t8G¢\u0006\u0006\u001a\u0004\b\b\u0010\n¨\u0006("}, d2 = {"Lokhttp3/Headers;", "", "Lkotlin/Pair;", "", "namesAndValues", "", "([Ljava/lang/String;)V", "[Ljava/lang/String;", "size", "", "()I", "byteCount", "", "equals", "", "other", "", "get", "name", "getDate", "Ljava/util/Date;", "getInstant", "Ljava/time/Instant;", "hashCode", "iterator", "", "index", "names", "", "newBuilder", "Lokhttp3/Headers$Builder;", "-deprecated_size", "toMultimap", "", "", "toString", "value", "values", "Builder", "Companion", "okhttp"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class Headers implements Iterable<Pair<? extends String, ? extends String>>, KMappedMarker {

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final String[] namesAndValues;

    public /* synthetic */ Headers(String[] strArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(strArr);
    }

    @JvmStatic
    public static final Headers of(Map<String, String> map) {
        return INSTANCE.of(map);
    }

    @JvmStatic
    public static final Headers of(String... strArr) {
        return INSTANCE.of(strArr);
    }

    private Headers(String[] namesAndValues) {
        this.namesAndValues = namesAndValues;
    }

    public final String get(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        return INSTANCE.get(this.namesAndValues, name);
    }

    public final Date getDate(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        String str = get(name);
        if (str == null) {
            return null;
        }
        return DatesKt.toHttpDateOrNull(str);
    }

    public final Instant getInstant(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        Date value = getDate(name);
        if (value == null) {
            return null;
        }
        return value.toInstant();
    }

    public final int size() {
        return this.namesAndValues.length / 2;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "size", imports = {}))
    /* renamed from: -deprecated_size, reason: not valid java name */
    public final int m1893deprecated_size() {
        return size();
    }

    public final String name(int index) {
        return this.namesAndValues[index * 2];
    }

    public final String value(int index) {
        return this.namesAndValues[(index * 2) + 1];
    }

    public final Set<String> names() {
        TreeSet result = new TreeSet(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
        int size = size();
        int i = 0;
        while (i < size) {
            int i2 = i;
            i++;
            result.add(name(i2));
        }
        Set<String> unmodifiableSet = Collections.unmodifiableSet(result);
        Intrinsics.checkNotNullExpressionValue(unmodifiableSet, "unmodifiableSet(result)");
        return unmodifiableSet;
    }

    public final List<String> values(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        List result = null;
        int size = size();
        int i = 0;
        while (i < size) {
            int i2 = i;
            i++;
            if (StringsKt.equals(name, name(i2), true)) {
                if (result == null) {
                    List result2 = new ArrayList(2);
                    result = result2;
                }
                result.add(value(i2));
            }
        }
        if (result != null) {
            List<String> unmodifiableList = Collections.unmodifiableList(result);
            Intrinsics.checkNotNullExpressionValue(unmodifiableList, "{\n      Collections.unmodifiableList(result)\n    }");
            return unmodifiableList;
        }
        return CollectionsKt.emptyList();
    }

    public final long byteCount() {
        long result = this.namesAndValues.length * 2;
        int i = 0;
        while (i < this.namesAndValues.length) {
            int i2 = i;
            i++;
            result += this.namesAndValues[i2].length();
        }
        return result;
    }

    @Override // java.lang.Iterable
    public Iterator<Pair<? extends String, ? extends String>> iterator() {
        int size = size();
        Pair[] pairArr = new Pair[size];
        for (int i = 0; i < size; i++) {
            pairArr[i] = TuplesKt.to(name(i), value(i));
        }
        return ArrayIteratorKt.iterator(pairArr);
    }

    public final Builder newBuilder() {
        Builder result = new Builder();
        CollectionsKt.addAll(result.getNamesAndValues$okhttp(), this.namesAndValues);
        return result;
    }

    public boolean equals(Object other) {
        return (other instanceof Headers) && Arrays.equals(this.namesAndValues, ((Headers) other).namesAndValues);
    }

    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }

    public String toString() {
        StringBuilder $this$toString_u24lambda_u2d0 = new StringBuilder();
        int size = size();
        int i = 0;
        while (i < size) {
            int i2 = i;
            i++;
            String name = name(i2);
            String value = value(i2);
            $this$toString_u24lambda_u2d0.append(name);
            $this$toString_u24lambda_u2d0.append(": ");
            $this$toString_u24lambda_u2d0.append(Util.isSensitiveHeader(name) ? "██" : value);
            $this$toString_u24lambda_u2d0.append("\n");
        }
        String sb = $this$toString_u24lambda_u2d0.toString();
        Intrinsics.checkNotNullExpressionValue(sb, "StringBuilder().apply(builderAction).toString()");
        return sb;
    }

    public final Map<String, List<String>> toMultimap() {
        TreeMap result = new TreeMap(StringsKt.getCASE_INSENSITIVE_ORDER(StringCompanionObject.INSTANCE));
        int size = size();
        int i = 0;
        while (i < size) {
            int i2 = i;
            i++;
            String name = name(i2);
            Locale US = Locale.US;
            Intrinsics.checkNotNullExpressionValue(US, "US");
            String name2 = name.toLowerCase(US);
            Intrinsics.checkNotNullExpressionValue(name2, "this as java.lang.String).toLowerCase(locale)");
            List values = (List) result.get(name2);
            if (values == null) {
                values = new ArrayList(2);
                result.put(name2, values);
            }
            values.add(value(i2));
        }
        return result;
    }

    /* compiled from: Headers.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0005J\u0018\u0010\b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0016\u0010\b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\rJ\u0016\u0010\b\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010J\u0015\u0010\u0011\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0005H\u0000¢\u0006\u0002\b\u0012J\u001d\u0010\u0011\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0000¢\u0006\u0002\b\u0012J\u0016\u0010\u0013\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u0006\u0010\u0014\u001a\u00020\u0010J\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u0005H\u0086\u0002J\u000e\u0010\u0016\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0005J\u0019\u0010\u0017\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0087\u0002J\u0019\u0010\u0017\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\rH\u0086\u0002J\u0019\u0010\u0017\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0086\u0002R\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0018"}, d2 = {"Lokhttp3/Headers$Builder;", "", "()V", "namesAndValues", "", "", "getNamesAndValues$okhttp", "()Ljava/util/List;", "add", "line", "name", "value", "Ljava/time/Instant;", "Ljava/util/Date;", "addAll", "headers", "Lokhttp3/Headers;", "addLenient", "addLenient$okhttp", "addUnsafeNonAscii", "build", "get", "removeAll", "set", "okhttp"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes5.dex */
    public static final class Builder {
        private final List<String> namesAndValues = new ArrayList(20);

        public final List<String> getNamesAndValues$okhttp() {
            return this.namesAndValues;
        }

        public final Builder addLenient$okhttp(String line) {
            Intrinsics.checkNotNullParameter(line, "line");
            Builder $this$addLenient_u24lambda_u2d0 = this;
            int index = StringsKt.indexOf$default((CharSequence) line, ':', 1, false, 4, (Object) null);
            if (index != -1) {
                String substring = line.substring(0, index);
                Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
                String substring2 = line.substring(index + 1);
                Intrinsics.checkNotNullExpressionValue(substring2, "this as java.lang.String).substring(startIndex)");
                $this$addLenient_u24lambda_u2d0.addLenient$okhttp(substring, substring2);
            } else if (line.charAt(0) == ':') {
                String substring3 = line.substring(1);
                Intrinsics.checkNotNullExpressionValue(substring3, "this as java.lang.String).substring(startIndex)");
                $this$addLenient_u24lambda_u2d0.addLenient$okhttp("", substring3);
            } else {
                $this$addLenient_u24lambda_u2d0.addLenient$okhttp("", line);
            }
            return this;
        }

        public final Builder add(String line) {
            Intrinsics.checkNotNullParameter(line, "line");
            Builder $this$add_u24lambda_u2d2 = this;
            int index = StringsKt.indexOf$default((CharSequence) line, ':', 0, false, 6, (Object) null);
            if (!(index != -1)) {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unexpected header: ", line).toString());
            }
            String substring = line.substring(0, index);
            Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String…ing(startIndex, endIndex)");
            String obj = StringsKt.trim((CharSequence) substring).toString();
            String substring2 = line.substring(index + 1);
            Intrinsics.checkNotNullExpressionValue(substring2, "this as java.lang.String).substring(startIndex)");
            $this$add_u24lambda_u2d2.add(obj, substring2);
            return this;
        }

        public final Builder add(String name, String value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$add_u24lambda_u2d3 = this;
            Headers.INSTANCE.checkName(name);
            Headers.INSTANCE.checkValue(value, name);
            $this$add_u24lambda_u2d3.addLenient$okhttp(name, value);
            return this;
        }

        public final Builder addUnsafeNonAscii(String name, String value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$addUnsafeNonAscii_u24lambda_u2d4 = this;
            Headers.INSTANCE.checkName(name);
            $this$addUnsafeNonAscii_u24lambda_u2d4.addLenient$okhttp(name, value);
            return this;
        }

        public final Builder addAll(Headers headers) {
            Intrinsics.checkNotNullParameter(headers, "headers");
            Builder $this$addAll_u24lambda_u2d5 = this;
            int size = headers.size();
            int i = 0;
            while (i < size) {
                int i2 = i;
                i++;
                $this$addAll_u24lambda_u2d5.addLenient$okhttp(headers.name(i2), headers.value(i2));
            }
            return this;
        }

        public final Builder add(String name, Date value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$add_u24lambda_u2d6 = this;
            $this$add_u24lambda_u2d6.add(name, DatesKt.toHttpDateString(value));
            return this;
        }

        public final Builder add(String name, Instant value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$add_u24lambda_u2d7 = this;
            $this$add_u24lambda_u2d7.add(name, new Date(value.toEpochMilli()));
            return this;
        }

        public final Builder set(String name, Date value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$set_u24lambda_u2d8 = this;
            $this$set_u24lambda_u2d8.set(name, DatesKt.toHttpDateString(value));
            return this;
        }

        public final Builder set(String name, Instant value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$set_u24lambda_u2d9 = this;
            return $this$set_u24lambda_u2d9.set(name, new Date(value.toEpochMilli()));
        }

        public final Builder addLenient$okhttp(String name, String value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$addLenient_u24lambda_u2d10 = this;
            $this$addLenient_u24lambda_u2d10.getNamesAndValues$okhttp().add(name);
            $this$addLenient_u24lambda_u2d10.getNamesAndValues$okhttp().add(StringsKt.trim((CharSequence) value).toString());
            return this;
        }

        public final Builder removeAll(String name) {
            Intrinsics.checkNotNullParameter(name, "name");
            Builder $this$removeAll_u24lambda_u2d11 = this;
            int i = 0;
            while (i < $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().size()) {
                if (StringsKt.equals(name, $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().get(i), true)) {
                    $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().remove(i);
                    $this$removeAll_u24lambda_u2d11.getNamesAndValues$okhttp().remove(i);
                    i -= 2;
                }
                i += 2;
            }
            return this;
        }

        public final Builder set(String name, String value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            Builder $this$set_u24lambda_u2d12 = this;
            Headers.INSTANCE.checkName(name);
            Headers.INSTANCE.checkValue(value, name);
            $this$set_u24lambda_u2d12.removeAll(name);
            $this$set_u24lambda_u2d12.addLenient$okhttp(name, value);
            return this;
        }

        public final String get(String name) {
            int i;
            Intrinsics.checkNotNullParameter(name, "name");
            int size = this.namesAndValues.size() - 2;
            int progressionLastElement = ProgressionUtilKt.getProgressionLastElement(size, 0, -2);
            if (progressionLastElement > size) {
                return null;
            }
            do {
                i = size;
                size -= 2;
                if (StringsKt.equals(name, this.namesAndValues.get(i), true)) {
                    return this.namesAndValues.get(i + 1);
                }
            } while (i != progressionLastElement);
            return null;
        }

        public final Headers build() {
            Collection $this$toTypedArray$iv = this.namesAndValues;
            Object[] array = $this$toTypedArray$iv.toArray(new String[0]);
            if (array != null) {
                return new Headers((String[]) array, null);
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
    }

    /* compiled from: Headers.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J%\u0010\t\u001a\u0004\u0018\u00010\u00062\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00060\u000b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010\fJ#\u0010\r\u001a\u00020\u000e2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u000b\"\u00020\u0006H\u0007¢\u0006\u0004\b\u000f\u0010\u0010J#\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u000b\"\u00020\u0006H\u0007¢\u0006\u0004\b\u0011\u0010\u0010J!\u0010\u000f\u001a\u00020\u000e2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0013H\u0007¢\u0006\u0002\b\u0011J\u001d\u0010\u0014\u001a\u00020\u000e*\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0013H\u0007¢\u0006\u0002\b\u000f¨\u0006\u0015"}, d2 = {"Lokhttp3/Headers$Companion;", "", "()V", "checkName", "", "name", "", "checkValue", "value", "get", "namesAndValues", "", "([Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "headersOf", "Lokhttp3/Headers;", "of", "([Ljava/lang/String;)Lokhttp3/Headers;", "-deprecated_of", "headers", "", "toHeaders", "okhttp"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes5.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String get(String[] namesAndValues, String name) {
            int i;
            int length = namesAndValues.length - 2;
            int progressionLastElement = ProgressionUtilKt.getProgressionLastElement(length, 0, -2);
            if (progressionLastElement > length) {
                return null;
            }
            do {
                i = length;
                length -= 2;
                if (StringsKt.equals(name, namesAndValues[i], true)) {
                    return namesAndValues[i + 1];
                }
            } while (i != progressionLastElement);
            return null;
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x004b, code lost:            if (r2 >= 0) goto L19;     */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x004d, code lost:            r4 = r3;        r3 = r3 + 2;        r5 = r0[r4];        r6 = r0[r4 + 1];        checkName(r5);        checkValue(r6, r5);     */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x005b, code lost:            if (r4 != r2) goto L28;     */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0063, code lost:            return new okhttp3.Headers(r0, null);     */
        @kotlin.jvm.JvmStatic
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final okhttp3.Headers of(java.lang.String... r9) {
            /*
                r8 = this;
                java.lang.String r0 = "namesAndValues"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
                int r0 = r9.length
                r1 = 2
                int r0 = r0 % r1
                r2 = 1
                r3 = 0
                if (r0 != 0) goto Le
                r0 = r2
                goto Lf
            Le:
                r0 = r3
            Lf:
                if (r0 == 0) goto L64
                java.lang.Object r0 = r9.clone()
                java.lang.String[] r0 = (java.lang.String[]) r0
                int r4 = r0.length
                r5 = r3
            L19:
                if (r5 >= r4) goto L44
                r6 = r5
                int r5 = r5 + 1
                r7 = r0[r6]
                if (r7 == 0) goto L24
                r7 = r2
                goto L25
            L24:
                r7 = r3
            L25:
                if (r7 == 0) goto L36
                r7 = r0[r6]
                java.lang.CharSequence r7 = (java.lang.CharSequence) r7
                java.lang.CharSequence r7 = kotlin.text.StringsKt.trim(r7)
                java.lang.String r7 = r7.toString()
                r0[r6] = r7
                goto L19
            L36:
                r1 = 0
                java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
                java.lang.String r2 = "Headers cannot be null"
                java.lang.String r2 = r2.toString()
                r1.<init>(r2)
                throw r1
            L44:
                int r2 = r0.length
                int r2 = r2 + (-1)
                int r2 = kotlin.internal.ProgressionUtilKt.getProgressionLastElement(r3, r2, r1)
                if (r2 < 0) goto L5d
            L4d:
                r4 = r3
                int r3 = r3 + r1
                r5 = r0[r4]
                int r6 = r4 + 1
                r6 = r0[r6]
                r8.checkName(r5)
                r8.checkValue(r6, r5)
                if (r4 != r2) goto L4d
            L5d:
                okhttp3.Headers r1 = new okhttp3.Headers
                r2 = 0
                r1.<init>(r0, r2)
                return r1
            L64:
                r0 = 0
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                java.lang.String r1 = "Expected alternating header names and values"
                java.lang.String r1 = r1.toString()
                r0.<init>(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.Headers.Companion.of(java.lang.String[]):okhttp3.Headers");
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "function name changed", replaceWith = @ReplaceWith(expression = "headersOf(*namesAndValues)", imports = {}))
        /* renamed from: -deprecated_of, reason: not valid java name */
        public final Headers m1895deprecated_of(String... namesAndValues) {
            Intrinsics.checkNotNullParameter(namesAndValues, "namesAndValues");
            return of((String[]) Arrays.copyOf(namesAndValues, namesAndValues.length));
        }

        @JvmStatic
        public final Headers of(Map<String, String> map) {
            Intrinsics.checkNotNullParameter(map, "<this>");
            String[] namesAndValues = new String[map.size() * 2];
            int i = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                String name = StringsKt.trim((CharSequence) k).toString();
                String value = StringsKt.trim((CharSequence) v).toString();
                checkName(name);
                checkValue(value, name);
                namesAndValues[i] = name;
                namesAndValues[i + 1] = value;
                i += 2;
            }
            return new Headers(namesAndValues, null);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "function moved to extension", replaceWith = @ReplaceWith(expression = "headers.toHeaders()", imports = {}))
        /* renamed from: -deprecated_of, reason: not valid java name */
        public final Headers m1894deprecated_of(Map<String, String> headers) {
            Intrinsics.checkNotNullParameter(headers, "headers");
            return of(headers);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void checkName(String name) {
            if (!(name.length() > 0)) {
                throw new IllegalArgumentException("name is empty".toString());
            }
            int length = name.length();
            int i = 0;
            while (i < length) {
                int i2 = i;
                i++;
                char c = name.charAt(i2);
                if (!('!' <= c && c < 127)) {
                    throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(c), Integer.valueOf(i2), name).toString());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void checkValue(String value, String name) {
            int length = value.length();
            int i = 0;
            while (i < length) {
                int i2 = i;
                i++;
                char c = value.charAt(i2);
                boolean z = true;
                if (c != '\t') {
                    if (!(' ' <= c && c < 127)) {
                        z = false;
                    }
                }
                if (!z) {
                    throw new IllegalArgumentException(Intrinsics.stringPlus(Util.format("Unexpected char %#04x at %d in %s value", Integer.valueOf(c), Integer.valueOf(i2), name), Util.isSensitiveHeader(name) ? "" : Intrinsics.stringPlus(": ", value)).toString());
                }
            }
        }
    }
}
