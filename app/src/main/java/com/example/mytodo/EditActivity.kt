package com.example.mytodo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {
    val TAG = "@@EditActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "trans successfully")
        setContentView(R.layout.activity_edit)
        initData()

    }


    private fun initData() {

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val outline = findViewById<TextView>(R.id.ipt_outline).text.toString();
            val content = findViewById<TextView>(R.id.ipt_content).text.toString();
            Log.d(TAG, "outline as $outline content as $content")
            val i = Intent(this@EditActivity, MainActivity::class.java) //建立Intent

            i.putExtra("OUTLINE", outline)
            i.putExtra("CONTENT", content) //传递两个参数
            Log.d(TAG, "send outline as $outline content as $content")
            startActivity(i)
        }
    }
}