package com.icedtealabs.notifierce;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

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
    private WeakReference<NotifierceView> layoutWeakReference;
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

        NotifierceView notifierceView = createNotifierceView(activity);
        layoutWeakReference = new WeakReference<>(notifierceView);

        final ViewGroup viewGroup = getActivityDecorView();
        getExistingOverlayInViewAndRemove(viewGroup);
        viewGroup.addView(notifierceView);

        notifierceView.startAnimation(loadAnimation(activity, R.anim.popup_show));
        if (autoHide) {
            Handler handler = new Handler();
            handler.removeCallbacks(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hide();
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
        Activity activity = getActivity();
        backgroundColor = getColor(activity, R.color.notifierce_warning);
        titleColor = getColor(activity, R.color.notifierce_white);
        messageColor = getColor(activity, R.color.notifierce_white);
        iconTintColor = getColor(activity, R.color.notifierce_white);
        icon = R.drawable.ic_warning;
        show();
    }

    public void error() {
        Activity activity = getActivity();
        backgroundColor = getColor(activity, R.color.notifierce_error);
        titleColor = getColor(activity, R.color.notifierce_white);
        messageColor = getColor(activity, R.color.notifierce_white);
        iconTintColor = getColor(activity, R.color.notifierce_white);
        icon = R.drawable.ic_error;
        show();
    }

    public void success() {
        Activity activity = getActivity();
        backgroundColor = getColor(activity, R.color.notifierce_succeed);
        titleColor = getColor(activity, R.color.notifierce_white);
        messageColor = getColor(activity, R.color.notifierce_white);
        iconTintColor = getColor(activity, R.color.notifierce_white);
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

    private NotifierceView createNotifierceView(Activity activity) {
        NotifierceView notifierceView = new NotifierceView(activity);
        notifierceView.setId(R.id.main_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        notifierceView.setLayoutParams(layoutParams);

        notifierceView.setTitle(title);
        notifierceView.setMessage(message);
        notifierceView.setTypeface(typeface);
        notifierceView.setBackgroundColor(backgroundColor);
        notifierceView.setTitleColor(titleColor);
        notifierceView.setMessageColor(messageColor);
        notifierceView.setIconTintColor(iconTintColor);
        if (icon != DEFAULT_VALUE) {
            notifierceView.setIcon(icon);
        }
        notifierceView.setOnClickListener(this);
        return notifierceView;
    }

    private void getExistingOverlayInViewAndRemove(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getId() == R.id.main_layout) {
                parent.removeView(child);
            }
            if (child instanceof ViewGroup) {
                getExistingOverlayInViewAndRemove((ViewGroup) child);
            }
        }
    }

    public interface OnSneakerClickListener {
        void onSneakerClick(View view);
    }

    private static int getStatusBarHeight(Activity activity) {
        int statusBarHeight = 0;
        if (activity != null) {
            Rect rectangle = new Rect();
            Window window = activity.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            statusBarHeight = rectangle.top;
        }

        return statusBarHeight;
    }

    private static int convertToDp(Activity activity, float sizeInDp) {
        float scale = 1.0f;
        if (activity != null) {
            scale = activity.getResources().getDisplayMetrics().density;
        }
        return (int) (sizeInDp * scale + 0.5f);
    }

    private static int getColor(Activity activity, int color) {
        if (activity != null) {
            try {
                return ContextCompat.getColor(activity, color);
            } catch (Exception e) {
                return color;
            }
        }
        return color;
    }

    public static class Builder {
        private Activity activity;
        private String title;
        private int titleColor;
        private String message;
        private int messageColor;
        private int icon;
        private boolean isCircular;
        private int iconColor;
        private int backgroundColor;
        private boolean autoHide;
        private int height;
        private int duration;
        private OnSneakerClickListener listener = null;
        private Typeface typeface = null;

        private Builder(@NonNull Activity activity) {
            this.activity = activity;
            this.title = "";
            this.titleColor = getColor(activity, R.color.notifierce_text_color);
            this.message = "";
            this.messageColor = getColor(activity, R.color.notifierce_text_color);
            this.icon = DEFAULT_VALUE;
            this.iconColor = Color.TRANSPARENT;
            this.isCircular = false;
            this.backgroundColor = getColor(activity, R.color.notifierce_default);
            this.autoHide = true;
            this.height = getStatusBarHeight(activity) + convertToDp(activity, 56);
            this.duration = DEFAULT_DURATION;
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
            titleColor = getColor(activity, color);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageColor(int color) {
            messageColor = getColor(activity, color);
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
            iconColor = getColor(activity, color);
            return this;
        }

        public Builder setBackgroundColor(int color) {
            backgroundColor = getColor(activity, color);
            return this;
        }

        public Builder autoHide(boolean autoHide) {
            this.autoHide = autoHide;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = convertToDp(activity, height);
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
