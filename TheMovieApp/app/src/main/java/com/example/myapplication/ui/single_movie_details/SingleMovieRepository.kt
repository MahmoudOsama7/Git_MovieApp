package com.example.myapplication.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.example.myapplication.data.repository.SingleMovieDetailsNetworkDataSource
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.MovieDetails

class SingleMovieRepository () {

    lateinit var singleMovieDetailsNetworkDataSource: SingleMovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (id:Int) : LiveData<MovieDetails> {

        singleMovieDetailsNetworkDataSource = SingleMovieDetailsNetworkDataSource()
        singleMovieDetailsNetworkDataSource.fetchMovieDetails(id)
        return singleMovieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return singleMovieDetailsNetworkDataSource.networkState
    }
}