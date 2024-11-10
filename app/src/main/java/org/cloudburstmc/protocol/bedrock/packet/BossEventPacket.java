package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class BossEventPacket implements BedrockPacket {
    private Action action;
    private long bossUniqueEntityId;
    private int color;
    private int darkenSky;
    private float healthPercentage;
    private int overlay;
    private long playerUniqueEntityId;
    private String title;

    /* loaded from: classes5.dex */
    public enum Action {
        CREATE,
        REGISTER_PLAYER,
        REMOVE,
        UNREGISTER_PLAYER,
        UPDATE_PERCENTAGE,
        UPDATE_NAME,
        UPDATE_PROPERTIES,
        UPDATE_STYLE,
        QUERY
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setBossUniqueEntityId(long bossUniqueEntityId) {
        this.bossUniqueEntityId = bossUniqueEntityId;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDarkenSky(int darkenSky) {
        this.darkenSky = darkenSky;
    }

    public void setHealthPercentage(float healthPercentage) {
        this.healthPercentage = healthPercentage;
    }

    public void setOverlay(int overlay) {
        this.overlay = overlay;
    }

    public void setPlayerUniqueEntityId(long playerUniqueEntityId) {
        this.playerUniqueEntityId = playerUniqueEntityId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BossEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BossEventPacket)) {
            return false;
        }
        BossEventPacket other = (BossEventPacket) o;
        if (!other.canEqual(this) || this.bossUniqueEntityId != other.bossUniqueEntityId || this.playerUniqueEntityId != other.playerUniqueEntityId || Float.compare(this.healthPercentage, other.healthPercentage) != 0 || this.darkenSky != other.darkenSky || this.color != other.color || this.overlay != other.overlay) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$title = this.title;
        Object other$title = other.title;
        return this$title != null ? this$title.equals(other$title) : other$title == null;
    }

    public int hashCode() {
        long $bossUniqueEntityId = this.bossUniqueEntityId;
        int result = (1 * 59) + ((int) (($bossUniqueEntityId >>> 32) ^ $bossUniqueEntityId));
        long $playerUniqueEntityId = this.playerUniqueEntityId;
        int result2 = (((((((((result * 59) + ((int) (($playerUniqueEntityId >>> 32) ^ $playerUniqueEntityId))) * 59) + Float.floatToIntBits(this.healthPercentage)) * 59) + this.darkenSky) * 59) + this.color) * 59) + this.overlay;
        Object $action = this.action;
        int result3 = (result2 * 59) + ($action == null ? 43 : $action.hashCode());
        Object $title = this.title;
        return (result3 * 59) + ($title != null ? $title.hashCode() : 43);
    }

    public String toString() {
        return "BossEventPacket(bossUniqueEntityId=" + this.bossUniqueEntityId + ", action=" + this.action + ", playerUniqueEntityId=" + this.playerUniqueEntityId + ", title=" + this.title + ", healthPercentage=" + this.healthPercentage + ", darkenSky=" + this.darkenSky + ", color=" + this.color + ", overlay=" + this.overlay + ")";
    }

    public long getBossUniqueEntityId() {
        return this.bossUniqueEntityId;
    }

    public Action getAction() {
        return this.action;
    }

    public long getPlayerUniqueEntityId() {
        return this.playerUniqueEntityId;
    }

    public String getTitle() {
        return this.title;
    }

    public float getHealthPercentage() {
        return this.healthPercentage;
    }

    public int getDarkenSky() {
        return this.darkenSky;
    }

    public int getColor() {
        return this.color;
    }

    public int getOverlay() {
        return this.overlay;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.BOSS_EVENT;
    }
}
