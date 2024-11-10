package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.net.ssl.X509KeyManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class OpenSslCachingKeyMaterialProvider extends OpenSslKeyMaterialProvider {
    private final ConcurrentMap<String, OpenSslKeyMaterial> cache;
    private volatile boolean full;
    private final int maxCachedEntries;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslCachingKeyMaterialProvider(X509KeyManager keyManager, String password, int maxCachedEntries) {
        super(keyManager, password);
        this.cache = new ConcurrentHashMap();
        this.maxCachedEntries = maxCachedEntries;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.ssl.OpenSslKeyMaterialProvider
    public OpenSslKeyMaterial chooseKeyMaterial(ByteBufAllocator allocator, String alias) throws Exception {
        OpenSslKeyMaterial material = this.cache.get(alias);
        if (material == null) {
            material = super.chooseKeyMaterial(allocator, alias);
            if (material == null) {
                return null;
            }
            if (this.full) {
                return material;
            }
            if (this.cache.size() > this.maxCachedEntries) {
                this.full = true;
                return material;
            }
            OpenSslKeyMaterial old = this.cache.putIfAbsent(alias, material);
            if (old != null) {
                material.release();
                material = old;
            }
        }
        return material.retain();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // io.netty.handler.ssl.OpenSslKeyMaterialProvider
    public void destroy() {
        do {
            Iterator<OpenSslKeyMaterial> iterator = this.cache.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().release();
                iterator.remove();
            }
        } while (!this.cache.isEmpty());
    }
}
