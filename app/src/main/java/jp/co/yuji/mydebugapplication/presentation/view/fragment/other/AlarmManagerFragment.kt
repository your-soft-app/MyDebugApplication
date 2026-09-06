package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmManagerFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return AlarmManagerFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        if (activity != null) {
            val adapter = CommonRecyclerViewAdapter(requireActivity(), getAlarmManagerInfo(requireActivity()))
            binding.recyclerView.adapter = adapter

            if (adapter.items.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyView.text = getString(R.string.alarm_manager_no_data_text)
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_alarm_manager
    }

    private fun getAlarmManagerInfo(activity: FragmentActivity): ArrayList<CommonDto> {
        val list = ArrayList<CommonDto>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            val alarmManager = activity.getSystemService(Context.ALARM_SERVICE)
            if (alarmManager is AlarmManager) {
                val alarmClockInfo = alarmManager.nextAlarmClock
                if (alarmClockInfo != null) {
                    list.add(CommonDto("=== Next Alarm Clock ===", ""))
                    list.add(CommonDto("TriggerTime", convertTimeToString(alarmClockInfo.triggerTime)))
                    if (alarmClockInfo.showIntent != null) {
                        list.add(CommonDto("PendingIntent", alarmClockInfo.showIntent.toString()))
                    }
                    list.add(CommonDto("describeContents", alarmClockInfo.describeContents().toString()))
                }
            }
        }

        return list
    }

    private fun convertTimeToString(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US)
        val date = Date(time)
        return simpleDateFormat.format(date)
    }

}