package jp.co.yuji.mydebugapplication.presentation.view.fragment.hard

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.domain.model.MyBatteryInfo
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment
import jp.co.yuji.mydebugapplication.presentation.view.receiver.BatteryReceiver

/**
 * Battery Info Fragment.
 */
class BatteryInfoFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return BatteryInfoFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private lateinit var adapter: CommonRecyclerViewAdapter

    private val receiver = BatteryReceiver(object: BatteryReceiver.OnBatteryChangeListener {
        override fun onBatteryChange(myBatteryInfo: MyBatteryInfo) {
            updateView(myBatteryInfo)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        if (activity != null) {
            val list = ArrayList<CommonDto>()
            adapter = CommonRecyclerViewAdapter(requireActivity(), list)
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver)
    }

    override fun getTitle(): Int {
        return R.string.screen_name_battery_info
    }

    private fun updateView(myBatteryInfo : MyBatteryInfo) {
        val list = adapter.items
        list.clear()

        try {
            val batteryLevelFloat = myBatteryInfo.level / myBatteryInfo.scale.toFloat()
            val batteryLevelInt = (batteryLevelFloat * 100).toInt()
            list.add(CommonDto("battery level",
                    batteryLevelInt.toString() + " %"))
        } catch (e: ArithmeticException) {
            println(e.message)
        }
        val batteryTemperature = myBatteryInfo.temperature / 10.0f
        list.add(CommonDto("battery temperature",
                batteryTemperature.toString() + " ℃"))
        list.add(CommonDto("health str", getHealthStr(myBatteryInfo.health)))

        list.add(CommonDto("health", myBatteryInfo.health.toString()))
        list.add(CommonDto("icon-small", myBatteryInfo.icon_small.toString()))
        list.add(CommonDto("level", myBatteryInfo.level.toString()))
        list.add(CommonDto("plugged str", getPluggedStr(myBatteryInfo.plugged)))
        list.add(CommonDto("plugged", myBatteryInfo.plugged.toString()))
        list.add(CommonDto("present str", getPresentStr(myBatteryInfo.present)))
        list.add(CommonDto("present", myBatteryInfo.present.toString()))
        list.add(CommonDto("scale", myBatteryInfo.scale.toString()))
        list.add(CommonDto("status str", getStatusStr(myBatteryInfo.status)))
        list.add(CommonDto("status", myBatteryInfo.status.toString()))
        list.add(CommonDto("technology", myBatteryInfo.technology))
        list.add(CommonDto("temperature", myBatteryInfo.temperature.toString()))
        list.add(CommonDto("voltage", myBatteryInfo.voltage.toString()))

        // for LOLLIPOP or more
        appendExtraInfo(list)

        adapter.notifyDataSetChanged()
    }

    private fun getHealthStr(health: Int):String {
        return when(health) {
            BatteryManager.BATTERY_HEALTH_UNKNOWN -> {
                "UNKNOWN"
            }
            BatteryManager.BATTERY_HEALTH_GOOD -> {
                "GOOD"
            }
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> {
                "OVER HEAT"
            }
            BatteryManager.BATTERY_HEALTH_DEAD -> {
                "DEAD"
            }
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> {
                "OVER VOLTAGE"
            }
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> {
                "UNSPECIFIED FAILURE"
            }
            BatteryManager.BATTERY_HEALTH_COLD -> {
                "COLD"
            }
            else -> {
                "UNKNOWN"
            }
        }
    }

    private fun getPluggedStr(plugged: Int):String {
        return when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> {
                "PLUGGED AC"
            }
            BatteryManager.BATTERY_PLUGGED_USB -> {
                "PLUGGED USB"
            }
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> {
                "PLUGGED WIRELESS"
            }
            else -> {
                "NOT PLUGGED"
            }
        }
    }

    private fun getPresentStr(present: Boolean):String {
        if (present) {
            return "PRESENT"
        }
        return "NO PRESENT"
    }

    private fun getStatusStr(status: Int):String {
        return when (status) {
            BatteryManager.BATTERY_STATUS_UNKNOWN -> {
                "UNKNOWN"
            }
            BatteryManager.BATTERY_STATUS_CHARGING -> {
                "CHARGING"
            }
            BatteryManager.BATTERY_STATUS_DISCHARGING -> {
                "DISCHARGING"
            }
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> {
                "NOT CHARGING"
            }
            BatteryManager.BATTERY_STATUS_FULL -> {
                "FULL"
            }
            else -> {
                "UNKNOWN"
            }
        }
    }

    private fun appendExtraInfo(list: ArrayList<CommonDto>) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            list.add(CommonDto("=== over LOLLIPOP ===", ""))

            val batteryManager = activity?.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

            val propertyCapacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            val propertyChargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)
            val propertyCurrentAverage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
            val propertyCurrentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
            val propertyEnergyCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)

            list.add(CommonDto("property capacity", propertyCapacity.toString()))
            list.add(CommonDto("property charge counter", propertyChargeCounter.toString()))
            list.add(CommonDto("property current average", propertyCurrentAverage.toString()))
            list.add(CommonDto("property current now", propertyCurrentNow.toString()))
            list.add(CommonDto("property energy counter", propertyEnergyCounter.toString()))
        }
    }

}