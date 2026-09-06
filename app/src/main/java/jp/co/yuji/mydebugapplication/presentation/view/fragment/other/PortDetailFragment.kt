package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonLogProgressBinding
import jp.co.yuji.mydebugapplication.presentation.presenter.other.PortDetailPresenter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

class PortDetailFragment: BaseFragment(R.layout.fragment_common_log_progress) {

    companion object {
        const val ARG_KEY = "arg_key"
        fun newInstance(command : String) : Fragment {
            val fragment = PortDetailFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KEY, command)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var binding: FragmentCommonLogProgressBinding

    private val presenter = PortDetailPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonLogProgressBinding.inflate(layoutInflater)
        val command = arguments?.getString(ARG_KEY)
        if (command != null) {
            binding.progressBar.visibility = View.VISIBLE

            presenter.getPort(command, object : PortDetailPresenter.OnGetPortListener {
                override fun onGetPort(result: String) {
                    binding.logText.text = getPortText(result)
                    binding.progressBar.visibility = View.GONE
                }
            })
        }
        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_port_detail
    }

    private fun getPortText(result: String) : String {
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