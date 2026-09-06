package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentActivityPinningBinding
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment
import jp.co.yuji.mydebugapplication.presentation.view.receiver.MyDeviceAdminReceiver

/**
 * Pinning Activity Fragment.
 */
class PinningActivityFragment : BaseFragment(R.layout.fragment_activity_pinning) {

    companion object {

        const val ARG_KEY = "arg_key"

        fun newInstance(type : Int) : Fragment {
            val fragment = PinningActivityFragment()
            val bundle = Bundle()
            bundle.putInt(PinningActivityFragment.ARG_KEY, type)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var binding: FragmentActivityPinningBinding

    enum class PinningType(val type: Int, val strResId: Int)  {
        PINNING(1, R.string.screen_name_pinning),
        LOCK_TASK(2, R.string.screen_name_lock_task)
    }

    private var devicePolicyManager : DevicePolicyManager? = null
    private var deviceAdmin : ComponentName? = null
    private var activityManager : ActivityManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentActivityPinningBinding.inflate(layoutInflater)
        val type = arguments?.getInt(ARG_KEY)
        val pinningType = PinningType.values().first { type == it.type }
        setTitleLazy(pinningType.strResId)

        devicePolicyManager = activity?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        deviceAdmin = ComponentName(requireActivity(), MyDeviceAdminReceiver::class.java)

        activityManager = activity?.getSystemService(
                Context.ACTIVITY_SERVICE) as ActivityManager

        when (pinningType) {
            PinningType.PINNING -> {
                startPinning()
                binding.finishPinningActivityButton.setText(R.string.finish_pinning_activity)
            }
            PinningType.LOCK_TASK -> {
                startLockTask()
                binding.finishPinningActivityButton.setText(R.string.finish_lock_task_activity)
            }
        }
        binding.finishPinningActivityButton.setOnClickListener { stopPinning() }

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_pinning
    }

    private fun startPinning() {
        executeForApiLevel21orHigher {
            println(activityManager?.lockTaskModeState)
            if (activityManager?.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE) {
                activity?.startLockTask()
            }
        }
    }

    private fun startLockTask() {
        if (devicePolicyManager != null && devicePolicyManager!!.isDeviceOwnerApp(activity?.packageName) && deviceAdmin != null) {
            devicePolicyManager?.setLockTaskPackages(deviceAdmin!!, arrayOf(activity?.packageName))
            startPinning()
        }
    }

    private fun stopPinning() {
        executeForApiLevel21orHigher {
            if (activityManager?.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE) {
                activity?.stopLockTask()
            }
        }
        activity?.finish()
    }

    private fun executeForApiLevel21orHigher(execute: () -> Unit) {
        execute()
    }
}