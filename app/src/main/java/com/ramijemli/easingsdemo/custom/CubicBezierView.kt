package com.ramijemli.easingsdemo.custom


import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.animation.doOnEnd


class CubicBezierView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val chartAnimation = ValueAnimator.ofFloat(0f, 1f)
    private var mPathPaint = Paint(ANTI_ALIAS_FLAG)
    private var mRulersPaint = Paint(ANTI_ALIAS_FLAG)
    private var mAxisPaint = Paint(ANTI_ALIAS_FLAG)
    private var mAnchorPaint = Paint(ANTI_ALIAS_FLAG)
    private var mHorRulerPath = Path()
    private var mVerRulerPath = Path()
    private var mTopAxisPath = Path()
    private var mBottomAxisPath = Path()

    private var mChartPath = Path()
    private var first = true
    private var anchorOffset = dipToPixels(context, 16f)
    private var offset = dipToPixels(context, 60f)
    private var radius = dipToPixels(context, 9f)
    private var mAnchorRect = RectF()


    init {
        //CHART LINE PAINT
        mPathPaint.color = Color.parseColor("#F44336")
        mPathPaint.strokeWidth = dipToPixels(context, 2f)
        mPathPaint.style = Paint.Style.STROKE
        mPathPaint.strokeCap = Paint.Cap.BUTT

        //RULERS PAINT
        mRulersPaint.color = Color.parseColor("#18FFFF")
        mRulersPaint.strokeWidth = dipToPixels(context, .5f)
        mRulersPaint.style = Paint.Style.STROKE
        mRulersPaint.strokeCap = Paint.Cap.ROUND
        val rulerPathEffect = DashPathEffect(floatArrayOf(dipToPixels(context, 6f), dipToPixels(context, 3f)), 0f)
        mRulersPaint.pathEffect = rulerPathEffect

        //CIRCLE PAINT
        mAnchorPaint.color = Color.parseColor("#F44336")
        mAnchorPaint.style = Paint.Style.FILL

        //AXIS PAINT
        mAxisPaint.color = Color.parseColor("#BDBDBD")
        mAxisPaint.strokeWidth = dipToPixels(context, .5f)
        mAxisPaint.style = Paint.Style.STROKE
        mAxisPaint.strokeCap = Paint.Cap.ROUND
        val axisPathEffect = DashPathEffect(floatArrayOf(dipToPixels(context, 6f), dipToPixels(context, 3f)), 0f)
        mAxisPaint.pathEffect = axisPathEffect

        //ANIMATION SETUP
        chartAnimation.duration = 4000
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        detach()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mAnchorRect.top = height - offset - radius
        mAnchorRect.bottom = height - offset + radius
        mAnchorRect.left = anchorOffset
        mAnchorRect.right = anchorOffset + radius * 2

        //AXIS
        mTopAxisPath.moveTo(mAnchorRect.right + anchorOffset, offset)
        mTopAxisPath.lineTo(width.toFloat() - 2 * (anchorOffset + radius), offset)

        mBottomAxisPath.moveTo(mAnchorRect.right + anchorOffset, height - offset)
        mBottomAxisPath.lineTo(width.toFloat() - 2 * (anchorOffset + radius), height - offset)

        mChartPath.moveTo(mAnchorRect.right + anchorOffset, height - offset)
        mChartPath.lineTo(width.toFloat() - 2 * (anchorOffset + radius), offset)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //AXIS
        canvas.drawPath(mTopAxisPath, mAxisPaint)
        canvas.drawPath(mBottomAxisPath, mAxisPaint)

        //HORIZONTAL RULER
        canvas.drawPath(mHorRulerPath, mRulersPaint)

        //HORIZONTAL RULER
        canvas.drawPath(mVerRulerPath, mRulersPaint)

        //CHART LINE
        canvas.drawPath(mChartPath, mPathPaint)

        //CIRCLE
        canvas.drawArc(mAnchorRect, 0f, 360f, true, mAnchorPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        startAnimation()
    }

    private fun drawProgress(elapsed: Float, totalDuration: Float, progress: Float, totalProgress: Float) {
        val top = offset
        val bottom = height - offset
        val x = (elapsed / totalDuration) * (width - (radius + anchorOffset) * 4) + mAnchorRect.right + anchorOffset
        val y = bottom - ((bottom - top) * (progress / totalProgress))

        if (first) {
            mChartPath.moveTo(x, y)
            first = false
        }

        //CHART LINE
        mChartPath.lineTo(x, y)

        //CIRCLE
        mAnchorRect.top = y - radius
        mAnchorRect.bottom = y + radius

        //HORIZONTAL RULER
        mHorRulerPath.reset()
        mHorRulerPath.moveTo(mAnchorRect.right + anchorOffset, y)
        mHorRulerPath.lineTo(width.toFloat(), y)

        //VERTICAL RULER
        mVerRulerPath.reset()
        mVerRulerPath.moveTo(x, 0f)
        mVerRulerPath.lineTo(x, height.toFloat())

        invalidate()
    }

    fun setInterpolator(interpolator: TimeInterpolator?) {
        interpolator?.let {
            chartAnimation.interpolator = interpolator
        }
    }

    private fun detach(){
        chartAnimation?.apply {
            if (isRunning) cancel()
            removeAllUpdateListeners()
        }
    }

    private fun startAnimation() {
        chartAnimation?.apply {
            if(isRunning) cancel()
            currentPlayTime = 0
            removeAllUpdateListeners()

            addUpdateListener {
                drawProgress(currentPlayTime.toFloat(), duration.toFloat(), animatedValue as Float, 1f)
            }
            doOnEnd {
                currentPlayTime = 0
                clear()
                start()
            }
            start()
        }
    }

    private fun clear() {
        mChartPath.reset()
        mHorRulerPath.reset()
        mVerRulerPath.reset()
        mAnchorRect.top = height - offset - radius
        mAnchorRect.bottom = height - offset + radius
        mAnchorRect.left = anchorOffset
        mAnchorRect.right = anchorOffset + radius * 2
        first = true
        invalidate()
    }

    private fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}