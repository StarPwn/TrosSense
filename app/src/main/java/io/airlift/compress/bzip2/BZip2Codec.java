package io.airlift.compress.bzip2;

import io.airlift.compress.hadoop.CodecAdapter;
import io.airlift.compress.hadoop.HadoopStreams;
import java.util.Optional;
import java.util.function.Function;

/* loaded from: classes.dex */
public class BZip2Codec extends CodecAdapter {
    public BZip2Codec() {
        super(new Function() { // from class: io.airlift.compress.bzip2.BZip2Codec$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BZip2Codec.lambda$new$0((Optional) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ HadoopStreams lambda$new$0(Optional configuration) {
        return new BZip2HadoopStreams();
    }
}
