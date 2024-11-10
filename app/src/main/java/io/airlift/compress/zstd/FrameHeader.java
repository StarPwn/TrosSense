package io.airlift.compress.zstd;

import java.util.Objects;
import java.util.StringJoiner;

/* loaded from: classes.dex */
class FrameHeader {
    final long contentSize;
    final long dictionaryId;
    final boolean hasChecksum;
    final long headerSize;
    final int windowSize;

    public FrameHeader(long headerSize, int windowSize, long contentSize, long dictionaryId, boolean hasChecksum) {
        Util.checkState(windowSize >= 0 || contentSize >= 0, "Invalid frame header: contentSize or windowSize must be set");
        this.headerSize = headerSize;
        this.windowSize = windowSize;
        this.contentSize = contentSize;
        this.dictionaryId = dictionaryId;
        this.hasChecksum = hasChecksum;
    }

    public int computeRequiredOutputBufferLookBackSize() {
        if (this.contentSize < 0) {
            return this.windowSize;
        }
        if (this.windowSize < 0) {
            return Math.toIntExact(this.contentSize);
        }
        return Math.toIntExact(Math.min(this.windowSize, this.contentSize));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FrameHeader that = (FrameHeader) o;
        if (this.headerSize == that.headerSize && this.windowSize == that.windowSize && this.contentSize == that.contentSize && this.dictionaryId == that.dictionaryId && this.hasChecksum == that.hasChecksum) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.headerSize), Integer.valueOf(this.windowSize), Long.valueOf(this.contentSize), Long.valueOf(this.dictionaryId), Boolean.valueOf(this.hasChecksum));
    }

    public String toString() {
        return new StringJoiner(", ", FrameHeader.class.getSimpleName() + "[", "]").add("headerSize=" + this.headerSize).add("windowSize=" + this.windowSize).add("contentSize=" + this.contentSize).add("dictionaryId=" + this.dictionaryId).add("hasChecksum=" + this.hasChecksum).toString();
    }
}
