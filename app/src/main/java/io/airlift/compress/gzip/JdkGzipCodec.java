package io.airlift.compress.gzip;

import io.airlift.compress.hadoop.CodecAdapter;
import io.airlift.compress.hadoop.HadoopStreams;
import java.util.Optional;
import java.util.function.Function;

/* loaded from: classes.dex */
public class JdkGzipCodec extends CodecAdapter {
    public JdkGzipCodec() {
        super(new Function() { // from class: io.airlift.compress.gzip.JdkGzipCodec$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return JdkGzipCodec.lambda$new$0((Optional) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HadoopStreams lambda$new$0(Optional configuration) {
        return new JdkGzipHadoopStreams();
    }
}
