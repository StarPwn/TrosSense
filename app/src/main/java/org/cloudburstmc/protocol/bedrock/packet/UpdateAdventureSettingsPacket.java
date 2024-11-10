package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateAdventureSettingsPacket implements BedrockPacket {
    private boolean autoJump;
    private boolean immutableWorld;
    private boolean noMvP;
    private boolean noPvM;
    private boolean showNameTags;

    public void setAutoJump(boolean autoJump) {
        this.autoJump = autoJump;
    }

    public void setImmutableWorld(boolean immutableWorld) {
        this.immutableWorld = immutableWorld;
    }

    public void setNoMvP(boolean noMvP) {
        this.noMvP = noMvP;
    }

    public void setNoPvM(boolean noPvM) {
        this.noPvM = noPvM;
    }

    public void setShowNameTags(boolean showNameTags) {
        this.showNameTags = showNameTags;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateAdventureSettingsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateAdventureSettingsPacket)) {
            return false;
        }
        UpdateAdventureSettingsPacket other = (UpdateAdventureSettingsPacket) o;
        return other.canEqual(this) && this.noPvM == other.noPvM && this.noMvP == other.noMvP && this.immutableWorld == other.immutableWorld && this.showNameTags == other.showNameTags && this.autoJump == other.autoJump;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.noPvM ? 79 : 97);
        return (((((((result * 59) + (this.noMvP ? 79 : 97)) * 59) + (this.immutableWorld ? 79 : 97)) * 59) + (this.showNameTags ? 79 : 97)) * 59) + (this.autoJump ? 79 : 97);
    }

    public String toString() {
        return "UpdateAdventureSettingsPacket(noPvM=" + this.noPvM + ", noMvP=" + this.noMvP + ", immutableWorld=" + this.immutableWorld + ", showNameTags=" + this.showNameTags + ", autoJump=" + this.autoJump + ")";
    }

    public boolean isNoPvM() {
        return this.noPvM;
    }

    public boolean isNoMvP() {
        return this.noMvP;
    }

    public boolean isImmutableWorld() {
        return this.immutableWorld;
    }

    public boolean isShowNameTags() {
        return this.showNameTags;
    }

    public boolean isAutoJump() {
        return this.autoJump;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_ADVENTURE_SETTINGS;
    }
}
