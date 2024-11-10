package org.cloudburstmc.protocol.bedrock.codec.compat.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.util.AsciiString;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer;
import org.cloudburstmc.protocol.bedrock.packet.LoginPacket;
import org.cloudburstmc.protocol.common.util.Preconditions;
import org.cloudburstmc.protocol.common.util.VarInts;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.JSONValue;

/* loaded from: classes5.dex */
public class LoginSerializerCompat implements BedrockPacketSerializer<LoginPacket> {
    public static final LoginSerializerCompat INSTANCE = new LoginSerializerCompat();

    private LoginSerializerCompat() {
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, LoginPacket packet) {
        throw new UnsupportedOperationException();
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, LoginPacket packet) {
        packet.setProtocolVersion(buffer.readInt());
        ByteBuf jwt = buffer.readSlice(VarInts.readUnsignedInt(buffer));
        Object json = JSONValue.parse(readString(jwt).toString());
        Preconditions.checkArgument((json instanceof JSONObject) && ((JSONObject) json).containsKey("chain"), "Invalid login chain");
        Object chain = ((JSONObject) json).get("chain");
        Preconditions.checkArgument(chain instanceof JSONArray, "Expected JSON array for login chain");
        Iterator it2 = ((JSONArray) chain).iterator();
        while (it2.hasNext()) {
            Object node = it2.next();
            Preconditions.checkArgument(node instanceof String, "Expected String in login chain");
            packet.getChain().add((String) node);
        }
        String value = (String) jwt.readCharSequence(jwt.readIntLE(), StandardCharsets.UTF_8);
        packet.setExtra(value);
    }

    protected AsciiString readString(ByteBuf buffer) {
        return (AsciiString) buffer.readCharSequence(buffer.readIntLE(), StandardCharsets.US_ASCII);
    }
}
