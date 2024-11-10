package org.jose4j.json.internal.json_simple.parser;

import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;

/* loaded from: classes5.dex */
class Yylex {
    public static final int STRING_BEGIN = 2;
    public static final int YYEOF = -1;
    public static final int YYINITIAL = 0;
    private static final String ZZ_ACTION_PACKED_0 = "\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018";
    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t";
    private static final int ZZ_BUFFERSIZE = 16384;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;
    private static final String ZZ_ROWMAP_PACKED_0 = "\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000¢\u0000½\u0000Ø\u00006\u00006\u00006\u00006\u00006\u00006\u0000ó\u0000Ď\u00006\u0000ĩ\u0000ń\u0000ş\u0000ź\u0000ƕ\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000ư\u0000ǋ\u0000Ǧ\u0000Ǧ\u0000ȁ\u0000Ȝ\u0000ȷ\u0000ɒ\u00006\u00006\u0000ɭ\u0000ʈ\u00006";
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private StringBuilder sb;
    private int yychar;
    private int yycolumn;
    private int yyline;
    private boolean zzAtBOL;
    private boolean zzAtEOF;
    private char[] zzBuffer;
    private int zzCurrentPos;
    private int zzEndRead;
    private int zzLexicalState;
    private int zzMarkedPos;
    private Reader zzReader;
    private int zzStartRead;
    private int zzState;
    private static final int[] ZZ_LEXSTATE = {0, 0, 1, 1};
    private static final String ZZ_CMAP_PACKED = "\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016ﾂ\u0000";
    private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);
    private static final int[] ZZ_ACTION = zzUnpackAction();
    private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
    private static final int[] ZZ_TRANS = {2, 2, 3, 4, 2, 2, 2, 5, 2, 6, 2, 2, 7, 8, 2, 9, 2, 2, 2, 2, 2, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, 19, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 34, 35, -1, -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, 39, -1, 39, -1, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 41, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, -1, 42, -1, 42, -1, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, 43, -1, 43, -1, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, 44, -1, 44, -1, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1};
    private static final String[] ZZ_ERROR_MSG = {"Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large"};
    private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

    private static int[] zzUnpackAction() {
        int[] result = new int[45];
        zzUnpackAction(ZZ_ACTION_PACKED_0, 0, result);
        return result;
    }

    private static int zzUnpackAction(String packed, int offset, int[] result) {
        int j;
        int count = 0;
        int j2 = offset;
        int l = packed.length();
        while (count < l) {
            int i = count + 1;
            int count2 = packed.charAt(count);
            int i2 = i + 1;
            int value = packed.charAt(i);
            while (true) {
                j = j2 + 1;
                result[j2] = value;
                count2--;
                if (count2 <= 0) {
                    break;
                }
                j2 = j;
            }
            count = i2;
            j2 = j;
        }
        return j2;
    }

    private static int[] zzUnpackRowMap() {
        int[] result = new int[45];
        zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, 0, result);
        return result;
    }

    private static int zzUnpackRowMap(String packed, int offset, int[] result) {
        int i = 0;
        int j = offset;
        int l = packed.length();
        while (i < l) {
            int i2 = i + 1;
            int high = packed.charAt(i) << 16;
            result[j] = packed.charAt(i2) | high;
            j++;
            i = i2 + 1;
        }
        return j;
    }

    private static int[] zzUnpackAttribute() {
        int[] result = new int[45];
        zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, 0, result);
        return result;
    }

    private static int zzUnpackAttribute(String packed, int offset, int[] result) {
        int j;
        int count = 0;
        int j2 = offset;
        int l = packed.length();
        while (count < l) {
            int i = count + 1;
            int count2 = packed.charAt(count);
            int i2 = i + 1;
            int value = packed.charAt(i);
            while (true) {
                j = j2 + 1;
                result[j2] = value;
                count2--;
                if (count2 <= 0) {
                    break;
                }
                j2 = j;
            }
            count = i2;
            j2 = j;
        }
        return j2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPosition() {
        return this.yychar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Yylex(Reader in) {
        this.zzLexicalState = 0;
        this.zzBuffer = new char[16384];
        this.zzAtBOL = true;
        this.sb = new StringBuilder();
        this.zzReader = in;
    }

    Yylex(InputStream in) {
        this(new InputStreamReader(in));
    }

    private static char[] zzUnpackCMap(String packed) {
        int j;
        char[] map = new char[65536];
        int count = 0;
        int j2 = 0;
        while (count < 90) {
            int i = count + 1;
            int count2 = packed.charAt(count);
            int i2 = i + 1;
            char value = packed.charAt(i);
            while (true) {
                j = j2 + 1;
                map[j2] = value;
                count2--;
                if (count2 <= 0) {
                    break;
                }
                j2 = j;
            }
            count = i2;
            j2 = j;
        }
        return map;
    }

    private boolean zzRefill() throws IOException {
        int c;
        if (this.zzStartRead > 0) {
            System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
            this.zzEndRead -= this.zzStartRead;
            this.zzCurrentPos -= this.zzStartRead;
            this.zzMarkedPos -= this.zzStartRead;
            this.zzStartRead = 0;
        }
        if (this.zzCurrentPos >= this.zzBuffer.length) {
            char[] newBuffer = new char[this.zzCurrentPos * 2];
            System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
            this.zzBuffer = newBuffer;
        }
        int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
        if (numRead > 0) {
            this.zzEndRead += numRead;
            return false;
        }
        if (numRead != 0 || (c = this.zzReader.read()) == -1) {
            return true;
        }
        char[] cArr = this.zzBuffer;
        int i = this.zzEndRead;
        this.zzEndRead = i + 1;
        cArr[i] = (char) c;
        return false;
    }

    public final void yyclose() throws IOException {
        this.zzAtEOF = true;
        this.zzEndRead = this.zzStartRead;
        if (this.zzReader != null) {
            this.zzReader.close();
        }
    }

    public final void yyreset(Reader reader) {
        this.zzReader = reader;
        this.zzAtBOL = true;
        this.zzAtEOF = false;
        this.zzStartRead = 0;
        this.zzEndRead = 0;
        this.zzMarkedPos = 0;
        this.zzCurrentPos = 0;
        this.yycolumn = 0;
        this.yychar = 0;
        this.yyline = 0;
        this.zzLexicalState = 0;
    }

    public final int yystate() {
        return this.zzLexicalState;
    }

    public final void yybegin(int newState) {
        this.zzLexicalState = newState;
    }

    public final String yytext() {
        return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }

    public final char yycharat(int pos) {
        return this.zzBuffer[this.zzStartRead + pos];
    }

    public final int yylength() {
        return this.zzMarkedPos - this.zzStartRead;
    }

    private void zzScanError(int errorCode) {
        String message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = ZZ_ERROR_MSG[0];
        }
        throw new Error(message);
    }

    public void yypushback(int number) {
        if (number > yylength()) {
            zzScanError(2);
        }
        this.zzMarkedPos -= number;
    }

    public Yytoken yylex() throws IOException, ParseException {
        int zzNext;
        int zzInput;
        int zzAction;
        char[] zzBufferL;
        int zzEndReadL;
        char c;
        int zzCurrentPosL;
        int zzEndReadL2 = this.zzEndRead;
        char[] zzBufferL2 = this.zzBuffer;
        char[] zzCMapL = ZZ_CMAP;
        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;
        while (true) {
            int zzMarkedPosL = this.zzMarkedPos;
            this.yychar += zzMarkedPosL - this.zzStartRead;
            int zzAction2 = -1;
            this.zzStartRead = zzMarkedPosL;
            this.zzCurrentPos = zzMarkedPosL;
            int zzInput2 = zzMarkedPosL;
            this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
            while (true) {
                if (zzInput2 < zzEndReadL2) {
                    zzCurrentPosL = zzInput2 + 1;
                    c = zzBufferL2[zzInput2];
                } else if (this.zzAtEOF) {
                    zzNext = -1;
                    zzInput = zzAction2;
                    zzAction = zzMarkedPosL;
                    zzBufferL = zzBufferL2;
                    zzEndReadL = zzEndReadL2;
                } else {
                    this.zzCurrentPos = zzInput2;
                    this.zzMarkedPos = zzMarkedPosL;
                    boolean eof = zzRefill();
                    int zzCurrentPosL2 = this.zzCurrentPos;
                    zzMarkedPosL = this.zzMarkedPos;
                    zzBufferL2 = this.zzBuffer;
                    zzEndReadL2 = this.zzEndRead;
                    if (eof) {
                        zzNext = -1;
                        zzInput = zzAction2;
                        zzAction = zzMarkedPosL;
                        zzBufferL = zzBufferL2;
                        zzEndReadL = zzEndReadL2;
                    } else {
                        int zzInput3 = zzCurrentPosL2 + 1;
                        c = zzBufferL2[zzCurrentPosL2];
                        zzCurrentPosL = zzInput3;
                    }
                }
                int zzNext2 = zzTransL[zzRowMapL[this.zzState] + zzCMapL[c]];
                if (zzNext2 == -1) {
                    zzNext = c;
                    zzInput = zzAction2;
                    zzAction = zzMarkedPosL;
                    zzBufferL = zzBufferL2;
                    zzEndReadL = zzEndReadL2;
                } else {
                    this.zzState = zzNext2;
                    int zzAttributes = zzAttrL[this.zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction2 = this.zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) == 8) {
                            zzNext = c;
                            zzInput = zzAction2;
                            zzAction = zzMarkedPosL;
                            zzBufferL = zzBufferL2;
                            zzEndReadL = zzEndReadL2;
                        }
                    }
                    zzInput2 = zzCurrentPosL;
                }
            }
            this.zzMarkedPos = zzAction;
            switch (zzInput < 0 ? zzInput : ZZ_ACTION[zzInput]) {
                case 1:
                    throw new ParseException(this.yychar, 0, new Character(yycharat(0)));
                case 2:
                    String yytext = yytext();
                    try {
                        Long val = Long.valueOf(yytext);
                        return new Yytoken(0, val);
                    } catch (NumberFormatException e) {
                        BigInteger val2 = new BigInteger(yytext);
                        return new Yytoken(0, val2);
                    }
                case 3:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                    break;
                case 4:
                    this.sb = null;
                    this.sb = new StringBuilder();
                    yybegin(2);
                    break;
                case 5:
                    return new Yytoken(1, null);
                case 6:
                    return new Yytoken(2, null);
                case 7:
                    return new Yytoken(3, null);
                case 8:
                    return new Yytoken(4, null);
                case 9:
                    return new Yytoken(5, null);
                case 10:
                    return new Yytoken(6, null);
                case 11:
                    this.sb.append(yytext());
                    break;
                case 12:
                    this.sb.append('\\');
                    break;
                case 13:
                    yybegin(0);
                    return new Yytoken(0, this.sb.toString());
                case 14:
                    this.sb.append('\"');
                    break;
                case 15:
                    this.sb.append('/');
                    break;
                case 16:
                    this.sb.append('\b');
                    break;
                case 17:
                    this.sb.append('\f');
                    break;
                case 18:
                    this.sb.append('\n');
                    break;
                case 19:
                    this.sb.append(StringUtil.CARRIAGE_RETURN);
                    break;
                case 20:
                    this.sb.append('\t');
                    break;
                case 21:
                    Double val3 = Double.valueOf(yytext());
                    return new Yytoken(0, val3);
                case 22:
                    return new Yytoken(0, null);
                case 23:
                    Boolean val4 = Boolean.valueOf(yytext());
                    return new Yytoken(0, val4);
                case 24:
                    try {
                        int ch = Integer.parseInt(yytext().substring(2), 16);
                        this.sb.append((char) ch);
                        break;
                    } catch (Exception e2) {
                        throw new ParseException(this.yychar, 2, e2);
                    }
                default:
                    if (zzNext == -1 && this.zzStartRead == this.zzCurrentPos) {
                        this.zzAtEOF = true;
                        return null;
                    }
                    zzScanError(1);
                    break;
                    break;
            }
            zzEndReadL2 = zzEndReadL;
            zzBufferL2 = zzBufferL;
        }
    }
}
