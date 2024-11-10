package io.netty.handler.codec;

import io.netty.util.AsciiString;
import io.netty.util.internal.PlatformDependent;
import java.text.ParseException;
import java.util.Date;

/* loaded from: classes4.dex */
public class CharSequenceValueConverter implements ValueConverter<CharSequence> {
    public static final CharSequenceValueConverter INSTANCE = new CharSequenceValueConverter();
    private static final AsciiString TRUE_ASCII = new AsciiString("true");

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertObject(Object value) {
        if (value instanceof CharSequence) {
            return (CharSequence) value;
        }
        return value.toString();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertInt(int value) {
        return String.valueOf(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertLong(long value) {
        return String.valueOf(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertDouble(double value) {
        return String.valueOf(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertChar(char value) {
        return String.valueOf(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertBoolean(boolean value) {
        return String.valueOf(value);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertFloat(float value) {
        return String.valueOf(value);
    }

    @Override // io.netty.handler.codec.ValueConverter
    public boolean convertToBoolean(CharSequence value) {
        return AsciiString.contentEqualsIgnoreCase(value, TRUE_ASCII);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertByte(byte value) {
        return String.valueOf((int) value);
    }

    @Override // io.netty.handler.codec.ValueConverter
    public byte convertToByte(CharSequence value) {
        if ((value instanceof AsciiString) && value.length() == 1) {
            return ((AsciiString) value).byteAt(0);
        }
        return Byte.parseByte(value.toString());
    }

    @Override // io.netty.handler.codec.ValueConverter
    public char convertToChar(CharSequence value) {
        return value.charAt(0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertShort(short value) {
        return String.valueOf((int) value);
    }

    @Override // io.netty.handler.codec.ValueConverter
    public short convertToShort(CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString) value).parseShort();
        }
        return Short.parseShort(value.toString());
    }

    @Override // io.netty.handler.codec.ValueConverter
    public int convertToInt(CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString) value).parseInt();
        }
        return Integer.parseInt(value.toString());
    }

    @Override // io.netty.handler.codec.ValueConverter
    public long convertToLong(CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString) value).parseLong();
        }
        return Long.parseLong(value.toString());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.handler.codec.ValueConverter
    public CharSequence convertTimeMillis(long value) {
        return DateFormatter.format(new Date(value));
    }

    @Override // io.netty.handler.codec.ValueConverter
    public long convertToTimeMillis(CharSequence value) {
        Date date = DateFormatter.parseHttpDate(value);
        if (date == null) {
            PlatformDependent.throwException(new ParseException("header can't be parsed into a Date: " + ((Object) value), 0));
            return 0L;
        }
        return date.getTime();
    }

    @Override // io.netty.handler.codec.ValueConverter
    public float convertToFloat(CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString) value).parseFloat();
        }
        return Float.parseFloat(value.toString());
    }

    @Override // io.netty.handler.codec.ValueConverter
    public double convertToDouble(CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString) value).parseDouble();
        }
        return Double.parseDouble(value.toString());
    }
}
