package org.cloudburstmc.protocol.bedrock.data.skin;

/* loaded from: classes5.dex */
public enum AnimationExpressionType {
    LINEAR,
    BLINKING;

    private static final AnimationExpressionType[] VALUES = values();

    public static AnimationExpressionType from(int id) {
        return VALUES[id];
    }
}
