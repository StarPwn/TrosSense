package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action;

/* loaded from: classes5.dex */
public final class BeaconPaymentAction implements ItemStackRequestAction {
    private final int primaryEffect;
    private final int secondaryEffect;

    public BeaconPaymentAction(int primaryEffect, int secondaryEffect) {
        this.primaryEffect = primaryEffect;
        this.secondaryEffect = secondaryEffect;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BeaconPaymentAction)) {
            return false;
        }
        BeaconPaymentAction other = (BeaconPaymentAction) o;
        return getPrimaryEffect() == other.getPrimaryEffect() && getSecondaryEffect() == other.getSecondaryEffect();
    }

    public int hashCode() {
        int result = (1 * 59) + getPrimaryEffect();
        return (result * 59) + getSecondaryEffect();
    }

    public String toString() {
        return "BeaconPaymentAction(primaryEffect=" + getPrimaryEffect() + ", secondaryEffect=" + getSecondaryEffect() + ")";
    }

    public int getPrimaryEffect() {
        return this.primaryEffect;
    }

    public int getSecondaryEffect() {
        return this.secondaryEffect;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction
    public ItemStackRequestActionType getType() {
        return ItemStackRequestActionType.BEACON_PAYMENT;
    }
}
