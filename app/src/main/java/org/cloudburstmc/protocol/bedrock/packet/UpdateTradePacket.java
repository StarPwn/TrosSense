package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class UpdateTradePacket implements BedrockPacket {
    private int containerId;
    private ContainerType containerType;
    private String displayName;
    private boolean newTradingUi;
    private NbtMap offers;
    private long playerUniqueEntityId;
    private boolean recipeAddedOnUpdate;
    private int size;
    private int tradeTier;
    private long traderUniqueEntityId;
    private boolean usingEconomyTrade;

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public void setContainerType(ContainerType containerType) {
        this.containerType = containerType;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setNewTradingUi(boolean newTradingUi) {
        this.newTradingUi = newTradingUi;
    }

    public void setOffers(NbtMap offers) {
        this.offers = offers;
    }

    public void setPlayerUniqueEntityId(long playerUniqueEntityId) {
        this.playerUniqueEntityId = playerUniqueEntityId;
    }

    public void setRecipeAddedOnUpdate(boolean recipeAddedOnUpdate) {
        this.recipeAddedOnUpdate = recipeAddedOnUpdate;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTradeTier(int tradeTier) {
        this.tradeTier = tradeTier;
    }

    public void setTraderUniqueEntityId(long traderUniqueEntityId) {
        this.traderUniqueEntityId = traderUniqueEntityId;
    }

    public void setUsingEconomyTrade(boolean usingEconomyTrade) {
        this.usingEconomyTrade = usingEconomyTrade;
    }

    protected boolean canEqual(Object other) {
        return other instanceof UpdateTradePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UpdateTradePacket)) {
            return false;
        }
        UpdateTradePacket other = (UpdateTradePacket) o;
        if (!other.canEqual(this) || this.containerId != other.containerId || this.size != other.size || this.tradeTier != other.tradeTier || this.traderUniqueEntityId != other.traderUniqueEntityId || this.playerUniqueEntityId != other.playerUniqueEntityId || this.newTradingUi != other.newTradingUi || this.recipeAddedOnUpdate != other.recipeAddedOnUpdate || this.usingEconomyTrade != other.usingEconomyTrade) {
            return false;
        }
        Object this$containerType = this.containerType;
        Object other$containerType = other.containerType;
        if (this$containerType != null ? !this$containerType.equals(other$containerType) : other$containerType != null) {
            return false;
        }
        Object this$displayName = this.displayName;
        Object other$displayName = other.displayName;
        if (this$displayName != null ? !this$displayName.equals(other$displayName) : other$displayName != null) {
            return false;
        }
        Object this$offers = this.offers;
        Object other$offers = other.offers;
        return this$offers != null ? this$offers.equals(other$offers) : other$offers == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.containerId;
        int result2 = (((result * 59) + this.size) * 59) + this.tradeTier;
        long $traderUniqueEntityId = this.traderUniqueEntityId;
        long $playerUniqueEntityId = this.playerUniqueEntityId;
        int result3 = ((((((((result2 * 59) + ((int) (($traderUniqueEntityId >>> 32) ^ $traderUniqueEntityId))) * 59) + ((int) (($playerUniqueEntityId >>> 32) ^ $playerUniqueEntityId))) * 59) + (this.newTradingUi ? 79 : 97)) * 59) + (this.recipeAddedOnUpdate ? 79 : 97)) * 59;
        int i = this.usingEconomyTrade ? 79 : 97;
        Object $containerType = this.containerType;
        int result4 = ((result3 + i) * 59) + ($containerType == null ? 43 : $containerType.hashCode());
        Object $displayName = this.displayName;
        int result5 = (result4 * 59) + ($displayName == null ? 43 : $displayName.hashCode());
        Object $offers = this.offers;
        return (result5 * 59) + ($offers != null ? $offers.hashCode() : 43);
    }

    public String toString() {
        return "UpdateTradePacket(containerId=" + this.containerId + ", containerType=" + this.containerType + ", size=" + this.size + ", tradeTier=" + this.tradeTier + ", traderUniqueEntityId=" + this.traderUniqueEntityId + ", playerUniqueEntityId=" + this.playerUniqueEntityId + ", displayName=" + this.displayName + ", offers=" + this.offers + ", newTradingUi=" + this.newTradingUi + ", recipeAddedOnUpdate=" + this.recipeAddedOnUpdate + ", usingEconomyTrade=" + this.usingEconomyTrade + ")";
    }

    public int getContainerId() {
        return this.containerId;
    }

    public ContainerType getContainerType() {
        return this.containerType;
    }

    public int getSize() {
        return this.size;
    }

    public int getTradeTier() {
        return this.tradeTier;
    }

    public long getTraderUniqueEntityId() {
        return this.traderUniqueEntityId;
    }

    public long getPlayerUniqueEntityId() {
        return this.playerUniqueEntityId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public NbtMap getOffers() {
        return this.offers;
    }

    public boolean isNewTradingUi() {
        return this.newTradingUi;
    }

    public boolean isRecipeAddedOnUpdate() {
        return this.recipeAddedOnUpdate;
    }

    public boolean isUsingEconomyTrade() {
        return this.usingEconomyTrade;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UPDATE_TRADE;
    }
}
