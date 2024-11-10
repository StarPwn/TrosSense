package io.netty.handler.codec.dns;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class DnsOpCode implements Comparable<DnsOpCode> {
    private final byte byteValue;
    private final String name;
    private String text;
    public static final DnsOpCode QUERY = new DnsOpCode(0, "QUERY");
    public static final DnsOpCode IQUERY = new DnsOpCode(1, "IQUERY");
    public static final DnsOpCode STATUS = new DnsOpCode(2, "STATUS");
    public static final DnsOpCode NOTIFY = new DnsOpCode(4, "NOTIFY");
    public static final DnsOpCode UPDATE = new DnsOpCode(5, "UPDATE");

    public static DnsOpCode valueOf(int b) {
        switch (b) {
            case 0:
                return QUERY;
            case 1:
                return IQUERY;
            case 2:
                return STATUS;
            case 3:
            default:
                return new DnsOpCode(b);
            case 4:
                return NOTIFY;
            case 5:
                return UPDATE;
        }
    }

    private DnsOpCode(int byteValue) {
        this(byteValue, "UNKNOWN");
    }

    public DnsOpCode(int byteValue, String name) {
        this.byteValue = (byte) byteValue;
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
    }

    public byte byteValue() {
        return this.byteValue;
    }

    public int hashCode() {
        return this.byteValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof DnsOpCode) && this.byteValue == ((DnsOpCode) obj).byteValue;
    }

    @Override // java.lang.Comparable
    public int compareTo(DnsOpCode o) {
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
