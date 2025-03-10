package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewCompat;
import androidx.fragment.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class SpecialEffectsController {
    private final ViewGroup mContainer;
    final ArrayList<Operation> mPendingOperations = new ArrayList<>();
    final ArrayList<Operation> mRunningOperations = new ArrayList<>();
    boolean mOperationDirectionIsPop = false;
    boolean mIsContainerPostponed = false;

    abstract void executeOperations(List<Operation> list, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpecialEffectsController getOrCreateController(ViewGroup container, FragmentManager fragmentManager) {
        SpecialEffectsControllerFactory factory = fragmentManager.getSpecialEffectsControllerFactory();
        return getOrCreateController(container, factory);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpecialEffectsController getOrCreateController(ViewGroup container, SpecialEffectsControllerFactory factory) {
        Object controller = container.getTag(R.id.special_effects_controller_view_tag);
        if (controller instanceof SpecialEffectsController) {
            return (SpecialEffectsController) controller;
        }
        SpecialEffectsController newController = factory.createController(container);
        container.setTag(R.id.special_effects_controller_view_tag, newController);
        return newController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpecialEffectsController(ViewGroup container) {
        this.mContainer = container;
    }

    public ViewGroup getContainer() {
        return this.mContainer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Operation.LifecycleImpact getAwaitingCompletionLifecycleImpact(FragmentStateManager fragmentStateManager) {
        Operation.LifecycleImpact lifecycleImpact = null;
        Operation pendingOperation = findPendingOperation(fragmentStateManager.getFragment());
        if (pendingOperation != null) {
            lifecycleImpact = pendingOperation.getLifecycleImpact();
        }
        Operation runningOperation = findRunningOperation(fragmentStateManager.getFragment());
        if (runningOperation != null && (lifecycleImpact == null || lifecycleImpact == Operation.LifecycleImpact.NONE)) {
            return runningOperation.getLifecycleImpact();
        }
        return lifecycleImpact;
    }

    private Operation findPendingOperation(Fragment fragment) {
        Iterator<Operation> it2 = this.mPendingOperations.iterator();
        while (it2.hasNext()) {
            Operation operation = it2.next();
            if (operation.getFragment().equals(fragment) && !operation.isCanceled()) {
                return operation;
            }
        }
        return null;
    }

    private Operation findRunningOperation(Fragment fragment) {
        Iterator<Operation> it2 = this.mRunningOperations.iterator();
        while (it2.hasNext()) {
            Operation operation = it2.next();
            if (operation.getFragment().equals(fragment) && !operation.isCanceled()) {
                return operation;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueAdd(Operation.State finalState, FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(FragmentManager.TAG, "SpecialEffectsController: Enqueuing add operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(finalState, Operation.LifecycleImpact.ADDING, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueShow(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(FragmentManager.TAG, "SpecialEffectsController: Enqueuing show operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(Operation.State.VISIBLE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueHide(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(FragmentManager.TAG, "SpecialEffectsController: Enqueuing hide operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(Operation.State.GONE, Operation.LifecycleImpact.NONE, fragmentStateManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enqueueRemove(FragmentStateManager fragmentStateManager) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(FragmentManager.TAG, "SpecialEffectsController: Enqueuing remove operation for fragment " + fragmentStateManager.getFragment());
        }
        enqueue(Operation.State.REMOVED, Operation.LifecycleImpact.REMOVING, fragmentStateManager);
    }

    private void enqueue(Operation.State finalState, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager) {
        synchronized (this.mPendingOperations) {
            CancellationSignal signal = new CancellationSignal();
            Operation existingOperation = findPendingOperation(fragmentStateManager.getFragment());
            if (existingOperation != null) {
                existingOperation.mergeWith(finalState, lifecycleImpact);
                return;
            }
            final FragmentStateManagerOperation operation = new FragmentStateManagerOperation(finalState, lifecycleImpact, fragmentStateManager, signal);
            this.mPendingOperations.add(operation);
            operation.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.SpecialEffectsController.1
                @Override // java.lang.Runnable
                public void run() {
                    if (SpecialEffectsController.this.mPendingOperations.contains(operation)) {
                        operation.getFinalState().applyState(operation.getFragment().mView);
                    }
                }
            });
            operation.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.SpecialEffectsController.2
                @Override // java.lang.Runnable
                public void run() {
                    SpecialEffectsController.this.mPendingOperations.remove(operation);
                    SpecialEffectsController.this.mRunningOperations.remove(operation);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateOperationDirection(boolean isPop) {
        this.mOperationDirectionIsPop = isPop;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void markPostponedState() {
        synchronized (this.mPendingOperations) {
            updateFinalState();
            this.mIsContainerPostponed = false;
            int index = this.mPendingOperations.size() - 1;
            while (true) {
                if (index < 0) {
                    break;
                }
                Operation operation = this.mPendingOperations.get(index);
                Operation.State currentState = Operation.State.from(operation.getFragment().mView);
                if (operation.getFinalState() != Operation.State.VISIBLE || currentState == Operation.State.VISIBLE) {
                    index--;
                } else {
                    Fragment fragment = operation.getFragment();
                    this.mIsContainerPostponed = fragment.isPostponed();
                    break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forcePostponedExecutePendingOperations() {
        if (this.mIsContainerPostponed) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(FragmentManager.TAG, "SpecialEffectsController: Forcing postponed operations");
            }
            this.mIsContainerPostponed = false;
            executePendingOperations();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void executePendingOperations() {
        if (this.mIsContainerPostponed) {
            return;
        }
        if (!ViewCompat.isAttachedToWindow(this.mContainer)) {
            forceCompleteAllOperations();
            this.mOperationDirectionIsPop = false;
            return;
        }
        synchronized (this.mPendingOperations) {
            if (!this.mPendingOperations.isEmpty()) {
                ArrayList<Operation> currentlyRunningOperations = new ArrayList<>(this.mRunningOperations);
                this.mRunningOperations.clear();
                Iterator<Operation> it2 = currentlyRunningOperations.iterator();
                while (it2.hasNext()) {
                    Operation operation = it2.next();
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v(FragmentManager.TAG, "SpecialEffectsController: Cancelling operation " + operation);
                    }
                    operation.cancel();
                    if (!operation.isComplete()) {
                        this.mRunningOperations.add(operation);
                    }
                }
                updateFinalState();
                ArrayList<Operation> newPendingOperations = new ArrayList<>(this.mPendingOperations);
                this.mPendingOperations.clear();
                this.mRunningOperations.addAll(newPendingOperations);
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(FragmentManager.TAG, "SpecialEffectsController: Executing pending operations");
                }
                Iterator<Operation> it3 = newPendingOperations.iterator();
                while (it3.hasNext()) {
                    it3.next().onStart();
                }
                executeOperations(newPendingOperations, this.mOperationDirectionIsPop);
                this.mOperationDirectionIsPop = false;
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(FragmentManager.TAG, "SpecialEffectsController: Finished executing pending operations");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forceCompleteAllOperations() {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(FragmentManager.TAG, "SpecialEffectsController: Forcing all operations to complete");
        }
        boolean attachedToWindow = ViewCompat.isAttachedToWindow(this.mContainer);
        synchronized (this.mPendingOperations) {
            updateFinalState();
            Iterator<Operation> it2 = this.mPendingOperations.iterator();
            while (it2.hasNext()) {
                it2.next().onStart();
            }
            ArrayList<Operation> runningOperations = new ArrayList<>(this.mRunningOperations);
            Iterator<Operation> it3 = runningOperations.iterator();
            while (it3.hasNext()) {
                Operation operation = it3.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(FragmentManager.TAG, "SpecialEffectsController: " + (attachedToWindow ? "" : "Container " + this.mContainer + " is not attached to window. ") + "Cancelling running operation " + operation);
                }
                operation.cancel();
            }
            ArrayList<Operation> pendingOperations = new ArrayList<>(this.mPendingOperations);
            Iterator<Operation> it4 = pendingOperations.iterator();
            while (it4.hasNext()) {
                Operation operation2 = it4.next();
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(FragmentManager.TAG, "SpecialEffectsController: " + (attachedToWindow ? "" : "Container " + this.mContainer + " is not attached to window. ") + "Cancelling pending operation " + operation2);
                }
                operation2.cancel();
            }
        }
    }

    private void updateFinalState() {
        Iterator<Operation> it2 = this.mPendingOperations.iterator();
        while (it2.hasNext()) {
            Operation operation = it2.next();
            if (operation.getLifecycleImpact() == Operation.LifecycleImpact.ADDING) {
                Fragment fragment = operation.getFragment();
                View view = fragment.requireView();
                Operation.State finalState = Operation.State.from(view.getVisibility());
                operation.mergeWith(finalState, Operation.LifecycleImpact.NONE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Operation {
        private State mFinalState;
        private final Fragment mFragment;
        private LifecycleImpact mLifecycleImpact;
        private final List<Runnable> mCompletionListeners = new ArrayList();
        private final HashSet<CancellationSignal> mSpecialEffectsSignals = new HashSet<>();
        private boolean mIsCanceled = false;
        private boolean mIsComplete = false;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public enum LifecycleImpact {
            NONE,
            ADDING,
            REMOVING
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public enum State {
            REMOVED,
            VISIBLE,
            GONE,
            INVISIBLE;

            /* JADX INFO: Access modifiers changed from: package-private */
            public static State from(View view) {
                if (view.getAlpha() == 0.0f && view.getVisibility() == 0) {
                    return INVISIBLE;
                }
                return from(view.getVisibility());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static State from(int visibility) {
                switch (visibility) {
                    case 0:
                        return VISIBLE;
                    case 4:
                        return INVISIBLE;
                    case 8:
                        return GONE;
                    default:
                        throw new IllegalArgumentException("Unknown visibility " + visibility);
                }
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public void applyState(View view) {
                switch (this) {
                    case REMOVED:
                        ViewGroup parent = (ViewGroup) view.getParent();
                        if (parent != null) {
                            if (FragmentManager.isLoggingEnabled(2)) {
                                Log.v(FragmentManager.TAG, "SpecialEffectsController: Removing view " + view + " from container " + parent);
                            }
                            parent.removeView(view);
                            return;
                        }
                        return;
                    case VISIBLE:
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v(FragmentManager.TAG, "SpecialEffectsController: Setting view " + view + " to VISIBLE");
                        }
                        view.setVisibility(0);
                        return;
                    case GONE:
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v(FragmentManager.TAG, "SpecialEffectsController: Setting view " + view + " to GONE");
                        }
                        view.setVisibility(8);
                        return;
                    case INVISIBLE:
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v(FragmentManager.TAG, "SpecialEffectsController: Setting view " + view + " to INVISIBLE");
                        }
                        view.setVisibility(4);
                        return;
                    default:
                        return;
                }
            }
        }

        Operation(State finalState, LifecycleImpact lifecycleImpact, Fragment fragment, CancellationSignal cancellationSignal) {
            this.mFinalState = finalState;
            this.mLifecycleImpact = lifecycleImpact;
            this.mFragment = fragment;
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: androidx.fragment.app.SpecialEffectsController.Operation.1
                @Override // androidx.core.os.CancellationSignal.OnCancelListener
                public void onCancel() {
                    Operation.this.cancel();
                }
            });
        }

        public State getFinalState() {
            return this.mFinalState;
        }

        LifecycleImpact getLifecycleImpact() {
            return this.mLifecycleImpact;
        }

        public final Fragment getFragment() {
            return this.mFragment;
        }

        final boolean isCanceled() {
            return this.mIsCanceled;
        }

        public String toString() {
            return "Operation {" + Integer.toHexString(System.identityHashCode(this)) + "} {mFinalState = " + this.mFinalState + "} {mLifecycleImpact = " + this.mLifecycleImpact + "} {mFragment = " + this.mFragment + "}";
        }

        final void cancel() {
            if (isCanceled()) {
                return;
            }
            this.mIsCanceled = true;
            if (this.mSpecialEffectsSignals.isEmpty()) {
                complete();
                return;
            }
            ArrayList<CancellationSignal> signals = new ArrayList<>(this.mSpecialEffectsSignals);
            Iterator<CancellationSignal> it2 = signals.iterator();
            while (it2.hasNext()) {
                CancellationSignal signal = it2.next();
                signal.cancel();
            }
        }

        final void mergeWith(State finalState, LifecycleImpact lifecycleImpact) {
            switch (lifecycleImpact) {
                case ADDING:
                    if (this.mFinalState == State.REMOVED) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v(FragmentManager.TAG, "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = " + this.mLifecycleImpact + " to ADDING.");
                        }
                        this.mFinalState = State.VISIBLE;
                        this.mLifecycleImpact = LifecycleImpact.ADDING;
                        return;
                    }
                    return;
                case REMOVING:
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v(FragmentManager.TAG, "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = " + this.mFinalState + " -> REMOVED. mLifecycleImpact  = " + this.mLifecycleImpact + " to REMOVING.");
                    }
                    this.mFinalState = State.REMOVED;
                    this.mLifecycleImpact = LifecycleImpact.REMOVING;
                    return;
                case NONE:
                    if (this.mFinalState != State.REMOVED) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v(FragmentManager.TAG, "SpecialEffectsController: For fragment " + this.mFragment + " mFinalState = " + this.mFinalState + " -> " + finalState + ". ");
                        }
                        this.mFinalState = finalState;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void addCompletionListener(Runnable listener) {
            this.mCompletionListeners.add(listener);
        }

        void onStart() {
        }

        public final void markStartedSpecialEffect(CancellationSignal signal) {
            onStart();
            this.mSpecialEffectsSignals.add(signal);
        }

        public final void completeSpecialEffect(CancellationSignal signal) {
            if (this.mSpecialEffectsSignals.remove(signal) && this.mSpecialEffectsSignals.isEmpty()) {
                complete();
            }
        }

        final boolean isComplete() {
            return this.mIsComplete;
        }

        public void complete() {
            if (this.mIsComplete) {
                return;
            }
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(FragmentManager.TAG, "SpecialEffectsController: " + this + " has called complete.");
            }
            this.mIsComplete = true;
            for (Runnable listener : this.mCompletionListeners) {
                listener.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FragmentStateManagerOperation extends Operation {
        private final FragmentStateManager mFragmentStateManager;

        FragmentStateManagerOperation(Operation.State finalState, Operation.LifecycleImpact lifecycleImpact, FragmentStateManager fragmentStateManager, CancellationSignal cancellationSignal) {
            super(finalState, lifecycleImpact, fragmentStateManager.getFragment(), cancellationSignal);
            this.mFragmentStateManager = fragmentStateManager;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Operation
        void onStart() {
            if (getLifecycleImpact() == Operation.LifecycleImpact.ADDING) {
                Fragment fragment = this.mFragmentStateManager.getFragment();
                View focusedView = fragment.mView.findFocus();
                if (focusedView != null) {
                    fragment.setFocusedView(focusedView);
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v(FragmentManager.TAG, "requestFocus: Saved focused view " + focusedView + " for Fragment " + fragment);
                    }
                }
                View view = getFragment().requireView();
                if (view.getParent() == null) {
                    this.mFragmentStateManager.addViewToContainer();
                    view.setAlpha(0.0f);
                }
                if (view.getAlpha() == 0.0f && view.getVisibility() == 0) {
                    view.setVisibility(4);
                }
                view.setAlpha(fragment.getPostOnViewCreatedAlpha());
                return;
            }
            if (getLifecycleImpact() == Operation.LifecycleImpact.REMOVING) {
                Fragment fragment2 = this.mFragmentStateManager.getFragment();
                View view2 = fragment2.requireView();
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(FragmentManager.TAG, "Clearing focus " + view2.findFocus() + " on view " + view2 + " for Fragment " + fragment2);
                }
                view2.clearFocus();
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Operation
        public void complete() {
            super.complete();
            this.mFragmentStateManager.moveToExpectedState();
        }
    }
}
