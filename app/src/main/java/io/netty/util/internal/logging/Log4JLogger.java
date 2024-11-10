package io.netty.util.internal.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/* loaded from: classes4.dex */
class Log4JLogger extends AbstractInternalLogger {
    static final String FQCN = Log4JLogger.class.getName();
    private static final long serialVersionUID = 2851357342488183058L;
    final transient Logger logger;
    final boolean traceCapable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Log4JLogger(Logger logger) {
        super(logger.getName());
        this.logger = logger;
        this.traceCapable = isTraceCapable();
    }

    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        } catch (NoSuchMethodError e) {
            return false;
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isTraceEnabled() {
        if (this.traceCapable) {
            return this.logger.isTraceEnabled();
        }
        return this.logger.isDebugEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String msg) {
        this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, msg, (Throwable) null);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String format, Object argA, Object argB) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String msg, Throwable t) {
        this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String msg) {
        this.logger.log(FQCN, Level.DEBUG, msg, (Throwable) null);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String format, Object arg) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(FQCN, Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String format, Object argA, Object argB) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(FQCN, Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String format, Object... arguments) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.log(FQCN, Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String msg, Throwable t) {
        this.logger.log(FQCN, Level.DEBUG, msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String msg) {
        this.logger.log(FQCN, Level.INFO, msg, (Throwable) null);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String format, Object arg) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(FQCN, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String format, Object argA, Object argB) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(FQCN, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String format, Object... argArray) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(FQCN, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String msg, Throwable t) {
        this.logger.log(FQCN, Level.INFO, msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isWarnEnabled() {
        return this.logger.isEnabledFor(Level.WARN);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String msg) {
        this.logger.log(FQCN, Level.WARN, msg, (Throwable) null);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String format, Object arg) {
        if (this.logger.isEnabledFor(Level.WARN)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(FQCN, Level.WARN, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String format, Object argA, Object argB) {
        if (this.logger.isEnabledFor(Level.WARN)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(FQCN, Level.WARN, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String format, Object... argArray) {
        if (this.logger.isEnabledFor(Level.WARN)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(FQCN, Level.WARN, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String msg, Throwable t) {
        this.logger.log(FQCN, Level.WARN, msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isErrorEnabled() {
        return this.logger.isEnabledFor(Level.ERROR);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String msg) {
        this.logger.log(FQCN, Level.ERROR, msg, (Throwable) null);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String format, Object arg) {
        if (this.logger.isEnabledFor(Level.ERROR)) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(FQCN, Level.ERROR, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String format, Object argA, Object argB) {
        if (this.logger.isEnabledFor(Level.ERROR)) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(FQCN, Level.ERROR, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String format, Object... argArray) {
        if (this.logger.isEnabledFor(Level.ERROR)) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(FQCN, Level.ERROR, ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String msg, Throwable t) {
        this.logger.log(FQCN, Level.ERROR, msg, t);
    }
}
