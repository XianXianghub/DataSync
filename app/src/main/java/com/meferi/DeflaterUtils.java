package com.meferi;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/* loaded from: classes2.dex */
public class DeflaterUtils {
    public static String zipString(String str) {
        Deflater deflater = new Deflater(9);
        deflater.setInput(str.getBytes());
        deflater.finish();
        byte[] bArr = new byte[256];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr, 0, deflater.deflate(bArr));
        }
        deflater.end();
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 1);
    }

    public static String unzipString(String str) throws Exception {
        byte[] decode = Base64.decode(str, 1);
        Inflater inflater = new Inflater();
        inflater.setInput(decode);
        byte[] bArr = new byte[256];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
        while (!inflater.finished()) {
            byteArrayOutputStream.write(bArr, 0, inflater.inflate(bArr));
        }
        inflater.end();
        return byteArrayOutputStream.toString();
    }
}
