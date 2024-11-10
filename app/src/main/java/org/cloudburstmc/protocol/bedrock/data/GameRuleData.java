package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public final class GameRuleData<T> {
    private final boolean editable;
    private final String name;
    private final T value;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameRuleData)) {
            return false;
        }
        GameRuleData<?> other = (GameRuleData) o;
        if (isEditable() != other.isEditable()) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$value = getValue();
        Object other$value = other.getValue();
        return this$value != null ? this$value.equals(other$value) : other$value == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isEditable() ? 79 : 97);
        Object $name = getName();
        int result2 = (result * 59) + ($name == null ? 43 : $name.hashCode());
        Object $value = getValue();
        return (result2 * 59) + ($value != null ? $value.hashCode() : 43);
    }

    public GameRuleData(String name, boolean editable, T value) {
        this.name = name;
        this.editable = editable;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public T getValue() {
        return this.value;
    }

    public GameRuleData(String name, T value) {
        this.name = name;
        this.value = value;
        this.editable = false;
    }

    public String toString() {
        return this.name + '=' + this.value;
    }
}
