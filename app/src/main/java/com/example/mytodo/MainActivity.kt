package com.example.mytodo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    val TAG = "@@MainActivity"
    lateinit var helper: DBHelper
    lateinit var adapter: MainActivity.TodoAdapter
    private var toUpdate: Todo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){
//打开DB
        helper = DBHelper(this, "todo.db", 3)
        adapter = TodoAdapter()
//下面这三行是要用adapter一定要写的
        val context=this
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        //
        readInDb()
        findViewById<TextView>(R.id.btn_add).setOnClickListener {
            startActivity(Intent(context, EditActivity::class.java))
        }
        saveInput()//传edit界面的数据

        findViewById<TextView>(R.id.btn_search).setOnClickListener {
           // Searchfor()
        }

    }

    private fun saveInput() {
        //取出字符串并转向saveInDb()中
        val mainOutline= intent.getStringExtra("OUTLINE").toString()
        val mainContent=intent.getStringExtra("CONTENT").toString()
        val item_id= intent.getStringExtra("item_id")?.toInt()


        Log.d(TAG,"mainOutline as ${mainOutline} mainContent as $mainContent id as ${item_id}")
        if (item_id != null) {
            updateindb(mainContent,mainOutline,item_id)
        }
        else
        {
            saveInDb(mainContent,mainOutline)
        }
    }
    private fun saveInDb(text: String, outline: String,) {
        //往数据库存数据！！！
        Log.d(TAG, "get in edit=$text  outline=$outline")
        val db = helper.writableDatabase
        val item = Todo(outline,text, System.currentTimeMillis())
        val values = ContentValues().apply {
            put(Todo.COL_OUTLINE,item.outline)
            put(Todo.COL_CONTENT, item.content)
            put(Todo.COL_TIME, item.createTime)
        }
        var rs = -1
        //Log.d(TAG,"这里这里$toUpdate")
        if (toUpdate != null) {
            item.id = toUpdate?.id
            Log.d(TAG, "UPDATE ID=$rs")
            rs = db.update(Todo.TABLE, values, "id=?", arrayOf(toUpdate?.id.toString()))
            if (rs != -1) {
                toUpdate?.id?.let { adapter.replaceItem(it, item) }
                toUpdate = null
            }
        } else {
            Log.d(TAG, "insert id =$rs")
            rs = db.insert(Todo.TABLE, null, values).toInt()
            Log.d(TAG, "insert id =$rs")
            if (rs != -1) {
                item.id = rs
                adapter.addItem(item)
            }
        }
        Toast.makeText(this, if (rs < 0) "保存失败" else "保存成功", Toast.LENGTH_LONG).show()

    }

    private fun updateindb(text: String, outline: String,item_id:Int) {
        //往数据库存数据！！！
        Log.d(TAG, "get in edit=$text  outline=$outline")
        val db = helper.writableDatabase
        val item = Todo(outline,text, System.currentTimeMillis())
        val values = ContentValues().apply {
            put(Todo.COL_OUTLINE,item.outline)
            put(Todo.COL_CONTENT, item.content)
            put(Todo.COL_TIME, item.createTime)
        }
        var rs = -1
        Log.d(TAG,"这里这里$toUpdate")
        if (item_id != null) {
            item.id = item_id
            Log.d(TAG, "UPDATE ID=$rs")
            rs = db.update(Todo.TABLE, values, "id=?", arrayOf(item_id.toString()))
            if (rs != -1) {
                item_id.let { adapter.replaceItem(it, item) }
               // toUpdate = null
            }
        } else {
            Log.d(TAG, "insert id =$rs")
            rs = db.insert(Todo.TABLE, null, values).toInt()
            Log.d(TAG, "insert id =$rs")
            if (rs != -1) {
                item.id = rs
                adapter.addItem(item)
            }
        }
        Toast.makeText(this, if (rs < 0) "保存失败" else "保存成功", Toast.LENGTH_LONG).show()

    }

    private fun setInputText(o_content: String,o_outline:String,item_id: Int?) {

        Log.d(TAG, "content=$o_content, outline=$o_outline")
        //findViewById<EditText>(R.id.ipt_text).setText(s)

//        Log.d(TAG, "outline as $o_outline content as $o_content")
        val i = Intent(this@MainActivity, EditActivity::class.java) //建立Intent

        i.putExtra("OLD_OUTLINE", o_outline)
        i.putExtra("OLD_CONTENT", o_content) //传递两个参数
        i.putExtra("item_id",item_id.toString()) //传递两个参数


        Log.d(TAG, "send outline as $o_outline content as $o_content id as ${item_id.toString()}")
        startActivity(i)
    }
    @SuppressLint("Range")
    private fun readInDb() {
        val db = helper.readableDatabase//文件为只读
        val cursor = db.query(
            Todo.TABLE, null, null, null, null, null,
            "${Todo.COL_ID} desc "
        )
        val arr = arrayListOf<Todo>()
        if (cursor.moveToFirst()) {
            do {//遍历Cursor对象，取出数据并打印
                arr.add(//添加数据库中的元素
                    Todo(
//idx是角标，id
                        //这里需要加个判断才行,加了也不得行，所以加了这个函数上面那个注解

                        cursor.getString(cursor.getColumnIndex(Todo.COL_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(Todo.COL_OUTLINE)),
                        cursor.getLong(cursor.getColumnIndex(Todo.COL_TIME)),
                    ).apply {
                        if (cursor.getColumnIndex(Todo.COL_ID) >= 0) {
                            id = cursor.getInt(cursor.getColumnIndex(Todo.COL_ID))
                        }
                        Log.d(TAG, "id ${Todo.COL_ID},cont ${Todo.COL_CONTENT},outline ${Todo.COL_OUTLINE}")
                    }
                )

            } while (cursor.moveToNext())
        }

        adapter.setData(arr)

        cursor.close()
    }

    inner class TodoAdapter : RecyclerView.Adapter<TodoViewHolder>() {
        val cont = arrayListOf<Todo>()//这个是getItemCount重要返还的数据大小的数据

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            //在实际开发种LayoutInflater这个类还是非常有用的，它的作用类似于findViewById()，不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！而findViewById()是找具体xml下的具体widget控件(如:Button,TextView等)。
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
            return TodoViewHolder(view).apply {
                outline=view.findViewById(R.id.outline)
                content = view.findViewById(R.id.content)
                time =view.findViewById(R.id.time)
                btnUpdate = view.findViewById(R.id.btn_revise)
                btnDelete = view.findViewById(R.id.btn_delete)
            }
            //  val holder=TeacherViewHolder(view)EXP0320中的写法,
            //        return holder
            //下面是EXP0320中TeacherViewHolder中的内容，和我这个有区别在于在HOlder中就已经找好val值了而我这里是在OnCreat里才找的
            //val name=itemView.findViewById<TextView>(R.id.name)
            //         val desc=itemView.findViewById<TextView>(R.id.description)
            //         val img=itemView.findViewById<ImageView>(R.id.avater)
//这个item需要我们用inflate函数把msg_item动态的加载进main布局，
//并且返回了一个用来获取item里控件并且对其进行操作的View对象。
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            //束缚控件的位置
            holder.render(cont[position])
        }

        override fun getItemCount(): Int {
            return cont.size
        }

        fun setData(arr: ArrayList<Todo>) {
            cont.addAll(arr)
            notifyDataSetChanged()
        }

        fun replaceItem(id: Int, item: Todo) {
            val idx = findIdx(id)
            if (idx >= 0) {
                Log.d(TAG, "idx=$idx,item=$item")
                cont.set(idx, item)
                notifyItemChanged(idx)
            }
        }

        fun addItem(item: Todo) {
            cont.add(0, item)
            notifyItemInserted(0)
        }

        private fun findIdx(id: Int?): Int {
            var idx = -1
            cont.forEachIndexed() {index,todo->
                if (todo.id == id) {
                    idx = index
                }
            }
            /*  cont.forEachIndexed{ index,todo-?
            if(todo.id==id){
                idx=index
            }
        }
        return idx
    }*/
            return idx

        }

        fun itemDeleted(id: Int?) {
            val idx = findIdx(id)
            Log.d(TAG, "to idx id=${idx}")
            if (idx >= 0) {

                cont.removeAt(idx)
                notifyItemRemoved(idx)
                //  Log.d(TAG,"to idx1 id=${idx}")

                // Log.d(TAG,"to idx2 id=${idx}")
            }
        }
    }
    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var id: TextView? = null
        var outline:TextView?=null
        var content: TextView? = null
        var time:TextView?=null
        var btnUpdate: TextView? = null
        var btnDelete: TextView? = null
        fun render(todo: Todo) {
            id?.text = todo.id.toString()
            outline?.text=todo.outline
            content?.text = todo.content

            btnDelete?.setOnClickListener {
                 Log.d(TAG, "to delete id=${todo.id}")
                 val db = helper.writableDatabase

                 db.delete(Todo.TABLE, "id=?", arrayOf(todo.id.toString()))

                 adapter.itemDeleted(todo.id)


             }
             btnUpdate?.setOnClickListener {
                 Log.d(TAG, "to Update id=${todo.id}")
                 toUpdate = todo
                 ///获取outline和content参数
                 Log.d(TAG, "最初=${toUpdate}")
                 setInputText(todo.content,todo.outline,todo.id)

             }
        }
    }

}