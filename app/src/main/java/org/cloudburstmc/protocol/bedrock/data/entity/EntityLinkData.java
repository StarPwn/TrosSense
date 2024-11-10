package org.cloudburstmc.protocol.bedrock.data.entity;

/* loaded from: classes5.dex */
public final class EntityLinkData {
    private final long from;
    private final boolean immediate;
    private final boolean riderInitiated;
    private final long to;
    private final Type type;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityLinkData)) {
            return false;
        }
        EntityLinkData other = (EntityLinkData) o;
        if (getFrom() != other.getFrom() || getTo() != other.getTo() || isImmediate() != other.isImmediate() || isRiderInitiated() != other.isRiderInitiated()) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type != null ? this$type.equals(other$type) : other$type == null;
    }

    public int hashCode() {
        long $from = getFrom();
        int result = (1 * 59) + ((int) (($from >>> 32) ^ $from));
        long $to = getTo();
        int result2 = ((((result * 59) + ((int) (($to >>> 32) ^ $to))) * 59) + (isImmediate() ? 79 : 97)) * 59;
        int i = isRiderInitiated() ? 79 : 97;
        Object $type = getType();
        return ((result2 + i) * 59) + ($type == null ? 43 : $type.hashCode());
    }

    public String toString() {
        return "EntityLinkData(from=" + getFrom() + ", to=" + getTo() + ", type=" + getType() + ", immediate=" + isImmediate() + ", riderInitiated=" + isRiderInitiated() + ")";
    }

    public EntityLinkData(long from, long to, Type type, boolean immediate, boolean riderInitiated) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.immediate = immediate;
        this.riderInitiated = riderInitiated;
    }

    public long getFrom() {
        return this.from;
    }

    public long getTo() {
        return this.to;
    }

    public Type getType() {
        return this.type;
    }

    public boolean isImmediate() {
        return this.immediate;
    }

    public boolean isRiderInitiated() {
        return this.riderInitiated;
    }

    @Deprecated
    public EntityLinkData(long from, long to, Type type, boolean immediate) {
        this(from, to, type, immediate, false);
    }

    /* loaded from: classes5.dex */
    public enum Type {
        REMOVE,
        RIDER,
        PASSENGER;

        private static final Type[] VALUES = values();

        public static Type byId(int id) {
            if (id >= 0 && id < VALUES.length) {
                return VALUES[id];
            }
            throw new UnsupportedOperationException("Unknown EntityLinkData.Type ID: " + id);
        }
    }
}
