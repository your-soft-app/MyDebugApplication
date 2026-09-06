package jp.co.yuji.mydebugapplication.presentation.view.fragment.hard

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.presentation.view.adapter.SensorInfoRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Sensor Info Fragment.
 */
class SensorInfoFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return SensorInfoFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private val listener = object: SensorInfoRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(sensor: Sensor) {
            val fragment = SensorDetailFragment.newInstance(sensor.type)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }

    private var sensorManager: SensorManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        if (activity != null) {
            val adapter = SensorInfoRecyclerViewAdapter(requireActivity(), getSensorInfo())
            binding.recyclerView.adapter = adapter
            adapter.setOnItemClickListener(listener)
        }

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)
        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_sensor_info
    }

    private fun getSensorInfo() : List<Sensor> {
        val list = sensorManager?.getSensorList(Sensor.TYPE_ALL)
        if (list != null) {
            return list
        }
        return ArrayList()
    }
}