package com.icedtealabs.notifierce;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifierceView extends LinearLayout {
    private RoundedImageView imgIcon;
    private TextView tvTitle;
    private TextView tvMessage;

    public NotifierceView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NotifierceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotifierceView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.view_notifierce, this);
        imgIcon = view.findViewById(R.id.img_notifierce_icon);
        tvTitle = view.findViewById(R.id.tv_notifierce_title);
        tvMessage = view.findViewById(R.id.tv_notifierce_message);

        setBackgroundColor(ActivityCompat.getColor(context, R.color.notifierce_default));
    }

    public void setIcon(int iconResource) {
        imgIcon.setVisibility(VISIBLE);
        imgIcon.setImageResource(iconResource);
    }

    public void setIconTintColor(int iconTintColor) {
        imgIcon.setColorFilter(iconTintColor);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
        tvTitle.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
    }

    public void setTitle(int title) {
        setTitle(getContext().getString(title));
    }

    public void setMessage(String message) {
        tvMessage.setText(message);
        tvMessage.setVisibility(TextUtils.isEmpty(message) ? GONE : VISIBLE);
    }

    public void setMessage(int message) {
        setMessage(getResources().getString(message));
    }

    public void setTitleColor(int titleColor) {
        tvTitle.setTextColor(titleColor);
    }

    public void setMessageColor(int messageColor) {
        tvMessage.setTextColor(messageColor);
    }

    public void setTypeface(Typeface typeface) {
        tvMessage.setTypeface(typeface);
        tvTitle.setTypeface(typeface);
    }

}
