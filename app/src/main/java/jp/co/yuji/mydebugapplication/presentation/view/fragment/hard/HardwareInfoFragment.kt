package jp.co.yuji.mydebugapplication.presentation.view.fragment.hard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentHardInfoBinding
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonInfoRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Hardware Info Fragment.
 */
class HardwareInfoFragment : BaseFragment(R.layout.fragment_hard_info) {

    companion object {
        fun newInstance() : Fragment {
            return HardwareInfoFragment()
        }
    }
    private lateinit var binding: FragmentHardInfoBinding

    private val listener = object: CommonInfoRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(position: Int) {
            val type = Type.find(position)
            val fragment = when (type) {
                Type.SENSOR -> SensorInfoFragment.newInstance()
                Type.DISPLAY -> DisplayInfoFragment.newInstance()
                Type.CAMERA -> CameraInfoFragment.newInstance()
                Type.CPU -> CpuInfoFragment.newInstance()
                Type.MEMORY -> MemoryInfoFragment.newInstance()
                Type.BATTERY -> BatteryInfoFragment.newInstance()
                Type.STORAGE -> StorageInfoFragment.newInstance()
                Type.SOUND -> SoundInfoFragment.newInstance()
                Type.TELEPHONE -> TelephoneInfoFragment.newInstance()
                null -> SensorInfoFragment.newInstance()
            }
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            postLogEvent("hardware type: ${type?.title}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHardInfoBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = CommonInfoRecyclerViewAdapter(getHardwareInfo())
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(listener)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        // ad
        loadBannerAd(binding.adView, requireActivity(), getString(R.string.hardware_info_bottom_unit_id))

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_hardware_info
    }

    private fun getHardwareInfo() : List<String> {
        val list = ArrayList<String>()

        for (type in Type.entries) {
            list.add(type.position, type.title)
        }

        return list
    }

    enum class Type(val title: String, val position: Int)  {
        SENSOR("Sensor", 0),
        DISPLAY("Display", 1),
        CAMERA("Camera", 2),
        CPU("CPU", 3),
        MEMORY("Memory", 4),
        BATTERY("Battery", 5),
        STORAGE("Storage", 6),
        SOUND("Sound", 7),
        TELEPHONE("Telephone", 8);

        companion object {
            fun find(position: Int): Type? {
                return entries.firstOrNull { it.position == position }
            }
        }
    }
}