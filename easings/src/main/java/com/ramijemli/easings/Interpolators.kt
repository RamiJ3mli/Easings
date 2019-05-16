package com.ramijemli.easings

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.NonNull
import androidx.core.view.animation.PathInterpolatorCompat

class Interpolators @JvmOverloads constructor(@NonNull val easing: Easings = Easings.LINEAR, var steps: Int = 1) :
    Interpolator {

    private var interpolator: Interpolator
    private var step: Float = -1f
    private var stepTime: Float = -1f

    init {
        interpolator = when (easing) {
            Easings.LINEAR -> LinearInterpolator()
            Easings.SIN_IN -> PathInterpolatorCompat.create(.47f, 0f, .745f, .715f)
            Easings.SIN_OUT -> PathInterpolatorCompat.create(.39f, .575f, .565f, 1f)
            Easings.SIN_IN_OUT -> PathInterpolatorCompat.create(.445f, .05f, .55f, .95f)
            Easings.QUAD_IN -> PathInterpolatorCompat.create(.55f, .085f, .68f, .53f)
            Easings.QUAD_OUT -> PathInterpolatorCompat.create(.25f, .46f, .45f, .94f)
            Easings.QUAD_IN_OUT -> PathInterpolatorCompat.create(.455f, .03f, .515f, .955f)
            Easings.CUBIC_IN -> PathInterpolatorCompat.create(.55f, .055f, .675f, .19f)
            Easings.CUBIC_OUT -> PathInterpolatorCompat.create(.215f, .61f, .355f, 1f)
            Easings.CUBIC_IN_OUT -> PathInterpolatorCompat.create(.645f, .045f, .355f, 1f)
            Easings.QUART_IN -> PathInterpolatorCompat.create(.895f, .03f, .685f, .22f)
            Easings.QUART_OUT -> PathInterpolatorCompat.create(.165f, .84f, .44f, 1f)
            Easings.QUART_IN_OUT -> PathInterpolatorCompat.create(.77f, 0f, .175f, 1f)
            Easings.QUINT_IN -> PathInterpolatorCompat.create(.755f, .05f, .855f, .06f)
            Easings.QUINT_OUT -> PathInterpolatorCompat.create(.23f, 1f, .32f, 1f)
            Easings.QUINT_IN_OUT -> PathInterpolatorCompat.create(.86f, 0f, .07f, 1f)
            Easings.EXP_IN -> PathInterpolatorCompat.create(.95f, .05f, .795f, .035f)
            Easings.EXP_OUT -> PathInterpolatorCompat.create(.19f, 1f, .22f, 1f)
            Easings.EXP_IN_OUT -> PathInterpolatorCompat.create(1f, 0f, 0f, 1f)
            Easings.CIRC_IN -> PathInterpolatorCompat.create(.6f, .04f, .98f, .335f)
            Easings.CIRC_OUT -> PathInterpolatorCompat.create(.075f, .82f, .165f, 1f)
            Easings.CIRC_IN_OUT -> PathInterpolatorCompat.create(.785f, .135f, .15f, .86f)
            Easings.BACK_IN -> PathInterpolatorCompat.create(.6f, -.28f, .735f, .045f)
            Easings.BACK_OUT -> PathInterpolatorCompat.create(.175f, .885f, .32f, 1.275f)
            Easings.BACK_IN_OUT -> PathInterpolatorCompat.create(.68f, -.55f, .265f, 1.55f)
            else -> LinearInterpolator()
        }
    }

    override fun getInterpolation(value: Float): Float {
        return when (easing) {
            Easings.ELASTIC_IN -> elasticIn(value)
            Easings.ELASTIC_OUT -> elasticOut(value)
            Easings.ELASTIC_IN_OUT -> elasticInOut(value)
            Easings.BOUNCE_IN -> bounceIn(value)
            Easings.BOUNCE_OUT -> bounceOut(value)
            Easings.BOUNCE_IN_OUT -> bounceInOut(value)
            Easings.STEPS -> step(value)
            else -> interpolator.getInterpolation(value)
        }
    }

//    private fun stepStart(t: Float): Float {
//        when {
//            t == 0f -> stepIncrement = 1 / steps.toFloat()
//            t >= stepIncrement -> stepIncrement += 1 / steps.toFloat()
//        }
//        return stepIncrement
//    }

    private fun step(t: Float): Float {
        when {
            t == 0f -> {
                step = 0f
                stepTime = 1 / steps.toFloat()
            }
            t >= stepTime -> {
                stepTime += 1 / steps.toFloat()
                step  += 1 / (steps.toFloat() - 1)
            }
        }
        return step
    }


    private fun bounceIn(t: Float): Float {
        return 1f - bounceOut(1f - t)
    }

    private fun bounceOut(t: Float): Float {
        return when {
            t < 1 / 2.75 -> (7.5625 * t * t).toFloat()
            t < 2 / 2.75 -> {
                val o = t - 1.5 / 2.75
                (7.5625 * o * o + 0.75).toFloat()
            }
            t < 2.5 / 2.75 -> {
                val o = t - 2.25 / 2.75
                (7.5625 * o * o + 0.9375).toFloat()
            }
            else -> {
                val o = t - 2.625 / 2.75
                (7.5625 * o * o + 0.984375).toFloat()
            }
        }
    }

    private fun bounceInOut(t: Float): Float {
        if (t < 0.5) {
            val t = 1 - 2 * t
            return when {
                t < (1 / 2.75) -> ((1 - (7.5625 * t * t)) / 2).toFloat()
                t < (2 / 2.75) -> ((1 - (7.5625 * (t - (1.5 / 2.75)) * (t - (1.5 / 2.75)) + 0.75)) / 2).toFloat()
                t < (2.5 / 2.75) -> ((1 - (7.5625 * (t - (2.25 / 2.75)) * (t - (2.25 / 2.75)) + 0.9375)) / 2).toFloat()
                else -> ((1 - (7.5625 * (t - (2.625 / 2.75)) * (t - (2.625 / 2.75)) + 0.984375)) / 2).toFloat()
            }
        } else {
            val t = 2 * t - 1
            return when {
                t < (1 / 2.75) -> (0.5 + (7.5625 * t * t) / 2).toFloat()
                t < (2 / 2.75) -> (0.5 + (7.5625 * (t - (1.5 / 2.75)) * (t - (1.5 / 2.75)) + 0.75) / 2).toFloat()
                t < (2.5 / 2.75) -> (0.5 + (7.5625 * (t - (2.25 / 2.75)) * (t - (2.25 / 2.75)) + 0.9375) / 2).toFloat()
                else -> (0.5 + (7.5625 * (t - (2.625 / 2.75)) * (t - (2.625 / 2.75)) + 0.984375) / 2).toFloat()
            }
        }
    }

    private fun elasticIn(t: Float): Float {
        if (t == 0f || t == 1f) return t
        val pi2 = Math.PI * 2
        val s = .3 / pi2 * Math.asin(1.0)
        val o = t - 1f
        return -(1 * Math.pow(2.0, 10.0 * o) * Math.sin((o - s) * pi2 / .3)).toFloat()
    }

    private fun elasticOut(t: Float): Float {
        if (t == 0f || t == 1f) return t
        val pi2 = Math.PI * 2
        val s = .3 / pi2 * Math.asin(1.0)
        return (Math.pow(2.0, (-10 * t).toDouble()) * Math.sin((t - s) * pi2 / .3) + 1).toFloat()
    }

    private fun elasticInOut(t: Float): Float {
        val pi2 = Math.PI * 2
        val s = .45 / pi2 * Math.asin(1.0)
        var o = t * 2f
        return if (o < 1) {
            o -= 1f
            (-0.5f * (Math.pow(2.0, (10 * o).toDouble()) * Math.sin((o - s) * pi2 / .45))).toFloat()
        } else {
            o -= 1f
            (Math.pow(2.0, (-10 * o).toDouble()) * Math.sin((o - s) * pi2 / .45) * 0.5 + 1).toFloat()
        }
    }
}
