package serverside.example.com.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView movie_name,plot,userrating,release;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        android.support.v7.widget.Toolbar toolbar=( android.support.v7.widget.Toolbar ) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    initCollapsingToolbar();

    imageView=(ImageView)findViewById(R.id.thumbnail_image_header);
    movie_name=(TextView)findViewById(R.id.movie_title);
    plot=(TextView)findViewById(R.id.plot_synposis);
    userrating=(TextView)findViewById(R.id.userrating);
    release=(TextView)findViewById(R.id.release_date);

        Intent newIntent=getIntent();
        if(newIntent.hasExtra("original_title"))
        {
            String thumbnail=getIntent().getExtras().getString("poster_path");
            String moviename=getIntent().getExtras().getString("original_title");
            String synopsis=getIntent().getExtras().getString("overview");
            String rating=getIntent().getExtras().getString("vote_average");
            String dateOfRelease=getIntent().getExtras().getString("release_date");

            Glide.with(this)
                    .load(thumbnail)
                    .into(imageView);

            movie_name.setText(moviename);
            plot.setText(synopsis);
            userrating.setText(rating);
            release.setText(dateOfRelease);

        }

            else
            Toast.makeText(this,"NO API Found!!",Toast.LENGTH_SHORT).show();

        }

        private  void initCollapsingToolbar()
        {
            final CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(" ");
            AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.appbar);
            appBarLayout.setExpanded(true);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow=false;
                int scrollrange=-1;
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if(scrollrange == -1)

                    {scrollrange=appBarLayout.getTotalScrollRange();

                    }

                    if(scrollrange +verticalOffset==0)
                    {
                        collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                        isShow=true;

                    }

                    else if(isShow)
                    {
                        collapsingToolbarLayout.setTitle(" ");
                        isShow=false;

                    }

                }
            });
        }
}
