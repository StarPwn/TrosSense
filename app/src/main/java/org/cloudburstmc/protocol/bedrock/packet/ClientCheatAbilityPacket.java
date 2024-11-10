package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.AbilityLayer;
import org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.cloudburstmc.protocol.common.PacketSignal;

@Deprecated
/* loaded from: classes5.dex */
public class ClientCheatAbilityPacket implements BedrockPacket, PlayerAbilityHolder {
    private List<AbilityLayer> abilityLayers = new ObjectArrayList();
    private CommandPermission commandPermission;
    private PlayerPermission playerPermission;
    private long uniqueEntityId;

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setAbilityLayers(List<AbilityLayer> abilityLayers) {
        this.abilityLayers = abilityLayers;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setCommandPermission(CommandPermission commandPermission) {
        this.commandPermission = commandPermission;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setPlayerPermission(PlayerPermission playerPermission) {
        this.playerPermission = playerPermission;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClientCheatAbilityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientCheatAbilityPacket)) {
            return false;
        }
        ClientCheatAbilityPacket other = (ClientCheatAbilityPacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$playerPermission = this.playerPermission;
        Object other$playerPermission = other.playerPermission;
        if (this$playerPermission != null ? !this$playerPermission.equals(other$playerPermission) : other$playerPermission != null) {
            return false;
        }
        Object this$commandPermission = this.commandPermission;
        Object other$commandPermission = other.commandPermission;
        if (this$commandPermission != null ? !this$commandPermission.equals(other$commandPermission) : other$commandPermission != null) {
            return false;
        }
        Object this$abilityLayers = this.abilityLayers;
        Object other$abilityLayers = other.abilityLayers;
        return this$abilityLayers != null ? this$abilityLayers.equals(other$abilityLayers) : other$abilityLayers == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $playerPermission = this.playerPermission;
        int result2 = (result * 59) + ($playerPermission == null ? 43 : $playerPermission.hashCode());
        Object $commandPermission = this.commandPermission;
        int result3 = (result2 * 59) + ($commandPermission == null ? 43 : $commandPermission.hashCode());
        Object $abilityLayers = this.abilityLayers;
        return (result3 * 59) + ($abilityLayers != null ? $abilityLayers.hashCode() : 43);
    }

    public String toString() {
        return "ClientCheatAbilityPacket(uniqueEntityId=" + this.uniqueEntityId + ", playerPermission=" + this.playerPermission + ", commandPermission=" + this.commandPermission + ", abilityLayers=" + this.abilityLayers + ")";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public PlayerPermission getPlayerPermission() {
        return this.playerPermission;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public CommandPermission getCommandPermission() {
        return this.commandPermission;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.PlayerAbilityHolder
    public List<AbilityLayer> getAbilityLayers() {
        return this.abilityLayers;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENT_CHEAT_ABILITY;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }
}
