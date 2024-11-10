package io.netty.channel.kqueue;

import io.netty.channel.DefaultFileRegion;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.unix.Unix;
import io.netty.util.internal.ClassInitializerUtil;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.channels.FileChannel;

/* loaded from: classes4.dex */
final class Native {
    private static final int CONNECT_DATA_IDEMPOTENT;
    private static final int CONNECT_RESUME_ON_READ_WRITE;
    static final int CONNECT_TCP_FASTOPEN;
    static final short EVFILT_READ;
    static final short EVFILT_SOCK;
    static final short EVFILT_USER;
    static final short EVFILT_WRITE;
    static final short EV_ADD;
    static final short EV_ADD_CLEAR_ENABLE;
    static final short EV_CLEAR;
    static final short EV_DELETE;
    static final short EV_DELETE_DISABLE;
    static final short EV_DISABLE;
    static final short EV_ENABLE;
    static final short EV_EOF;
    static final short EV_ERROR;
    static final boolean IS_SUPPORTING_TCP_FASTOPEN_CLIENT;
    static final boolean IS_SUPPORTING_TCP_FASTOPEN_SERVER;
    static final int NOTE_CONNRESET;
    static final int NOTE_DISCONNECTED;
    static final int NOTE_RDHUP;
    static final int NOTE_READCLOSED;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) Native.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int keventAddUserEvent(int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int keventTriggerUserEvent(int i, int i2);

    private static native int keventWait(int i, long j, int i2, long j2, int i3, int i4, int i5);

    private static native int kqueueCreate();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int offsetofKEventFFlags();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int offsetofKEventFilter();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int offsetofKEventFlags();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int offsetofKEventIdent();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int offsetofKeventData();

    private static native int registerUnix();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int sizeofKEvent();

    static /* synthetic */ int access$000() {
        return registerUnix();
    }

    static {
        ClassInitializerUtil.tryLoadClasses(Native.class, PeerCredentials.class, DefaultFileRegion.class, FileChannel.class, FileDescriptor.class);
        try {
            sizeofKEvent();
        } catch (UnsatisfiedLinkError e) {
            loadNativeLibrary();
        }
        Unix.registerInternal(new Runnable() { // from class: io.netty.channel.kqueue.Native.1
            @Override // java.lang.Runnable
            public void run() {
                Native.access$000();
            }
        });
        EV_ADD = KQueueStaticallyReferencedJniMethods.evAdd();
        EV_ENABLE = KQueueStaticallyReferencedJniMethods.evEnable();
        EV_DISABLE = KQueueStaticallyReferencedJniMethods.evDisable();
        EV_DELETE = KQueueStaticallyReferencedJniMethods.evDelete();
        EV_CLEAR = KQueueStaticallyReferencedJniMethods.evClear();
        EV_ERROR = KQueueStaticallyReferencedJniMethods.evError();
        EV_EOF = KQueueStaticallyReferencedJniMethods.evEOF();
        NOTE_READCLOSED = KQueueStaticallyReferencedJniMethods.noteReadClosed();
        NOTE_CONNRESET = KQueueStaticallyReferencedJniMethods.noteConnReset();
        NOTE_DISCONNECTED = KQueueStaticallyReferencedJniMethods.noteDisconnected();
        NOTE_RDHUP = NOTE_READCLOSED | NOTE_CONNRESET | NOTE_DISCONNECTED;
        EV_ADD_CLEAR_ENABLE = (short) (EV_ADD | EV_CLEAR | EV_ENABLE);
        EV_DELETE_DISABLE = (short) (EV_DELETE | EV_DISABLE);
        EVFILT_READ = KQueueStaticallyReferencedJniMethods.evfiltRead();
        EVFILT_WRITE = KQueueStaticallyReferencedJniMethods.evfiltWrite();
        EVFILT_USER = KQueueStaticallyReferencedJniMethods.evfiltUser();
        EVFILT_SOCK = KQueueStaticallyReferencedJniMethods.evfiltSock();
        CONNECT_RESUME_ON_READ_WRITE = KQueueStaticallyReferencedJniMethods.connectResumeOnReadWrite();
        CONNECT_DATA_IDEMPOTENT = KQueueStaticallyReferencedJniMethods.connectDataIdempotent();
        CONNECT_TCP_FASTOPEN = CONNECT_RESUME_ON_READ_WRITE | CONNECT_DATA_IDEMPOTENT;
        IS_SUPPORTING_TCP_FASTOPEN_CLIENT = isSupportingFastOpenClient();
        IS_SUPPORTING_TCP_FASTOPEN_SERVER = isSupportingFastOpenServer();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static io.netty.channel.unix.FileDescriptor newKQueue() {
        return new io.netty.channel.unix.FileDescriptor(kqueueCreate());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int keventWait(int kqueueFd, KQueueEventArray changeList, KQueueEventArray eventList, int tvSec, int tvNsec) throws IOException {
        int ready = keventWait(kqueueFd, changeList.memoryAddress(), changeList.size(), eventList.memoryAddress(), eventList.capacity(), tvSec, tvNsec);
        if (ready < 0) {
            throw Errors.newIOException("kevent", ready);
        }
        return ready;
    }

    private static void loadNativeLibrary() {
        String name = PlatformDependent.normalizedOs();
        if (!"osx".equals(name) && !name.contains("bsd")) {
            throw new IllegalStateException("Only supported on OSX/BSD");
        }
        String sharedLibName = "netty_transport_native_kqueue_" + PlatformDependent.normalizedArch();
        ClassLoader cl = PlatformDependent.getClassLoader(Native.class);
        try {
            NativeLibraryLoader.load(sharedLibName, cl);
        } catch (UnsatisfiedLinkError e1) {
            try {
                NativeLibraryLoader.load("netty_transport_native_kqueue", cl);
                logger.debug("Failed to load {}", sharedLibName, e1);
            } catch (UnsatisfiedLinkError e2) {
                ThrowableUtil.addSuppressed(e1, e2);
                throw e1;
            }
        }
    }

    private static boolean isSupportingFastOpenClient() {
        try {
            return KQueueStaticallyReferencedJniMethods.fastOpenClient() == 1;
        } catch (Exception e) {
            logger.debug("Failed to probe fastOpenClient sysctl, assuming client-side TCP FastOpen cannot be used.", (Throwable) e);
            return false;
        }
    }

    private static boolean isSupportingFastOpenServer() {
        try {
            return KQueueStaticallyReferencedJniMethods.fastOpenServer() == 1;
        } catch (Exception e) {
            logger.debug("Failed to probe fastOpenServer sysctl, assuming server-side TCP FastOpen cannot be used.", (Throwable) e);
            return false;
        }
    }

    private Native() {
    }
}
