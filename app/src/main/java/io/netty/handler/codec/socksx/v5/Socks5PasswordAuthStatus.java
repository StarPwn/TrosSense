package io.netty.handler.codec.socksx.v5;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class Socks5PasswordAuthStatus implements Comparable<Socks5PasswordAuthStatus> {
    private final byte byteValue;
    private final String name;
    private String text;
    public static final Socks5PasswordAuthStatus SUCCESS = new Socks5PasswordAuthStatus(0, "SUCCESS");
    public static final Socks5PasswordAuthStatus FAILURE = new Socks5PasswordAuthStatus(255, "FAILURE");

    public static Socks5PasswordAuthStatus valueOf(byte b) {
        switch (b) {
            case -1:
                return FAILURE;
            case 0:
                return SUCCESS;
            default:
                return new Socks5PasswordAuthStatus(b);
        }
    }

    public Socks5PasswordAuthStatus(int byteValue) {
        this(byteValue, "UNKNOWN");
    }

    public Socks5PasswordAuthStatus(int byteValue, String name) {
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
        this.byteValue = (byte) byteValue;
    }

    public byte byteValue() {
        return this.byteValue;
    }

    public boolean isSuccess() {
        return this.byteValue == 0;
    }

    public int hashCode() {
        return this.byteValue;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Socks5PasswordAuthStatus) && this.byteValue == ((Socks5PasswordAuthStatus) obj).byteValue;
    }

    @Override // java.lang.Comparable
    public int compareTo(Socks5PasswordAuthStatus o) {
        return this.byteValue - o.byteValue;
    }

    public String toString() {
        String text = this.text;
        if (text == null) {
            String text2 = this.name + '(' + (this.byteValue & 255) + ')';
            this.text = text2;
            return text2;
        }
        return text;
    }
}
