package io.airlift.compress.zstd;

/* loaded from: classes.dex */
class RepeatedOffsets {
    private int offset0 = 1;
    private int offset1 = 4;
    private int tempOffset0;
    private int tempOffset1;

    public int getOffset0() {
        return this.offset0;
    }

    public int getOffset1() {
        return this.offset1;
    }

    public void saveOffset0(int offset) {
        this.tempOffset0 = offset;
    }

    public void saveOffset1(int offset) {
        this.tempOffset1 = offset;
    }

    public void commit() {
        this.offset0 = this.tempOffset0;
        this.offset1 = this.tempOffset1;
    }
}
