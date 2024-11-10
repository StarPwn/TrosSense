package org.cloudburstmc.protocol.bedrock.data;

import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;

/* loaded from: classes5.dex */
public interface PlayerAbilityHolder {
    List<AbilityLayer> getAbilityLayers();

    CommandPermission getCommandPermission();

    PlayerPermission getPlayerPermission();

    long getUniqueEntityId();

    void setAbilityLayers(List<AbilityLayer> list);

    void setCommandPermission(CommandPermission commandPermission);

    void setPlayerPermission(PlayerPermission playerPermission);

    void setUniqueEntityId(long j);
}
