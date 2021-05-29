/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit.model

import java.util.*

class CheckList(val id: String, var name: String, val tasks: MutableList<Task>) {

    fun createTask(): Task {
        val task=Task.newInstance()
        tasks.add(task)
        return task
    }

    companion object {
        fun newInstance()=CheckList(UUID.randomUUID().toString(),"New checklist", mutableListOf())
    }
}