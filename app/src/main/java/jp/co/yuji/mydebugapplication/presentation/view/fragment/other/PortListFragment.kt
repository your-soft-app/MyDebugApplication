package jp.co.yuji.mydebugapplication.presentation.view.fragment.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yuji.mydebugapplication.R
import jp.co.yuji.mydebugapplication.databinding.FragmentCommonBinding
import jp.co.yuji.mydebugapplication.presentation.view.adapter.common.CommonInfoRecyclerViewAdapter
import jp.co.yuji.mydebugapplication.presentation.view.fragment.BaseFragment

class PortListFragment: BaseFragment(R.layout.fragment_common) {

    companion object {
        fun newInstance() : Fragment {
            return PortListFragment()
        }
    }
    private lateinit var binding: FragmentCommonBinding

    private val listener = object:CommonInfoRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(position: Int) {
            val command = ActionType.find(position)?.command
            if (command == null) {
                postLogEvent("command is null")
                return
            }
            val fragment = PortDetailFragment.newInstance(command)
            activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCommonBinding.inflate(layoutInflater)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = CommonInfoRecyclerViewAdapter(getPortList())
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(listener)

        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        return binding.root
    }

    override fun getTitle(): Int {
        return R.string.screen_name_port_list
    }

    private fun getPortList() : List<String> {
        val list = ArrayList<String>()

        for (type in ActionType.entries) {
            list.add(type.position, type.title)
        }

        return list
    }

    enum class ActionType(val title: String, val position: Int, val command: String)  {
        ALL("Listen Port", 0, "netstat -lp"),
        TCP("TCP", 1, "netstat -tp"),
        UDP("UDP", 2, "netstat -up");

        companion object {
            fun find(position: Int): ActionType? {
                return entries.firstOrNull { it.position == position }
            }
        }
    }

}