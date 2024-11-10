package io.netty.handler.ssl.util;

import io.netty.util.internal.PlatformDependent;
import java.security.SecureRandom;
import java.util.Random;

/* loaded from: classes4.dex */
final class ThreadLocalInsecureRandom extends SecureRandom {
    private static final SecureRandom INSTANCE = new ThreadLocalInsecureRandom();
    private static final long serialVersionUID = -8209473337192526191L;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SecureRandom current() {
        return INSTANCE;
    }

    private ThreadLocalInsecureRandom() {
    }

    @Override // java.security.SecureRandom
    public String getAlgorithm() {
        return "insecure";
    }

    @Override // java.security.SecureRandom
    public void setSeed(byte[] seed) {
    }

    @Override // java.security.SecureRandom, java.util.Random
    public void setSeed(long seed) {
    }

    @Override // java.security.SecureRandom, java.util.Random
    public void nextBytes(byte[] bytes) {
        random().nextBytes(bytes);
    }

    @Override // java.security.SecureRandom
    public byte[] generateSeed(int numBytes) {
        byte[] seed = new byte[numBytes];
        random().nextBytes(seed);
        return seed;
    }

    @Override // java.util.Random
    public int nextInt() {
        return random().nextInt();
    }

    @Override // java.util.Random
    public int nextInt(int n) {
        return random().nextInt(n);
    }

    @Override // java.util.Random
    public boolean nextBoolean() {
        return random().nextBoolean();
    }

    @Override // java.util.Random
    public long nextLong() {
        return random().nextLong();
    }

    @Override // java.util.Random
    public float nextFloat() {
        return random().nextFloat();
    }

    @Override // java.util.Random
    public double nextDouble() {
        return random().nextDouble();
    }

    @Override // java.util.Random
    public double nextGaussian() {
        return random().nextGaussian();
    }

    private static Random random() {
        return PlatformDependent.threadLocalRandom();
    }
}
