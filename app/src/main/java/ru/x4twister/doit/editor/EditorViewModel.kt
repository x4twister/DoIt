package ru.x4twister.doit.editor

import android.view.View
import androidx.databinding.BaseObservable
import ru.x4twister.doit.model.Task

class EditorViewModel(val callback: Callback): BaseObservable() {

    interface Callback{
        fun onAdd()
    }

    var editMode: Boolean=false
        set(value) {
            field = value
            notifyChange()
        }

    fun editVisibility()=
        if (editMode)
            View.VISIBLE
        else
            View.GONE

    fun onClick(view: View) {
        callback.onAdd()
    }
}