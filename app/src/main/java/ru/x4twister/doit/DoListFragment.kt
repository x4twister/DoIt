/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.x4twister.doit.databinding.FragmentDoListBinding
import ru.x4twister.doit.databinding.ListItemChecklistBinding
import ru.x4twister.doit.editor.CheckListActivity
import ru.x4twister.doit.model.CheckList
import ru.x4twister.doit.model.CheckListLab
import java.io.InputStreamReader

class DoListFragment: Fragment() {

    private val checkListAdapter by lazy {
        CheckListAdapter(CheckListLab.checkLists)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentDoListBinding=DataBindingUtil
            .inflate(inflater,R.layout.fragment_do_list,container,false)

        binding.recycleView.run {
            layoutManager=LinearLayoutManager(activity)
            adapter=checkListAdapter
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_do_list,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // R.id.new_checklist not found
        return when (item.title) {
            "Create" -> {
                val checkList= CheckListLab.createCheckList()

                val intent= CheckListActivity.newIntent(requireContext(),checkList.id)
                startActivity(intent)
                updateUI()
                true
            }
            "Load" -> {
                val intent= Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type="text/plain"
                }
                // NOTE deprecated? This is from the official docs (04.08.21)
                startActivityForResult(intent, REQUEST_DATA)
                true
            }
            else -> onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode!= Activity.RESULT_OK)
            return

        when (requestCode) {
            REQUEST_DATA -> {
                intent?.data?.also {
                    val name=readNameFromUri(it)
                    val text=readTextFromUri(it)
                    val checkList= CheckListLab.createChecklistFromText(name,text)

                    val intent= CheckListActivity.newIntent(requireContext(),checkList.id)
                    startActivity(intent)
                    updateUI()
                }
            }
        }
    }

    private fun readNameFromUri(uri: Uri): String {
        requireContext().contentResolver.query(uri, null, null, null, null)?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            return it.getString(nameIndex)
        }

        return ""
    }

    private fun readTextFromUri(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)!!
        return InputStreamReader(inputStream).buffered().readText()
    }

    private fun updateUI() {
        checkListAdapter.run {
            setCheckLists(CheckListLab.checkLists)
            notifyDataSetChanged()
        }
    }

    companion object {
        const val REQUEST_DATA=0

        fun newInstance()=DoListFragment()
    }

    inner class CheckListHolder(private val binding: ListItemChecklistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(checkList: CheckList) {
            binding.viewModel!!.checkList=checkList
        }

        init {
            binding.viewModel=CheckListViewModel()
        }
    }

    inner class CheckListAdapter(private var checkLists: List<CheckList>): RecyclerView.Adapter<CheckListHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListHolder {
            val inflater=LayoutInflater.from(activity)
            val binding:ListItemChecklistBinding=DataBindingUtil
                .inflate(inflater,R.layout.list_item_checklist,parent,false)

            return CheckListHolder(binding)
        }

        override fun getItemCount()=checkLists.size

        override fun onBindViewHolder(holder: CheckListHolder, position: Int) {
            holder.bind(checkLists[position])
        }

        fun setCheckLists(newCheckLists: List<CheckList>){
            checkLists=newCheckLists
        }
    }
}