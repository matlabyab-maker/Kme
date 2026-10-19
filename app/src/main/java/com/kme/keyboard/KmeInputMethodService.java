package com.kme.keyboard;

import android.inputmethodservice.InputMethodService;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Starter IME. The supplied layout image is displayed as a reference surface.
 * Send invokes the focused editor's declared action, then falls back to Enter.
 */
public class KmeInputMethodService extends InputMethodService {
    private LinearLayout root;

    @Override
    public View onCreateInputView() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248, 247, 239));

        ImageView preview = new ImageView(this);
        try {
            preview.setImageDrawable(Drawable.createFromStream(
                getAssets().open("keyboard_layout.jpg"), null));
        } catch (Exception ignored) { }
        preview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        preview.setAdjustViewBounds(true);
        root.addView(preview, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        Button send = new Button(this);
        send.setText("Send  ➤");
        send.setTextSize(18);
        send.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        send.setAllCaps(false);
        send.setOnClickListener(v -> performSendAction());
        root.addView(send, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(52)));

        TextView note = new TextView(this);
        note.setText("Kme • Send uses the focused site's editor action");
        note.setTextSize(11);
        note.setGravity(Gravity.CENTER);
        note.setTextColor(Color.DKGRAY);
        root.addView(note, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(24)));
        return root;
    }

    private void performSendAction() {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        EditorInfo info = getCurrentInputEditorInfo();
        int action = info == null ? EditorInfo.IME_ACTION_NONE : (info.imeOptions & EditorInfo.IME_MASK_ACTION);
        boolean sent = false;
        if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
            sent = ic.performEditorAction(action);
        }
        if (!sent) {
            ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER));
            ic.sendKeyEvent(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER));
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
