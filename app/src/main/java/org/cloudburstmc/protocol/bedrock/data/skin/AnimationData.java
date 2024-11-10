package org.cloudburstmc.protocol.bedrock.data.skin;

/* loaded from: classes5.dex */
public final class AnimationData {
    private final AnimationExpressionType expressionType;
    private final float frames;
    private final ImageData image;
    private final AnimatedTextureType textureType;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AnimationData)) {
            return false;
        }
        AnimationData other = (AnimationData) o;
        if (Float.compare(getFrames(), other.getFrames()) != 0) {
            return false;
        }
        Object this$image = getImage();
        Object other$image = other.getImage();
        if (this$image != null ? !this$image.equals(other$image) : other$image != null) {
            return false;
        }
        Object this$textureType = getTextureType();
        Object other$textureType = other.getTextureType();
        if (this$textureType != null ? !this$textureType.equals(other$textureType) : other$textureType != null) {
            return false;
        }
        Object this$expressionType = getExpressionType();
        Object other$expressionType = other.getExpressionType();
        return this$expressionType != null ? this$expressionType.equals(other$expressionType) : other$expressionType == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(getFrames());
        Object $image = getImage();
        int result2 = (result * 59) + ($image == null ? 43 : $image.hashCode());
        Object $textureType = getTextureType();
        int result3 = (result2 * 59) + ($textureType == null ? 43 : $textureType.hashCode());
        Object $expressionType = getExpressionType();
        return (result3 * 59) + ($expressionType != null ? $expressionType.hashCode() : 43);
    }

    public String toString() {
        return "AnimationData(image=" + getImage() + ", textureType=" + getTextureType() + ", frames=" + getFrames() + ", expressionType=" + getExpressionType() + ")";
    }

    public AnimationData(ImageData image, AnimatedTextureType textureType, float frames, AnimationExpressionType expressionType) {
        this.image = image;
        this.textureType = textureType;
        this.frames = frames;
        this.expressionType = expressionType;
    }

    public ImageData getImage() {
        return this.image;
    }

    public AnimatedTextureType getTextureType() {
        return this.textureType;
    }

    public float getFrames() {
        return this.frames;
    }

    public AnimationExpressionType getExpressionType() {
        return this.expressionType;
    }

    public AnimationData(ImageData image, AnimatedTextureType textureType, float frames) {
        this.image = image;
        this.textureType = textureType;
        this.frames = frames;
        this.expressionType = AnimationExpressionType.LINEAR;
    }
}
