package com.kme.keyboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/** Kme keyboard. Only the keys shown in keyboard_layout.jpg are interactive. */
public class KmeInputMethodService extends InputMethodService {
    private LinearLayout keyboardRoot;
    private boolean caps = false;
    private boolean symbolsPage = false;
    private boolean englishMode = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int KEY_BG = Color.rgb(250, 249, 241);
    private static final int KEY_TEXT = Color.rgb(37, 52, 62);
    private static final int SUN_YELLOW = Color.rgb(255, 205, 35);

    @Override public View onCreateInputView() {
        keyboardRoot = new LinearLayout(this);
        keyboardRoot.setOrientation(LinearLayout.VERTICAL);
        keyboardRoot.setPadding(dp(1), dp(1), dp(1), dp(1));
        keyboardRoot.setBackgroundColor(Color.rgb(231, 229, 218));

        buildKeyboard();
        return keyboardRoot;
    }

    private void buildKeyboard() {
        row(43,
            key("Past",1f,"paste"), key("Copy",1f,"copy"), key("Copy\nScreen",1.12f,"copy_screen"),
            key("Select",1f,"select"), key("ReDo",1f,"redo"), key("UnDo",1f,"undo"),
            key("8\nHistory",1f,"history"), key("Tool",1f,"tool"), key("File",1f,"file"), key("Send",1f,"send"));
        row(34, spacer(5f), key("↑",.55f,"up"), key("↓",.55f,"down"), key("Resize",1.1f,"resize"), key("Muse",1.1f,"muse"));
        String[] digits = englishMode
            ? new String[]{"1","2","3","4","5","6","7","8","9","0"}
            : new String[]{"۱","۲","۳","۴","۵","۶","۷","۸","۹","۰"};
        row(47,
            key(digits[0]+"\\n!",1f,"digit:1"),key(digits[1]+"\\n@",1f,"digit:2"),
            key(digits[2]+"\\n#",1f,"digit:3"),key(digits[3]+"\\n$",1f,"digit:4"),
            key(digits[4]+"\\n%",1f,"digit:5"),key(digits[5]+"\\n^",1f,"digit:6"),
            key(digits[6]+"\\n&",1f,"digit:7"),key(digits[7]+"\\n*",1f,"digit:8"),
            key(digits[8]+"\\n(",1f,"digit:9"),key(digits[9]+"\\n)",1f,"digit:0"),
            key("⌫",1.45f,"delete"));
        if (englishMode) {
            row(49,
                key("q",1f,"text:q"),key("w",1f,"text:w"),key("e",1f,"text:e"),key("r",1f,"text:r"),
                key("t",1f,"text:t"),key("y",1f,"text:y"),key("u",1f,"text:u"),key("i",1f,"text:i"),
                key("o",1f,"text:o"),key("p",1f,"text:p"),key("Enter",1.12f,"enter"));
            row(49,
                key("a",1f,"text:a"),key("s",1f,"text:s"),key("d",1f,"text:d"),key("f",1f,"text:f"),
                key("g",1f,"text:g"),key("h",1f,"text:h"),key("j",1f,"text:j"),key("k",1f,"text:k"),
                key("l",1f,"text:l"),key(";",1f,"text:;"),key("'",1f,"text:'"),spacer(1.12f));
            row(44,
                key("Caps",1.05f,"caps"),key("z",1f,"text:z"),key("x",1f,"text:x"),key("c",1f,"text:c"),
                key("v",1f,"text:v"),key("b",1f,"text:b"),key("n",1f,"text:n"),key("m",1f,"text:m"),
                key(",",1f,"text:,"),key("=",0.55f,"text:="),key("-",0.55f,"text:-"));
        } else {
            row(49,
                key("ض",1f,"text:ض"),key("ص",1f,"text:ص"),key("ث",1f,"text:ث"),key("ق",1f,"text:ق"),key("ف",1f,"text:ف"),
                key("غ",1f,"text:غ"),key("ع",1f,"text:ع"),key("ه",1f,"text:ه"),key("خ",1f,"text:خ"),key("ح",1f,"text:ح"),
                key("ج",1f,"text:ج"),key("Enter",1.12f,"enter"));
            row(49,
                key("ش",1f,"text:ش"),key("س",1f,"text:س"),key("ی",1f,"text:ی"),key("ب",1f,"text:ب"),key("ل",1f,"text:ل"),
                key("ا",1f,"text:ا"),key("ت",1f,"text:ت"),key("ن",1f,"text:ن"),key("م",1f,"text:م"),key("ک",1f,"text:ک"),
                key("گ",1f,"text:گ"),spacer(1.12f));
            row(44,
                key("Caps",1.05f,"caps"),key("ظ",1f,"text:ظ"),key("ط",1f,"text:ط"),key("ژ",1f,"text:ژ"),key("ز",1f,"text:ز"),
                key("ر",1f,"text:ر"),key("ذ",1f,"text:ذ"),key("د",1f,"text:د"),key("پ",1f,"text:پ"),key("و",1f,"text:و"),
                key("چ",1f,"text:چ"),key("=",.55f,"text:="),key("-",.55f,"text:-"));
        }
        row(52,
            key("😀",.8f,"emoji"),key("&@#\n*¥✓",.95f,"symbols"),key("اعراب",.9f,"diacritics"),key(englishMode ? "🌐 EN" : "🌐 FA",1.05f,"language"),
            key("Space",2.5f,"space"),key("،",.75f,"text:،"),key(".",.75f,"text:."),key("؟",.75f,"text:؟"),
            key("⬅",1.15f,"left"),key("➡",1.15f,"right"),key("↑",.55f,"up"),key("↓",.55f,"down"),
            key("[",.45f,"text:["),key("}",.45f,"text:}"));
    }

    private void row(int heightDp, KeySpec... keys) {
        LinearLayout r = new LinearLayout(this); r.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, dp(heightDp)); rp.bottomMargin = dp(1);
        keyboardRoot.addView(r, rp);
        for (KeySpec s : keys) {
            View v = s.action == null ? new View(this) : makeButton(s);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, -1, s.weight); lp.rightMargin = dp(1); r.addView(v, lp);
        }
    }
    private KeySpec key(String label,float weight,String action){return new KeySpec(label,weight,action);}
    private KeySpec spacer(float weight){return new KeySpec("",weight,null);}
    private Button makeButton(KeySpec s) {
        Button b = new Button(this); b.setText(s.label); b.setAllCaps(false); b.setTextColor(KEY_TEXT);
        b.setTextSize(s.label.length()>7?10:(s.label.contains("\n")?11:14)); b.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
        b.setGravity(Gravity.CENTER); b.setIncludeFontPadding(false); b.setMinWidth(0); b.setMinimumWidth(0); b.setMinHeight(0); b.setMinimumHeight(0);
        b.setPadding(dp(1),0,dp(1),0); b.setBackground(background(KEY_BG));
        b.setOnClickListener(v -> { lightKey(b); handleAction(s.action); }); return b;
    }
    private GradientDrawable background(int color) {
        GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(6)); d.setStroke(dp(1),Color.rgb(218,216,205)); return d;
    }
    private void lightKey(Button b) {
        b.setBackground(background(SUN_YELLOW));
        handler.postDelayed(() -> { if(b.getParent()!=null)b.setBackground(background(KEY_BG)); }, 320);
    }
    private void handleAction(String a) {
        if(a==null)return; InputConnection ic=getCurrentInputConnection();
        if(a.startsWith("digit:")) {
            if (ic != null) {
                String d = a.substring(6);
                if (!englishMode) {
                    String[] fa = {"۰","۱","۲","۳","۴","۵","۶","۷","۸","۹"};
                    d = fa[Integer.parseInt(d)];
                }
                ic.commitText(d, 1);
            }
            return;
        }
        if(a.startsWith("text:")){ if(ic!=null){String t=a.substring(5); if(caps&&t.length()==1&&t.matches("[a-z]"))t=t.toUpperCase(); ic.commitText(t,1);} return; }
        switch(a){
            case "space": if(ic!=null)ic.commitText(" ",1); break;
            case "delete": if(ic!=null&&!ic.deleteSurroundingText(1,0))sendKey(ic,KeyEvent.KEYCODE_DEL); break;
            case "enter": sendEnter(ic); break;
            case "send": performSendAction(); break;
            case "paste": pasteClipboard(); break;
            case "copy": case "copy_screen": copySelectedText(); break;
            case "select": if(ic!=null)sendCtrlKey(ic,KeyEvent.KEYCODE_A); break;
            case "undo": if(ic!=null)sendCtrlKey(ic,KeyEvent.KEYCODE_Z); break;
            case "redo": if(ic!=null)sendCtrlKey(ic,KeyEvent.KEYCODE_Y); break;
            case "history": pasteClipboard(); break;
            case "file": openFilePicker(); break;
            case "tool": showToast("Tool"); break;
            case "resize": showToast("برای تغییر اندازه، تنظیمات کیبورد دستگاه را بررسی کنید"); break;
            case "muse": showToast("Muse"); break;
            case "emoji": if(ic!=null)ic.commitText("😀",1); break;
            case "symbols": symbolsPage=!symbolsPage; if(ic!=null)ic.commitText(symbolsPage?"&@#*¥✓":"",1); break;
            case "diacritics": if(ic!=null)ic.commitText("َ ِ ُ ّ ْ ٰ",1); break;
            case "language":
                englishMode = !englishMode;
                if (keyboardRoot != null) {
                    keyboardRoot.removeAllViews();
                    buildKeyboard();
                }
                break;
            case "caps": caps=!caps; showToast(caps?"Caps روشن":"Caps خاموش"); break;
            case "left": if(ic!=null)sendKey(ic,KeyEvent.KEYCODE_DPAD_LEFT); break;
            case "right": if(ic!=null)sendKey(ic,KeyEvent.KEYCODE_DPAD_RIGHT); break;
            case "up": if(ic!=null)sendKey(ic,KeyEvent.KEYCODE_DPAD_UP); break;
            case "down": if(ic!=null)sendKey(ic,KeyEvent.KEYCODE_DPAD_DOWN); break;
        }
    }
    private void performSendAction() {
        InputConnection ic=getCurrentInputConnection(); if(ic==null)return;
        EditorInfo info=getCurrentInputEditorInfo(); int action=info==null?EditorInfo.IME_ACTION_NONE:(info.imeOptions&EditorInfo.IME_MASK_ACTION);
        if(action!=EditorInfo.IME_ACTION_NONE&&action!=EditorInfo.IME_ACTION_UNSPECIFIED&&ic.performEditorAction(action))return;
        sendKey(ic,KeyEvent.KEYCODE_ENTER);
    }
    private void sendEnter(InputConnection ic) {
        if(ic==null)return; EditorInfo info=getCurrentInputEditorInfo(); int action=info==null?EditorInfo.IME_ACTION_NONE:(info.imeOptions&EditorInfo.IME_MASK_ACTION);
        if(action!=EditorInfo.IME_ACTION_NONE&&action!=EditorInfo.IME_ACTION_UNSPECIFIED&&ic.performEditorAction(action))return;
        sendKey(ic,KeyEvent.KEYCODE_ENTER);
    }
    private void sendKey(InputConnection ic,int code){if(ic==null)return;ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,code));ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,code));}
    private void sendCtrlKey(InputConnection ic,int code){if(ic==null)return;ic.sendKeyEvent(new KeyEvent(0,0,KeyEvent.ACTION_DOWN,code,0,KeyEvent.META_CTRL_ON));ic.sendKeyEvent(new KeyEvent(0,0,KeyEvent.ACTION_UP,code,0,KeyEvent.META_CTRL_ON));}
    private void pasteClipboard(){ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);InputConnection ic=getCurrentInputConnection();if(cm!=null&&cm.hasPrimaryClip()&&ic!=null){CharSequence t=cm.getPrimaryClip().getItemAt(0).coerceToText(this);if(t!=null)ic.commitText(t,1);}}
    private void copySelectedText(){InputConnection ic=getCurrentInputConnection();if(ic==null)return;CharSequence t=ic.getSelectedText(0);if(t!=null&&t.length()>0){ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);if(cm!=null)cm.setPrimaryClip(ClipData.newPlainText("Kme",t));}else showToast("ابتدا متن را انتخاب کنید");}
    private void openFilePicker(){try{android.content.Intent i=new android.content.Intent(android.content.Intent.ACTION_OPEN_DOCUMENT);i.addCategory(android.content.Intent.CATEGORY_OPENABLE);i.setType("*/*");i.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);}catch(Exception e){showToast("انتخاب فایل در این برنامه پشتیبانی نمی‌شود");}}
    private void showToast(String m){Toast.makeText(this,m,Toast.LENGTH_SHORT).show();}
    private int dp(int v){return(int)(v*getResources().getDisplayMetrics().density+0.5f);}
    private static class KeySpec{final String label;final float weight;final String action;KeySpec(String l,float w,String a){label=l;weight=w;action=a;}}
}
