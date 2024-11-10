package com.trossense.sdk;

import com.trossense.dj;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.cloudburstmc.protocol.common.DefinitionRegistry;

/* loaded from: classes3.dex */
public class d implements DefinitionRegistry<BlockDefinition> {
    private static final long b = dj.a(6136195345308771400L, -4369717393756852384L, MethodHandles.lookup().lookupClass()).a(199505415010038L);
    private static final String[] c;
    private HashMap<Integer, b> a = new HashMap<>();

    static {
        String[] strArr = new String[3];
        int length = "<a@[m\f\u0004!tLJ\t=`O[a\u0012M\u0006q".length();
        int i = 0;
        char c2 = 6;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c2 + i3;
            int i5 = i + 1;
            strArr[i] = a(70, a("<a@[m\f\u0004!tLJ\t=`O[a\u0012M\u0006q".substring(i3, i4)));
            if (i4 >= length) {
                c = strArr;
                return;
            } else {
                i2 = i4;
                c2 = "<a@[m\f\u0004!tLJ\t=`O[a\u0012M\u0006q".charAt(i4);
                i = i5;
            }
        }
    }

    public d(short s, InputStream inputStream, short s2, int i) {
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(inputStream);
            NBTInputStream nBTInputStream = new NBTInputStream(new DataInputStream(gZIPInputStream));
            while (gZIPInputStream.available() > 0) {
                NbtMap nbtMap = (NbtMap) nBTInputStream.readTag();
                String[] strArr = c;
                String string = nbtMap.getString(strArr[1]);
                int i2 = nbtMap.getInt(strArr[2]);
                this.a.put(Integer.valueOf(i2), new b(string, i2, nbtMap.getCompound(strArr[0])));
                if (s < 0 || s < 0) {
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 9;
                    break;
                case 1:
                    i2 = 83;
                    break;
                case 2:
                    i2 = 103;
                    break;
                case 3:
                    i2 = 105;
                    break;
                case 4:
                    i2 = 78;
                    break;
                case 5:
                    i2 = 57;
                    break;
                default:
                    i2 = 110;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'n');
        }
        return charArray;
    }

    @Override // org.cloudburstmc.protocol.common.DefinitionRegistry
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public BlockDefinition getDefinition(int i) {
        return this.a.getOrDefault(Integer.valueOf(i), new c(i));
    }

    @Override // org.cloudburstmc.protocol.common.DefinitionRegistry
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public boolean isRegistered(BlockDefinition blockDefinition) {
        return true;
    }
}
