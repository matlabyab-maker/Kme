package com.kme.keyboard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildScreen();
        updateStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    private void buildScreen() {
        int pad = dp(20);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, pad, pad, pad);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText(R.string.setup_title);
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(64)));

        TextView help = new TextView(this);
        help.setText(R.string.setup_help);
        help.setTextSize(16);
        help.setGravity(Gravity.CENTER);
        root.addView(help, new LinearLayout.LayoutParams(-1, dp(100)));

        status = new TextView(this);
        status.setTextSize(16);
        status.setGravity(Gravity.CENTER);
        root.addView(status, new LinearLayout.LayoutParams(-1, dp(64)));

        Button enable = new Button(this);
        enable.setText(R.string.enable_keyboard);
        enable.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            } catch (Exception ignored) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        root.addView(enable, new LinearLayout.LayoutParams(-1, dp(56)));

        Button choose = new Button(this);
        choose.setText(R.string.choose_keyboard);
        choose.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showInputMethodPicker();
        });
        root.addView(choose, new LinearLayout.LayoutParams(-1, dp(56)));
        setContentView(root);
    }

    private void updateStatus() {
        if (status == null) return;
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        String serviceId = new ComponentName(this, KmeInputMethodService.class).flattenToShortString();
        boolean enabled = false;
        if (imm != null) {
            for (android.view.inputmethod.InputMethodInfo info : imm.getEnabledInputMethodList()) {
                if (serviceId.equals(info.getId())) { enabled = true; break; }
            }
        }
        status.setText(enabled ? R.string.keyboard_enabled : R.string.keyboard_disabled);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
