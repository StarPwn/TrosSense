package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.StoreOfferRedirectType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ShowStoreOfferPacket implements BedrockPacket {
    private String offerId;
    private StoreOfferRedirectType redirectType;

    @Deprecated
    private boolean shownToAll;

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setRedirectType(StoreOfferRedirectType redirectType) {
        this.redirectType = redirectType;
    }

    @Deprecated
    public void setShownToAll(boolean shownToAll) {
        this.shownToAll = shownToAll;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ShowStoreOfferPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ShowStoreOfferPacket)) {
            return false;
        }
        ShowStoreOfferPacket other = (ShowStoreOfferPacket) o;
        if (!other.canEqual(this) || this.shownToAll != other.shownToAll) {
            return false;
        }
        Object this$offerId = this.offerId;
        Object other$offerId = other.offerId;
        if (this$offerId != null ? !this$offerId.equals(other$offerId) : other$offerId != null) {
            return false;
        }
        Object this$redirectType = this.redirectType;
        Object other$redirectType = other.redirectType;
        return this$redirectType != null ? this$redirectType.equals(other$redirectType) : other$redirectType == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.shownToAll ? 79 : 97);
        Object $offerId = this.offerId;
        int result2 = (result * 59) + ($offerId == null ? 43 : $offerId.hashCode());
        Object $redirectType = this.redirectType;
        return (result2 * 59) + ($redirectType != null ? $redirectType.hashCode() : 43);
    }

    public String toString() {
        return "ShowStoreOfferPacket(offerId=" + this.offerId + ", shownToAll=" + this.shownToAll + ", redirectType=" + this.redirectType + ")";
    }

    public String getOfferId() {
        return this.offerId;
    }

    @Deprecated
    public boolean isShownToAll() {
        return this.shownToAll;
    }

    public StoreOfferRedirectType getRedirectType() {
        return this.redirectType;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SHOW_STORE_OFFER;
    }
}
