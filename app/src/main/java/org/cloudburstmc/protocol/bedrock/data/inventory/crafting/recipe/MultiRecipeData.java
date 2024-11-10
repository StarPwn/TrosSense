package org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe;

import java.util.UUID;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.CraftingDataType;

/* loaded from: classes5.dex */
public class MultiRecipeData implements UniqueCraftingData {
    private final int netId;
    private final UUID uuid;

    public String toString() {
        return "MultiRecipeData(uuid=" + getUuid() + ", netId=" + getNetId() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof MultiRecipeData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MultiRecipeData)) {
            return false;
        }
        MultiRecipeData other = (MultiRecipeData) o;
        if (!other.canEqual(this) || getNetId() != other.getNetId()) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        return this$uuid != null ? this$uuid.equals(other$uuid) : other$uuid == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getNetId();
        Object $uuid = getUuid();
        return (result * 59) + ($uuid == null ? 43 : $uuid.hashCode());
    }

    private MultiRecipeData(UUID uuid, int netId) {
        this.uuid = uuid;
        this.netId = netId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.UniqueCraftingData
    public UUID getUuid() {
        return this.uuid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.NetworkRecipeData
    public int getNetId() {
        return this.netId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData
    public CraftingDataType getType() {
        return CraftingDataType.MULTI;
    }

    public static MultiRecipeData of(UUID uuid, int netId) {
        return new MultiRecipeData(uuid, netId);
    }
}
