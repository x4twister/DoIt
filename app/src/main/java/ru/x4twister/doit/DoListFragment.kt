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
import ru.x4twister.doit.databinding.ListItemTargetBinding

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
            adapter=TargetAdapter(listOf(Target("abc"),Target("xyz")))
        }

        return binding.root
    }

    companion object {
        fun newInstance()=DoListFragment()
    }

    inner class TargetHolder(val binding: ListItemTargetBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(target: Target) {
            binding.viewModel!!.target=target
        }

        init {
            binding.viewModel=TargetViewModel()
        }
    }

    inner class TargetAdapter(private val targets: List<Target>): RecyclerView.Adapter<TargetHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetHolder {
            val inflater=LayoutInflater.from(activity)
            val binding:ListItemTargetBinding=DataBindingUtil
                .inflate(inflater,R.layout.list_item_target,parent,false)

            return TargetHolder(binding)
        }

        override fun getItemCount()=targets.size

        override fun onBindViewHolder(holder: TargetHolder, position: Int) {
            holder.bind(targets[position])
        }
    }
}