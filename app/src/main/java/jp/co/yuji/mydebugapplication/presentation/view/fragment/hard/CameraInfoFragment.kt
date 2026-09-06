package jp.co.yuji.mydebugapplication.presentation.view.fragment.hard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonProgressBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.presenter.hard.CameraInfoPresenter
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonSelectableRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Camera Info Fragment.
 */
class CameraInfoFragment : BaseFragment(R.layout.fragment_common_progress) {

    companion object {
        fun newInstance() : Fragment {
            return CameraInfoFragment()
        }
    }

    private lateinit var binding: FragmentCommonProgressBinding

    private val presenter = CameraInfoPresenter()

    private var adapter: CommonSelectableRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonProgressBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        val list = ArrayList<CommonDto>()
        if (activity != null) {
            adapter = CommonSelectableRecyclerViewAdapter(requireActivity(), list)
            binding.recyclerView.adapter = adapter
        }
        binding.progressBar?.visibility = View.VISIBLE

        addCameraInfo(list)

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_camera_info
    }

    private fun addCameraInfo(list : ArrayList<CommonDto>) {
        if (activity != null) {
            presenter.getCameraInfo(requireActivity(), object: CameraInfoPresenter.OnGetCameraInfoListener {
                override fun onGetCameraInfo(cameraList: List<CommonDto>) {
                    list.addAll(cameraList)
                    if (adapter != null) {
                        adapter?.notifyDataSetChanged()
                        binding.progressBar?.visibility = View.GONE
                    }
                }
            })
        }
    }

}