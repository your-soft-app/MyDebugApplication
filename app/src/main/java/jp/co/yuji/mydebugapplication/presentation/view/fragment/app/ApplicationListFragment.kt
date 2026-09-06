package jp.co.yuji.mydebugapplication.presentation.view.fragment.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.yuji.mydebugapplication.BuildConfig
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonProgressBinding
import jp.co.yuji.mydebugapplication.domain.model.ApplicationListDto
import jp.co.yuji.mydebugapplication.presentation.presenter.app.ApplicationListPresenter
import jp.co.yuji.mydebugapplication.presentation.view.adapter.ApplicationListRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment


/**
 * Application List Fragment.
 */
class ApplicationListFragment : BaseFragment(R.layout.fragment_common_progress) {

    companion object {
        const val ARG_KEY = "arg_key"

        fun newInstance(actionTypePosition : Int) : Fragment {
            val fragment = ApplicationListFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_KEY, actionTypePosition)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentCommonProgressBinding
    private val presenter = ApplicationListPresenter()
    private var adapter: ApplicationListRecyclerViewAdapter? = null

    private val listener = object: ApplicationListRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(packageName: String) {
            val fragment = ApplicationDetailFragment.newInstance(packageName)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            postLogEvent("package name: $packageName")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonProgressBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val actionTypePosition = arguments?.getInt(ARG_KEY)
        val list = ArrayList<ApplicationListDto>()
        adapter = ApplicationListRecyclerViewAdapter(list)
        binding.recyclerView.adapter = adapter
        adapter?.setOnItemClickListener(listener)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        if (actionTypePosition != null) {
            addApplicationList(actionTypePosition, binding.recyclerView, binding.emptyView)
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu to use in the action bar
        inflater.inflate(R.menu.search, menu)

        val menuItem = menu.findItem(R.id.menu_search_view)
        val searchView = menuItem.actionView as? SearchView
        searchView?.setOnQueryTextListener(this.onQueryTextListener)
        searchView?.imeOptions = EditorInfo.IME_ACTION_DONE

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun getTitle(): Int {
        return R.string.screen_name_application_list
    }

    private fun addApplicationList(actionTypePosition: Int, recyclerView: RecyclerView, emptyView: TextView) {
        val actionType: ApplicationInfoFragment.ActionType? = ApplicationInfoFragment.ActionType.find(actionTypePosition)
        if (activity != null && actionType != null) {
            presenter.getApplicationList(requireActivity(), actionType, object: ApplicationListPresenter.OnGetApplicationListListener {
                override fun onGetApplicationList(appList: List<ApplicationListDto>) {
                    adapter?.updateList(appList)
                    binding.progressBar.visibility = View.GONE
                    if (appList.isNotEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        emptyView.visibility = View.GONE
                    } else {
                        recyclerView.visibility = View.GONE
                        emptyView.visibility = View.VISIBLE
                        emptyView.text = getString(R.string.application_info_no_data_text)
                    }
                }
            })
        }
    }

    private val onQueryTextListener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(searchWord: String): Boolean {
            if (BuildConfig.DEBUG) {
                println("onQueryTextSubmit searchWord : $searchWord")
            }
//            adapter?.filter(searchWord)
            return false
        }
        override fun onQueryTextChange(searchWord: String): Boolean {
            if (BuildConfig.DEBUG) {
                println("onQueryTextChange searchWord : $searchWord")
            }
            adapter?.filter(searchWord)
            return false
        }
    }

}