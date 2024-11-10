package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CreateAction implements ItemStackRequestAction {
    private final int slot;

    public CreateAction(int slot) {
        this.slot = slot;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CreateAction)) {
            return false;
        }
        CreateAction other = (CreateAction) o;
        return getSlot() == other.getSlot();
    }

    public int hashCode() {
        int result = (1 * 59) + getSlot();
        return result;
    }

    public String toString() {
        return "CreateAction(slot=" + getSlot() + ")";
    }

    public int getSlot() {
        return this.slot;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CREATE;
    }
}
