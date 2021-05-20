package ru.x4twister.doit.model

object CheckListLab {

    fun getCheckList(id: String)=
        checkLists.find {
            it.id==id
        }

    val checkLists: List<CheckList> = (1..50).map { it ->
        CheckList(it.toString(),"Name $it",(1..5).map {
            Task("Task $it",false)
        })
    }
}