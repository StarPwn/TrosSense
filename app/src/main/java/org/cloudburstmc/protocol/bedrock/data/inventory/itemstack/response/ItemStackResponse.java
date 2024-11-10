package org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response;

import java.util.List;

/* loaded from: classes5.dex */
public final class ItemStackResponse {
    private final List<ItemStackResponseContainer> containers;
    private final int requestId;
    private final ItemStackResponseStatus result;

    @Deprecated
    private final boolean success;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackResponse)) {
            return false;
        }
        ItemStackResponse other = (ItemStackResponse) o;
        if (isSuccess() != other.isSuccess() || getRequestId() != other.getRequestId()) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result != null ? !this$result.equals(other$result) : other$result != null) {
            return false;
        }
        Object this$containers = getContainers();
        Object other$containers = other.getContainers();
        return this$containers != null ? this$containers.equals(other$containers) : other$containers == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isSuccess() ? 79 : 97);
        int result2 = (result * 59) + getRequestId();
        Object $result = getResult();
        int result3 = (result2 * 59) + ($result == null ? 43 : $result.hashCode());
        Object $containers = getContainers();
        return (result3 * 59) + ($containers != null ? $containers.hashCode() : 43);
    }

    public String toString() {
        return "ItemStackResponse(result=" + getResult() + ", requestId=" + getRequestId() + ", containers=" + getContainers() + ")";
    }

    @Deprecated
    public boolean isSuccess() {
        return this.success;
    }

    public ItemStackResponseStatus getResult() {
        return this.result;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public List<ItemStackResponseContainer> getContainers() {
        return this.containers;
    }

    @Deprecated
    public ItemStackResponse(boolean success, int requestId, List<ItemStackResponseContainer> containers) {
        this.success = success;
        this.requestId = requestId;
        this.containers = containers;
        this.result = success ? ItemStackResponseStatus.OK : ItemStackResponseStatus.ERROR;
    }

    public ItemStackResponse(ItemStackResponseStatus result, int requestId, List<ItemStackResponseContainer> containers) {
        this.result = result;
        this.requestId = requestId;
        this.containers = containers;
        this.success = false;
    }
}
