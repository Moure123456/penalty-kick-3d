package com.example.penaltygame;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class GameCommands {

    private final WebView webView;

    // استلام الـ WebView لربط الأوامر باللعبة
    public GameCommands(WebView webView) {
        this.webView = webView;
    }

    // ==========================================
    // 1. أوامر حركة وجري اللاعبين (Player Motion)
    // ==========================================

    /**
     * أمر تراجع اللاعب للخلف ثم الجري والتسديد القوي نحو المرمى
     * @param dx اتجاه الانحناء الأفقي (الكيرف)
     * @param dy قوة التسديد الارتفاعية
     */
    @JavascriptInterface
    public void triggerRunUpAndKick(float dx, float dy) {
        runOnUI(() -> {
            String js = String.format("window.startPlayerRunUpAnimation(%f, %f);", dx, dy);
            webView.evaluateJavascript(js, null);
        });
    }

    /**
     * أمر القفز والانقضاض المباشر للحارس (Goal Keeper Dive)
     * @param targetX المسافة الأفقية للقفزة
     * @param targetY ارتفاع القفزة
     */
    @JavascriptInterface
    public void triggerKeeperDive(float targetX, float targetY) {
        runOnUI(() -> {
            String js = String.format("if(window.keeperDive) window.keeperDive(%f, %f);", targetX, targetY);
            webView.evaluateJavascript(js, null);
        });
    }

    // ==========================================
    // 2. أوامر الكأس واحتفاليات الفوز (Trophy & Celebration)
    // ==========================================

    /**
     * إظهار الكأس ثلاثي الأبعاد وتفعيل هتافات الجماهير والالعاب النارية
     */
    @JavascriptInterface
    public void showTrophyCelebration() {
        runOnUI(() -> webView.evaluateJavascript(
            "if(window.triggerWinSequence) window.triggerWinSequence();", null
        ));
    }

    /**
     * إعادة تصفير العدادات وبدء شوط جديد
     */
    @JavascriptInterface
    public void resetRound() {
        runOnUI(() -> webView.evaluateJavascript("resetPositions();", null));
    }

    // ==========================================
    // 3. أوامر واقعية اللاعبين والجمهور (Realism & Crowd)
    // ==========================================

    /**
     * تفعيل أو إيقاف حركة الجماهير في المدرجات
     */
    @JavascriptInterface
    public void setCrowdActive(boolean active) {
        runOnUI(() -> webView.evaluateJavascript(
            "window.animateCrowd = " + active + ";", null
        ));
    }

    /**
     * تطبيق محرك الواقعية لإضاءة البشرة وظلال اللاعبين الحقيقية
     */
    @JavascriptInterface
    public void enableRealisticHumanEngine() {
        runOnUI(() -> webView.evaluateJavascript(
            "if(window.enableRealisticEngine) window.enableRealisticEngine();", null
        ));
    }

    // دالة مساعدة لتشغيل الكود على الخيط الرئيسي للواجهة (UI Thread)
    private void runOnUI(Runnable action) {
        if (webView != null) {
            webView.post(action);
        }
    }
}

