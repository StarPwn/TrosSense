package io.netty.channel.socket.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import java.io.IOException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.Channel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public final class NioChannelOption<T> extends ChannelOption<T> {
    private final SocketOption<T> option;

    private NioChannelOption(SocketOption<T> option) {
        super(option.name());
        this.option = option;
    }

    public static <T> ChannelOption<T> of(SocketOption<T> option) {
        return new NioChannelOption(option);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> boolean setOption(Channel jdkChannel, NioChannelOption<T> option, T value) {
        NetworkChannel channel = (NetworkChannel) jdkChannel;
        if (!channel.supportedOptions().contains(((NioChannelOption) option).option)) {
            return false;
        }
        if ((channel instanceof ServerSocketChannel) && ((NioChannelOption) option).option == StandardSocketOptions.IP_TOS) {
            return false;
        }
        try {
            channel.setOption(((NioChannelOption) option).option, value);
            return true;
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T getOption(Channel channel, NioChannelOption<T> nioChannelOption) {
        NetworkChannel networkChannel = (NetworkChannel) channel;
        if (!networkChannel.supportedOptions().contains(((NioChannelOption) nioChannelOption).option)) {
            return null;
        }
        if ((networkChannel instanceof ServerSocketChannel) && ((NioChannelOption) nioChannelOption).option == StandardSocketOptions.IP_TOS) {
            return null;
        }
        try {
            return (T) networkChannel.getOption(((NioChannelOption) nioChannelOption).option);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ChannelOption[] getOptions(Channel jdkChannel) {
        NetworkChannel channel = (NetworkChannel) jdkChannel;
        Set<SocketOption<?>> supportedOpts = channel.supportedOptions();
        if (channel instanceof ServerSocketChannel) {
            List<ChannelOption<?>> extraOpts = new ArrayList<>(supportedOpts.size());
            for (SocketOption<?> opt : supportedOpts) {
                if (opt != StandardSocketOptions.IP_TOS) {
                    extraOpts.add(new NioChannelOption<>(opt));
                }
            }
            return (ChannelOption[]) extraOpts.toArray(new ChannelOption[0]);
        }
        ChannelOption<?>[] extraOpts2 = new ChannelOption[supportedOpts.size()];
        int i = 0;
        Iterator<SocketOption<?>> it2 = supportedOpts.iterator();
        while (it2.hasNext()) {
            extraOpts2[i] = new NioChannelOption<>(it2.next());
            i++;
        }
        return extraOpts2;
    }
}
