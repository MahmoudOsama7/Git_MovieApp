package com.example.myapplication.ui.popular_movie


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.pojo.Movie
import com.example.myapplication.data.repository.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

<<<<<<< HEAD

=======
/**

 */
>>>>>>> dea620d3dae2313ae0bb9b6252fd965f55e5c972
@AndroidEntryPoint
class MovieListActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var movieAdapter :PopularMoviePagedListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setName()
        loadData()
    }

    private fun setName()
    {
        val intent=intent
        val list=intent.getStringExtra("name") as String
        viewModel.setListName(list)
    }
    private fun initView()
    {
        initAdapter()
    }
    private fun bindNetwork(it: NetworkState?) {
        movieAdapter.setNetworkState(it)
    }

    private fun binUI(it: PagedList<Movie>?) {
        movieAdapter.submitList(it)
    }

    private fun initAdapter()
    {
        val gridLayoutManager = GridLayoutManager(this, 3)
        movieAdapter = PopularMoviePagedListAdapter(this)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }
        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter
    }
    private fun loadData()
    {
        viewModel.moviePagedList.observe(this){
            binUI(it)
        }

        viewModel.networkState.observe(this){
            val pb=findViewById<ProgressBar>(R.id.progress_bar_popular)
            pb.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) {
                Log.d("MovieListDataSource", "LoadingData ")
                View.VISIBLE
            }else
            {
                View.GONE
            }
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            bindNetwork(it)
        }
    }
}
