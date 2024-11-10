package io.airlift.compress.zstd;

import java.util.Arrays;

/* loaded from: classes.dex */
class NodeTable {
    int[] count;
    byte[] numberOfBits;
    short[] parents;
    int[] symbols;

    public NodeTable(int size) {
        this.count = new int[size];
        this.parents = new short[size];
        this.symbols = new int[size];
        this.numberOfBits = new byte[size];
    }

    public void reset() {
        Arrays.fill(this.count, 0);
        Arrays.fill(this.parents, (short) 0);
        Arrays.fill(this.symbols, 0);
        Arrays.fill(this.numberOfBits, (byte) 0);
    }

    public void copyNode(int from, int to) {
        this.count[to] = this.count[from];
        this.parents[to] = this.parents[from];
        this.symbols[to] = this.symbols[from];
        this.numberOfBits[to] = this.numberOfBits[from];
    }
}
