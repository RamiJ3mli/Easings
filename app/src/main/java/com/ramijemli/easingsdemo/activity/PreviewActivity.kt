package com.ramijemli.easingsdemo.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ramijemli.easings.Easings
import com.ramijemli.easings.Interpolators
import com.ramijemli.easingsdemo.R
import com.ramijemli.easingsdemo.adapter.*
import kotlinx.android.synthetic.main.activity_preview.*

const val ARG_INTERPOLATOR = "PreviewActivity.ARG_INTERPOLATOR"

class PreviewActivity : AppCompatActivity() {

    private lateinit var inter: TimeInterpolator
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ramijemli.easingsdemo.R.layout.activity_preview)

        intent.extras?.apply {
            val pos = getInt(ARG_INTERPOLATOR, 0)
            inter = when (pos) {
                SINUSOIDAL_IN -> Interpolators(Easings.SIN_IN)
                SINUSOIDAL_OUT -> Interpolators(Easings.SIN_OUT)
                SINUSOIDAL_IN_OUT -> Interpolators(Easings.SIN_IN_OUT)

                QUADRATIC_IN -> Interpolators(Easings.QUAD_IN)
                QUADRATIC_OUT -> Interpolators(Easings.QUAD_OUT)
                QUADRATIC_IN_OUT -> Interpolators(Easings.QUAD_IN_OUT)

                CUBIC_IN -> Interpolators(Easings.CUBIC_IN)
                CUBIC_OUT -> Interpolators(Easings.CUBIC_OUT)
                CUBIC_IN_OUT -> Interpolators(Easings.CUBIC_IN_OUT)

                QUARTIC_IN -> Interpolators(Easings.QUART_IN)
                QUARTIC_OUT -> Interpolators(Easings.QUART_OUT)
                QUARTIC_IN_OUT -> Interpolators(Easings.QUART_IN_OUT)

                QUINTIC_IN -> Interpolators(Easings.QUINT_IN)
                QUINTIC_OUT -> Interpolators(Easings.QUINT_OUT)
                QUINTIC_IN_OUT -> Interpolators(Easings.QUINT_IN_OUT)

                EXPONENTIAL_IN -> Interpolators(Easings.EXP_IN)
                EXPONENTIAL_OUT -> Interpolators(Easings.EXP_OUT)
                EXPONENTIAL_IN_OUT -> Interpolators(Easings.EXP_IN_OUT)

                CIRCULAR_IN -> Interpolators(Easings.CIRC_IN)
                CIRCULAR_OUT -> Interpolators(Easings.CIRC_OUT)
                CIRCULAR_IN_OUT -> Interpolators(Easings.CIRC_IN_OUT)

                BACK_IN -> Interpolators(Easings.BACK_IN)
                BACK_OUT -> Interpolators(Easings.BACK_OUT)
                BACK_IN_OUT -> Interpolators(Easings.BACK_IN_OUT)

                ELASTIC_IN -> Interpolators(Easings.ELASTIC_IN)
                ELASTIC_OUT -> Interpolators(Easings.ELASTIC_OUT)
                ELASTIC_IN_OUT -> Interpolators(Easings.ELASTIC_IN_OUT)

                BOUNCE_IN -> Interpolators(Easings.BOUNCE_IN)
                BOUNCE_OUT -> Interpolators(Easings.BOUNCE_OUT)
                BOUNCE_IN_OUT -> Interpolators(Easings.BOUNCE_IN_OUT)
                else -> LinearInterpolator()
            }

            this@PreviewActivity.resources?.getStringArray(R.array.interpolator_titles)?.apply {
                name.text = get(pos)
            }
        }

        setupAnimation()
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
    private fun setupAnimation() {
        val translateAnim = ObjectAnimator.ofFloat(translate, View.TRANSLATION_X, 0f, dipToPixels(applicationContext, 200f)).apply {
            duration = 2000
            interpolator = inter
        }

        val scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
        val scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
        val scaleAnim = ObjectAnimator.ofPropertyValuesHolder(scale, scalex, scaley).apply {
            duration = 2000
            interpolator = inter
        }

        val rotateAnim =ObjectAnimator.ofFloat(rotation, View.ROTATION, 0f, 270f).apply {
            duration = 2000
            interpolator = inter
        }

        handler = Handler()
        runnable = Runnable {
            translateAnim.start()
            scaleAnim.start()
            rotateAnim.start()
            handler.postDelayed(runnable, 2600)
        }
        handler.postDelayed(runnable, 200)
    }

    private fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

}
