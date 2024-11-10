package io.netty.channel.kqueue;

import io.netty.channel.DefaultFileRegion;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.NativeInetAddress;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.unix.Socket;
import io.netty.util.internal.ObjectUtil;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class BsdSocket extends Socket {
    private static final int APPLE_SND_LOW_AT_MAX = 131072;
    static final int BSD_SND_LOW_AT_MAX = Math.min(131072, 32768);
    private static final int FREEBSD_SND_LOW_AT_MAX = 32768;
    private static final int UNSPECIFIED_SOURCE_INTERFACE = 0;

    private static native int connectx(int i, int i2, boolean z, byte[] bArr, int i3, int i4, boolean z2, byte[] bArr2, int i5, int i6, int i7, long j, int i8, int i9);

    private static native String[] getAcceptFilter(int i) throws IOException;

    private static native PeerCredentials getPeerCredentials(int i) throws IOException;

    private static native int getSndLowAt(int i) throws IOException;

    private static native int getTcpNoPush(int i) throws IOException;

    private static native int isTcpFastOpen(int i) throws IOException;

    private static native long sendFile(int i, DefaultFileRegion defaultFileRegion, long j, long j2, long j3) throws IOException;

    private static native void setAcceptFilter(int i, String str, String str2) throws IOException;

    private static native void setSndLowAt(int i, int i2) throws IOException;

    private static native void setTcpFastOpen(int i, int i2) throws IOException;

    private static native void setTcpNoPush(int i, int i2) throws IOException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BsdSocket(int fd) {
        super(fd);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAcceptFilter(AcceptFilter acceptFilter) throws IOException {
        setAcceptFilter(intValue(), acceptFilter.filterName(), acceptFilter.filterArgs());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTcpNoPush(boolean z) throws IOException {
        setTcpNoPush(intValue(), z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSndLowAt(int lowAt) throws IOException {
        setSndLowAt(intValue(), lowAt);
    }

    public void setTcpFastOpen(boolean z) throws IOException {
        setTcpFastOpen(intValue(), z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTcpNoPush() throws IOException {
        return getTcpNoPush(intValue()) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSndLowAt() throws IOException {
        return getSndLowAt(intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AcceptFilter getAcceptFilter() throws IOException {
        String[] result = getAcceptFilter(intValue());
        return result == null ? AcceptFilter.PLATFORM_UNSUPPORTED : new AcceptFilter(result[0], result[1]);
    }

    public boolean isTcpFastOpen() throws IOException {
        return isTcpFastOpen(intValue()) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PeerCredentials getPeerCredentials() throws IOException {
        return getPeerCredentials(intValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long sendFile(DefaultFileRegion src, long baseOffset, long offset, long length) throws IOException {
        src.open();
        long res = sendFile(intValue(), src, baseOffset, offset, length);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendfile", (int) res);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int connectx(InetSocketAddress source, InetSocketAddress destination, IovArray data, boolean tcpFastOpen) throws IOException {
        int sourceScopeId;
        byte[] sourceAddress;
        boolean sourceIPv6;
        byte[] sourceAddress2;
        int sourceScopeId2;
        int sourcePort;
        byte[] destinationAddress;
        int destinationScopeId;
        int iovCount;
        long iovAddress;
        int iovDataLength;
        ObjectUtil.checkNotNull(destination, "Destination InetSocketAddress cannot be null.");
        int flags = tcpFastOpen ? Native.CONNECT_TCP_FASTOPEN : 0;
        if (source == null) {
            sourceIPv6 = false;
            sourceAddress2 = null;
            sourceScopeId2 = 0;
            sourcePort = 0;
        } else {
            InetAddress sourceInetAddress = source.getAddress();
            boolean sourceIPv62 = useIpv6(this, sourceInetAddress);
            if (sourceInetAddress instanceof Inet6Address) {
                sourceAddress = sourceInetAddress.getAddress();
                sourceScopeId = ((Inet6Address) sourceInetAddress).getScopeId();
            } else {
                sourceScopeId = 0;
                sourceAddress = NativeInetAddress.ipv4MappedIpv6Address(sourceInetAddress.getAddress());
            }
            sourceIPv6 = sourceIPv62;
            sourceAddress2 = sourceAddress;
            sourceScopeId2 = sourceScopeId;
            sourcePort = source.getPort();
        }
        InetAddress destinationInetAddress = destination.getAddress();
        boolean destinationIPv6 = useIpv6(this, destinationInetAddress);
        if (destinationInetAddress instanceof Inet6Address) {
            byte[] destinationAddress2 = destinationInetAddress.getAddress();
            destinationAddress = destinationAddress2;
            destinationScopeId = ((Inet6Address) destinationInetAddress).getScopeId();
        } else {
            destinationAddress = NativeInetAddress.ipv4MappedIpv6Address(destinationInetAddress.getAddress());
            destinationScopeId = 0;
        }
        int destinationPort = destination.getPort();
        if (data == null || data.count() == 0) {
            iovCount = 0;
            iovAddress = 0;
            iovDataLength = 0;
        } else {
            long iovAddress2 = data.memoryAddress(0);
            iovCount = data.count();
            long size = data.size();
            if (size > 2147483647L) {
                throw new IOException("IovArray.size() too big: " + size + " bytes.");
            }
            iovAddress = iovAddress2;
            iovDataLength = (int) size;
        }
        int iovDataLength2 = iovDataLength;
        int iovDataLength3 = destinationScopeId;
        int result = connectx(intValue(), 0, sourceIPv6, sourceAddress2, sourceScopeId2, sourcePort, destinationIPv6, destinationAddress, iovDataLength3, destinationPort, flags, iovAddress, iovCount, iovDataLength2);
        if (result == Errors.ERRNO_EINPROGRESS_NEGATIVE) {
            return -iovDataLength2;
        }
        if (result < 0) {
            return Errors.ioResult("connectx", result);
        }
        return result;
    }

    public static BsdSocket newSocketStream() {
        return new BsdSocket(newSocketStream0());
    }

    public static BsdSocket newSocketStream(InternetProtocolFamily protocol) {
        return new BsdSocket(newSocketStream0(protocol));
    }

    public static BsdSocket newSocketDgram() {
        return new BsdSocket(newSocketDgram0());
    }

    public static BsdSocket newSocketDgram(InternetProtocolFamily protocol) {
        return new BsdSocket(newSocketDgram0(protocol));
    }

    public static BsdSocket newSocketDomain() {
        return new BsdSocket(newSocketDomain0());
    }

    public static BsdSocket newSocketDomainDgram() {
        return new BsdSocket(newSocketDomainDgram0());
    }
}
