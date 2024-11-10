package org.cloudburstmc.protocol.bedrock.data.inventory;

import java.util.List;

/* loaded from: classes5.dex */
public final class EnchantOptionData {
    private final int cost;
    private final String enchantName;
    private final int enchantNetId;
    private final List<EnchantData> enchants0;
    private final List<EnchantData> enchants1;
    private final List<EnchantData> enchants2;
    private final int primarySlot;

    public EnchantOptionData(int cost, int primarySlot, List<EnchantData> enchants0, List<EnchantData> enchants1, List<EnchantData> enchants2, String enchantName, int enchantNetId) {
        this.cost = cost;
        this.primarySlot = primarySlot;
        this.enchants0 = enchants0;
        this.enchants1 = enchants1;
        this.enchants2 = enchants2;
        this.enchantName = enchantName;
        this.enchantNetId = enchantNetId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EnchantOptionData)) {
            return false;
        }
        EnchantOptionData other = (EnchantOptionData) o;
        if (getCost() != other.getCost() || getPrimarySlot() != other.getPrimarySlot() || getEnchantNetId() != other.getEnchantNetId()) {
            return false;
        }
        Object this$enchants0 = getEnchants0();
        Object other$enchants0 = other.getEnchants0();
        if (this$enchants0 != null ? !this$enchants0.equals(other$enchants0) : other$enchants0 != null) {
            return false;
        }
        Object this$enchants1 = getEnchants1();
        Object other$enchants1 = other.getEnchants1();
        if (this$enchants1 != null ? !this$enchants1.equals(other$enchants1) : other$enchants1 != null) {
            return false;
        }
        Object this$enchants2 = getEnchants2();
        Object other$enchants2 = other.getEnchants2();
        if (this$enchants2 != null ? !this$enchants2.equals(other$enchants2) : other$enchants2 != null) {
            return false;
        }
        Object this$enchantName = getEnchantName();
        Object other$enchantName = other.getEnchantName();
        return this$enchantName != null ? this$enchantName.equals(other$enchantName) : other$enchantName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getCost();
        int result2 = (((result * 59) + getPrimarySlot()) * 59) + getEnchantNetId();
        Object $enchants0 = getEnchants0();
        int result3 = (result2 * 59) + ($enchants0 == null ? 43 : $enchants0.hashCode());
        Object $enchants1 = getEnchants1();
        int result4 = (result3 * 59) + ($enchants1 == null ? 43 : $enchants1.hashCode());
        Object $enchants2 = getEnchants2();
        int result5 = (result4 * 59) + ($enchants2 == null ? 43 : $enchants2.hashCode());
        Object $enchantName = getEnchantName();
        return (result5 * 59) + ($enchantName != null ? $enchantName.hashCode() : 43);
    }

    public String toString() {
        return "EnchantOptionData(cost=" + getCost() + ", primarySlot=" + getPrimarySlot() + ", enchants0=" + getEnchants0() + ", enchants1=" + getEnchants1() + ", enchants2=" + getEnchants2() + ", enchantName=" + getEnchantName() + ", enchantNetId=" + getEnchantNetId() + ")";
    }

    public int getCost() {
        return this.cost;
    }

    public int getPrimarySlot() {
        return this.primarySlot;
    }

    public List<EnchantData> getEnchants0() {
        return this.enchants0;
    }

    public List<EnchantData> getEnchants1() {
        return this.enchants1;
    }

    public List<EnchantData> getEnchants2() {
        return this.enchants2;
    }

    public String getEnchantName() {
        return this.enchantName;
    }

    public int getEnchantNetId() {
        return this.enchantNetId;
    }
}
