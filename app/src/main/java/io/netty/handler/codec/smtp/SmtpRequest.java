package io.netty.handler.codec.smtp;

import java.util.List;

/* loaded from: classes4.dex */
public interface SmtpRequest {
    SmtpCommand command();

    List<CharSequence> parameters();
}
