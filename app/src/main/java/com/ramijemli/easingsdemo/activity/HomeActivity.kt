package com.ramijemli.easingsdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    }
}
