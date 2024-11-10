package org.cloudburstmc.protocol.bedrock.data.camera;

import java.awt.Color;

/* loaded from: classes5.dex */
public class CameraFadeInstruction {
    private Color color;
    private TimeData timeData;

    protected boolean canEqual(Object other) {
        return other instanceof CameraFadeInstruction;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CameraFadeInstruction)) {
            return false;
        }
        CameraFadeInstruction other = (CameraFadeInstruction) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$timeData = getTimeData();
        Object other$timeData = other.getTimeData();
        if (this$timeData != null ? !this$timeData.equals(other$timeData) : other$timeData != null) {
            return false;
        }
        Object this$color = getColor();
        Object other$color = other.getColor();
        return this$color != null ? this$color.equals(other$color) : other$color == null;
    }

    public int hashCode() {
        Object $timeData = getTimeData();
        int result = (1 * 59) + ($timeData == null ? 43 : $timeData.hashCode());
        Object $color = getColor();
        return (result * 59) + ($color != null ? $color.hashCode() : 43);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTimeData(TimeData timeData) {
        this.timeData = timeData;
    }

    public String toString() {
        return "CameraFadeInstruction(timeData=" + getTimeData() + ", color=" + getColor() + ")";
    }

    public CameraFadeInstruction(TimeData timeData, Color color) {
        this.timeData = timeData;
        this.color = color;
    }

    public CameraFadeInstruction() {
    }

    public TimeData getTimeData() {
        return this.timeData;
    }

    public Color getColor() {
        return this.color;
    }

    /* loaded from: classes5.dex */
    public static class TimeData {
        private final float fadeInTime;
        private final float fadeOutTime;
        private final float waitTime;

        public TimeData(float fadeInTime, float waitTime, float fadeOutTime) {
            this.fadeInTime = fadeInTime;
            this.waitTime = waitTime;
            this.fadeOutTime = fadeOutTime;
        }

        protected boolean canEqual(Object other) {
            return other instanceof TimeData;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TimeData)) {
                return false;
            }
            TimeData other = (TimeData) o;
            return other.canEqual(this) && Float.compare(getFadeInTime(), other.getFadeInTime()) == 0 && Float.compare(getWaitTime(), other.getWaitTime()) == 0 && Float.compare(getFadeOutTime(), other.getFadeOutTime()) == 0;
        }

        public int hashCode() {
            int result = (1 * 59) + Float.floatToIntBits(getFadeInTime());
            return (((result * 59) + Float.floatToIntBits(getWaitTime())) * 59) + Float.floatToIntBits(getFadeOutTime());
        }

        public String toString() {
            return "CameraFadeInstruction.TimeData(fadeInTime=" + getFadeInTime() + ", waitTime=" + getWaitTime() + ", fadeOutTime=" + getFadeOutTime() + ")";
        }

        public float getFadeInTime() {
            return this.fadeInTime;
        }

        public float getWaitTime() {
            return this.waitTime;
        }

        public float getFadeOutTime() {
            return this.fadeOutTime;
        }
    }
}
