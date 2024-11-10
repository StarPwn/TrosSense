package io.airlift.compress.lz4;

import io.airlift.compress.hadoop.CodecAdapter;
import io.airlift.compress.hadoop.HadoopStreams;
import java.util.Optional;
import java.util.function.Function;
import org.apache.hadoop.conf.Configuration;

/* loaded from: classes.dex */
public class Lz4Codec extends CodecAdapter {
    public Lz4Codec() {
        super(new Function() { // from class: io.airlift.compress.lz4.Lz4Codec$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Lz4Codec.lambda$new$0((Optional) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HadoopStreams lambda$new$0(Optional configuration) {
        return new Lz4HadoopStreams(getBufferSize(configuration));
    }

    private static int getBufferSize(Optional<Configuration> configuration) {
        return ((Integer) configuration.map(new Function() { // from class: io.airlift.compress.lz4.Lz4Codec$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer valueOf;
                valueOf = Integer.valueOf(((Configuration) obj).getInt("io.compression.codec.lz4.buffersize", 262144));
                return valueOf;
            }
        }).orElse(262144)).intValue();
    }
}
