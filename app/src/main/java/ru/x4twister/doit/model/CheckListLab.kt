/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit.model

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

object CheckListLab {

    private val realm = Realm.getDefaultInstance()

    val checkLists: RealmResults<CheckList>
        get() {
            return realm.where<CheckList>().findAll()
        }

    fun getCheckList(id: String) =
        realm.where<CheckList>().equalTo("id", id).findFirst()

    fun createCheckList(): CheckList {
        realm.beginTransaction()
        val checkList = CheckList.newInstance()
        realm.commitTransaction()

        return checkList
    }

    fun deleteCheckList(checkList: CheckList) {
        realm.executeTransaction {
            checkList.deleteFromRealm()
        }
    }
}