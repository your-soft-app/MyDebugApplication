package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentAdbShellBinding
import jp.co.yuji.mydebugapplication.presentation.presenter.other.ExecShellPresenter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Exec Shell Fragment.
 */
class ExecShellFragment : BaseFragment(R.layout.fragment_adb_shell) {

    companion object {
        fun newInstance() : Fragment {
            return ExecShellFragment()
        }
    }
    private lateinit var binding: FragmentAdbShellBinding
    private val presenter = ExecShellPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAdbShellBinding.inflate(layoutInflater)

        val supportMessage =
            "pm list features\n" +
            "pm list libraries\n" +
            "ls /system/usr\n" +
            "etc"
        binding.adbShellResultText.text = supportMessage

        binding.adbShellExecuteButton.setOnClickListener {
            binding.adbShellResultText.text = getExecuteText(binding.adbShellEditText.text.toString())
            binding.root.hideKeyboard()
            binding.adbShellResultText.requestFocus()
            binding.progressBar.visibility = View.VISIBLE

            val command = binding.adbShellEditText.text.toString()
            presenter.execShell(command, object : ExecShellPresenter.OnExecShellListener {
                override fun onExecShell(result: String) {
                    binding.adbShellResultText.text = getExecuteText(result)
                    binding.root.hideKeyboard()
                    binding.adbShellResultText.requestFocus()
                    binding.progressBar?.visibility = View.GONE
                }
            })
        }

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_exec_shell
    }

    private fun getExecuteText(result: String) : String {
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

    private fun View.hideKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

}