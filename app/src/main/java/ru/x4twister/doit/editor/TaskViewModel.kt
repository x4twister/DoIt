package ru.x4twister.doit.editor

import android.view.View
import androidx.databinding.BaseObservable
import ru.x4twister.doit.model.Task

class TaskViewModel(val callback:Callback,val editMode: ()->Boolean): BaseObservable() {

    interface Callback{
        fun onEdit(task: Task)
        fun onDelete(task: Task)
    }

    val title
        get() = task!!.name

    val checked
        get() = task!!.done

    var task: Task? = null
        set(value) {
            field = value
            notifyChange()
        }

    fun onSwitchClick(view: View){
        task!!.done=task!!.done.not()
    }

    fun onClick(view: View) {
        if (editMode())
            callback.onEdit(task!!)
    }

    fun onDeleteClick(view: View) {
        callback.onDelete(task!!)
    }

    fun editVisibility()=
        if (editMode())
            View.VISIBLE
        else
            View.GONE

    fun defaultVisibility()=
        if (editMode())
            View.GONE
        else
            View.VISIBLE
}