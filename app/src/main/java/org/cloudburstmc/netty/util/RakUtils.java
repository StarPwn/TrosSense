package org.cloudburstmc.netty.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.DefaultChannelPipeline;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Queue;

/* loaded from: classes5.dex */
public class RakUtils {
    private static final int AF_INET6 = 23;
    private static final Constructor<DefaultChannelPipeline> DEFAULT_CHANNEL_PIPELINE_CONSTRUCTOR;

    static {
        try {
            Constructor<DefaultChannelPipeline> constructor = DefaultChannelPipeline.class.getDeclaredConstructor(Channel.class);
            constructor.setAccessible(true);
            DEFAULT_CHANNEL_PIPELINE_CONSTRUCTOR = constructor;
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Unable to find DefaultChannelPipeline(Channel) constructor", e);
        }
    }

    public static DefaultChannelPipeline newChannelPipeline(Channel channel) {
        try {
            return DEFAULT_CHANNEL_PIPELINE_CONSTRUCTOR.newInstance(channel);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to instantiate DefaultChannelPipeline", e);
        }
    }

    public static InetSocketAddress readAddress(ByteBuf buffer) {
        int port;
        InetAddress address;
        short type = buffer.readByte();
        try {
            if (type == 4) {
                byte[] addressBytes = new byte[4];
                buffer.readBytes(addressBytes);
                flip(addressBytes);
                address = Inet4Address.getByAddress(addressBytes);
                port = buffer.readUnsignedShort();
            } else if (type == 6) {
                buffer.readShortLE();
                port = buffer.readUnsignedShort();
                buffer.readInt();
                byte[] addressBytes2 = new byte[16];
                buffer.readBytes(addressBytes2);
                int scopeId = buffer.readInt();
                address = Inet6Address.getByAddress((String) null, addressBytes2, scopeId);
            } else {
                throw new UnsupportedOperationException("Unknown Internet Protocol version.");
            }
            return new InetSocketAddress(address, port);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void writeAddress(ByteBuf buffer, InetSocketAddress address) {
        byte[] addressBytes = address.getAddress().getAddress();
        if (address.getAddress() instanceof Inet4Address) {
            buffer.writeByte(4);
            flip(addressBytes);
            buffer.writeBytes(addressBytes);
            buffer.writeShort(address.getPort());
            return;
        }
        if (address.getAddress() instanceof Inet6Address) {
            buffer.writeByte(6);
            buffer.writeShortLE(23);
            buffer.writeShort(address.getPort());
            buffer.writeInt(0);
            buffer.writeBytes(addressBytes);
            buffer.writeInt(((Inet6Address) address.getAddress()).getScopeId());
            return;
        }
        throw new UnsupportedOperationException("Unknown InetAddress instance");
    }

    private static void flip(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((~bytes[i]) & 255);
        }
    }

    public static int writeAckEntries(ByteBuf buffer, Queue<IntRange> ackQueue, int mtu) {
        int startIndex = buffer.writerIndex();
        buffer.writeZero(2);
        int mtu2 = mtu - 2;
        int count = 0;
        while (true) {
            IntRange ackRange = ackQueue.peek();
            if (ackRange == null) {
                break;
            }
            boolean singleton = ackRange.start == ackRange.end;
            int size = singleton ? 4 : 7;
            if (mtu2 < size) {
                break;
            }
            count++;
            mtu2 -= size;
            buffer.writeBoolean(singleton);
            buffer.writeMediumLE(ackRange.start);
            if (!singleton) {
                buffer.writeMediumLE(ackRange.end);
            }
            ackQueue.remove();
        }
        int finalIndex = buffer.writerIndex();
        buffer.writerIndex(startIndex);
        buffer.writeShort(count);
        buffer.writerIndex(finalIndex);
        return count;
    }

    public static int clamp(int value, int low, int high) {
        return value < low ? low : value > high ? high : value;
    }

    public static int powerOfTwoCeiling(int value) {
        int value2 = value - 1;
        int value3 = value2 | (value2 >> 1);
        int value4 = value3 | (value3 >> 2);
        int value5 = value4 | (value4 >> 4);
        int value6 = value5 | (value5 >> 8);
        return (value6 | (value6 >> 16)) + 1;
    }
}
