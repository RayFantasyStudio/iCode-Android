package com.rayfantasy.icode.ui.activity


import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.rayfantasy.icode.R
import org.jetbrains.anko.startActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {
    val handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_splash)
        handler.postDelayed(
                Runnable {
                    run {
                        startActivity<MainActivity>()
                        finish();
                    }
                }
                , 1500)
    }
}
