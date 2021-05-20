/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.x4twister.doit.databinding.FragmentDoListBinding
import ru.x4twister.doit.databinding.ListItemChecklistBinding
import ru.x4twister.doit.model.CheckList
import ru.x4twister.doit.model.CheckListLab

class DoListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentDoListBinding=DataBindingUtil
            .inflate(inflater,R.layout.fragment_do_list,container,false)

        binding.recycleView.run {
            layoutManager=LinearLayoutManager(activity)
            adapter=CheckListAdapter(CheckListLab.checkLists)
        }

        return binding.root
    }

    companion object {
        fun newInstance()=DoListFragment()
    }

    inner class CheckListHolder(val binding: ListItemChecklistBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(checkList: CheckList) {
            binding.viewModel!!.checkList=checkList
        }

        init {
            binding.viewModel=CheckListViewModel()
        }
    }

    inner class CheckListAdapter(private val checkLists: List<CheckList>): RecyclerView.Adapter<CheckListHolder>() {

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
    }
}