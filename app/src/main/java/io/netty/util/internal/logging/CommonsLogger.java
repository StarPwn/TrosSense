package io.netty.util.internal.logging;

import io.netty.util.internal.ObjectUtil;
import org.apache.commons.logging.Log;

@Deprecated
/* loaded from: classes4.dex */
class CommonsLogger extends AbstractInternalLogger {
    private static final long serialVersionUID = 8647838678388394885L;
    private final transient Log logger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CommonsLogger(Log logger, String name) {
        super(name);
        this.logger = (Log) ObjectUtil.checkNotNull(logger, "logger");
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String msg) {
        this.logger.trace(msg);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String format, Object arg) {
        if (this.logger.isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.trace(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String format, Object argA, Object argB) {
        if (this.logger.isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.trace(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String format, Object... arguments) {
        if (this.logger.isTraceEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.trace(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void trace(String msg, Throwable t) {
        this.logger.trace(msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String msg) {
        this.logger.debug(msg);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String format, Object arg) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String format, Object argA, Object argB) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String format, Object... arguments) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.debug(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void debug(String msg, Throwable t) {
        this.logger.debug(msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String msg) {
        this.logger.info(msg);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String format, Object arg) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.info(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String format, Object argA, Object argB) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.info(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String format, Object... arguments) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.info(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void info(String msg, Throwable t) {
        this.logger.info(msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String msg) {
        this.logger.warn(msg);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String format, Object arg) {
        if (this.logger.isWarnEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String format, Object argA, Object argB) {
        if (this.logger.isWarnEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String format, Object... arguments) {
        if (this.logger.isWarnEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.warn(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void warn(String msg, Throwable t) {
        this.logger.warn(msg, t);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String msg) {
        this.logger.error(msg);
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String format, Object arg) {
        if (this.logger.isErrorEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.error(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String format, Object argA, Object argB) {
        if (this.logger.isErrorEnabled()) {
            FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.error(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String format, Object... arguments) {
        if (this.logger.isErrorEnabled()) {
            FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.error(ft.getMessage(), ft.getThrowable());
        }
    }

    @Override // io.netty.util.internal.logging.InternalLogger
    public void error(String msg, Throwable t) {
        this.logger.error(msg, t);
    }
}
