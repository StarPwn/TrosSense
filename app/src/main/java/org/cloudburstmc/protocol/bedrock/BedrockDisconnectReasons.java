package org.cloudburstmc.protocol.bedrock;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.cloudburstmc.netty.channel.raknet.RakDisconnectReason;

/* loaded from: classes5.dex */
public final class BedrockDisconnectReasons {
    public static final String CLOSED = "disconnect.closed";
    public static final String DISCONNECTED = "disconnect.disconnected";
    private static final Map<RakDisconnectReason, String> FROM_RAKNET = generateRakNetMappings();
    public static final String REMOVED = "disconnect.removed";
    public static final String TIMEOUT = "disconnect.timeout";
    public static final String UNKNOWN = "disconnect.lost";

    private BedrockDisconnectReasons() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static Map<RakDisconnectReason, String> generateRakNetMappings() {
        EnumMap<RakDisconnectReason, String> map = new EnumMap<>((Class<RakDisconnectReason>) RakDisconnectReason.class);
        map.put((EnumMap<RakDisconnectReason, String>) RakDisconnectReason.CLOSED_BY_REMOTE_PEER, (RakDisconnectReason) CLOSED);
        map.put((EnumMap<RakDisconnectReason, String>) RakDisconnectReason.DISCONNECTED, (RakDisconnectReason) DISCONNECTED);
        map.put((EnumMap<RakDisconnectReason, String>) RakDisconnectReason.TIMED_OUT, (RakDisconnectReason) TIMEOUT);
        map.put((EnumMap<RakDisconnectReason, String>) RakDisconnectReason.BAD_PACKET, (RakDisconnectReason) REMOVED);
        return Collections.unmodifiableMap(map);
    }

    public static String getReason(RakDisconnectReason reason) {
        return FROM_RAKNET.getOrDefault(reason, reason.name());
    }
}
