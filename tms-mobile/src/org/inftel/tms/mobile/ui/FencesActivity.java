
package org.inftel.tms.mobile.ui;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.ui.fragments.FenceDetailFragment;
import org.inftel.tms.mobile.ui.fragments.FenceListFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout
        setContentView(R.layout.preferences_fences);

        // Get a handle to the Fragments
        fenceListFragment = (FenceListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fences_list_fragment);
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

}
