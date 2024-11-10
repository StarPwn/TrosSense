package org.msgpack.value;

import java.time.Instant;

/* loaded from: classes5.dex */
public interface TimestampValue extends ExtensionValue {
    long getEpochSecond();

    int getNano();

    long toEpochMillis();

    Instant toInstant();
}
