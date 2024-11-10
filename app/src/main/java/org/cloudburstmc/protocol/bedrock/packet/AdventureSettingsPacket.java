package org.cloudburstmc.protocol.bedrock.packet;

import java.util.EnumSet;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.data.AdventureSetting;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.common.PacketSignal;

@Deprecated
/* loaded from: classes5.dex */
public class AdventureSettingsPacket implements BedrockPacket {
    private long uniqueEntityId;
    private final Set<AdventureSetting> settings = EnumSet.noneOf(AdventureSetting.class);
    private CommandPermission commandPermission = CommandPermission.ANY;
    private PlayerPermission playerPermission = PlayerPermission.VISITOR;

    public void setCommandPermission(CommandPermission commandPermission) {
        this.commandPermission = commandPermission;
    }

    public void setPlayerPermission(PlayerPermission playerPermission) {
        this.playerPermission = playerPermission;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AdventureSettingsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdventureSettingsPacket)) {
            return false;
        }
        AdventureSettingsPacket other = (AdventureSettingsPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$settings = this.settings;
        Object other$settings = other.settings;
        if (this$settings != null ? !this$settings.equals(other$settings) : other$settings != null) {
            return false;
        }
        Object this$commandPermission = this.commandPermission;
        Object other$commandPermission = other.commandPermission;
        if (this$commandPermission != null ? !this$commandPermission.equals(other$commandPermission) : other$commandPermission != null) {
            return false;
        }
        Object this$playerPermission = this.playerPermission;
        Object other$playerPermission = other.playerPermission;
        return this$playerPermission != null ? this$playerPermission.equals(other$playerPermission) : other$playerPermission == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $settings = this.settings;
        int result2 = (result * 59) + ($settings == null ? 43 : $settings.hashCode());
        Object $commandPermission = this.commandPermission;
        int result3 = (result2 * 59) + ($commandPermission == null ? 43 : $commandPermission.hashCode());
        Object $playerPermission = this.playerPermission;
        return (result3 * 59) + ($playerPermission != null ? $playerPermission.hashCode() : 43);
    }

    public String toString() {
        return "AdventureSettingsPacket(settings=" + this.settings + ", commandPermission=" + this.commandPermission + ", playerPermission=" + this.playerPermission + ", uniqueEntityId=" + this.uniqueEntityId + ")";
    }

    public Set<AdventureSetting> getSettings() {
        return this.settings;
    }

    public CommandPermission getCommandPermission() {
        return this.commandPermission;
    }

    public PlayerPermission getPlayerPermission() {
        return this.playerPermission;
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADVENTURE_SETTINGS;
    }
}
