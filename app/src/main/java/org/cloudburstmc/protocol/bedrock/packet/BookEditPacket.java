package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class BookEditPacket implements BedrockPacket {
    private Action action;
    private String author;
    private int inventorySlot;
    private int pageNumber;
    private String photoName;
    private int secondaryPageNumber;
    private String text;
    private String title;
    private String xuid;

    /* loaded from: classes5.dex */
    public enum Action {
        REPLACE_PAGE,
        ADD_PAGE,
        DELETE_PAGE,
        SWAP_PAGES,
        SIGN_BOOK
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setInventorySlot(int inventorySlot) {
        this.inventorySlot = inventorySlot;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setSecondaryPageNumber(int secondaryPageNumber) {
        this.secondaryPageNumber = secondaryPageNumber;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setXuid(String xuid) {
        this.xuid = xuid;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BookEditPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BookEditPacket)) {
            return false;
        }
        BookEditPacket other = (BookEditPacket) o;
        if (!other.canEqual(this) || this.inventorySlot != other.inventorySlot || this.pageNumber != other.pageNumber || this.secondaryPageNumber != other.secondaryPageNumber) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$text = this.text;
        Object other$text = other.text;
        if (this$text != null ? !this$text.equals(other$text) : other$text != null) {
            return false;
        }
        Object this$photoName = this.photoName;
        Object other$photoName = other.photoName;
        if (this$photoName != null ? !this$photoName.equals(other$photoName) : other$photoName != null) {
            return false;
        }
        Object this$title = this.title;
        Object other$title = other.title;
        if (this$title != null ? !this$title.equals(other$title) : other$title != null) {
            return false;
        }
        Object this$author = this.author;
        Object other$author = other.author;
        if (this$author != null ? !this$author.equals(other$author) : other$author != null) {
            return false;
        }
        Object this$xuid = this.xuid;
        Object other$xuid = other.xuid;
        if (this$xuid == null) {
            if (other$xuid == null) {
                return true;
            }
        } else if (this$xuid.equals(other$xuid)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (1 * 59) + this.inventorySlot;
        int result2 = (((result * 59) + this.pageNumber) * 59) + this.secondaryPageNumber;
        Object $action = this.action;
        int result3 = (result2 * 59) + ($action == null ? 43 : $action.hashCode());
        Object $text = this.text;
        int result4 = (result3 * 59) + ($text == null ? 43 : $text.hashCode());
        Object $photoName = this.photoName;
        int result5 = (result4 * 59) + ($photoName == null ? 43 : $photoName.hashCode());
        Object $title = this.title;
        int result6 = (result5 * 59) + ($title == null ? 43 : $title.hashCode());
        Object $author = this.author;
        int result7 = (result6 * 59) + ($author == null ? 43 : $author.hashCode());
        Object $xuid = this.xuid;
        return (result7 * 59) + ($xuid != null ? $xuid.hashCode() : 43);
    }

    public String toString() {
        return "BookEditPacket(action=" + this.action + ", inventorySlot=" + this.inventorySlot + ", pageNumber=" + this.pageNumber + ", secondaryPageNumber=" + this.secondaryPageNumber + ", text=" + this.text + ", photoName=" + this.photoName + ", title=" + this.title + ", author=" + this.author + ", xuid=" + this.xuid + ")";
    }

    public Action getAction() {
        return this.action;
    }

    public int getInventorySlot() {
        return this.inventorySlot;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getSecondaryPageNumber() {
        return this.secondaryPageNumber;
    }

    public String getText() {
        return this.text;
    }

    public String getPhotoName() {
        return this.photoName;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getXuid() {
        return this.xuid;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.BOOK_EDIT;
    }
}
