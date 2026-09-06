package jp.co.yuji.mydebugapplication.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Marker Interface.
 */
interface AdvertisableActivity

/**
 * Activity Base Class.
 */
abstract class BaseActivity() : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
//        if (this is AdvertisableActivity) {
//            println("this is AdvertisableActivity")
//            MobileAds.initialize(this)
//        }

        // Initialize the Google Mobile Ads SDK.
        // @string/app_id
//        val initConfig = InitializationConfig.Builder(getString(R.string.app_id)).build()
//        MobileAds.initialize(this@MainActivity, initConfig) {}

//        val backgroundScope = CoroutineScope(Dispatchers.IO)
//        backgroundScope.launch {
//            val initConfig = InitializationConfig.Builder(getString(R.string.app_id)).build()
//            // Initialize GMA Next-Gen SDK on a background thread.
//            MobileAds.initialize(
//                this@MainActivity,
//                // Sample Ad Manager app ID: ca-app-pub-3940256099942544~3347511713
//                InitializationConfig.Builder("SAMPLE_APP_ID").build()
//            ) {
//                // Adapter initialization is complete.
//            }
//            // SDK initialization is complete. If you don't want to wait for bidding adapters to finish
//            // initializing, start loading ads now.
//        }
    }

    fun postLogEvent(contentType: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}