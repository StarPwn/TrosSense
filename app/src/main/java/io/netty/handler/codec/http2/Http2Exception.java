package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ThrowableUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes4.dex */
public class Http2Exception extends Exception {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = -6941186345430164209L;
    private final Http2Error error;
    private final ShutdownHint shutdownHint;

    /* loaded from: classes4.dex */
    public enum ShutdownHint {
        NO_SHUTDOWN,
        GRACEFUL_SHUTDOWN,
        HARD_SHUTDOWN
    }

    public Http2Exception(Http2Error error) {
        this(error, ShutdownHint.HARD_SHUTDOWN);
    }

    public Http2Exception(Http2Error error, ShutdownHint shutdownHint) {
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    public Http2Exception(Http2Error error, String message) {
        this(error, message, ShutdownHint.HARD_SHUTDOWN);
    }

    public Http2Exception(Http2Error error, String message, ShutdownHint shutdownHint) {
        super(message);
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    public Http2Exception(Http2Error error, String message, Throwable cause) {
        this(error, message, cause, ShutdownHint.HARD_SHUTDOWN);
    }

    public Http2Exception(Http2Error error, String message, Throwable cause, ShutdownHint shutdownHint) {
        super(message, cause);
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Http2Exception newStatic(Http2Error error, String message, ShutdownHint shutdownHint, Class<?> clazz, String method) {
        Http2Exception exception;
        if (PlatformDependent.javaVersion() >= 7) {
            exception = new StacklessHttp2Exception(error, message, shutdownHint, true);
        } else {
            exception = new StacklessHttp2Exception(error, message, shutdownHint);
        }
        return (Http2Exception) ThrowableUtil.unknownStackTrace(exception, clazz, method);
    }

    private Http2Exception(Http2Error error, String message, ShutdownHint shutdownHint, boolean shared) {
        super(message, null, false, true);
        if (!shared) {
            throw new AssertionError();
        }
        this.error = (Http2Error) ObjectUtil.checkNotNull(error, "error");
        this.shutdownHint = (ShutdownHint) ObjectUtil.checkNotNull(shutdownHint, "shutdownHint");
    }

    public Http2Error error() {
        return this.error;
    }

    public ShutdownHint shutdownHint() {
        return this.shutdownHint;
    }

    public static Http2Exception connectionError(Http2Error error, String fmt, Object... args) {
        return new Http2Exception(error, formatErrorMessage(fmt, args));
    }

    public static Http2Exception connectionError(Http2Error error, Throwable cause, String fmt, Object... args) {
        return new Http2Exception(error, formatErrorMessage(fmt, args), cause);
    }

    public static Http2Exception closedStreamError(Http2Error error, String fmt, Object... args) {
        return new ClosedStreamCreationException(error, formatErrorMessage(fmt, args));
    }

    public static Http2Exception streamError(int id, Http2Error error, String fmt, Object... args) {
        if (id == 0) {
            return connectionError(error, fmt, args);
        }
        return new StreamException(id, error, formatErrorMessage(fmt, args));
    }

    public static Http2Exception streamError(int id, Http2Error error, Throwable cause, String fmt, Object... args) {
        if (id == 0) {
            return connectionError(error, cause, fmt, args);
        }
        return new StreamException(id, error, formatErrorMessage(fmt, args), cause);
    }

    public static Http2Exception headerListSizeError(int id, Http2Error error, boolean onDecode, String fmt, Object... args) {
        if (id == 0) {
            return connectionError(error, fmt, args);
        }
        return new HeaderListSizeException(id, error, formatErrorMessage(fmt, args), onDecode);
    }

    private static String formatErrorMessage(String fmt, Object[] args) {
        if (fmt == null) {
            if (args == null || args.length == 0) {
                return "Unexpected error";
            }
            return "Unexpected error: " + Arrays.toString(args);
        }
        return String.format(fmt, args);
    }

    public static boolean isStreamError(Http2Exception e) {
        return e instanceof StreamException;
    }

    public static int streamId(Http2Exception e) {
        if (isStreamError(e)) {
            return ((StreamException) e).streamId();
        }
        return 0;
    }

    /* loaded from: classes4.dex */
    public static final class ClosedStreamCreationException extends Http2Exception {
        private static final long serialVersionUID = -6746542974372246206L;

        public ClosedStreamCreationException(Http2Error error) {
            super(error);
        }

        public ClosedStreamCreationException(Http2Error error, String message) {
            super(error, message);
        }

        public ClosedStreamCreationException(Http2Error error, String message, Throwable cause) {
            super(error, message, cause);
        }
    }

    /* loaded from: classes4.dex */
    public static class StreamException extends Http2Exception {
        private static final long serialVersionUID = 602472544416984384L;
        private final int streamId;

        StreamException(int streamId, Http2Error error, String message) {
            super(error, message, ShutdownHint.NO_SHUTDOWN);
            this.streamId = streamId;
        }

        StreamException(int streamId, Http2Error error, String message, Throwable cause) {
            super(error, message, cause, ShutdownHint.NO_SHUTDOWN);
            this.streamId = streamId;
        }

        public int streamId() {
            return this.streamId;
        }
    }

    /* loaded from: classes4.dex */
    public static final class HeaderListSizeException extends StreamException {
        private static final long serialVersionUID = -8807603212183882637L;
        private final boolean decode;

        HeaderListSizeException(int streamId, Http2Error error, String message, boolean decode) {
            super(streamId, error, message);
            this.decode = decode;
        }

        public boolean duringDecode() {
            return this.decode;
        }
    }

    /* loaded from: classes4.dex */
    public static final class CompositeStreamException extends Http2Exception implements Iterable<StreamException> {
        private static final long serialVersionUID = 7091134858213711015L;
        private final List<StreamException> exceptions;

        public CompositeStreamException(Http2Error error, int initialCapacity) {
            super(error, ShutdownHint.NO_SHUTDOWN);
            this.exceptions = new ArrayList(initialCapacity);
        }

        public void add(StreamException e) {
            this.exceptions.add(e);
        }

        @Override // java.lang.Iterable
        public Iterator<StreamException> iterator() {
            return this.exceptions.iterator();
        }
    }

    /* loaded from: classes4.dex */
    private static final class StacklessHttp2Exception extends Http2Exception {
        private static final long serialVersionUID = 1077888485687219443L;

        StacklessHttp2Exception(Http2Error error, String message, ShutdownHint shutdownHint) {
            super(error, message, shutdownHint);
        }

        StacklessHttp2Exception(Http2Error error, String message, ShutdownHint shutdownHint, boolean shared) {
            super(error, message, shutdownHint, shared);
        }

        @Override // java.lang.Throwable
        public Throwable fillInStackTrace() {
            return this;
        }
    }
}
