package ru.x4twister.doit.editor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
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
import java.io.OutputStreamWriter

class CheckListFragment: Fragment() {

    interface Callback{
        fun onCheckListDeleted()
    }

    val callback: Callback by lazy {
        activity as Callback
    }

    private var checkListId: String =""

    private val checkList: CheckList by lazy {
        CheckListLab.getCheckList(checkListId)!!
    }

    private var currentTask: Task?=null

    private val checkListViewModel by lazy {
        EditorViewModel(object: EditorViewModel.Callback{
            override fun onAdd() {
                requestTaskName(checkList.createTask())
            }
        })
    }

    private val taskAdapter by lazy {
        TaskAdapter(checkList.tasks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener(EditTextFragment.KEY,this) { key, bundle ->
            val requestCode=bundle.getInt(EditTextFragment.EXTRA_REQUEST)
            val resultCode=bundle.getInt(EditTextFragment.EXTRA_RESULT)
            val result = bundle.getString(EditTextFragment.EXTRA_TEXT)!!

            onEditTextFragmentResult(requestCode,resultCode,result)
        }

        checkListId= requireArguments().getSerializable(ARG_CHECKLIST_ID).toString()

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

        checkListViewModel.editMode=checkList.tasks.isEmpty()
        binding.viewModel=checkListViewModel

        binding.recycleView.run {
            layoutManager= LinearLayoutManager(activity)
            adapter=taskAdapter

            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_checklist,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.title){
            "Edit" -> {
                checkListViewModel.editMode=checkListViewModel.editMode.not()
                taskAdapter.notifyDataSetChanged()
                true
            }
            /*"Info" -> {
                val intent= InfoActivity.newIntent(context!!,checklist.id)
                context!!.startActivity(intent)
                true
            }*/
            "Save checklist" -> {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TITLE, "${checkList.name}.txt")
                }
                // NOTE deprecated? This is from the official docs (04.08.21)
                startActivityForResult(intent, CREATE_FILE)
                true
            }
            "Rename checklist" -> {
                showDialog(checkList.name, "name", REQUEST_TEXT)
                true
            }
            "Delete checklist" -> {
                showDialog("", "Enter '${checkList.name}' for delete", REQUEST_DELETE)
                true
            }
            "Reset" -> {
                checkList.tasks.filter {
                    it.done
                }.forEach {
                    it.done=it.done.not()
                }
                taskAdapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode!= Activity.RESULT_OK)
            return

        when (requestCode) {
            CREATE_FILE -> {
                val text=checkList.tasks.joinToString(separator = "\n",transform = {
                    it.name
                })

                intent?.data?.also {
                    writeTextFromUri(text,it)
                }
            }
        }
    }

    private fun writeTextFromUri(text: String, uri: Uri) {
        val outputStream = requireContext().contentResolver.openOutputStream(uri)!!
        OutputStreamWriter(outputStream).apply {
            write(text)
            close()
        }
    }

    private fun requestTaskName(task: Task) {
        currentTask = task
        showDialog(task.name, "Name", REQUEST_TASK)
    }

    private fun showDialog(default: String, title: String, type: Int) {
        val dialog = EditTextFragment.newInstance(type, default, title)
        dialog.show(parentFragmentManager, DIALOG_TEXT)
    }

    private fun onEditTextFragmentResult(requestCode: Int, resultCode: Int, result: String) {
        if (resultCode!= Activity.RESULT_OK)
            return

        when (requestCode){
            REQUEST_TEXT -> {
                checkList.name= result
                activity?.title=checkList.name
            }

            REQUEST_DELETE -> {
                if (checkList.name==result) {
                    CheckListLab.deleteCheckList(checkList)
                    callback.onCheckListDeleted()
                }
            }

            REQUEST_TASK -> {
                currentTask!!.name=result
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object{

        const val ARG_CHECKLIST_ID="checklist_id"
        const val DIALOG_TEXT="DialogText"
        const val REQUEST_TEXT=0
        const val REQUEST_DELETE=1
        const val REQUEST_TASK=2

        const val CREATE_FILE=0

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
            binding.viewModel=TaskViewModel(object: TaskViewModel.Callback{
                override fun onEdit(task: Task) {
                    requestTaskName(task)
                }

                override fun onDelete(task: Task) {
                    checkList.deleteTask(task)
                    taskAdapter.notifyDataSetChanged()
                }
            }) { checkListViewModel.editMode }
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