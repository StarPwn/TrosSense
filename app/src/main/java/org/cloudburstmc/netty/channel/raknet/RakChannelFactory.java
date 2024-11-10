package org.cloudburstmc.netty.channel.raknet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFactory;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.internal.StringUtil;
import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.function.Consumer;

/* loaded from: classes5.dex */
public class RakChannelFactory<T extends Channel> implements ChannelFactory<T> {
    private final Constructor<? extends T> constructor;
    private final Constructor<? extends DatagramChannel> datagramConstructor;
    private final Consumer<DatagramChannel> parentConsumer;

    private RakChannelFactory(Class<? extends T> clazz, Class<? extends DatagramChannel> datagramClass, Consumer<DatagramChannel> parentConsumer) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(datagramClass, "datagramClass");
        try {
            this.constructor = clazz.getConstructor(DatagramChannel.class);
            try {
                this.datagramConstructor = datagramClass.getConstructor(new Class[0]);
                this.parentConsumer = parentConsumer;
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("Class " + StringUtil.simpleClassName((Class<?>) clazz) + " does not have a public non-arg constructor", e);
            }
        } catch (NoSuchMethodException e2) {
            throw new IllegalArgumentException("Proxy class " + StringUtil.simpleClassName((Class<?>) clazz) + " does not have a public non-arg constructor", e2);
        }
    }

    public static RakChannelFactory<RakServerChannel> server(Class<? extends DatagramChannel> clazz) {
        return new RakChannelFactory<>(RakServerChannel.class, clazz, null);
    }

    public static RakChannelFactory<RakServerChannel> server(Class<? extends DatagramChannel> clazz, Consumer<DatagramChannel> parentConsumer) {
        return new RakChannelFactory<>(RakServerChannel.class, clazz, parentConsumer);
    }

    public static RakChannelFactory<RakClientChannel> client(Class<? extends DatagramChannel> clazz) {
        return new RakChannelFactory<>(RakClientChannel.class, clazz, null);
    }

    public static RakChannelFactory<RakClientChannel> client(Class<? extends DatagramChannel> clazz, Consumer<DatagramChannel> parentConsumer) {
        return new RakChannelFactory<>(RakClientChannel.class, clazz, parentConsumer);
    }

    @Override // io.netty.channel.ChannelFactory, io.netty.bootstrap.ChannelFactory
    public T newChannel() {
        try {
            DatagramChannel channel = this.datagramConstructor.newInstance(new Object[0]);
            if (this.parentConsumer != null) {
                this.parentConsumer.accept(channel);
            }
            return this.constructor.newInstance(channel);
        } catch (Throwable t) {
            throw new ChannelException("Unable to create Channel from class " + this.constructor.getDeclaringClass(), t);
        }
    }

    public String toString() {
        return StringUtil.simpleClassName((Class<?>) RakChannelFactory.class) + '(' + StringUtil.simpleClassName((Class<?>) this.constructor.getDeclaringClass()) + ".class, " + StringUtil.simpleClassName((Class<?>) this.datagramConstructor.getDeclaringClass()) + ".class)";
    }
}
