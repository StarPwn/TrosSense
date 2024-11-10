package net.jodah.expiringmap.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes5.dex */
public class NamedThreadFactory implements ThreadFactory {
    private final String nameFormat;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public NamedThreadFactory(String nameFormat) {
        this.nameFormat = nameFormat;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, String.format(this.nameFormat, Integer.valueOf(this.threadNumber.getAndIncrement())));
        thread.setDaemon(true);
        return thread;
    }
}
