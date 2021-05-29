/*
 * Copyright (c) 2020 x4twister
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */

package ru.x4twister.doit.editor

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import ru.x4twister.doit.R
import ru.x4twister.doit.databinding.DialogEditTextBinding

class EditTextFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding: DialogEditTextBinding=DataBindingUtil
            .inflate(LayoutInflater.from(activity),R.layout.dialog_edit_text,null,false)

        val request=requireArguments().getInt(ARG_REQUEST)
        val value=requireArguments().getString(ARG_VALUE)
        val title=requireArguments().getString(ARG_TITLE)
        binding.editText.setText(value)

        return AlertDialog.Builder(activity)
            .setView(binding.root)
            .setTitle(title)
            .setPositiveButton("Ok"){ dialogInterface: DialogInterface, i: Int ->
                sendResult(request,Activity.RESULT_OK,binding.editText.text.toString())
            }
            .create()
    }

    private fun sendResult(request: Int, result: Int, text: String) {
        parentFragmentManager.setFragmentResult(KEY, bundleOf(EXTRA_REQUEST to request,EXTRA_RESULT to result,EXTRA_TEXT to text))
    }

    companion object{
        const val KEY="ru.x4twister.doit.editor.key"
        const val EXTRA_REQUEST="ru.x4twister.doit.editor.request"
        const val EXTRA_RESULT="ru.x4twister.doit.editor.result"
        const val EXTRA_TEXT="ru.x4twister.doit.editor.text"
        const val ARG_REQUEST="request"
        const val ARG_VALUE="value"
        const val ARG_TITLE="title"

        fun newInstance(request: Int, value: String, title: String): EditTextFragment {
            val args=Bundle()
            args.putInt(ARG_REQUEST,request)
            args.putString(ARG_VALUE,value)
            args.putString(ARG_TITLE,title)

            val fragment=EditTextFragment()
            fragment.arguments=args

            return fragment
        }

        fun getValue(data: Intent?) =
            data!!.getStringExtra(EXTRA_TEXT)!!
    }
}