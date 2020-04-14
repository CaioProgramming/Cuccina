package com.inlustris.cuccina

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.inlustris.cuccina.databinding.ActivitySplashBinding
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashbind: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setContentView(splashbind.root)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        splashbind.background.post { background() }

    }

    private fun background() {
        val colors = intArrayOf(
                this.resources.getColor(R.color.md_blue_400),
                this.resources.getColor(R.color.md_yellow_400),
                this.resources.getColor(R.color.md_blue_900),
                this.resources.getColor(R.color.md_red_400),
                this.resources.getColor(R.color.md_purple_400),
                this.resources.getColor(R.color.md_green_400),
                this.resources.getColor(R.color.md_red_A200),
                this.resources.getColor(R.color.md_light_green_500),
                this.resources.getColor(R.color.md_yellow_200),
                this.resources.getColor(R.color.md_red_A400)
        )
        val bounds = Rect()
        background.getDrawingRect(bounds)
        val random = Random()
        val color = random.nextInt(colors.size)
        background.setBackgroundColor(colors[color])
        val cx = bounds.centerX()
        val cy = bounds.centerY()
        val radius = background.width.coerceAtLeast(background.height)
        val anim = ViewAnimationUtils.createCircularReveal(background, cx, cy, 100f, radius.toFloat())
        anim.duration = 1000
        background.visibility = View.VISIBLE
        anim.start()
        Handler().postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            //overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
            finish()
        }, 2500)
    }
}