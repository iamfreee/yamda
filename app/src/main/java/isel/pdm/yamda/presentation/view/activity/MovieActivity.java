package isel.pdm.yamda.presentation.view.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import isel.pdm.yamda.R;
import isel.pdm.yamda.data.handlers.receiver.NotificationPublisher;
import isel.pdm.yamda.model.entity.Genre;
import isel.pdm.yamda.model.entity.MovieDetails;
import isel.pdm.yamda.presentation.presenter.MovieViewPresenter;
import isel.pdm.yamda.presentation.view.activity.base.AbstractBaseActivity;

/**
 * Activity to display the movie details
 */
public class MovieActivity extends AbstractBaseActivity {

    public interface FollowListener {
        void setFollow(int movieId, boolean value);
    }

    public static final String ID_TAG = "movie_id";

    private View movieView;
    private View loadingView;

    private AlarmManager alarmManager;

    private MovieDetails movie;
    private Boolean isBeingFollowed;

    // Receiver to be activated after a certain time to instantiate notification in argument
    private Intent notificationIntent;
    private FollowListener followListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.movie_layout);

        this.movieView = this.findViewById(R.id.movie_view);
        this.loadingView = this.findViewById(R.id.loading_movie);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        this.notificationIntent = new Intent(this, NotificationPublisher.class);

        this.presenter = new MovieViewPresenter(this, getIntent().getExtras().getInt(ID_TAG));

        this.checkFollow(findViewById(R.id.follow));

        this.setUpSupportActionBar();
    }

    /**
     * Make the back button close this activity
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void checkFollow(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBeingFollowed) {
                    showToastMessage("You are no longer following this movie");
                    cancelNotification();
                } else {
                    showToastMessage("You are now following this movie");
                    scheduleNotification(getNotificationReleased());
                }
                isBeingFollowed = !isBeingFollowed;
                if (followListener != null) {
                    followListener.setFollow(movie.getId(), isBeingFollowed);
                }
            }
        });
    }

    private void cancelNotification() {
        alarmManager.cancel(PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT));
        PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT).cancel();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void scheduleNotification(Notification notification) {
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, movie.getId());  // Movie id
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);      // Notification (argument)

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        long futureInMillis = movie.whenIsBeingReleased();
//        long futureInMillis = SystemClock.elapsedRealtime() + 5000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);  // Set an alarm to tick at x time
        //        Log.v("DEBUG", String.valueOf(new Date(Calendar.getInstance().getTime().getTime() + futureInMillis)));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotificationReleased() {
        Intent intent = new Intent(this, MovieActivity.class);      // Activity instantiated after clicking notification
        intent.putExtra(MovieActivity.ID_TAG, movie.getId());       // id of movie
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        return new Notification.Builder(this)
                .setContentTitle(movie.getTitle())                              // Title of notification
                .setContentText(getResources().getString(R.string.movie_released))    // Text of notification
                .setSmallIcon(R.drawable.yamda)                                 // Icon of notification
                .setContentIntent(pIntent).build();
    }


    public View getMovieView() {
        return movieView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setFollowedState(Boolean state) {
        this.isBeingFollowed = state;
    }

    public void setFollowListener(FollowListener followListener) {
        this.followListener = followListener;
    }

    public void updateView(MovieDetails data) {
        this.movie = data;

        ImageView backdropView = (ImageView) findViewById(R.id.backDropPath);
        ImageView imageView = (ImageView) findViewById(R.id.cover);
        TextView title = (TextView) findViewById(R.id.title);
        TextView genre = (TextView) findViewById(R.id.genre);
        TextView rating = (TextView) findViewById(R.id.tagline);
        TextView runtime = (TextView) findViewById(R.id.runtime);
        TextView releaseYear = (TextView) findViewById(R.id.release_date);
        TextView overview = (TextView) findViewById(R.id.overview);


        Picasso.with(this).load(movie.getBackdrop()).into(backdropView);
        Picasso.with(this).load(movie.getPoster()).into(imageView);
        title.setText(movie.getTitle());
        genre.setText(createGenreText(movie.getGenres()));
        rating.setText(this.getString(R.string.row_rating, movie.getRating()));
        runtime.setText(createRuntimeText(movie.getRuntime()));
        releaseYear.setText(this.getString(R.string.row_released, movie.getRelease_date()));
        overview.setText(movie.getOverview());
        if (movie.whenIsBeingReleased() > 0) {
            findViewById(R.id.follow).setVisibility(View.VISIBLE);
            ((ToggleButton) findViewById(R.id.follow)).setChecked(isBeingFollowed);
        }
    }

    /**
     * @param runtime minutes
     * @return
     */
    private String createRuntimeText(int runtime) {
        int hours = runtime / 60;
        int minutes = runtime % 60;
        return hours + "h " + minutes + "m";
    }

    private String createGenreText(Genre[] genres) {
        StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < genres.length; i++) {
            stringbuilder.append(genres[i].getName());
            if (i < genres.length - 1) {
                stringbuilder.append(", ");
            }
        }
        return stringbuilder.toString();
    }

    public static Intent createIntent(Context context, int id) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(ID_TAG, id);

        return intent;
    }
}
