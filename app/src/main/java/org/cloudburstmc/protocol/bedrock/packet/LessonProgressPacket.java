package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.ee.LessonAction;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class LessonProgressPacket implements BedrockPacket {
    private LessonAction action;
    private String activityId;
    private int score;

    public void setAction(LessonAction action) {
        this.action = action;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    protected boolean canEqual(Object other) {
        return other instanceof LessonProgressPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LessonProgressPacket)) {
            return false;
        }
        LessonProgressPacket other = (LessonProgressPacket) o;
        if (!other.canEqual(this) || this.score != other.score) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$activityId = this.activityId;
        Object other$activityId = other.activityId;
        return this$activityId != null ? this$activityId.equals(other$activityId) : other$activityId == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.score;
        Object $action = this.action;
        int result2 = (result * 59) + ($action == null ? 43 : $action.hashCode());
        Object $activityId = this.activityId;
        return (result2 * 59) + ($activityId != null ? $activityId.hashCode() : 43);
    }

    public String toString() {
        return "LessonProgressPacket(action=" + this.action + ", score=" + this.score + ", activityId=" + this.activityId + ")";
    }

    public LessonAction getAction() {
        return this.action;
    }

    public int getScore() {
        return this.score;
    }

    public String getActivityId() {
        return this.activityId;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.LESSON_PROGRESS;
    }
}
