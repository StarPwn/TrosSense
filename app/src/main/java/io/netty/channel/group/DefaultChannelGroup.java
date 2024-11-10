package io.netty.channel.group;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.ServerChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes4.dex */
public class DefaultChannelGroup extends AbstractSet<Channel> implements ChannelGroup {
    private static final AtomicInteger nextId = new AtomicInteger();
    private volatile boolean closed;
    private final EventExecutor executor;
    private final String name;
    private final ConcurrentMap<ChannelId, Channel> nonServerChannels;
    private final ChannelFutureListener remover;
    private final ConcurrentMap<ChannelId, Channel> serverChannels;
    private final boolean stayClosed;
    private final VoidChannelGroupFuture voidFuture;

    public DefaultChannelGroup(EventExecutor executor) {
        this(executor, false);
    }

    public DefaultChannelGroup(String name, EventExecutor executor) {
        this(name, executor, false);
    }

    public DefaultChannelGroup(EventExecutor executor, boolean stayClosed) {
        this("group-0x" + Integer.toHexString(nextId.incrementAndGet()), executor, stayClosed);
    }

    public DefaultChannelGroup(String name, EventExecutor executor, boolean stayClosed) {
        this.serverChannels = PlatformDependent.newConcurrentHashMap();
        this.nonServerChannels = PlatformDependent.newConcurrentHashMap();
        this.remover = new ChannelFutureListener() { // from class: io.netty.channel.group.DefaultChannelGroup.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                DefaultChannelGroup.this.remove(future.channel());
            }
        };
        this.voidFuture = new VoidChannelGroupFuture(this);
        ObjectUtil.checkNotNull(name, "name");
        this.name = name;
        this.executor = executor;
        this.stayClosed = stayClosed;
    }

    @Override // io.netty.channel.group.ChannelGroup
    public String name() {
        return this.name;
    }

    @Override // io.netty.channel.group.ChannelGroup
    public Channel find(ChannelId id) {
        Channel c = this.nonServerChannels.get(id);
        if (c != null) {
            return c;
        }
        return this.serverChannels.get(id);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.nonServerChannels.size() + this.serverChannels.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object o) {
        if (o instanceof ServerChannel) {
            return this.serverChannels.containsValue(o);
        }
        if (o instanceof Channel) {
            return this.nonServerChannels.containsValue(o);
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(Channel channel) {
        ConcurrentMap<ChannelId, Channel> map = channel instanceof ServerChannel ? this.serverChannels : this.nonServerChannels;
        boolean added = map.putIfAbsent(channel.id(), channel) == null;
        if (added) {
            channel.closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) this.remover);
        }
        if (this.stayClosed && this.closed) {
            channel.close();
        }
        return added;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        Channel c = null;
        if (o instanceof ChannelId) {
            Channel c2 = this.nonServerChannels.remove(o);
            c = c2;
            if (c == null) {
                Channel c3 = this.serverChannels.remove(o);
                c = c3;
            }
        } else if (o instanceof Channel) {
            Channel c4 = (Channel) o;
            if (c4 instanceof ServerChannel) {
                c = this.serverChannels.remove(c4.id());
            } else {
                c = this.nonServerChannels.remove(c4.id());
            }
        }
        if (c == null) {
            return false;
        }
        c.closeFuture().removeListener((GenericFutureListener<? extends Future<? super Void>>) this.remover);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.nonServerChannels.clear();
        this.serverChannels.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<Channel> iterator() {
        return new CombinedIterator(this.serverChannels.values().iterator(), this.nonServerChannels.values().iterator());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public Object[] toArray() {
        Collection<Channel> channels = new ArrayList<>(size());
        channels.addAll(this.serverChannels.values());
        channels.addAll(this.nonServerChannels.values());
        return channels.toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        ArrayList arrayList = new ArrayList(size());
        arrayList.addAll(this.serverChannels.values());
        arrayList.addAll(this.nonServerChannels.values());
        return (T[]) arrayList.toArray(tArr);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture close() {
        return close(ChannelMatchers.all());
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture disconnect() {
        return disconnect(ChannelMatchers.all());
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture deregister() {
        return deregister(ChannelMatchers.all());
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture write(Object message) {
        return write(message, ChannelMatchers.all());
    }

    private static Object safeDuplicate(Object message) {
        if (message instanceof ByteBuf) {
            return ((ByteBuf) message).retainedDuplicate();
        }
        if (message instanceof ByteBufHolder) {
            return ((ByteBufHolder) message).retainedDuplicate();
        }
        return ReferenceCountUtil.retain(message);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture write(Object message, ChannelMatcher matcher) {
        return write(message, matcher, false);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture write(Object message, ChannelMatcher matcher, boolean voidPromise) {
        ChannelGroupFuture future;
        ObjectUtil.checkNotNull(message, "message");
        ObjectUtil.checkNotNull(matcher, "matcher");
        if (voidPromise) {
            for (Channel c : this.nonServerChannels.values()) {
                if (matcher.matches(c)) {
                    c.write(safeDuplicate(message), c.voidPromise());
                }
            }
            future = this.voidFuture;
        } else {
            Map<Channel, ChannelFuture> futures = new LinkedHashMap<>(this.nonServerChannels.size());
            for (Channel c2 : this.nonServerChannels.values()) {
                if (matcher.matches(c2)) {
                    futures.put(c2, c2.write(safeDuplicate(message)));
                }
            }
            future = new DefaultChannelGroupFuture(this, futures, this.executor);
        }
        ReferenceCountUtil.release(message);
        return future;
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroup flush() {
        return flush(ChannelMatchers.all());
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture flushAndWrite(Object message) {
        return writeAndFlush(message);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture writeAndFlush(Object message) {
        return writeAndFlush(message, ChannelMatchers.all());
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture disconnect(ChannelMatcher matcher) {
        ObjectUtil.checkNotNull(matcher, "matcher");
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<>(size());
        for (Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.disconnect());
            }
        }
        for (Channel c2 : this.nonServerChannels.values()) {
            if (matcher.matches(c2)) {
                futures.put(c2, c2.disconnect());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture close(ChannelMatcher matcher) {
        ObjectUtil.checkNotNull(matcher, "matcher");
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<>(size());
        if (this.stayClosed) {
            this.closed = true;
        }
        for (Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.close());
            }
        }
        for (Channel c2 : this.nonServerChannels.values()) {
            if (matcher.matches(c2)) {
                futures.put(c2, c2.close());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture deregister(ChannelMatcher matcher) {
        ObjectUtil.checkNotNull(matcher, "matcher");
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<>(size());
        for (Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.deregister());
            }
        }
        for (Channel c2 : this.nonServerChannels.values()) {
            if (matcher.matches(c2)) {
                futures.put(c2, c2.deregister());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroup flush(ChannelMatcher matcher) {
        for (Channel c : this.nonServerChannels.values()) {
            if (matcher.matches(c)) {
                c.flush();
            }
        }
        return this;
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture flushAndWrite(Object message, ChannelMatcher matcher) {
        return writeAndFlush(message, matcher);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher) {
        return writeAndFlush(message, matcher, false);
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture writeAndFlush(Object message, ChannelMatcher matcher, boolean voidPromise) {
        ChannelGroupFuture future;
        ObjectUtil.checkNotNull(message, "message");
        if (voidPromise) {
            for (Channel c : this.nonServerChannels.values()) {
                if (matcher.matches(c)) {
                    c.writeAndFlush(safeDuplicate(message), c.voidPromise());
                }
            }
            future = this.voidFuture;
        } else {
            Map<Channel, ChannelFuture> futures = new LinkedHashMap<>(this.nonServerChannels.size());
            for (Channel c2 : this.nonServerChannels.values()) {
                if (matcher.matches(c2)) {
                    futures.put(c2, c2.writeAndFlush(safeDuplicate(message)));
                }
            }
            future = new DefaultChannelGroupFuture(this, futures, this.executor);
        }
        ReferenceCountUtil.release(message);
        return future;
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture newCloseFuture() {
        return newCloseFuture(ChannelMatchers.all());
    }

    @Override // io.netty.channel.group.ChannelGroup
    public ChannelGroupFuture newCloseFuture(ChannelMatcher matcher) {
        Map<Channel, ChannelFuture> futures = new LinkedHashMap<>(size());
        for (Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.closeFuture());
            }
        }
        for (Channel c2 : this.nonServerChannels.values()) {
            if (matcher.matches(c2)) {
                futures.put(c2, c2.closeFuture());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
    public boolean equals(Object o) {
        return this == o;
    }

    @Override // java.lang.Comparable
    public int compareTo(ChannelGroup o) {
        int v = name().compareTo(o.name());
        if (v != 0) {
            return v;
        }
        return System.identityHashCode(this) - System.identityHashCode(o);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return StringUtil.simpleClassName(this) + "(name: " + name() + ", size: " + size() + ')';
    }
}
