/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit.model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject

open class Task : RealmObject() {

    var name: String = "name"
        set(value) {
            realm.executeTransaction {
                field = value
            }
        }

    var done: Boolean = false
        set(value) {
            realm.executeTransaction {
                field = value
            }
        }

    companion object {
        fun newInstance() = Realm.getDefaultInstance().createObject<Task>()
    }
}