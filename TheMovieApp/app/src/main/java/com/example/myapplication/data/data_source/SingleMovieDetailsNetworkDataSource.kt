package com.example.myapplication.data.data_source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.pojo.MovieDetails
import com.example.myapplication.data.repository.NetworkState
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class SingleMovieDetailsNetworkDataSource {


    private lateinit var observable: Single<MovieDetails>

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedMovieDetailsResponse =  MutableLiveData<MovieDetails>()
    val downloadedMovieResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(id:Int) {

        _networkState.postValue(NetworkState.LOADING)
         //observable  =getMovieDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        _networkState.postValue(NetworkState.LOADING)

        //creating the observer that will subscribe to the observable as to take all the data from this observable
        val observer = object : SingleObserver<MovieDetails> {
            override fun onSubscribe(d: Disposable?) {
                Log.d("MovieDetailsDataSource", "onSubscribe: ")
            }

            override fun onSuccess(value: MovieDetails) {
                _downloadedMovieDetailsResponse.postValue(value)
                _networkState.postValue(NetworkState.LOADED)
            }

            override fun onError(e: Throwable?) {
                Log.d("MovieDetailsDataSource",e?.message!!)
                _networkState.postValue(NetworkState.ERROR)
            }
        }
        observable.subscribe(observer)
    }
}