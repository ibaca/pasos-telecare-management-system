
package org.inftel.tms.mobile.ui;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.ui.fragments.FenceDetailFragment;
import org.inftel.tms.mobile.ui.fragments.FenceListFragment;
import org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory;
import org.inftel.tms.mobile.util.base.ILastLocationFinder;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FencesActivity extends FragmentActivity {

    private static final String TAG = "FencesActivity";

    protected FenceListFragment fenceListFragment;

    private FenceDetailFragment fenceDetailFragment;

    private ILastLocationFinder lastLocationFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        setContentView(R.layout.fences_container);

        // Get a handle to the Fragments
        fenceListFragment = (FenceListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fences_list_fragment);

        // Last location helper
        lastLocationFinder = PlatformSpecificImplementationFactory.getLastLocationFinder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fences_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.fencesAdd:
                // Create new fence and show details
                Log.i(TAG, "boton crear fence pulsado");
                selectDetail(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Updates (or displays) the venue detail Fragment when a venue is selected
     * (normally by clicking a place on the Place List.
     * 
     * @param reference Place Reference
     * @param id Place Identifier
     */
    public void selectDetail(String fenceUri) {
        fenceDetailFragment = FenceDetailFragment.newInstance(fenceUri);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (findViewById(R.id.fences_container) != null) {
            ft.addToBackStack(null);
            ft.hide(fenceListFragment);
            ft.replace(R.id.fences_container, fenceDetailFragment);
            ft.show(fenceDetailFragment);
            ft.commit();
        } else {
            ft.disallowAddToBackStack();
            ft.replace(R.id.fences_details_fragment, fenceDetailFragment);
            ft.commit();
        }
    }

    /**
     * Busca la ultima posicion conocida (usando {@link ILastLocationFinder}).
     * Find the last known location (using a {@link LastLocationFinder}) and
     * updates the place list accordingly.
     * 
     * @param updateWhenLocationChanges Request location updates
     */
    protected void getLastLocation(int maxDistance, int maxTime) {
        // This isn't directly affecting the UI, so put it on a worker thread.
        final AsyncTask<Void, Void, Location> findLationTask = new AsyncTask<Void, Void, Location>() {
            @Override
            protected Location doInBackground(Void... params) {
                Location lastKnownLocation = lastLocationFinder.getLastBestLocation(
                        TmsConstants.MAX_DISTANCE,
                        System.currentTimeMillis() - TmsConstants.MAX_TIME);
                return lastKnownLocation;
            }

            protected void onPostExecute(Location result) {
                // Aqui se puede actualizar el IU
            }
        };
        findLationTask.execute();
    }
}
