package io.netty.channel.unix;

/* loaded from: classes4.dex */
public abstract class GenericUnixChannelOption<T> extends UnixChannelOption<T> {
    private final int level;
    private final int optname;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GenericUnixChannelOption(String name, int level, int optname) {
        super(name);
        this.level = level;
        this.optname = optname;
    }

    public int level() {
        return this.level;
    }

    public int optname() {
        return this.optname;
    }
}
