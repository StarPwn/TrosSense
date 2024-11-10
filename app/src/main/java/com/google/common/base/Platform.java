package com.google.common.base;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class Platform {
    private static final Logger logger = Logger.getLogger(Platform.class.getName());
    private static final PatternCompiler patternCompiler = loadPatternCompiler();

    private Platform() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long systemNanoTime() {
        return System.nanoTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
        return matcher.precomputedInternal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
        WeakReference<? extends Enum<?>> ref = Enums.getEnumConstants(enumClass).get(value);
        if (ref == null) {
            return Optional.absent();
        }
        return Optional.of(enumClass.cast(ref.get()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatCompact4Digits(double value) {
        return String.format(Locale.ROOT, "%.4g", Double.valueOf(value));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean stringIsNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CommonPattern compilePattern(String pattern) {
        Preconditions.checkNotNull(pattern);
        return patternCompiler.compile(pattern);
    }

    static boolean usingJdkPatternCompiler() {
        return patternCompiler instanceof JdkPatternCompiler;
    }

    private static PatternCompiler loadPatternCompiler() {
        ServiceLoader<PatternCompiler> loader = ServiceLoader.load(PatternCompiler.class);
        try {
            Iterator<PatternCompiler> it2 = loader.iterator();
            while (it2.hasNext()) {
                try {
                    return it2.next();
                } catch (ServiceConfigurationError e) {
                    logPatternCompilerError(e);
                }
            }
        } catch (ServiceConfigurationError e2) {
            logPatternCompilerError(e2);
        }
        return new JdkPatternCompiler();
    }

    private static void logPatternCompilerError(ServiceConfigurationError e) {
        logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", (Throwable) e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class JdkPatternCompiler implements PatternCompiler {
        private JdkPatternCompiler() {
        }

        @Override // com.google.common.base.PatternCompiler
        public CommonPattern compile(String pattern) {
            return new JdkPattern(Pattern.compile(pattern));
        }
    }
}
