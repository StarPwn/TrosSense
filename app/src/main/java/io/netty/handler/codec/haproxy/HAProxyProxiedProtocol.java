package io.netty.handler.codec.haproxy;

import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;

/* loaded from: classes4.dex */
public enum HAProxyProxiedProtocol {
    UNKNOWN((byte) 0, AddressFamily.AF_UNSPEC, TransportProtocol.UNSPEC),
    TCP4((byte) 17, AddressFamily.AF_IPv4, TransportProtocol.STREAM),
    TCP6(BinaryMemcacheOpcodes.SASL_AUTH, AddressFamily.AF_IPv6, TransportProtocol.STREAM),
    UDP4((byte) 18, AddressFamily.AF_IPv4, TransportProtocol.DGRAM),
    UDP6((byte) 34, AddressFamily.AF_IPv6, TransportProtocol.DGRAM),
    UNIX_STREAM((byte) 49, AddressFamily.AF_UNIX, TransportProtocol.STREAM),
    UNIX_DGRAM((byte) 50, AddressFamily.AF_UNIX, TransportProtocol.DGRAM);

    private final AddressFamily addressFamily;
    private final byte byteValue;
    private final TransportProtocol transportProtocol;

    HAProxyProxiedProtocol(byte byteValue, AddressFamily addressFamily, TransportProtocol transportProtocol) {
        this.byteValue = byteValue;
        this.addressFamily = addressFamily;
        this.transportProtocol = transportProtocol;
    }

    public static HAProxyProxiedProtocol valueOf(byte tpafByte) {
        switch (tpafByte) {
            case 0:
                return UNKNOWN;
            case 17:
                return TCP4;
            case 18:
                return UDP4;
            case 33:
                return TCP6;
            case 34:
                return UDP6;
            case 49:
                return UNIX_STREAM;
            case 50:
                return UNIX_DGRAM;
            default:
                throw new IllegalArgumentException("unknown transport protocol + address family: " + (tpafByte & 255));
        }
    }

    public byte byteValue() {
        return this.byteValue;
    }

    public AddressFamily addressFamily() {
        return this.addressFamily;
    }

    public TransportProtocol transportProtocol() {
        return this.transportProtocol;
    }

    /* loaded from: classes4.dex */
    public enum AddressFamily {
        AF_UNSPEC((byte) 0),
        AF_IPv4((byte) 16),
        AF_IPv6((byte) 32),
        AF_UNIX((byte) 48);

        private static final byte FAMILY_MASK = -16;
        private final byte byteValue;

        AddressFamily(byte byteValue) {
            this.byteValue = byteValue;
        }

        public static AddressFamily valueOf(byte tpafByte) {
            int addressFamily = tpafByte & FAMILY_MASK;
            switch ((byte) addressFamily) {
                case 0:
                    return AF_UNSPEC;
                case 16:
                    return AF_IPv4;
                case 32:
                    return AF_IPv6;
                case 48:
                    return AF_UNIX;
                default:
                    throw new IllegalArgumentException("unknown address family: " + addressFamily);
            }
        }

        public byte byteValue() {
            return this.byteValue;
        }
    }

    /* loaded from: classes4.dex */
    public enum TransportProtocol {
        UNSPEC((byte) 0),
        STREAM((byte) 1),
        DGRAM((byte) 2);

        private static final byte TRANSPORT_MASK = 15;
        private final byte transportByte;

        TransportProtocol(byte transportByte) {
            this.transportByte = transportByte;
        }

        public static TransportProtocol valueOf(byte tpafByte) {
            int transportProtocol = tpafByte & 15;
            switch ((byte) transportProtocol) {
                case 0:
                    return UNSPEC;
                case 1:
                    return STREAM;
                case 2:
                    return DGRAM;
                default:
                    throw new IllegalArgumentException("unknown transport protocol: " + transportProtocol);
            }
        }

        public byte byteValue() {
            return this.transportByte;
        }
    }
}
