/*
 * Copyright (c) 2021 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit

class DoListActivity : SingleFragmentActivity() {

    override fun createFragment()=DoListFragment.newInstance()
}
