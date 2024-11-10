package org.cloudburstmc.protocol.bedrock.data.inventory.crafting;

/* loaded from: classes5.dex */
public final class RecipeIngredient {
    public static final RecipeIngredient EMPTY = new RecipeIngredient(0, 0, 0);
    private final int auxValue;
    private final int id;
    private final int stackSize;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RecipeIngredient)) {
            return false;
        }
        RecipeIngredient other = (RecipeIngredient) o;
        return getId() == other.getId() && getAuxValue() == other.getAuxValue() && getStackSize() == other.getStackSize();
    }

    public int hashCode() {
        int result = (1 * 59) + getId();
        return (((result * 59) + getAuxValue()) * 59) + getStackSize();
    }

    public String toString() {
        return "RecipeIngredient(id=" + getId() + ", auxValue=" + getAuxValue() + ", stackSize=" + getStackSize() + ")";
    }

    private RecipeIngredient(int id, int auxValue, int stackSize) {
        this.id = id;
        this.auxValue = auxValue;
        this.stackSize = stackSize;
    }

    public int getId() {
        return this.id;
    }

    public int getAuxValue() {
        return this.auxValue;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public static RecipeIngredient of(int id, int auxValue, int stackSize) {
        if (id == 0) {
            return EMPTY;
        }
        return new RecipeIngredient(id, auxValue, stackSize);
    }
}
