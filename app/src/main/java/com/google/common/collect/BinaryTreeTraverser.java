package com.google.common.collect;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T> {
    public abstract Optional<T> leftChild(T t);

    public abstract Optional<T> rightChild(T t);

    @Override // com.google.common.collect.TreeTraverser
    public final Iterable<T> children(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() { // from class: com.google.common.collect.BinaryTreeTraverser.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() { // from class: com.google.common.collect.BinaryTreeTraverser.1.1
                    boolean doneLeft;
                    boolean doneRight;

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // com.google.common.collect.AbstractIterator
                    protected T computeNext() {
                        if (!this.doneLeft) {
                            this.doneLeft = true;
                            Optional<T> left = BinaryTreeTraverser.this.leftChild(root);
                            if (left.isPresent()) {
                                return left.get();
                            }
                        }
                        if (!this.doneRight) {
                            this.doneRight = true;
                            Optional<T> right = BinaryTreeTraverser.this.rightChild(root);
                            if (right.isPresent()) {
                                return right.get();
                            }
                        }
                        return endOfData();
                    }
                };
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Iterable
            public void forEach(Consumer<? super T> action) {
                BinaryTreeTraverser.acceptIfPresent(action, BinaryTreeTraverser.this.leftChild(root));
                BinaryTreeTraverser.acceptIfPresent(action, BinaryTreeTraverser.this.rightChild(root));
            }
        };
    }

    @Override // com.google.common.collect.TreeTraverser
    UnmodifiableIterator<T> preOrderIterator(T root) {
        return new PreOrderIterator(root);
    }

    /* loaded from: classes.dex */
    private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Deque<T> stack = new ArrayDeque(8);

        PreOrderIterator(T root) {
            this.stack.addLast(root);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override // java.util.Iterator, com.google.common.collect.PeekingIterator
        public T next() {
            T result = this.stack.removeLast();
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(result));
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(result));
            return result;
        }

        @Override // com.google.common.collect.PeekingIterator
        public T peek() {
            return this.stack.getLast();
        }
    }

    @Override // com.google.common.collect.TreeTraverser
    UnmodifiableIterator<T> postOrderIterator(T root) {
        return new PostOrderIterator(root);
    }

    /* loaded from: classes.dex */
    private final class PostOrderIterator extends UnmodifiableIterator<T> {
        private final BitSet hasExpanded;
        private final Deque<T> stack = new ArrayDeque(8);

        PostOrderIterator(T root) {
            this.stack.addLast(root);
            this.hasExpanded = new BitSet();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        @Override // java.util.Iterator
        public T next() {
            while (true) {
                T node = this.stack.getLast();
                boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
                if (expandedNode) {
                    this.stack.removeLast();
                    this.hasExpanded.clear(this.stack.size());
                    return node;
                }
                this.hasExpanded.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
            }
        }
    }

    public final FluentIterable<T> inOrderTraversal(final T root) {
        Preconditions.checkNotNull(root);
        return new FluentIterable<T>() { // from class: com.google.common.collect.BinaryTreeTraverser.2
            @Override // java.lang.Iterable
            public UnmodifiableIterator<T> iterator() {
                return new InOrderIterator(root);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Iterable
            public void forEach(final Consumer<? super T> consumer) {
                Preconditions.checkNotNull(consumer);
                new Consumer<T>() { // from class: com.google.common.collect.BinaryTreeTraverser.2.1
                    @Override // java.util.function.Consumer
                    public void accept(T t) {
                        BinaryTreeTraverser.acceptIfPresent(this, BinaryTreeTraverser.this.leftChild(t));
                        consumer.accept(t);
                        BinaryTreeTraverser.acceptIfPresent(this, BinaryTreeTraverser.this.rightChild(t));
                    }
                }.accept(root);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class InOrderIterator extends AbstractIterator<T> {
        private final Deque<T> stack = new ArrayDeque(8);
        private final BitSet hasExpandedLeft = new BitSet();

        InOrderIterator(T root) {
            this.stack.addLast(root);
        }

        @Override // com.google.common.collect.AbstractIterator
        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                T node = this.stack.getLast();
                if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpandedLeft.clear(this.stack.size());
                    BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
                    return node;
                }
                this.hasExpandedLeft.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
            }
            return endOfData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void pushIfPresent(Deque<T> stack, Optional<T> node) {
        if (node.isPresent()) {
            stack.addLast(node.get());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> void acceptIfPresent(Consumer<? super T> consumer, Optional<T> optional) {
        if (optional.isPresent()) {
            consumer.accept(optional.get());
        }
    }
}
