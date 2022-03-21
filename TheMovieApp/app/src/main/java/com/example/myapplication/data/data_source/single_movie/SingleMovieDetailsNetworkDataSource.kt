package com.example.myapplication.data.data_source.single_movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.api.TheMovieDBInterface
import com.example.myapplication.data.di.MovieRepository
import com.example.myapplication.data.pojo.MovieDetails
import com.example.myapplication.data.repository.NetworkState
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SingleMovieDetailsNetworkDataSource
@Inject
constructor(private var movieRepository: MovieRepository,private var compositeDisposable: CompositeDisposable)
{
    private lateinit var observable: Single<MovieDetails>

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> get() = _networkState

    private val _downloadedMovieDetailsResponse =  MutableLiveData<MovieDetails>()
    val downloadedMovieResponse: LiveData<MovieDetails> get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(id:Int) {
        _networkState.postValue(NetworkState.LOADING)
         observable  =movieRepository.getMovieDetails(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        _networkState.postValue(NetworkState.LOADING)

        //creating the observer that will subscribe to the observable as to take all the data from this observable
        val observer = object : SingleObserver<MovieDetails> {
            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
                Log.d("SingleMovieDataSource", "CompositeDisposable Is Initialized ")
            }

            override fun onSuccess(value: MovieDetails) {
                _downloadedMovieDetailsResponse.postValue(value)
                _networkState.postValue(NetworkState.LOADED)
                Log.d("SingleMovieDataSource", "Data Received Successfully ")
            }

            override fun onError(e: Throwable?) {
                Log.d("SingleMovieDataSource", "Error When Receiving The Data")
                _networkState.postValue(NetworkState.ERROR)
            }
        }
        observable.subscribe(observer)
    }
    fun removeObservables()
    {
        compositeDisposable.clear()
        Log.d("SingleMovieDataSource", "CompositeDisposable is Cleared ")
    }
}