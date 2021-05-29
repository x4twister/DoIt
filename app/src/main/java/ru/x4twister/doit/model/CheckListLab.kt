package ru.x4twister.doit.model

object CheckListLab {

    fun getCheckList(id: String)=
        checkLists.find {
            it.id==id
        }

    fun createCheckList(): CheckList {
        val checkList = CheckList.newInstance()
        checkLists.add(checkList)
        return checkList
    }

    fun deleteCheckList(checkList: CheckList) {
        checkLists.remove(checkList)
    }

    var checkLists: MutableList<CheckList> = mutableListOf()
}