package jp.co.yuji.mydebugapplication.presentation.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.ActivityPinningBinding
import jp.co.yuji.mydebugapplication.presentation.view.fragment.other.PinningActivityFragment

/**
 * Pinning Activity.
 */
class PinningActivity : BaseActivity() {

    companion object {

        const val ARG_KEY = "arg_key"

        fun startActivity(activity : Activity, pinningType : PinningActivityFragment.PinningType) {
            val intent = Intent(activity, PinningActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(ARG_KEY, pinningType.type)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityPinningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init view
        val fragment = PinningActivityFragment.newInstance(intent.getIntExtra(ARG_KEY,
                PinningActivityFragment.PinningType.PINNING.type))
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}