package io.netty.channel.unix;

import io.netty.channel.ChannelException;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

/* loaded from: classes4.dex */
public class Socket extends FileDescriptor {

    @Deprecated
    public static final int UDS_SUN_PATH_SIZE = 100;
    private static volatile boolean isIpv6Preferred;
    protected final boolean ipv6;

    private static native int accept(int i, byte[] bArr);

    private static native int bind(int i, boolean z, byte[] bArr, int i2, int i3);

    private static native int bindDomainSocket(int i, byte[] bArr);

    private static native int connect(int i, boolean z, byte[] bArr, int i2, int i3);

    private static native int connectDomainSocket(int i, byte[] bArr);

    private static native int disconnect(int i, boolean z);

    private static native int finishConnect(int i);

    private static native int getIntOpt(int i, int i2, int i3) throws IOException;

    private static native void getRawOptAddress(int i, int i2, int i3, long j, int i4) throws IOException;

    private static native void getRawOptArray(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws IOException;

    private static native int getReceiveBufferSize(int i) throws IOException;

    private static native int getSendBufferSize(int i) throws IOException;

    private static native int getSoError(int i) throws IOException;

    private static native int getSoLinger(int i) throws IOException;

    private static native int getTrafficClass(int i, boolean z) throws IOException;

    private static native int isBroadcast(int i) throws IOException;

    private static native boolean isIPv6(int i);

    private static native boolean isIPv6Preferred0(boolean z);

    private static native int isKeepAlive(int i) throws IOException;

    private static native int isReuseAddress(int i) throws IOException;

    private static native int isReusePort(int i) throws IOException;

    private static native int isTcpNoDelay(int i) throws IOException;

    private static native int listen(int i, int i2);

    private static native byte[] localAddress(int i);

    private static native byte[] localDomainSocketAddress(int i);

    private static native int msgFastopen();

    private static native int newSocketDgramFd(boolean z);

    private static native int newSocketDomainDgramFd();

    private static native int newSocketDomainFd();

    private static native int newSocketStreamFd(boolean z);

    private static native int recv(int i, ByteBuffer byteBuffer, int i2, int i3);

    private static native int recvAddress(int i, long j, int i2, int i3);

    private static native int recvFd(int i);

    private static native DatagramSocketAddress recvFrom(int i, ByteBuffer byteBuffer, int i2, int i3) throws IOException;

    private static native DatagramSocketAddress recvFromAddress(int i, long j, int i2, int i3) throws IOException;

    private static native DomainDatagramSocketAddress recvFromAddressDomainSocket(int i, long j, int i2, int i3) throws IOException;

    private static native DomainDatagramSocketAddress recvFromDomainSocket(int i, ByteBuffer byteBuffer, int i2, int i3) throws IOException;

    private static native byte[] remoteAddress(int i);

    private static native byte[] remoteDomainSocketAddress(int i);

    private static native int send(int i, ByteBuffer byteBuffer, int i2, int i3);

    private static native int sendAddress(int i, long j, int i2, int i3);

    private static native int sendFd(int i, int i2);

    private static native int sendTo(int i, boolean z, ByteBuffer byteBuffer, int i2, int i3, byte[] bArr, int i4, int i5, int i6);

    private static native int sendToAddress(int i, boolean z, long j, int i2, int i3, byte[] bArr, int i4, int i5, int i6);

    private static native int sendToAddressDomainSocket(int i, long j, int i2, int i3, byte[] bArr);

    private static native int sendToAddresses(int i, boolean z, long j, int i2, byte[] bArr, int i3, int i4, int i5);

    private static native int sendToAddressesDomainSocket(int i, long j, int i2, byte[] bArr);

    private static native int sendToDomainSocket(int i, ByteBuffer byteBuffer, int i2, int i3, byte[] bArr);

    private static native void setBroadcast(int i, int i2) throws IOException;

    private static native void setIntOpt(int i, int i2, int i3, int i4) throws IOException;

    private static native void setKeepAlive(int i, int i2) throws IOException;

    private static native void setRawOptAddress(int i, int i2, int i3, long j, int i4) throws IOException;

    private static native void setRawOptArray(int i, int i2, int i3, byte[] bArr, int i4, int i5) throws IOException;

    private static native void setReceiveBufferSize(int i, int i2) throws IOException;

    private static native void setReuseAddress(int i, int i2) throws IOException;

    private static native void setReusePort(int i, int i2) throws IOException;

    private static native void setSendBufferSize(int i, int i2) throws IOException;

    private static native void setSoLinger(int i, int i2) throws IOException;

    private static native void setTcpNoDelay(int i, int i2) throws IOException;

    private static native void setTrafficClass(int i, boolean z, int i2) throws IOException;

    private static native int shutdown(int i, boolean z, boolean z2);

    public Socket(int fd) {
        super(fd);
        this.ipv6 = isIPv6(fd);
    }

    private boolean useIpv6(InetAddress address) {
        return useIpv6(this, address);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean useIpv6(Socket socket, InetAddress address) {
        return socket.ipv6 || (address instanceof Inet6Address);
    }

    public final void shutdown() throws IOException {
        shutdown(true, true);
    }

    public final void shutdown(boolean read, boolean write) throws IOException {
        int oldState;
        int newState;
        do {
            oldState = this.state;
            if (isClosed(oldState)) {
                throw new ClosedChannelException();
            }
            newState = oldState;
            if (read && !isInputShutdown(newState)) {
                newState = inputShutdown(newState);
            }
            if (write && !isOutputShutdown(newState)) {
                newState = outputShutdown(newState);
            }
            if (newState == oldState) {
                return;
            }
        } while (!casState(oldState, newState));
        int res = shutdown(this.fd, read, write);
        if (res < 0) {
            Errors.ioResult("shutdown", res);
        }
    }

    public final boolean isShutdown() {
        int state = this.state;
        return isInputShutdown(state) && isOutputShutdown(state);
    }

    public final boolean isInputShutdown() {
        return isInputShutdown(this.state);
    }

    public final boolean isOutputShutdown() {
        return isOutputShutdown(this.state);
    }

    public final int sendTo(ByteBuffer buf, int pos, int limit, InetAddress addr, int port) throws IOException {
        return sendTo(buf, pos, limit, addr, port, false);
    }

    public final int sendTo(ByteBuffer buf, int pos, int limit, InetAddress addr, int port, boolean fastOpen) throws IOException {
        int scopeId;
        byte[] address;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address) addr).getScopeId();
        } else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        int flags = fastOpen ? msgFastopen() : 0;
        int res = sendTo(this.fd, useIpv6(addr), buf, pos, limit, address, scopeId, port, flags);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE && fastOpen) {
            return 0;
        }
        if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            throw new PortUnreachableException("sendTo failed");
        }
        return Errors.ioResult("sendTo", res);
    }

    public final int sendToDomainSocket(ByteBuffer buf, int pos, int limit, byte[] path) throws IOException {
        int res = sendToDomainSocket(this.fd, buf, pos, limit, path);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendToDomainSocket", res);
    }

    public final int sendToAddress(long memoryAddress, int pos, int limit, InetAddress addr, int port) throws IOException {
        return sendToAddress(memoryAddress, pos, limit, addr, port, false);
    }

    public final int sendToAddress(long memoryAddress, int pos, int limit, InetAddress addr, int port, boolean fastOpen) throws IOException {
        int scopeId;
        byte[] address;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address) addr).getScopeId();
        } else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        int flags = fastOpen ? msgFastopen() : 0;
        int res = sendToAddress(this.fd, useIpv6(addr), memoryAddress, pos, limit, address, scopeId, port, flags);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE && fastOpen) {
            return 0;
        }
        if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            throw new PortUnreachableException("sendToAddress failed");
        }
        return Errors.ioResult("sendToAddress", res);
    }

    public final int sendToAddressDomainSocket(long memoryAddress, int pos, int limit, byte[] path) throws IOException {
        int res = sendToAddressDomainSocket(this.fd, memoryAddress, pos, limit, path);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendToAddressDomainSocket", res);
    }

    public final int sendToAddresses(long memoryAddress, int length, InetAddress addr, int port) throws IOException {
        return sendToAddresses(memoryAddress, length, addr, port, false);
    }

    public final int sendToAddresses(long memoryAddress, int length, InetAddress addr, int port, boolean fastOpen) throws IOException {
        int scopeId;
        byte[] address;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address) addr).getScopeId();
        } else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        int flags = fastOpen ? msgFastopen() : 0;
        int res = sendToAddresses(this.fd, useIpv6(addr), memoryAddress, length, address, scopeId, port, flags);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE && fastOpen) {
            return 0;
        }
        if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            throw new PortUnreachableException("sendToAddresses failed");
        }
        return Errors.ioResult("sendToAddresses", res);
    }

    public final int sendToAddressesDomainSocket(long memoryAddress, int length, byte[] path) throws IOException {
        int res = sendToAddressesDomainSocket(this.fd, memoryAddress, length, path);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendToAddressesDomainSocket", res);
    }

    public final DatagramSocketAddress recvFrom(ByteBuffer buf, int pos, int limit) throws IOException {
        return recvFrom(this.fd, buf, pos, limit);
    }

    public final DatagramSocketAddress recvFromAddress(long memoryAddress, int pos, int limit) throws IOException {
        return recvFromAddress(this.fd, memoryAddress, pos, limit);
    }

    public final DomainDatagramSocketAddress recvFromDomainSocket(ByteBuffer buf, int pos, int limit) throws IOException {
        return recvFromDomainSocket(this.fd, buf, pos, limit);
    }

    public final DomainDatagramSocketAddress recvFromAddressDomainSocket(long memoryAddress, int pos, int limit) throws IOException {
        return recvFromAddressDomainSocket(this.fd, memoryAddress, pos, limit);
    }

    public int recv(ByteBuffer buf, int pos, int limit) throws IOException {
        int res = recv(intValue(), buf, pos, limit);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        return Errors.ioResult("recv", res);
    }

    public int recvAddress(long address, int pos, int limit) throws IOException {
        int res = recvAddress(intValue(), address, pos, limit);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        return Errors.ioResult("recvAddress", res);
    }

    public int send(ByteBuffer buf, int pos, int limit) throws IOException {
        int res = send(intValue(), buf, pos, limit);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("send", res);
    }

    public int sendAddress(long address, int pos, int limit) throws IOException {
        int res = sendAddress(intValue(), address, pos, limit);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendAddress", res);
    }

    public final int recvFd() throws IOException {
        int res = recvFd(this.fd);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return 0;
        }
        throw Errors.newIOException("recvFd", res);
    }

    public final int sendFd(int fdToSend) throws IOException {
        int res = sendFd(this.fd, fdToSend);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return -1;
        }
        throw Errors.newIOException("sendFd", res);
    }

    public final boolean connect(SocketAddress socketAddress) throws IOException {
        int res;
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            InetAddress inetAddress = inetSocketAddress.getAddress();
            NativeInetAddress address = NativeInetAddress.newInstance(inetAddress);
            res = connect(this.fd, useIpv6(inetAddress), address.address, address.scopeId, inetSocketAddress.getPort());
        } else if (socketAddress instanceof DomainSocketAddress) {
            DomainSocketAddress unixDomainSocketAddress = (DomainSocketAddress) socketAddress;
            res = connectDomainSocket(this.fd, unixDomainSocketAddress.path().getBytes(CharsetUtil.UTF_8));
        } else {
            throw new Error("Unexpected SocketAddress implementation " + socketAddress);
        }
        if (res < 0) {
            return Errors.handleConnectErrno("connect", res);
        }
        return true;
    }

    public final boolean finishConnect() throws IOException {
        int res = finishConnect(this.fd);
        if (res < 0) {
            return Errors.handleConnectErrno("finishConnect", res);
        }
        return true;
    }

    public final void disconnect() throws IOException {
        int res = disconnect(this.fd, this.ipv6);
        if (res < 0) {
            Errors.handleConnectErrno("disconnect", res);
        }
    }

    public final void bind(SocketAddress socketAddress) throws IOException {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) socketAddress;
            InetAddress inetAddress = addr.getAddress();
            NativeInetAddress address = NativeInetAddress.newInstance(inetAddress);
            int res = bind(this.fd, useIpv6(inetAddress), address.address, address.scopeId, addr.getPort());
            if (res < 0) {
                throw Errors.newIOException("bind", res);
            }
            return;
        }
        if (socketAddress instanceof DomainSocketAddress) {
            int res2 = bindDomainSocket(this.fd, ((DomainSocketAddress) socketAddress).path().getBytes(CharsetUtil.UTF_8));
            if (res2 < 0) {
                throw Errors.newIOException("bind", res2);
            }
            return;
        }
        throw new Error("Unexpected SocketAddress implementation " + socketAddress);
    }

    public final void listen(int backlog) throws IOException {
        int res = listen(this.fd, backlog);
        if (res < 0) {
            throw Errors.newIOException("listen", res);
        }
    }

    public final int accept(byte[] addr) throws IOException {
        int res = accept(this.fd, addr);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return -1;
        }
        throw Errors.newIOException("accept", res);
    }

    public final InetSocketAddress remoteAddress() {
        byte[] addr = remoteAddress(this.fd);
        if (addr == null) {
            return null;
        }
        return NativeInetAddress.address(addr, 0, addr.length);
    }

    public final DomainSocketAddress remoteDomainSocketAddress() {
        byte[] addr = remoteDomainSocketAddress(this.fd);
        if (addr == null) {
            return null;
        }
        return new DomainSocketAddress(new String(addr));
    }

    public final InetSocketAddress localAddress() {
        byte[] addr = localAddress(this.fd);
        if (addr == null) {
            return null;
        }
        return NativeInetAddress.address(addr, 0, addr.length);
    }

    public final DomainSocketAddress localDomainSocketAddress() {
        byte[] addr = localDomainSocketAddress(this.fd);
        if (addr == null) {
            return null;
        }
        return new DomainSocketAddress(new String(addr));
    }

    public final int getReceiveBufferSize() throws IOException {
        return getReceiveBufferSize(this.fd);
    }

    public final int getSendBufferSize() throws IOException {
        return getSendBufferSize(this.fd);
    }

    public final boolean isKeepAlive() throws IOException {
        return isKeepAlive(this.fd) != 0;
    }

    public final boolean isTcpNoDelay() throws IOException {
        return isTcpNoDelay(this.fd) != 0;
    }

    public final boolean isReuseAddress() throws IOException {
        return isReuseAddress(this.fd) != 0;
    }

    public final boolean isReusePort() throws IOException {
        return isReusePort(this.fd) != 0;
    }

    public final boolean isBroadcast() throws IOException {
        return isBroadcast(this.fd) != 0;
    }

    public final int getSoLinger() throws IOException {
        return getSoLinger(this.fd);
    }

    public final int getSoError() throws IOException {
        return getSoError(this.fd);
    }

    public final int getTrafficClass() throws IOException {
        return getTrafficClass(this.fd, this.ipv6);
    }

    public final void setKeepAlive(boolean z) throws IOException {
        setKeepAlive(this.fd, z ? 1 : 0);
    }

    public final void setReceiveBufferSize(int receiveBufferSize) throws IOException {
        setReceiveBufferSize(this.fd, receiveBufferSize);
    }

    public final void setSendBufferSize(int sendBufferSize) throws IOException {
        setSendBufferSize(this.fd, sendBufferSize);
    }

    public final void setTcpNoDelay(boolean z) throws IOException {
        setTcpNoDelay(this.fd, z ? 1 : 0);
    }

    public final void setSoLinger(int soLinger) throws IOException {
        setSoLinger(this.fd, soLinger);
    }

    public final void setReuseAddress(boolean z) throws IOException {
        setReuseAddress(this.fd, z ? 1 : 0);
    }

    public final void setReusePort(boolean z) throws IOException {
        setReusePort(this.fd, z ? 1 : 0);
    }

    public final void setBroadcast(boolean z) throws IOException {
        setBroadcast(this.fd, z ? 1 : 0);
    }

    public final void setTrafficClass(int trafficClass) throws IOException {
        setTrafficClass(this.fd, this.ipv6, trafficClass);
    }

    public void setIntOpt(int level, int optname, int optvalue) throws IOException {
        setIntOpt(this.fd, level, optname, optvalue);
    }

    public void setRawOpt(int level, int optname, ByteBuffer optvalue) throws IOException {
        int limit = optvalue.limit();
        if (optvalue.isDirect()) {
            setRawOptAddress(this.fd, level, optname, optvalue.position() + Buffer.memoryAddress(optvalue), optvalue.remaining());
        } else if (optvalue.hasArray()) {
            setRawOptArray(this.fd, level, optname, optvalue.array(), optvalue.arrayOffset() + optvalue.position(), optvalue.remaining());
        } else {
            byte[] bytes = new byte[optvalue.remaining()];
            optvalue.duplicate().get(bytes);
            setRawOptArray(this.fd, level, optname, bytes, 0, bytes.length);
        }
        optvalue.position(limit);
    }

    public int getIntOpt(int level, int optname) throws IOException {
        return getIntOpt(this.fd, level, optname);
    }

    public void getRawOpt(int level, int optname, ByteBuffer out) throws IOException {
        if (out.isDirect()) {
            getRawOptAddress(this.fd, level, optname, out.position() + Buffer.memoryAddress(out), out.remaining());
        } else if (out.hasArray()) {
            getRawOptArray(this.fd, level, optname, out.array(), out.position() + out.arrayOffset(), out.remaining());
        } else {
            byte[] outArray = new byte[out.remaining()];
            getRawOptArray(this.fd, level, optname, outArray, 0, outArray.length);
            out.put(outArray);
        }
        out.position(out.limit());
    }

    public static boolean isIPv6Preferred() {
        return isIpv6Preferred;
    }

    public static boolean shouldUseIpv6(InternetProtocolFamily family) {
        return family == null ? isIPv6Preferred() : family == InternetProtocolFamily.IPv6;
    }

    @Override // io.netty.channel.unix.FileDescriptor
    public String toString() {
        return "Socket{fd=" + this.fd + '}';
    }

    public static Socket newSocketStream() {
        return new Socket(newSocketStream0());
    }

    public static Socket newSocketDgram() {
        return new Socket(newSocketDgram0());
    }

    public static Socket newSocketDomain() {
        return new Socket(newSocketDomain0());
    }

    public static Socket newSocketDomainDgram() {
        return new Socket(newSocketDomainDgram0());
    }

    public static void initialize() {
        isIpv6Preferred = isIPv6Preferred0(NetUtil.isIpV4StackPreferred());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketStream0() {
        return newSocketStream0(isIPv6Preferred());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketStream0(InternetProtocolFamily protocol) {
        return newSocketStream0(shouldUseIpv6(protocol));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketStream0(boolean ipv6) {
        int res = newSocketStreamFd(ipv6);
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketStream", res));
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketDgram0() {
        return newSocketDgram0(isIPv6Preferred());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketDgram0(InternetProtocolFamily family) {
        return newSocketDgram0(shouldUseIpv6(family));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketDgram0(boolean ipv6) {
        int res = newSocketDgramFd(ipv6);
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketDgram", res));
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketDomain0() {
        int res = newSocketDomainFd();
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketDomain", res));
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int newSocketDomainDgram0() {
        int res = newSocketDomainDgramFd();
        if (res < 0) {
            throw new ChannelException(Errors.newIOException("newSocketDomainDgram", res));
        }
        return res;
    }
}
