package io.netty.handler.codec.http.multipart;

/* loaded from: classes4.dex */
final class FileUploadUtil {
    private FileUploadUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int hashCode(FileUpload upload) {
        return upload.getName().hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean equals(FileUpload upload1, FileUpload upload2) {
        return upload1.getName().equalsIgnoreCase(upload2.getName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int compareTo(FileUpload upload1, FileUpload upload2) {
        return upload1.getName().compareToIgnoreCase(upload2.getName());
    }
}
