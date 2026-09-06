package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonDetailRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment
import jp.co.yuji.mydebugapplication.presentation.view.receiver.MyBroadcastReceiver

class BroadcastInfoFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return BroadcastInfoFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private lateinit var adapter: CommonDetailRecyclerViewAdapter

    private val receiver = MyBroadcastReceiver(object: MyBroadcastReceiver.OnReceiveListener {
        override fun onReceive(intent: Intent) {
            println("Action: ${intent.action}")
            println("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}")

            val action = intent.action ?: "action is null"
            updateView(action, intent.toUri(Intent.URI_INTENT_SCHEME))
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        if (activity != null) {
            val list = ArrayList<CommonDto>()
            adapter = CommonDetailRecyclerViewAdapter(requireActivity(), list)
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        filter.addAction(Intent.ACTION_BATTERY_LOW)
        filter.addAction(Intent.ACTION_BATTERY_OKAY)
        filter.addAction(Intent.ACTION_BOOT_COMPLETED)
        filter.addAction(Intent.ACTION_CAMERA_BUTTON)
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED)
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        filter.addAction(Intent.ACTION_DEVICE_STORAGE_LOW)
        filter.addAction(Intent.ACTION_DEVICE_STORAGE_OK)
        filter.addAction(Intent.ACTION_GTALK_SERVICE_CONNECTED)
        filter.addAction(Intent.ACTION_GTALK_SERVICE_DISCONNECTED)
        filter.addAction(Intent.ACTION_HEADSET_PLUG)
        filter.addAction(Intent.ACTION_INPUT_METHOD_CHANGED)
        filter.addAction(Intent.ACTION_MANAGE_PACKAGE_STORAGE)
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL)
        filter.addAction(Intent.ACTION_MEDIA_BUTTON)
        filter.addAction(Intent.ACTION_MEDIA_CHECKING)
        filter.addAction(Intent.ACTION_MEDIA_EJECT)
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        filter.addAction(Intent.ACTION_MEDIA_NOFS)
        filter.addAction(Intent.ACTION_MEDIA_REMOVED)
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED)
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED)
        filter.addAction(Intent.ACTION_MEDIA_SHARED)
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE)
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED)
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
        filter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED)
        filter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        filter.addAction(Intent.ACTION_PACKAGE_RESTARTED)
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        filter.addAction(Intent.ACTION_REBOOT)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SHUTDOWN)
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        filter.addAction(Intent.ACTION_TIME_TICK)
        filter.addAction(Intent.ACTION_UID_REMOVED)
        filter.addAction(Intent.ACTION_UMS_CONNECTED)
        filter.addAction(Intent.ACTION_UMS_DISCONNECTED)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        filter.addAction(Intent.ACTION_WALLPAPER_CHANGED)
        ContextCompat.registerReceiver(
            requireActivity(),
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(receiver)
    }

    override fun getTitle(): Int {
        return R.string.screen_name_broadcast_info
    }

    private fun updateView(action : String, uri: String) {
        val list = adapter.items
        list.add(CommonDto(action, uri))

        adapter.notifyDataSetChanged()
    }
}