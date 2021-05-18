/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit

import androidx.databinding.BaseObservable

class TargetViewModel: BaseObservable() {

    val title
        get() = target!!.name

    var target:Target? = null
        set(value) {
            field = value
            notifyChange()
        }
}