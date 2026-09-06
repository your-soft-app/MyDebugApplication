package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonProgressBinding
import jp.co.yuji.mydebugapplication.presentation.presenter.other.WiFiInfoListPresenter
import jp.co.yuji.mydebugapplication.presentation.view.adapter.WiFiInfoListRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * WiFi Info List Fragment.
 */
class WiFiInfoListFragment : BaseFragment(R.layout.fragment_common_progress) {

    companion object {
        const val PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 0
        fun newInstance() : Fragment {
            return WiFiInfoListFragment()
        }
    }
    private lateinit var binding: FragmentCommonProgressBinding
    private val presenter = WiFiInfoListPresenter()
    private var adapter: WiFiInfoListRecyclerViewAdapter? = null

    private val listener = object: WiFiInfoListRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(scanResult: ScanResult) {
            val fragment = WiFiInfoDetailFragment.newInstance(scanResult)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonProgressBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val list = ArrayList<ScanResult>()
        adapter = WiFiInfoListRecyclerViewAdapter(list)
        binding.recyclerView.adapter = adapter
        adapter?.setOnItemClickListener(listener)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            addWiFiList(list)
        } else {
            val permissions: Array<String> = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE)
            requestPermissions(
                permissions,
                PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION)
        }
        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_wifi_info_list
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addWiFiList(adapter?.getItems())
        } else {
            binding.progressBar?.visibility = View.GONE
        }
    }

    private fun addWiFiList(list : ArrayList<ScanResult>?) {
        presenter.getWiFiInfoList(requireActivity(), object: WiFiInfoListPresenter.OnGetWiFiInfoListListener {
            override fun onGetWiFiInfoList(wifiInfoList: List<ScanResult>) {
                if (wifiInfoList.isEmpty()) {
                    binding.emptyView?.setText(R.string.wifi_info_no_results_text)
                    binding.emptyView?.visibility = View.VISIBLE
                } else {
                    list?.addAll(wifiInfoList)
                    adapter?.notifyDataSetChanged()
                    binding.emptyView?.visibility = View.GONE
                }
                binding.progressBar?.visibility = View.GONE
            }
        })
    }

}