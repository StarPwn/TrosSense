package com.google.common.base;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class JdkPattern extends CommonPattern implements Serializable {
    private static final long serialVersionUID = 0;
    private final Pattern pattern;

    /* JADX INFO: Access modifiers changed from: package-private */
    public JdkPattern(Pattern pattern) {
        this.pattern = (Pattern) Preconditions.checkNotNull(pattern);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.base.CommonPattern
    public CommonMatcher matcher(CharSequence t) {
        return new JdkMatcher(this.pattern.matcher(t));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.base.CommonPattern
    public String pattern() {
        return this.pattern.pattern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.base.CommonPattern
    public int flags() {
        return this.pattern.flags();
    }

    @Override // com.google.common.base.CommonPattern
    public String toString() {
        return this.pattern.toString();
    }

    @Override // com.google.common.base.CommonPattern
    public int hashCode() {
        return this.pattern.hashCode();
    }

    @Override // com.google.common.base.CommonPattern
    public boolean equals(Object o) {
        if (!(o instanceof JdkPattern)) {
            return false;
        }
        return this.pattern.equals(((JdkPattern) o).pattern);
    }

    /* loaded from: classes.dex */
    private static final class JdkMatcher extends CommonMatcher {
        final Matcher matcher;

        JdkMatcher(Matcher matcher) {
            this.matcher = (Matcher) Preconditions.checkNotNull(matcher);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.base.CommonMatcher
        public boolean matches() {
            return this.matcher.matches();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.base.CommonMatcher
        public boolean find() {
            return this.matcher.find();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.base.CommonMatcher
        public boolean find(int index) {
            return this.matcher.find(index);
        }

        @Override // com.google.common.base.CommonMatcher
        String replaceAll(String replacement) {
            return this.matcher.replaceAll(replacement);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.base.CommonMatcher
        public int end() {
            return this.matcher.end();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.base.CommonMatcher
        public int start() {
            return this.matcher.start();
        }
    }
}
