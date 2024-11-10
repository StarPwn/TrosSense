package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CraftCreativeAction implements ItemStackRequestAction {
    private final int creativeItemNetworkId;

    public CraftCreativeAction(int creativeItemNetworkId) {
        this.creativeItemNetworkId = creativeItemNetworkId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftCreativeAction)) {
            return false;
        }
        CraftCreativeAction other = (CraftCreativeAction) o;
        return getCreativeItemNetworkId() == other.getCreativeItemNetworkId();
    }

    public int hashCode() {
        int result = (1 * 59) + getCreativeItemNetworkId();
        return result;
    }

    public String toString() {
        return "CraftCreativeAction(creativeItemNetworkId=" + getCreativeItemNetworkId() + ")";
    }

    public int getCreativeItemNetworkId() {
        return this.creativeItemNetworkId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_CREATIVE;
    }
}
