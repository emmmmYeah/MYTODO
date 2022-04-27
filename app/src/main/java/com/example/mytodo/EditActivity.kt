package com.example.mytodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {
    val TAG = "@@EditActivity"
//    var id: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "trans successfully")
        setContentView(R.layout.activity_edit)//update和init要区分开

        val o_Outline= intent.getStringExtra("OLD_OUTLINE").toString()
        val o_Content=intent.getStringExtra("OLD_CONTENT").toString()
        val id= intent.getStringExtra("item_id")
        Log.d(TAG,"mainOutline as ${o_Outline} mainContent as $o_Content  id as $id")
        if (id != null) {
            Log.d(TAG,"id as ${id}")
            setEditText(o_Outline,o_Content)
        }
        else
        {
            setEditText(o_Outline,o_Content)
        }
    if (id != null) {
        initData(id)
    }
    else{
        initData(null)
    }


    }

    private fun setEditText(oOutline: String, oContent: String) {
        findViewById<EditText>(R.id.ipt_outline).setText(oOutline)
        findViewById<EditText>(R.id.ipt_content).setText(oContent)



    }


    private fun initData(item_id: String?) {

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val outline = findViewById<TextView>(R.id.ipt_outline).text.toString();
            val content = findViewById<TextView>(R.id.ipt_content).text.toString();

            Log.d(TAG, "outline as $outline content as $content")
            val i = Intent(this@EditActivity, MainActivity::class.java) //建立Intent

            i.putExtra("OUTLINE", outline)
            i.putExtra("CONTENT", content) //传递两个参数
            i.putExtra("item_id", item_id) //传递两个参数

            Log.d(TAG, "send outline as $outline content as $content id as $item_id")
            startActivity(i)
        }
    }
}