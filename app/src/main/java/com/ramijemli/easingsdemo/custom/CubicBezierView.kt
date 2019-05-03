package com.ramijemli.easingsdemo.custom


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.core.animation.doOnEnd
import androidx.core.view.animation.PathInterpolatorCompat


class CubicBezierView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val mAnimation = ValueAnimator.ofFloat(0f, 1000f)

    private var mAnimationPaint = Paint(ANTI_ALIAS_FLAG)
    private var mChartPaint = Paint(ANTI_ALIAS_FLAG)
    private var mRulersPaint = Paint(ANTI_ALIAS_FLAG)
    private var mAxisPaint = Paint(ANTI_ALIAS_FLAG)
    private var mCirclePaint = Paint(ANTI_ALIAS_FLAG)

    private var mAnimationPath = Path()
    private var mChartPath = Path()
    private var mHorRulerPath = Path()
    private var mVerRulerPath = Path()
    private var mTopAxisPath = Path()
    private var mBottomAxisPath = Path()

    private var first = true
    private var offset = dipToPixels(context, 60f)
    private var radius = dipToPixels(context, 9f)
    private var cpRadius = dipToPixels(context, 18f)
    private var mChartRect = RectF()
    private var mCircleRect = RectF()
    private var mCP1Rect = RectF()
    private var mCP2Rect = RectF()
    private var isCP1 = false
    private var isCP2 = false

    @Nullable
    var onPathInterpolatorChanged: OnPathInterpolatorChanged? = null

    init {
        //ANIMATION PAINT
        mAnimationPaint.color = Color.parseColor("#F44336")
        mAnimationPaint.strokeWidth = dipToPixels(context, 3f)
        mAnimationPaint.style = Paint.Style.STROKE
        mAnimationPaint.strokeCap = Paint.Cap.BUTT

        //CHART LINE PAINT
        mChartPaint.color = Color.parseColor("#ffffff")
        mChartPaint.strokeWidth = dipToPixels(context, 2f)
        mChartPaint.style = Paint.Style.STROKE
        mChartPaint.strokeCap = Paint.Cap.BUTT

        //RULERS PAINT
        mRulersPaint.color = Color.parseColor("#18FFFF")
        mRulersPaint.strokeWidth = dipToPixels(context, .5f)
        mRulersPaint.style = Paint.Style.STROKE
        mRulersPaint.strokeCap = Paint.Cap.ROUND
        val rulerPathEffect = DashPathEffect(floatArrayOf(dipToPixels(context, 6f), dipToPixels(context, 3f)), 0f)
        mRulersPaint.pathEffect = rulerPathEffect

        //CIRCLE PAINT
        mCirclePaint.color = Color.parseColor("#F44336")
        mCirclePaint.style = Paint.Style.FILL

        //AXIS PAINT
        mAxisPaint.color = Color.parseColor("#BDBDBD")
        mAxisPaint.strokeWidth = dipToPixels(context, .5f)
        mAxisPaint.style = Paint.Style.STROKE
        mAxisPaint.strokeCap = Paint.Cap.ROUND
        val axisPathEffect = DashPathEffect(floatArrayOf(dipToPixels(context, 6f), dipToPixels(context, 3f)), 0f)
        mAxisPaint.pathEffect = axisPathEffect

        //ANIMATION SETUP
        mAnimation.duration = 2000
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        detach()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mChartRect.left = offset
        mChartRect.right = width - offset
        mChartRect.top = (h - mChartRect.width()) / 2
        mChartRect.bottom = mChartRect.top + mChartRect.width()

        mCircleRect.top = mChartRect.bottom - radius
        mCircleRect.bottom = mChartRect.bottom + radius
        mCircleRect.left = mChartRect.left / 2 - mCircleRect.height() / 2
        mCircleRect.right = mChartRect.left / 2 + mCircleRect.height() / 2

        //AXIS
        mTopAxisPath.moveTo(mChartRect.left, mChartRect.top)
        mTopAxisPath.lineTo(mChartRect.right, mChartRect.top)

        mBottomAxisPath.moveTo(mChartRect.left, mChartRect.bottom)
        mBottomAxisPath.lineTo(mChartRect.right, mChartRect.bottom)

        //CP
        mCP1Rect.left = mChartRect.centerX() - cpRadius
        mCP1Rect.top = mChartRect.top - cpRadius
        mCP1Rect.right = mChartRect.centerX() + cpRadius
        mCP1Rect.bottom = mChartRect.top + cpRadius

        mCP2Rect.left = mChartRect.centerX() - cpRadius
        mCP2Rect.top = mChartRect.bottom - cpRadius
        mCP2Rect.right = mChartRect.centerX() + cpRadius
        mCP2Rect.bottom = mChartRect.bottom + cpRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //AXIS
        mAxisPaint.color = Color.parseColor("#BDBDBD")
        canvas.drawPath(mTopAxisPath, mAxisPaint)
        canvas.drawPath(mBottomAxisPath, mAxisPaint)

        if (mAnimation.isRunning) {
            canvas.drawPath(mHorRulerPath, mRulersPaint)
            canvas.drawPath(mVerRulerPath, mRulersPaint)
            canvas.drawPath(mAnimationPath, mAnimationPaint)
        } else {
            //CHART LINE
            mChartPath.reset()
            mChartPath.moveTo(mChartRect.left, mChartRect.bottom)
            mChartPath.cubicTo(
                mCP2Rect.centerX(),
                mCP2Rect.centerY(),
                mCP1Rect.centerX(),
                mCP1Rect.centerY(),
                mChartRect.right,
                mChartRect.top
            )
            mChartPaint.strokeWidth = dipToPixels(context, 2f)
            canvas.drawPath(mChartPath, mChartPaint)

            //FIRST CONTROL POINT
            mAxisPaint.color = Color.parseColor("#F44336")
            canvas.drawLine(mChartRect.right, mChartRect.top, mCP1Rect.centerX(), mCP1Rect.centerY(), mAxisPaint)
            canvas.drawArc(mCP1Rect, 0f, 360f, true, mCirclePaint)
            //SECOND CONTROL POINT
            canvas.drawLine(mChartRect.left, mChartRect.bottom, mCP2Rect.centerX(), mCP2Rect.centerY(), mAxisPaint)
            canvas.drawArc(mCP2Rect, 0f, 360f, true, mCirclePaint)
        }

        //CIRCLE
        canvas.drawArc(mCircleRect, 0f, 360f, true, mCirclePaint)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointX = event.x
        val pointY = event.y
        if (mAnimation.isRunning) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when {
                    isInsideCP1(pointX, pointY) -> isCP1 = true
                    isInsideCP2(pointX, pointY) -> isCP2 = true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isCP1) {
                    if (pointX + cpRadius <= mChartRect.right && pointX - cpRadius >= mChartRect.left) {
                        mCP1Rect.left = pointX - cpRadius
                        mCP1Rect.right = pointX + cpRadius
                    }
                    if (pointY + cpRadius <= height && pointY - cpRadius >= 0) {
                        mCP1Rect.top = pointY - cpRadius
                        mCP1Rect.bottom = pointY + cpRadius
                    }
                    postInvalidate()
                }
                if (isCP2) {
                    if (pointX + cpRadius <= mChartRect.right && pointX - cpRadius >= mChartRect.left) {
                        mCP2Rect.left = pointX - cpRadius
                        mCP2Rect.right = pointX + cpRadius
                    }
                    if (pointY + cpRadius <= height && pointY - cpRadius >= 0) {
                        mCP2Rect.top = pointY - cpRadius
                        mCP2Rect.bottom = pointY + cpRadius
                    }
                    postInvalidate()
                }
                val x1 = (mCP2Rect.centerX() - mChartRect.left) / mChartRect.height()
                val y1 = (mChartRect.bottom - mCP2Rect.centerY()) / mChartRect.height()
                val x2 = (mCP1Rect.centerX() - mChartRect.left) / mChartRect.height()
                val y2 = (mChartRect.bottom - mCP1Rect.centerY()) / mChartRect.height()
                onPathInterpolatorChanged?.onPathChanged(x1, y1, x2, y2)
            }

            MotionEvent.ACTION_UP -> {
                isCP1 = false
                isCP2 = false
            }
        }

        postInvalidate() // Indicate view should be redrawn
        return true // Indicate we've consumed the touch
    }

    private fun isInsideCP1(x: Float, y: Float): Boolean {
        return (Math.sqrt(
            Math.pow(
                Math.abs(x - mCP1Rect.centerX().toDouble()),
                2.0
            ) + Math.pow(Math.abs(y - mCP1Rect.centerY().toDouble()), 2.0)
        ) < cpRadius)
    }

    private fun isInsideCP2(x: Float, y: Float): Boolean {
        return (Math.sqrt(
            Math.pow(
                Math.abs(x - mCP2Rect.centerX().toDouble()),
                2.0
            ) + Math.pow(Math.abs(y - mCP2Rect.centerY().toDouble()), 2.0)
        ) < cpRadius)
    }

    private fun drawProgress(elapsed: Float, totalDuration: Float, progress: Float, totalProgress: Float) {
        val x = (elapsed / totalDuration) * mChartRect.width() + mChartRect.left
        val y = mChartRect.bottom - (mChartRect.height() * (progress / totalProgress))

        if (first) {
            mAnimationPath.moveTo(x, y)
            first = false
        }

        //CHART LINE
        mAnimationPath.lineTo(x, y)

        //CIRCLE
        mCircleRect.top = y - mCircleRect.width() / 2
        mCircleRect.bottom = y + mCircleRect.width() / 2

        //HORIZONTAL RULER
        mHorRulerPath.reset()
        mHorRulerPath.moveTo(mChartRect.left, y)
        mHorRulerPath.lineTo(width.toFloat(), y)

        //VERTICAL RULER
        mVerRulerPath.reset()
        mVerRulerPath.moveTo(x, 0f)
        mVerRulerPath.lineTo(x, height.toFloat())

        invalidate()
    }

    private fun detach() {
        onPathInterpolatorChanged = null
        mAnimation?.apply {
            if (isRunning) cancel()
            removeAllUpdateListeners()
        }
    }

    fun startAnimation() {
        mAnimation?.apply {
            if (isRunning) cancel()
            val x1 = (mCP2Rect.centerX() - mChartRect.left) / mChartRect.height()
            val y1 = (mChartRect.bottom - mCP2Rect.centerY()) / mChartRect.height()
            val x2 = (mCP1Rect.centerX() - mChartRect.left) / mChartRect.height()
            val y2 = (mChartRect.bottom - mCP1Rect.centerY()) / mChartRect.height()
            interpolator = PathInterpolatorCompat.create(x1, y1, x2, y2)
            removeAllUpdateListeners()

            addUpdateListener {
                drawProgress(currentPlayTime.toFloat(), duration.toFloat(), animatedValue as Float, 1000f)
            }
            doOnEnd {
                first = true
                mAnimationPath.reset()
                invalidate()
            }
            start()
        }
    }

    private fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }

    interface OnPathInterpolatorChanged {
        fun onPathChanged(x1: Float, y1: Float, x2: Float, y2: Float)
    }
}