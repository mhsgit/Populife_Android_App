package com.populstay.populife.app;

import static androidx.core.util.Preconditions.checkNotNull;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.populstay.populife.R;
import com.populstay.populife.util.date.DateUtil;
import com.populstay.populife.util.log.MyDiskLogStrategy;
import com.populstay.populife.util.log.PeachLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public CustomUncaughtExceptionHandler(Context context){
        this.context = context;
    }

    private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
        checkNotNull(folderName);
        checkNotNull(fileName);

        File folder = new File(folderName);
        if (!folder.exists()) {
            //TODO: What if folder is not created, what happens then?
            folder.mkdirs();
        }

        int newFileCount = 0;
        File newFile;
        File existingFile = null;

        newFile = new File(folder, String.format("%s_%s_%s.txt", fileName, newFileCount, DateUtil.getDateToString(new Date(),DateUtil.DATE_TIME_PATTERN_2)));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s_%s_%s.txt", fileName, newFileCount, DateUtil.getDateToString(new Date(),DateUtil.DATE_TIME_PATTERN_2)));
        }

        if (existingFile != null) {
            if (existingFile.length() >= 500 * 1024) {
                return newFile;
            }
            return existingFile;
        }

        return newFile;
    }

    /**
     * 获取手机信息
     */
    private String appendPhoneInfo() throws PackageManager.NameNotFoundException
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        StringBuilder sb = new StringBuilder();
        //App版本
        sb.append("App Version: ");
        sb.append(pi.versionName);
        sb.append("_");
        sb.append(pi.versionCode + "\n");

        //Android版本号
        sb.append("OS Version: ");
        sb.append(Build.VERSION.RELEASE);
        sb.append("_");
        sb.append(Build.VERSION.SDK_INT + "\n");

        //手机制造商
        sb.append("Vendor: ");
        sb.append(Build.MANUFACTURER + "\n");

        //手机型号
        sb.append("Model: ");
        sb.append(Build.MODEL + "\n");

        //CPU架构
        sb.append("CPU: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            sb.append(Arrays.toString(Build.SUPPORTED_ABIS));
        } else
        {
            sb.append(Build.CPU_ABI);
        }
        return sb.toString();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        // 在这里处理未捕获的异常
        PeachLogger.e("CustomExceptionHandler", "Unhandled exception: " + throwable);
        // 可以在这里编写自定义的异常处理逻辑，例如日志记录、错误报告等
        // 请注意，如果不调用默认的异常处理程序，应用程序可能会被终止
        PeachLogger.e("CustomExceptionHandler", "getMessage= " + throwable.getMessage());
        PeachLogger.e("CustomExceptionHandler", "getCause= " + throwable.getCause());
        PeachLogger.e("CustomExceptionHandler", "getStackTrace= " + Arrays.toString(throwable.getStackTrace()));

        String folder = context.getExternalCacheDir().getAbsolutePath() + File.separator + "logs";
        File logFile = getLogFile(folder, "errors");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try
        {
            //往文件中写入数据
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
            pw.println(time);
            pw.println(appendPhoneInfo());
            throwable.printStackTrace(pw);
            pw.close();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        } catch (PackageManager.NameNotFoundException e1)
        {
            e1.printStackTrace();
        }

        Toast toast = Toast.makeText(context, context.getString(R.string.crash_exit), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //Sleep一会后结束程序
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            PeachLogger.e("CustomExceptionHandler", "InterruptedException exception: " + e);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);

        // 调用默认的异常处理程序
        //Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, throwable);
    }
}

