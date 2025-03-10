package com.google.common.io;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.TreeTraverser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class MoreFiles {
    private MoreFiles() {
    }

    public static ByteSource asByteSource(Path path, OpenOption... options) {
        return new PathByteSource(path, options);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PathByteSource extends ByteSource {
        private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
        private final boolean followLinks;
        private final OpenOption[] options;
        private final Path path;

        private PathByteSource(Path path, OpenOption... options) {
            this.path = (Path) Preconditions.checkNotNull(path);
            this.options = (OpenOption[]) options.clone();
            this.followLinks = followLinks(this.options);
        }

        private static boolean followLinks(OpenOption[] options) {
            for (OpenOption option : options) {
                if (option == LinkOption.NOFOLLOW_LINKS) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return java.nio.file.Files.newInputStream(this.path, this.options);
        }

        private BasicFileAttributes readAttributes() throws IOException {
            return java.nio.file.Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? FOLLOW_LINKS : new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            try {
                BasicFileAttributes attrs = readAttributes();
                if (attrs.isDirectory() || attrs.isSymbolicLink()) {
                    return Optional.absent();
                }
                return Optional.of(Long.valueOf(attrs.size()));
            } catch (IOException e) {
                return Optional.absent();
            }
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            BasicFileAttributes attrs = readAttributes();
            if (attrs.isDirectory()) {
                throw new IOException("can't read: is a directory");
            }
            if (attrs.isSymbolicLink()) {
                throw new IOException("can't read: is a symbolic link");
            }
            return attrs.size();
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() throws IOException {
            SeekableByteChannel channel = java.nio.file.Files.newByteChannel(this.path, this.options);
            try {
                byte[] readFile = Files.readFile(Channels.newInputStream(channel), channel.size());
                if (channel != null) {
                    channel.close();
                }
                return readFile;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    }
                    throw th2;
                }
            }
        }

        public String toString() {
            return "MoreFiles.asByteSource(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }

    public static ByteSink asByteSink(Path path, OpenOption... options) {
        return new PathByteSink(path, options);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PathByteSink extends ByteSink {
        private final OpenOption[] options;
        private final Path path;

        private PathByteSink(Path path, OpenOption... options) {
            this.path = (Path) Preconditions.checkNotNull(path);
            this.options = (OpenOption[]) options.clone();
        }

        @Override // com.google.common.io.ByteSink
        public OutputStream openStream() throws IOException {
            return java.nio.file.Files.newOutputStream(this.path, this.options);
        }

        public String toString() {
            return "MoreFiles.asByteSink(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }

    public static CharSource asCharSource(Path path, Charset charset, OpenOption... options) {
        return asByteSource(path, options).asCharSource(charset);
    }

    public static CharSink asCharSink(Path path, Charset charset, OpenOption... options) {
        return asByteSink(path, options).asCharSink(charset);
    }

    public static ImmutableList<Path> listFiles(Path dir) throws IOException {
        try {
            DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(dir);
            try {
                ImmutableList<Path> copyOf = ImmutableList.copyOf(stream);
                if (stream != null) {
                    stream.close();
                }
                return copyOf;
            } finally {
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
    }

    public static TreeTraverser<Path> directoryTreeTraverser() {
        return DirectoryTreeTraverser.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static final class DirectoryTreeTraverser extends TreeTraverser<Path> {
        private static final DirectoryTreeTraverser INSTANCE = new DirectoryTreeTraverser();

        private DirectoryTreeTraverser() {
        }

        @Override // com.google.common.collect.TreeTraverser
        public Iterable<Path> children(Path dir) {
            if (java.nio.file.Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    return MoreFiles.listFiles(dir);
                } catch (IOException e) {
                    throw new DirectoryIteratorException(e);
                }
            }
            return ImmutableList.of();
        }
    }

    public static Predicate<Path> isDirectory(LinkOption... options) {
        final LinkOption[] optionsCopy = (LinkOption[]) options.clone();
        return new Predicate<Path>() { // from class: com.google.common.io.MoreFiles.1
            @Override // com.google.common.base.Predicate
            public boolean apply(Path input) {
                return java.nio.file.Files.isDirectory(input, optionsCopy);
            }

            public String toString() {
                return "MoreFiles.isDirectory(" + Arrays.toString(optionsCopy) + ")";
            }
        };
    }

    public static Predicate<Path> isRegularFile(LinkOption... options) {
        final LinkOption[] optionsCopy = (LinkOption[]) options.clone();
        return new Predicate<Path>() { // from class: com.google.common.io.MoreFiles.2
            @Override // com.google.common.base.Predicate
            public boolean apply(Path input) {
                return java.nio.file.Files.isRegularFile(input, optionsCopy);
            }

            public String toString() {
                return "MoreFiles.isRegularFile(" + Arrays.toString(optionsCopy) + ")";
            }
        };
    }

    public static void touch(Path path) throws IOException {
        Preconditions.checkNotNull(path);
        try {
            java.nio.file.Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (NoSuchFileException e) {
            try {
                java.nio.file.Files.createFile(path, new FileAttribute[0]);
            } catch (FileAlreadyExistsException e2) {
            }
        }
    }

    public static void createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
        Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
        Path parent = normalizedAbsolutePath.getParent();
        if (parent != null && !java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
            java.nio.file.Files.createDirectories(parent, attrs);
            if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
                throw new IOException("Unable to create parent directories of " + path);
            }
        }
    }

    public static String getFileExtension(Path path) {
        String fileName;
        int dotIndex;
        Path name = path.getFileName();
        return (name == null || (dotIndex = (fileName = name.toString()).lastIndexOf(46)) == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(Path path) {
        Path name = path.getFileName();
        if (name == null) {
            return "";
        }
        String fileName = name.toString();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }

    public static void deleteRecursively(Path path, RecursiveDeleteOption... options) throws IOException {
        Path parentPath = getParentPath(path);
        if (parentPath == null) {
            throw new FileSystemException(path.toString(), null, "can't delete recursively");
        }
        Collection<IOException> exceptions = null;
        boolean sdsSupported = false;
        try {
            DirectoryStream<Path> parent = java.nio.file.Files.newDirectoryStream(parentPath);
            try {
                if (parent instanceof SecureDirectoryStream) {
                    sdsSupported = true;
                    exceptions = deleteRecursivelySecure((SecureDirectoryStream) parent, path.getFileName());
                }
                if (parent != null) {
                    parent.close();
                }
                if (!sdsSupported) {
                    checkAllowsInsecure(path, options);
                    exceptions = deleteRecursivelyInsecure(path);
                }
            } finally {
            }
        } catch (IOException e) {
            if (0 == 0) {
                throw e;
            }
            exceptions.add(e);
        }
        if (exceptions != null) {
            throwDeleteFailed(path, exceptions);
        }
    }

    public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options) throws IOException {
        Collection<IOException> exceptions = null;
        try {
            DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path);
            try {
                if (stream instanceof SecureDirectoryStream) {
                    SecureDirectoryStream<Path> sds = (SecureDirectoryStream) stream;
                    exceptions = deleteDirectoryContentsSecure(sds);
                } else {
                    checkAllowsInsecure(path, options);
                    exceptions = deleteDirectoryContentsInsecure(stream);
                }
                if (stream != null) {
                    stream.close();
                }
            } finally {
            }
        } catch (IOException e) {
            if (0 == 0) {
                throw e;
            }
            exceptions.add(e);
        }
        if (exceptions != null) {
            throwDeleteFailed(path, exceptions);
        }
    }

    @Nullable
    private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (isDirectory(dir, path, LinkOption.NOFOLLOW_LINKS)) {
                SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, LinkOption.NOFOLLOW_LINKS);
                try {
                    exceptions = deleteDirectoryContentsSecure(childDir);
                    if (childDir != null) {
                        childDir.close();
                    }
                    if (exceptions == null) {
                        dir.deleteDirectory(path);
                    }
                } finally {
                }
            } else {
                dir.deleteFile(path);
            }
            return exceptions;
        } catch (IOException e) {
            return addException(null, e);
        }
    }

    @Nullable
    private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
        Collection<IOException> exceptions = null;
        try {
            for (Path path : dir) {
                exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()));
            }
            return exceptions;
        } catch (DirectoryIteratorException e) {
            return addException(exceptions, e.getCause());
        }
    }

    @Nullable
    private static Collection<IOException> deleteRecursivelyInsecure(Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (java.nio.file.Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path);
                try {
                    exceptions = deleteDirectoryContentsInsecure(stream);
                    if (stream != null) {
                        stream.close();
                    }
                } finally {
                }
            }
            if (exceptions == null) {
                java.nio.file.Files.delete(path);
            }
            return exceptions;
        } catch (IOException e) {
            return addException(null, e);
        }
    }

    @Nullable
    private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
        Collection<IOException> exceptions = null;
        try {
            for (Path entry : dir) {
                exceptions = concat(exceptions, deleteRecursivelyInsecure(entry));
            }
            return exceptions;
        } catch (DirectoryIteratorException e) {
            return addException(exceptions, e.getCause());
        }
    }

    @Nullable
    private static Path getParentPath(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            return parent;
        }
        if (path.getNameCount() == 0) {
            return null;
        }
        return path.getFileSystem().getPath(".", new String[0]);
    }

    private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
        if (!Arrays.asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
            throw new InsecureRecursiveDeleteException(path.toString());
        }
    }

    private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options) throws IOException {
        return ((BasicFileAttributeView) dir.getFileAttributeView(name, BasicFileAttributeView.class, options)).readAttributes().isDirectory();
    }

    private static Collection<IOException> addException(@Nullable Collection<IOException> exceptions, IOException e) {
        if (exceptions == null) {
            exceptions = new ArrayList();
        }
        exceptions.add(e);
        return exceptions;
    }

    @Nullable
    private static Collection<IOException> concat(@Nullable Collection<IOException> exceptions, @Nullable Collection<IOException> other) {
        if (exceptions == null) {
            return other;
        }
        if (other != null) {
            exceptions.addAll(other);
        }
        return exceptions;
    }

    private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
        FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
        for (IOException e : exceptions) {
            deleteFailed.addSuppressed(e);
        }
        throw deleteFailed;
    }
}
