package org.cloudburstmc.netty.channel.raknet;

/* loaded from: classes5.dex */
public enum RakReliability {
    UNRELIABLE(false, false, false, false),
    UNRELIABLE_SEQUENCED(false, false, true, false),
    RELIABLE(true, false, false, false),
    RELIABLE_ORDERED(true, true, false, false),
    RELIABLE_SEQUENCED(true, false, true, false),
    UNRELIABLE_WITH_ACK_RECEIPT(false, false, false, true),
    UNRELIABLE_SEQUENCED_WITH_ACK_RECEIPT(false, false, true, true),
    RELIABLE_WITH_ACK_RECEIPT(true, false, false, true),
    RELIABLE_ORDERED_WITH_ACK_RECEIPT(true, true, false, true),
    RELIABLE_SEQUENCED_WITH_ACK_RECEIPT(true, false, true, true);

    private static final RakReliability[] VALUES = values();
    final boolean ordered;
    final boolean reliable;
    final boolean sequenced;
    final int size;
    final boolean withAckReceipt;

    RakReliability(boolean reliable, boolean ordered, boolean sequenced, boolean withAckReceipt) {
        this.reliable = reliable;
        this.ordered = ordered;
        this.sequenced = sequenced;
        this.withAckReceipt = withAckReceipt;
        int size = this.reliable ? 0 + 3 : 0;
        size = this.sequenced ? size + 3 : size;
        this.size = this.ordered ? size + 4 : size;
    }

    public static RakReliability fromId(int id) {
        if (id < 0 || id > 7) {
            return null;
        }
        return VALUES[id];
    }

    public int getSize() {
        return this.size;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public boolean isReliable() {
        return this.reliable;
    }

    public boolean isSequenced() {
        return this.sequenced;
    }

    public boolean isWithAckReceipt() {
        return this.withAckReceipt;
    }
}
