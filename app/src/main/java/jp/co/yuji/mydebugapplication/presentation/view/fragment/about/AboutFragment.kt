package jp.co.yuji.mydebugapplication.presentation.view.fragment.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentAboutBinding
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    companion object {

        const val ARG_KEY = "arg_key"

        fun newInstance(url : String) : Fragment {
            val fragment = AboutFragment()
            val bundle = Bundle()
            bundle.putString(ARG_KEY, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAboutBinding.inflate(layoutInflater)
        arguments?.getString(ARG_KEY).let {
            binding.webView.loadUrl(it!!)
        }
        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_about
    }

}