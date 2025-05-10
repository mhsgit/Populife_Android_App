package com.populstay.populife.util.email;

import static android.net.MailTo.MAILTO_SCHEME;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.populstay.populife.R;
import com.populstay.populife.util.toast.ToastUtil;

public class EmailUtil {


    private static Intent createEmailIntent(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(MAILTO_SCHEME));

        // 添加邮件参数（双重保障）
        Uri.Builder uriBuilder = Uri.parse(MAILTO_SCHEME).buildUpon();
        uriBuilder.appendQueryParameter("extra_email", Uri.encode(emailAddress));
        intent.setData(uriBuilder.build());
        // 兼容不同客户端的额外参数
        intent.putExtra("extra_email", new String[]{emailAddress});
        return intent;
    }

     public static void sendEmail(Context context) {
        String emailAddress = "support@populife.co";
        // 构建mailto URI
        Uri mailUri = Uri.parse("mailto:" + Uri.encode(emailAddress));

        // 创建Intent
        Intent intent = new Intent(Intent.ACTION_SENDTO, mailUri);
        intent.putExtra(Intent.EXTRA_EMAIL, Uri.encode(emailAddress)); // 双重保障

        //intent = createEmailIntent(emailAddress);
        // 验证是否有邮件应用
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            ToastUtil.showToast(context.getString(R.string.email_install_hint));
        }
    }

}
