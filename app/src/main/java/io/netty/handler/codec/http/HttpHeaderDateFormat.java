package io.netty.handler.codec.http;

import io.netty.util.concurrent.FastThreadLocal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Deprecated
/* loaded from: classes4.dex */
public final class HttpHeaderDateFormat extends SimpleDateFormat {
    private static final FastThreadLocal<HttpHeaderDateFormat> dateFormatThreadLocal = new FastThreadLocal<HttpHeaderDateFormat>() { // from class: io.netty.handler.codec.http.HttpHeaderDateFormat.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public HttpHeaderDateFormat initialValue() {
            return new HttpHeaderDateFormat();
        }
    };
    private static final long serialVersionUID = -925286159755905325L;
    private final SimpleDateFormat format1;
    private final SimpleDateFormat format2;

    public static HttpHeaderDateFormat get() {
        return dateFormatThreadLocal.get();
    }

    private HttpHeaderDateFormat() {
        super("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        this.format1 = new HttpHeaderDateFormatObsolete1();
        this.format2 = new HttpHeaderDateFormatObsolete2();
        setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override // java.text.SimpleDateFormat, java.text.DateFormat
    public Date parse(String text, ParsePosition pos) {
        Date date = super.parse(text, pos);
        if (date == null) {
            date = this.format1.parse(text, pos);
        }
        if (date == null) {
            return this.format2.parse(text, pos);
        }
        return date;
    }

    /* loaded from: classes4.dex */
    private static final class HttpHeaderDateFormatObsolete1 extends SimpleDateFormat {
        private static final long serialVersionUID = -3178072504225114298L;

        HttpHeaderDateFormatObsolete1() {
            super("E, dd-MMM-yy HH:mm:ss z", Locale.ENGLISH);
            setTimeZone(TimeZone.getTimeZone("GMT"));
        }
    }

    /* loaded from: classes4.dex */
    private static final class HttpHeaderDateFormatObsolete2 extends SimpleDateFormat {
        private static final long serialVersionUID = 3010674519968303714L;

        HttpHeaderDateFormatObsolete2() {
            super("E MMM d HH:mm:ss yyyy", Locale.ENGLISH);
            setTimeZone(TimeZone.getTimeZone("GMT"));
        }
    }
}
