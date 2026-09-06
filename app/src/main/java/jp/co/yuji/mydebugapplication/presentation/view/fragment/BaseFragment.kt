package jp.co.yuji.mydebugapplication.presentation.view.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import jp.co.yuji.mydebugapplication.presentation.view.activity.BaseActivity

/**
 * Fragment Base Class.
 */
abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    companion object {
        private val TAG = BaseFragment::class.java.simpleName
    }

    var adView: AdView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setTitle(getTitle())
        return null
    }

    abstract fun getTitle(): Int

    private fun setTitle(stringRes: Int) {
        requireActivity().title = requireActivity().getString(stringRes)
    }

    fun setTitleLazy(stringRes: Int) {
        requireActivity().title = requireActivity().getString(stringRes)
    }

    fun postLogEvent(contentType: String) {
        val baseActivity = activity
        if (baseActivity is BaseActivity) {
            baseActivity.postLogEvent(contentType)
        }
    }

    fun loadBannerAd(adView: AdView, activity: Activity, adUnitId: String) {
        this.adView = adView
        // Get a BannerAdRequest for a 360 wide large anchored adaptive banner ad.
        val adSize = AdSize.getLargeAnchoredAdaptiveBannerAdSize(activity, 360)

        // for test
        // Sample Ad Manager app ID: ca-app-pub-3940256099942544~3347511713
        // /21775744923/example/adaptive-banner
//        val adRequest = BannerAdRequest.Builder("/21775744923/example/adaptive-banner", adSize).build()

        // for production
        val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()

        adView.loadAd(
            adRequest,
            object : AdLoadCallback<BannerAd> {
                override fun onAdLoaded(ad: BannerAd) {
                    Log.d(TAG, "Banner ad loaded.")
                }
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, "Banner ad failed to load: $adError")
                }
            },
        )
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        releaseAd()
    }

    private fun releaseAd() {
        val parentView = adView?.parent
        if (parentView is ViewGroup) {
            parentView.removeView(adView)
        }

        // Destroy the banner ad resources.
        adView?.destroy()

        // Drop reference to the banner ad.
        adView = null
    }

}