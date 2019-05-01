package com.ramijemli.easingsdemo.activity

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramijemli.easings.Easings
import com.ramijemli.easings.Interpolators
import com.ramijemli.easingsdemo.R
import com.ramijemli.easingsdemo.adapter.InterpolatorAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: InterpolatorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupRV()
    }

    private fun setupRV() {
        val llm = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
        llm.isItemPrefetchEnabled = true
        interRv.layoutManager = llm
        interRv.setHasFixedSize(true)
        adapter = InterpolatorAdapter(baseContext)
        interRv.adapter = adapter
        interRv.animate().apply {
            translationYBy(100f)
            interpolator = Interpolators(Easings.SIN_IN)
            start()
        }

        val animator = ValueAnimator.ofFloat(0f,1f)
        animator.interpolator = Interpolators(Easings.BOUNCE_OUT)
        animator.start()
    }
}
