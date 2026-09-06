package jp.co.yuji.mydebugapplication.presentation.view.activity

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.google.android.material.navigation.NavigationBarView
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.ActivityMainBinding
import jp.co.yuji.mydebugapplication.presentation.view.fragment.app.ApplicationInfoFragment
import jp.co.yuji.mydebugapplication.presentation.view.fragment.device.DeviceInfoFragment
import jp.co.yuji.mydebugapplication.presentation.view.fragment.hard.HardwareInfoFragment
import jp.co.yuji.mydebugapplication.presentation.view.fragment.other.OtherInfoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Main Activity.
 */
class MainActivity : BaseActivity(), AdvertisableActivity {

    private lateinit var binding: ActivityMainBinding

    private val mOnItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        println("$item.itemId")
        when (item.itemId) {
            R.id.navigation_device_info -> {
                val fragment = DeviceInfoFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                postLogEvent("device info")
            }
            R.id.navigation_app_info -> {
                val fragment = ApplicationInfoFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                postLogEvent("app info")
            }
            R.id.navigation_hard_info -> {
                val fragment = HardwareInfoFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                postLogEvent("hard info")
            }
            R.id.navigation_other_info -> {
                val fragment = OtherInfoFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                postLogEvent("other info")
            }
        }
        return@OnItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(mOnItemSelectedListener)

        // init view
        val fragment = DeviceInfoFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize GMA Next-Gen SDK on a background thread.
            MobileAds.initialize(
                this@MainActivity,
                // for test
                // Sample Ad Manager app ID: ca-app-pub-3940256099942544~3347511713
//                InitializationConfig.Builder("ca-app-pub-3940256099942544~3347511713").build()

                // for production
                InitializationConfig.Builder(getString(R.string.app_id)).build()
            ) {
                // Adapter initialization is complete.
            }
            // SDK initialization is complete. If you don't want to wait for bidding adapters to finish
            // initializing, start loading ads now.
        }
    }

}
