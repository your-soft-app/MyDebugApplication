package jp.co.yuji.mydebugapplication.presentation.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.ActivityAboutBinding
import jp.co.yuji.mydebugapplication.presentation.view.fragment.about.AboutFragment

class AboutActivity : BaseActivity() {

    companion object {
        const val ARG_KEY = "arg_key"
        fun startActivity(activity : Activity, url: String) {
            val intent = Intent(activity, AboutActivity::class.java).apply {
                putExtra(ARG_KEY, url)
            }
            activity.startActivity(intent)
        }
    }
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init view
        intent.getStringExtra(ARG_KEY)?.let {
            val fragment = AboutFragment.newInstance(it)
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }

    }
}