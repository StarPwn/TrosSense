package org.cloudburstmc.netty.channel.raknet;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;

/* loaded from: classes5.dex */
public class RakConstants {
    public static final long CC_ADDITIONAL_VARIANCE = 30;
    public static final long CC_MAXIMUM_THRESHOLD = 2000;
    public static final long CC_SYN = 10;
    public static final int DEFAULT_GLOBAL_PACKET_LIMIT = 100000;
    public static final int DEFAULT_OFFLINE_PACKET_LIMIT = 10;
    public static final int DEFAULT_PACKET_LIMIT = 120;
    public static final byte FLAG_ACK = 64;
    public static final byte FLAG_CONTINUOUS_SEND = 8;
    public static final byte FLAG_HAS_B_AND_AS = 32;
    public static final byte FLAG_NACK = 32;
    public static final byte FLAG_NEEDS_B_AND_AS = 4;
    public static final byte FLAG_PACKET_PAIR = 16;
    public static final byte FLAG_VALID = Byte.MIN_VALUE;
    public static final short ID_ADVERTISE_SYSTEM = 29;
    public static final short ID_ALREADY_CONNECTED = 18;
    public static final short ID_CONNECTED_PING = 0;
    public static final short ID_CONNECTED_PONG = 3;
    public static final short ID_CONNECTION_BANNED = 23;
    public static final short ID_CONNECTION_LOST = 22;
    public static final short ID_CONNECTION_REQUEST = 9;
    public static final short ID_CONNECTION_REQUEST_ACCEPTED = 16;
    public static final short ID_CONNECTION_REQUEST_FAILED = 17;
    public static final short ID_DETECT_LOST_CONNECTION = 4;
    public static final short ID_DISCONNECTION_NOTIFICATION = 21;
    public static final short ID_INCOMPATIBLE_PROTOCOL_VERSION = 25;
    public static final short ID_IP_RECENTLY_CONNECTED = 26;
    public static final short ID_NEW_INCOMING_CONNECTION = 19;
    public static final short ID_NO_FREE_INCOMING_CONNECTIONS = 20;
    public static final short ID_OPEN_CONNECTION_REPLY_1 = 6;
    public static final short ID_OPEN_CONNECTION_REPLY_2 = 8;
    public static final short ID_OPEN_CONNECTION_REQUEST_1 = 5;
    public static final short ID_OPEN_CONNECTION_REQUEST_2 = 7;
    public static final short ID_TIMESTAMP = 27;
    public static final short ID_UNCONNECTED_PING = 1;
    public static final short ID_UNCONNECTED_PING_OPEN_CONNECTIONS = 2;
    public static final short ID_UNCONNECTED_PONG = 28;
    public static final short ID_USER_PACKET_ENUM = 128;
    public static final int IPV4_MESSAGE_SIZE = 7;
    public static final int IPV6_MESSAGE_SIZE = 29;
    public static final int MAXIMUM_CONNECTION_ATTEMPTS = 10;
    public static final int MAXIMUM_ENCAPSULATED_HEADER_SIZE = 28;
    public static final int MAXIMUM_MTU_SIZE = 1400;
    public static final int MAXIMUM_ORDERING_CHANNELS = 16;
    public static final int MINIMUM_MTU_SIZE = 576;
    public static final int RAKNET_DATAGRAM_HEADER_SIZE = 4;
    public static final byte RAKNET_PROTOCOL_VERSION = 11;
    public static final int SESSION_STALE_MS = 5000;
    public static final int SESSION_TIMEOUT_MS = 10000;
    public static final int UDP_HEADER_SIZE = 8;
    public static final byte[] DEFAULT_UNCONNECTED_MAGIC = {0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120};
    public static final InetSocketAddress LOOPBACK_V4 = new InetSocketAddress(Inet4Address.getLoopbackAddress(), 0);
    public static final InetSocketAddress LOOPBACK_V6 = new InetSocketAddress(Inet6Address.getLoopbackAddress(), 0);
    public static final InetSocketAddress LOCAL_ADDRESS = new InetSocketAddress(0);
    public static final InetSocketAddress[] LOCAL_IP_ADDRESSES_V4 = new InetSocketAddress[10];
    public static final InetSocketAddress[] LOCAL_IP_ADDRESSES_V6 = new InetSocketAddress[10];

    static {
        LOCAL_IP_ADDRESSES_V4[0] = LOOPBACK_V4;
        LOCAL_IP_ADDRESSES_V6[0] = LOOPBACK_V6;
        for (int i = 1; i < 10; i++) {
            LOCAL_IP_ADDRESSES_V4[i] = new InetSocketAddress("0.0.0.0", 0);
            LOCAL_IP_ADDRESSES_V6[i] = new InetSocketAddress("::0", 0);
        }
    }
}
