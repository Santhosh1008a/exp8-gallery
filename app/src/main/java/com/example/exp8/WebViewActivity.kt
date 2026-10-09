package com.example.exp8

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutErrorState: LinearLayout
    private lateinit var tvTitle: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "WebViewActivity: onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_webview)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.webview_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webView = findViewById(R.id.web_view_container)
        progressBar = findViewById(R.id.progress_bar_loading)
        layoutErrorState = findViewById(R.id.layout_error_state)
        tvTitle = findViewById(R.id.tv_header_title)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnRefresh = findViewById<ImageButton>(R.id.btn_refresh)
        val btnRetry = findViewById<MaterialButton>(R.id.btn_retry)

        setupWebViewSettings()
        setupClients()

        val initialUrl = intent.getStringExtra(EXTRA_URL) ?: DEFAULT_URL
        loadWebUrl(initialUrl)

        btnBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }

        btnRefresh.setOnClickListener {
            layoutErrorState.visibility = View.GONE
            webView.reload()
        }

        btnRetry.setOnClickListener {
            layoutErrorState.visibility = View.GONE
            loadWebUrl(initialUrl)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    remove()
                    finish()
                }
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebViewSettings() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            allowFileAccess = false
            allowContentAccess = false
        }
    }

    private fun setupClients() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                } else {
                    progressBar.visibility = View.GONE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                if (!title.isNullOrBlank()) {
                    tvTitle.text = title
                }
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                layoutErrorState.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    layoutErrorState.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun loadWebUrl(url: String) {
        layoutErrorState.visibility = View.GONE
        webView.loadUrl(url)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "WebViewActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "WebViewActivity: onResume")
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "WebViewActivity: onPause")
        webView.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "WebViewActivity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "WebViewActivity: onDestroy")
        webView.destroy()
    }

    companion object {
        private const val TAG = "FRAME_LIFECYCLE"
        const val DEFAULT_URL = "https://unsplash.com"
        const val EXTRA_URL = "extra_web_url"
    }
}