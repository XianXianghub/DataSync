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
        Log.d(TAG, "undecode00="+str);
        String[] split = str.split("\u001e");

        for (int i = 0; i < split.length; i++) {
            Log.d(TAG, split[i]);
            split[i] = unzipBase64(split[i]);
        }
        Log.d(TAG, "undecode="+Arrays.toString(split));
        for (String str3 : split) {
            if (!TextUtils.isEmpty(str3)) {
                if (str3.startsWith("scantool", 0)) {
//                    ScanToolImport(str3);
                } else if (str3.startsWith("scan", 0)) {
                     if(!ScannerManager.getInstance().setSyncService(str3)){
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

//        String ggg = "1:6;2:48;3:0;4:0;5:0;6:0;7:0;8:0;9:1;10:0;11:0;12:0;13:0;14:0;15:1;16:48;17:1;18:1;19:48;20:1;21:4;22:48;23:0;24:0;25:0;26:1;27:150;28:0;29:0;30:4;31:0;32:48;33:0;34:0;35:0;36:1;37:0;38:6;39:48;40:0;41:2;42:48;43:0;44:1;45:48;46:1;47:6;48:48;49:0;50:1;51:48;52:1;53:0;54:0;55:1;56:2751;57:0;58:6;59:48;60:0;61:0;62:0;63:0;64:0;65:0;66:0;67:0;68:0;69:0;70:1;71:0;72:0;73:0;74:4;75:48;76:0;77:0;78:0;79:0;80:1;81:0;82:4;83:48;84:0;85:1;86:48;87:1;88:4;89:48;90:0;91:0;92:0;93:4;94:48;95:0;96:0;97:0;98:0;99:1;100:0;101:0;102:1;103:48;104:1;105:6;106:48;107:0;108:0;109:1;110:48;111:1;112:0;113:0;114:0;115:0;116:0;117:0;118:1;119:48;120:0;121:1;122:48;123:0;124:6;125:48;126:0;127:1;128:48;129:0;130:1;131:0;132:6;133:48;134:0;135:0;136:0;137:0;138:0;139:0;140:1;141:0;142:0;143:UTF-8;145:0;147:5000;149:1;151:0;153: ;155:1;157:0;159:type;161:0;163: ;165:android.intent.extra.MEF_DATA1;167:android.intent.extra.MEF_DATA2;169:3;171:0;173:0;175:1;177:2;179:1;181:1;183:1;185:0;187:1;189:60;191:0;193:0;195:0;197:50;199:1;201:1,3,7,7,7,7,7,7,7,7,0;203:0;205: ;207:386;209:290;211:9;213: ;215:0;217:1;219:android.intent.action.MEF_ACTION;221:1;223:1;225:1;227:0;229:0;231:typeName;233:0;235:2;237:0;239:0;241:1;243:1;245:1;247:1,2,3;249: ;251:0;253:0;255:0;257:0;259:0;261:0;263:446;265:0;267:1;269:350;271:1;273:3;275:1;277:0;";
//        String zipBase64 = zipBase64(ggg.toString());
//        return new ConfigBean(str, MainApplication.getInstance().getString(R.string.config_system), zipBase64, QRCodeUtil.createQRCode(zipBase64, 700));
    }

    public static void saveBitmapForPdf(Context context, List<ConfigBean> list, String str, String str2) {
        Log.d("yyy", "111value==="+list.size());
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
