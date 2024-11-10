package io.netty.channel.epoll;

import java.net.SocketAddress;

/* loaded from: classes4.dex */
public final class VSockAddress extends SocketAddress {
    public static final int VMADDR_CID_ANY = -1;
    public static final int VMADDR_CID_HOST = 2;
    public static final int VMADDR_CID_HYPERVISOR = 0;
    public static final int VMADDR_CID_LOCAL = 1;
    public static final int VMADDR_PORT_ANY = -1;
    private static final long serialVersionUID = 8600894096347158429L;
    private final int cid;
    private final int port;

    public VSockAddress(int cid, int port) {
        this.cid = cid;
        this.port = port;
    }

    public int getCid() {
        return this.cid;
    }

    public int getPort() {
        return this.port;
    }

    public String toString() {
        return "VSockAddress{cid=" + this.cid + ", port=" + this.port + '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VSockAddress)) {
            return false;
        }
        VSockAddress that = (VSockAddress) o;
        return this.cid == that.cid && this.port == that.port;
    }

    public int hashCode() {
        int result = this.cid;
        return (result * 31) + this.port;
    }
}
