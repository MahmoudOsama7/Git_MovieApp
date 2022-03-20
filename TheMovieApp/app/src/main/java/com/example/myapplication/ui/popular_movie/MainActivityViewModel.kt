package com.example.myapplication.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
@HiltViewModel
class MainActivityViewModel
@Inject
constructor(private val movieRepository:MoviePagedListRepository): ViewModel()
{

    private val compositeDisposable = CompositeDisposable()

    private var listName:String="popular"

    fun setListName(listName:String)
    {
        this.listName=listName
    }

    val  moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(listName)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
    fun clearComposite()
    {
        movieRepository.getData(compositeDisposable)
        movieRepository.clearComposite()
    }

}