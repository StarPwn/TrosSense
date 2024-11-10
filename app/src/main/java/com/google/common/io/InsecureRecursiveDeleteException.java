package com.google.common.io;

import java.nio.file.FileSystemException;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class InsecureRecursiveDeleteException extends FileSystemException {
    public InsecureRecursiveDeleteException(@Nullable String file) {
        super(file, null, "unable to guarantee security of recursive delete");
    }
}
