package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.Thread;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes4.dex */
public final class ThreadLocalRandom extends Random {
    private static final long addend = 11;
    private static volatile long initialSeedUniquifier = 0;
    private static final long mask = 281474976710655L;
    private static final long multiplier = 25214903917L;
    private static volatile long seedGeneratorEndTime = 0;
    private static final long seedGeneratorStartTime;
    private static final Thread seedGeneratorThread;
    private static final BlockingQueue<Long> seedQueue;
    private static final long serialVersionUID = -5851777807851030925L;
    boolean initialized;
    private long pad0;
    private long pad1;
    private long pad2;
    private long pad3;
    private long pad4;
    private long pad5;
    private long pad6;
    private long pad7;
    private long rnd;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ThreadLocalRandom.class);
    private static final AtomicLong seedUniquifier = new AtomicLong();

    static {
        initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L);
        if (initialSeedUniquifier == 0) {
            boolean secureRandom = SystemPropertyUtil.getBoolean("java.util.secureRandomSeed", false);
            if (secureRandom) {
                seedQueue = new LinkedBlockingQueue();
                seedGeneratorStartTime = System.nanoTime();
                seedGeneratorThread = new Thread("initialSeedUniquifierGenerator") { // from class: io.netty.util.internal.ThreadLocalRandom.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        SecureRandom random = new SecureRandom();
                        byte[] seed = random.generateSeed(8);
                        long unused = ThreadLocalRandom.seedGeneratorEndTime = System.nanoTime();
                        long s = ((seed[0] & 255) << 56) | ((seed[1] & 255) << 48) | ((seed[2] & 255) << 40) | ((seed[3] & 255) << 32) | ((seed[4] & 255) << 24) | ((seed[5] & 255) << 16) | ((seed[6] & 255) << 8) | (255 & seed[7]);
                        ThreadLocalRandom.seedQueue.add(Long.valueOf(s));
                    }
                };
                seedGeneratorThread.setDaemon(true);
                seedGeneratorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() { // from class: io.netty.util.internal.ThreadLocalRandom.2
                    @Override // java.lang.Thread.UncaughtExceptionHandler
                    public void uncaughtException(Thread t, Throwable e) {
                        ThreadLocalRandom.logger.debug("An exception has been raised by {}", t.getName(), e);
                    }
                });
                seedGeneratorThread.start();
                return;
            }
            initialSeedUniquifier = mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
            seedGeneratorThread = null;
            seedQueue = null;
            seedGeneratorStartTime = 0L;
            return;
        }
        seedGeneratorThread = null;
        seedQueue = null;
        seedGeneratorStartTime = 0L;
    }

    public static void setInitialSeedUniquifier(long initialSeedUniquifier2) {
        initialSeedUniquifier = initialSeedUniquifier2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0046, code lost:            r0 = r14.longValue();     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long getInitialSeedUniquifier() {
        /*
            long r0 = io.netty.util.internal.ThreadLocalRandom.initialSeedUniquifier
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 == 0) goto L9
            return r0
        L9:
            java.lang.Class<io.netty.util.internal.ThreadLocalRandom> r4 = io.netty.util.internal.ThreadLocalRandom.class
            monitor-enter(r4)
            long r5 = io.netty.util.internal.ThreadLocalRandom.initialSeedUniquifier     // Catch: java.lang.Throwable -> L96
            r0 = r5
            int r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r5 == 0) goto L15
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L96
            return r0
        L15:
            r5 = 3
            long r7 = io.netty.util.internal.ThreadLocalRandom.seedGeneratorStartTime     // Catch: java.lang.Throwable -> L96
            java.util.concurrent.TimeUnit r9 = java.util.concurrent.TimeUnit.SECONDS     // Catch: java.lang.Throwable -> L96
            r10 = 3
            long r12 = r9.toNanos(r10)     // Catch: java.lang.Throwable -> L96
            long r7 = r7 + r12
            r9 = 0
        L23:
            long r12 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> L96
            long r12 = r7 - r12
            int r14 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r14 > 0) goto L36
            java.util.concurrent.BlockingQueue<java.lang.Long> r14 = io.netty.util.internal.ThreadLocalRandom.seedQueue     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            java.lang.Object r14 = r14.poll()     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            java.lang.Long r14 = (java.lang.Long) r14     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            goto L40
        L36:
            java.util.concurrent.BlockingQueue<java.lang.Long> r14 = io.netty.util.internal.ThreadLocalRandom.seedQueue     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            java.util.concurrent.TimeUnit r15 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            java.lang.Object r14 = r14.poll(r12, r15)     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            java.lang.Long r14 = (java.lang.Long) r14     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
        L40:
            if (r14 == 0) goto L48
            long r10 = r14.longValue()     // Catch: java.lang.InterruptedException -> L5f java.lang.Throwable -> L96
            r0 = r10
            goto L69
        L48:
            int r14 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
            if (r14 > 0) goto L5e
            java.lang.Thread r14 = io.netty.util.internal.ThreadLocalRandom.seedGeneratorThread     // Catch: java.lang.Throwable -> L96
            r14.interrupt()     // Catch: java.lang.Throwable -> L96
            io.netty.util.internal.logging.InternalLogger r14 = io.netty.util.internal.ThreadLocalRandom.logger     // Catch: java.lang.Throwable -> L96
            java.lang.String r15 = "Failed to generate a seed from SecureRandom within {} seconds. Not enough entropy?"
            java.lang.Long r10 = java.lang.Long.valueOf(r10)     // Catch: java.lang.Throwable -> L96
            r14.warn(r15, r10)     // Catch: java.lang.Throwable -> L96
            goto L69
        L5e:
            goto L23
        L5f:
            r10 = move-exception
            r9 = 1
            io.netty.util.internal.logging.InternalLogger r11 = io.netty.util.internal.ThreadLocalRandom.logger     // Catch: java.lang.Throwable -> L96
            java.lang.String r14 = "Failed to generate a seed from SecureRandom due to an InterruptedException."
            r11.warn(r14)     // Catch: java.lang.Throwable -> L96
        L69:
            r10 = 3627065505421648153(0x3255ecdc33bae119, double:3.253008663204319E-66)
            long r0 = r0 ^ r10
            long r10 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> L96
            long r10 = java.lang.Long.reverse(r10)     // Catch: java.lang.Throwable -> L96
            long r0 = r0 ^ r10
            io.netty.util.internal.ThreadLocalRandom.initialSeedUniquifier = r0     // Catch: java.lang.Throwable -> L96
            if (r9 == 0) goto L88
            java.lang.Thread r10 = java.lang.Thread.currentThread()     // Catch: java.lang.Throwable -> L96
            r10.interrupt()     // Catch: java.lang.Throwable -> L96
            java.lang.Thread r10 = io.netty.util.internal.ThreadLocalRandom.seedGeneratorThread     // Catch: java.lang.Throwable -> L96
            r10.interrupt()     // Catch: java.lang.Throwable -> L96
        L88:
            long r10 = io.netty.util.internal.ThreadLocalRandom.seedGeneratorEndTime     // Catch: java.lang.Throwable -> L96
            int r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r2 != 0) goto L94
            long r2 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> L96
            io.netty.util.internal.ThreadLocalRandom.seedGeneratorEndTime = r2     // Catch: java.lang.Throwable -> L96
        L94:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L96
            return r0
        L96:
            r2 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L96
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.util.internal.ThreadLocalRandom.getInitialSeedUniquifier():long");
    }

    private static long newSeed() {
        long current;
        long actualCurrent;
        long next;
        do {
            current = seedUniquifier.get();
            actualCurrent = current != 0 ? current : getInitialSeedUniquifier();
            next = 181783497276652981L * actualCurrent;
        } while (!seedUniquifier.compareAndSet(current, next));
        if (current == 0 && logger.isDebugEnabled()) {
            if (seedGeneratorEndTime != 0) {
                logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", Long.valueOf(actualCurrent), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(seedGeneratorEndTime - seedGeneratorStartTime))));
            } else {
                logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x", Long.valueOf(actualCurrent)));
            }
        }
        return System.nanoTime() ^ next;
    }

    private static long mix64(long z) {
        long z2 = ((z >>> 33) ^ z) * (-49064778989728563L);
        long z3 = ((z2 >>> 33) ^ z2) * (-4265267296055464877L);
        return (z3 >>> 33) ^ z3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ThreadLocalRandom() {
        super(newSeed());
        this.initialized = true;
    }

    public static ThreadLocalRandom current() {
        return InternalThreadLocalMap.get().random();
    }

    @Override // java.util.Random
    public void setSeed(long seed) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
        this.rnd = (multiplier ^ seed) & mask;
    }

    @Override // java.util.Random
    protected int next(int bits) {
        this.rnd = ((this.rnd * multiplier) + addend) & mask;
        return (int) (this.rnd >>> (48 - bits));
    }

    public int nextInt(int least, int bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextInt(bound - least) + least;
    }

    public long nextLong(long n) {
        ObjectUtil.checkPositive(n, RsaJsonWebKey.MODULUS_MEMBER_NAME);
        long offset = 0;
        while (n >= 2147483647L) {
            int bits = next(2);
            long half = n >>> 1;
            long nextn = (bits & 2) == 0 ? half : n - half;
            if ((bits & 1) == 0) {
                offset += n - nextn;
            }
            n = nextn;
        }
        return nextInt((int) n) + offset;
    }

    public long nextLong(long least, long bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextLong(bound - least) + least;
    }

    public double nextDouble(double n) {
        ObjectUtil.checkPositive(n, RsaJsonWebKey.MODULUS_MEMBER_NAME);
        return nextDouble() * n;
    }

    public double nextDouble(double least, double bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return (nextDouble() * (bound - least)) + least;
    }
}
