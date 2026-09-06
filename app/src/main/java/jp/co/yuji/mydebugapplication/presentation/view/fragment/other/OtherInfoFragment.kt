package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentOtherInfoBinding
import jp.co.yuji.mydebugapplication.presentation.view.activity.AboutActivity
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonInfoRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment


/**
 * Other Info Fragment.
 */
class OtherInfoFragment : BaseFragment(R.layout.fragment_other_info) {

    companion object {
        fun newInstance() : Fragment {
            return OtherInfoFragment()
        }
    }
    private lateinit var binding: FragmentOtherInfoBinding

    private val listener = object: CommonInfoRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(position: Int) {
            val type = Type.find(position)
            val fragment = when (type) {
                Type.SYSTEM_PROPERTIES -> SystemPropertiesFragment.newInstance()
                Type.LOG -> LogDetailFragment.newInstance()
                Type.WI_FI -> WiFiInfoListFragment.newInstance()
                Type.EXEC_SHELL -> ExecShellFragment.newInstance()
                Type.PINNING -> PinningFragment.newInstance()
                Type.NETWORK_INFO -> NetworkInfoFragment.newInstance()
                Type.ACTIVITY_MANAGER -> ActivityManagerFragment.newInstance()
                Type.ALARM_MANAGER -> AlarmManagerFragment.newInstance()
                Type.PORT -> PortListFragment.newInstance()
                Type.BROADCAST -> BroadcastInfoFragment.newInstance()
                null -> SystemPropertiesFragment.newInstance()
            }
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            postLogEvent("other type: ${type?.title}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentOtherInfoBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = CommonInfoRecyclerViewAdapter(getOtherInfo())
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(listener)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        // ad
        loadBannerAd(binding.adView, requireActivity(), getString(R.string.other_info_bottom_unit_id))

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu to use in the action bar
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_privacy_policy -> {
                startActivity("https://your-soft-app.github.io/privacy/", "privacy policy")
                return true
            }
            R.id.menu_license -> {
                startActivity("file:///android_asset/license.html", "license")
                return true
            }
            R.id.menu_version -> {
                startActivity("file:///android_asset/version.html", "version")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getTitle(): Int {
        return R.string.screen_name_other_info
    }

    private fun getOtherInfo() : List<String> {
        val list = ArrayList<String>()

        for (type in Type.values()) {
            list.add(type.position, type.title)
        }

        return list
    }

    private fun startActivity(url: String, contentType: String) {
        postLogEvent(contentType)
        if (activity != null) {
            AboutActivity.startActivity(requireActivity(), url)
        }
    }

    enum class Type(val title: String, val position: Int)  {
        SYSTEM_PROPERTIES("System Properties", 0),
        LOG("Log", 1),
        WI_FI("Wi-Fi", 2),
        EXEC_SHELL("Exec Shell", 3),
        PINNING("Pinning", 4),
        NETWORK_INFO("Network Info", 5),
        ACTIVITY_MANAGER("Activity Manager", 6),
        ALARM_MANAGER("Alarm Manager", 7),
        PORT("Port", 8),
        BROADCAST("Broadcast", 9);

        companion object {
            fun find(position: Int): Type? {
                return entries.firstOrNull { it.position == position }
            }
        }
    }
}