package com.gillall.mesa.desafio1.ui.details

import android.app.Activity
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gillall.mesa.desafio1.R
import com.gillall.mesa.desafio1.databinding.DetailsFragmentBinding
import com.gillall.mesa.desafio1.mesa.NewsData


class DetailsFragment : Fragment() {

    private lateinit var detailsBinding: DetailsFragmentBinding

    private lateinit var viewModel: DetailsViewModel
    private lateinit var news: NewsData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.details_fragment,
            container,
            false
        )

        arguments?.getParcelable<NewsData>("news").also { it -> news = it!! }
        println(news)
        detailsBinding.item = news
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        detailsBinding.addToFavorites.setOnClickListener {
            viewModel.fav(news)
        }

        val cm =//need more work, status change listener
            requireActivity().getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager

        //detailsBinding.newsSite.webChromeClient = WebChromeClient()
        detailsBinding.newsSite.settings.javaScriptEnabled = true
        detailsBinding.newsSite.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url)
                return false // then it is not handled by default action
            }
        }

        if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected) {
            detailsBinding.newsSite.settings.cacheMode = WebSettings.LOAD_DEFAULT;
        } else {
            detailsBinding.newsSite.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK;
        }

        detailsBinding.newsSite.loadUrl(news.url)
        return detailsBinding.root
    }
}