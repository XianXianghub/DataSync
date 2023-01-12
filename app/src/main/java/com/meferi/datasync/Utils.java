package com.meferi.datasync;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.print.PrintAttributes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import androidx.core.app.NotificationCompat;

import com.meferi.DeflaterUtils;
import com.meferi.scanner.ScannerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class Utils {
    private static String TAG = "Utils";

    public static void setWindowStatusBarColor(Activity activity, int i) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.addFlags(Integer.MIN_VALUE);
                window.setStatusBarColor(activity.getResources().getColor(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigBean ScannerSettingExpert(String str) throws Exception {
        Log.d(TAG, "export scanner");
        String config = ScannerManager.getInstance().getSyncService();
        String zipBase64 = config;
        return new ConfigBean(str, MainApplication.getInstance().getString(R.string.config_scan), zipBase64, QRCodeUtil.createQRCode(zipBase64, 700));
    }

    public static void SettingImport(Context context, String str, String str2) throws Exception {
        Log.d(TAG, "undecode00=" + str);
        String[] split = str.split("\u001e");

        for (int i = 0; i < split.length; i++) {
            Log.d(TAG, split[i]);
            split[i] = unzipBase64(split[i]);
        }
        Log.d(TAG, "undecode=" + Arrays.toString(split));
        for (String str3 : split) {
            if (!TextUtils.isEmpty(str3)) {
                if (str3.startsWith("scantool", 0)) {
//                    ScanToolImport(str3);
                } else if (str3.startsWith("scan", 0)) {
                    if (!ScannerManager.getInstance().setSyncService(str3)) {
                        throw new Exception("set failed");
                    }
                } else if (str3.startsWith(NotificationCompat.CATEGORY_SYSTEM, 0)) {
                    SettingImportSystem(str3);

                } else {
                    throw new Exception("Incorrect string format");
                }
            }
        }
    }

    public static void SettingImportSystem(String str) {
        String[] split = str.split("\u001c");
        int i = 1;
        for (Map.Entry<String, String> entry : PropertyMap.initProperty("system_setting.xml", "setting").entrySet()) {
            if (i < split.length) {
                try {
                    MainApplication.deviceManagerPlus.setSystemConfig(entry.getValue(), split[i]);
                } catch (Exception e) {
                    Log.d("error", Log.getStackTraceString(e));
                }
            }
            i++;
        }
    }

    public static ConfigBean SystemSettingsExpert(String str) throws Exception {
        StringBuilder sb = new StringBuilder("sys\u001c");
        for (Map.Entry<String, String> entry : PropertyMap.initProperty("system_setting.xml", "setting").entrySet()) {
            String systemCongfig = MainApplication.deviceManagerPlus.getSystemCongfig(entry.getValue());
            Log.d("bds", entry.getValue() + " " + systemCongfig);
            sb.append(systemCongfig);
            sb.append("\u001c");
        }
        String zipBase64 = zipBase64(sb.toString());
        return new ConfigBean(str, MainApplication.getInstance().getString(R.string.config_system), zipBase64, QRCodeUtil.createQRCode(zipBase64, 700));
    }

    public static void saveBitmapForPdf(Context context, List<ConfigBean> list, String str, String str2) {
        Log.d("yyy", "111value===" + list.size());
        Throwable th;
        Exception e;
        FileOutputStream fileOutputStream = null;
        PdfDocument pdfDocument = new PdfDocument();
        int widthMils = ((PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72) / 1000) + 100;
        int heightMils = (PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72) / 1000;
        Matrix matrix = new Matrix();
        float width = (((float) widthMils) / ((float) list.get(0).getConfigQrcode().getWidth())) * 0.8f;
        matrix.postScale(width, width);
        matrix.postTranslate(50.0f, 105.0f);
        Paint paint = new Paint(1);
        paint.setTextSize(20.0f);
        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "simkai.ttf"));
        for (int i = 0; i < list.size(); i++) {
            PdfDocument.Page startPage = pdfDocument.startPage(new PdfDocument.PageInfo.Builder(widthMils, heightMils, i).create());
            Canvas canvas = startPage.getCanvas();
            canvas.drawText(list.get(i).getConfigName(), 80.0f, 80.0f, paint);
            canvas.drawText(list.get(i).getConfigType(), 80.0f, 105.0f, paint);
            canvas.drawBitmap(list.get(i).getConfigQrcode(), matrix, paint);
            pdfDocument.finishPage(startPage);
        }
        try {
            FileOutputStream fileOutputStream2 = null;
            try {
                try {
                    fileOutputStream = new FileOutputStream(new File(str, str2));
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                pdfDocument.writeTo(fileOutputStream);
                pdfDocument.close();
                fileOutputStream.close();
            } catch (Exception e3) {
                e = e3;
                fileOutputStream2 = fileOutputStream;
                e.printStackTrace();
                pdfDocument.close();
                if (fileOutputStream2 != null) {
                    fileOutputStream2.close();
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream2 = fileOutputStream;
                pdfDocument.close();
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        } catch (IOException e5) {
            while (true) {
                e5.printStackTrace();
                return;
            }
        }
    }

    public static String unzipBase64(String str) throws Exception {
        return DeflaterUtils.unzipString(str);
    }

    public static String zipBase64(String str) {
        return DeflaterUtils.zipString(str);
    }
}
