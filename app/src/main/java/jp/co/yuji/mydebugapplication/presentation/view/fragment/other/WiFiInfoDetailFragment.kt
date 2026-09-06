package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.presenter.other.WiFiInfoDetailPresenter
import jp.co.yuji.mydebugapplication.presentation.view.adapter.WiFiInfoDetailRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * WiFi Info Detail Fragment.
 */
class WiFiInfoDetailFragment : BaseFragment(R.layout.fragment_common) {

    companion object {

        const val ARG_KEY = "arg_key"

        fun newInstance(scanResult : ScanResult) : Fragment {
            val fragment = WiFiInfoDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_KEY, scanResult)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private val presenter = WiFiInfoDetailPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val scanResult = arguments?.getParcelable<ScanResult>(ARG_KEY)

        if (activity != null && scanResult != null) {
            val adapter = WiFiInfoDetailRecyclerViewAdapter(requireActivity(), getWiFiInfoDetail(requireActivity(), scanResult))
            binding.recyclerView.adapter = adapter
        }

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_wifi_info_detail
    }

    private fun getWiFiInfoDetail(context: Context, scanResult: ScanResult): List<CommonDto> {
        return presenter.getWiFiInfoDetail(context, scanResult)
    }

}