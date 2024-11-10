package org.msgpack.value;

import java.math.BigInteger;

/* loaded from: classes5.dex */
public interface NumberValue extends Value {
    BigInteger toBigInteger();

    byte toByte();

    double toDouble();

    float toFloat();

    int toInt();

    long toLong();

    short toShort();
}
