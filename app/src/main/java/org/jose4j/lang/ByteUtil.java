package org.jose4j.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import org.jose4j.base64url.Base64Url;

/* loaded from: classes5.dex */
public class ByteUtil {
    public static final byte[] EMPTY_BYTES = new byte[0];
    private static final int MAX_BYTE_LENGTH = 268435455;

    public static byte[] convertUnsignedToSignedTwosComp(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int idx = 0; idx < ints.length; idx++) {
            bytes[idx] = getByte(ints[idx]);
        }
        return bytes;
    }

    public static int[] convertSignedTwosCompToUnsigned(byte[] bytes) {
        int[] ints = new int[bytes.length];
        for (int idx = 0; idx < bytes.length; idx++) {
            ints[idx] = getInt(bytes[idx]);
        }
        return ints;
    }

    public static byte getByte(int intValue) {
        byte[] bytes = getBytes(intValue);
        if (bytes[0] != 0 || bytes[1] != 0 || bytes[2] != 0) {
            throw new IllegalArgumentException("Integer value (" + intValue + ") too large to stuff into one byte.");
        }
        return bytes[3];
    }

    public static byte[] getBytes(int intValue) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(intValue);
        return byteBuffer.array();
    }

    public static byte[] getBytes(long longValue) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(longValue);
        return byteBuffer.array();
    }

    public static int getInt(byte b) {
        return b >= 0 ? b : 256 - (~(b - 1));
    }

    public static boolean secureEquals(byte[] bytes1, byte[] bytes2) {
        byte[] bytes12 = bytes1 == null ? EMPTY_BYTES : bytes1;
        byte[] bytes22 = bytes2 == null ? EMPTY_BYTES : bytes2;
        int shortest = Math.min(bytes12.length, bytes22.length);
        int longest = Math.max(bytes12.length, bytes22.length);
        int result = 0;
        for (int i = 0; i < shortest; i++) {
            result |= bytes12[i] ^ bytes22[i];
        }
        return result == 0 && shortest == longest;
    }

    public static byte[] concat(byte[]... byteArrays) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (byte[] bytes : byteArrays) {
                byteArrayOutputStream.write(bytes);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("IOEx from ByteArrayOutputStream?!", e);
        }
    }

    public static byte[] subArray(byte[] inputBytes, int startPos, int length) {
        byte[] subArray = new byte[length];
        System.arraycopy(inputBytes, startPos, subArray, 0, subArray.length);
        return subArray;
    }

    public static byte[] leftHalf(byte[] inputBytes) {
        return subArray(inputBytes, 0, inputBytes.length / 2);
    }

    public static byte[] rightHalf(byte[] inputBytes) {
        int half = inputBytes.length / 2;
        return subArray(inputBytes, half, half);
    }

    public static int bitLength(byte[] bytes) {
        return bitLength(bytes.length);
    }

    public static int bitLength(int byteLength) {
        if (byteLength > MAX_BYTE_LENGTH || byteLength < 0) {
            throw new UncheckedJoseException("Invalid byte length (" + byteLength + ") for converting to bit length");
        }
        return byteLength * 8;
    }

    public static byte[] reverse(byte[] in) {
        if (in == null) {
            return null;
        }
        byte[] reversed = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            reversed[(reversed.length - 1) - i] = in[i];
        }
        return reversed;
    }

    public static int byteLength(int numberOfBits) {
        return numberOfBits / 8;
    }

    public static byte[] randomBytes(int length, SecureRandom secureRandom) {
        byte[] bytes = new byte[length];
        (secureRandom == null ? new SecureRandom() : secureRandom).nextBytes(bytes);
        return bytes;
    }

    public static byte[] randomBytes(int length) {
        return randomBytes(length, null);
    }

    public static String toDebugString(byte[] bytes) {
        Base64Url base64Url = new Base64Url();
        String s = base64Url.base64UrlEncode(bytes);
        int[] ints = convertSignedTwosCompToUnsigned(bytes);
        return Arrays.toString(ints) + "(" + ints.length + "bytes/" + bitLength(ints.length) + "bits) | base64url encoded: " + s;
    }
}
