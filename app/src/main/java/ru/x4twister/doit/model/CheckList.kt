/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit.model

import java.util.*

class CheckList(val id: String, var name: String, val tasks: List<Task>) {

    companion object {
        fun newInstance()=CheckList(UUID.randomUUID().toString(),"New checklist", (1..5).map {
            Task("Task $it", false)
        })
    }
}