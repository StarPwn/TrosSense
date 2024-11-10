package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public enum BuildPlatform {
    UNDEFINED,
    GOOGLE,
    IOS,
    OSX,
    AMAZON,
    GEAR_VR,
    HOLOLENS,
    UWP,
    WIN_32,
    DEDICATED,
    TV_OS,
    SONY,
    NX,
    XBOX,
    WINDOWS_PHONE,
    LINUX;

    private static final BuildPlatform[] VALUES = values();

    public static BuildPlatform from(int id) {
        return (id <= 0 || id >= VALUES.length) ? VALUES[0] : VALUES[id];
    }
}
