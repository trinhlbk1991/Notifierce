package com.icedtealabs.notifierce;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static android.view.animation.AnimationUtils.loadAnimation;

public class Notifierce implements View.OnClickListener {

    private static final int DEFAULT_VALUE = -100000;
    private static final int DEFAULT_DURATION = 3000;

    private int icon;
    private int backgroundColor;
    private int height;
    private int iconTintColor;
    private String title;
    private String message;
    private int titleColor;
    private int messageColor;
    private boolean autoHide;
    private int duration;
    private WeakReference<LinearLayout> layoutWeakReference;
    private WeakReference<Activity> activityWeakReference;
    private boolean isCircular;
    private OnSneakerClickListener listener = null;
    private Typeface typeface = null;

    public static Builder builder(@NonNull Activity activity) {
        return new Builder(activity);
    }

    private Notifierce(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    public void show() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        LinearLayout layout = new LinearLayout(activity);
        layoutWeakReference = new WeakReference<>(layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height == DEFAULT_VALUE ? (getStatusBarHeight() + convertToDp(56)) : convertToDp(height));
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setPadding(46, getStatusBarHeight(), 46, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setElevation(6);
        }

        layout.setBackgroundColor(backgroundColor);

        if (icon != DEFAULT_VALUE) {
            if (!isCircular) {
                AppCompatImageView ivIcon = new AppCompatImageView(activity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertToDp(24), convertToDp(24));
                ivIcon.setLayoutParams(lp);

                ivIcon.setImageResource(icon);
                ivIcon.setClickable(false);
                if (iconTintColor != DEFAULT_VALUE) {
                    ivIcon.setColorFilter(iconTintColor);
                }
                layout.addView(ivIcon);
            } else {
                RoundedImageView ivIcon = new RoundedImageView(activity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(convertToDp(24), convertToDp(24));
                ivIcon.setLayoutParams(lp);

                ivIcon.setImageResource(icon);
                ivIcon.setClickable(false);
                if (iconTintColor != DEFAULT_VALUE) {
                    ivIcon.setColorFilter(iconTintColor);
                }
                layout.addView(ivIcon);
            }
        }

        LinearLayout textLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayout.setLayoutParams(textLayoutParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!title.isEmpty()) {
            TextView tvTitle = new TextView(activity);
            tvTitle.setLayoutParams(lpTv);
            tvTitle.setGravity(Gravity.CENTER_VERTICAL);
            if (!message.isEmpty())
                tvTitle.setPadding(46, 26, 26, 0); // Top padding if there is message
            else
                tvTitle.setPadding(46, 0, 26, 0); // No top padding if there is no message
            if (titleColor != DEFAULT_VALUE)
                tvTitle.setTextColor(titleColor);

            if (typeface != null)
                tvTitle.setTypeface(typeface);

            tvTitle.setTextSize(14);
            tvTitle.setText(title);
            tvTitle.setClickable(false);
            textLayout.addView(tvTitle);
        }

        if (!message.isEmpty()) {
            TextView tvMessage = new TextView(activity);
            tvMessage.setLayoutParams(lpTv);
            tvMessage.setGravity(Gravity.CENTER_VERTICAL);
            if (!title.isEmpty())
                tvMessage.setPadding(46, 0, 26, 26); // Bottom padding if there is title
            else
                tvMessage.setPadding(46, 0, 26, 0); // No bottom padding if there is no title
            if (messageColor != DEFAULT_VALUE)
                tvMessage.setTextColor(messageColor);

            if (typeface != null)
                tvMessage.setTypeface(typeface);

            tvMessage.setTextSize(12);
            tvMessage.setText(message);
            tvMessage.setClickable(false);
            textLayout.addView(tvMessage);
        }
        layout.addView(textLayout);
        layout.setId(R.id.mainLayout);


        final ViewGroup viewGroup = getActivityDecorView();
        getExistingOverlayInViewAndRemove(viewGroup);

        layout.setOnClickListener(this);
        viewGroup.addView(layout);

        layout.startAnimation(loadAnimation(activity, R.anim.popup_show));
        if (autoHide) {
            Handler handler = new Handler();
            handler.removeCallbacks(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View layout = getLayout();
                    if (layout != null) {
                        layout.startAnimation(loadAnimation(activity, R.anim.popup_hide));
                        viewGroup.removeView(layout);
                    }
                }
            }, duration);
        }
    }

    public void hide() {
        View layout = getLayout();
        Context context = getActivity();
        ViewGroup decorView = getActivityDecorView();
        if (layout != null && context != null && decorView != null) {
            layout.startAnimation(loadAnimation(context, R.anim.popup_hide));
            decorView.removeView(layout);
        }
    }

    public void warning() {
        backgroundColor = Color.parseColor("#ffc100");
        titleColor = Color.parseColor("#000000");
        messageColor = Color.parseColor("#000000");
        iconTintColor = Color.parseColor("#000000");
        icon = R.drawable.ic_warning;

        show();
    }

    public void error() {
        backgroundColor = Color.parseColor("#ff0000");
        titleColor = Color.parseColor("#FFFFFF");
        messageColor = Color.parseColor("#FFFFFF");
        iconTintColor = Color.parseColor("#FFFFFF");
        icon = R.drawable.ic_error;

        show();
    }

    public void success() {
        backgroundColor = Color.parseColor("#2bb600");
        titleColor = Color.parseColor("#FFFFFF");
        messageColor = Color.parseColor("#FFFFFF");
        iconTintColor = Color.parseColor("#FFFFFF");
        icon = R.drawable.ic_success;
        show();
    }


    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onSneakerClick(view);
        }

        View layout = getLayout();
        Activity activity = getActivity();
        ViewGroup decorView = getActivityDecorView();
        if (activity != null && layout != null && decorView != null) {
            layout.startAnimation(loadAnimation(activity, R.anim.popup_hide));
            decorView.removeView(layout);
        }
    }

    private ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;
        Activity activity = getActivity();
        if (activity != null) {
            decorView = (ViewGroup) activity.getWindow().getDecorView();
        }
        return decorView;
    }

    private Activity getActivity() {
        return activityWeakReference.get();
    }

    private View getLayout() {
        return layoutWeakReference.get();
    }

    private void getExistingOverlayInViewAndRemove(ViewGroup parent) {

        for (int i = 0; i < parent.getChildCount(); i++) {

            View child = parent.getChildAt(i);
            if (child.getId() == R.id.mainLayout) {
                parent.removeView(child);
            }
            if (child instanceof ViewGroup) {
                getExistingOverlayInViewAndRemove((ViewGroup) child);
            }
        }
    }

    private int getStatusBarHeight() {
        Activity activity = getActivity();
        int statusBarHeight = 0;
        if (activity != null) {
            Rect rectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            statusBarHeight = rectangle.top;
        }

        return statusBarHeight;
    }

    private int convertToDp(float sizeInDp) {
        Activity activity = getActivity();
        float scale = 1.0f;
        if (activity != null) {
            scale = activity.getResources().getDisplayMetrics().density;
        }
        return (int) (sizeInDp * scale + 0.5f);
    }

    public interface OnSneakerClickListener {
        void onSneakerClick(View view);
    }

    public static class Builder {

        private Activity activity;
        private String title = "";
        private int titleColor = DEFAULT_VALUE;
        private String message = "";
        private int messageColor = DEFAULT_VALUE;
        private int icon = DEFAULT_VALUE;
        private boolean isCircular = false;
        private int iconColor = DEFAULT_VALUE;
        private int backgroundColor = DEFAULT_VALUE;
        private boolean autoHide = true;
        private int height = DEFAULT_VALUE;
        private int duration = DEFAULT_DURATION;
        private OnSneakerClickListener listener = null;
        private Typeface typeface = null;

        private Builder(@NonNull Activity activity) {
            this.activity = activity;
        }

        public Notifierce build() {
            Notifierce notifierce = new Notifierce(activity);
            notifierce.title = title;
            notifierce.titleColor = titleColor;
            notifierce.message = message;
            notifierce.messageColor = messageColor;
            notifierce.icon = icon;
            notifierce.isCircular = isCircular;
            notifierce.iconTintColor = iconColor;
            notifierce.backgroundColor = backgroundColor;
            notifierce.autoHide = autoHide;
            notifierce.height = height;
            notifierce.duration = duration;
            notifierce.listener = listener;
            notifierce.typeface = typeface;
            return notifierce;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int messageId) {
            if (activity != null) {
                this.message = activity.getString(messageId);
            }
            return this;
        }

        public Builder setTitleColor(int color) {
            if (activity != null) {
                try {
                    titleColor = ContextCompat.getColor(activity, color);
                } catch (Exception e) {
                    titleColor = color;
                }
            }
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageColor(int color) {
            if (activity != null) {
                try {
                    messageColor = ContextCompat.getColor(activity, color);
                } catch (Exception e) {
                    messageColor = color;
                }
            }
            return this;
        }

        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public Builder makeIconCircular(boolean isCircular) {
            this.isCircular = isCircular;
            return this;
        }

        public Builder setIconColor(int color) {
            if (activity != null) {
                try {
                    iconColor = ContextCompat.getColor(activity, color);
                } catch (Exception e) {
                    iconColor = color;
                }
            }
            return this;
        }

        public Builder setBackgroundColor(int color) {
            if (activity != null) {
                try {
                    backgroundColor = ContextCompat.getColor(activity, color);
                } catch (Exception e) {
                    backgroundColor = color;
                }
            }
            return this;
        }

        public Builder autoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }


        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setOnNotifierceClickListener(OnSneakerClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

    }

}
