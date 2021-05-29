package ru.x4twister.doit.editor

import androidx.databinding.BaseObservable

class EditorViewModel: BaseObservable() {

    var editMode: Boolean=false
        set(value) {
            field = value
            notifyChange()
        }
}