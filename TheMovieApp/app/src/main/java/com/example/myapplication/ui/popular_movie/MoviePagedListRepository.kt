package com.example.myapplication.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.myapplication.data.api.TheMovieDBClient.POSTS_PER_PAGE
import com.example.myapplication.data.data_source.movie_list.MovieDataSourceFactory
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.Movie
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MoviePagedListRepository
@Inject
constructor(private var movieDataSourceFactory: MovieDataSourceFactory){

    private var compositeDisposable:CompositeDisposable=CompositeDisposable()
    private lateinit var moviePagedList: LiveData<PagedList<Movie>>

    fun fetchLiveMoviePagedList (listName:String) : LiveData<PagedList<Movie>>
    {
        movieDataSourceFactory.getData(listName,compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POSTS_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return movieDataSourceFactory._networkState
    }
    fun clearComposite()
    {
        movieDataSourceFactory.clearComposite()
    }
    fun getData(compositeDisposable: CompositeDisposable){
        this.compositeDisposable=compositeDisposable
    }

}