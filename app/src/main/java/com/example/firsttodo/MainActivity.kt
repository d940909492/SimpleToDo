package com.example.firsttodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var tasklist = mutableListOf<String>()
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : Adapter.OnlongClickLister{
            override fun onItemLongClicked(position: Int) {
                // 1. remove item from the list
                tasklist.removeAt(position)

                // 2. notify the adapter that data set has been change
                adapter.notifyDataSetChanged()

                saveitem()
            }

        }

        loaditem()

        tasklist.add("*if you want delete something, just pressing and holding the text that you want to delete")
        tasklist.add("Write down what ever u want to do")


        //look up recyclerview in layout
        val Recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        // Create adapter passing in the sample user data
        adapter = Adapter(tasklist, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        Recyclerview.adapter = adapter
        // Set layout manager to position the items
        Recyclerview.layoutManager = LinearLayoutManager(this)

        //set up the button and input field, so the user can enter something and add to the list

        val inputtextfield = findViewById<EditText>(R.id.addtaskfield)

        // 1.get references to the button
        // 2.set an onclicklister
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. grab the text the user has inputted into addtaskfield(id)
            val userinput = inputtextfield.text.toString()

            // 2.add the string to list of tasks - tasklist
            tasklist.add(userinput)
            adapter.notifyItemChanged(tasklist.size - 1)// notify the adapter that data has been update

            // 3. reset the field
            inputtextfield.setText("")

            saveitem()
        }
    }

    //save the data
    //save data by writing and reading from a file
    //get the file we need
    fun datafile():File{
        return File(filesDir, "data.txt")
    }
    //Load the items by reading every line in the data file
    fun loaditem() {
        try {
            tasklist = FileUtils.readLines(datafile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //save item by writing thenm into data file
    fun saveitem() {
        try {
            FileUtils.writeLines(datafile(), tasklist)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
