package ru.x4twister.doit.model

class Task (var name:String, var done: Boolean){
    companion object {
        fun newInstance()=Task("name",false)
    }
}