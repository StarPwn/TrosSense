package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class LabTableCombineAction implements ItemStackRequestAction {
    public boolean equals(Object o) {
        return o == this || (o instanceof LabTableCombineAction);
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "LabTableCombineAction()";
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.LAB_TABLE_COMBINE;
    }
}
