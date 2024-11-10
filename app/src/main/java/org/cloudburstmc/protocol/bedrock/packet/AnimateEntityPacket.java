package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AnimateEntityPacket implements BedrockPacket {
    private String animation;
    private float blendOutTime;
    private String controller;
    private String nextState;
    private final LongList runtimeEntityIds = new LongArrayList();
    private String stopExpression;
    private int stopExpressionVersion;

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setBlendOutTime(float blendOutTime) {
        this.blendOutTime = blendOutTime;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public void setStopExpression(String stopExpression) {
        this.stopExpression = stopExpression;
    }

    public void setStopExpressionVersion(int stopExpressionVersion) {
        this.stopExpressionVersion = stopExpressionVersion;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AnimateEntityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AnimateEntityPacket)) {
            return false;
        }
        AnimateEntityPacket other = (AnimateEntityPacket) o;
        if (!other.canEqual(this) || this.stopExpressionVersion != other.stopExpressionVersion || Float.compare(this.blendOutTime, other.blendOutTime) != 0) {
            return false;
        }
        Object this$animation = this.animation;
        Object other$animation = other.animation;
        if (this$animation != null ? !this$animation.equals(other$animation) : other$animation != null) {
            return false;
        }
        Object this$nextState = this.nextState;
        Object other$nextState = other.nextState;
        if (this$nextState != null ? !this$nextState.equals(other$nextState) : other$nextState != null) {
            return false;
        }
        Object this$stopExpression = this.stopExpression;
        Object other$stopExpression = other.stopExpression;
        if (this$stopExpression != null ? !this$stopExpression.equals(other$stopExpression) : other$stopExpression != null) {
            return false;
        }
        Object this$controller = this.controller;
        Object other$controller = other.controller;
        if (this$controller != null ? !this$controller.equals(other$controller) : other$controller != null) {
            return false;
        }
        Object this$runtimeEntityIds = this.runtimeEntityIds;
        Object other$runtimeEntityIds = other.runtimeEntityIds;
        return this$runtimeEntityIds != null ? this$runtimeEntityIds.equals(other$runtimeEntityIds) : other$runtimeEntityIds == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.stopExpressionVersion;
        int result2 = (result * 59) + Float.floatToIntBits(this.blendOutTime);
        Object $animation = this.animation;
        int result3 = (result2 * 59) + ($animation == null ? 43 : $animation.hashCode());
        Object $nextState = this.nextState;
        int result4 = (result3 * 59) + ($nextState == null ? 43 : $nextState.hashCode());
        Object $stopExpression = this.stopExpression;
        int result5 = (result4 * 59) + ($stopExpression == null ? 43 : $stopExpression.hashCode());
        Object $controller = this.controller;
        int result6 = (result5 * 59) + ($controller == null ? 43 : $controller.hashCode());
        Object $runtimeEntityIds = this.runtimeEntityIds;
        return (result6 * 59) + ($runtimeEntityIds != null ? $runtimeEntityIds.hashCode() : 43);
    }

    public String toString() {
        return "AnimateEntityPacket(animation=" + this.animation + ", nextState=" + this.nextState + ", stopExpression=" + this.stopExpression + ", stopExpressionVersion=" + this.stopExpressionVersion + ", controller=" + this.controller + ", blendOutTime=" + this.blendOutTime + ", runtimeEntityIds=" + this.runtimeEntityIds + ")";
    }

    public String getAnimation() {
        return this.animation;
    }

    public String getNextState() {
        return this.nextState;
    }

    public String getStopExpression() {
        return this.stopExpression;
    }

    public int getStopExpressionVersion() {
        return this.stopExpressionVersion;
    }

    public String getController() {
        return this.controller;
    }

    public float getBlendOutTime() {
        return this.blendOutTime;
    }

    public LongList getRuntimeEntityIds() {
        return this.runtimeEntityIds;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ANIMATE_ENTITY;
    }
}
