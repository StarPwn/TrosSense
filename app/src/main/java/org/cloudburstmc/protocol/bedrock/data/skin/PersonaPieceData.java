package org.cloudburstmc.protocol.bedrock.data.skin;

/* loaded from: classes5.dex */
public final class PersonaPieceData {
    private final String id;
    private final boolean isDefault;
    private final String packId;
    private final String productId;
    private final String type;

    public PersonaPieceData(String id, String type, String packId, boolean isDefault, String productId) {
        this.id = id;
        this.type = type;
        this.packId = packId;
        this.isDefault = isDefault;
        this.productId = productId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersonaPieceData)) {
            return false;
        }
        PersonaPieceData other = (PersonaPieceData) o;
        if (isDefault() != other.isDefault()) {
            return false;
        }
        Object this$id = getId();
        Object other$id = other.getId();
        if (this$id != null ? !this$id.equals(other$id) : other$id != null) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        if (this$type != null ? !this$type.equals(other$type) : other$type != null) {
            return false;
        }
        Object this$packId = getPackId();
        Object other$packId = other.getPackId();
        if (this$packId != null ? !this$packId.equals(other$packId) : other$packId != null) {
            return false;
        }
        Object this$productId = getProductId();
        Object other$productId = other.getProductId();
        return this$productId != null ? this$productId.equals(other$productId) : other$productId == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isDefault() ? 79 : 97);
        Object $id = getId();
        int result2 = (result * 59) + ($id == null ? 43 : $id.hashCode());
        Object $type = getType();
        int result3 = (result2 * 59) + ($type == null ? 43 : $type.hashCode());
        Object $packId = getPackId();
        int result4 = (result3 * 59) + ($packId == null ? 43 : $packId.hashCode());
        Object $productId = getProductId();
        return (result4 * 59) + ($productId != null ? $productId.hashCode() : 43);
    }

    public String toString() {
        return "PersonaPieceData(id=" + getId() + ", type=" + getType() + ", packId=" + getPackId() + ", isDefault=" + isDefault() + ", productId=" + getProductId() + ")";
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getPackId() {
        return this.packId;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public String getProductId() {
        return this.productId;
    }
}
