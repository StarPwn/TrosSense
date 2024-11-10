package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LecternUpdatePacket implements BedrockPacket {
    private Vector3i blockPosition;

    @Deprecated
    private boolean droppingBook;
    private int page;
    private int totalPages;

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    @Deprecated
    public void setDroppingBook(boolean droppingBook) {
        this.droppingBook = droppingBook;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LecternUpdatePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LecternUpdatePacket)) {
            return false;
        }
        LecternUpdatePacket other = (LecternUpdatePacket) o;
        if (!other.canEqual(this) || this.page != other.page || this.totalPages != other.totalPages || this.droppingBook != other.droppingBook) {
            return false;
        }
        Object this$blockPosition = this.blockPosition;
        Object other$blockPosition = other.blockPosition;
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.page;
        int result2 = ((result * 59) + this.totalPages) * 59;
        int i = this.droppingBook ? 79 : 97;
        Object $blockPosition = this.blockPosition;
        return ((result2 + i) * 59) + ($blockPosition == null ? 43 : $blockPosition.hashCode());
    }

    public String toString() {
        return "LecternUpdatePacket(page=" + this.page + ", totalPages=" + this.totalPages + ", blockPosition=" + this.blockPosition + ", droppingBook=" + this.droppingBook + ")";
    }

    public int getPage() {
        return this.page;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    @Deprecated
    public boolean isDroppingBook() {
        return this.droppingBook;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LECTERN_UPDATE;
    }
}
