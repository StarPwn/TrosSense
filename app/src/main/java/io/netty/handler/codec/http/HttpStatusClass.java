package io.netty.handler.codec.http;

import io.netty.util.AsciiString;

/* loaded from: classes4.dex */
public enum HttpStatusClass {
    INFORMATIONAL(100, 200, "Informational"),
    SUCCESS(200, 300, "Success"),
    REDIRECTION(300, 400, "Redirection"),
    CLIENT_ERROR(400, 500, "Client Error"),
    SERVER_ERROR(500, 600, "Server Error"),
    UNKNOWN(0, 0, "Unknown Status") { // from class: io.netty.handler.codec.http.HttpStatusClass.1
        @Override // io.netty.handler.codec.http.HttpStatusClass
        public boolean contains(int code) {
            return code < 100 || code >= 600;
        }
    };

    private static final HttpStatusClass[] statusArray = new HttpStatusClass[6];
    private final AsciiString defaultReasonPhrase;
    private final int max;
    private final int min;

    static {
        statusArray[1] = INFORMATIONAL;
        statusArray[2] = SUCCESS;
        statusArray[3] = REDIRECTION;
        statusArray[4] = CLIENT_ERROR;
        statusArray[5] = SERVER_ERROR;
    }

    public static HttpStatusClass valueOf(int code) {
        if (UNKNOWN.contains(code)) {
            return UNKNOWN;
        }
        return statusArray[fast_div100(code)];
    }

    private static int fast_div100(int dividend) {
        return (int) ((dividend * 1374389535) >> 37);
    }

    public static HttpStatusClass valueOf(CharSequence code) {
        if (code != null && code.length() == 3) {
            char c0 = code.charAt(0);
            return (isDigit(c0) && isDigit(code.charAt(1)) && isDigit(code.charAt(2))) ? valueOf(digit(c0) * 100) : UNKNOWN;
        }
        return UNKNOWN;
    }

    private static int digit(char c) {
        return c - '0';
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    HttpStatusClass(int min, int max, String defaultReasonPhrase) {
        this.min = min;
        this.max = max;
        this.defaultReasonPhrase = AsciiString.cached(defaultReasonPhrase);
    }

    public boolean contains(int code) {
        return code >= this.min && code < this.max;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AsciiString defaultReasonPhrase() {
        return this.defaultReasonPhrase;
    }
}
