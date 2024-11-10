package org.cloudburstmc.protocol.bedrock.codec.v662.serializer;

import io.netty.buffer.ByteBuf;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper;
import org.cloudburstmc.protocol.bedrock.codec.v527.serializer.PlayerAuthInputSerializer_v527;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes5.dex */
public class PlayerAuthInputSerializer_v662 extends PlayerAuthInputSerializer_v527 {
    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.serializer.PlayerAuthInputSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void serialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerAuthInputPacket packet) {
        super.serialize(buffer, helper, packet);
        if (packet.getInputData().contains(PlayerAuthInputData.IN_CLIENT_PREDICTED_IN_VEHICLE)) {
            helper.writeVector2f(buffer, packet.getVehicleRotation());
            VarInts.writeLong(buffer, packet.getPredictedVehicle());
        }
        helper.writeVector2f(buffer, packet.getAnalogMoveVector());
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.v428.serializer.PlayerAuthInputSerializer_v428, org.cloudburstmc.protocol.bedrock.codec.v419.serializer.PlayerAuthInputSerializer_v419, org.cloudburstmc.protocol.bedrock.codec.v388.serializer.PlayerAuthInputSerializer_v388, org.cloudburstmc.protocol.bedrock.codec.BedrockPacketSerializer
    public void deserialize(ByteBuf buffer, BedrockCodecHelper helper, PlayerAuthInputPacket packet) {
        super.deserialize(buffer, helper, packet);
        if (packet.getInputData().contains(PlayerAuthInputData.IN_CLIENT_PREDICTED_IN_VEHICLE)) {
            packet.setVehicleRotation(helper.readVector2f(buffer));
            packet.setPredictedVehicle(VarInts.readLong(buffer));
        }
        packet.setAnalogMoveVector(helper.readVector2f(buffer));
    }
}
