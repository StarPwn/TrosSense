package io.netty.channel.socket.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DefaultDatagramChannelConfig;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SocketUtils;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;
import java.util.Map;

/* loaded from: classes4.dex */
class NioDatagramChannelConfig extends DefaultDatagramChannelConfig {
    private static final Method GET_OPTION;
    private static final Object IP_MULTICAST_IF;
    private static final Object IP_MULTICAST_LOOP;
    private static final Object IP_MULTICAST_TTL;
    private static final Method SET_OPTION;
    private final DatagramChannel javaChannel;

    static {
        ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
        Class<?> socketOptionType = null;
        try {
            socketOptionType = Class.forName("java.net.SocketOption", true, classLoader);
        } catch (Exception e) {
        }
        Class<?> stdSocketOptionType = null;
        try {
            stdSocketOptionType = Class.forName("java.net.StandardSocketOptions", true, classLoader);
        } catch (Exception e2) {
        }
        Object ipMulticastTtl = null;
        Object ipMulticastIf = null;
        Object ipMulticastLoop = null;
        Method getOption = null;
        Method setOption = null;
        if (socketOptionType != null) {
            try {
                ipMulticastTtl = stdSocketOptionType.getDeclaredField("IP_MULTICAST_TTL").get(null);
                try {
                    ipMulticastIf = stdSocketOptionType.getDeclaredField("IP_MULTICAST_IF").get(null);
                    try {
                        ipMulticastLoop = stdSocketOptionType.getDeclaredField("IP_MULTICAST_LOOP").get(null);
                        Class<?> networkChannelClass = null;
                        try {
                            networkChannelClass = Class.forName("java.nio.channels.NetworkChannel", true, classLoader);
                        } catch (Throwable th) {
                        }
                        if (networkChannelClass == null) {
                            getOption = null;
                            setOption = null;
                        } else {
                            try {
                                getOption = networkChannelClass.getDeclaredMethod("getOption", socketOptionType);
                                try {
                                    setOption = networkChannelClass.getDeclaredMethod("setOption", socketOptionType, Object.class);
                                } catch (Exception e3) {
                                    throw new Error("cannot locate the setOption() method", e3);
                                }
                            } catch (Exception e4) {
                                throw new Error("cannot locate the getOption() method", e4);
                            }
                        }
                    } catch (Exception e5) {
                        throw new Error("cannot locate the IP_MULTICAST_LOOP field", e5);
                    }
                } catch (Exception e6) {
                    throw new Error("cannot locate the IP_MULTICAST_IF field", e6);
                }
            } catch (Exception e7) {
                throw new Error("cannot locate the IP_MULTICAST_TTL field", e7);
            }
        }
        IP_MULTICAST_TTL = ipMulticastTtl;
        IP_MULTICAST_IF = ipMulticastIf;
        IP_MULTICAST_LOOP = ipMulticastLoop;
        GET_OPTION = getOption;
        SET_OPTION = setOption;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NioDatagramChannelConfig(NioDatagramChannel channel, DatagramChannel javaChannel) {
        super(channel, javaChannel.socket());
        this.javaChannel = javaChannel;
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public int getTimeToLive() {
        return ((Integer) getOption0(IP_MULTICAST_TTL)).intValue();
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public DatagramChannelConfig setTimeToLive(int ttl) {
        setOption0(IP_MULTICAST_TTL, Integer.valueOf(ttl));
        return this;
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public InetAddress getInterface() {
        NetworkInterface inf = getNetworkInterface();
        if (inf != null) {
            Enumeration<InetAddress> addresses = SocketUtils.addressesFromNetworkInterface(inf);
            if (addresses.hasMoreElements()) {
                return addresses.nextElement();
            }
            return null;
        }
        return null;
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public DatagramChannelConfig setInterface(InetAddress interfaceAddress) {
        try {
            setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
            return this;
        } catch (SocketException e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public NetworkInterface getNetworkInterface() {
        return (NetworkInterface) getOption0(IP_MULTICAST_IF);
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
        setOption0(IP_MULTICAST_IF, networkInterface);
        return this;
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public boolean isLoopbackModeDisabled() {
        return ((Boolean) getOption0(IP_MULTICAST_LOOP)).booleanValue();
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.socket.DatagramChannelConfig
    public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled) {
        setOption0(IP_MULTICAST_LOOP, Boolean.valueOf(loopbackModeDisabled));
        return this;
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public DatagramChannelConfig setAutoRead(boolean autoRead) {
        super.setAutoRead(autoRead);
        return this;
    }

    @Override // io.netty.channel.DefaultChannelConfig
    protected void autoReadCleared() {
        ((NioDatagramChannel) this.channel).clearReadPending0();
    }

    private Object getOption0(Object option) {
        if (GET_OPTION == null) {
            throw new UnsupportedOperationException();
        }
        try {
            return GET_OPTION.invoke(this.javaChannel, option);
        } catch (Exception e) {
            throw new ChannelException(e);
        }
    }

    private void setOption0(Object option, Object value) {
        if (SET_OPTION == null) {
            throw new UnsupportedOperationException();
        }
        try {
            SET_OPTION.invoke(this.javaChannel, option, value);
        } catch (Exception e) {
            throw new ChannelException(e);
        }
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> boolean setOption(ChannelOption<T> option, T value) {
        if (PlatformDependent.javaVersion() >= 7 && (option instanceof NioChannelOption)) {
            return NioChannelOption.setOption(this.javaChannel, (NioChannelOption) option, value);
        }
        return super.setOption(option, value);
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (PlatformDependent.javaVersion() >= 7 && (channelOption instanceof NioChannelOption)) {
            return (T) NioChannelOption.getOption(this.javaChannel, (NioChannelOption) channelOption);
        }
        return (T) super.getOption(channelOption);
    }

    @Override // io.netty.channel.socket.DefaultDatagramChannelConfig, io.netty.channel.DefaultChannelConfig, io.netty.channel.ChannelConfig
    public Map<ChannelOption<?>, Object> getOptions() {
        if (PlatformDependent.javaVersion() >= 7) {
            return getOptions(super.getOptions(), NioChannelOption.getOptions(this.javaChannel));
        }
        return super.getOptions();
    }
}
