package io.netty.channel.kqueue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import java.io.IOException;

/* loaded from: classes4.dex */
abstract class AbstractKQueueDatagramChannel extends AbstractKQueueChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(true, 16);

    protected abstract boolean doWriteMessage(Object obj) throws Exception;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractKQueueDatagramChannel(Channel parent, BsdSocket fd, boolean active) {
        super(parent, fd, active);
    }

    @Override // io.netty.channel.kqueue.AbstractKQueueChannel, io.netty.channel.Channel
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override // io.netty.channel.AbstractChannel
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        int maxMessagesPerWrite = maxMessagesPerWrite();
        while (maxMessagesPerWrite > 0) {
            Object msg = in.current();
            if (msg != null) {
                boolean done = false;
                try {
                    int i = config().getWriteSpinCount();
                    while (true) {
                        if (i <= 0) {
                            break;
                        }
                        if (!doWriteMessage(msg)) {
                            i--;
                        } else {
                            done = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    maxMessagesPerWrite--;
                    in.remove(e);
                }
                if (!done) {
                    break;
                }
                in.remove();
                maxMessagesPerWrite--;
            } else {
                break;
            }
        }
        writeFilter(!in.isEmpty());
    }
}
