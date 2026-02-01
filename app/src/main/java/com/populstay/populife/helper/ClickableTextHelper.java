package com.populstay.populife.helper;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class ClickableTextHelper {

    public static void setClickableText(TextView textView, String prefix, String clickableText,
                                        int clickableColor, View.OnClickListener listener) {

        String fullText = prefix + clickableText;
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onClick(widget);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(clickableColor);
                ds.setUnderlineText(false);
            }
        };

        int startIndex = prefix.length();
        int endIndex = startIndex + clickableText.length();

        spannableString.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }
}