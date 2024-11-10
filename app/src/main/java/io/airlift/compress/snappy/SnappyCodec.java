package io.airlift.compress.snappy;

import io.airlift.compress.hadoop.CodecAdapter;
import io.airlift.compress.hadoop.HadoopStreams;
import java.util.Optional;
import java.util.function.Function;
import org.apache.hadoop.conf.Configuration;

/* loaded from: classes.dex */
public class SnappyCodec extends CodecAdapter {
    public SnappyCodec() {
        super(new Function() { // from class: io.airlift.compress.snappy.SnappyCodec$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SnappyCodec.lambda$new$0((Optional) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HadoopStreams lambda$new$0(Optional configuration) {
        return new SnappyHadoopStreams(getBufferSize(configuration));
    }

    private static int getBufferSize(Optional<Configuration> configuration) {
        return ((Integer) configuration.map(new Function() { // from class: io.airlift.compress.snappy.SnappyCodec$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer valueOf;
                valueOf = Integer.valueOf(((Configuration) obj).getInt("io.compression.codec.snappy.buffersize", 262144));
                return valueOf;
            }
        }).orElse(262144)).intValue();
    }
}
