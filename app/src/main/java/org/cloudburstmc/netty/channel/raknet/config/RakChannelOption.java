package org.cloudburstmc.netty.channel.raknet.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;

/* loaded from: classes5.dex */
public class RakChannelOption<T> extends ChannelOption<T> {
    public static final ChannelOption<Integer> RAK_MAX_CHANNELS = valueOf(RakChannelOption.class, "RAK_MAX_CHANNELS");
    public static final ChannelOption<Integer> RAK_ORDERING_CHANNELS = valueOf(RakChannelOption.class, "RAK_ORDERING_CHANNELS");
    public static final ChannelOption<Integer> RAK_MTU = valueOf(RakChannelOption.class, "RAK_MTU");
    public static final ChannelOption<Integer> RAK_MAX_MTU = valueOf(RakChannelOption.class, "RAK_MAX_MTU");
    public static final ChannelOption<Integer> RAK_MIN_MTU = valueOf(RakChannelOption.class, "RAK_MIN_MTU");
    public static final ChannelOption<Long> RAK_GUID = valueOf(RakChannelOption.class, "RAK_GUID");
    public static final ChannelOption<Long> RAK_REMOTE_GUID = valueOf(RakChannelOption.class, "RAK_REMOTE_GUID");
    public static final ChannelOption<Integer> RAK_MAX_CONNECTIONS = valueOf(RakChannelOption.class, "RAK_MAX_CONNECTIONS");
    public static final ChannelOption<Integer> RAK_PROTOCOL_VERSION = valueOf(RakChannelOption.class, "RAK_PROTOCOL_VERSION");
    public static final ChannelOption<int[]> RAK_SUPPORTED_PROTOCOLS = valueOf(RakChannelOption.class, "RAK_SUPPORTED_PROTOCOLS");
    public static final ChannelOption<ByteBuf> RAK_UNCONNECTED_MAGIC = valueOf(RakChannelOption.class, "RAK_UNCONNECTED_MAGIC");
    public static final ChannelOption<Long> RAK_CONNECT_TIMEOUT = valueOf(RakChannelOption.class, "RAK_CONNECT_TIMEOUT");
    public static final ChannelOption<RakMetrics> RAK_METRICS = valueOf(RakChannelOption.class, "RAK_METRICS");
    public static final ChannelOption<ByteBuf> RAK_ADVERTISEMENT = valueOf(RakChannelOption.class, "RAK_ADVERTISEMENT");
    public static final ChannelOption<Boolean> RAK_HANDLE_PING = valueOf(RakChannelOption.class, "RAK_HANDLE_PING");
    public static final ChannelOption<Long> RAK_SESSION_TIMEOUT = valueOf(RakChannelOption.class, "RAK_SESSION_TIMEOUT");
    public static final ChannelOption<Boolean> RAK_AUTO_FLUSH = valueOf(RakChannelOption.class, "RAK_AUTO_FLUSH");
    public static final ChannelOption<Integer> RAK_FLUSH_INTERVAL = valueOf(RakChannelOption.class, "RAK_FLUSH_INTERVAL");
    public static final ChannelOption<Integer> RAK_PACKET_LIMIT = valueOf(RakChannelOption.class, "RAK_PACKET_LIMIT");
    public static final ChannelOption<Integer> RAK_OFFLINE_PACKET_LIMIT = valueOf(RakChannelOption.class, "RAK_OFFLINE_PACKET_LIMIT");
    public static final ChannelOption<Integer> RAK_GLOBAL_PACKET_LIMIT = valueOf(RakChannelOption.class, "RAK_GLOBAL_PACKET_LIMIT");

    protected RakChannelOption() {
        super(null);
    }
}
