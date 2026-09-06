package jp.co.yuji.mydebugapplication.presentation.view.fragment.app

import android.os.Build
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
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonDetailRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Application Detail Fragment.
 */
class ApplicationDetailFragment : BaseFragment(R.layout.fragment_common) {

    companion object {

        const val ARG_KEY = "arg_key"

        fun newInstance(packageName : String) : Fragment {
            val fragment = ApplicationDetailFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KEY, packageName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentCommonBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val packageName = arguments?.getString(ARG_KEY)

        if (activity != null && packageName != null) {
            val adapter = CommonDetailRecyclerViewAdapter(requireActivity(), getApplicationDetail(packageName))
            binding.recyclerView.adapter = adapter
        }

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_application_detail
    }

    private fun getApplicationDetail(packageName: String): ArrayList<CommonDto> {
        val list = ArrayList<CommonDto>()

        try {
            val applicationInfo = activity?.packageManager?.getApplicationInfo(packageName, 0)
            if (applicationInfo != null) {
                // field
                list.add(CommonDto("taskAffinity", applicationInfo.taskAffinity?.toString().orEmpty()))
                list.add(CommonDto("permission", applicationInfo.permission?.toString().orEmpty()))
                list.add(CommonDto("processName", applicationInfo.processName?.toString().orEmpty()))
                list.add(CommonDto("className", applicationInfo.className?.toString().orEmpty()))
                if (applicationInfo.descriptionRes > 0) {
                    list.add(CommonDto("descriptionRes", requireActivity().getString(applicationInfo.descriptionRes)))
                } else {
                    list.add(CommonDto("descriptionRes", "no resource"))
                }
                list.add(CommonDto("theme", applicationInfo.theme.toString()))
                list.add(CommonDto("manageSpaceActivityName", applicationInfo.manageSpaceActivityName?.toString().orEmpty()))
                list.add(CommonDto("backupAgentName", applicationInfo.backupAgentName?.toString().orEmpty()))
                list.add(CommonDto("uiOptions", applicationInfo.uiOptions.toString()))
                list.add(CommonDto("flags", applicationInfo.flags.toString()))
                list.add(CommonDto("requiresSmallestWidthDp", applicationInfo.requiresSmallestWidthDp.toString()))
                list.add(CommonDto("compatibleWidthLimitDp", applicationInfo.compatibleWidthLimitDp.toString()))
                list.add(CommonDto("largestWidthLimitDp", applicationInfo.largestWidthLimitDp.toString()))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    list.add(CommonDto("storageUuid", applicationInfo.storageUuid?.toString().orEmpty()))
                }
                list.add(CommonDto("sourceDir", applicationInfo.sourceDir?.toString().orEmpty()))
                list.add(CommonDto("publicSourceDir", applicationInfo.publicSourceDir?.toString().orEmpty()))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    list.add(CommonDto("splitNames", applicationInfo.splitNames?.toString().orEmpty()))
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    list.add(CommonDto("splitSourceDirs", applicationInfo.splitSourceDirs?.toString().orEmpty()))
                    list.add(CommonDto("splitPublicSourceDirs", applicationInfo.splitPublicSourceDirs?.toString().orEmpty()))
                }
                list.add(CommonDto("sharedLibraryFiles", applicationInfo.sharedLibraryFiles?.toString().orEmpty()))
                list.add(CommonDto("dataDir", applicationInfo.dataDir?.toString().orEmpty()))
                list.add(CommonDto("deviceProtectedDataDir", applicationInfo.deviceProtectedDataDir?.toString().orEmpty()))
                list.add(CommonDto("nativeLibraryDir", applicationInfo.nativeLibraryDir?.toString().orEmpty()))
                list.add(CommonDto("uid", applicationInfo.uid.toString()))
                list.add(CommonDto("minSdkVersion", applicationInfo.minSdkVersion.toString()))
                list.add(CommonDto("targetSdkVersion", applicationInfo.targetSdkVersion.toString()))
                list.add(CommonDto("enabled", applicationInfo.enabled.toString()))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    list.add(CommonDto("category", applicationInfo.category.toString()))
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }

        return list
    }

}