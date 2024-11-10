package com.trossense;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import okhttp3.OkHttpClient;

/* loaded from: classes3.dex */
public class g {
    public static final String c;
    private static final Gson d;
    private static final String e;
    private static boolean f;
    private static final String[] g;
    private OkHttpClient a = new OkHttpClient();
    private Activity b;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0027. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[23];
        b(false);
        String str = "起叶\u0007rB\rD%\u0007m\u0004$r+u\u0002油再\u0002油再\u0007rB\rD%\u0007m\t$r+uH.]>o\u0002儔倽\u0003\u0000P厹\u0002髝诀\u0002髝诀\u0002瘪彔\u0003!v \t$r+uH.]>o\u0002起叶\u0002宗砀\fe2`5Rv\u0000`8})_\b<R&h\u0011-@6\u0002儔倽\u0002瘪彔\u0002匰寇";
        int length = "起叶\u0007rB\rD%\u0007m\u0004$r+u\u0002油再\u0002油再\u0007rB\rD%\u0007m\t$r+uH.]>o\u0002儔倽\u0003\u0000P厹\u0002髝诀\u0002髝诀\u0002瘪彔\u0003!v \t$r+uH.]>o\u0002起叶\u0002宗砀\fe2`5Rv\u0000`8})_\b<R&h\u0011-@6\u0002儔倽\u0002瘪彔\u0002匰寇".length();
        char c2 = 2;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 65;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a = a(i5, b(substring));
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
                        str = "/x.\u0004*|%{";
                        length = "/x.\u0004*|%{".length();
                        c2 = 3;
                        i2 = -1;
                        i3 = i;
                        i5 = 79;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c2;
                if (i2 >= length) {
                    g = strArr;
                    e = strArr[13];
                    c = strArr[16];
                    d = new GsonBuilder().setPrettyPrinting().create();
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 79;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    public g(Activity activity) {
        this.b = activity;
        a(activity);
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 16;
                    break;
                case 1:
                    i2 = 64;
                    break;
                case 2:
                    i2 = 15;
                    break;
                case 3:
                    i2 = 70;
                    break;
                case 4:
                    i2 = 39;
                    break;
                case 5:
                    i2 = 5;
                    break;
                default:
                    i2 = 111;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OkHttpClient a(g gVar) {
        return gVar.a;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(android.app.Activity r14) {
        /*
            Method dump skipped, instructions count: 250
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.g.a(android.app.Activity):void");
    }

    private void a(Activity activity, String str) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(1);
        EditText editText = new EditText(activity);
        String[] strArr = g;
        editText.setHint(strArr[0]);
        editText.setFocusable(false);
        editText.setText(str);
        linearLayout.addView(editText);
        EditText editText2 = new EditText(activity);
        editText2.setHint(strArr[20]);
        linearLayout.addView(editText2);
        final AlertDialog create = new AlertDialog.Builder(activity, android.R.style.Theme.Material.Light.Dialog.Alert).setTitle(strArr[9]).setCancelable(false).setView(linearLayout).setPositiveButton(strArr[7], (DialogInterface.OnClickListener) null).setNegativeButton(strArr[10], (DialogInterface.OnClickListener) null).create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.trossense.g$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(g.g[5])));
            }
        });
        create.setButton(-1, strArr[18], new l(this, editText2, editText));
        create.setButton(-2, strArr[10], new n(this, editText, activity));
        create.show();
    }

    private void a(DialogInterface dialogInterface, boolean z) {
        try {
            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField(g[17]);
            declaredField.setAccessible(true);
            declaredField.set(dialogInterface, Boolean.valueOf(z));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(g gVar, Activity activity, String str) {
        gVar.a(activity, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(g gVar, DialogInterface dialogInterface, boolean z) {
        gVar.a(dialogInterface, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(g gVar, String str) {
        gVar.a(str);
    }

    private void a(final String str) {
        this.b.runOnUiThread(new Runnable() { // from class: com.trossense.g$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                g.this.m209lambda$mkMsg$1$comtrossenseg(str);
            }
        });
    }

    public static void b(boolean z) {
        f = z;
    }

    public static boolean b() {
        return f;
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'o');
        }
        return charArray;
    }

    public static boolean c() {
        return !b();
    }

    public void a(String str, String str2) {
        JsonObject jsonObject = new JsonObject();
        String[] strArr = g;
        jsonObject.addProperty(strArr[2], str);
        jsonObject.addProperty(strArr[12], str2);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(this.b.getDataDir(), strArr[6])));
            d.toJson((JsonElement) jsonObject, (Appendable) bufferedWriter);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$mkMsg$1$com-trossense-g, reason: not valid java name */
    public void m209lambda$mkMsg$1$comtrossenseg(String str) {
        Toast.makeText(this.b.getBaseContext(), str, 1).show();
    }
}
