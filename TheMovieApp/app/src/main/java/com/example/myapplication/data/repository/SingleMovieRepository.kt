package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.data_source.single_movie.SingleMovieDetailsNetworkDataSource
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.MovieDetails
import javax.inject.Inject

class SingleMovieRepository
@Inject
constructor(private var singleMovieDetailsNetworkDataSource: SingleMovieDetailsNetworkDataSource)
{

    fun fetchSingleMovieDetails (id:Int) : LiveData<MovieDetails> {
        singleMovieDetailsNetworkDataSource.fetchMovieDetails(id)
        return singleMovieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return singleMovieDetailsNetworkDataSource.networkState
    }
    fun clearComposite(){
        singleMovieDetailsNetworkDataSource.removeObservables()
    }
}