package org.cloudburstmc.protocol.bedrock.util;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

/* loaded from: classes5.dex */
public final class XblUtils {
    private static final URL REQUEST_URL;
    private static final String REQUEST_URL_STRING = "https://login.live.com/oauth20_authorize.srf?client_id=00000000441cc96b&redirect_uri=https://login.live.com/oauth20_desktop.srf&response_type=token&display=touch&scope=service::user.auth.xboxlive.com::MBI_SSL&locale=en";
    private static final URL TOKEN_URL;
    private static final String TOKEN_URL_STRING = "https://login.live.com/oauth20.srf";
    private static final InternalLogger log = InternalLoggerFactory.getInstance((Class<?>) XblUtils.class);

    private XblUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        try {
            TOKEN_URL = new URL(TOKEN_URL_STRING);
            REQUEST_URL = new URL(REQUEST_URL_STRING);
        } catch (MalformedURLException e) {
            throw new AssertionError("Unable to create XBL URLs", e);
        }
    }

    public static CompletableFuture test() throws IOException {
        final CompletableFuture future = new CompletableFuture();
        ForkJoinPool.commonPool().execute(new Runnable() { // from class: org.cloudburstmc.protocol.bedrock.util.XblUtils$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                XblUtils.lambda$test$0(future);
            }
        });
        return CompletableFuture.supplyAsync(new Supplier() { // from class: org.cloudburstmc.protocol.bedrock.util.XblUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                return XblUtils.lambda$test$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$test$0(CompletableFuture future) {
        try {
            HttpURLConnection connection = (HttpURLConnection) TOKEN_URL.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            try {
                StringBuilder buffer = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line != null) {
                        buffer.append(line);
                    } else {
                        String response = buffer.toString();
                        in.close();
                        log.debug("RESPONSE\n{}", response);
                        return;
                    }
                }
            } finally {
            }
        } catch (Throwable e) {
            future.completeExceptionally(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$test$1() {
        return null;
    }
}
