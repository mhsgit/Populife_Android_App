package com.populstay.populife.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.populstay.populife.R;
import com.populstay.populife.app.AccountManager;
import com.populstay.populife.base.BaseApplication;
import com.populstay.populife.constant.Constant;
import com.populstay.populife.util.device.DeviceUtil;
import com.populstay.populife.util.net.NetworkUtil;
import com.populstay.populife.util.notification.NotificationUtil;
import com.populstay.populife.util.storage.PeachPreference;

import org.json.JSONObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;

/**
 * Redis 事件推送服务
 */
public class EventPushService extends Service {

    public static final String ACTION_NEW_DEVICE_LOGIN =
            "populife_action_new_device_login";
    public static final String ACTION_KEY_STATUS_CHANGE =
            "populife_action_key_status_change";

    private static final String TAG = "EventPushService";

    /** Redis 配置 */
    private static final String JEDIS_HOST = "api.populife.co";
    private static final String JEDIS_AUTH = "c49871320";
    private static final int JEDIS_DB = Constant.DEBUG ? 7 : 1;
//    private static final int JEDIS_DB =  1;

    /** 心跳间隔：60 秒 */
    private static final long HEARTBEAT_INTERVAL = 60;

    /** 当前设备消息 key */
    private static final String DEVICE_MSG_KEY =
            DeviceUtil.getDeviceId(BaseApplication.getApplication());

    /** 线程 & 状态控制 */
    private volatile boolean isRunning = false;
    private ScheduledExecutorService heartbeatExecutor;
    private ExecutorService jedisExecutor;

    // -------------------- Service 生命周期 --------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startHeartbeat();
        startJedisIfNeeded();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundInternal();
    }

    @Override
    public void onDestroy() {
        stopAll();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final int FOREGROUND_ID = 2001;
    private static final String CHANNEL_ID = "populife_foreground";

    private void startForegroundInternal() {
        Notification notification =
                NotificationUtil.buildServiceNotification(this).build();

        startForeground(FOREGROUND_ID, notification);
    }


    // -------------------- 心跳检测 --------------------

    private void startHeartbeat() {
        if (heartbeatExecutor != null) return;

        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            if (!NetworkUtil.isNetConnected()) {
                Log.d(TAG, "Network disconnected");
                isRunning = false;
                return;
            }
            if (!isRunning) {
                Log.d(TAG, "Restart jedis loop");
                startJedisIfNeeded();
            }
        }, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    // -------------------- Jedis 启动 --------------------

    private synchronized void startJedisIfNeeded() {
        if (jedisExecutor != null && !jedisExecutor.isShutdown()) return;

        jedisExecutor = Executors.newSingleThreadExecutor();
        jedisExecutor.execute(this::jedisLoop);
    }

    private void jedisLoop() {
        isRunning = true;

        while (isRunning) {
            try (Jedis jedis = new Jedis(JEDIS_HOST, 6379, 10000)) {

                jedis.auth(JEDIS_AUTH);
                jedis.select(JEDIS_DB);

                // 添加连接成功日志
                Log.d(TAG, "Jedis connected successfully");
                Log.d(TAG, "Ping result: " + jedis.ping());
                Log.d(TAG, "Current DB: " + JEDIS_DB);
                Log.d(TAG, "DEVICE_MSG_KEY: " + DEVICE_MSG_KEY);

                // 检查当前数据库是否有数据
                Long queueSize = jedis.llen(DEVICE_MSG_KEY);
                Log.d(TAG, "Queue size for " + DEVICE_MSG_KEY + ": " + queueSize);

                // 查看所有相关的key
                Set<String> keys = jedis.keys("*" + DEVICE_MSG_KEY + "*");
                Log.d(TAG, "All related keys: " + keys);

                while (isRunning) {
                    Log.d(TAG, "Waiting for message with brpop...");
                    List<String> result = jedis.brpop(30, DEVICE_MSG_KEY);

                    if (result == null) {
                        Log.d(TAG, "BRPOP timeout after 30 seconds, no message");
                        continue;
                    }

                    if (result.size() < 2) {
                        Log.w(TAG, "Unexpected result size: " + result.size());
                        continue;
                    }

                    Log.d(TAG, "Received message! Key: " + result.get(0) + ", Value: " + result.get(1));
                    handleMessage(result.get(0), result.get(1));
                }

            } catch (Exception e) {
                Log.e(TAG, "Error in Jedis loop", e);
                sleepQuiet(3000);
            }
        }
    }
    private void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }


    // -------------------- 消息处理 --------------------

    private void handleMessage(String deviceId, String json) {
        if (!DEVICE_MSG_KEY.equals(deviceId)) return;
        if (!AccountManager.isSignIn()) return;

        try {
            JSONObject obj = new JSONObject(json);
            int eventCode = obj.optInt("event");
            String msg = obj.optString("msg");

            NotificationUtil.showPushNotification(this, eventCode, msg);
            PeachPreference.setBoolean(
                    PeachPreference.HAVE_NEW_MESSAGE, true);

            dispatchEvent(eventCode);

        } catch (Exception e) {
            Log.e(TAG, "Message parse error", e);
        }
    }

    private void dispatchEvent(int eventCode) {
        Intent intent = null;

        switch (eventCode) {
            case 1:
                intent = new Intent(ACTION_NEW_DEVICE_LOGIN);
                break;

            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                intent = new Intent(ACTION_KEY_STATUS_CHANGE);
                break;

            case 13:
                Log.d(TAG, "通过 app 解锁");
                break;

            case 18:
                Log.d(TAG, "通过密码解锁");
                break;

            default:
                break;
        }

        if (intent != null) {
            sendBroadcast(intent);
        }
    }

    // -------------------- 资源释放 --------------------

    private void stopAll() {
        isRunning = false;

        if (heartbeatExecutor != null) {
            heartbeatExecutor.shutdownNow();
            heartbeatExecutor = null;
        }

        if (jedisExecutor != null) {
            jedisExecutor.shutdownNow();
            jedisExecutor = null;
        }
    }
}
