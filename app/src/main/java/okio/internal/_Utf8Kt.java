package okio.internal;

import com.trossense.bl;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okio.Utf8;
import org.msgpack.core.MessagePack;

/* compiled from: -Utf8.kt */
@Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005¨\u0006\u0007"}, d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "beginIndex", "", "endIndex", "okio"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class _Utf8Kt {
    public static /* synthetic */ String commonToUtf8String$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        return commonToUtf8String(bArr, i, i2);
    }

    public static final String commonToUtf8String(byte[] $this$commonToUtf8String, int beginIndex, int endIndex) {
        int length;
        int length2;
        byte b1$iv$iv;
        int length3;
        int i;
        int length4;
        int length5;
        byte b2$iv$iv;
        int length6;
        int length7;
        int length8;
        int length9;
        Intrinsics.checkNotNullParameter($this$commonToUtf8String, "<this>");
        if (beginIndex < 0 || endIndex > $this$commonToUtf8String.length || beginIndex > endIndex) {
            throw new ArrayIndexOutOfBoundsException("size=" + $this$commonToUtf8String.length + " beginIndex=" + beginIndex + " endIndex=" + endIndex);
        }
        char[] chars = new char[endIndex - beginIndex];
        int length10 = 0;
        int $i$f$processUtf16Chars = 0;
        int index$iv = beginIndex;
        while (index$iv < endIndex) {
            byte b0$iv = $this$commonToUtf8String[index$iv];
            if (b0$iv >= 0) {
                char c = (char) b0$iv;
                chars[length10] = c;
                index$iv++;
                length10++;
                while (index$iv < endIndex && $this$commonToUtf8String[index$iv] >= 0) {
                    int index$iv2 = index$iv + 1;
                    char c2 = (char) $this$commonToUtf8String[index$iv];
                    chars[length10] = c2;
                    index$iv = index$iv2;
                    length10++;
                }
            } else {
                int other$iv$iv = b0$iv >> 5;
                if (other$iv$iv == -2) {
                    if (endIndex <= index$iv + 1) {
                        char c3 = (char) Utf8.REPLACEMENT_CODE_POINT;
                        chars[length10] = c3;
                        length10++;
                        b1$iv$iv = 1;
                        length = $i$f$processUtf16Chars;
                    } else {
                        byte b0$iv$iv = $this$commonToUtf8String[index$iv];
                        byte b1$iv$iv2 = $this$commonToUtf8String[index$iv + 1];
                        if ((b1$iv$iv2 & MessagePack.Code.NIL) == 128) {
                            int codePoint$iv$iv = (b1$iv$iv2 ^ 3968) ^ (b0$iv$iv << 6);
                            if (codePoint$iv$iv < 128) {
                                length = $i$f$processUtf16Chars;
                                char c4 = (char) Utf8.REPLACEMENT_CODE_POINT;
                                length2 = length10 + 1;
                                chars[length10] = c4;
                            } else {
                                length = $i$f$processUtf16Chars;
                                char c5 = (char) codePoint$iv$iv;
                                length2 = length10 + 1;
                                chars[length10] = c5;
                            }
                            length10 = length2;
                            b1$iv$iv = 2;
                        } else {
                            char c6 = (char) Utf8.REPLACEMENT_CODE_POINT;
                            chars[length10] = c6;
                            length = $i$f$processUtf16Chars;
                            length10++;
                            b1$iv$iv = 1;
                        }
                    }
                    index$iv += b1$iv$iv;
                    $i$f$processUtf16Chars = length;
                } else {
                    int $i$f$processUtf16Chars2 = $i$f$processUtf16Chars;
                    int other$iv$iv2 = b0$iv >> 4;
                    if (other$iv$iv2 == -2) {
                        if (endIndex <= index$iv + 2) {
                            char c7 = (char) Utf8.REPLACEMENT_CODE_POINT;
                            int length11 = length10 + 1;
                            chars[length10] = c7;
                            if (endIndex > index$iv + 1) {
                                int byte$iv$iv$iv = $this$commonToUtf8String[index$iv + 1];
                                int other$iv$iv$iv$iv = 192 & byte$iv$iv$iv;
                                if (other$iv$iv$iv$iv == 128) {
                                    length10 = length11;
                                    i = 2;
                                }
                            }
                            length10 = length11;
                            i = 1;
                        } else {
                            byte b0$iv$iv2 = $this$commonToUtf8String[index$iv];
                            byte b1$iv$iv3 = $this$commonToUtf8String[index$iv + 1];
                            if ((b1$iv$iv3 & MessagePack.Code.NIL) == 128) {
                                int length12 = index$iv + 2;
                                byte b2$iv$iv2 = $this$commonToUtf8String[length12];
                                if ((b2$iv$iv2 & MessagePack.Code.NIL) == 128) {
                                    int codePoint$iv$iv2 = (((-123008) ^ b2$iv$iv2) ^ (b1$iv$iv3 << 6)) ^ (b0$iv$iv2 << 12);
                                    if (codePoint$iv$iv2 < 2048) {
                                        char c8 = (char) Utf8.REPLACEMENT_CODE_POINT;
                                        length3 = length10 + 1;
                                        chars[length10] = c8;
                                    } else {
                                        if (55296 <= codePoint$iv$iv2 && codePoint$iv$iv2 < 57344) {
                                            char c9 = (char) Utf8.REPLACEMENT_CODE_POINT;
                                            length3 = length10 + 1;
                                            chars[length10] = c9;
                                        } else {
                                            char c10 = (char) codePoint$iv$iv2;
                                            length3 = length10 + 1;
                                            chars[length10] = c10;
                                        }
                                    }
                                    length10 = length3;
                                    i = 3;
                                } else {
                                    char c11 = (char) Utf8.REPLACEMENT_CODE_POINT;
                                    chars[length10] = c11;
                                    length10++;
                                    i = 2;
                                }
                            } else {
                                char c12 = (char) Utf8.REPLACEMENT_CODE_POINT;
                                chars[length10] = c12;
                                length10++;
                                i = 1;
                            }
                        }
                        index$iv += i;
                        $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                    } else {
                        int other$iv$iv3 = b0$iv >> 3;
                        if (other$iv$iv3 == -2) {
                            if (endIndex <= index$iv + 3) {
                                if (65533 != 65533) {
                                    char c13 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                    int length13 = length10 + 1;
                                    chars[length10] = c13;
                                    char c14 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                    length9 = length13 + 1;
                                    chars[length13] = c14;
                                } else {
                                    chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                    length9 = length10 + 1;
                                }
                                if (endIndex > index$iv + 1) {
                                    int byte$iv$iv$iv2 = $this$commonToUtf8String[index$iv + 1];
                                    int other$iv$iv$iv$iv2 = 192 & byte$iv$iv$iv2;
                                    byte byte$iv$iv$iv3 = other$iv$iv$iv$iv2 == 128 ? (byte) 1 : (byte) 0;
                                    if (byte$iv$iv$iv3 != 0) {
                                        if (endIndex > index$iv + 2) {
                                            int byte$iv$iv$iv4 = $this$commonToUtf8String[index$iv + 2];
                                            int other$iv$iv$iv$iv3 = 192 & byte$iv$iv$iv4;
                                            if (other$iv$iv$iv$iv3 == 128) {
                                                length10 = length9;
                                                b2$iv$iv = 3;
                                            }
                                        }
                                        length10 = length9;
                                        b2$iv$iv = 2;
                                    }
                                }
                                length10 = length9;
                                b2$iv$iv = 1;
                            } else {
                                byte b0$iv$iv3 = $this$commonToUtf8String[index$iv];
                                byte b1$iv$iv4 = $this$commonToUtf8String[index$iv + 1];
                                if ((b1$iv$iv4 & MessagePack.Code.NIL) == 128) {
                                    byte b2$iv$iv3 = $this$commonToUtf8String[index$iv + 2];
                                    if ((b2$iv$iv3 & MessagePack.Code.NIL) == 128) {
                                        byte b3$iv$iv = $this$commonToUtf8String[index$iv + 3];
                                        if ((b3$iv$iv & MessagePack.Code.NIL) == 128) {
                                            int codePoint$iv$iv3 = (((3678080 ^ b3$iv$iv) ^ (b2$iv$iv3 << 6)) ^ (b1$iv$iv4 << 12)) ^ (b0$iv$iv3 << 18);
                                            if (codePoint$iv$iv3 <= 1114111) {
                                                if (55296 <= codePoint$iv$iv3 && codePoint$iv$iv3 < 57344) {
                                                    if (65533 != 65533) {
                                                        char c15 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                                        int length14 = length10 + 1;
                                                        chars[length10] = c15;
                                                        char c16 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                                        length5 = length14 + 1;
                                                        chars[length14] = c16;
                                                        b2$iv$iv = 4;
                                                        length10 = length5;
                                                    } else {
                                                        length4 = length10 + 1;
                                                        chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                                        length5 = length4;
                                                        b2$iv$iv = 4;
                                                        length10 = length5;
                                                    }
                                                } else if (codePoint$iv$iv3 < 65536) {
                                                    if (65533 != 65533) {
                                                        char c17 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                                        int length15 = length10 + 1;
                                                        chars[length10] = c17;
                                                        char c18 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                                        length5 = length15 + 1;
                                                        chars[length15] = c18;
                                                        b2$iv$iv = 4;
                                                        length10 = length5;
                                                    } else {
                                                        length4 = length10 + 1;
                                                        chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                                        length5 = length4;
                                                        b2$iv$iv = 4;
                                                        length10 = length5;
                                                    }
                                                } else if (codePoint$iv$iv3 != 65533) {
                                                    char c19 = (char) ((codePoint$iv$iv3 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                                    int length16 = length10 + 1;
                                                    chars[length10] = c19;
                                                    char c20 = (char) ((codePoint$iv$iv3 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                                    length5 = length16 + 1;
                                                    chars[length16] = c20;
                                                    b2$iv$iv = 4;
                                                    length10 = length5;
                                                } else {
                                                    length4 = length10 + 1;
                                                    chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                                    length5 = length4;
                                                    b2$iv$iv = 4;
                                                    length10 = length5;
                                                }
                                            } else if (65533 != 65533) {
                                                char c21 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                                int length17 = length10 + 1;
                                                chars[length10] = c21;
                                                char c22 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                                length5 = length17 + 1;
                                                chars[length17] = c22;
                                                b2$iv$iv = 4;
                                                length10 = length5;
                                            } else {
                                                length4 = length10 + 1;
                                                chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                                length5 = length4;
                                                b2$iv$iv = 4;
                                                length10 = length5;
                                            }
                                        } else {
                                            if (65533 != 65533) {
                                                char c23 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                                int length18 = length10 + 1;
                                                chars[length10] = c23;
                                                char c24 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                                length6 = length18 + 1;
                                                chars[length18] = c24;
                                            } else {
                                                chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                                length6 = length10 + 1;
                                            }
                                            length10 = length6;
                                            b2$iv$iv = 3;
                                        }
                                    } else {
                                        if (65533 != 65533) {
                                            char c25 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            int length19 = length10 + 1;
                                            chars[length10] = c25;
                                            char c26 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            length7 = length19 + 1;
                                            chars[length19] = c26;
                                        } else {
                                            chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                            length7 = length10 + 1;
                                        }
                                        length10 = length7;
                                        b2$iv$iv = 2;
                                    }
                                } else {
                                    if (65533 != 65533) {
                                        char c27 = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                        int length20 = length10 + 1;
                                        chars[length10] = c27;
                                        char c28 = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                        length8 = length20 + 1;
                                        chars[length20] = c28;
                                    } else {
                                        chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                                        length8 = length10 + 1;
                                    }
                                    length10 = length8;
                                    b2$iv$iv = 1;
                                }
                            }
                            index$iv += b2$iv$iv;
                            $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                        } else {
                            chars[length10] = Utf8.REPLACEMENT_CHARACTER;
                            index$iv++;
                            length10++;
                            $i$f$processUtf16Chars = $i$f$processUtf16Chars2;
                        }
                    }
                }
            }
        }
        return StringsKt.concatToString(chars, 0, length10);
    }

    public static final byte[] commonAsUtf8ToByteArray(String $this$commonAsUtf8ToByteArray) {
        Intrinsics.checkNotNullParameter($this$commonAsUtf8ToByteArray, "<this>");
        byte[] bytes = new byte[$this$commonAsUtf8ToByteArray.length() * 4];
        int length = $this$commonAsUtf8ToByteArray.length();
        for (int index = 0; index < length; index++) {
            char b0 = $this$commonAsUtf8ToByteArray.charAt(index);
            if (Intrinsics.compare((int) b0, 128) >= 0) {
                int size = index;
                int endIndex$iv = $this$commonAsUtf8ToByteArray.length();
                int index$iv = index;
                while (index$iv < endIndex$iv) {
                    char c$iv = $this$commonAsUtf8ToByteArray.charAt(index$iv);
                    if (Intrinsics.compare((int) c$iv, 128) < 0) {
                        byte c = (byte) c$iv;
                        bytes[size] = c;
                        index$iv++;
                        size++;
                        while (index$iv < endIndex$iv && Intrinsics.compare((int) $this$commonAsUtf8ToByteArray.charAt(index$iv), 128) < 0) {
                            int index$iv2 = index$iv + 1;
                            byte c2 = (byte) $this$commonAsUtf8ToByteArray.charAt(index$iv);
                            bytes[size] = c2;
                            index$iv = index$iv2;
                            size++;
                        }
                    } else if (Intrinsics.compare((int) c$iv, 2048) < 0) {
                        byte c3 = (byte) ((c$iv >> 6) | bl.cq);
                        int size2 = size + 1;
                        bytes[size] = c3;
                        byte c4 = (byte) ((c$iv & '?') | 128);
                        bytes[size2] = c4;
                        index$iv++;
                        size = size2 + 1;
                    } else if (55296 <= c$iv && c$iv < 57344) {
                        if (Intrinsics.compare((int) c$iv, 56319) <= 0 && endIndex$iv > index$iv + 1) {
                            char charAt = $this$commonAsUtf8ToByteArray.charAt(index$iv + 1);
                            if (56320 <= charAt && charAt < 57344) {
                                int codePoint$iv = ((c$iv << '\n') + $this$commonAsUtf8ToByteArray.charAt(index$iv + 1)) - 56613888;
                                byte c5 = (byte) ((codePoint$iv >> 18) | bl.db);
                                int size3 = size + 1;
                                bytes[size] = c5;
                                byte c6 = (byte) (((codePoint$iv >> 12) & 63) | 128);
                                int size4 = size3 + 1;
                                bytes[size3] = c6;
                                byte c7 = (byte) (((codePoint$iv >> 6) & 63) | 128);
                                int size5 = size4 + 1;
                                bytes[size4] = c7;
                                byte c8 = (byte) ((codePoint$iv & 63) | 128);
                                bytes[size5] = c8;
                                index$iv += 2;
                                size = size5 + 1;
                            }
                        }
                        bytes[size] = Utf8.REPLACEMENT_BYTE;
                        index$iv++;
                        size++;
                    } else {
                        byte c9 = (byte) ((c$iv >> '\f') | bl.cW);
                        int size6 = size + 1;
                        bytes[size] = c9;
                        byte c10 = (byte) (((c$iv >> 6) & 63) | 128);
                        int size7 = size6 + 1;
                        bytes[size6] = c10;
                        byte c11 = (byte) ((c$iv & '?') | 128);
                        bytes[size7] = c11;
                        index$iv++;
                        size = size7 + 1;
                    }
                }
                byte[] copyOf = Arrays.copyOf(bytes, size);
                Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(this, newSize)");
                return copyOf;
            }
            bytes[index] = (byte) b0;
        }
        int index2 = $this$commonAsUtf8ToByteArray.length();
        byte[] copyOf2 = Arrays.copyOf(bytes, index2);
        Intrinsics.checkNotNullExpressionValue(copyOf2, "copyOf(this, newSize)");
        return copyOf2;
    }
}
