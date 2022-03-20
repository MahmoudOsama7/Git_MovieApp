package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.myapplication.R
import com.example.myapplication.ui.popular_movie.MovieListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private var getSelectedData="popular"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setTheAutoCompleteTextView()
        takeTheDataFromTheAutoCompleteTextView()
    }
    private fun setTheAutoCompleteTextView()
    {
        val arrayList=ArrayList<String>()
        arrayList.add("latest")
        arrayList.add("now_playing")
        arrayList.add("popular")
        arrayList.add("top_rated")
        arrayList.add("upcoming")
        autoCompleteTextView=findViewById(R.id.attv)
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList)
        autoCompleteTextView.setAdapter(adapter)
    }
    private fun takeTheDataFromTheAutoCompleteTextView()
    {
        autoCompleteTextView.setOnItemClickListener{ parent, view, position, id ->
            getSelectedData=parent.getItemAtPosition(position) as String
            val intent = Intent(this, MovieListActivity::class.java)
            intent.putExtra("name",getSelectedData)
            startActivity(intent)
        }
    }

}