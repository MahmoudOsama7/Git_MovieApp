package com.example.myapplication.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.repository.NetworkState
import com.example.myapplication.data.pojo.Movie
import com.example.myapplication.R
import com.example.myapplication.data.api.TheMovieDBClient.Companion.POSTER_BASE_URL
import com.example.myapplication.ui.single_movie_details.SingleMovie


class PopularMoviePagedListAdapter(private val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

     val MOVIE_VIEW_TYPE = 1
     val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(holder,context,getItem(position))
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(holder,networkState)
        }
    }

    private fun hasExtraRow(): Boolean {

        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }


    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private  var movieTitle : TextView = itemView.findViewById(R.id.cv_movie_title)
        private  var movieRelease:TextView = itemView.findViewById(R.id.cv_movie_release_date)
        private var moviePoster:ImageView = itemView.findViewById(R.id.cv_iv_movie_poster)

        fun bind(holder: MovieItemViewHolder, context: Context,movie:Movie?) {

            holder.movieTitle.text=movie?.title
            holder.movieRelease.text=movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(holder.moviePoster)

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
                //Toast.makeText(context, movie?.id.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private var progressBar:ProgressBar=itemView.findViewById(R.id.progress_bar_item)
        private var txtError:TextView=itemView.findViewById(R.id.error_msg_item)

        fun bind(holder:NetworkStateItemViewHolder,networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                holder.progressBar.visibility= View.VISIBLE
            }
            else  {
                holder.progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                holder.txtError.visibility = View.VISIBLE
                holder.txtError.text = networkState.msg
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                holder.txtError.visibility = View.VISIBLE
                holder.txtError.text = networkState.msg
            }
            else {
                holder.txtError.visibility = View.GONE
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState?) {
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && hadExtraRow) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }




}