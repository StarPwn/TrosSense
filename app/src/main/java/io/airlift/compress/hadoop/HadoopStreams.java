package io.airlift.compress.hadoop;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/* loaded from: classes.dex */
public interface HadoopStreams {
    HadoopInputStream createInputStream(InputStream in) throws IOException;

    HadoopOutputStream createOutputStream(OutputStream out) throws IOException;

    String getDefaultFileExtension();

    List<String> getHadoopCodecName();
}
