package jp.co.yuji.mydebugapplication.presentation.view.fragment.device

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment
import java.math.BigInteger
import java.net.InetAddress
import java.util.Locale
import java.util.TimeZone


/**
 * Device Info Fragment.
 */
class DeviceInfoFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return DeviceInfoFragment()
        }
    }

    private lateinit var binding: FragmentCommonBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        if (activity != null) {
            val adapter = CommonRecyclerViewAdapter(requireActivity(), getDeviceInfo())
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_device_info
    }

    private fun getDeviceInfo() : ArrayList<CommonDto> {
        val list = ArrayList<CommonDto>()
        addBuildInfo(list)
        addScreenInfo(list)
        addMemoryInfo(list)
        addTimeInfo(list)
        addOtherInfo(list)
        return list
    }

    private fun addBuildInfo(list : ArrayList<CommonDto>) {
        list.add(CommonDto("Android", android.os.Build.VERSION.RELEASE))
        list.add(CommonDto("API Level", (android.os.Build.VERSION.SDK_INT).toString()))
        list.add(CommonDto("Device", android.os.Build.DEVICE))
        list.add(CommonDto("Model", android.os.Build.MODEL))
        list.add(CommonDto("Product", android.os.Build.PRODUCT))
    }

    private fun addScreenInfo(list : ArrayList<CommonDto>) {
        val displayMetrics = DisplayMetrics()
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        list.add(CommonDto("Resolution", "$screenWidth X $screenHeight"))

        val metrics = resources.displayMetrics
        list.add(CommonDto("Xdpi", metrics.xdpi.toString() + " dpi"))
        list.add(CommonDto("Ydpi", metrics.ydpi.toString() + " dpi"))
        list.add(CommonDto("Density", metrics.density.toString()))
        list.add(CommonDto("DensityDpi", metrics.densityDpi.toString() + " dpi"))
        list.add(CommonDto("ScaledDensity", metrics.scaledDensity.toString()))
    }

    private fun addMemoryInfo(list : ArrayList<CommonDto>) {
        val activityManager = context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        list.add(CommonDto("Total Memory", (memoryInfo.totalMem/1024/1024).toString() + " MB"))
        list.add(CommonDto("Available Memory", (memoryInfo.availMem/1024/1024).toString() + " MB"))
        list.add(CommonDto("LowMemory Threshold", (memoryInfo.threshold/1024/1024).toString() + " MB"))
        list.add(CommonDto("LowMemory?", memoryInfo.lowMemory.toString()))
    }

    private fun addTimeInfo(list : ArrayList<CommonDto>) {
        val timeZone = TimeZone.getDefault ()
        list.add(CommonDto("TimeZone", timeZone.displayName))
        val uptime = SystemClock.uptimeMillis()
        val day = (uptime/1000/60/60/24).toInt()
        val remainedDay = uptime%(1000*60*60*24)
        val hour = (remainedDay/1000/60/60).toInt()
        val remainedHour = remainedDay%(1000*60*60)
        val minutes = (remainedHour/1000/60).toInt()
        list.add(CommonDto("Uptime", day.toString() + " days "
                + hour.toString() + " h "
                + minutes.toString() + " m"))
    }

    @SuppressLint("HardwareIds")
    private fun addOtherInfo(list : ArrayList<CommonDto>) {
        list.add(CommonDto("Language", Locale.getDefault().language))
        list.add(CommonDto("Display Language", Locale.getDefault().displayLanguage))
        list.add(CommonDto("Display Name", Locale.getDefault().displayName))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            list.add(CommonDto("Display Script", Locale.getDefault().displayScript))
//        }
//        list.add(CommonDto("Display Variant", Locale.getDefault().displayVariant))
        list.add(CommonDto("IP Address", getIpAddress()))
        list.add(CommonDto("Mac Address", getMacAddress()))

        var androidId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
        if (androidId == null) {
            androidId = "none"
        }
        list.add(CommonDto("Android ID", androidId))
    }

    private fun getIpAddress() : String {
        try {
            val wifiManager = context?.getSystemService(WIFI_SERVICE) as WifiManager
            val wifiInfo: WifiInfo = wifiManager.connectionInfo
            val myIPAddress: ByteArray = BigInteger.valueOf(wifiInfo.ipAddress.toLong()).toByteArray()
            // you must reverse the byte array before conversion. Use Apache's commons library
            myIPAddress.reverse()
            val myInetIP: InetAddress = InetAddress.getByAddress(myIPAddress)
            return myInetIP.hostAddress
        } catch(e: Exception) {
            postLogEvent("exception in get ip address: ${e.message}")
        }
        return "none"
    }

    @SuppressLint("HardwareIds")
    private fun getMacAddress() : String {
        try {
            val wifiManager = context?.getSystemService(WIFI_SERVICE) as WifiManager
            val info = wifiManager.connectionInfo
            return info.macAddress
        } catch(e: Exception) {
            postLogEvent("exception in get mac address: ${e.message}")
        }
        return "none"
    }

}