package jp.co.yuji.mydebugapplication.presentation.view.fragment.hard

import android.Manifest
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonSelectableRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment


class TelephoneInfoFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        const val PERMISSIONS_REQUEST_CODE_READ_PHONE_STATE = 0
        fun newInstance() : Fragment {
            return TelephoneInfoFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private lateinit var adapter: CommonSelectableRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val list = ArrayList<CommonDto>()

        if (activity != null) {
            adapter = CommonSelectableRecyclerViewAdapter(requireActivity(), list)
            binding.recyclerView.adapter = adapter
        }

        addTelephoneInfo(list)
        addRequiredPermissionTelephoneInfo(list)
        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_telephone_info
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE_READ_PHONE_STATE
                && grantResults.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults.size > 3
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    addRequiredPermissionTelephoneInfo(adapter.getItems())
                }
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults.size > 1
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    addRequiredPermissionTelephoneInfo(adapter.getItems())
                }
            }

            adapter.notifyDataSetChanged()
        }

    }

    private fun addTelephoneInfo(list: ArrayList<CommonDto>) {

        val telephonyManager = activity?.getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        val phoneCount = telephonyManager.phoneCount
        list.add(CommonDto("phoneCount", phoneCount.toString()))

        val phoneType = telephonyManager.phoneType
        list.add(CommonDto("phoneType", phoneType.toString()))

        val networkOperatorName = telephonyManager.networkOperatorName
        if (networkOperatorName != null) {
            list.add(CommonDto("networkOperatorName", networkOperatorName))
        }

        val networkOperator = telephonyManager.networkOperator
        if (networkOperator != null) {
            list.add(CommonDto("networkOperator", networkOperator))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val networkSpecifier = telephonyManager.networkSpecifier
            if (networkSpecifier != null) {
                list.add(CommonDto("networkSpecifier", networkSpecifier))
            }
        }

        val isNetworkRoaming = telephonyManager.isNetworkRoaming
        val isNetworkRoamingString = if (isNetworkRoaming) "true" else "false"
        list.add(CommonDto("isNetworkRoaming", isNetworkRoamingString))

        val networkCountryIso = telephonyManager.networkCountryIso
        if (networkCountryIso != null) {
            list.add(CommonDto("networkCountryIso", networkCountryIso))
        }

        val hasIccCard = telephonyManager.hasIccCard()
        val hasIccCardString = if (hasIccCard) "true" else "false"
        list.add(CommonDto("hasIccCard", hasIccCardString))

        val simState = telephonyManager.simState
        list.add(CommonDto("simState", simState.toString()))

        val simOperator = telephonyManager.simOperator
        if (simOperator != null) {
            list.add(CommonDto("simOperator", simOperator))
        }

        val simOperatorName = telephonyManager.simOperatorName
        if (simOperatorName != null) {
            list.add(CommonDto("simOperatorName", simOperatorName))
        }

        val simCountryIso = telephonyManager.simCountryIso
        if (simCountryIso != null) {
            list.add(CommonDto("simCountryIso", simCountryIso))
        }

        val callState = telephonyManager.callState
        list.add(CommonDto("callState", callState.toString()))

        val dataActivity = telephonyManager.dataActivity
        list.add(CommonDto("dataActivity", dataActivity.toString()))

        val dataState = telephonyManager.dataState
        list.add(CommonDto("dataState", dataState.toString()))

        val isVoiceCapable = telephonyManager.isVoiceCapable
        val isVoiceCapableString = if (isVoiceCapable) "true" else "false"
        list.add(CommonDto("isVoiceCapable", isVoiceCapableString))

        val isSmsCapable = telephonyManager.isSmsCapable
        val isSmsCapableString = if (isSmsCapable) "true" else "false"
        list.add(CommonDto("isSmsCapable", isSmsCapableString))

        val mmsUserAgent = telephonyManager.mmsUserAgent
        if (mmsUserAgent != null) {
            list.add(CommonDto("mmsUserAgent", mmsUserAgent))
        }

        val mmsUAProfUrl = telephonyManager.mmsUAProfUrl
        if (mmsUAProfUrl != null) {
            list.add(CommonDto("mmsUAProfUrl", mmsUAProfUrl))
        }

        val hasCarrierPrivileges = telephonyManager.hasCarrierPrivileges()
        val hasCarrierPrivilegesString = if (hasCarrierPrivileges) "true" else "false"
        list.add(CommonDto("hasCarrierPrivileges", hasCarrierPrivilegesString))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val isConcurrentVoiceAndDataSupported = telephonyManager.isConcurrentVoiceAndDataSupported
            val isConcurrentVoiceAndDataSupportedString = if (isConcurrentVoiceAndDataSupported) "true" else "false"
            list.add(CommonDto("isConcurrentVoiceAndDataSupported", isConcurrentVoiceAndDataSupportedString))

            val isDataEnabled = telephonyManager.isDataEnabled
            val isDataEnabledString = if (isDataEnabled) "true" else "false"
            list.add(CommonDto("isDataEnabled", isDataEnabledString))
        }

    }

    private fun addRequiredPermissionTelephoneInfo(list: ArrayList<CommonDto>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_PHONE_NUMBERS)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions: Array<String> = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_COARSE_LOCATION)
                requestPermissions(
                        permissions,
                        PERMISSIONS_REQUEST_CODE_READ_PHONE_STATE)
                return
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions: Array<String> = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION)
                requestPermissions(
                        permissions,
                        PERMISSIONS_REQUEST_CODE_READ_PHONE_STATE)
                return
            }
        }

        val telephonyManager = activity?.getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val deviceId = telephonyManager.deviceId
            if (deviceId != null) {
                list.add(CommonDto("deviceId", deviceId))
            }
        } else {
            list.add(CommonDto("deviceId", "for only apps preloaded on this device"))
        }

        val deviceSoftwareVersion = telephonyManager.deviceSoftwareVersion
        if (deviceSoftwareVersion != null) {
            list.add(CommonDto("deviceSoftwareVersion", deviceSoftwareVersion))
        }

        val networkType = telephonyManager.networkType
        list.add(CommonDto("networkType", networkType.toString()))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val imei = telephonyManager.imei
                if (imei != null) {
                    list.add(CommonDto("imei", imei))
                }
            } else {
                list.add(CommonDto("imei", "for only apps preloaded on this device"))
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val meid = telephonyManager.meid
                if (meid != null) {
                    list.add(CommonDto("meid", meid))
                }
            } else {
                list.add(CommonDto("meid", "for only apps preloaded on this device"))
            }

            val carrierConfig = telephonyManager.carrierConfig
            if (carrierConfig != null) {
                list.add(CommonDto("carrierConfig", carrierConfig.toString()))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val dataNetworkType = telephonyManager.dataNetworkType
            list.add(CommonDto("dataNetworkType", dataNetworkType.toString()))

            val voiceNetworkType = telephonyManager.voiceNetworkType
            list.add(CommonDto("voiceNetworkType", voiceNetworkType.toString()))
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val simSerialNumber = telephonyManager.simSerialNumber
            if (simSerialNumber != null) {
                list.add(CommonDto("simSerialNumber", simSerialNumber))
            }
        } else {
            list.add(CommonDto("simSerialNumber", "for only apps preloaded on this device"))
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val subscriberId = telephonyManager.subscriberId
            if (subscriberId != null) {
                list.add(CommonDto("subscriberId", subscriberId))
            }
        } else {
            list.add(CommonDto("subscriberId", "for only apps preloaded on this device"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val groupIdLevel1 = telephonyManager.groupIdLevel1
            if (groupIdLevel1 != null) {
                list.add(CommonDto("groupIdLevel1", groupIdLevel1))
            }
        }

        val line1Number = telephonyManager.line1Number
        if (line1Number != null) {
            list.add(CommonDto("line1Number", line1Number))
        }

        val voiceMailNumber = telephonyManager.voiceMailNumber
        if (voiceMailNumber != null) {
            list.add(CommonDto("voiceMailNumber", voiceMailNumber))
        }

        val voiceMailAlphaTag = telephonyManager.voiceMailAlphaTag
        if (voiceMailAlphaTag != null) {
            list.add(CommonDto("voiceMailAlphaTag", voiceMailAlphaTag))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val allCellInfo = telephonyManager.allCellInfo
                if (allCellInfo != null && allCellInfo.size > 0) {
                    list.add(CommonDto("=== CellInfo ===", ""))
                    for ((num, cellInfo) in allCellInfo.withIndex()) {
                        list.add(CommonDto(num.toString(), ""))
                        val isRegistered = cellInfo.isRegistered
                        val isRegisteredString = if (isRegistered) "true" else "false"
                        list.add(CommonDto("isRegistered", isRegisteredString))

                        val timeStamp = cellInfo.timeStamp
                        list.add(CommonDto("timeStamp", timeStamp.toString()))
                    }
                }
            } else {
                list.add(CommonDto("=== CellInfo ===", ""))
                list.add(CommonDto("", "Not allowed to access cell info"))
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val canChangeDtmfToneLength = telephonyManager.canChangeDtmfToneLength()
            val canChangeDtmfToneLengthString = if (canChangeDtmfToneLength) "true" else "false"
            list.add(CommonDto("canChangeDtmfToneLength", canChangeDtmfToneLengthString))

            val isWorldPhone = telephonyManager.isWorldPhone
            val isWorldPhoneString = if (isWorldPhone) "true" else "false"
            list.add(CommonDto("isWorldPhone", isWorldPhoneString))

            val isTtyModeSupported = telephonyManager.isTtyModeSupported
            val isTtyModeSupportedString = if (isTtyModeSupported) "true" else "false"
            list.add(CommonDto("isTtyModeSupported", isTtyModeSupportedString))

            val isHearingAidCompatibilitySupported = telephonyManager.isHearingAidCompatibilitySupported
            val isHearingAidCompatibilitySupportedString = if (isHearingAidCompatibilitySupported) "true" else "false"
            list.add(CommonDto("isHearingAidCompatibilitySupported", isHearingAidCompatibilitySupportedString))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val forbiddenPlmns = telephonyManager.forbiddenPlmns
            if (forbiddenPlmns != null && forbiddenPlmns.isNotEmpty()) {
                list.add(CommonDto("=== forbiddenPlmns ===", ""))
                for (forbiddenPlmn in forbiddenPlmns) {
                    list.add(CommonDto("", forbiddenPlmn))
                }
            }

            val serviceState = telephonyManager.serviceState
            if (serviceState != null) {
                list.add(CommonDto("serviceState", serviceState.toString()))
            }
        }

    }
}