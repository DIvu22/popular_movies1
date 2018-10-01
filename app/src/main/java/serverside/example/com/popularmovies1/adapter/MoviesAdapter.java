package serverside.example.com.popularmovies1.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import serverside.example.com.popularmovies1.DetailActivity;
import serverside.example.com.popularmovies1.Model.Movie;
import serverside.example.com.popularmovies1.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context context;
    private List<Movie> movieList;

     public MoviesAdapter(Context context,List<Movie> movieList)
     {
         this.context=context;
         this.movieList=movieList;
     }

    @NonNull
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card,parent,false);

        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder holder, int i) {
         holder.title.setText(movieList.get(i).getOriginalTitle());
         String vote=Double.toString(movieList.get(i).getVoteAverage());
         holder.userrating.setText(vote);

        Glide.with(context)
                .load(movieList.get(i).getPosterPath())
                .into(holder.thumbnail);


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public  class  MyViewHolder extends  RecyclerView.ViewHolder{

         private TextView title,userrating;
         private  ImageView thumbnail;

         public  MyViewHolder(View view)
         {
             super(view);
             title=(TextView) view.findViewById(R.id.title);
             userrating=(TextView) view.findViewById(R.id.userrating);
             thumbnail=(ImageView) view.findViewById(R.id.thumbnail);

             view.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     int pos= getAdapterPosition();
                     if(pos!=RecyclerView.NO_POSITION)
                     {
                         Movie clicked=movieList.get(pos);
                         Intent intent=new Intent(context, DetailActivity.class);
                         intent.putExtra("original_title",movieList.get(pos).getOriginalTitle());
                         intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                         intent.putExtra("overview",movieList.get(pos).getOverview());
                         intent.putExtra("vote_average",Double.toString(movieList.get(pos).getVoteAverage()));
                         intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         context.startActivity(intent);

                         Toast.makeText(view.getContext(),"You clicked"+ clicked.getOriginalTitle(),Toast.LENGTH_SHORT).show();

                     }
                 }
             });




         }
    }
}
