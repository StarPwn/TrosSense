package io.netty.buffer.search;

import io.netty.util.internal.PlatformDependent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

/* loaded from: classes4.dex */
public class AhoCorasicSearchProcessorFactory extends AbstractMultiSearchProcessorFactory {
    static final int ALPHABET_SIZE = 256;
    static final int BITS_PER_SYMBOL = 8;
    private final int[] jumpTable;
    private final int[] matchForNeedleId;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class Context {
        int[] jumpTable;
        int[] matchForNeedleId;

        private Context() {
        }
    }

    /* loaded from: classes4.dex */
    public static class Processor implements MultiSearchProcessor {
        private long currentPosition;
        private final int[] jumpTable;
        private final int[] matchForNeedleId;

        Processor(int[] jumpTable, int[] matchForNeedleId) {
            this.jumpTable = jumpTable;
            this.matchForNeedleId = matchForNeedleId;
        }

        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) {
            this.currentPosition = PlatformDependent.getInt(this.jumpTable, this.currentPosition | (value & 255));
            if (this.currentPosition < 0) {
                this.currentPosition = -this.currentPosition;
                return false;
            }
            return true;
        }

        @Override // io.netty.buffer.search.MultiSearchProcessor
        public int getFoundNeedleId() {
            return this.matchForNeedleId[((int) this.currentPosition) >> 8];
        }

        @Override // io.netty.buffer.search.SearchProcessor
        public void reset() {
            this.currentPosition = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AhoCorasicSearchProcessorFactory(byte[]... needles) {
        for (byte[] needle : needles) {
            if (needle.length == 0) {
                throw new IllegalArgumentException("Needle must be non empty");
            }
        }
        Context context = buildTrie(needles);
        this.jumpTable = context.jumpTable;
        this.matchForNeedleId = context.matchForNeedleId;
        linkSuffixes();
        for (int i = 0; i < this.jumpTable.length; i++) {
            if (this.matchForNeedleId[this.jumpTable[i] >> 8] >= 0) {
                this.jumpTable[i] = -this.jumpTable[i];
            }
        }
    }

    private static Context buildTrie(byte[][] needles) {
        ArrayList<Integer> jumpTableBuilder = new ArrayList<>(256);
        for (int i = 0; i < 256; i++) {
            jumpTableBuilder.add(-1);
        }
        ArrayList<Integer> matchForBuilder = new ArrayList<>();
        matchForBuilder.add(-1);
        for (int needleId = 0; needleId < needles.length; needleId++) {
            byte[] needle = needles[needleId];
            int currentPosition = 0;
            for (byte ch0 : needle) {
                int ch = ch0 & 255;
                int next = currentPosition + ch;
                if (jumpTableBuilder.get(next).intValue() == -1) {
                    jumpTableBuilder.set(next, Integer.valueOf(jumpTableBuilder.size()));
                    for (int i2 = 0; i2 < 256; i2++) {
                        jumpTableBuilder.add(-1);
                    }
                    matchForBuilder.add(-1);
                }
                currentPosition = jumpTableBuilder.get(next).intValue();
            }
            matchForBuilder.set(currentPosition >> 8, Integer.valueOf(needleId));
        }
        Context context = new Context();
        context.jumpTable = new int[jumpTableBuilder.size()];
        for (int i3 = 0; i3 < jumpTableBuilder.size(); i3++) {
            context.jumpTable[i3] = jumpTableBuilder.get(i3).intValue();
        }
        int i4 = matchForBuilder.size();
        context.matchForNeedleId = new int[i4];
        for (int i5 = 0; i5 < matchForBuilder.size(); i5++) {
            context.matchForNeedleId[i5] = matchForBuilder.get(i5).intValue();
        }
        return context;
    }

    private void linkSuffixes() {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(0);
        int[] suffixLinks = new int[this.matchForNeedleId.length];
        Arrays.fill(suffixLinks, -1);
        while (!queue.isEmpty()) {
            int v = queue.remove().intValue();
            int vPosition = v >> 8;
            int u = suffixLinks[vPosition] == -1 ? 0 : suffixLinks[vPosition];
            if (this.matchForNeedleId[vPosition] == -1) {
                this.matchForNeedleId[vPosition] = this.matchForNeedleId[u >> 8];
            }
            for (int ch = 0; ch < 256; ch++) {
                int vIndex = v | ch;
                int uIndex = u | ch;
                int jumpV = this.jumpTable[vIndex];
                int jumpU = this.jumpTable[uIndex];
                if (jumpV != -1) {
                    suffixLinks[jumpV >> 8] = (v <= 0 || jumpU == -1) ? 0 : jumpU;
                    queue.add(Integer.valueOf(jumpV));
                } else {
                    this.jumpTable[vIndex] = jumpU != -1 ? jumpU : 0;
                }
            }
        }
    }

    @Override // io.netty.buffer.search.SearchProcessorFactory
    public Processor newSearchProcessor() {
        return new Processor(this.jumpTable, this.matchForNeedleId);
    }
}
