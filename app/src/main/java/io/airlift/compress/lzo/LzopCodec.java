package io.airlift.compress.lzo;

import io.airlift.compress.hadoop.CodecAdapter;
import io.airlift.compress.hadoop.HadoopStreams;
import java.util.Optional;
import java.util.function.Function;

/* loaded from: classes.dex */
public class LzopCodec extends CodecAdapter {
    public LzopCodec() {
        super(new Function() { // from class: io.airlift.compress.lzo.LzopCodec$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return LzopCodec.lambda$new$0((Optional) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HadoopStreams lambda$new$0(Optional configuration) {
        return new LzopHadoopStreams(LzoCodec.getBufferSize(configuration));
    }
}
