package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.ClientPlayMode;
import org.cloudburstmc.protocol.bedrock.data.InputInteractionModel;
import org.cloudburstmc.protocol.bedrock.data.InputMode;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.data.PlayerBlockActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.ItemUseTransaction;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;

/* loaded from: classes3.dex */
public class ad extends PlayerAuthInputPacket {
    private static String[] f;
    private static final long g = dj.a(-230715471969655190L, 5629401003451417240L, MethodHandles.lookup().lookupClass()).a(253587061003408L);
    private static final String[] h;
    private boolean a;
    private boolean b;
    private float c;
    private float d;
    private boolean e;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004a. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[22];
        b((String[]) null);
        String str = "#'o\u0013}\nWhJa\u000by0]lsa\u000f!\u000b#'f\u001co#@{uo@\t#'k\u0005h\u0014Y;:\u0013#'g\ty\u000bk{fm\u0016N\u0003Izb}\t!\t#'c\u0012h\u000fWa:\f#'g\u0013l\u0013LBhj\u0018!\u0007#'z\u0014\u007f\r\u0005\u000b#'~\u0011}\u001fu`ck@\u0011#'m\u001cq\u0003JnCk\r}\u0014Ljc3\u0018#'g\u0013l\u0013LFiz\u0018n\u0007[{na\u0013Q\t\\jk3\f#'g\u0013l\u0013LKfz\u001c!&Abz\u0018}\u0015]_ko\u0004y\u0014yzsf4r\u0016M{Wo\u001ew\u0003L'ua\t}\u0012Q`i3\b#'j\u0018p\u0012Y2\t#'k\u0005h\u0014Y=:\u000b#'~\u0012o\u000fLfh`@\u0013#'~\u000fy\u0002Qlsk\u0019J\u0003Pfdb\u0018!\t#'k\u0005h\u0014Y>:\t#'k\u0005h\u0014Y<:\u0010#'~\u0011}\u001f]}Fm\tu\tV|:\u0012#'x\u0018t\u000f[cb\\\u0012h\u0007Lfh`@";
        int length = "#'o\u0013}\nWhJa\u000by0]lsa\u000f!\u000b#'f\u001co#@{uo@\t#'k\u0005h\u0014Y;:\u0013#'g\ty\u000bk{fm\u0016N\u0003Izb}\t!\t#'c\u0012h\u000fWa:\f#'g\u0013l\u0013LBhj\u0018!\u0007#'z\u0014\u007f\r\u0005\u000b#'~\u0011}\u001fu`ck@\u0011#'m\u001cq\u0003JnCk\r}\u0014Ljc3\u0018#'g\u0013l\u0013LFiz\u0018n\u0007[{na\u0013Q\t\\jk3\f#'g\u0013l\u0013LKfz\u001c!&Abz\u0018}\u0015]_ko\u0004y\u0014yzsf4r\u0016M{Wo\u001ew\u0003L'ua\t}\u0012Q`i3\b#'j\u0018p\u0012Y2\t#'k\u0005h\u0014Y=:\u000b#'~\u0012o\u000fLfh`@\u0013#'~\u000fy\u0002Qlsk\u0019J\u0003Pfdb\u0018!\t#'k\u0005h\u0014Y>:\t#'k\u0005h\u0014Y<:\u0010#'~\u0011}\u001f]}Fm\tu\tV|:\u0012#'x\u0018t\u000f[cb\\\u0012h\u0007Lfh`@".length();
        char c = 19;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 121;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "LH\bf\u0016d\u0002\u0013\r5`\u0012g$\u0001\u000b\u0015{\u001cgj\u0012LH\u0017`4h-\u0005,\b`\u0016j#\t\u0007\u000f/";
                        length = "LH\bf\u0016d\u0002\u0013\r5`\u0012g$\u0001\u000b\u0015{\u001cgj\u0012LH\u0017`4h-\u0005,\b`\u0016j#\t\u0007\u000f/".length();
                        c = 21;
                        i2 = -1;
                        i3 = i;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        i5 = 22;
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    h = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                i5 = 22;
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 118;
                    break;
                case 1:
                    i2 = 126;
                    break;
                case 2:
                    i2 = 119;
                    break;
                case 3:
                    i2 = 4;
                    break;
                case 4:
                    i2 = 101;
                    break;
                case 5:
                    i2 = 31;
                    break;
                default:
                    i2 = 65;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'A');
        }
        return charArray;
    }

    public static void b(String[] strArr) {
        f = strArr;
    }

    public static String[] f() {
        return f;
    }

    public void a(float f2) {
        this.c = f2;
    }

    public void a(boolean z) {
        this.a = z;
    }

    public boolean a() {
        return this.a;
    }

    public void b(float f2) {
        this.d = f2;
    }

    public void b(boolean z) {
        this.b = z;
    }

    public boolean b() {
        return this.b;
    }

    public float c() {
        return this.c;
    }

    public void c(boolean z) {
        this.e = z;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket
    protected boolean canEqual(Object obj) {
        return obj instanceof ad;
    }

    public float d() {
        return this.d;
    }

    public boolean e() {
        return this.e;
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x0184, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x016c, code lost:            if (r2.equals(r4) == false) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x016e, code lost:            if (r0 == null) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0155, code lost:            if (r2.equals(r4) == false) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0157, code lost:            if (r0 == null) goto L111;     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0140, code lost:            if (r2.equals(r4) != false) goto L103;     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0142, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0129, code lost:            if (r2.equals(r4) == false) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x012b, code lost:            if (r0 == null) goto L96;     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0111, code lost:            if (r2.equals(r4) == false) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0113, code lost:            if (r0 == null) goto L88;     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x00fc, code lost:            if (r2.equals(r4) != false) goto L80;     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x00fe, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x00e5, code lost:            if (r2.equals(r4) == false) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x00e7, code lost:            if (r0 == null) goto L73;     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x00d0, code lost:            if (r2.equals(r4) != false) goto L65;     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x00d2, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x00b9, code lost:            if (r2.equals(r4) == false) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x00bb, code lost:            if (r0 == null) goto L58;     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x00a4, code lost:            if (r2.equals(r4) != false) goto L50;     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x00a6, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x008f, code lost:            if (r0 == null) goto L43;     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0087, code lost:            if (r0 != null) goto L39;     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0093, code lost:            r2 = getPosition();        r4 = r9.getPosition();     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x009b, code lost:            if (r2 != null) goto L47;     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x009d, code lost:            if (r4 == null) goto L50;     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x009f, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00a7, code lost:            r2 = getMotion();        r4 = r9.getMotion();     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00af, code lost:            if (r2 != null) goto L54;     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b1, code lost:            if (r4 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b3, code lost:            if (r0 == null) goto L58;     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00bf, code lost:            r2 = getInputData();        r4 = r9.getInputData();     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c7, code lost:            if (r2 != null) goto L62;     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00c9, code lost:            if (r4 == null) goto L65;     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00cb, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00d3, code lost:            r2 = getInputMode();        r4 = r9.getInputMode();     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00db, code lost:            if (r2 != null) goto L69;     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00dd, code lost:            if (r4 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00df, code lost:            if (r0 == null) goto L73;     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00eb, code lost:            r2 = getPlayMode();        r4 = r9.getPlayMode();     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00f3, code lost:            if (r2 != null) goto L77;     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00f5, code lost:            if (r4 == null) goto L80;     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00f7, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ff, code lost:            r2 = getVrGazeDirection();        r4 = r9.getVrGazeDirection();     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0107, code lost:            if (r2 != null) goto L84;     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0109, code lost:            if (r4 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x010b, code lost:            if (r0 == null) goto L88;     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0117, code lost:            r2 = getDelta();        r4 = r9.getDelta();     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x011f, code lost:            if (r2 != null) goto L92;     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0121, code lost:            if (r4 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0123, code lost:            if (r0 == null) goto L96;     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x012f, code lost:            r2 = getItemUseTransaction();        r4 = r9.getItemUseTransaction();     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0137, code lost:            if (r2 != null) goto L100;     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0139, code lost:            if (r4 == null) goto L103;     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x013b, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0143, code lost:            r2 = getItemStackRequest();        r4 = r9.getItemStackRequest();     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x014b, code lost:            if (r2 != null) goto L107;     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x014d, code lost:            if (r4 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x014f, code lost:            if (r0 == null) goto L111;     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x015a, code lost:            r2 = getPlayerActions();        r4 = r9.getPlayerActions();     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0162, code lost:            if (r2 != null) goto L115;     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0164, code lost:            if (r4 != null) goto L141;     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0166, code lost:            if (r0 == null) goto L119;     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0171, code lost:            r0 = getInputInteractionModel();        r2 = r9.getInputInteractionModel();     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0179, code lost:            if (r0 != null) goto L123;     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x017b, code lost:            if (r2 == null) goto L126;     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x017d, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0185, code lost:            r0 = getAnalogMoveVector();        r2 = r9.getAnalogMoveVector();     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x018d, code lost:            if (r0 != null) goto L130;     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x018f, code lost:            if (r2 == null) goto L133;     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0191, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0199, code lost:            r0 = getVehicleRotation();        r9 = r9.getVehicleRotation();     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x01a1, code lost:            if (r0 != null) goto L137;     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01a3, code lost:            if (r9 == null) goto L140;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01a5, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01ad, code lost:            return true;     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01aa, code lost:            if (r0.equals(r9) != false) goto L140;     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01ac, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0196, code lost:            if (r0.equals(r2) != false) goto L133;     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0198, code lost:            return false;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0182, code lost:            if (r0.equals(r2) != false) goto L126;     */
    @Override // org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r9) {
        /*
            Method dump skipped, instructions count: 431
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.ad.equals(java.lang.Object):boolean");
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket
    public int hashCode() {
        long tick = getTick();
        int i = (((int) (tick ^ (tick >>> 32))) + 59) * 59;
        int i2 = isCameraDeparted() ? 79 : 97;
        long predictedVehicle = getPredictedVehicle();
        int floatToIntBits = (((((((((((i + i2) * 59) + ((int) ((predictedVehicle >>> 32) ^ predictedVehicle))) * 59) + (a() ? 79 : 97)) * 59) + (b() ? 79 : 97)) * 59) + Float.floatToIntBits(c())) * 59) + Float.floatToIntBits(d())) * 59;
        int i3 = e() ? 79 : 97;
        Vector3f rotation = getRotation();
        int hashCode = ((floatToIntBits + i3) * 59) + (rotation == null ? 43 : rotation.hashCode());
        Vector3f position = getPosition();
        int hashCode2 = (hashCode * 59) + (position == null ? 43 : position.hashCode());
        Vector2f motion = getMotion();
        int hashCode3 = (hashCode2 * 59) + (motion == null ? 43 : motion.hashCode());
        Set<PlayerAuthInputData> inputData = getInputData();
        int hashCode4 = (hashCode3 * 59) + (inputData == null ? 43 : inputData.hashCode());
        InputMode inputMode = getInputMode();
        int hashCode5 = (hashCode4 * 59) + (inputMode == null ? 43 : inputMode.hashCode());
        ClientPlayMode playMode = getPlayMode();
        int hashCode6 = (hashCode5 * 59) + (playMode == null ? 43 : playMode.hashCode());
        Vector3f vrGazeDirection = getVrGazeDirection();
        int hashCode7 = (hashCode6 * 59) + (vrGazeDirection == null ? 43 : vrGazeDirection.hashCode());
        Vector3f delta = getDelta();
        int hashCode8 = (hashCode7 * 59) + (delta == null ? 43 : delta.hashCode());
        ItemUseTransaction itemUseTransaction = getItemUseTransaction();
        int hashCode9 = (hashCode8 * 59) + (itemUseTransaction == null ? 43 : itemUseTransaction.hashCode());
        ItemStackRequest itemStackRequest = getItemStackRequest();
        int hashCode10 = (hashCode9 * 59) + (itemStackRequest == null ? 43 : itemStackRequest.hashCode());
        List<PlayerBlockActionData> playerActions = getPlayerActions();
        int hashCode11 = (hashCode10 * 59) + (playerActions == null ? 43 : playerActions.hashCode());
        InputInteractionModel inputInteractionModel = getInputInteractionModel();
        int hashCode12 = (hashCode11 * 59) + (inputInteractionModel == null ? 43 : inputInteractionModel.hashCode());
        Vector2f analogMoveVector = getAnalogMoveVector();
        int hashCode13 = (hashCode12 * 59) + (analogMoveVector == null ? 43 : analogMoveVector.hashCode());
        Vector2f vehicleRotation = getVehicleRotation();
        return (hashCode13 * 59) + (vehicleRotation != null ? vehicleRotation.hashCode() : 43);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket
    public String toString() {
        Vector3f rotation = getRotation();
        StringBuilder sb = new StringBuilder();
        String[] strArr = h;
        return sb.append(strArr[11]).append(rotation).append(strArr[14]).append(getPosition()).append(strArr[4]).append(getMotion()).append(strArr[10]).append(getInputData()).append(strArr[5]).append(getInputMode()).append(strArr[7]).append(getPlayMode()).append(strArr[21]).append(getVrGazeDirection()).append(strArr[6]).append(getTick()).append(strArr[12]).append(getDelta()).append(strArr[8]).append(isCameraDeparted()).append(strArr[20]).append(getItemUseTransaction()).append(strArr[3]).append(getItemStackRequest()).append(strArr[18]).append(getPlayerActions()).append(strArr[9]).append(getInputInteractionModel()).append(strArr[0]).append(getAnalogMoveVector()).append(strArr[15]).append(getPredictedVehicle()).append(strArr[19]).append(getVehicleRotation()).append(strArr[1]).append(a()).append(strArr[16]).append(b()).append(strArr[13]).append(c()).append(strArr[17]).append(d()).append(strArr[2]).append(e()).append(")").toString();
    }
}
