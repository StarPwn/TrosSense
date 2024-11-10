package io.netty.handler.ssl;

import io.netty.handler.codec.rtsp.RtspHeaders;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes4.dex */
public final class OpenSslCertificateCompressionConfig implements Iterable<AlgorithmConfig> {
    private final List<AlgorithmConfig> pairList;

    /* loaded from: classes4.dex */
    public enum AlgorithmMode {
        Compress,
        Decompress,
        Both
    }

    private OpenSslCertificateCompressionConfig(AlgorithmConfig... pairs) {
        this.pairList = Collections.unmodifiableList(Arrays.asList(pairs));
    }

    @Override // java.lang.Iterable
    public Iterator<AlgorithmConfig> iterator() {
        return this.pairList.iterator();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /* loaded from: classes4.dex */
    public static final class Builder {
        private final List<AlgorithmConfig> algorithmList;

        private Builder() {
            this.algorithmList = new ArrayList();
        }

        public Builder addAlgorithm(OpenSslCertificateCompressionAlgorithm algorithm, AlgorithmMode mode) {
            this.algorithmList.add(new AlgorithmConfig(algorithm, mode));
            return this;
        }

        public OpenSslCertificateCompressionConfig build() {
            return new OpenSslCertificateCompressionConfig((AlgorithmConfig[]) this.algorithmList.toArray(new AlgorithmConfig[0]));
        }
    }

    /* loaded from: classes4.dex */
    public static final class AlgorithmConfig {
        private final OpenSslCertificateCompressionAlgorithm algorithm;
        private final AlgorithmMode mode;

        private AlgorithmConfig(OpenSslCertificateCompressionAlgorithm algorithm, AlgorithmMode mode) {
            this.algorithm = (OpenSslCertificateCompressionAlgorithm) ObjectUtil.checkNotNull(algorithm, "algorithm");
            this.mode = (AlgorithmMode) ObjectUtil.checkNotNull(mode, RtspHeaders.Values.MODE);
        }

        public AlgorithmMode mode() {
            return this.mode;
        }

        public OpenSslCertificateCompressionAlgorithm algorithm() {
            return this.algorithm;
        }
    }
}
