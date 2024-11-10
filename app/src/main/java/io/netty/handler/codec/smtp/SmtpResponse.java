package io.netty.handler.codec.smtp;

import java.util.List;

/* loaded from: classes4.dex */
public interface SmtpResponse {
    int code();

    List<CharSequence> details();
}
