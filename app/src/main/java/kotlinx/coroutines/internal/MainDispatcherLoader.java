package kotlinx.coroutines.internal;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import kotlin.Metadata;
import kotlin.sequences.SequencesKt;
import kotlinx.coroutines.MainCoroutineDispatcher;

/* compiled from: MainDispatchers.kt */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/internal/MainDispatcherLoader;", "", "()V", "FAST_SERVICE_LOADER_ENABLED", "", "dispatcher", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "loadMainDispatcher", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class MainDispatcherLoader {
    public static final MainDispatcherLoader INSTANCE = new MainDispatcherLoader();
    private static final boolean FAST_SERVICE_LOADER_ENABLED = SystemPropsKt.systemProp("kotlinx.coroutines.fast.service.loader", true);
    public static final MainCoroutineDispatcher dispatcher = INSTANCE.loadMainDispatcher();

    private MainDispatcherLoader() {
    }

    private final MainCoroutineDispatcher loadMainDispatcher() {
        Object maxElem$iv;
        MainCoroutineDispatcher tryCreateDispatcher;
        try {
            List factories = FAST_SERVICE_LOADER_ENABLED ? FastServiceLoader.INSTANCE.loadMainDispatcherFactory$kotlinx_coroutines_core() : SequencesKt.toList(SequencesKt.asSequence(ServiceLoader.load(MainDispatcherFactory.class, MainDispatcherFactory.class.getClassLoader()).iterator()));
            Iterable $this$maxByOrNull$iv = factories;
            Iterator iterator$iv = $this$maxByOrNull$iv.iterator();
            if (iterator$iv.hasNext()) {
                maxElem$iv = iterator$iv.next();
                if (iterator$iv.hasNext()) {
                    MainDispatcherFactory it2 = (MainDispatcherFactory) maxElem$iv;
                    int maxValue$iv = it2.getLoadPriority();
                    do {
                        Object e$iv = iterator$iv.next();
                        MainDispatcherFactory it3 = (MainDispatcherFactory) e$iv;
                        int v$iv = it3.getLoadPriority();
                        if (maxValue$iv < v$iv) {
                            maxElem$iv = e$iv;
                            maxValue$iv = v$iv;
                        }
                    } while (iterator$iv.hasNext());
                }
            } else {
                maxElem$iv = null;
            }
            MainDispatcherFactory mainDispatcherFactory = (MainDispatcherFactory) maxElem$iv;
            return (mainDispatcherFactory == null || (tryCreateDispatcher = MainDispatchersKt.tryCreateDispatcher(mainDispatcherFactory, factories)) == null) ? MainDispatchersKt.createMissingDispatcher$default(null, null, 3, null) : tryCreateDispatcher;
        } catch (Throwable e) {
            return MainDispatchersKt.createMissingDispatcher$default(e, null, 2, null);
        }
    }
}
