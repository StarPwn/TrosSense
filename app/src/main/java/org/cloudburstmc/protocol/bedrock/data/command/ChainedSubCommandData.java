package org.cloudburstmc.protocol.bedrock.data.command;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class ChainedSubCommandData {
    private final String name;
    private final List<Value> values = new ObjectArrayList();

    public ChainedSubCommandData(String name) {
        this.name = name;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ChainedSubCommandData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChainedSubCommandData)) {
            return false;
        }
        ChainedSubCommandData other = (ChainedSubCommandData) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$name = getName();
        Object other$name = other.getName();
        if (this$name != null ? !this$name.equals(other$name) : other$name != null) {
            return false;
        }
        Object this$values = getValues();
        Object other$values = other.getValues();
        return this$values != null ? this$values.equals(other$values) : other$values == null;
    }

    public int hashCode() {
        Object $name = getName();
        int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
        Object $values = getValues();
        return (result * 59) + ($values != null ? $values.hashCode() : 43);
    }

    public String toString() {
        return "ChainedSubCommandData(name=" + getName() + ", values=" + getValues() + ")";
    }

    public String getName() {
        return this.name;
    }

    public List<Value> getValues() {
        return this.values;
    }

    /* loaded from: classes5.dex */
    public static class Value {
        private final String first;
        private final String second;

        public Value(String first, String second) {
            this.first = first;
            this.second = second;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Value;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Value)) {
                return false;
            }
            Value other = (Value) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$first = getFirst();
            Object other$first = other.getFirst();
            if (this$first != null ? !this$first.equals(other$first) : other$first != null) {
                return false;
            }
            Object this$second = getSecond();
            Object other$second = other.getSecond();
            return this$second != null ? this$second.equals(other$second) : other$second == null;
        }

        public int hashCode() {
            Object $first = getFirst();
            int result = (1 * 59) + ($first == null ? 43 : $first.hashCode());
            Object $second = getSecond();
            return (result * 59) + ($second != null ? $second.hashCode() : 43);
        }

        public String toString() {
            return "ChainedSubCommandData.Value(first=" + getFirst() + ", second=" + getSecond() + ")";
        }

        public String getFirst() {
            return this.first;
        }

        public String getSecond() {
            return this.second;
        }
    }
}
