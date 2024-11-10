package io.netty.resolver.dns;

import io.netty.util.internal.MathUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.Random;

/* loaded from: classes4.dex */
final class DnsQueryIdSpace {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BUCKETS = 4;
    private static final int BUCKET_DROP_THRESHOLD = 500;
    private static final int BUCKET_SIZE = 16384;
    private static final int MAX_ID = 65535;
    private final DnsQueryIdRange[] idBuckets = new DnsQueryIdRange[4];

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsQueryIdSpace() {
        if (this.idBuckets.length != MathUtil.findNextPositivePowerOfTwo(this.idBuckets.length)) {
            throw new AssertionError();
        }
        this.idBuckets[0] = newBucket(0);
    }

    private static DnsQueryIdRange newBucket(int idBucketsIdx) {
        return new DnsQueryIdRange(16384, idBucketsIdx * 16384);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int nextId() {
        int freeIdx = -1;
        for (int bucketIdx = 0; bucketIdx < this.idBuckets.length; bucketIdx++) {
            DnsQueryIdRange bucket = this.idBuckets[bucketIdx];
            if (bucket != null) {
                int id = bucket.nextId();
                if (id != -1) {
                    return id;
                }
            } else if (freeIdx == -1 || PlatformDependent.threadLocalRandom().nextBoolean()) {
                freeIdx = bucketIdx;
            }
        }
        if (freeIdx == -1) {
            return -1;
        }
        DnsQueryIdRange bucket2 = newBucket(freeIdx);
        this.idBuckets[freeIdx] = bucket2;
        int id2 = bucket2.nextId();
        if (id2 < 0) {
            throw new AssertionError();
        }
        return id2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pushId(int id) {
        DnsQueryIdRange otherBucket;
        int bucketIdx = id / 16384;
        if (bucketIdx >= this.idBuckets.length) {
            throw new IllegalArgumentException("id too large: " + id);
        }
        DnsQueryIdRange bucket = this.idBuckets[bucketIdx];
        if (bucket == null) {
            throw new AssertionError();
        }
        bucket.pushId(id);
        if (bucket.usableIds() == bucket.maxUsableIds()) {
            for (int idx = 0; idx < this.idBuckets.length; idx++) {
                if (idx != bucketIdx && (otherBucket = this.idBuckets[idx]) != null && otherBucket.usableIds() > BUCKET_DROP_THRESHOLD) {
                    this.idBuckets[bucketIdx] = null;
                    return;
                }
            }
        }
    }

    int usableIds() {
        int usableIds = 0;
        DnsQueryIdRange[] dnsQueryIdRangeArr = this.idBuckets;
        int length = dnsQueryIdRangeArr.length;
        for (int i = 0; i < length; i++) {
            DnsQueryIdRange bucket = dnsQueryIdRangeArr[i];
            usableIds += bucket == null ? 16384 : bucket.usableIds();
        }
        return usableIds;
    }

    int maxUsableIds() {
        return this.idBuckets.length * 16384;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DnsQueryIdRange {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private int count;
        private final short[] ids;
        private final int startId;

        DnsQueryIdRange(int bucketSize, int startId) {
            this.ids = new short[bucketSize];
            this.startId = startId;
            for (int v = startId; v < bucketSize + startId; v++) {
                pushId(v);
            }
        }

        int nextId() {
            if (this.count < 0) {
                throw new AssertionError();
            }
            if (this.count == 0) {
                return -1;
            }
            short id = this.ids[this.count - 1];
            this.count--;
            return 65535 & id;
        }

        void pushId(int id) {
            if (this.count == this.ids.length) {
                throw new IllegalStateException("overflow");
            }
            if (id > this.startId + this.ids.length || id < this.startId) {
                throw new AssertionError();
            }
            Random random = PlatformDependent.threadLocalRandom();
            int insertionPosition = random.nextInt(this.count + 1);
            this.ids[this.count] = this.ids[insertionPosition];
            this.ids[insertionPosition] = (short) id;
            this.count++;
        }

        int usableIds() {
            return this.count;
        }

        int maxUsableIds() {
            return this.ids.length;
        }
    }
}
