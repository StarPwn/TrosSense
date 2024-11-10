package io.netty.util;

/* loaded from: classes4.dex */
public final class Signal extends Error implements Constant<Signal> {
    private static final ConstantPool<Signal> pool = new ConstantPool<Signal>() { // from class: io.netty.util.Signal.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.ConstantPool
        public Signal newConstant(int id, String name) {
            return new Signal(id, name);
        }
    };
    private static final long serialVersionUID = -221145131122459977L;
    private final SignalConstant constant;

    public static Signal valueOf(String name) {
        return pool.valueOf(name);
    }

    public static Signal valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return pool.valueOf(firstNameComponent, secondNameComponent);
    }

    private Signal(int id, String name) {
        this.constant = new SignalConstant(id, name);
    }

    public void expect(Signal signal) {
        if (this != signal) {
            throw new IllegalStateException("unexpected signal: " + signal);
        }
    }

    @Override // java.lang.Throwable
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override // io.netty.util.Constant
    public int id() {
        return this.constant.id();
    }

    @Override // io.netty.util.Constant
    public String name() {
        return this.constant.name();
    }

    public boolean equals(Object obj) {
        return this == obj;
    }

    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override // java.lang.Comparable
    public int compareTo(Signal other) {
        if (this == other) {
            return 0;
        }
        return this.constant.compareTo(other.constant);
    }

    @Override // java.lang.Throwable
    public String toString() {
        return name();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class SignalConstant extends AbstractConstant<SignalConstant> {
        SignalConstant(int id, String name) {
            super(id, name);
        }
    }
}
