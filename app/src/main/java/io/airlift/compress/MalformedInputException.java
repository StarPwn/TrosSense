package io.airlift.compress;

/* loaded from: classes.dex */
public class MalformedInputException extends RuntimeException {
    private final long offset;

    public MalformedInputException(long offset) {
        this(offset, "Malformed input");
    }

    public MalformedInputException(long offset, String reason) {
        super(reason + ": offset=" + offset);
        this.offset = offset;
    }

    public long getOffset() {
        return this.offset;
    }
}
