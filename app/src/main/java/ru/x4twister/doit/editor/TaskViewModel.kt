package ru.x4twister.doit.editor

import android.view.View
import androidx.databinding.BaseObservable
import ru.x4twister.doit.model.Task

class TaskViewModel: BaseObservable() {

    val title
        get() = task!!.name

    val checked
        get() = task!!.done

    var task: Task? = null
        set(value) {
            field = value
            notifyChange()
        }

    fun onClick(view: View){
        task!!.done=!task!!.done
    }
}