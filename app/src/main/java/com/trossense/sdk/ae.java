package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import org.cloudburstmc.protocol.bedrock.data.BlockPropertyData;
import org.cloudburstmc.protocol.bedrock.data.ChatRestrictionLevel;
import org.cloudburstmc.protocol.bedrock.data.EduSharedUriResource;
import org.cloudburstmc.protocol.bedrock.data.ExperimentData;
import org.cloudburstmc.protocol.bedrock.data.GamePublishSetting;
import org.cloudburstmc.protocol.bedrock.data.GameRuleData;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.NetworkPermissions;
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.SpawnBiomeType;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.packet.StartGamePacket;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;

/* loaded from: classes3.dex */
public class ae extends StartGamePacket {
    private static final long c = dj.a(-2430517003366813041L, -5226837866559739034L, MethodHandles.lookup().lookupClass()).a(83080268219616L);
    private static final String[] d;
    private byte[] a;
    private boolean b;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0046. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[77];
        String str = "3Q\u0013\u0000\u0017fZZ\t\u0005\n\u0017lRz\u001f\u0001\u000e\tB^r\u0014\u0005\u0003\u0004|\u0002#3Q\u0006\n\u0017sZm0\u0000\u001b\rjMv\u0005\u0014\u001b\fsZ]\u001d\u001a\f\u000eGMz\u0010\u001e\u0006\u000bb\u0002\u001b3Q\u0018\u001a\tqVo\u001d\u0014\u0016\u0000w|p\u0003\u0007\n\tdKv\u001e\u001b&\u00018\u00123Q\u0016\u001d\u0000dKz\u0015<\u0001 aVk\u001e\u0007R\u000e3Q\u0016\u001a\u0017wZq\u0005!\u0006\u0006n\u0002\u001e3Q\u0011\u0006\u0016d]s\u0018\u001b\b5i^f\u0014\u0007&\u000bqZm\u0010\u0016\u001b\fjQlL\u00123Q\u0007\u001a\u000bqVr\u00140\u0001\u0011lKf8\u0011R\u00123Q\u0016\u001a\u0016qPr3\u001c\u0000\b`q~\u001c\u0010R\u00143Q\u0007\n\u0012lQ{9\u001c\u001c\u0011jMf\"\u001c\u0015\u00008\u00183Q\u0017\u0003\nfTM\u0014\u0012\u0006\u0016qMf2\u001d\n\u0006nLj\u001cH\u00183Q\u0010\u000b\u0010f^k\u0018\u001a\u00015wP{\u0004\u0016\u001b\fjQV\u0015H\u00123Q\u0017\u0003\nfTO\u0003\u001a\u001f\u0000wKv\u0014\u0006R\u001c3Q\u0002\u0000\u0017i[K\u0014\u0018\u001f\tdKz>\u0005\u001b\fjQS\u001e\u0016\u0004\u0000a\u0002\n3Q\u0019\n\u0013`SV\u0015H\u00143Q\u0019\u0006\blKz\u0015\"\u0000\u0017i[H\u0018\u0011\u001b\r8\u001a3Q\u0011\n\u0003dJs\u0005%\u0003\u0004|Zm!\u0010\u001d\blLl\u0018\u001a\u0001X\u001a3Q\u0013\u001d\nhsp\u0012\u001e\n\u0001RPm\u001d\u0011;\u0000hOs\u0010\u0001\nX\u001c3Q\u0014\u001a\u0011mPm\u0018\u0001\u000e\u0011lIz<\u001a\u0019\u0000hZq\u00058\u0000\u0001`\u0002\u00123Q\u0002\u0000\u0017i[K\u0014\u0018\u001f\tdKz8\u0011R\u000e3Q\u0011\u0006\b`Ql\u0018\u001a\u0001,a\u0002\u00103Q\u0019\n\u0013`SX\u0010\u0018\n1|OzL\u00153Q\u001b\n\u0011rPm\u001a%\n\u0017hVl\u0002\u001c\u0000\u000bv\u0002\u000e3Q\u0012\n\u000b`M~\u0005\u001a\u001d,a\u0002\u00123Q\u0018\u001a\tqVo\u001d\u0014\u0016\u0000wx~\u001c\u0010R\u000e3Q\u0010\u0017\u0015`Mv\u001c\u0010\u0001\u0011v\u0002\u00173Q\u0010\u000b\u0010VW~\u0003\u0010\u000b0wVM\u0014\u0006\u0000\u0010w\\zL\r3Q\u001b\n\u0011mZm%\f\u001f\u00008\u000b3Q\u001d\u000e\u0017a\\p\u0003\u0010R\u00143Q\u0013\u001d\nhhp\u0003\u0019\u000b1`Ro\u001d\u0014\u001b\u00008\u001f3Q\u0010\u0017\u0015`Mv\u001c\u0010\u0001\u0011vom\u0014\u0003\u0006\npLs\b!\u0000\u0002bSz\u0015H\u00173Q\u0011\u0006\u0016d]s\u0018\u001b\b&pLk\u001e\u0018<\u000elQlL\u00183Q\u0005\u0003\u0004qYp\u0003\u0018-\u0017j^{\u0012\u0014\u001c\u0011HP{\u0014H!Q\u0014\u0001\n\u0004vZL\u0005\u0014\u001d\u0011B^r\u0014%\u000e\u0006nZkY\u0012\u000e\b`Mj\u001d\u0010\u001cX\u00113Q\u0005\u0003\u0004|Zm6\u0014\u0002\u0000QFo\u0014H\u00153Q\u0017\n\rdIv\u001e\u0007?\u0004fTS\u001e\u0016\u0004\u0000a\u0002\u000e3Q\u0002\u0000\u0017i[Z\u0015\u001c\u001b\nw\u0002\u00183Q\u0000\u001c\fkXR\u0002\u0014(\u0004hZm\u0005\u0014\b\u0016JQs\bH\u00173Q\u0001\n\u001dqJm\u0014%\u000e\u0006nLM\u0014\u0004\u001a\fwZ{L\u00123Q\u0001\u001d\u0010vKv\u001f\u0012?\tdFz\u0003\u0006R\u00143Q\u0017\u001d\nd[|\u0010\u0006\u001b\fkXK\u001e9\u000e\u000b8\u000f3Q\u0011\n\u0003dJs\u0005&\u001f\u0004rQ\"\u00113Q\u0000\u0001\ftJz4\u001b\u001b\fqFV\u0015H\u00173Q\u0006\n\u0017sZm2\u001d\u001a\u000bnkv\u0012\u001e=\u0004kXzL\u00133Q\r\r\tGMp\u0010\u0011\f\u0004vKR\u001e\u0011\nX\f3Q\u0019\n\u0013`SQ\u0010\u0018\nX\u00113Q\u0019\u0006\u0002mKq\u0018\u001b\b)`Iz\u001dH\u00113Q\u0003\u000e\u000blSs\u0010#\n\u0017vVp\u001fH\u001a3Q\u001a\u0001\t|lo\u0010\u0002\u0001\fkXI@#\u0006\ti^x\u0014\u0007\u001cX!3Q\u001c\u0001\u0013`Qk\u001e\u0007\u0006\u0000vlz\u0003\u0003\n\u0017DJk\u0019\u001a\u001d\fq^k\u0018\u0003\nX\r3Q\u0011\u0006\u0003cV|\u0004\u0019\u001b\u001c8\u001e3Q\u0016\u0003\f`Qk\"\u001c\u000b\u0000BZq\u0014\u0007\u000e\u0011lPq4\u001b\u000e\u0007iZ{L\u00143Q\u0017\u0000\u000bpL\\\u0019\u0010\u001c\u0011@Q~\u0013\u0019\n\u00018\f3Q\u0007\u000e\fksz\u0007\u0010\u0003X\u000f3Q\u0006\n\u0017sZm4\u001b\b\fkZ\"\u00123Q\u0010\u0001\u0006m^q\u0005\u0018\n\u000bqlz\u0014\u0011R\u00183Q\u0017\u0003\nfTQ\u0014\u0001\u0018\nwTV\u0015\u0006'\u0004vWz\u0015H\u00133Q\u0011\u000e\u001cFF|\u001d\u0010<\u0011jOK\u0018\u0018\nX\u00153Q\u0005\u0003\u0004|Zm!\u0007\u0000\u0015`Mk\b1\u000e\u0011d\u0002\u00153Q\u0007\n\u0016jJm\u0012\u0010?\u0004fTS\u001e\u0016\u0004\u0000a\u0002\u00113Q\u0010\u0002\nqZ\\\u0019\u0014\u001b(pKz\u0015H\u00123Q\u0006\u001b\u0004wKv\u001f\u00128\fqWR\u0010\u0005R\u00193Q\u0005\u001d\u0000hVj\u001c\"\u0000\u0017i[K\u0014\u0018\u001f\tdKz8\u0011R\u00113Q\u0006\u001f\u0004rQ]\u0018\u001a\u0002\u0000QFo\u0014H\u00143Q\u0011\u0006\u0016d]s\u0018\u001b\b5`Ml\u001e\u001b\u000e\u00168\u00153Q\u0010\u000b\u0010CZ~\u0005\u0000\u001d\u0000vzq\u0010\u0017\u0003\u0000a\u0002\u00153Q\u0010\u0017\u0015jMk\u0014\u0011)\u0017jRZ\u0015\u001c\u001b\nw\u0002!3Q\u0005\u0003\u0004qYp\u0003\u0018#\nfTz\u00156\u0000\u000bqZq\u00056\u0000\u000bcVm\u001c\u0010\u000bX\u00123Q\u0016\u0000\bh^q\u0015\u0006*\u000bd]s\u0014\u0011R\u00133Q\u0010\u000b\u0010@[v\u0005\u001c\u0000\u000bJYy\u0014\u0007\u001cX\b3Q\u0001\u001d\fdS\"\u000b3Q\u0007\u0000\u0011dKv\u001e\u001bR\u00113Q\u0005\u0003\u0004|Zm!\u001a\u001c\fqVp\u001fH\u00073Q\u0001\n\bu\u0002\u00173Q\u0014\f\rlZi\u0014\u0018\n\u000bqL[\u0018\u0006\u000e\u0007iZ{L\u00153Q\u0019\u0006\blKz\u0015\"\u0000\u0017i[W\u0014\u001c\b\rq\u0002";
        int length = "3Q\u0013\u0000\u0017fZZ\t\u0005\n\u0017lRz\u001f\u0001\u000e\tB^r\u0014\u0005\u0003\u0004|\u0002#3Q\u0006\n\u0017sZm0\u0000\u001b\rjMv\u0005\u0014\u001b\fsZ]\u001d\u001a\f\u000eGMz\u0010\u001e\u0006\u000bb\u0002\u001b3Q\u0018\u001a\tqVo\u001d\u0014\u0016\u0000w|p\u0003\u0007\n\tdKv\u001e\u001b&\u00018\u00123Q\u0016\u001d\u0000dKz\u0015<\u0001 aVk\u001e\u0007R\u000e3Q\u0016\u001a\u0017wZq\u0005!\u0006\u0006n\u0002\u001e3Q\u0011\u0006\u0016d]s\u0018\u001b\b5i^f\u0014\u0007&\u000bqZm\u0010\u0016\u001b\fjQlL\u00123Q\u0007\u001a\u000bqVr\u00140\u0001\u0011lKf8\u0011R\u00123Q\u0016\u001a\u0016qPr3\u001c\u0000\b`q~\u001c\u0010R\u00143Q\u0007\n\u0012lQ{9\u001c\u001c\u0011jMf\"\u001c\u0015\u00008\u00183Q\u0017\u0003\nfTM\u0014\u0012\u0006\u0016qMf2\u001d\n\u0006nLj\u001cH\u00183Q\u0010\u000b\u0010f^k\u0018\u001a\u00015wP{\u0004\u0016\u001b\fjQV\u0015H\u00123Q\u0017\u0003\nfTO\u0003\u001a\u001f\u0000wKv\u0014\u0006R\u001c3Q\u0002\u0000\u0017i[K\u0014\u0018\u001f\tdKz>\u0005\u001b\fjQS\u001e\u0016\u0004\u0000a\u0002\n3Q\u0019\n\u0013`SV\u0015H\u00143Q\u0019\u0006\blKz\u0015\"\u0000\u0017i[H\u0018\u0011\u001b\r8\u001a3Q\u0011\n\u0003dJs\u0005%\u0003\u0004|Zm!\u0010\u001d\blLl\u0018\u001a\u0001X\u001a3Q\u0013\u001d\nhsp\u0012\u001e\n\u0001RPm\u001d\u0011;\u0000hOs\u0010\u0001\nX\u001c3Q\u0014\u001a\u0011mPm\u0018\u0001\u000e\u0011lIz<\u001a\u0019\u0000hZq\u00058\u0000\u0001`\u0002\u00123Q\u0002\u0000\u0017i[K\u0014\u0018\u001f\tdKz8\u0011R\u000e3Q\u0011\u0006\b`Ql\u0018\u001a\u0001,a\u0002\u00103Q\u0019\n\u0013`SX\u0010\u0018\n1|OzL\u00153Q\u001b\n\u0011rPm\u001a%\n\u0017hVl\u0002\u001c\u0000\u000bv\u0002\u000e3Q\u0012\n\u000b`M~\u0005\u001a\u001d,a\u0002\u00123Q\u0018\u001a\tqVo\u001d\u0014\u0016\u0000wx~\u001c\u0010R\u000e3Q\u0010\u0017\u0015`Mv\u001c\u0010\u0001\u0011v\u0002\u00173Q\u0010\u000b\u0010VW~\u0003\u0010\u000b0wVM\u0014\u0006\u0000\u0010w\\zL\r3Q\u001b\n\u0011mZm%\f\u001f\u00008\u000b3Q\u001d\u000e\u0017a\\p\u0003\u0010R\u00143Q\u0013\u001d\nhhp\u0003\u0019\u000b1`Ro\u001d\u0014\u001b\u00008\u001f3Q\u0010\u0017\u0015`Mv\u001c\u0010\u0001\u0011vom\u0014\u0003\u0006\npLs\b!\u0000\u0002bSz\u0015H\u00173Q\u0011\u0006\u0016d]s\u0018\u001b\b&pLk\u001e\u0018<\u000elQlL\u00183Q\u0005\u0003\u0004qYp\u0003\u0018-\u0017j^{\u0012\u0014\u001c\u0011HP{\u0014H!Q\u0014\u0001\n\u0004vZL\u0005\u0014\u001d\u0011B^r\u0014%\u000e\u0006nZkY\u0012\u000e\b`Mj\u001d\u0010\u001cX\u00113Q\u0005\u0003\u0004|Zm6\u0014\u0002\u0000QFo\u0014H\u00153Q\u0017\n\rdIv\u001e\u0007?\u0004fTS\u001e\u0016\u0004\u0000a\u0002\u000e3Q\u0002\u0000\u0017i[Z\u0015\u001c\u001b\nw\u0002\u00183Q\u0000\u001c\fkXR\u0002\u0014(\u0004hZm\u0005\u0014\b\u0016JQs\bH\u00173Q\u0001\n\u001dqJm\u0014%\u000e\u0006nLM\u0014\u0004\u001a\fwZ{L\u00123Q\u0001\u001d\u0010vKv\u001f\u0012?\tdFz\u0003\u0006R\u00143Q\u0017\u001d\nd[|\u0010\u0006\u001b\fkXK\u001e9\u000e\u000b8\u000f3Q\u0011\n\u0003dJs\u0005&\u001f\u0004rQ\"\u00113Q\u0000\u0001\ftJz4\u001b\u001b\fqFV\u0015H\u00173Q\u0006\n\u0017sZm2\u001d\u001a\u000bnkv\u0012\u001e=\u0004kXzL\u00133Q\r\r\tGMp\u0010\u0011\f\u0004vKR\u001e\u0011\nX\f3Q\u0019\n\u0013`SQ\u0010\u0018\nX\u00113Q\u0019\u0006\u0002mKq\u0018\u001b\b)`Iz\u001dH\u00113Q\u0003\u000e\u000blSs\u0010#\n\u0017vVp\u001fH\u001a3Q\u001a\u0001\t|lo\u0010\u0002\u0001\fkXI@#\u0006\ti^x\u0014\u0007\u001cX!3Q\u001c\u0001\u0013`Qk\u001e\u0007\u0006\u0000vlz\u0003\u0003\n\u0017DJk\u0019\u001a\u001d\fq^k\u0018\u0003\nX\r3Q\u0011\u0006\u0003cV|\u0004\u0019\u001b\u001c8\u001e3Q\u0016\u0003\f`Qk\"\u001c\u000b\u0000BZq\u0014\u0007\u000e\u0011lPq4\u001b\u000e\u0007iZ{L\u00143Q\u0017\u0000\u000bpL\\\u0019\u0010\u001c\u0011@Q~\u0013\u0019\n\u00018\f3Q\u0007\u000e\fksz\u0007\u0010\u0003X\u000f3Q\u0006\n\u0017sZm4\u001b\b\fkZ\"\u00123Q\u0010\u0001\u0006m^q\u0005\u0018\n\u000bqlz\u0014\u0011R\u00183Q\u0017\u0003\nfTQ\u0014\u0001\u0018\nwTV\u0015\u0006'\u0004vWz\u0015H\u00133Q\u0011\u000e\u001cFF|\u001d\u0010<\u0011jOK\u0018\u0018\nX\u00153Q\u0005\u0003\u0004|Zm!\u0007\u0000\u0015`Mk\b1\u000e\u0011d\u0002\u00153Q\u0007\n\u0016jJm\u0012\u0010?\u0004fTS\u001e\u0016\u0004\u0000a\u0002\u00113Q\u0010\u0002\nqZ\\\u0019\u0014\u001b(pKz\u0015H\u00123Q\u0006\u001b\u0004wKv\u001f\u00128\fqWR\u0010\u0005R\u00193Q\u0005\u001d\u0000hVj\u001c\"\u0000\u0017i[K\u0014\u0018\u001f\tdKz8\u0011R\u00113Q\u0006\u001f\u0004rQ]\u0018\u001a\u0002\u0000QFo\u0014H\u00143Q\u0011\u0006\u0016d]s\u0018\u001b\b5`Ml\u001e\u001b\u000e\u00168\u00153Q\u0010\u000b\u0010CZ~\u0005\u0000\u001d\u0000vzq\u0010\u0017\u0003\u0000a\u0002\u00153Q\u0010\u0017\u0015jMk\u0014\u0011)\u0017jRZ\u0015\u001c\u001b\nw\u0002!3Q\u0005\u0003\u0004qYp\u0003\u0018#\nfTz\u00156\u0000\u000bqZq\u00056\u0000\u000bcVm\u001c\u0010\u000bX\u00123Q\u0016\u0000\bh^q\u0015\u0006*\u000bd]s\u0014\u0011R\u00133Q\u0010\u000b\u0010@[v\u0005\u001c\u0000\u000bJYy\u0014\u0007\u001cX\b3Q\u0001\u001d\fdS\"\u000b3Q\u0007\u0000\u0011dKv\u001e\u001bR\u00113Q\u0005\u0003\u0004|Zm!\u001a\u001c\fqVp\u001fH\u00073Q\u0001\n\bu\u0002\u00173Q\u0014\f\rlZi\u0014\u0018\n\u000bqL[\u0018\u0006\u000e\u0007iZ{L\u00153Q\u0019\u0006\blKz\u0015\"\u0000\u0017i[W\u0014\u001c\b\rq\u0002".length();
        char c2 = 28;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 32;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "R0gka\u0000c\u0017R0wfe\u0010\f\u001bc`|m\u0007*\u0017\u007fzBa\u0012;\u0012-";
                        length = "R0gka\u0000c\u0017R0wfe\u0010\f\u001bc`|m\u0007*\u0017\u007fzBa\u0012;\u0012-".length();
                        c2 = 7;
                        i2 = -1;
                        i3 = i;
                        i5 = 65;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c2;
                if (i2 >= length) {
                    d = strArr;
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 65;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 63;
                    break;
                case 1:
                    i2 = 81;
                    break;
                case 2:
                    i2 = 85;
                    break;
                case 3:
                    i2 = 79;
                    break;
                case 4:
                    i2 = 69;
                    break;
                case 5:
                    i2 = 37;
                    break;
                default:
                    i2 = 31;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 31);
        }
        return charArray;
    }

    public void a(boolean z) {
        this.b = z;
    }

    public void a(byte[] bArr) {
        this.a = bArr;
    }

    public byte[] a() {
        return this.a;
    }

    public boolean b() {
        return this.b;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.StartGamePacket
    protected boolean canEqual(Object obj) {
        return obj instanceof StartGamePacket;
    }

    /* JADX WARN: Code restructure failed: missing block: B:169:0x0278, code lost:            if (r0 != null) goto L176;     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0284, code lost:            r2 = getRotation();        r4 = r9.getRotation();     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x028c, code lost:            if (r2 != null) goto L184;     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x028e, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0290, code lost:            if (r0 == null) goto L188;     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x029c, code lost:            r2 = getSpawnBiomeType();        r4 = r9.getSpawnBiomeType();     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x02a4, code lost:            if (r2 != null) goto L192;     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x02a6, code lost:            if (r4 == null) goto L195;     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x02a8, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x02b0, code lost:            r2 = getCustomBiomeName();        r4 = r9.getCustomBiomeName();     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x02b8, code lost:            if (r2 != null) goto L199;     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02ba, code lost:            if (r4 == null) goto L202;     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x02bc, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x02c4, code lost:            r2 = getLevelGameType();        r4 = r9.getLevelGameType();     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x02cc, code lost:            if (r2 != null) goto L206;     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x02ce, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x02d0, code lost:            if (r0 == null) goto L210;     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x02dc, code lost:            r2 = getDefaultSpawn();        r4 = r9.getDefaultSpawn();     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x02e4, code lost:            if (r2 != null) goto L214;     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x02e6, code lost:            if (r4 == null) goto L217;     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02e8, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x02f0, code lost:            r2 = getEducationProductionId();        r4 = r9.getEducationProductionId();     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x02f8, code lost:            if (r2 != null) goto L221;     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x02fa, code lost:            if (r4 == null) goto L224;     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x02fc, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0304, code lost:            r2 = getXblBroadcastMode();        r4 = r9.getXblBroadcastMode();     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x030c, code lost:            if (r2 != null) goto L228;     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x030e, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0310, code lost:            if (r0 == null) goto L232;     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x031c, code lost:            r2 = getPlatformBroadcastMode();        r4 = r9.getPlatformBroadcastMode();     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0324, code lost:            if (r2 != null) goto L236;     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0326, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0328, code lost:            if (r0 == null) goto L240;     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0334, code lost:            r2 = getExperiments();        r4 = r9.getExperiments();     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x033c, code lost:            if (r2 != null) goto L244;     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x033e, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x0340, code lost:            if (r0 == null) goto L248;     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x034c, code lost:            r2 = getDefaultPlayerPermission();        r4 = r9.getDefaultPlayerPermission();     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0354, code lost:            if (r2 != null) goto L252;     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x0356, code lost:            if (r4 == null) goto L255;     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x0358, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x0360, code lost:            r2 = getVanillaVersion();        r4 = r9.getVanillaVersion();     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x0368, code lost:            if (r2 != null) goto L259;     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x036a, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x036c, code lost:            if (r0 == null) goto L263;     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x0378, code lost:            r2 = getEduSharedUriResource();        r4 = r9.getEduSharedUriResource();     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x0380, code lost:            if (r2 != null) goto L267;     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x0382, code lost:            if (r4 == null) goto L270;     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0384, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x038c, code lost:            r2 = getForceExperimentalGameplay();        r4 = r9.getForceExperimentalGameplay();     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x0394, code lost:            if (r2 != null) goto L274;     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0396, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0398, code lost:            if (r0 == null) goto L278;     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x03a4, code lost:            r2 = getChatRestrictionLevel();        r4 = r9.getChatRestrictionLevel();     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x03ac, code lost:            if (r2 != null) goto L282;     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x03ae, code lost:            if (r4 == null) goto L285;     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x03b0, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x03b8, code lost:            r2 = getLevelId();        r4 = r9.getLevelId();     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x03c0, code lost:            if (r2 != null) goto L289;     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x03c2, code lost:            if (r4 == null) goto L292;     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x03c4, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x03cc, code lost:            r2 = getLevelName();        r4 = r9.getLevelName();     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x03d4, code lost:            if (r2 != null) goto L296;     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x03d6, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x03d8, code lost:            if (r0 == null) goto L300;     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x03e4, code lost:            r2 = getPremiumWorldTemplateId();        r4 = r9.getPremiumWorldTemplateId();     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x03ec, code lost:            if (r2 != null) goto L304;     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x03ee, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x03f0, code lost:            if (r0 == null) goto L308;     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x03fc, code lost:            r2 = getAuthoritativeMovementMode();        r4 = r9.getAuthoritativeMovementMode();     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x0404, code lost:            if (r2 != null) goto L312;     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x0406, code lost:            if (r4 == null) goto L315;     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x0408, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x0410, code lost:            r2 = getBlockPalette();        r4 = r9.getBlockPalette();     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x0418, code lost:            if (r2 != null) goto L319;     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x041a, code lost:            if (r4 == null) goto L322;     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x041c, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x0424, code lost:            r2 = getBlockProperties();        r4 = r9.getBlockProperties();     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x042c, code lost:            if (r2 != null) goto L326;     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x042e, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0430, code lost:            if (r0 == null) goto L330;     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x043c, code lost:            r2 = getItemDefinitions();        r4 = r9.getItemDefinitions();     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x0444, code lost:            if (r2 != null) goto L334;     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x0446, code lost:            if (r4 == null) goto L337;     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x0448, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x0450, code lost:            r2 = getMultiplayerCorrelationId();        r4 = r9.getMultiplayerCorrelationId();     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x0458, code lost:            if (r2 != null) goto L341;     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x045a, code lost:            if (r4 == null) goto L344;     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x045c, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x0464, code lost:            r2 = getServerEngine();        r4 = r9.getServerEngine();     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x046c, code lost:            if (r2 != null) goto L348;     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x046e, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0470, code lost:            if (r0 == null) goto L352;     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x047b, code lost:            r2 = getPlayerPropertyData();        r4 = r9.getPlayerPropertyData();     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0483, code lost:            if (r2 != null) goto L356;     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x0485, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0487, code lost:            if (r0 == null) goto L360;     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0492, code lost:            r2 = getWorldTemplateId();        r4 = r9.getWorldTemplateId();     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x049a, code lost:            if (r2 != null) goto L364;     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x049c, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x049e, code lost:            if (r0 == null) goto L368;     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x04a9, code lost:            r2 = getNetworkPermissions();        r4 = r9.getNetworkPermissions();     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x04b1, code lost:            if (r2 != null) goto L372;     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x04b3, code lost:            if (r4 != null) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x04b5, code lost:            if (r0 == null) goto L376;     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x04cc, code lost:            if (java.util.Arrays.equals(a(), r9.a()) != false) goto L379;     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04ce, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x04cf, code lost:            return true;     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x04bb, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x04bd, code lost:            if (r0 == null) goto L376;     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x04a4, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x04a6, code lost:            if (r0 == null) goto L368;     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x048d, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x048f, code lost:            if (r0 == null) goto L360;     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x0476, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x0478, code lost:            if (r0 == null) goto L352;     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x0461, code lost:            if (r2.equals(r4) != false) goto L344;     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0463, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x044d, code lost:            if (r2.equals(r4) != false) goto L337;     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x044f, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0436, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0438, code lost:            if (r0 == null) goto L330;     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0421, code lost:            if (r2.equals(r4) != false) goto L322;     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0423, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x040d, code lost:            if (r2.equals(r4) != false) goto L315;     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x040f, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x03f6, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x03f8, code lost:            if (r0 == null) goto L308;     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x03de, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x03e0, code lost:            if (r0 == null) goto L300;     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x03c9, code lost:            if (r2.equals(r4) != false) goto L292;     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x03cb, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x03b5, code lost:            if (r2.equals(r4) != false) goto L285;     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x03b7, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x039e, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x03a0, code lost:            if (r0 == null) goto L278;     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0389, code lost:            if (r2.equals(r4) != false) goto L270;     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x038b, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0372, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0374, code lost:            if (r0 == null) goto L263;     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x035d, code lost:            if (r2.equals(r4) != false) goto L255;     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x035f, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x0346, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0348, code lost:            if (r0 == null) goto L248;     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x032e, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0330, code lost:            if (r0 == null) goto L240;     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x0316, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0318, code lost:            if (r0 == null) goto L232;     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0301, code lost:            if (r2.equals(r4) != false) goto L224;     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0303, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x02ed, code lost:            if (r2.equals(r4) != false) goto L217;     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x02ef, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x02d6, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x02d8, code lost:            if (r0 == null) goto L210;     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x02c1, code lost:            if (r2.equals(r4) != false) goto L202;     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x02c3, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x02ad, code lost:            if (r2.equals(r4) != false) goto L195;     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x02af, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0296, code lost:            if (r2.equals(r4) == false) goto L380;     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0298, code lost:            if (r0 == null) goto L188;     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x0280, code lost:            if (r0 == null) goto L180;     */
    @Override // org.cloudburstmc.protocol.bedrock.packet.StartGamePacket
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r9) {
        /*
            Method dump skipped, instructions count: 1233
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.ae.equals(java.lang.Object):boolean");
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.StartGamePacket
    public int hashCode() {
        long uniqueEntityId = getUniqueEntityId();
        long runtimeEntityId = getRuntimeEntityId();
        int i = ((((int) (uniqueEntityId ^ (uniqueEntityId >>> 32))) + 59) * 59) + ((int) (runtimeEntityId ^ (runtimeEntityId >>> 32)));
        long seed = getSeed();
        int dimensionId = ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((i * 59) + ((int) (seed ^ (seed >>> 32)))) * 59) + getDimensionId()) * 59) + getGeneratorId()) * 59) + getDifficulty()) * 59) + (isAchievementsDisabled() ? 79 : 97)) * 59) + getDayCycleStopTime()) * 59) + getEduEditionOffers()) * 59) + (isEduFeaturesEnabled() ? 79 : 97)) * 59) + Float.floatToIntBits(getRainLevel())) * 59) + Float.floatToIntBits(getLightningLevel())) * 59) + (isPlatformLockedContentConfirmed() ? 79 : 97)) * 59) + (isMultiplayerGame() ? 79 : 97)) * 59) + (isBroadcastingToLan() ? 79 : 97)) * 59) + (isCommandsEnabled() ? 79 : 97)) * 59) + (isTexturePacksRequired() ? 79 : 97)) * 59) + (isExperimentsPreviouslyToggled() ? 79 : 97)) * 59) + (isBonusChestEnabled() ? 79 : 97)) * 59) + (isStartingWithMap() ? 79 : 97)) * 59) + (isTrustingPlayers() ? 79 : 97)) * 59) + getServerChunkTickRange()) * 59) + (isBehaviorPackLocked() ? 79 : 97)) * 59) + (isResourcePackLocked() ? 79 : 97)) * 59) + (isFromLockedWorldTemplate() ? 79 : 97)) * 59) + (isUsingMsaGamertagsOnly() ? 79 : 97)) * 59) + (isFromWorldTemplate() ? 79 : 97)) * 59) + (isWorldTemplateOptionLocked() ? 79 : 97)) * 59) + (isOnlySpawningV1Villagers() ? 79 : 97)) * 59) + getLimitedWorldWidth()) * 59) + getLimitedWorldHeight()) * 59) + (isNetherType() ? 79 : 97)) * 59) + (isDisablingPlayerInteractions() ? 79 : 97)) * 59) + (isDisablingPersonas() ? 79 : 97)) * 59) + (isDisablingCustomSkins() ? 79 : 97)) * 59) + (isTrial() ? 79 : 97)) * 59) + getRewindHistorySize()) * 59;
        int i2 = isServerAuthoritativeBlockBreaking() ? 79 : 97;
        long currentTick = getCurrentTick();
        int enchantmentSeed = (((((dimensionId + i2) * 59) + ((int) (currentTick ^ (currentTick >>> 32)))) * 59) + getEnchantmentSeed()) * 59;
        int i3 = isInventoriesServerAuthoritative() ? 79 : 97;
        long blockRegistryChecksum = getBlockRegistryChecksum();
        int i4 = (((((((((((((((enchantmentSeed + i3) * 59) + ((int) ((blockRegistryChecksum >>> 32) ^ blockRegistryChecksum))) * 59) + (isWorldEditor() ? 79 : 97)) * 59) + (isClientSideGenerationEnabled() ? 79 : 97)) * 59) + (isEmoteChatMuted() ? 79 : 97)) * 59) + (isBlockNetworkIdsHashed() ? 79 : 97)) * 59) + (isCreatedInEditor() ? 79 : 97)) * 59) + (isExportedFromEditor() ? 79 : 97)) * 59;
        int i5 = b() ? 79 : 97;
        List<GameRuleData<?>> gamerules = getGamerules();
        int hashCode = ((i4 + i5) * 59) + (gamerules == null ? 43 : gamerules.hashCode());
        GameType playerGameType = getPlayerGameType();
        int hashCode2 = (hashCode * 59) + (playerGameType == null ? 43 : playerGameType.hashCode());
        Vector3f playerPosition = getPlayerPosition();
        int hashCode3 = (hashCode2 * 59) + (playerPosition == null ? 43 : playerPosition.hashCode());
        Vector2f rotation = getRotation();
        int hashCode4 = (hashCode3 * 59) + (rotation == null ? 43 : rotation.hashCode());
        SpawnBiomeType spawnBiomeType = getSpawnBiomeType();
        int hashCode5 = (hashCode4 * 59) + (spawnBiomeType == null ? 43 : spawnBiomeType.hashCode());
        String customBiomeName = getCustomBiomeName();
        int hashCode6 = (hashCode5 * 59) + (customBiomeName == null ? 43 : customBiomeName.hashCode());
        GameType levelGameType = getLevelGameType();
        int hashCode7 = (hashCode6 * 59) + (levelGameType == null ? 43 : levelGameType.hashCode());
        Vector3i defaultSpawn = getDefaultSpawn();
        int hashCode8 = (hashCode7 * 59) + (defaultSpawn == null ? 43 : defaultSpawn.hashCode());
        String educationProductionId = getEducationProductionId();
        int hashCode9 = (hashCode8 * 59) + (educationProductionId == null ? 43 : educationProductionId.hashCode());
        GamePublishSetting xblBroadcastMode = getXblBroadcastMode();
        int hashCode10 = (hashCode9 * 59) + (xblBroadcastMode == null ? 43 : xblBroadcastMode.hashCode());
        GamePublishSetting platformBroadcastMode = getPlatformBroadcastMode();
        int hashCode11 = (hashCode10 * 59) + (platformBroadcastMode == null ? 43 : platformBroadcastMode.hashCode());
        List<ExperimentData> experiments = getExperiments();
        int hashCode12 = (hashCode11 * 59) + (experiments == null ? 43 : experiments.hashCode());
        PlayerPermission defaultPlayerPermission = getDefaultPlayerPermission();
        int hashCode13 = (hashCode12 * 59) + (defaultPlayerPermission == null ? 43 : defaultPlayerPermission.hashCode());
        String vanillaVersion = getVanillaVersion();
        int hashCode14 = (hashCode13 * 59) + (vanillaVersion == null ? 43 : vanillaVersion.hashCode());
        EduSharedUriResource eduSharedUriResource = getEduSharedUriResource();
        int hashCode15 = (hashCode14 * 59) + (eduSharedUriResource == null ? 43 : eduSharedUriResource.hashCode());
        OptionalBoolean forceExperimentalGameplay = getForceExperimentalGameplay();
        int hashCode16 = (hashCode15 * 59) + (forceExperimentalGameplay == null ? 43 : forceExperimentalGameplay.hashCode());
        ChatRestrictionLevel chatRestrictionLevel = getChatRestrictionLevel();
        int hashCode17 = (hashCode16 * 59) + (chatRestrictionLevel == null ? 43 : chatRestrictionLevel.hashCode());
        String levelId = getLevelId();
        int hashCode18 = (hashCode17 * 59) + (levelId == null ? 43 : levelId.hashCode());
        String levelName = getLevelName();
        int hashCode19 = (hashCode18 * 59) + (levelName == null ? 43 : levelName.hashCode());
        String premiumWorldTemplateId = getPremiumWorldTemplateId();
        int hashCode20 = (hashCode19 * 59) + (premiumWorldTemplateId == null ? 43 : premiumWorldTemplateId.hashCode());
        AuthoritativeMovementMode authoritativeMovementMode = getAuthoritativeMovementMode();
        int hashCode21 = (hashCode20 * 59) + (authoritativeMovementMode == null ? 43 : authoritativeMovementMode.hashCode());
        NbtList<NbtMap> blockPalette = getBlockPalette();
        int hashCode22 = (hashCode21 * 59) + (blockPalette == null ? 43 : blockPalette.hashCode());
        List<BlockPropertyData> blockProperties = getBlockProperties();
        int hashCode23 = (hashCode22 * 59) + (blockProperties == null ? 43 : blockProperties.hashCode());
        List<ItemDefinition> itemDefinitions = getItemDefinitions();
        int hashCode24 = (hashCode23 * 59) + (itemDefinitions == null ? 43 : itemDefinitions.hashCode());
        String multiplayerCorrelationId = getMultiplayerCorrelationId();
        int hashCode25 = (hashCode24 * 59) + (multiplayerCorrelationId == null ? 43 : multiplayerCorrelationId.hashCode());
        String serverEngine = getServerEngine();
        int hashCode26 = (hashCode25 * 59) + (serverEngine == null ? 43 : serverEngine.hashCode());
        NbtMap playerPropertyData = getPlayerPropertyData();
        int hashCode27 = (hashCode26 * 59) + (playerPropertyData == null ? 43 : playerPropertyData.hashCode());
        UUID worldTemplateId = getWorldTemplateId();
        int hashCode28 = (hashCode27 * 59) + (worldTemplateId == null ? 43 : worldTemplateId.hashCode());
        NetworkPermissions networkPermissions = getNetworkPermissions();
        return (((hashCode28 * 59) + (networkPermissions != null ? networkPermissions.hashCode() : 43)) * 59) + Arrays.hashCode(a());
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.StartGamePacket
    public String toString() {
        List<GameRuleData<?>> gamerules = getGamerules();
        StringBuilder sb = new StringBuilder();
        String[] strArr = d;
        return sb.append(strArr[32]).append(gamerules).append(strArr[41]).append(getUniqueEntityId()).append(strArr[6]).append(getRuntimeEntityId()).append(strArr[33]).append(getPlayerGameType()).append(strArr[71]).append(getPlayerPosition()).append(strArr[70]).append(getRotation()).append(strArr[75]).append(getSeed()).append(strArr[62]).append(getSpawnBiomeType()).append(strArr[7]).append(getCustomBiomeName()).append(strArr[19]).append(getDimensionId()).append(strArr[22]).append(getGeneratorId()).append(strArr[20]).append(getLevelGameType()).append(strArr[49]).append(getDifficulty()).append(strArr[40]).append(getDefaultSpawn()).append(strArr[73]).append(isAchievementsDisabled()).append(strArr[56]).append(getDayCycleStopTime()).append(strArr[68]).append(getEduEditionOffers()).append(strArr[64]).append(isEduFeaturesEnabled()).append(strArr[10]).append(getEducationProductionId()).append(strArr[52]).append(getRainLevel()).append(strArr[45]).append(getLightningLevel()).append(strArr[66]).append(isPlatformLockedContentConfirmed()).append(strArr[23]).append(isMultiplayerGame()).append(strArr[39]).append(isBroadcastingToLan()).append(strArr[43]).append(getXblBroadcastMode()).append(strArr[31]).append(getPlatformBroadcastMode()).append(strArr[67]).append(isCommandsEnabled()).append(strArr[37]).append(isTexturePacksRequired()).append(strArr[24]).append(getExperiments()).append(strArr[29]).append(isExperimentsPreviouslyToggled()).append(strArr[51]).append(isBonusChestEnabled()).append(strArr[60]).append(isStartingWithMap()).append(strArr[38]).append(isTrustingPlayers()).append(strArr[15]).append(getDefaultPlayerPermission()).append(strArr[42]).append(getServerChunkTickRange()).append(strArr[34]).append(isBehaviorPackLocked()).append(strArr[58]).append(isResourcePackLocked()).append(strArr[16]).append(isFromLockedWorldTemplate()).append(strArr[36]).append(isUsingMsaGamertagsOnly()).append(strArr[28]).append(isFromWorldTemplate()).append(strArr[12]).append(isWorldTemplateOptionLocked()).append(strArr[47]).append(isOnlySpawningV1Villagers()).append(strArr[46]).append(getVanillaVersion()).append(strArr[14]).append(getLimitedWorldWidth()).append(strArr[74]).append(getLimitedWorldHeight()).append(strArr[26]).append(isNetherType()).append(strArr[25]).append(getEduSharedUriResource()).append(strArr[0]).append(getForceExperimentalGameplay()).append(strArr[76]).append(getChatRestrictionLevel()).append(strArr[5]).append(isDisablingPlayerInteractions()).append(strArr[63]).append(isDisablingPersonas()).append(strArr[30]).append(isDisablingCustomSkins()).append(strArr[13]).append(getLevelId()).append(strArr[44]).append(getLevelName()).append(strArr[61]).append(getPremiumWorldTemplateId()).append(strArr[69]).append(isTrial()).append(strArr[17]).append(getAuthoritativeMovementMode()).append(strArr[8]).append(getRewindHistorySize()).append(strArr[1]).append(isServerAuthoritativeBlockBreaking()).append(strArr[4]).append(getCurrentTick()).append(strArr[54]).append(getEnchantmentSeed()).append(strArr[11]).append(getBlockProperties()).append(strArr[2]).append(getMultiplayerCorrelationId()).append(strArr[48]).append(isInventoriesServerAuthoritative()).append(strArr[53]).append(getServerEngine()).append(strArr[57]).append(getPlayerPropertyData()).append(strArr[9]).append(getBlockRegistryChecksum()).append(strArr[18]).append(getWorldTemplateId()).append(strArr[35]).append(isWorldEditor()).append(strArr[50]).append(isClientSideGenerationEnabled()).append(strArr[59]).append(isEmoteChatMuted()).append(strArr[55]).append(isBlockNetworkIdsHashed()).append(strArr[3]).append(isCreatedInEditor()).append(strArr[65]).append(isExportedFromEditor()).append(strArr[21]).append(getNetworkPermissions()).append(strArr[27]).append(b()).append(strArr[72]).append(Arrays.toString(a())).append(")").toString();
    }
}
