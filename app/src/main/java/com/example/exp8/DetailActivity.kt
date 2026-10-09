package com.example.exp8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class DetailActivity : AppCompatActivity() {

    private var frameId: Int = -1
    private var isSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "DetailActivity: onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        frameId = intent.getIntExtra(EXTRA_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Untitled Frame"
        val drawableResId = intent.getIntExtra(EXTRA_RES_ID, R.drawable.frame_coastal_cliff)
        val filename = intent.getStringExtra(EXTRA_FILENAME) ?: "unknown.webp"
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""
        isSelected = intent.getBooleanExtra(EXTRA_IS_SELECTED, false)

        val imgDetail = findViewById<ImageView>(R.id.img_detail)
        val tvTitle = findViewById<TextView>(R.id.tv_detail_title)
        val tvStatus = findViewById<TextView>(R.id.tv_detail_status)
        val tvFilename = findViewById<TextView>(R.id.tv_detail_filename)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_description)
        val btnToggle = findViewById<MaterialButton>(R.id.btn_toggle_selection)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        imgDetail.setImageResource(drawableResId)
        tvTitle.text = title
        tvFilename.text = filename
        tvDescription.text = description

        updateStatusUI(tvStatus, btnToggle)

        btnToggle.setOnClickListener {
            isSelected = !isSelected
            updateStatusUI(tvStatus, btnToggle)

            val resultIntent = Intent().apply {
                putExtra(EXTRA_ID, frameId)
                putExtra(EXTRA_IS_SELECTED, isSelected)
            }
            setResult(RESULT_OK, resultIntent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun updateStatusUI(tvStatus: TextView, btnToggle: MaterialButton) {
        if (isSelected) {
            tvStatus.text = getString(R.string.status_selected)
            tvStatus.setTextColor(getColor(R.color.frame_accent))
            btnToggle.text = getString(R.string.menu_deselect)
        } else {
            tvStatus.text = getString(R.string.status_not_selected)
            tvStatus.setTextColor(getColor(R.color.frame_text_dim))
            btnToggle.text = getString(R.string.menu_select)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "DetailActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "DetailActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "DetailActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "DetailActivity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "DetailActivity: onDestroy")
    }

    companion object {
        private const val TAG = "FRAME_LIFECYCLE"
        const val EXTRA_ID = "extra_frame_id"
        const val EXTRA_TITLE = "extra_frame_title"
        const val EXTRA_RES_ID = "extra_frame_res_id"
        const val EXTRA_FILENAME = "extra_frame_filename"
        const val EXTRA_DESCRIPTION = "extra_frame_description"
        const val EXTRA_IS_SELECTED = "extra_frame_is_selected"
    }
}