package io.airlift.compress.deflate;

import io.airlift.compress.hadoop.HadoopInputStream;
import io.airlift.compress.hadoop.HadoopOutputStream;
import io.airlift.compress.hadoop.HadoopStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class JdkDeflateHadoopStreams implements HadoopStreams {
    private static final int BUFFER_SIZE = 8192;

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public String getDefaultFileExtension() {
        return ".deflate";
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public List<String> getHadoopCodecName() {
        return Collections.singletonList("org.apache.hadoop.io.compress.DefaultCodec");
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public HadoopInputStream createInputStream(InputStream in) throws IOException {
        return new JdkDeflateHadoopInputStream(in, 8192);
    }

    @Override // io.airlift.compress.hadoop.HadoopStreams
    public HadoopOutputStream createOutputStream(OutputStream out) throws IOException {
        return new JdkDeflateHadoopOutputStream(out, 8192);
    }
}
