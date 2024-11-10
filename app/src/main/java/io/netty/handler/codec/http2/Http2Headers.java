package io.netty.handler.codec.http2;

import io.netty.handler.codec.Headers;
import io.netty.util.AsciiString;
import java.util.Iterator;
import java.util.Map;
import okhttp3.internal.http2.Header;

/* loaded from: classes4.dex */
public interface Http2Headers extends Headers<CharSequence, CharSequence, Http2Headers> {
    Http2Headers authority(CharSequence charSequence);

    CharSequence authority();

    boolean contains(CharSequence charSequence, CharSequence charSequence2, boolean z);

    @Override // io.netty.handler.codec.Headers, java.lang.Iterable
    Iterator<Map.Entry<CharSequence, CharSequence>> iterator();

    Http2Headers method(CharSequence charSequence);

    CharSequence method();

    Http2Headers path(CharSequence charSequence);

    CharSequence path();

    Http2Headers scheme(CharSequence charSequence);

    CharSequence scheme();

    Http2Headers status(CharSequence charSequence);

    CharSequence status();

    Iterator<CharSequence> valueIterator(CharSequence charSequence);

    /* loaded from: classes4.dex */
    public enum PseudoHeaderName {
        METHOD(Header.TARGET_METHOD_UTF8, true),
        SCHEME(Header.TARGET_SCHEME_UTF8, true),
        AUTHORITY(Header.TARGET_AUTHORITY_UTF8, true),
        PATH(Header.TARGET_PATH_UTF8, true),
        STATUS(Header.RESPONSE_STATUS_UTF8, false),
        PROTOCOL(":protocol", true);

        private static final char PSEUDO_HEADER_PREFIX = ':';
        private static final byte PSEUDO_HEADER_PREFIX_BYTE = 58;
        private final boolean requestOnly;
        private final AsciiString value;

        PseudoHeaderName(String value, boolean requestOnly) {
            this.value = AsciiString.cached(value);
            this.requestOnly = requestOnly;
        }

        public AsciiString value() {
            return this.value;
        }

        public static boolean hasPseudoHeaderFormat(CharSequence headerName) {
            if (!(headerName instanceof AsciiString)) {
                return headerName.length() > 0 && headerName.charAt(0) == ':';
            }
            AsciiString asciiHeaderName = (AsciiString) headerName;
            return asciiHeaderName.length() > 0 && asciiHeaderName.byteAt(0) == 58;
        }

        public static boolean isPseudoHeader(CharSequence header) {
            return getPseudoHeader(header) != null;
        }

        public static boolean isPseudoHeader(AsciiString header) {
            return getPseudoHeader(header) != null;
        }

        public static boolean isPseudoHeader(String header) {
            return getPseudoHeader(header) != null;
        }

        public static PseudoHeaderName getPseudoHeader(CharSequence header) {
            if (header instanceof AsciiString) {
                return getPseudoHeader((AsciiString) header);
            }
            return getPseudoHeaderName(header);
        }

        private static PseudoHeaderName getPseudoHeaderName(CharSequence header) {
            int length = header.length();
            if (length > 0 && header.charAt(0) == ':') {
                switch (length) {
                    case 5:
                        if (Header.TARGET_PATH_UTF8.contentEquals(header)) {
                            return PATH;
                        }
                        return null;
                    case 7:
                        if (Header.TARGET_METHOD_UTF8 == header) {
                            return METHOD;
                        }
                        if (Header.TARGET_SCHEME_UTF8 == header) {
                            return SCHEME;
                        }
                        if (Header.RESPONSE_STATUS_UTF8 == header) {
                            return STATUS;
                        }
                        if (Header.TARGET_METHOD_UTF8.contentEquals(header)) {
                            return METHOD;
                        }
                        if (Header.TARGET_SCHEME_UTF8.contentEquals(header)) {
                            return SCHEME;
                        }
                        if (Header.RESPONSE_STATUS_UTF8.contentEquals(header)) {
                            return STATUS;
                        }
                        return null;
                    case 9:
                        if (":protocol".contentEquals(header)) {
                            return PROTOCOL;
                        }
                        return null;
                    case 10:
                        if (Header.TARGET_AUTHORITY_UTF8.contentEquals(header)) {
                            return AUTHORITY;
                        }
                        return null;
                }
            }
            return null;
        }

        public static PseudoHeaderName getPseudoHeader(AsciiString header) {
            int length = header.length();
            if (length > 0 && header.charAt(0) == ':') {
                switch (length) {
                    case 5:
                        if (PATH.value().equals(header)) {
                            return PATH;
                        }
                        return null;
                    case 7:
                        if (header == METHOD.value()) {
                            return METHOD;
                        }
                        if (header == SCHEME.value()) {
                            return SCHEME;
                        }
                        if (header == STATUS.value()) {
                            return STATUS;
                        }
                        if (METHOD.value().equals(header)) {
                            return METHOD;
                        }
                        if (SCHEME.value().equals(header)) {
                            return SCHEME;
                        }
                        if (STATUS.value().equals(header)) {
                            return STATUS;
                        }
                        return null;
                    case 9:
                        if (PROTOCOL.value().equals(header)) {
                            return PROTOCOL;
                        }
                        return null;
                    case 10:
                        if (AUTHORITY.value().equals(header)) {
                            return AUTHORITY;
                        }
                        return null;
                }
            }
            return null;
        }

        public boolean isRequestOnly() {
            return this.requestOnly;
        }
    }
}
