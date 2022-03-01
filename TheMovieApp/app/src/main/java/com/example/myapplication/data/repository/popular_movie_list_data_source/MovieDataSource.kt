package com.example.myapplication.data.repository.popular_movie_list_data_source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.myapplication.data.api.TheMovieDBClient.Companion.FIRST_PAGE
import com.example.myapplication.data.api.TheMovieDBClient.Companion.getPopularMovieList
import com.example.myapplication.data.api.TheMovieDBClient.Companion.getTopRatedMovieList
import com.example.myapplication.data.api.TheMovieDBClient.Companion.getUpcomingMovieList
import com.example.myapplication.data.pojo.Movie
import com.example.myapplication.data.pojo.MovieResponse
import com.example.myapplication.data.repository.NetworkState
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val listName:String,private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Movie>(){

    private var page = FIRST_PAGE
    private var observable1:Single<MovieResponse> ?=null
    private var observable2:Single<MovieResponse> ?=null
    private var observable3:Single<MovieResponse> ?=null
    private lateinit var observer:SingleObserver<MovieResponse>

    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val _networkState:LiveData<NetworkState> = networkState



    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {

        networkState.postValue(NetworkState.LOADING)
        when(listName)
        {
            "popular" ->observable1=getPopularMovieList(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            "upcoming"->observable2=getUpcomingMovieList(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            "top_rated"->observable3=getTopRatedMovieList(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
        observer = object:SingleObserver<MovieResponse> {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                Log.d("MovieDetailsDataSource", "onSubscribe:")
            }

            override fun onSuccess(value: MovieResponse) {
                callback.onResult(value.movieList, null, page+1)
                networkState.postValue(NetworkState.LOADED)
                Log.e("MovieDataSource","osos")
            }

            override fun onError(e: Throwable) {
                networkState.postValue(NetworkState.ERROR)
                Log.e("MovieDataSource", e.message!!)
            }
        }
        observable1?.subscribe(observer)
        observable2?.subscribe(observer)
        observable3?.subscribe(observer)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        when(listName)
        {
            "popular" ->observable1=getPopularMovieList(params.key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            "upcoming"->observable2=getUpcomingMovieList(params.key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            "top_rated"->observable3=getTopRatedMovieList(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
        observer = object:SingleObserver<MovieResponse>{
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
                Log.d("MovieDetailsDataSource", "onSubscribe: ")
            }

            override fun onSuccess(value: MovieResponse?) {
                if(value!!.totalPages >= params.key) {
                    callback.onResult(value.movieList, params.key+1)
                    Log.d("3ash", "onSuccess: "+value.movieList.size)
                    networkState.postValue(NetworkState.LOADED)
                }
                else{
                    networkState.postValue(NetworkState.ENDOFLIST)
                }
            }

            override fun onError(e: Throwable) {
                networkState.postValue(NetworkState.ERROR)
                Log.e("MovieDataSource", e.message!!)
            }
        }
        observable1?.subscribe(observer)
        observable2?.subscribe(observer)
        observable3?.subscribe(observer)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }

    fun removeObservables()
    {
        compositeDisposable.clear()
    }

}