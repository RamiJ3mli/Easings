package com.ramijemli.easingsdemo.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramijemli.easingsdemo.R
import com.ramijemli.easingsdemo.adapter.InterpolatorAdapter
import com.ramijemli.easingsdemo.custom.RecyclerItemClickListenr
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: InterpolatorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupLayout()
    }

    private fun setupLayout() {
        editor.setOnClickListener {
            startActivity(Intent(this@HomeActivity, EditorActivity::class.java))
        }

        val llm = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
        llm.isItemPrefetchEnabled = true
        interRv.layoutManager = llm
        interRv.setHasFixedSize(true)
        adapter = InterpolatorAdapter(baseContext)
        interRv.adapter = adapter
        interRv.addOnItemTouchListener(RecyclerItemClickListenr(this, interRv, object : RecyclerItemClickListenr.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val i = Intent(this@HomeActivity, PreviewActivity::class.java)
                i.putExtra(ARG_INTERPOLATOR,position)
                startActivity(i)
            }
        }))

        Toast.makeText(applicationContext, "Tap on a chart to see interpolator in action", Toast.LENGTH_LONG).show()
    }
}
