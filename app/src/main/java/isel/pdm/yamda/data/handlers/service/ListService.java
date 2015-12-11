package isel.pdm.yamda.data.handlers.service;

import android.app.IntentService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public abstract class ListService extends IntentService {

    public final String TAG = "DEBUG_" + getClass().getSimpleName();

    protected static final String PAGE = ListService.class.getName() + ".id";

    public static final String DATA = "data_ok";

    public static final String MOVIES_PARAM = "movies_parameter";

    public ListService() {
        super("MovieListService");
    }
}
