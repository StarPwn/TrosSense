package org.jose4j.lang;

/* loaded from: classes5.dex */
public class ExceptionHelp {
    public static String toStringWithCauses(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t);
        while (t.getCause() != null) {
            t = t.getCause();
            sb.append("; caused by: ").append(t);
        }
        return sb.toString();
    }

    public static String toStringWithCausesAndAbbreviatedStack(Throwable t, Class stopAt) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (t != null) {
            if (!first) {
                sb.append("; caused by: ");
            }
            sb.append(t).append(" at ");
            StackTraceElement[] stackTrace = t.getStackTrace();
            int length = stackTrace.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    StackTraceElement ste = stackTrace[i];
                    if (ste.getClassName().equals(stopAt.getName())) {
                        sb.append("...omitted...");
                        break;
                    }
                    sb.append(ste).append("; ");
                    i++;
                }
            }
            t = t.getCause();
            first = false;
        }
        return sb.toString();
    }
}
