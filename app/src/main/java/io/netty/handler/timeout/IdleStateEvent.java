package io.netty.handler.timeout;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

/* loaded from: classes4.dex */
public class IdleStateEvent {
    private final boolean first;
    private final IdleState state;
    public static final IdleStateEvent FIRST_READER_IDLE_STATE_EVENT = new DefaultIdleStateEvent(IdleState.READER_IDLE, true);
    public static final IdleStateEvent READER_IDLE_STATE_EVENT = new DefaultIdleStateEvent(IdleState.READER_IDLE, false);
    public static final IdleStateEvent FIRST_WRITER_IDLE_STATE_EVENT = new DefaultIdleStateEvent(IdleState.WRITER_IDLE, true);
    public static final IdleStateEvent WRITER_IDLE_STATE_EVENT = new DefaultIdleStateEvent(IdleState.WRITER_IDLE, false);
    public static final IdleStateEvent FIRST_ALL_IDLE_STATE_EVENT = new DefaultIdleStateEvent(IdleState.ALL_IDLE, true);
    public static final IdleStateEvent ALL_IDLE_STATE_EVENT = new DefaultIdleStateEvent(IdleState.ALL_IDLE, false);

    protected IdleStateEvent(IdleState state, boolean first) {
        this.state = (IdleState) ObjectUtil.checkNotNull(state, "state");
        this.first = first;
    }

    public IdleState state() {
        return this.state;
    }

    public boolean isFirst() {
        return this.first;
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.state + (this.first ? ", first" : "") + ')';
    }

    /* loaded from: classes4.dex */
    private static final class DefaultIdleStateEvent extends IdleStateEvent {
        private final String representation;

        DefaultIdleStateEvent(IdleState state, boolean first) {
            super(state, first);
            this.representation = "IdleStateEvent(" + state + (first ? ", first" : "") + ')';
        }

        @Override // io.netty.handler.timeout.IdleStateEvent
        public String toString() {
            return this.representation;
        }
    }
}
