package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UnlockedRecipesPacket implements BedrockPacket {
    private ActionType action;
    private final List<String> unlockedRecipes = new ObjectArrayList();

    /* loaded from: classes5.dex */
    public enum ActionType {
        EMPTY,
        INITIALLY_UNLOCKED,
        NEWLY_UNLOCKED,
        REMOVE_UNLOCKED,
        REMOVE_ALL
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UnlockedRecipesPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnlockedRecipesPacket)) {
            return false;
        }
        UnlockedRecipesPacket other = (UnlockedRecipesPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$unlockedRecipes = this.unlockedRecipes;
        Object other$unlockedRecipes = other.unlockedRecipes;
        return this$unlockedRecipes != null ? this$unlockedRecipes.equals(other$unlockedRecipes) : other$unlockedRecipes == null;
    }

    public int hashCode() {
        Object $action = this.action;
        int result = (1 * 59) + ($action == null ? 43 : $action.hashCode());
        Object $unlockedRecipes = this.unlockedRecipes;
        return (result * 59) + ($unlockedRecipes != null ? $unlockedRecipes.hashCode() : 43);
    }

    public String toString() {
        return "UnlockedRecipesPacket(action=" + this.action + ", unlockedRecipes=" + this.unlockedRecipes + ")";
    }

    public ActionType getAction() {
        return this.action;
    }

    public List<String> getUnlockedRecipes() {
        return this.unlockedRecipes;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UNLOCKED_RECIPES;
    }
}
