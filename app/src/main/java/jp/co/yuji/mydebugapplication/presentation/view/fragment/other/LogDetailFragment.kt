package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonLogProgressBinding
import jp.co.yuji.mydebugapplication.presentation.presenter.other.LogDetailPresenter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Log Detail Fragment.
 */
class LogDetailFragment : BaseFragment(R.layout.fragment_common_log_progress) {

    companion object {
        fun newInstance() : Fragment {
            return LogDetailFragment()
        }
    }
    private lateinit var binding: FragmentCommonLogProgressBinding
    private val presenter = LogDetailPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonLogProgressBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE

        presenter.getLog(object : LogDetailPresenter.OnGetLogListener {
            override fun onGetLog(log: String) {
                binding.logText.text = log
                binding.progressBar?.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_log_detail
    }

}