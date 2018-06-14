package mahipal.constraintlayoutdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.animation.ValueAnimator
import java.util.concurrent.TimeUnit
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.LinearInterpolator
import android.widget.Toast


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var clockAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_pause.setOnClickListener(this)

        clockAnimator = animateSecondPointer(TimeUnit.SECONDS.toMillis(60))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_pause -> {
                if (clockAnimator != null) {
                    when {
                        clockAnimator?.isPaused!! -> {
                            clockAnimator?.resume()
                            Toast.makeText(applicationContext, "Resumed", Toast.LENGTH_SHORT).show();
                        }
                        clockAnimator?.isRunning!! -> {
                            Toast.makeText(applicationContext, "Paused", Toast.LENGTH_SHORT).show();
                            clockAnimator?.pause()
                        }
                        else -> clockAnimator?.start()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (clockAnimator != null) {
            if (clockAnimator?.isPaused!!) {
                clockAnimator?.resume()
            }
        }

        Log.d(MainActivity::class.java.simpleName,"onResume() method called")
    }

    override fun onPause() {
        super.onPause()
        if (clockAnimator?.isRunning!!) {
            clockAnimator?.pause()
        }
        Log.d(MainActivity::class.java.simpleName,"onPause() method called")
    }

    private fun animateSecondPointer(orbitDuration: Long): ValueAnimator {
        val anim = ValueAnimator.ofInt(0, 359)

        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator?.animatedValue as Int
            val layoutParams = iv_sec.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.circleAngle = value.toFloat()
            iv_sec.layoutParams = layoutParams;
        }
        anim.duration = orbitDuration
        anim.interpolator = LinearInterpolator()
        anim.repeatMode = ValueAnimator.RESTART
        anim.repeatCount = ValueAnimator.INFINITE

        return anim
    }

    override fun onDestroy() {
        super.onDestroy()
        if (clockAnimator?.isRunning!!) {
            clockAnimator?.cancel()
        }
        Log.d(MainActivity::class.java.simpleName,"onDestroy() method called")
    }
}
