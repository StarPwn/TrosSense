package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class CraftLoomAction implements ItemStackRequestAction {
    private final String patternId;

    public CraftLoomAction(String patternId) {
        this.patternId = patternId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CraftLoomAction)) {
            return false;
        }
        CraftLoomAction other = (CraftLoomAction) o;
        Object this$patternId = getPatternId();
        Object other$patternId = other.getPatternId();
        return this$patternId != null ? this$patternId.equals(other$patternId) : other$patternId == null;
    }

    public int hashCode() {
        Object $patternId = getPatternId();
        int result = (1 * 59) + ($patternId == null ? 43 : $patternId.hashCode());
        return result;
    }

    public String toString() {
        return "CraftLoomAction(patternId=" + getPatternId() + ")";
    }

    public String getPatternId() {
        return this.patternId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.CRAFT_LOOM;
    }
}
