package org.cloudburstmc.protocol.bedrock.data.inventory.crafting;

/* loaded from: classes5.dex */
public final class ContainerMixData {
    private final int inputId;
    private final int outputId;
    private final int reagentId;

    public ContainerMixData(int inputId, int reagentId, int outputId) {
        this.inputId = inputId;
        this.reagentId = reagentId;
        this.outputId = outputId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContainerMixData)) {
            return false;
        }
        ContainerMixData other = (ContainerMixData) o;
        return getInputId() == other.getInputId() && getReagentId() == other.getReagentId() && getOutputId() == other.getOutputId();
    }

    public int hashCode() {
        int result = (1 * 59) + getInputId();
        return (((result * 59) + getReagentId()) * 59) + getOutputId();
    }

    public String toString() {
        return "ContainerMixData(inputId=" + getInputId() + ", reagentId=" + getReagentId() + ", outputId=" + getOutputId() + ")";
    }

    public int getInputId() {
        return this.inputId;
    }

    public int getReagentId() {
        return this.reagentId;
    }

    public int getOutputId() {
        return this.outputId;
    }
}
