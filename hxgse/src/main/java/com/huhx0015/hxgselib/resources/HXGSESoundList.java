package com.huhx0015.hxgselib.resources;

import android.util.Log;
import com.huhx0015.hxgselib.R;
import com.huhx0015.hxgselib.model.HXGSESoundFX;
import java.util.LinkedList;

/** -----------------------------------------------------------------------------------------------
 *  [HXGSESoundList] CLASS
 *  PROGRAMMER: Michael Yoon Huh (Huh X0015)
 *  DESCRIPTION: This class is responsible for providing methods to return the list of sound effects
 *  available for playback in this application.
 *  -----------------------------------------------------------------------------------------------
 */

public class HXGSESoundList {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private static final String TAG = HXGSESoundList.class.getSimpleName(); // Used for logging output to logcat.

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    // hxgseSoundList(): Creates and returns an LinkedList of sound effects.
    public static LinkedList<HXGSESoundFX> hxgseSoundList() {

        LinkedList<HXGSESoundFX> soundList = new LinkedList<>(); // Creates a new LinkedList<Integer> object.

        // Adds the HXGSESoundFX objects to the LinkedList.
        // TODO: Define your sound effects and associated resources here.

        Log.d(TAG, "INITIALIZATION: List of sound effects has been constructed successfully.");

        return soundList;
    }
}
