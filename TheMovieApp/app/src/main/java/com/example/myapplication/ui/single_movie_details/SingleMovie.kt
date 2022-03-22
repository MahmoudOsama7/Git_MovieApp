package com.example.myapplication.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.api.Constants.POSTER_BASE_URL


import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.MovieDetails
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*
@AndroidEntryPoint
class SingleMovie : AppCompatActivity() {


    private val viewModel: SingleMovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        getFullMovieDetails()
        loadData()
    }

    private fun loadData() {
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })
        viewModel.networkState.observe(this, Observer {
            bindNetwork(it)
        })
    }

    private fun bindNetwork(it: NetworkState?) {
        progress_bar.visibility = if (it == NetworkState.LOADING)
        {
            Log.d("SingleMovieDataSource", "LoadingData ")
            View.VISIBLE
        } else
        {
            Log.d("SingleMovieDataSource", "DataLoaded ")
            View.GONE
        }
        txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
    }


    private fun bindUI(it: MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview
        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)
        val moviePosterURL = POSTER_BASE_URL+ it.posterPath
        Glide.with(this).load(moviePosterURL).into(iv_movie_poster)
    }
    private fun getFullMovieDetails()
    {
        val intent = intent
        val id=intent.getIntExtra("id",0)
        viewModel.setMovieID(id)
    }
}
