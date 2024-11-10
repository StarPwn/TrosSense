package io.netty.channel.oio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.RecvByteBufAllocator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
/* loaded from: classes4.dex */
public abstract class AbstractOioMessageChannel extends AbstractOioChannel {
    private final List<Object> readBuf;

    protected abstract int doReadMessages(List<Object> list) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractOioMessageChannel(Channel parent) {
        super(parent);
        this.readBuf = new ArrayList();
    }

    @Override // io.netty.channel.oio.AbstractOioChannel
    protected void doRead() {
        if (!this.readPending) {
            return;
        }
        this.readPending = false;
        ChannelConfig config = config();
        ChannelPipeline pipeline = pipeline();
        RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
        allocHandle.reset(config);
        boolean closed = false;
        Throwable exception = null;
        while (true) {
            try {
                int localRead = doReadMessages(this.readBuf);
                if (localRead == 0) {
                    break;
                }
                if (localRead < 0) {
                    closed = true;
                    break;
                } else {
                    allocHandle.incMessagesRead(localRead);
                    if (!allocHandle.continueReading()) {
                        break;
                    }
                }
            } catch (Throwable t) {
                exception = t;
            }
        }
        boolean readData = false;
        int size = this.readBuf.size();
        if (size > 0) {
            readData = true;
            for (int i = 0; i < size; i++) {
                this.readPending = false;
                pipeline.fireChannelRead(this.readBuf.get(i));
            }
            this.readBuf.clear();
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();
        }
        if (exception != null) {
            if (exception instanceof IOException) {
                closed = true;
            }
            pipeline.fireExceptionCaught(exception);
        }
        if (closed) {
            if (isOpen()) {
                unsafe().close(unsafe().voidPromise());
            }
        } else if (this.readPending || config.isAutoRead() || (!readData && isActive())) {
            read();
        }
    }
}
