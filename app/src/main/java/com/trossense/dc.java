package com.trossense;

import android.opengl.GLES20;

/* loaded from: classes3.dex */
public class dc {
    private static final String[] a;

    static {
        String[] strArr = new String[3];
        int length = ")o9Z|N$|l1H'\u001c<\u0003L\u0006l^]=.h(\u0007\u0019]=(s9^fH,|w5_'\u001c?\fn#UgU&2:Z]gH;5c%Hv\u001c?9bb\u001cri?g\u000b&]aE 2fpJv_{|w\u0005J(6?3h4\u001c~] 2)yG\u0019\u001ci;m\u000fl|O (h?R3\u0001i)^\u001djCq((s9D3\u0016i*Q?OzH 3ok63\u001c?\twp\u00013]\u001c*:ZAk)o9Z|N$|l1H'\u001c<\u0003L\u0006l^]=.h(\u0007\u0019]=(s9^fH,|w5_'\u001c?\fn#UgU&2:ZJ|U-|l1U}\u0014`'\u000bp\u001ctP\u0016\fn#UgU&2!m\u001cfc\u0004\nQ\u001d]gN $!z\u001cel&/h$U|RrV|U,s5_zO 3opQvX )l \u001cuP&=uk6fR :n\"Q3J,?5pJPS%3sk6eS 8!=]zRauzZ\u001c3[%\u0003G\"]t\u007f&0n\"\u001c.\u001c?\u001fn<Sa\u0007C!".length();
        int i = 0;
        char c = 158;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(bl.bm, b(")o9Z|N$|l1H'\u001c<\u0003L\u0006l^]=.h(\u0007\u0019]=(s9^fH,|w5_'\u001c?\fn#UgU&2:Z]gH;5c%Hv\u001c?9bb\u001cri?g\u000b&]aE 2fpJv_{|w\u0005J(6?3h4\u001c~] 2)yG\u0019\u001ci;m\u000fl|O (h?R3\u0001i)^\u001djCq((s9D3\u0016i*Q?OzH 3ok63\u001c?\twp\u00013]\u001c*:ZAk)o9Z|N$|l1H'\u001c<\u0003L\u0006l^]=.h(\u0007\u0019]=(s9^fH,|w5_'\u001c?\fn#UgU&2:ZJ|U-|l1U}\u0014`'\u000bp\u001ctP\u0016\fn#UgU&2!m\u001cfc\u0004\nQ\u001d]gN $!z\u001cel&/h$U|RrV|U,s5_zO 3opQvX )l \u001cuP&=uk6fR :n\"Q3J,?5pJPS%3sk6eS 8!=]zRauzZ\u001c3[%\u0003G\"]t\u007f&0n\"\u001c.\u001c?\u001fn<Sa\u0007C!".substring(i3, i4)));
            if (i4 >= length) {
                a = strArr;
                return;
            } else {
                i2 = i4;
                c = ")o9Z|N$|l1H'\u001c<\u0003L\u0006l^]=.h(\u0007\u0019]=(s9^fH,|w5_'\u001c?\fn#UgU&2:Z]gH;5c%Hv\u001c?9bb\u001cri?g\u000b&]aE 2fpJv_{|w\u0005J(6?3h4\u001c~] 2)yG\u0019\u001ci;m\u000fl|O (h?R3\u0001i)^\u001djCq((s9D3\u0016i*Q?OzH 3ok63\u001c?\twp\u00013]\u001c*:ZAk)o9Z|N$|l1H'\u001c<\u0003L\u0006l^]=.h(\u0007\u0019]=(s9^fH,|w5_'\u001c?\fn#UgU&2:ZJ|U-|l1U}\u0014`'\u000bp\u001ctP\u0016\fn#UgU&2!m\u001cfc\u0004\nQ\u001d]gN $!z\u001cel&/h$U|RrV|U,s5_zO 3opQvX )l \u001cuP&=uk6fR :n\"Q3J,?5pJPS%3sk6eS 8!=]zRauzZ\u001c3[%\u0003G\"]t\u007f&0n\"\u001c.\u001c?\u001fn<Sa\u0007C!".charAt(i4);
                i = i5;
            }
        }
    }

    public static db a() {
        String[] strArr = a;
        return a(strArr[2], strArr[1]);
    }

    public static db a(String str) {
        return a(str, a[0]);
    }

    public static db a(String str, String str2) {
        int glCreateShader = GLES20.glCreateShader(35632);
        int glCreateShader2 = GLES20.glCreateShader(35633);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glShaderSource(glCreateShader2, str2);
        GLES20.glCompileShader(glCreateShader);
        GLES20.glCompileShader(glCreateShader2);
        int glCreateProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(glCreateProgram, glCreateShader);
        GLES20.glAttachShader(glCreateProgram, glCreateShader2);
        GLES20.glValidateProgram(glCreateProgram);
        GLES20.glLinkProgram(glCreateProgram);
        GLES20.glDeleteShader(glCreateShader);
        GLES20.glDeleteShader(glCreateShader2);
        return new db(glCreateProgram);
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 65;
            switch (i2 % 7) {
                case 0:
                    i3 = 33;
                    break;
                case 1:
                    i3 = 124;
                    break;
                case 2:
                    i3 = 45;
                    break;
                case 3:
                case 5:
                    break;
                case 4:
                    i3 = 110;
                    break;
                default:
                    i3 = 52;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '4');
        }
        return charArray;
    }
}
