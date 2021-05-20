/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit

import android.view.View
import androidx.databinding.BaseObservable
import ru.x4twister.doit.editor.CheckListActivity
import ru.x4twister.doit.model.CheckList

class CheckListViewModel: BaseObservable() {

    val title
        get() = checkList!!.name

    var checkList: CheckList? = null
        set(value) {
            field = value
            notifyChange()
        }

    fun onClick(view: View){
        val context=view.context
        val intent= CheckListActivity.newIntent(context, checkList!!.id)
        context.startActivity(intent)
    }
}