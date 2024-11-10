package org.msgpack.core;

import java.math.BigInteger;

/* loaded from: classes5.dex */
public class MessageIntegerOverflowException extends MessageTypeException {
    private final BigInteger bigInteger;

    public MessageIntegerOverflowException(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public MessageIntegerOverflowException(long j) {
        this(BigInteger.valueOf(j));
    }

    public MessageIntegerOverflowException(String str, BigInteger bigInteger) {
        super(str);
        this.bigInteger = bigInteger;
    }

    public BigInteger getBigInteger() {
        return this.bigInteger;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this.bigInteger.toString();
    }
}
