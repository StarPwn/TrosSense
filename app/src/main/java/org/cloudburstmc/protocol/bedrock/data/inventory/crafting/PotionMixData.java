package org.cloudburstmc.protocol.bedrock.data.inventory.crafting;

/* loaded from: classes5.dex */
public final class PotionMixData {
    private final int inputId;
    private final int inputMeta;
    private final int outputId;
    private final int outputMeta;
    private final int reagentId;
    private final int reagentMeta;

    public PotionMixData(int inputId, int inputMeta, int reagentId, int reagentMeta, int outputId, int outputMeta) {
        this.inputId = inputId;
        this.inputMeta = inputMeta;
        this.reagentId = reagentId;
        this.reagentMeta = reagentMeta;
        this.outputId = outputId;
        this.outputMeta = outputMeta;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PotionMixData)) {
            return false;
        }
        PotionMixData other = (PotionMixData) o;
        return getInputId() == other.getInputId() && getInputMeta() == other.getInputMeta() && getReagentId() == other.getReagentId() && getReagentMeta() == other.getReagentMeta() && getOutputId() == other.getOutputId() && getOutputMeta() == other.getOutputMeta();
    }

    public int hashCode() {
        int result = (1 * 59) + getInputId();
        return (((((((((result * 59) + getInputMeta()) * 59) + getReagentId()) * 59) + getReagentMeta()) * 59) + getOutputId()) * 59) + getOutputMeta();
    }

    public String toString() {
        return "PotionMixData(inputId=" + getInputId() + ", inputMeta=" + getInputMeta() + ", reagentId=" + getReagentId() + ", reagentMeta=" + getReagentMeta() + ", outputId=" + getOutputId() + ", outputMeta=" + getOutputMeta() + ")";
    }

    public int getInputId() {
        return this.inputId;
    }

    public int getInputMeta() {
        return this.inputMeta;
    }

    public int getReagentId() {
        return this.reagentId;
    }

    public int getReagentMeta() {
        return this.reagentMeta;
    }

    public int getOutputId() {
        return this.outputId;
    }

    public int getOutputMeta() {
        return this.outputMeta;
    }
}
