package com.example.myapplication.ui.single_movie_details


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.MovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
@HiltViewModel
class SingleMovieViewModel
    @Inject
    constructor(private val singleMovieRepository : SingleMovieRepository):ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private var id=0

    fun setMovieID(id:Int)
    {
        this.id=id
    }

    val  movieDetails : LiveData<MovieDetails> by lazy {
        singleMovieRepository.fetchSingleMovieDetails(id)
    }

    val networkState : LiveData<NetworkState> by lazy {
        singleMovieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}