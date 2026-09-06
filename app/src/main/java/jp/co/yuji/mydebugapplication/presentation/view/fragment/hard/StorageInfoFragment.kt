package jp.co.yuji.mydebugapplication.presentation.view.fragment.hard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.domain.model.CommonDto
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonDetailRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

/**
 * Storage Info Fragment.
 */
class StorageInfoFragment : BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return StorageInfoFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private lateinit var adapter: CommonDetailRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        if (activity != null) {
            adapter = CommonDetailRecyclerViewAdapter(requireActivity(), getStorageInfo())
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_storage_info
    }

    private fun getStorageInfo(): ArrayList<CommonDto> {
        val list = ArrayList<CommonDto>()
        val context = activity as Context

        val filesDir = context.filesDir.absolutePath
        list.add(CommonDto("Context.getFilesDir()", filesDir))

        val cacheDir = context.cacheDir.absolutePath
        list.add(CommonDto("Context.getCacheDir()", cacheDir))

        val externalStorageDir = Environment.getExternalStorageDirectory().absolutePath
        list.add(CommonDto("Environment.getExternalStorageDirectory()", externalStorageDir))

        addExternalFilesDir(context, list)

        val externalCacheDir = context.externalCacheDir?.absolutePath
        if (externalCacheDir != null) {
            list.add(CommonDto("Context.getExternalCacheDir()", externalCacheDir))
        }

        addExternalMediaDir(context, list)

        val obbDir = context.obbDir.absolutePath
        list.add(CommonDto("Context.getObbDir()", obbDir))

        addObbDirs(context, list)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val codeCacheDir = context.codeCacheDir.absolutePath
            list.add(CommonDto("Context.getCodeCacheDir()", codeCacheDir))
        }

        return list
    }

    private fun addExternalFilesDir(context: Context, list: ArrayList<CommonDto>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val dirs = context.getExternalFilesDirs(null)
            for (dir in dirs) {
                if (dir != null) {
                    val path = dir.absolutePath
                    list.add(CommonDto("Context.getExternalFilesDirs(String var1)", path))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (Environment.isExternalStorageRemovable(dir)) {
                            list.add(CommonDto("Environment.isExternalStorageRemovable(File path)", path))
                        }
                    }
                }
            }
        }
    }

    private fun addExternalMediaDir(context: Context, list: ArrayList<CommonDto>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val dirs = context.externalMediaDirs
            for (dir in dirs) {
                if (dir != null) {
                    val path = dir.absolutePath
                    list.add(CommonDto("Context.getExternalMediaDirs()", path))
                }
            }
        }
    }

    private fun addObbDirs(context: Context, list: ArrayList<CommonDto>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val dirs = context.obbDirs
            for (dir in dirs) {
                if (dir != null) {
                    val path = dir.absolutePath
                    list.add(CommonDto("Context.getObbDirs()", path))
                }
            }
        }
    }

}