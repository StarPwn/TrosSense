package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetDifficultyPacket implements BedrockPacket {
    private int difficulty;

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetDifficultyPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetDifficultyPacket)) {
            return false;
        }
        SetDifficultyPacket other = (SetDifficultyPacket) o;
        return other.canEqual(this) && this.difficulty == other.difficulty;
    }

    public int hashCode() {
        int result = (1 * 59) + this.difficulty;
        return result;
    }

    public String toString() {
        return "SetDifficultyPacket(difficulty=" + this.difficulty + ")";
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_DIFFICULTY;
    }
}
