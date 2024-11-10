package org.cloudburstmc.protocol.bedrock.data.skin;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes5.dex */
public class ImageData {
    private static final int DOUBLE_SKIN_SIZE = 16384;
    public static final ImageData EMPTY = new ImageData(0, 0, new byte[0]);
    private static final int PIXEL_SIZE = 4;
    private static final int SINGLE_SKIN_SIZE = 8192;
    private static final int SKIN_128_128_SIZE = 65536;
    private static final int SKIN_128_64_SIZE = 32768;
    private static final int SKIN_PERSONA_SIZE = 262144;
    private final int height;
    private final byte[] image;
    private final int width;

    public String toString() {
        return "ImageData(width=" + getWidth() + ", height=" + getHeight() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof ImageData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ImageData)) {
            return false;
        }
        ImageData other = (ImageData) o;
        return other.canEqual(this) && this.width == other.width && this.height == other.height && Arrays.equals(this.image, other.image);
    }

    public int hashCode() {
        int result = (1 * 59) + this.width;
        return (((result * 59) + this.height) * 59) + Arrays.hashCode(this.image);
    }

    ImageData(int width, int height, byte[] image) {
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public byte[] getImage() {
        return this.image;
    }

    public static ImageData of(int width, int height, byte[] image) {
        Objects.requireNonNull(image, "image");
        return new ImageData(width, height, image);
    }

    public static ImageData of(byte[] image) {
        Objects.requireNonNull(image, "image");
        switch (image.length) {
            case 0:
                return EMPTY;
            case 8192:
                return new ImageData(64, 32, image);
            case 16384:
                return new ImageData(64, 64, image);
            case 32768:
                return new ImageData(128, 64, image);
            case 65536:
                return new ImageData(128, 128, image);
            case 262144:
                return new ImageData(256, 256, image);
            default:
                throw new IllegalArgumentException("Invalid skin length");
        }
    }

    public void checkLegacySkinSize() {
        switch (this.image.length) {
            case 8192:
            case 16384:
            case 32768:
            case 65536:
                return;
            default:
                throw new IllegalArgumentException("Invalid legacy skin");
        }
    }

    public void checkPersonaSkinSize() {
        switch (this.image.length) {
            case 262144:
                return;
            default:
                throw new IllegalArgumentException("Invalid persona skin");
        }
    }

    public void checkLegacyCapeSize() {
        if (this.image.length != 0 && this.image.length != 8192) {
            throw new IllegalArgumentException("Invalid legacy cape");
        }
    }

    public static ImageData from(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();
        return new ImageData(image.getWidth(), image.getHeight(), outputStream.toByteArray());
    }
}
