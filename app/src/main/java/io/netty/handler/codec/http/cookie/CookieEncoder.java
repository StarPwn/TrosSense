package io.netty.handler.codec.http.cookie;

/* loaded from: classes4.dex */
public abstract class CookieEncoder {
    protected final boolean strict;

    /* JADX INFO: Access modifiers changed from: protected */
    public CookieEncoder(boolean strict) {
        this.strict = strict;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateCookie(String name, String value) {
        if (this.strict) {
            int pos = CookieUtil.firstInvalidCookieNameOctet(name);
            if (pos >= 0) {
                throw new IllegalArgumentException("Cookie name contains an invalid char: " + name.charAt(pos));
            }
            CharSequence unwrappedValue = CookieUtil.unwrapValue(value);
            if (unwrappedValue == null) {
                throw new IllegalArgumentException("Cookie value wrapping quotes are not balanced: " + value);
            }
            int pos2 = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue);
            if (pos2 >= 0) {
                throw new IllegalArgumentException("Cookie value contains an invalid char: " + unwrappedValue.charAt(pos2));
            }
        }
    }
}
