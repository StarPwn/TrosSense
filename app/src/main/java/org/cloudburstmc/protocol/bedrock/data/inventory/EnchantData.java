package org.cloudburstmc.protocol.bedrock.data.inventory;

/* loaded from: classes5.dex */
public final class EnchantData {
    private final int level;
    private final int type;

    public EnchantData(int type, int level) {
        this.type = type;
        this.level = level;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EnchantData)) {
            return false;
        }
        EnchantData other = (EnchantData) o;
        return getType() == other.getType() && getLevel() == other.getLevel();
    }

    public int hashCode() {
        int result = (1 * 59) + getType();
        return (result * 59) + getLevel();
    }

    public String toString() {
        return "EnchantData(type=" + getType() + ", level=" + getLevel() + ")";
    }

    public int getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }
}
