/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit.model

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import java.util.*

open class CheckList : RealmObject() {

    @PrimaryKey
    var id: String = ""

    var name: String = "New checklist"
        set(value) {
            realm.executeTransaction {
                field = value
            }
        }

    val tasks: RealmList<Task> = RealmList()

    fun createTask(): Task {
        realm.beginTransaction()
        val task = Task.newInstance()
        tasks.add(task)
        realm.commitTransaction()

        return task
    }

    fun deleteTask(task: Task) {
        realm.executeTransaction {
            tasks.remove(task)
            task.deleteFromRealm()
        }
    }

    fun done(): Boolean {
        return tasks.all {
            it.done
        }
    }

    companion object {
        fun newInstance() =
            Realm.getDefaultInstance().createObject<CheckList>(UUID.randomUUID().toString())
    }
}