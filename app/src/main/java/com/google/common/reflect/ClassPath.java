package com.google.common.reflect;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class ClassPath {
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;
    private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>() { // from class: com.google.common.reflect.ClassPath.1
        @Override // com.google.common.base.Predicate
        public boolean apply(ClassInfo info) {
            return info.className.indexOf(36) == -1;
        }
    };
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();

    private ClassPath(ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }

    public static ClassPath from(ClassLoader classloader) throws IOException {
        DefaultScanner scanner = new DefaultScanner();
        scanner.scan(classloader);
        return new ClassPath(scanner.getResources());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
        Preconditions.checkNotNull(packageName);
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        UnmodifiableIterator<ClassInfo> it2 = getTopLevelClasses().iterator();
        while (it2.hasNext()) {
            ClassInfo classInfo = it2.next();
            if (classInfo.getPackageName().equals(packageName)) {
                builder.add((ImmutableSet.Builder<ClassInfo>) classInfo);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
        Preconditions.checkNotNull(packageName);
        String packagePrefix = packageName + '.';
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        UnmodifiableIterator<ClassInfo> it2 = getTopLevelClasses().iterator();
        while (it2.hasNext()) {
            ClassInfo classInfo = it2.next();
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add((ImmutableSet.Builder<ClassInfo>) classInfo);
            }
        }
        return builder.build();
    }

    /* loaded from: classes.dex */
    public static class ResourceInfo {
        final ClassLoader loader;
        private final String resourceName;

        static ResourceInfo of(String resourceName, ClassLoader loader) {
            if (resourceName.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION)) {
                return new ClassInfo(resourceName, loader);
            }
            return new ResourceInfo(resourceName, loader);
        }

        ResourceInfo(String resourceName, ClassLoader loader) {
            this.resourceName = (String) Preconditions.checkNotNull(resourceName);
            this.loader = (ClassLoader) Preconditions.checkNotNull(loader);
        }

        public final URL url() {
            URL url = this.loader.getResource(this.resourceName);
            if (url == null) {
                throw new NoSuchElementException(this.resourceName);
            }
            return url;
        }

        public final ByteSource asByteSource() {
            return Resources.asByteSource(url());
        }

        public final CharSource asCharSource(Charset charset) {
            return Resources.asCharSource(url(), charset);
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ResourceInfo)) {
                return false;
            }
            ResourceInfo that = (ResourceInfo) obj;
            return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    /* loaded from: classes.dex */
    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(String resourceName, ClassLoader loader) {
            super(resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.digit().trimLeadingFrom(innerClassName);
            }
            String packageName = getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }

        public String getName() {
            return this.className;
        }

        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.reflect.ClassPath.ResourceInfo
        public String toString() {
            return this.className;
        }
    }

    /* loaded from: classes.dex */
    static abstract class Scanner {
        private final Set<File> scannedUris = Sets.newHashSet();

        protected abstract void scanDirectory(ClassLoader classLoader, File file) throws IOException;

        protected abstract void scanJarFile(ClassLoader classLoader, JarFile jarFile) throws IOException;

        Scanner() {
        }

        public final void scan(ClassLoader classloader) throws IOException {
            UnmodifiableIterator<Map.Entry<File, ClassLoader>> it2 = getClassPathEntries(classloader).entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<File, ClassLoader> entry = it2.next();
                scan(entry.getKey(), entry.getValue());
            }
        }

        final void scan(File file, ClassLoader classloader) throws IOException {
            if (this.scannedUris.add(file.getCanonicalFile())) {
                scanFrom(file, classloader);
            }
        }

        private void scanFrom(File file, ClassLoader classloader) throws IOException {
            try {
                if (!file.exists()) {
                    return;
                }
                if (file.isDirectory()) {
                    scanDirectory(classloader, file);
                } else {
                    scanJar(file, classloader);
                }
            } catch (SecurityException e) {
                ClassPath.logger.warning("Cannot access " + file + ": " + e);
            }
        }

        private void scanJar(File file, ClassLoader classloader) throws IOException {
            try {
                JarFile jarFile = new JarFile(file);
                try {
                    UnmodifiableIterator<File> it2 = getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                    while (it2.hasNext()) {
                        File path = it2.next();
                        scan(path, classloader);
                    }
                    scanJarFile(classloader, jarFile);
                    try {
                        jarFile.close();
                    } catch (IOException e) {
                    }
                } catch (Throwable th) {
                    try {
                        jarFile.close();
                    } catch (IOException e2) {
                    }
                    throw th;
                }
            } catch (IOException e3) {
            }
        }

        static ImmutableSet<File> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
            if (manifest == null) {
                return ImmutableSet.of();
            }
            ImmutableSet.Builder<File> builder = ImmutableSet.builder();
            String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
            if (classpathAttribute != null) {
                for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                    try {
                        URL url = getClassPathEntry(jarFile, path);
                        if (url.getProtocol().equals("file")) {
                            builder.add((ImmutableSet.Builder<File>) new File(url.getFile()));
                        }
                    } catch (MalformedURLException e) {
                        ClassPath.logger.warning("Invalid Class-Path entry: " + path);
                    }
                }
            }
            return builder.build();
        }

        static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
            LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
            ClassLoader parent = classloader.getParent();
            if (parent != null) {
                entries.putAll(getClassPathEntries(parent));
            }
            if (classloader instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = (URLClassLoader) classloader;
                for (URL entry : urlClassLoader.getURLs()) {
                    if (entry.getProtocol().equals("file")) {
                        File file = new File(entry.getFile());
                        if (!entries.containsKey(file)) {
                            entries.put(file, classloader);
                        }
                    }
                }
            }
            return ImmutableMap.copyOf((Map) entries);
        }

        static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
            return new URL(jarFile.toURI().toURL(), path);
        }
    }

    /* loaded from: classes.dex */
    static final class DefaultScanner extends Scanner {
        private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();

        DefaultScanner() {
        }

        ImmutableSet<ResourceInfo> getResources() {
            ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
            for (Map.Entry<ClassLoader, String> entry : this.resources.entries()) {
                builder.add((ImmutableSet.Builder<ResourceInfo>) ResourceInfo.of(entry.getValue(), entry.getKey()));
            }
            return builder.build();
        }

        @Override // com.google.common.reflect.ClassPath.Scanner
        protected void scanJarFile(ClassLoader classloader, JarFile file) {
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && !entry.getName().equals("META-INF/MANIFEST.MF")) {
                    this.resources.get((SetMultimap<ClassLoader, String>) classloader).add(entry.getName());
                }
            }
        }

        @Override // com.google.common.reflect.ClassPath.Scanner
        protected void scanDirectory(ClassLoader classloader, File directory) throws IOException {
            scanDirectory(directory, classloader, "");
        }

        private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix) throws IOException {
            File[] files = directory.listFiles();
            if (files == null) {
                ClassPath.logger.warning("Cannot read directory " + directory);
                return;
            }
            for (File f : files) {
                String name = f.getName();
                if (f.isDirectory()) {
                    scanDirectory(f, classloader, packagePrefix + name + "/");
                } else {
                    String resourceName = packagePrefix + name;
                    if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                        this.resources.get((SetMultimap<ClassLoader, String>) classloader).add(resourceName);
                    }
                }
            }
        }
    }

    static String getClassName(String filename) {
        int classNameEnd = filename.length() - CLASS_FILE_NAME_EXTENSION.length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }
}
