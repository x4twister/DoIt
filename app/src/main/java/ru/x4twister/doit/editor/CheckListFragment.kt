package ru.x4twister.doit.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.x4twister.doit.R
import ru.x4twister.doit.databinding.FragmentChecklistListBinding
import ru.x4twister.doit.databinding.ListItemTaskBinding
import ru.x4twister.doit.model.CheckList
import ru.x4twister.doit.model.CheckListLab
import ru.x4twister.doit.model.Task

class CheckListFragment: Fragment() {

    interface Callback{
        fun onTopicDeleted()
    }

    val callback: Callback by lazy {
        activity as Callback
    }

    private var checkListId: String =""

    private val checkList: CheckList by lazy {
        CheckListLab.getCheckList(checkListId)!!
    }

    private val checkListViewModel by lazy {
        EditorViewModel()
    }

    private val taskAdapter by lazy {
        TaskAdapter(checkList.tasks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkListId= arguments!!.getSerializable(ARG_CHECKLIST_ID).toString()

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentChecklistListBinding =DataBindingUtil
            .inflate(inflater, R.layout.fragment_checklist_list,container,false)

        activity?.title=checkList.name

        binding.viewModel=checkListViewModel

        binding.recycleView.run {
            layoutManager= LinearLayoutManager(activity)
            adapter=taskAdapter

            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }


    companion object{

        const val ARG_CHECKLIST_ID="checklist_id"

        fun newInstance(checkListId: String): CheckListFragment {
            val args= Bundle()
            args.putSerializable(ARG_CHECKLIST_ID,checkListId)

            val fragment=CheckListFragment()
            fragment.arguments=args
            return fragment
        }
    }

    inner class TaskHolder(private val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(task: Task) {
            binding.viewModel!!.task=task
        }

        init {
            binding.viewModel=TaskViewModel()
        }
    }

    inner class TaskAdapter(private var tasks: List<Task>): RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val inflater= LayoutInflater.from(activity)
            val binding:ListItemTaskBinding= DataBindingUtil
                .inflate(inflater,R.layout.list_item_task,parent,false)

            return TaskHolder(binding)
        }

        override fun getItemCount()=tasks.size

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            holder.bind(tasks[position])
        }

        fun setTasks(newTasks: List<Task>){
            tasks=newTasks
        }
    }
}