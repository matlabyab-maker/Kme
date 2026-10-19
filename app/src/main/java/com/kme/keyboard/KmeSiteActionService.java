package com.kme.keyboard;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Performs Send/File against the currently open app's page, without adding keyboard keys. */
public class KmeSiteActionService extends AccessibilityService {
    private static volatile KmeSiteActionService instance;

    @Override public void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
        AccessibilityServiceInfo info = getServiceInfo();
        if (info != null) {
            info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                    | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
            info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            setServiceInfo(info);
        }
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) { }
    @Override public void onInterrupt() { }
    @Override public void onDestroy() {
        if (instance == this) instance = null;
        super.onDestroy();
    }

    public static boolean requestAction(String action) {
        KmeSiteActionService s = instance;
        return s != null && s.clickPageControl(action);
    }

    private boolean clickPageControl(String action) {
        List<AccessibilityNodeInfo> roots = new ArrayList<>();
        try {
            for (AccessibilityWindowInfo window : getWindows()) {
                AccessibilityNodeInfo root = window.getRoot();
                if (root == null) continue;
                CharSequence pkg = root.getPackageName();
                if (pkg != null && getPackageName().contentEquals(pkg)) continue;
                roots.add(root);
            }
            if (roots.isEmpty()) {
                AccessibilityNodeInfo root = getRootInActiveWindow();
                if (root != null && (root.getPackageName() == null || !getPackageName().contentEquals(root.getPackageName()))) roots.add(root);
            }
            for (AccessibilityNodeInfo root : roots) {
                AccessibilityNodeInfo target = findTarget(root, action);
                if (target != null) {
                    boolean done = clickNodeOrParent(target);
                    target.recycle();
                    if (done) return true;
                }
            }
        } catch (Exception ignored) { }
        finally { for (AccessibilityNodeInfo n : roots) try { n.recycle(); } catch (Exception ignored) { } }
        return false;
    }

    private AccessibilityNodeInfo findTarget(AccessibilityNodeInfo root, String action) {
        if (root == null) return null;
        List<AccessibilityNodeInfo> matches = new ArrayList<>();
        collect(root, action, matches, 0);
        if (matches.isEmpty()) return null;
        AccessibilityNodeInfo result = matches.get(0);
        for (int i = 1; i < matches.size(); i++) matches.get(i).recycle();
        return result;
    }

    private void collect(AccessibilityNodeInfo node, String action, List<AccessibilityNodeInfo> out, int depth) {
        if (node == null || depth > 35 || out.size() >= 12) return;
        CharSequence text = node.getText();
        CharSequence desc = node.getContentDescription();
        String label = ((text == null ? "" : text.toString()) + " " + (desc == null ? "" : desc.toString())).toLowerCase(Locale.ROOT).trim();
        if (matches(label, action) && (node.isClickable() || hasClickableParent(node))) {
            out.add(AccessibilityNodeInfo.obtain(node));
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                collect(child, action, out, depth + 1);
                child.recycle();
            }
        }
    }

    private boolean matches(String label, String action) {
        if (label.isEmpty()) return false;
        if ("send".equals(action)) {
            String[] words = {"send", "send message", "ارسال", "فرستادن", "ارسال پیام", "submit", "post message"};
            for (String w : words) if (label.equals(w) || label.startsWith(w + " ") || label.endsWith(" " + w)) return true;
            return false;
        }
        String[] words = {"attach", "attachment", "attach file", "upload", "upload file", "choose file", "add file", "add attachment", "پیوست", "افزودن فایل", "بارگذاری فایل", "انتخاب فایل", "بارگذاری", "فایل ضمیمه"};
        for (String w : words) if (label.equals(w) || label.contains(w)) return true;
        return false;
    }

    private boolean hasClickableParent(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo parent = node.getParent();
        for (int i = 0; parent != null && i < 6; i++) {
            boolean clickable = parent.isClickable();
            AccessibilityNodeInfo next = parent.getParent();
            parent.recycle();
            if (clickable) { if (next != null) next.recycle(); return true; }
            parent = next;
        }
        if (parent != null) parent.recycle();
        return false;
    }

    private boolean clickNodeOrParent(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo current = AccessibilityNodeInfo.obtain(node);
        for (int i = 0; current != null && i < 7; i++) {
            if (current.isClickable() && current.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                current.recycle(); return true;
            }
            AccessibilityNodeInfo parent = current.getParent();
            current.recycle();
            current = parent;
        }
        if (current != null) current.recycle();
        return false;
    }
}
