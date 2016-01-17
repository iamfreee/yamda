package isel.pdm.yamda.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import isel.pdm.yamda.R;
import isel.pdm.yamda.ui.activity.base.ToolbarActivity;
import isel.pdm.yamda.ui.fragment.MovieDetailsFragment;

/**
 * Activity to display the movie details
 */
public class MovieCreditsActivity extends ToolbarActivity {


    public static final String ID_TAG = "movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int movieId = getIntent().getIntExtra(ID_TAG, 0);
        this.enableBackButton();

        // create and add the fragment
        MovieDetailsFragment firstFragment = new MovieDetailsFragment();
        Bundle b = new Bundle();
        b.putInt(ID_TAG, movieId);
        firstFragment.setArguments(b);

        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.content, firstFragment).commit();
    }

    /**
     * Create the intent to navigate to this activity
     * @param context
     * @param id
     * @return
     */
    public static Intent createIntent(Context context, int id) {
        Intent intent = new Intent(context, MovieCreditsActivity.class);
        intent.putExtra(ID_TAG, id);

        return intent;
    }
}
