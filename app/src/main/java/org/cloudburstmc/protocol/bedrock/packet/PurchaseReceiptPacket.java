package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PurchaseReceiptPacket implements BedrockPacket {
    private final List<String> receipts = new ObjectArrayList();

    protected boolean canEqual(Object other) {
        return other instanceof PurchaseReceiptPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PurchaseReceiptPacket)) {
            return false;
        }
        PurchaseReceiptPacket other = (PurchaseReceiptPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$receipts = this.receipts;
        Object other$receipts = other.receipts;
        return this$receipts != null ? this$receipts.equals(other$receipts) : other$receipts == null;
    }

    public int hashCode() {
        Object $receipts = this.receipts;
        int result = (1 * 59) + ($receipts == null ? 43 : $receipts.hashCode());
        return result;
    }

    public String toString() {
        return "PurchaseReceiptPacket(receipts=" + this.receipts + ")";
    }

    public List<String> getReceipts() {
        return this.receipts;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PURCHASE_RECEIPT;
    }
}
