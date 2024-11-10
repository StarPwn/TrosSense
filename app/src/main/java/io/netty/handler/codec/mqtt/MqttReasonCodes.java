package io.netty.handler.codec.mqtt;

import com.trossense.bl;
import io.netty.handler.codec.memcache.binary.DefaultBinaryMemcacheResponse;
import org.msgpack.core.MessagePack;

/* loaded from: classes4.dex */
public final class MqttReasonCodes {
    private MqttReasonCodes() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> E valueOfHelper(byte b, E[] values) {
        try {
            return values[b & 255];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("unknown reason code: " + ((int) b));
        }
    }

    /* loaded from: classes4.dex */
    public enum Disconnect {
        NORMAL_DISCONNECT((byte) 0),
        DISCONNECT_WITH_WILL_MESSAGE((byte) 4),
        UNSPECIFIED_ERROR(Byte.MIN_VALUE),
        MALFORMED_PACKET(DefaultBinaryMemcacheResponse.RESPONSE_MAGIC_BYTE),
        PROTOCOL_ERROR((byte) -126),
        IMPLEMENTATION_SPECIFIC_ERROR((byte) -125),
        NOT_AUTHORIZED((byte) -121),
        SERVER_BUSY((byte) -119),
        SERVER_SHUTTING_DOWN((byte) -117),
        KEEP_ALIVE_TIMEOUT((byte) -115),
        SESSION_TAKEN_OVER((byte) -114),
        TOPIC_FILTER_INVALID((byte) -113),
        TOPIC_NAME_INVALID(MessagePack.Code.FIXARRAY_PREFIX),
        RECEIVE_MAXIMUM_EXCEEDED((byte) -109),
        TOPIC_ALIAS_INVALID((byte) -108),
        PACKET_TOO_LARGE((byte) -107),
        MESSAGE_RATE_TOO_HIGH((byte) -106),
        QUOTA_EXCEEDED((byte) -105),
        ADMINISTRATIVE_ACTION((byte) -104),
        PAYLOAD_FORMAT_INVALID((byte) -103),
        RETAIN_NOT_SUPPORTED((byte) -102),
        QOS_NOT_SUPPORTED((byte) -101),
        USE_ANOTHER_SERVER((byte) -100),
        SERVER_MOVED((byte) -99),
        SHARED_SUBSCRIPTIONS_NOT_SUPPORTED((byte) -98),
        CONNECTION_RATE_EXCEEDED((byte) -97),
        MAXIMUM_CONNECT_TIME(MessagePack.Code.FIXSTR_PREFIX),
        SUBSCRIPTION_IDENTIFIERS_NOT_SUPPORTED((byte) -95),
        WILDCARD_SUBSCRIPTIONS_NOT_SUPPORTED((byte) -94);

        protected static final Disconnect[] VALUES;
        private final byte byteValue;

        static {
            Disconnect[] values = values();
            VALUES = new Disconnect[bl.bY];
            for (Disconnect code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        Disconnect(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static Disconnect valueOf(byte b) {
            return (Disconnect) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum Auth {
        SUCCESS((byte) 0),
        CONTINUE_AUTHENTICATION((byte) 24),
        REAUTHENTICATE((byte) 25);

        private static final Auth[] VALUES;
        private final byte byteValue;

        static {
            Auth[] values = values();
            VALUES = new Auth[26];
            for (Auth code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        Auth(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static Auth valueOf(byte b) {
            return (Auth) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum PubAck {
        SUCCESS((byte) 0),
        NO_MATCHING_SUBSCRIBERS((byte) 16),
        UNSPECIFIED_ERROR(Byte.MIN_VALUE),
        IMPLEMENTATION_SPECIFIC_ERROR((byte) -125),
        NOT_AUTHORIZED((byte) -121),
        TOPIC_NAME_INVALID(MessagePack.Code.FIXARRAY_PREFIX),
        PACKET_IDENTIFIER_IN_USE((byte) -111),
        QUOTA_EXCEEDED((byte) -105),
        PAYLOAD_FORMAT_INVALID((byte) -103);

        private static final PubAck[] VALUES;
        private final byte byteValue;

        static {
            PubAck[] values = values();
            VALUES = new PubAck[bl.bP];
            for (PubAck code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        PubAck(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static PubAck valueOf(byte b) {
            return (PubAck) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum PubRec {
        SUCCESS((byte) 0),
        NO_MATCHING_SUBSCRIBERS((byte) 16),
        UNSPECIFIED_ERROR(Byte.MIN_VALUE),
        IMPLEMENTATION_SPECIFIC_ERROR((byte) -125),
        NOT_AUTHORIZED((byte) -121),
        TOPIC_NAME_INVALID(MessagePack.Code.FIXARRAY_PREFIX),
        PACKET_IDENTIFIER_IN_USE((byte) -111),
        QUOTA_EXCEEDED((byte) -105),
        PAYLOAD_FORMAT_INVALID((byte) -103);

        private static final PubRec[] VALUES;
        private final byte byteValue;

        static {
            PubRec[] values = values();
            VALUES = new PubRec[bl.bP];
            for (PubRec code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        PubRec(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static PubRec valueOf(byte b) {
            return (PubRec) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum PubRel {
        SUCCESS((byte) 0),
        PACKET_IDENTIFIER_NOT_FOUND((byte) -110);

        private static final PubRel[] VALUES;
        private final byte byteValue;

        static {
            PubRel[] values = values();
            VALUES = new PubRel[bl.bI];
            for (PubRel code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        PubRel(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static PubRel valueOf(byte b) {
            return (PubRel) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum PubComp {
        SUCCESS((byte) 0),
        PACKET_IDENTIFIER_NOT_FOUND((byte) -110);

        private static final PubComp[] VALUES;
        private final byte byteValue;

        static {
            PubComp[] values = values();
            VALUES = new PubComp[bl.bI];
            for (PubComp code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        PubComp(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static PubComp valueOf(byte b) {
            return (PubComp) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum SubAck {
        GRANTED_QOS_0((byte) 0),
        GRANTED_QOS_1((byte) 1),
        GRANTED_QOS_2((byte) 2),
        UNSPECIFIED_ERROR(Byte.MIN_VALUE),
        IMPLEMENTATION_SPECIFIC_ERROR((byte) -125),
        NOT_AUTHORIZED((byte) -121),
        TOPIC_FILTER_INVALID((byte) -113),
        PACKET_IDENTIFIER_IN_USE((byte) -111),
        QUOTA_EXCEEDED((byte) -105),
        SHARED_SUBSCRIPTIONS_NOT_SUPPORTED((byte) -98),
        SUBSCRIPTION_IDENTIFIERS_NOT_SUPPORTED((byte) -95),
        WILDCARD_SUBSCRIPTIONS_NOT_SUPPORTED((byte) -94);

        private static final SubAck[] VALUES;
        private final byte byteValue;

        static {
            SubAck[] values = values();
            VALUES = new SubAck[bl.bY];
            for (SubAck code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        SubAck(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static SubAck valueOf(byte b) {
            return (SubAck) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }

    /* loaded from: classes4.dex */
    public enum UnsubAck {
        SUCCESS((byte) 0),
        NO_SUBSCRIPTION_EXISTED((byte) 17),
        UNSPECIFIED_ERROR(Byte.MIN_VALUE),
        IMPLEMENTATION_SPECIFIC_ERROR((byte) -125),
        NOT_AUTHORIZED((byte) -121),
        TOPIC_FILTER_INVALID((byte) -113),
        PACKET_IDENTIFIER_IN_USE((byte) -111);

        private static final UnsubAck[] VALUES;
        private final byte byteValue;

        static {
            UnsubAck[] values = values();
            VALUES = new UnsubAck[bl.bH];
            for (UnsubAck code : values) {
                int unsignedByte = code.byteValue & 255;
                VALUES[unsignedByte] = code;
            }
        }

        UnsubAck(byte byteValue) {
            this.byteValue = byteValue;
        }

        public byte byteValue() {
            return this.byteValue;
        }

        public static UnsubAck valueOf(byte b) {
            return (UnsubAck) MqttReasonCodes.valueOfHelper(b, VALUES);
        }
    }
}
