package com.populstay.populife.util.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.populstay.populife.R;
import com.populstay.populife.activity.MainActivity;
import com.populstay.populife.activity.SignActivity;
import com.populstay.populife.sign.ISignListener;
import com.populstay.populife.sign.SignHandler;

/**
 * Notification 工具类
 *
 * 设计原则：
 * 1️⃣ 前台服务通知 & 推送通知使用不同 Channel
 * 2️⃣ 推送 Channel = HIGH（可弹窗）
 * 3️⃣ Service Channel = LOW（仅存在感）
 */
public final class NotificationUtil {

    /** 前台服务 Channel（不弹窗） */
    public static final String CHANNEL_SERVICE = "populife_service";

    /** 推送消息 Channel（弹窗） */
    public static final String CHANNEL_PUSH = "populife_push";

    private NotificationUtil() {}

    /* -------------------------------- */
    /* 前台服务通知（保活用，不弹窗） */
    /* -------------------------------- */
    public static NotificationCompat.Builder buildServiceNotification(Context context) {

        createServiceChannelIfNeeded(context);

        return new NotificationCompat.Builder(context, CHANNEL_SERVICE)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_running))
                .setSmallIcon(R.mipmap.ic_logo)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);
    }

    /* ------------------------------ */
    /* 推送通知（真正给用户看的） */
    /* ------------------------------ */
    public static void showPushNotification(
            Context context,
            int eventCode,
            String contentText
    ) {

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        createPushChannelIfNeeded(context);

        Intent intent = buildIntent(context, eventCode);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                getPendingIntentFlag()
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_PUSH)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(contentText)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                context.getResources(), R.mipmap.ic_logo))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL); // 声音 + 震动

        // 使用不同 ID，避免覆盖
        int notifyId = (int) System.currentTimeMillis();
        manager.notify(notifyId, builder.build());
    }

    /* ------------------------------ */
    /* Channel 创建 */
    /* ------------------------------ */

    private static void createServiceChannelIfNeeded(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            if (manager == null) return;

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_SERVICE,
                    context.getString(R.string.notification_background_title),
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setShowBadge(false);
            manager.createNotificationChannel(channel);
        }
    }

    private static void createPushChannelIfNeeded(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            if (manager == null) return;

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_PUSH,
                            context.getString(R.string.notification_push_title),
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            manager.createNotificationChannel(channel);
        }
    }


    /* ------------------------------ */
    /* 点击通知跳转逻辑 */
    /* ------------------------------ */
    private static Intent buildIntent(Context context, int eventCode) {
        Intent intent = new Intent();

        if (eventCode == 1) { // 异地登录
            intent.setClass(context, SignActivity.class);
            intent.putExtra(
                    SignActivity.KEY_ACCOUNT_SIGN_ACTION_TYPE,
                    SignActivity.VAL_ACCOUNT_SIGN_IN
            );

            // 强制登出
            SignHandler.onSignOut(new ISignListener() {
                @Override public void onSignInSuccess() {}
                @Override public void onSignUpSuccess() {}
                @Override public void onSignOutSuccess() {}
            });
        } else {
            intent.setClass(context, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private static int getPendingIntentFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.FLAG_UPDATE_CURRENT;
    }
    public static boolean areNotificationsEnabled(Context context) {
        NotificationManagerCompat manager =
                NotificationManagerCompat.from(context);
        return manager.areNotificationsEnabled();
    }

    public static boolean isPushChannelEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);
            if (manager == null) return false;

            NotificationChannel channel =
                    manager.getNotificationChannel(NotificationUtil.CHANNEL_PUSH);

            if (channel == null) {
                // Channel 还没创建，默认视为开启
                return true;
            }

            return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
        }
        return true;
    }

    public static boolean canReceivePushNotification(Context context) {
        return areNotificationsEnabled(context) && isPushChannelEnabled(context);
    }

    public static void openPushChannelSettings(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        } else {
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
