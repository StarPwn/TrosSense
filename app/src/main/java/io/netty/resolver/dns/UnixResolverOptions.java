package io.netty.resolver.dns;

/* loaded from: classes4.dex */
final class UnixResolverOptions {
    private final int attempts;
    private final int ndots;
    private final int timeout;

    UnixResolverOptions(int ndots, int timeout, int attempts) {
        this.ndots = ndots;
        this.timeout = timeout;
        this.attempts = attempts;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Builder newBuilder() {
        return new Builder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int ndots() {
        return this.ndots;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int timeout() {
        return this.timeout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int attempts() {
        return this.attempts;
    }

    public String toString() {
        return getClass().getSimpleName() + "{ndots=" + this.ndots + ", timeout=" + this.timeout + ", attempts=" + this.attempts + '}';
    }

    /* loaded from: classes4.dex */
    static final class Builder {
        private int attempts;
        private int ndots;
        private int timeout;

        private Builder() {
            this.ndots = 1;
            this.timeout = 5;
            this.attempts = 16;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setNdots(int ndots) {
            this.ndots = ndots;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public UnixResolverOptions build() {
            return new UnixResolverOptions(this.ndots, this.timeout, this.attempts);
        }
    }
}
