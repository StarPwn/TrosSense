package io.airlift.compress.lzo;

import io.airlift.compress.hadoop.CodecAdapter;
import io.airlift.compress.hadoop.HadoopStreams;
import java.util.Optional;
import java.util.function.Function;
import org.apache.hadoop.conf.Configuration;

/* loaded from: classes.dex */
public class LzoCodec extends CodecAdapter {
    public LzoCodec() {
        super(new Function() { // from class: io.airlift.compress.lzo.LzoCodec$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return LzoCodec.lambda$new$0((Optional) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HadoopStreams lambda$new$0(Optional configuration) {
        return new LzoHadoopStreams(getBufferSize(configuration));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getBufferSize(Optional<Configuration> configuration) {
        return ((Integer) configuration.map(new Function() { // from class: io.airlift.compress.lzo.LzoCodec$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Integer valueOf;
                valueOf = Integer.valueOf(((Configuration) obj).getInt("io.compression.codec.lzo.buffersize", 262144));
                return valueOf;
            }
        }).orElse(262144)).intValue();
    }
}
