package com.example.mytodo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(val context: Context, name:String, version:Int)
    : SQLiteOpenHelper(context,name,null,version) {
    val TAG="@@DBHelper"
    //输入创建数据库表TABLE的命令，联合TODO数据集进行创建，剩下的就去全靠mainactivity
    private val createDB = "CREATE TABLE ${Todo.TABLE}(" +
            "${Todo.COL_ID} integer PRIMARY KEY autoincrement," +
            "${Todo.COL_OUTLINE} text," +
            "${Todo.COL_CONTENT} text," +
            "${Todo.COL_TIME} REAL)"
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG,"DB onCreate ... ")
        if (db != null) {
            db.execSQL(createDB)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldversion: Int, newversion: Int) {
        TODO("Not yet implemented")
    }
}