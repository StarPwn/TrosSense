package io.netty.handler.ssl;

/* loaded from: classes4.dex */
public final class SniCompletionEvent extends SslCompletionEvent {
    private final String hostname;

    public SniCompletionEvent(String hostname) {
        this.hostname = hostname;
    }

    public SniCompletionEvent(String hostname, Throwable cause) {
        super(cause);
        this.hostname = hostname;
    }

    public SniCompletionEvent(Throwable cause) {
        this(null, cause);
    }

    public String hostname() {
        return this.hostname;
    }

    @Override // io.netty.handler.ssl.SslCompletionEvent
    public String toString() {
        Throwable cause = cause();
        if (cause == null) {
            return getClass().getSimpleName() + "(SUCCESS='" + this.hostname + "'\")";
        }
        return getClass().getSimpleName() + '(' + cause + ')';
    }
}
