/*
 Copyright (c) 2014 Barend Garvelink

 SebastiaanSchool is licensed under a
 Creative Commons Attribution-NonCommercial 3.0 Unported License.

 You should have received a copy of the license along with this
 work.  If not, see <http://creativecommons.org/licenses/by-nc/3.0/>.
 */
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
