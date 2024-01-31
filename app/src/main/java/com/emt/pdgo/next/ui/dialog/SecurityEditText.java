package com.emt.pdgo.next.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.pdp.rmmit.pdp.R;

public class SecurityEditText extends AppCompatEditText {
    private KeyboardDialog dialog;
    private final KeyboardAttribute keyboardAttribute;

    public SecurityEditText(Context context) {
        this(context, null);
    }

    public SecurityEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public SecurityEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SecurityEditText);
        ColorStateList chooserSelectedColor = a.getColorStateList(R.styleable.SecurityEditText_chooserSelectedColor);
        ColorStateList chooserUnselectedColor = a.getColorStateList(R.styleable.SecurityEditText_chooserUnselectedColor);
        Drawable chooserBackground = a.getDrawable(R.styleable.SecurityEditText_chooserBackground);
        Drawable keyboardBackground = a.getDrawable(R.styleable.SecurityEditText_keyboardBackground);
        boolean isKeyPreview = a.getBoolean(R.styleable.SecurityEditText_keyPreview, true);
        a.recycle();
        keyboardAttribute = new KeyboardAttribute(chooserSelectedColor, chooserUnselectedColor, chooserBackground, keyboardBackground, isKeyPreview);
        initialize();
    }

    private void initialize() {
        setClickable(true);
        setShowSoftInputOnFocus(false);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            hideSystemKeyboard();
            showSoftInput();
        } else {
            hideSoftKeyboard();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        if (this.isFocused()) {
            hideSystemKeyboard();
            showSoftInput();
        }
        return false;
    }

    private void hideSystemKeyboard() {
        InputMethodManager manager = (InputMethodManager) this.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    private void showSoftInput() {
        if (dialog == null) {
            dialog = KeyboardDialog.show(getContext(), this);
        } else {
            dialog.show();
        }
    }

    private void hideSoftKeyboard() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.isFocused()) {
            hideSystemKeyboard();
            showSoftInput();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isFocused()) {
            hideSoftKeyboard();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus && hasFocus()) {
            this.post(() -> {
                hideSystemKeyboard();
                showSoftInput();
            });
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                return true;
            } else {
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    public KeyboardAttribute getKeyboardAttribute() {
        return keyboardAttribute;
    }
}
