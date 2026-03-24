package com.populstay.populife.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.populstay.populife.R;
import com.populstay.populife.util.date.DateUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Utils {
	private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f"};
	private static long lastClickTime;

	private Utils() {

	}

	public static boolean isServiceRunning(Context context, String serviceName) {
		boolean isRunning = false;
		if (context == null || serviceName == null) return isRunning;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> arrayList = activityManager.getRunningServices(100);
		if (arrayList == null || arrayList.size() == 0) return isRunning;
		for (int i = 0, n = arrayList.size(); i < n; i++) {
			if (serviceName.equals(arrayList.get(i).service.getClassName().toString())) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static String getClientVersion(@NonNull Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getApplicationInfo().packageName, 0).versionName;
		} catch (PackageManager.NameNotFoundException ex) {
			return "";
		}
	}

	public static void closeInputKeyboard(Activity context) {
		if (context == null || context.getCurrentFocus() == null || context.getCurrentFocus().getWindowToken() == null)
			return;
		try {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
					(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (NullPointerException nullPoint) {
			nullPoint.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/* 日期格式化类 SimpleDateFormat
	 * 作用1： 可以把日期转换转指定格式的字符串 format()
	 * 作用2： 可以把一个 字符转换成对应的日期。 parse()
	 */
	public static String getDate(long t, String format) {
		Date date = new Date(t);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		return simpleDateFormat.format(date);
	}

	public static boolean AndroidM() {
		return Build.VERSION.SDK_INT >= 23;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * get App versionCode
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		String versionCode = "";
		try {
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionCode = packageInfo.versionCode + "";
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * get App versionName
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		String versionName = "";
		try {
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static String MD5(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception exception) {
		}
		return resultString;
	}

	public static int getStatusBarHeight(Context context) {
		int status_bar_height = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			status_bar_height = context.getResources().getDimensionPixelSize(resourceId);
		}
		return status_bar_height;
	}

	public static void takePhoto(Activity context, String path, int reqCode, Uri imageUri) {
		Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		context.startActivityForResult(intentToTakePhoto, reqCode);
	}

	public static int getNavigationBarHeight(Activity activity) {
		Resources resources = activity.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		return resources.getDimensionPixelSize(resourceId);
	}

	public static void takePhoto(Fragment context, String path, int reqCode, Uri imageUri) {
		Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		context.startActivityForResult(intentToTakePhoto, reqCode);
	}

	public static void choosePhoto(Activity context, int reqCode) {
		Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
		// 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
		intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		context.startActivityForResult(intentToPickPic, reqCode);
	}

	public static void choosePhoto(Fragment context, int reqCode) {
		Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
		// 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
		intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		context.startActivityForResult(intentToPickPic, reqCode);
	}

	public static String bitmapToBase64(Bitmap bitmap) {
		if (bitmap == null) return null;
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static Bitmap getBitmapFromImageView(ImageView imageView) {
		imageView.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
		imageView.setDrawingCacheEnabled(false);
		return bitmap;
	}

	/**
	 * @param fragment    当前activity
	 * @param orgUri      剪裁原图的Uri
	 * @param desUri      剪裁后的图片的Uri
	 * @param aspectX     X方向的比例
	 * @param aspectY     Y方向的比例
	 * @param width       剪裁图片的宽度
	 * @param height      剪裁图片高度
	 * @param requestCode 剪裁图片的请求码
	 */
	public static void cropImageUri(Activity fragment, Uri orgUri, Uri desUri, int aspectX, int aspectY, int width,
									int height, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		if (Build.MANUFACTURER.toUpperCase().contains("HUAWEI")) {
			intent.putExtra("aspectX", 9998 * aspectX);
			intent.putExtra("aspectY", 9999 * aspectY);
		} else {
			intent.putExtra("aspectX", aspectX);
			intent.putExtra("aspectY", aspectY);
		}
		intent.putExtra("crop", "true");
		Rect rounded = new Rect();
		rounded.left = 0;
		rounded.top = 0;
		rounded.right = width;
		rounded.bottom = width * aspectY / aspectX;
		intent.putExtra("cropped-rect", rounded);
		intent.setDataAndType(orgUri, "image/*");
		intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("scale", true);
		//将剪切的图片保存到目标Uri中
		intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * @return 防止快速点击事件
	 */
	public synchronized static boolean isFastClick(final int milliSecond) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < milliSecond) {
			return false;
		}
		lastClickTime = time;
		return true;
	}

	/**
	 * 尺寸压缩（通过缩放图片像素来减少图片占用内存大小）
	 *
	 */

	public static void sizeCompress(Bitmap bmp, String path) {

		File file = new File(path);
		// 尺寸压缩倍数,值越大，图片尺寸越小
		int ratio = 2;
		int h = bmp.getHeight();
		int w = bmp.getWidth();
		int big = h;
		if (big < w){
			big = w;
		}
		if (big > 120){
			ratio = (int) ((big / 120) * 0.5);
			h /= ratio;
			w /= ratio;
		}
		// 压缩Bitmap到对应尺寸
		Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Rect rect = new Rect(0, 0, w, h);
		canvas.drawBitmap(bmp, null, rect, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 把压缩后的数据存放到baos中
		result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fos){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    public static String get955CodeLimit(Context context, String msg) {
        if (msg.contains("分钟级")) {
            return context.getString(R.string.note_get_verification_code_limit1);
        } else if (msg.contains("小时级")) {
            return context.getString(R.string.note_get_verification_code_limit2);
        } else if (msg.contains("天级")) {
            return context.getString(R.string.note_get_verification_code_limit3);
        }
        return context.getString(R.string.note_get_verification_code_fail);
    }

	/**
	 * 获取分享密码的内容
	 *
	 * @param context      上下文
	 * @param passcodeType 密码类型
	 * @param keyboardPwd  键盘密码
	 * @param createDate   创建时间
	 * @param startDate    开始时间
	 * @param endDate      结束时间
	 * @param lockName     锁名称
	 * @return 分享内容
	 */
    public static String getShareContent(Context context, int passcodeType, String keyboardPwd, long createDate, long startDate, long endDate, String lockName) {
        String content = "";
        String effectiveTime;
        String expiryTime;

        switch (passcodeType) {
            case 1: // 单次 (One-time)
                effectiveTime = DateUtil.getDateToString(createDate, "yyyy-MM-dd HH:mm");
                content = context.getString(R.string.share_pwd_one_time, keyboardPwd, effectiveTime);
                break;

            case 2: // 随机永久 (Random Permanent)
                effectiveTime = DateUtil.getDateToString(createDate, "yyyy-MM-dd HH:mm");
                content = context.getString(R.string.share_pwd_permanent_random, keyboardPwd, effectiveTime);
                break;

            case 16: // 自定义永久 (Custom Permanent)
                content = context.getString(R.string.share_pwd_permanent_custom, keyboardPwd);
                break;

            case 3: // 随机限时 (Random Time-limited)
                effectiveTime = DateUtil.getDateToString(startDate, "yyyy-MM-dd HH:mm");
                expiryTime = DateUtil.getDateToString(endDate, "yyyy-MM-dd HH:mm");
                content = context.getString(R.string.share_pwd_period_random, keyboardPwd, effectiveTime, expiryTime);
                break;

            case 15: // 自定义限时 (Custom Time-limited)
                effectiveTime = DateUtil.getDateToString(startDate, "yyyy-MM-dd HH:mm");
                expiryTime = DateUtil.getDateToString(endDate, "yyyy-MM-dd HH:mm");
                content = context.getString(R.string.share_pwd_period_custom, keyboardPwd, effectiveTime, expiryTime);
                break;

            case 4: // 清空 (Clear)
                effectiveTime = DateUtil.getDateToString(createDate, "yyyy-MM-dd HH:mm");
                content = context.getString(R.string.share_pwd_clear, keyboardPwd, effectiveTime);
                break;

            case 5:  // Weekend Cyclic
            case 6:  // Daily Cyclic
            case 7:  // Workday Cyclic
            case 8:  // Monday Cyclic
            case 9:  // Tuesday Cyclic
            case 10: // Wednesday Cyclic
            case 11: // Thursday Cyclic
            case 12: // Friday Cyclic
            case 13: // Saturday Cyclic
            case 14: // Sunday Cyclic
                String cyclicMode = "";
                switch (passcodeType) {
                    case 5: cyclicMode = context.getString(R.string.weekend_cyclic); break;
                    case 6: cyclicMode = context.getString(R.string.daily_cyclic); break;
                    case 7: cyclicMode = context.getString(R.string.workday_cyclic); break;
                    case 8: cyclicMode = context.getString(R.string.monday_cyclic); break;
                    case 9: cyclicMode = context.getString(R.string.tuesday_cyclic); break;
                    case 10: cyclicMode = context.getString(R.string.wednesday_cyclic); break;
                    case 11: cyclicMode = context.getString(R.string.thursday_cyclic); break;
                    case 12: cyclicMode = context.getString(R.string.friday_cyclic); break;
                    case 13: cyclicMode = context.getString(R.string.saturday_cyclic); break;
                    case 14: cyclicMode = context.getString(R.string.sunday_cyclic); break;
                }
                content = context.getString(R.string.share_pwd_recurring,
                        keyboardPwd,                                    // %1$s
                        cyclicMode,                                     // %2$s
                        DateUtil.getDateToString(startDate, "HH:mm"),   // %3$s
                        DateUtil.getDateToString(endDate, "HH:mm")      // %4$s
                );
                break;
        }
        return content;
    }
}
