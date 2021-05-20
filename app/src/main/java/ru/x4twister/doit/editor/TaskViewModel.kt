package ru.x4twister.doit.editor

import androidx.databinding.BaseObservable
import ru.x4twister.doit.model.Task

class TaskViewModel: BaseObservable() {

    val title
        get() = task!!.name

    var task: Task? = null
        set(value) {
            field = value
            notifyChange()
        }
}