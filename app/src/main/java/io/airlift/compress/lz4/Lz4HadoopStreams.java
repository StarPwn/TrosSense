package io.airlift.compress.lz4;

import io.airlift.compress.hadoop.HadoopInputStream;
import io.airlift.compress.hadoop.HadoopOutputStream;
import io.airlift.compress.hadoop.HadoopStreams;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class Lz4HadoopStreams implements HadoopStreams {
    private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 262144;
    private final int bufferSize;

    public Lz4HadoopStreams() {
        this(262144);
    }

    public Lz4HadoopStreams(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public String getDefaultFileExtension() {
        return ".lz4";
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public List<String> getHadoopCodecName() {
        return Collections.singletonList("org.apache.hadoop.io.compress.Lz4Codec");
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public HadoopInputStream createInputStream(InputStream in) {
        return new Lz4HadoopInputStream(in, this.bufferSize);
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public HadoopOutputStream createOutputStream(OutputStream out) {
        return new Lz4HadoopOutputStream(out, this.bufferSize);
    }
}
