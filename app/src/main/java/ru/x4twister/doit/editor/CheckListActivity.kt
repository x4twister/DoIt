package ru.x4twister.doit.editor

import android.content.Context
import android.content.Intent
import ru.x4twister.doit.SingleFragmentActivity

class CheckListActivity: SingleFragmentActivity(), CheckListFragment.Callback  {

    override fun createFragment(): CheckListFragment {
        val checkListId=intent.getSerializableExtra(EXTRA_CHECKLIST_ID) as String
        return CheckListFragment.newInstance(checkListId)
    }

    companion object {

        const val EXTRA_CHECKLIST_ID="ru.x4twister.doit.editor.checklist_id"

        fun newIntent(packageContext: Context, topicId: String): Intent {
            val intent= Intent(packageContext,CheckListActivity::class.java)
            intent.putExtra(EXTRA_CHECKLIST_ID,topicId)
            return intent
        }
    }

    override fun onTopicDeleted() {
        finish()
    }
}