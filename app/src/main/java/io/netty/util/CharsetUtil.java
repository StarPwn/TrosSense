package io.netty.util;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Map;
import org.jose4j.lang.StringUtil;

/* loaded from: classes4.dex */
public final class CharsetUtil {
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_8 = Charset.forName(StringUtil.UTF_8);
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName(StringUtil.US_ASCII);
    private static final Charset[] CHARSETS = {UTF_16, UTF_16BE, UTF_16LE, UTF_8, ISO_8859_1, US_ASCII};

    public static Charset[] values() {
        return CHARSETS;
    }

    @Deprecated
    public static CharsetEncoder getEncoder(Charset charset) {
        return encoder(charset);
    }

    public static CharsetEncoder encoder(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableCharacterAction) {
        ObjectUtil.checkNotNull(charset, "charset");
        CharsetEncoder e = charset.newEncoder();
        e.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
        return e;
    }

    public static CharsetEncoder encoder(Charset charset, CodingErrorAction codingErrorAction) {
        return encoder(charset, codingErrorAction, codingErrorAction);
    }

    public static CharsetEncoder encoder(Charset charset) {
        ObjectUtil.checkNotNull(charset, "charset");
        Map<Charset, CharsetEncoder> map = InternalThreadLocalMap.get().charsetEncoderCache();
        CharsetEncoder e = map.get(charset);
        if (e != null) {
            e.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            return e;
        }
        CharsetEncoder e2 = encoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        map.put(charset, e2);
        return e2;
    }

    @Deprecated
    public static CharsetDecoder getDecoder(Charset charset) {
        return decoder(charset);
    }

    public static CharsetDecoder decoder(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableCharacterAction) {
        ObjectUtil.checkNotNull(charset, "charset");
        CharsetDecoder d = charset.newDecoder();
        d.onMalformedInput(malformedInputAction).onUnmappableCharacter(unmappableCharacterAction);
        return d;
    }

    public static CharsetDecoder decoder(Charset charset, CodingErrorAction codingErrorAction) {
        return decoder(charset, codingErrorAction, codingErrorAction);
    }

    public static CharsetDecoder decoder(Charset charset) {
        ObjectUtil.checkNotNull(charset, "charset");
        Map<Charset, CharsetDecoder> map = InternalThreadLocalMap.get().charsetDecoderCache();
        CharsetDecoder d = map.get(charset);
        if (d != null) {
            d.reset().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            return d;
        }
        CharsetDecoder d2 = decoder(charset, CodingErrorAction.REPLACE, CodingErrorAction.REPLACE);
        map.put(charset, d2);
        return d2;
    }

    private CharsetUtil() {
    }
}
