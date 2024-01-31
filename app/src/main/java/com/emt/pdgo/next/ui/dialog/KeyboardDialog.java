package com.emt.pdgo.next.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.pdp.rmmit.pdp.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class KeyboardDialog extends Dialog implements KeyboardView.OnKeyboardActionListener {
    private static final int ORDER_NUMBER = 0;
    private static final int ORDER_SYMBOL = 1;
    private static final int ORDER_LETTER = 2;
    
    private Keyboard mLetterKeyboard;
    private Keyboard mSymbolKeyboard;
    private Keyboard mNumberKeyboard;
    private int mCurrentOrder;
    private SparseArray<Keyboard> mOrderToKeyboard;
    private ArrayList<String> mNumberPool;

    private ColorStateList mSelectedTextColor = ColorStateList.valueOf(Color.BLUE);
    private ColorStateList mUnSelectedTextColor = ColorStateList.valueOf(Color.BLACK);

    private boolean isNumberRandom = true;
    private boolean isUpper = false;

    private WeakReference<SecurityEditText> mTargetEditText;
    private KeyboardAttribute attribute;

    public KeyboardDialog(Context context, SecurityEditText editText) {
        super(context, R.style.NoFrameDialog);
        mOrderToKeyboard = new SparseArray<>();
        mNumberPool = new ArrayList<>();
        mNumberPool.add("48#0");
        mNumberPool.add("49#1");
        mNumberPool.add("50#2");
        mNumberPool.add("51#3");
        mNumberPool.add("52#4");
        mNumberPool.add("53#5");
        mNumberPool.add("54#6");
        mNumberPool.add("55#7");
        mNumberPool.add("56#8");
        mNumberPool.add("57#9");
        mTargetEditText = new WeakReference<>(editText);
    }

    public static KeyboardDialog show(Context context, SecurityEditText editText) {
        KeyboardDialog dialog = new KeyboardDialog(context, editText);
        dialog.show();
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mBinding = DialogKeyboardBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(R.layout.keyboard_dialog);
        initAttribute();
        initKeyboards();
        initKeyboardChooser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            setCanceledOnTouchOutside(false);
            window.setAttributes(layoutParams);
            window.setWindowAnimations(R.style.KeyboardDialogAnimation);
        }
    }

    private void initAttribute() {
        attribute = mTargetEditText.get().getKeyboardAttribute();
    }

    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;
    @BindView(R.id.keyboard_chooser)
    ConstraintLayout keyboardChooser;
    private void initKeyboards() {
        if (attribute.keyboardBackground != null) {
            keyboardView.setBackground(attribute.keyboardBackground);
        }
        if (attribute.chooserBackground != null) {
            keyboardChooser.setBackground(attribute.chooserBackground);
        }
        if (attribute.chooserSelectedColor != null) {
            mSelectedTextColor = attribute.chooserSelectedColor;
        }
        if (attribute.chooserUnselectedColor != null) {
            mUnSelectedTextColor = attribute.chooserUnselectedColor;
        }
        if (attribute.isKeyPreview) {
            keyboardView.setPreviewEnabled(true);
        } else {
            keyboardView.setPreviewEnabled(false);
        }
        keyboardView.setOnKeyboardActionListener(this);
        if (isPortrait()) {
            mLetterKeyboard = new Keyboard(getContext(), R.xml.gs_keyboard_english);
            mSymbolKeyboard = new Keyboard(getContext(), R.xml.gs_keyboard_symbols_shift);
            mNumberKeyboard = new Keyboard(getContext(), R.xml.gs_keyboard_number);
        } else {
            mLetterKeyboard = new Keyboard(getContext(), R.xml.gs_keyboard_english_land);
            mSymbolKeyboard = new Keyboard(getContext(), R.xml.gs_keyboard_symbols_shift_land);
            mNumberKeyboard = new Keyboard(getContext(), R.xml.gs_keyboard_number_land);
        }
        if (isNumberRandom) {
            randomNumbers();
        }
        mOrderToKeyboard.put(ORDER_NUMBER, mNumberKeyboard);
        mOrderToKeyboard.put(ORDER_SYMBOL, mSymbolKeyboard);
        mOrderToKeyboard.put(ORDER_LETTER, mLetterKeyboard);
        mCurrentOrder = ORDER_LETTER;
        onCurrentKeyboardChange();
    }

    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_symbol)
    TextView tvSymbol;
    @BindView(R.id.tv_letter)
    TextView tvLetter;
    private void initKeyboardChooser() {
        tvNumber.setOnClickListener(v -> {
            mCurrentOrder = ORDER_NUMBER;
            onCurrentKeyboardChange();
        });
        tvSymbol.setOnClickListener(v -> {
            mCurrentOrder = ORDER_SYMBOL;
            onCurrentKeyboardChange();
        });

        tvLetter.setOnClickListener(v -> {
            mCurrentOrder = ORDER_LETTER;
            onCurrentKeyboardChange();
        });
    }

    private void onCurrentKeyboardChange() {
        if (mCurrentOrder == ORDER_NUMBER && isNumberRandom) {
            randomNumbers();
        }
        keyboardView.setKeyboard(mOrderToKeyboard.get(mCurrentOrder));
        switch (mCurrentOrder) {
            case ORDER_NUMBER:
                tvNumber.setTextColor(mSelectedTextColor);
                tvSymbol.setTextColor(mUnSelectedTextColor);
                tvLetter.setTextColor(mUnSelectedTextColor);
                break;
            case ORDER_SYMBOL:
                tvNumber.setTextColor(mUnSelectedTextColor);
                tvSymbol.setTextColor(mSelectedTextColor);
                tvLetter.setTextColor(mUnSelectedTextColor);
                break;
            case ORDER_LETTER:
                tvNumber.setTextColor(mUnSelectedTextColor);
                tvSymbol.setTextColor(mUnSelectedTextColor);
                tvLetter.setTextColor(mSelectedTextColor);
                break;
            default:
                throw new IllegalStateException(getContext().getString(R.string.exception_invalid_keyboard));
        }
    }

    private boolean isPortrait() {
        return getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 键盘数字随机切换
     */
    private void randomNumbers() {
        if (mNumberKeyboard != null) {
            ArrayList<String> source = new ArrayList<>(mNumberPool);
            List<Keyboard.Key> keys = mNumberKeyboard.getKeys();
            for (Keyboard.Key key : keys) {
                if (key.label != null && isNumber(key.label.toString())) {
                    int number = new Random().nextInt(source.size());
                    String[] text = source.get(number).split("#");
                    key.label = text[1];
                    key.codes[0] = Integer.valueOf(text[0], 10);
                    source.remove(number);
                }
            }
        }
    }

    private boolean isNumber(String str) {
        String numStr = getContext().getString(R.string.zeroToNine);
        return numStr.contains(str.toLowerCase());
    }

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
        if (mLetterKeyboard != null) {
            List<Keyboard.Key> keys = mLetterKeyboard.getKeys();
            if (isUpper) {
                isUpper = false;
                for (Keyboard.Key key : keys) {
                    if (key.label != null && isLetter(key.label.toString())) {
                        key.label = key.label.toString().toLowerCase();
                        key.codes[0] = key.codes[0] + 32;
                    }
                    if (key.codes[0] == -1) {
                        key.icon = getContext().getResources().getDrawable(
                                R.drawable.keyboard_shift);
                    }
                }
            } else {// 小写切换大写
                isUpper = true;
                for (Keyboard.Key key : keys) {
                    if (key.label != null && isLetter(key.label.toString())) {
                        key.label = key.label.toString().toUpperCase();
                        key.codes[0] = key.codes[0] - 32;
                    }
                    if (key.codes[0] == -1) {
                        key.icon = getContext().getResources().getDrawable(
                                R.drawable.keyboard_shift_c);
                    }
                }
            }
        }
    }

    private boolean isLetter(String str) {
        String letterStr = getContext().getString(R.string.aToz);
        return letterStr.contains(str.toLowerCase());
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = mTargetEditText.get().getText();
        int start = mTargetEditText.get().getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            hideKeyboard();
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            changeKey();
            mCurrentOrder = ORDER_LETTER;
            onCurrentKeyboardChange();
        } else {
            editable.insert(start, Character.toString((char) primaryCode));
        }
    }

    private void hideKeyboard() {
        this.dismiss();
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
