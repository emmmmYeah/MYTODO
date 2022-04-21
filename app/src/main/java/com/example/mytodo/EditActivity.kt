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
    //var id: Int? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "trans successfully")
        setContentView(R.layout.activity_edit)//update和init要区分开

        val o_Outline= intent.getStringExtra("OLD_OUTLINE").toString()
        val o_Content=intent.getStringExtra("OLD_CONTENT").toString()
        var id= intent.getStringExtra("item_id")?.toString()
        Log.d(TAG,"mainOutline as ${o_Outline} mainContent as $o_Content  id as $id")
        if (id != null) {
            setEditText(o_Outline,o_Content,id.toInt())
        }
        else
        {
            setEditText(o_Outline,o_Content,null)
        }
        initData()


    }

    private fun setEditText(oOutline: String, oContent: String, item_id: Int?) {
        findViewById<EditText>(R.id.ipt_outline).setText(oOutline)
        findViewById<EditText>(R.id.ipt_content).setText(oContent)
        if (item_id != null) {
            findViewById<TextView>(R.id.item_id).setText(item_id)
        }


    }


    private fun initData() {

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val outline = findViewById<TextView>(R.id.ipt_outline).text.toString();
            val content = findViewById<TextView>(R.id.ipt_content).text.toString();
            val item_id = findViewById<TextView>(R.id.item_id).text.toString();
            Log.d(TAG, "outline as $outline content as $content")
            val i = Intent(this@EditActivity, MainActivity::class.java) //建立Intent

            i.putExtra("OUTLINE", outline)
            i.putExtra("CONTENT", content) //传递两个参数
            i.putExtra("id", item_id.toInt()) //传递两个参数
            Log.d(TAG, "send outline as $outline content as $content id as $item_id")
            startActivity(i)
        }
    }
}