package io.netty.handler.codec.smtp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
final class SmtpUtils {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<CharSequence> toUnmodifiableList(CharSequence... sequences) {
        if (sequences == null || sequences.length == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(sequences));
    }

    private SmtpUtils() {
    }
}
