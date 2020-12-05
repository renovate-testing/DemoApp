package io.goooler.demoapp.webview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.core.os.bundleOf
import io.goooler.demoapp.base.util.putArguments
import io.goooler.demoapp.base.util.unsafeLazy
import io.goooler.demoapp.base.widget.CustomWebView
import io.goooler.demoapp.common.base.BaseThemeFragment
import io.goooler.demoapp.webview.databinding.WebFragmentBinding

class WebFragment private constructor() : BaseThemeFragment() {

    private val binding by unsafeLazy {
        WebFragmentBinding.inflate(layoutInflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.webView.onEventListener = listener
            it.webView.addJavascriptInterface(listener, "android")
        }
    }

    var onEventListener: OnEventListener? = null

    val url: String? get() = binding.webView.url

    val canGoBack: Boolean get() = binding.webView.canGoBack()

    fun goBack() {
        binding.webView.goBack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.getString(URL)?.let {
            binding.webView.loadUrl(it)
        }
    }

    private val listener = object : CustomWebView.OnEventListener, JsInterface {
        override fun onInterceptUri(uri: Uri) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        override fun onReceivedTitle(title: String) {
            onEventListener?.onReceivedTitle(title)
        }

        override fun onProgressChanged(i: Int) {
            onEventListener?.onProgressChanged(i)
        }

        override fun loadFinish() = Unit

        @JavascriptInterface
        override fun setTitle(title: String) {
            onEventListener?.onReceivedTitle(title)
        }
    }

    interface OnEventListener {
        fun onReceivedTitle(title: String)
        fun onProgressChanged(i: Int)
    }

    companion object {
        private const val URL = "url"

        fun newInstance(url: String) = WebFragment().putArguments(
            bundleOf(
                URL to url
            )
        )
    }
}