package io.netty.buffer;

import java.util.Arrays;

/* loaded from: classes4.dex */
final class IntPriorityQueue {
    public static final int NO_VALUE = -1;
    private int[] array = new int[9];
    private int size;

    public void offer(int handle) {
        if (handle == -1) {
            throw new IllegalArgumentException("The NO_VALUE (-1) cannot be added to the queue.");
        }
        this.size++;
        if (this.size == this.array.length) {
            this.array = Arrays.copyOf(this.array, ((this.array.length - 1) * 2) + 1);
        }
        this.array[this.size] = handle;
        lift(this.size);
    }

    public void remove(int value) {
        for (int i = 1; i <= this.size; i++) {
            if (this.array[i] == value) {
                int[] iArr = this.array;
                int[] iArr2 = this.array;
                int i2 = this.size;
                this.size = i2 - 1;
                iArr[i] = iArr2[i2];
                lift(i);
                sink(i);
                return;
            }
        }
    }

    public int peek() {
        if (this.size == 0) {
            return -1;
        }
        return this.array[1];
    }

    public int poll() {
        if (this.size == 0) {
            return -1;
        }
        int val = this.array[1];
        this.array[1] = this.array[this.size];
        this.array[this.size] = 0;
        this.size--;
        sink(1);
        return val;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private void lift(int index) {
        while (index > 1) {
            int parentIndex = index >> 1;
            if (subord(parentIndex, index)) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                return;
            }
        }
    }

    private void sink(int index) {
        while (true) {
            int i = index << 1;
            int child = i;
            if (i <= this.size) {
                if (child < this.size && subord(child, child + 1)) {
                    child++;
                }
                if (subord(index, child)) {
                    swap(index, child);
                    index = child;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private boolean subord(int a, int b) {
        return this.array[a] > this.array[b];
    }

    private void swap(int a, int b) {
        int value = this.array[a];
        this.array[a] = this.array[b];
        this.array[b] = value;
    }
}
