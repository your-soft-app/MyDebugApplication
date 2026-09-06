package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentPinningBinding
import jp.co.yuji.mydebugapplication.presentation.view.activity.PinningActivity
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment
import jp.co.yuji.mydebugapplication.presentation.view.receiver.MyDeviceAdminReceiver

/**
 * Pinning Fragment.
 */
class PinningFragment : BaseFragment(R.layout.fragment_pinning) {

    companion object {
        fun newInstance() : Fragment {
            return PinningFragment()
        }
    }
    private lateinit var binding: FragmentPinningBinding

    private var devicePolicyManager : DevicePolicyManager? = null

    private var deviceAdmin : ComponentName? = null

    private var activityManager : ActivityManager? = null

    private var clearDeviceOwnerButton : Button? = null
    private var startLockTaskButton : Button? = null
    private var stopLockTaskButton : Button? = null
    private var startLockTaskActivityButton : Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPinningBinding.inflate(layoutInflater)

        devicePolicyManager = activity?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        deviceAdmin = ComponentName(requireActivity(), MyDeviceAdminReceiver::class.java)

        activityManager = activity?.getSystemService(
                Context.ACTIVITY_SERVICE) as ActivityManager

        binding.startPinningButton.setOnClickListener { startPinning() }
        binding.stopPinningButton.setOnClickListener { stopPinning() }
        binding.startPinningActivityButton.setOnClickListener { startPinningActivity() }

//        setDeviceOwnerView(view)
//        setDeviceOwnerStatus(view.isDeviceOwnerTextView)

//        if (isDeviceOwner()) {
//            view.clearDeviceOwnerButton?.setOnClickListener { clearDeviceOwner() }
//            view.startLockTaskButton?.setOnClickListener { startLockTask() }
//            view.stopLockTaskButton?.setOnClickListener { stopLockTask() }
//            view.startLockTaskActivityButton?.setOnClickListener { startLockTaskActivity() }
//        } else {
//            disableButton()
//        }

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

    private fun stopPinning() {
        executeForApiLevel21orHigher {
            if (activityManager?.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE) {
                activity?.stopLockTask()
            }
        }
    }

    private fun startPinningActivity() {
        if (activity != null) {
            PinningActivity.startActivity(requireActivity(), PinningActivityFragment.PinningType.PINNING)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun clearDeviceOwner() {
        executeForApiLevel21orHigher {
            if (isDeviceOwner()) {
                devicePolicyManager?.clearDeviceOwnerApp(activity?.packageName)
                disableButton()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startLockTask() {
        if (devicePolicyManager != null && devicePolicyManager!!.isDeviceOwnerApp(activity?.packageName) && deviceAdmin != null) {
            devicePolicyManager?.setLockTaskPackages(deviceAdmin!!, arrayOf(activity?.packageName))
            startPinning()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun stopLockTask() {
        stopPinning()
    }

    private fun startLockTaskActivity() {
        if (activity != null) {
            PinningActivity.startActivity(requireActivity(), PinningActivityFragment.PinningType.LOCK_TASK)
        }
    }

    private fun executeForApiLevel21orHigher(execute: () -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(activity, R.string.error_support_api_level_21, Toast.LENGTH_LONG).show()
            return
        }
        execute()
    }

//    private fun setDeviceOwnerStatus(@NonNull view: TextView) {
//        val stringResId = if (isDeviceOwner()) R.string.is_device_owner_status_true else R.string.is_device_owner_status_false
//        view.setText(stringResId)
//    }

    private fun isDeviceOwner() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && devicePolicyManager != null
                && devicePolicyManager!!.isDeviceOwnerApp(activity?.packageName)
    }

//    private fun setDeviceOwnerView(@NonNull view: View) {
//        clearDeviceOwnerButton = view.clearDeviceOwnerButton
//        startLockTaskButton = view.startLockTaskButton
//        stopLockTaskButton = view.stopLockTaskButton
//        startLockTaskActivityButton = view.startLockTaskActivityButton
//    }

    private fun disableButton() {
        clearDeviceOwnerButton?.isEnabled = false
        startLockTaskButton?.isEnabled = false
        stopLockTaskButton?.isEnabled = false
        startLockTaskActivityButton?.isEnabled = false
    }

}