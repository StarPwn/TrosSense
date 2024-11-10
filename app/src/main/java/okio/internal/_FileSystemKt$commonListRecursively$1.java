package okio.internal;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;
import okio.FileSystem;
import okio.Path;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: -FileSystem.kt */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Lokio/Path;"}, k = 3, mv = {1, 6, 0}, xi = 48)
@DebugMetadata(c = "okio.internal._FileSystemKt$commonListRecursively$1", f = "-FileSystem.kt", i = {0, 0}, l = {93}, m = "invokeSuspend", n = {"$this$sequence", "stack"}, s = {"L$0", "L$1"})
/* loaded from: classes5.dex */
public final class _FileSystemKt$commonListRecursively$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super Path>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Path $dir;
    final /* synthetic */ boolean $followSymlinks;
    final /* synthetic */ FileSystem $this_commonListRecursively;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public _FileSystemKt$commonListRecursively$1(Path path, FileSystem fileSystem, boolean z, Continuation<? super _FileSystemKt$commonListRecursively$1> continuation) {
        super(2, continuation);
        this.$dir = path;
        this.$this_commonListRecursively = fileSystem;
        this.$followSymlinks = z;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        _FileSystemKt$commonListRecursively$1 _filesystemkt_commonlistrecursively_1 = new _FileSystemKt$commonListRecursively$1(this.$dir, this.$this_commonListRecursively, this.$followSymlinks, continuation);
        _filesystemkt_commonlistrecursively_1.L$0 = obj;
        return _filesystemkt_commonlistrecursively_1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<? super Path> sequenceScope, Continuation<? super Unit> continuation) {
        return ((_FileSystemKt$commonListRecursively$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        _FileSystemKt$commonListRecursively$1 _filesystemkt_commonlistrecursively_1;
        SequenceScope $this$sequence;
        ArrayDeque stack;
        Iterator<Path> it2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                _filesystemkt_commonlistrecursively_1 = this;
                SequenceScope $this$sequence2 = (SequenceScope) _filesystemkt_commonlistrecursively_1.L$0;
                ArrayDeque stack2 = new ArrayDeque();
                stack2.addLast(_filesystemkt_commonlistrecursively_1.$dir);
                $this$sequence = $this$sequence2;
                stack = stack2;
                it2 = _filesystemkt_commonlistrecursively_1.$this_commonListRecursively.list(_filesystemkt_commonlistrecursively_1.$dir).iterator();
                break;
            case 1:
                _filesystemkt_commonlistrecursively_1 = this;
                it2 = (Iterator) _filesystemkt_commonlistrecursively_1.L$2;
                ArrayDeque stack3 = (ArrayDeque) _filesystemkt_commonlistrecursively_1.L$1;
                SequenceScope $this$sequence3 = (SequenceScope) _filesystemkt_commonlistrecursively_1.L$0;
                ResultKt.throwOnFailure($result);
                stack = stack3;
                $this$sequence = $this$sequence3;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it2.hasNext()) {
            Path next = it2.next();
            _filesystemkt_commonlistrecursively_1.L$0 = $this$sequence;
            _filesystemkt_commonlistrecursively_1.L$1 = stack;
            _filesystemkt_commonlistrecursively_1.L$2 = it2;
            _filesystemkt_commonlistrecursively_1.label = 1;
            if (_FileSystemKt.collectRecursively($this$sequence, _filesystemkt_commonlistrecursively_1.$this_commonListRecursively, stack, next, _filesystemkt_commonlistrecursively_1.$followSymlinks, false, _filesystemkt_commonlistrecursively_1) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}
