package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonLogProgressBinding
import jp.co.yuji.mydebugapplication.presentation.presenter.other.SystemPropertiesPresenter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * System Properties Fragment.
 */
class SystemPropertiesFragment : BaseFragment(R.layout.fragment_common_log_progress) {

    companion object {
        fun newInstance() : Fragment {
            return SystemPropertiesFragment()
        }
    }
    private lateinit var binding: FragmentCommonLogProgressBinding
    private val presenter = SystemPropertiesPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonLogProgressBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE

        presenter.getSystemProperties(object : SystemPropertiesPresenter.OnGetSystemPropertiesListener {
            override fun onGetSystemProperties(result: String) {
                binding.logText.text = getSystemPropertiesText(result)
                binding.progressBar.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_system_properties
    }

    private fun getSystemPropertiesText(result: String) : String {
        val resultText = StringBuilder()
        resultText.append(result)
        resultText.append("\n")
        resultText.append("\n")
        resultText.append("\n")
        resultText.append("\n")
        resultText.append("\n")
        resultText.append("\n")
        resultText.append("\n")
        return resultText.toString()
    }
}