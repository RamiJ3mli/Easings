package com.ramijemli.easingsdemo.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ramijemli.easingsdemo.R
import com.ramijemli.easingsdemo.custom.CubicBezierView
import kotlinx.android.synthetic.main.activity_editor.*


class EditorActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        editor.onPathInterpolatorChanged = object : CubicBezierView.OnPathInterpolatorChanged {
            override fun onPathChanged(x1: Float, y1: Float, x2: Float, y2: Float) {
                var xT1 = x1.toString()
                var yT1 = y1.toString()
                var xT2 = x2.toString()
                var yT2 = y2.toString()

                if (xT1.length > 5) xT1 = xT1.substring(0, 5)
                if (yT1.length > 5) yT1 = yT1.substring(0, 5)
                if (xT2.length > 5) xT2 = xT2.substring(0, 5)
                if (yT2.length > 5) yT2 = yT2.substring(0, 5)

                path.text = "(${xT1}f,  ${yT1}f,  ${xT2}f,  ${yT2}f)"
            }
        }
        animate.setOnClickListener {
            editor.startAnimation()
        }
    }

    override fun onDestroy() {
        editor.onPathInterpolatorChanged = null
        animate.setOnClickListener(null)
        super.onDestroy()
    }
}
