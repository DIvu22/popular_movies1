package serverside.example.com.popularmovies1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import serverside.example.com.popularmovies1.Api.ApiClient;
import serverside.example.com.popularmovies1.Api.ApiInterface;
import serverside.example.com.popularmovies1.Model.Movie;
import serverside.example.com.popularmovies1.Model.MoviesResponse;
import serverside.example.com.popularmovies1.adapter.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
     private ProgressDialog pd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static  final String LOG_TAG=MoviesAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initviews();
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.main_content);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_orange_dark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initviews();
                Toast.makeText(MainActivity.this,"Movies Refreshed",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Activity getActivity()
    {
        Context context=this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ( Activity ) context;
            }
            context = (( ContextWrapper ) context).getBaseContext();
        }
        return  null;

        }

    private void initviews()
    {
        pd= new ProgressDialog(this);
        pd.setMessage("Fetching Movies...");
        pd.setCancelable(false);
        pd.show();

        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        movieList=new ArrayList<>();
        adapter=new MoviesAdapter(this,movieList);

        if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }

        else
        {
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

           checkSortOrder();
    }

    private  void  loadJSON()
    {
       try
       {
           if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty())
           {
               Toast.makeText(getApplicationContext(),"Please firstly obtain API key from themoviedb.org",Toast.LENGTH_SHORT).show();
               pd.dismiss();
               return;
           }
           ApiClient client= new ApiClient();
           ApiInterface apiInterface= client.getClient().create(ApiInterface.class);
           Call<MoviesResponse>call=apiInterface.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
           call.enqueue(new Callback<MoviesResponse>() {
               @Override
               public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                   List <Movie> movies=response.body().getResults();
                   recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
                   recyclerView.smoothScrollToPosition(0);
                   if(swipeRefreshLayout.isRefreshing())
                   {
                       swipeRefreshLayout.setRefreshing(false);
                   }
                   pd.dismiss();
               }

               @Override
               public void onFailure(Call<MoviesResponse> call, Throwable t) {
                   Log.d("Error",t.getMessage());
                   Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();

               }
           });
       } catch (Exception e)
       {
           Log.d("error",e.getMessage());
           Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
       }

    }
    private  void  loadJSON1()
    {
        try
        {
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Please firstly obtain API key from themoviedb.org",Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            ApiClient client= new ApiClient();
            ApiInterface apiInterface= client.getClient().create(ApiInterface.class);
            Call<MoviesResponse>call=apiInterface.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List <Movie> movies=response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeRefreshLayout.isRefreshing())
                    {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error",t.getMessage());
                    Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e)
        {
            Log.d("error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG,"Preferences updated");
        checkSortOrder();
    }

    private void checkSortOrder() {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder=preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if(sortOrder.equals(this.getString(R.string.pref_most_popular)))
        {
            loadJSON();
        }

        else
        {
            loadJSON1();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(movieList.isEmpty()){
            checkSortOrder();
        }
        else
        {

        }

    }
}
