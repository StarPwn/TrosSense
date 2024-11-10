package io.netty.util.internal.logging;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.io.ObjectStreamException;
import java.io.Serializable;

/* loaded from: classes4.dex */
public abstract class AbstractInternalLogger implements InternalLogger, Serializable {
    static final String EXCEPTION_MESSAGE = "Unexpected exception:";
    private static final long serialVersionUID = -6382972526573193470L;
    private final String name;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractInternalLogger(String name) {
        this.name = (String) ObjectUtil.checkNotNull(name, "name");
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public String name() {
        return this.name;
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isEnabled(InternalLogLevel level) {
        switch (level) {
            case TRACE:
                return isTraceEnabled();
            case DEBUG:
                return isDebugEnabled();
            case INFO:
                return isInfoEnabled();
            case WARN:
                return isWarnEnabled();
            case ERROR:
                return isErrorEnabled();
            default:
                throw new Error();
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(Throwable t) {
        trace(EXCEPTION_MESSAGE, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(Throwable t) {
        debug(EXCEPTION_MESSAGE, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(Throwable t) {
        info(EXCEPTION_MESSAGE, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(Throwable t) {
        warn(EXCEPTION_MESSAGE, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(Throwable t) {
        error(EXCEPTION_MESSAGE, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void log(InternalLogLevel level, String msg, Throwable cause) {
        switch (level) {
            case TRACE:
                trace(msg, cause);
                return;
            case DEBUG:
                debug(msg, cause);
                return;
            case INFO:
                info(msg, cause);
                return;
            case WARN:
                warn(msg, cause);
                return;
            case ERROR:
                error(msg, cause);
                return;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void log(InternalLogLevel level, Throwable cause) {
        switch (level) {
            case TRACE:
                trace(cause);
                return;
            case DEBUG:
                debug(cause);
                return;
            case INFO:
                info(cause);
                return;
            case WARN:
                warn(cause);
                return;
            case ERROR:
                error(cause);
                return;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void log(InternalLogLevel level, String msg) {
        switch (level) {
            case TRACE:
                trace(msg);
                return;
            case DEBUG:
                debug(msg);
                return;
            case INFO:
                info(msg);
                return;
            case WARN:
                warn(msg);
                return;
            case ERROR:
                error(msg);
                return;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void log(InternalLogLevel level, String format, Object arg) {
        switch (level) {
            case TRACE:
                trace(format, arg);
                return;
            case DEBUG:
                debug(format, arg);
                return;
            case INFO:
                info(format, arg);
                return;
            case WARN:
                warn(format, arg);
                return;
            case ERROR:
                error(format, arg);
                return;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void log(InternalLogLevel level, String format, Object argA, Object argB) {
        switch (level) {
            case TRACE:
                trace(format, argA, argB);
                return;
            case DEBUG:
                debug(format, argA, argB);
                return;
            case INFO:
                info(format, argA, argB);
                return;
            case WARN:
                warn(format, argA, argB);
                return;
            case ERROR:
                error(format, argA, argB);
                return;
            default:
                throw new Error();
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void log(InternalLogLevel level, String format, Object... arguments) {
        switch (level) {
            case TRACE:
                trace(format, arguments);
                return;
            case DEBUG:
                debug(format, arguments);
                return;
            case INFO:
                info(format, arguments);
                return;
            case WARN:
                warn(format, arguments);
                return;
            case ERROR:
                error(format, arguments);
                return;
            default:
                throw new Error();
        }
    }

    protected Object readResolve() throws ObjectStreamException {
        return InternalLoggerFactory.getInstance(name());
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + name() + ')';
    }
}
