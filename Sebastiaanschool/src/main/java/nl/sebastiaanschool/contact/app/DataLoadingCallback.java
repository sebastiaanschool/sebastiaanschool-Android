package nl.sebastiaanschool.contact.app;

/**
 * Used by the Parse backend, ignored by the stub backend.
 * Created by barend on 3-11-13.
 */
public interface DataLoadingCallback {
    /**
     * Invoked when loading starts.
     */
    void onStartLoading();

    /**
     * Invoked when loading stops.
     * @param e nullable. Only set if the loading failed.
     */
    void onStopLoading(Exception e);
}
