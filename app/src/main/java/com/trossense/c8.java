package com.trossense;

import com.trossense.sdk.entity.type.EntityActor;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3f;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;

/* loaded from: classes3.dex */
public class c8 {
    private static final long a = dj.a(-2095748734050006016L, -8021768588283512961L, MethodHandles.lookup().lookupClass()).a(91337119571540L);
    private static final String[] b;

    static {
        String[] strArr = new String[2];
        int length = "JzI<\u0006k}FcU:\u000bJzI<\u0006k}FcU:".length();
        int i = 0;
        char c = 11;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(13, a("JzI<\u0006k}FcU:\u000bJzI<\u0006k}FcU:".substring(i3, i4)));
            if (i4 >= length) {
                b = strArr;
                return;
            } else {
                i2 = i4;
                c = "JzI<\u0006k}FcU:\u000bJzI<\u0006k}FcU:".charAt(i4);
                i = i5;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double a(EntityActor entityActor, EntityActor entityActor2) {
        Vec3f N = entityActor.N();
        Vec3f N2 = entityActor2.N();
        float f = N.x - N2.x;
        float f2 = N.y - N2.y;
        float f3 = N.z - N2.z;
        return Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
    }

    public static float a(Vec3f vec3f, Vec3f vec3f2) {
        return (float) Math.sqrt(Math.pow(vec3f.x - vec3f2.x, 2.0d) + Math.pow(vec3f.z - vec3f2.z, 2.0d));
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 36;
                    break;
                case 1:
                    i2 = 2;
                    break;
                case 2:
                    i2 = 55;
                    break;
                case 3:
                    i2 = 69;
                    break;
                case 4:
                    i2 = 100;
                    break;
                case 5:
                    i2 = 11;
                    break;
                default:
                    i2 = 51;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x00d3, code lost:            if (r14 == 0) goto L36;     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00f4, code lost:            if (r6 != null) goto L60;     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x012e, code lost:            if (r6 != null) goto L82;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x013b A[LOOP:0: B:18:0x0090->B:25:0x013b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0141 A[EDGE_INSN: B:26:0x0141->B:27:0x0141 BREAK  A[LOOP:0: B:18:0x0090->B:25:0x013b], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0067  */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v10, types: [boolean] */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v13 */
    /* JADX WARN: Type inference failed for: r14v14, types: [int] */
    /* JADX WARN: Type inference failed for: r14v15 */
    /* JADX WARN: Type inference failed for: r14v18 */
    /* JADX WARN: Type inference failed for: r14v19 */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r14v9, types: [boolean] */
    /* JADX WARN: Type inference failed for: r7v1, types: [boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List a(long r18, com.trossense.sdk.entity.type.EntityLocalPlayer r20, float r21) {
        /*
            Method dump skipped, instructions count: 322
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.c8.a(long, com.trossense.sdk.entity.type.EntityLocalPlayer, float):java.util.List");
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '3');
        }
        return charArray;
    }

    public static EntityActor b(final EntityLocalPlayer entityLocalPlayer, float f, long j) {
        List a2 = a((j ^ a) ^ 88004806875535L, entityLocalPlayer, f);
        if (a2.isEmpty()) {
            return null;
        }
        a2.sort(Comparator.comparingDouble(new ToDoubleFunction() { // from class: com.trossense.c8$$ExternalSyntheticLambda0
            @Override // java.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                double a3;
                a3 = c8.a(EntityLocalPlayer.this, (EntityActor) obj);
                return a3;
            }
        }));
        return (EntityActor) a2.get(0);
    }

    public static List c(long j, EntityLocalPlayer entityLocalPlayer, float f) {
        ArrayList arrayList = new ArrayList();
        ItemData q = entityLocalPlayer.h(0).q();
        int i = q.getTag() != null ? q.getTag().getInt(b[1], -1) : -1;
        for (EntityActor entityActor : entityLocalPlayer.W().e()) {
            if ((entityActor instanceof com.trossense.sdk.entity.type.c) && a(entityLocalPlayer.N(), entityActor.N()) < f) {
                if (entityActor instanceof com.trossense.sdk.entity.type.b) {
                    ItemData q2 = entityActor.h(0).q();
                    if (i != -1 && q2.getTag() != null && q2.getTag().getInt(b[0], -1) == i) {
                    }
                }
                arrayList.add(entityActor);
            }
        }
        return arrayList;
    }
}
