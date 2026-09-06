package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ConfigurationInfo
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
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonDetailRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

class ActivityManagerFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return ActivityManagerFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        if (activity != null) {
            val adapter = CommonDetailRecyclerViewAdapter(requireActivity(), getActivityManagerInfo())
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_activity_manager
    }

    private fun getActivityManagerInfo() : ArrayList<CommonDto> {
        val list = ArrayList<CommonDto>()

        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val appTasks = activityManager.appTasks
            for (appTask in appTasks) {
                list.add(CommonDto(" === TaskInfo ===", ""))
                val taskInfo = appTask.taskInfo
                list.add(CommonDto("affiliatedTaskId", taskInfo.affiliatedTaskId.toString()))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    list.add(CommonDto("baseActivity", taskInfo.baseActivity?.toString().orEmpty()))
                }

                list.add(CommonDto("baseIntent", taskInfo.baseIntent.toString().orEmpty()))
                list.add(CommonDto("description", taskInfo.description?.toString().orEmpty()))
                list.add(CommonDto("id", taskInfo.id.toString()))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    list.add(CommonDto("numActivities", taskInfo.numActivities.toString()))
                }
                list.add(CommonDto("origActivity", taskInfo.origActivity?.toString().orEmpty()))
                list.add(CommonDto("persistentId", taskInfo.persistentId.toString()))
                list.add(CommonDto("taskDescription label", taskInfo.taskDescription?.label?.toString().orEmpty()))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    list.add(CommonDto("topActivity", taskInfo.topActivity?.toString().orEmpty()))
                }
            }
            val size = activityManager.appTaskThumbnailSize
            list.add(CommonDto("AppTaskThumbnailSize", size.toString()))
        }

        val processErrorStateInfo = activityManager.processesInErrorState
        if (processErrorStateInfo != null) {
            for (info in processErrorStateInfo) {
                list.add(CommonDto(" === ProcessErrorStateInfo ===", ""))
                list.add(CommonDto("condition", info.condition.toString()))
                list.add(CommonDto("condition str", getConditionStr(info.condition)))
                list.add(CommonDto("crashData", info.crashData?.toString().orEmpty()))
                list.add(CommonDto("longMsg", info.longMsg?.toString().orEmpty()))
                list.add(CommonDto("pid", info.pid.toString()))
                list.add(CommonDto("shortMsg", info.shortMsg?.toString().orEmpty()))
                list.add(CommonDto("stackTrace", info.stackTrace?.toString().orEmpty()))
                list.add(CommonDto("tag", info.tag?.toString().orEmpty()))
                list.add(CommonDto("uid", info.uid.toString()))
            }
        }
        val memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        list.add(CommonDto(" === MemoryInfo ===", ""))
        list.add(CommonDto("availMem", memoryInfo.availMem.toString()))
        list.add(CommonDto("lowMemory", memoryInfo.lowMemory.toString()))
        list.add(CommonDto("threshold", memoryInfo.threshold.toString()))
        list.add(CommonDto("totalMem", memoryInfo.totalMem.toString()))

        val runningAppProcessInfo = activityManager.runningAppProcesses
        if (runningAppProcessInfo != null) {
            for (info in runningAppProcessInfo) {
                list.add(CommonDto(" === RunningAppProcessInfo ===", ""))
                list.add(CommonDto("importance", info.importance.toString()))
                list.add(CommonDto("importance str", getImportanceStr(info.importance)))
                list.add(CommonDto("importanceReasonCode", info.importanceReasonCode.toString()))
                list.add(CommonDto("importanceReasonCode", getImportanceReasonStr(info.importanceReasonCode)))
                list.add(CommonDto("importanceReasonComponent", info.importanceReasonComponent?.toString().orEmpty()))
                list.add(CommonDto("importanceReasonPid", info.importanceReasonPid.toString()))
                list.add(CommonDto("lastTrimLevel", info.lastTrimLevel.toString()))
                list.add(CommonDto("lru", info.lru.toString()))
                list.add(CommonDto("pid", info.pid.toString()))
                list.add(CommonDto("pkgList", info.pkgList?.toString().orEmpty()))
                list.add(CommonDto("processName", info.processName?.toString().orEmpty()))
                list.add(CommonDto("uid", info.uid.toString()))
            }
        }
        val configurationInfo = activityManager.deviceConfigurationInfo
        if (configurationInfo != null) {
            list.add(CommonDto(" === ConfigurationInfo ===", ""))
            list.add(CommonDto("reqGlEsVersion", configurationInfo.reqGlEsVersion.toString()))
            list.add(CommonDto("reqInputFeatures str", getReqInputFeaturesStr(configurationInfo.reqInputFeatures)))
            list.add(CommonDto("reqInputFeatures", configurationInfo.reqInputFeatures.toString()))
            list.add(CommonDto("reqKeyboardType", configurationInfo.reqKeyboardType.toString()))
            list.add(CommonDto("reqNavigation", configurationInfo.reqNavigation.toString()))
            list.add(CommonDto("reqTouchScreen", configurationInfo.reqTouchScreen.toString()))
        }

        val launcherLargeIconDensity = activityManager.launcherLargeIconDensity
        list.add(CommonDto("launcherLargeIconDensity", launcherLargeIconDensity.toString()))
        val launcherLargeIconSize = activityManager.launcherLargeIconSize
        list.add(CommonDto("launcherLargeIconSize", launcherLargeIconSize.toString()))
        val isUserAMonkey = ActivityManager.isUserAMonkey()
        list.add(CommonDto("isUserAMonkey", isUserAMonkey.toString()))
        val isRunningInTestHarness = ActivityManager.isRunningInTestHarness()
        list.add(CommonDto("isRunningInTestHarness", isRunningInTestHarness.toString()))

        return list
    }

    private fun getConditionStr(num: Int): String {
        return when (num) {
            ActivityManager.ProcessErrorStateInfo.CRASHED -> "CRASHED"
            ActivityManager.ProcessErrorStateInfo.NOT_RESPONDING -> "NOT_RESPONDING"
            ActivityManager.ProcessErrorStateInfo.NO_ERROR -> "NO_ERROR"
            else -> "unknown"
        }
    }

    private fun getImportanceStr(num: Int): String {
        return when (num) {
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED -> "IMPORTANCE_CACHED"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND -> "IMPORTANCE_FOREGROUND"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE -> "IMPORTANCE_FOREGROUND_SERVICE"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE -> "IMPORTANCE_GONE"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE -> "IMPORTANCE_PERCEPTIBLE"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE_PRE_26 -> "IMPORTANCE_PERCEPTIBLE_PRE_26"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE -> "IMPORTANCE_SERVICE"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_TOP_SLEEPING -> "IMPORTANCE_TOP_SLEEPING"
            ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE -> "IMPORTANCE_VISIBLE"
            else -> "unknown"
        }
    }

    private fun getImportanceReasonStr(num: Int): String {
        return when (num) {
            ActivityManager.RunningAppProcessInfo.REASON_PROVIDER_IN_USE -> "REASON_PROVIDER_IN_USE"
            ActivityManager.RunningAppProcessInfo.REASON_SERVICE_IN_USE -> "REASON_SERVICE_IN_USE"
            ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN -> "REASON_UNKNOWN"
            else -> "unknown"
        }
    }

    private fun getReqInputFeaturesStr(num: Int): String {
        return when (num) {
            ConfigurationInfo.INPUT_FEATURE_FIVE_WAY_NAV -> "INPUT_FEATURE_FIVE_WAY_NAV"
            ConfigurationInfo.INPUT_FEATURE_HARD_KEYBOARD -> "ConfigurationInfo"
            else -> "unknown"
        }
    }

}