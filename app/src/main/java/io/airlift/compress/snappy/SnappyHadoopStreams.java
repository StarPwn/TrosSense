package io.airlift.compress.snappy;

import io.airlift.compress.hadoop.HadoopInputStream;
import io.airlift.compress.hadoop.HadoopOutputStream;
import io.airlift.compress.hadoop.HadoopStreams;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class SnappyHadoopStreams implements HadoopStreams {
    private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 262144;
    private final int bufferSize;

    public SnappyHadoopStreams() {
        this(262144);
    }

    public SnappyHadoopStreams(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public String getDefaultFileExtension() {
        return ".snappy";
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public List<String> getHadoopCodecName() {
        return Collections.singletonList("org.apache.hadoop.io.compress.SnappyCodec");
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public HadoopInputStream createInputStream(InputStream in) {
        return new SnappyHadoopInputStream(in);
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public HadoopOutputStream createOutputStream(OutputStream out) {
        return new SnappyHadoopOutputStream(out, this.bufferSize);
    }
}
