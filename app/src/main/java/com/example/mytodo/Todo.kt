package com.example.mytodo


data class Todo(
    val outline:String,
    val content: String,
    val createTime:Long,
){
    companion object{
        val TABLE="todo"
        val COL_ID="id"
        val COL_OUTLINE="outline"
        val COL_CONTENT="content"
        val COL_TIME="createTime"
    }
    var id:Int?=null

}