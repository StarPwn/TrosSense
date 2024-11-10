package io.netty.channel.kqueue;

/* loaded from: classes4.dex */
final class KQueueStaticallyReferencedJniMethods {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int connectDataIdempotent();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int connectResumeOnReadWrite();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evAdd();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evClear();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evDelete();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evDisable();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evEOF();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evEnable();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evError();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evfiltRead();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evfiltSock();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evfiltUser();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short evfiltWrite();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int fastOpenClient();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int fastOpenServer();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short noteConnReset();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short noteDisconnected();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native short noteReadClosed();

    private KQueueStaticallyReferencedJniMethods() {
    }
}
