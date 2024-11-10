package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CraftNonImplementedAction implements ItemStackRequestAction {
    public boolean equals(Object o) {
        return o == this || (o instanceof CraftNonImplementedAction);
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "CraftNonImplementedAction()";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_NON_IMPLEMENTED_DEPRECATED;
    }
}
