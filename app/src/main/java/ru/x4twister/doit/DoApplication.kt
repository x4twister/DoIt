/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class DoApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configuration = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(configuration)
    }
}