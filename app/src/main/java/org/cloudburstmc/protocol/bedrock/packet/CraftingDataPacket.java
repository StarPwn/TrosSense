package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.ContainerMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.MaterialReducer;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.PotionMixData;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.recipe.RecipeData;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class CraftingDataPacket implements BedrockPacket {
    private boolean cleanRecipes;
    private final List<RecipeData> craftingData = new ObjectArrayList();
    private final List<PotionMixData> potionMixData = new ObjectArrayList();
    private final List<ContainerMixData> containerMixData = new ObjectArrayList();
    private final List<MaterialReducer> materialReducers = new ObjectArrayList();

    public void setCleanRecipes(boolean cleanRecipes) {
        this.cleanRecipes = cleanRecipes;
    }

    public String toString() {
        return "CraftingDataPacket(craftingData=" + this.craftingData + ", potionMixData=" + this.potionMixData + ", containerMixData=" + this.containerMixData + ", materialReducers=" + this.materialReducers + ", cleanRecipes=" + this.cleanRecipes + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof CraftingDataPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftingDataPacket)) {
            return false;
        }
        CraftingDataPacket other = (CraftingDataPacket) o;
        if (!other.canEqual(this) || this.cleanRecipes != other.cleanRecipes) {
            return false;
        }
        Object this$craftingData = this.craftingData;
        Object other$craftingData = other.craftingData;
        if (this$craftingData != null ? !this$craftingData.equals(other$craftingData) : other$craftingData != null) {
            return false;
        }
        Object this$potionMixData = this.potionMixData;
        Object other$potionMixData = other.potionMixData;
        if (this$potionMixData != null ? !this$potionMixData.equals(other$potionMixData) : other$potionMixData != null) {
            return false;
        }
        Object this$containerMixData = this.containerMixData;
        Object other$containerMixData = other.containerMixData;
        if (this$containerMixData != null ? !this$containerMixData.equals(other$containerMixData) : other$containerMixData != null) {
            return false;
        }
        Object this$materialReducers = this.materialReducers;
        Object other$materialReducers = other.materialReducers;
        return this$materialReducers != null ? this$materialReducers.equals(other$materialReducers) : other$materialReducers == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.cleanRecipes ? 79 : 97);
        Object $craftingData = this.craftingData;
        int result2 = (result * 59) + ($craftingData == null ? 43 : $craftingData.hashCode());
        Object $potionMixData = this.potionMixData;
        int result3 = (result2 * 59) + ($potionMixData == null ? 43 : $potionMixData.hashCode());
        Object $containerMixData = this.containerMixData;
        int result4 = (result3 * 59) + ($containerMixData == null ? 43 : $containerMixData.hashCode());
        Object $materialReducers = this.materialReducers;
        return (result4 * 59) + ($materialReducers != null ? $materialReducers.hashCode() : 43);
    }

    public List<RecipeData> getCraftingData() {
        return this.craftingData;
    }

    public List<PotionMixData> getPotionMixData() {
        return this.potionMixData;
    }

    public List<ContainerMixData> getContainerMixData() {
        return this.containerMixData;
    }

    public List<MaterialReducer> getMaterialReducers() {
        return this.materialReducers;
    }

    public boolean isCleanRecipes() {
        return this.cleanRecipes;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CRAFTING_DATA;
    }
}
