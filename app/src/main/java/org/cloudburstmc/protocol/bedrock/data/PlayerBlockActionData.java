package org.cloudburstmc.protocol.bedrock.data;

import org.cloudburstmc.math.vector.Vector3i;

/* loaded from: classes5.dex */
public class PlayerBlockActionData {
    PlayerActionType action;
    Vector3i blockPosition;
    int face;

    protected boolean canEqual(Object other) {
        return other instanceof PlayerBlockActionData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerBlockActionData)) {
            return false;
        }
        PlayerBlockActionData other = (PlayerBlockActionData) o;
        if (!other.canEqual(this) || getFace() != other.getFace()) {
            return false;
        }
        Object this$action = getAction();
        Object other$action = other.getAction();
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$blockPosition = getBlockPosition();
        Object other$blockPosition = other.getBlockPosition();
        return this$blockPosition != null ? this$blockPosition.equals(other$blockPosition) : other$blockPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getFace();
        Object $action = getAction();
        int result2 = (result * 59) + ($action == null ? 43 : $action.hashCode());
        Object $blockPosition = getBlockPosition();
        return (result2 * 59) + ($blockPosition != null ? $blockPosition.hashCode() : 43);
    }

    public void setAction(PlayerActionType action) {
        this.action = action;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public String toString() {
        return "PlayerBlockActionData(action=" + getAction() + ", blockPosition=" + getBlockPosition() + ", face=" + getFace() + ")";
    }

    public PlayerActionType getAction() {
        return this.action;
    }

    public Vector3i getBlockPosition() {
        return this.blockPosition;
    }

    public int getFace() {
        return this.face;
    }
}
