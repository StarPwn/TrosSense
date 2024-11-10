package io.netty.handler.codec.haproxy;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.haproxy.HAProxyProxiedProtocol;
import io.netty.handler.codec.haproxy.HAProxyTLV;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public final class HAProxyMessage extends AbstractReferenceCounted {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int MAX_NESTING_LEVEL = 128;
    private static final ResourceLeakDetector<HAProxyMessage> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(HAProxyMessage.class);
    private final HAProxyCommand command;
    private final String destinationAddress;
    private final int destinationPort;
    private final ResourceLeakTracker<HAProxyMessage> leak;
    private final HAProxyProtocolVersion protocolVersion;
    private final HAProxyProxiedProtocol proxiedProtocol;
    private final String sourceAddress;
    private final int sourcePort;
    private final List<HAProxyTLV> tlvs;

    private HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, String sourcePort, String destinationPort) {
        this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, portStringToInt(sourcePort), portStringToInt(destinationPort));
    }

    public HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, int sourcePort, int destinationPort) {
        this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, sourcePort, destinationPort, Collections.emptyList());
    }

    public HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, int sourcePort, int destinationPort, List<? extends HAProxyTLV> tlvs) {
        ObjectUtil.checkNotNull(protocolVersion, "protocolVersion");
        ObjectUtil.checkNotNull(proxiedProtocol, "proxiedProtocol");
        ObjectUtil.checkNotNull(tlvs, "tlvs");
        HAProxyProxiedProtocol.AddressFamily addrFamily = proxiedProtocol.addressFamily();
        checkAddress(sourceAddress, addrFamily);
        checkAddress(destinationAddress, addrFamily);
        checkPort(sourcePort, addrFamily);
        checkPort(destinationPort, addrFamily);
        this.protocolVersion = protocolVersion;
        this.command = command;
        this.proxiedProtocol = proxiedProtocol;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.tlvs = Collections.unmodifiableList(tlvs);
        this.leak = leakDetector.track(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HAProxyMessage decodeHeader(ByteBuf header) {
        int addressLen;
        int startIdx;
        int dstPort;
        String dstAddress;
        String srcAddress;
        int addressLen2;
        int addressLen3;
        ObjectUtil.checkNotNull(header, "header");
        if (header.readableBytes() >= 16) {
            header.skipBytes(12);
            byte verCmdByte = header.readByte();
            try {
                HAProxyProtocolVersion ver = HAProxyProtocolVersion.valueOf(verCmdByte);
                if (ver != HAProxyProtocolVersion.V2) {
                    throw new HAProxyProtocolException("version 1 unsupported: 0x" + Integer.toHexString(verCmdByte));
                }
                try {
                    HAProxyCommand cmd = HAProxyCommand.valueOf(verCmdByte);
                    if (cmd == HAProxyCommand.LOCAL) {
                        return unknownMsg(HAProxyProtocolVersion.V2, HAProxyCommand.LOCAL);
                    }
                    try {
                        HAProxyProxiedProtocol protAndFam = HAProxyProxiedProtocol.valueOf(header.readByte());
                        if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
                            return unknownMsg(HAProxyProtocolVersion.V2, HAProxyCommand.PROXY);
                        }
                        int addressInfoLen = header.readUnsignedShort();
                        HAProxyProxiedProtocol.AddressFamily addressFamily = protAndFam.addressFamily();
                        if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_UNIX) {
                            if (addressInfoLen < 216 || header.readableBytes() < 216) {
                                throw new HAProxyProtocolException("incomplete UNIX socket address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 216+ bytes)");
                            }
                            int startIdx2 = header.readerIndex();
                            int addressEnd = header.indexOf(startIdx2, startIdx2 + 108, (byte) 0);
                            if (addressEnd == -1) {
                                addressLen2 = 108;
                            } else {
                                addressLen2 = addressEnd - startIdx2;
                            }
                            String srcAddress2 = header.toString(startIdx2, addressLen2, CharsetUtil.US_ASCII);
                            int startIdx3 = startIdx2 + 108;
                            int addressEnd2 = header.indexOf(startIdx3, startIdx3 + 108, (byte) 0);
                            if (addressEnd2 == -1) {
                                addressLen3 = 108;
                            } else {
                                addressLen3 = addressEnd2 - startIdx3;
                            }
                            String dstAddress2 = header.toString(startIdx3, addressLen3, CharsetUtil.US_ASCII);
                            header.readerIndex(startIdx3 + 108);
                            startIdx = 0;
                            dstPort = 0;
                            dstAddress = dstAddress2;
                            srcAddress = srcAddress2;
                        } else {
                            if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv4) {
                                if (addressInfoLen < 12 || header.readableBytes() < 12) {
                                    throw new HAProxyProtocolException("incomplete IPv4 address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 12+ bytes)");
                                }
                                addressLen = 4;
                            } else if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv6) {
                                if (addressInfoLen < 36 || header.readableBytes() < 36) {
                                    throw new HAProxyProtocolException("incomplete IPv6 address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 36+ bytes)");
                                }
                                addressLen = 16;
                            } else {
                                throw new HAProxyProtocolException("unable to parse address information (unknown address family: " + addressFamily + ')');
                            }
                            String srcAddress3 = ipBytesToString(header, addressLen);
                            String dstAddress3 = ipBytesToString(header, addressLen);
                            int srcPort = header.readUnsignedShort();
                            int dstPort2 = header.readUnsignedShort();
                            startIdx = srcPort;
                            dstPort = dstPort2;
                            dstAddress = dstAddress3;
                            srcAddress = srcAddress3;
                        }
                        List<HAProxyTLV> tlvs = readTlvs(header);
                        return new HAProxyMessage(ver, cmd, protAndFam, srcAddress, dstAddress, startIdx, dstPort, tlvs);
                    } catch (IllegalArgumentException e) {
                        throw new HAProxyProtocolException(e);
                    }
                } catch (IllegalArgumentException e2) {
                    throw new HAProxyProtocolException(e2);
                }
            } catch (IllegalArgumentException e3) {
                throw new HAProxyProtocolException(e3);
            }
        }
        throw new HAProxyProtocolException("incomplete header: " + header.readableBytes() + " bytes (expected: 16+ bytes)");
    }

    private static List<HAProxyTLV> readTlvs(ByteBuf header) {
        HAProxyTLV readNextTLV;
        HAProxyTLV haProxyTLV = readNextTLV(header, 0);
        if (haProxyTLV == null) {
            return Collections.emptyList();
        }
        List<HAProxyTLV> haProxyTLVs = new ArrayList<>(4);
        do {
            haProxyTLVs.add(haProxyTLV);
            if (haProxyTLV instanceof HAProxySSLTLV) {
                haProxyTLVs.addAll(((HAProxySSLTLV) haProxyTLV).encapsulatedTLVs());
            }
            readNextTLV = readNextTLV(header, 0);
            haProxyTLV = readNextTLV;
        } while (readNextTLV != null);
        return haProxyTLVs;
    }

    private static HAProxyTLV readNextTLV(ByteBuf header, int nestingLevel) {
        if (nestingLevel > 128) {
            throw new HAProxyProtocolException("Maximum TLV nesting level reached: " + nestingLevel + " (expected: < 128)");
        }
        if (header.readableBytes() < 4) {
            return null;
        }
        byte typeAsByte = header.readByte();
        HAProxyTLV.Type type = HAProxyTLV.Type.typeForByteValue(typeAsByte);
        int length = header.readUnsignedShort();
        switch (type) {
            case PP2_TYPE_SSL:
                ByteBuf rawContent = header.retainedSlice(header.readerIndex(), length);
                ByteBuf byteBuf = header.readSlice(length);
                byte client = byteBuf.readByte();
                int verify = byteBuf.readInt();
                if (byteBuf.readableBytes() >= 4) {
                    List<HAProxyTLV> encapsulatedTlvs = new ArrayList<>(4);
                    do {
                        HAProxyTLV haProxyTLV = readNextTLV(byteBuf, nestingLevel + 1);
                        if (haProxyTLV != null) {
                            encapsulatedTlvs.add(haProxyTLV);
                        }
                        return new HAProxySSLTLV(verify, client, encapsulatedTlvs, rawContent);
                    } while (byteBuf.readableBytes() >= 4);
                    return new HAProxySSLTLV(verify, client, encapsulatedTlvs, rawContent);
                }
                return new HAProxySSLTLV(verify, client, Collections.emptyList(), rawContent);
            case PP2_TYPE_ALPN:
            case PP2_TYPE_AUTHORITY:
            case PP2_TYPE_SSL_VERSION:
            case PP2_TYPE_SSL_CN:
            case PP2_TYPE_NETNS:
            case OTHER:
                return new HAProxyTLV(type, typeAsByte, header.readRetainedSlice(length));
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HAProxyMessage decodeHeader(String header) {
        if (header == null) {
            throw new HAProxyProtocolException("header");
        }
        String[] parts = header.split(" ");
        int numParts = parts.length;
        if (numParts < 2) {
            throw new HAProxyProtocolException("invalid header: " + header + " (expected: 'PROXY' and proxied protocol values)");
        }
        if (!"PROXY".equals(parts[0])) {
            throw new HAProxyProtocolException("unknown identifier: " + parts[0]);
        }
        try {
            HAProxyProxiedProtocol protAndFam = HAProxyProxiedProtocol.valueOf(parts[1]);
            if (protAndFam != HAProxyProxiedProtocol.TCP4 && protAndFam != HAProxyProxiedProtocol.TCP6 && protAndFam != HAProxyProxiedProtocol.UNKNOWN) {
                throw new HAProxyProtocolException("unsupported v1 proxied protocol: " + parts[1]);
            }
            if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
                return unknownMsg(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY);
            }
            if (numParts != 6) {
                throw new HAProxyProtocolException("invalid TCP4/6 header: " + header + " (expected: 6 parts)");
            }
            try {
                return new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, protAndFam, parts[2], parts[3], parts[4], parts[5]);
            } catch (RuntimeException e) {
                throw new HAProxyProtocolException("invalid HAProxy message", e);
            }
        } catch (IllegalArgumentException e2) {
            throw new HAProxyProtocolException(e2);
        }
    }

    private static HAProxyMessage unknownMsg(HAProxyProtocolVersion version, HAProxyCommand command) {
        return new HAProxyMessage(version, command, HAProxyProxiedProtocol.UNKNOWN, (String) null, (String) null, 0, 0);
    }

    private static String ipBytesToString(ByteBuf header, int addressLen) {
        StringBuilder sb = new StringBuilder();
        if (addressLen == 4) {
            for (int i = 0; i < 4; i++) {
                sb.append(header.readByte() & 255);
                sb.append('.');
            }
        } else {
            for (int i2 = 0; i2 < 8; i2++) {
                sb.append(Integer.toHexString(header.readUnsignedShort()));
                sb.append(':');
            }
        }
        int i3 = sb.length();
        sb.setLength(i3 - 1);
        return sb.toString();
    }

    private static int portStringToInt(String value) {
        try {
            int port = Integer.parseInt(value);
            if (port <= 0 || port > 65535) {
                throw new IllegalArgumentException("invalid port: " + value + " (expected: 1 ~ 65535)");
            }
            return port;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid port: " + value, e);
        }
    }

    private static void checkAddress(String address, HAProxyProxiedProtocol.AddressFamily addrFamily) {
        ObjectUtil.checkNotNull(addrFamily, "addrFamily");
        switch (addrFamily) {
            case AF_UNSPEC:
                if (address != null) {
                    throw new IllegalArgumentException("unable to validate an AF_UNSPEC address: " + address);
                }
                return;
            case AF_UNIX:
                ObjectUtil.checkNotNull(address, "address");
                if (address.getBytes(CharsetUtil.US_ASCII).length > 108) {
                    throw new IllegalArgumentException("invalid AF_UNIX address: " + address);
                }
                return;
            default:
                ObjectUtil.checkNotNull(address, "address");
                switch (addrFamily) {
                    case AF_IPv4:
                        if (!NetUtil.isValidIpV4Address(address)) {
                            throw new IllegalArgumentException("invalid IPv4 address: " + address);
                        }
                        return;
                    case AF_IPv6:
                        if (!NetUtil.isValidIpV6Address(address)) {
                            throw new IllegalArgumentException("invalid IPv6 address: " + address);
                        }
                        return;
                    default:
                        throw new IllegalArgumentException("unexpected addrFamily: " + addrFamily);
                }
        }
    }

    private static void checkPort(int port, HAProxyProxiedProtocol.AddressFamily addrFamily) {
        switch (addrFamily) {
            case AF_UNSPEC:
            case AF_UNIX:
                if (port != 0) {
                    throw new IllegalArgumentException("port cannot be specified with addrFamily: " + addrFamily);
                }
                return;
            case AF_IPv4:
            case AF_IPv6:
                if (port < 0 || port > 65535) {
                    throw new IllegalArgumentException("invalid port: " + port + " (expected: 0 ~ 65535)");
                }
                return;
            default:
                throw new IllegalArgumentException("unexpected addrFamily: " + addrFamily);
        }
    }

    public HAProxyProtocolVersion protocolVersion() {
        return this.protocolVersion;
    }

    public HAProxyCommand command() {
        return this.command;
    }

    public HAProxyProxiedProtocol proxiedProtocol() {
        return this.proxiedProtocol;
    }

    public String sourceAddress() {
        return this.sourceAddress;
    }

    public String destinationAddress() {
        return this.destinationAddress;
    }

    public int sourcePort() {
        return this.sourcePort;
    }

    public int destinationPort() {
        return this.destinationPort;
    }

    public List<HAProxyTLV> tlvs() {
        return this.tlvs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int tlvNumBytes() {
        int tlvNumBytes = 0;
        for (int i = 0; i < this.tlvs.size(); i++) {
            tlvNumBytes += this.tlvs.get(i).totalNumBytes();
        }
        return tlvNumBytes;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HAProxyMessage touch() {
        tryRecord();
        return (HAProxyMessage) super.touch();
    }

    @Override // io.netty.util.ReferenceCounted
    public HAProxyMessage touch(Object hint) {
        if (this.leak != null) {
            this.leak.record(hint);
        }
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HAProxyMessage retain() {
        tryRecord();
        return (HAProxyMessage) super.retain();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public HAProxyMessage retain(int increment) {
        tryRecord();
        return (HAProxyMessage) super.retain(increment);
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public boolean release() {
        tryRecord();
        return super.release();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public boolean release(int decrement) {
        tryRecord();
        return super.release(decrement);
    }

    private void tryRecord() {
        if (this.leak != null) {
            this.leak.record();
        }
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        try {
            for (HAProxyTLV tlv : this.tlvs) {
                tlv.release();
            }
            ResourceLeakTracker<HAProxyMessage> leak = this.leak;
            if (leak != null) {
                boolean closed = leak.close(this);
                if (!closed) {
                    throw new AssertionError();
                }
            }
        } catch (Throwable th) {
            ResourceLeakTracker<HAProxyMessage> leak2 = this.leak;
            if (leak2 != null) {
                boolean closed2 = leak2.close(this);
                if (!closed2) {
                    throw new AssertionError();
                }
            }
            throw th;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(256).append(StringUtil.simpleClassName(this)).append("(protocolVersion: ").append(this.protocolVersion).append(", command: ").append(this.command).append(", proxiedProtocol: ").append(this.proxiedProtocol).append(", sourceAddress: ").append(this.sourceAddress).append(", destinationAddress: ").append(this.destinationAddress).append(", sourcePort: ").append(this.sourcePort).append(", destinationPort: ").append(this.destinationPort).append(", tlvs: [");
        if (!this.tlvs.isEmpty()) {
            for (HAProxyTLV tlv : this.tlvs) {
                sb.append(tlv).append(", ");
            }
            sb.setLength(sb.length() - 2);
        }
        sb.append("])");
        return sb.toString();
    }
}
