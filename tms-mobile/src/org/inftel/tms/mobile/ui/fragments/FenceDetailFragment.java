
package org.inftel.tms.mobile.ui.fragments;

// TODO Create a richer UI to display places Details. This should include images,
// TODO ratings, reviews, other people checked in here, etc.

import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_LOCATION_LAT;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_LOCATION_LNG;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.contentproviders.FencesContentProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * UI Fragment to display the details for a selected venue.
 */
public class FenceDetailFragment extends Fragment implements LoaderCallbacks<Cursor> {

    /**
     * Factory that produces a new {@link FenceDetailFragment} populated with
     * details corresponding to the reference / ID of the venue passed in.
     * 
     * @param reference Venue Reference
     * @param id Venue Unique ID
     * @return {@link FenceDetailFragment}
     */
    public static FenceDetailFragment newInstance(String fenceUri) {
        FenceDetailFragment fragment = new FenceDetailFragment();

        // Supply reference and ID inputs as arguments.
        Bundle args = new Bundle();
        args.putString(TmsConstants.ARGUMENTS_CONTENT_URI, fenceUri);
        fragment.setArguments(args);

        return fragment;
    }

    protected static String TAG = "FenceDetailFragment";
    protected Handler handler = new Handler();
    protected Activity activity;
    private ContentResolver contentResolver;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText radiusEditText;
    private RadioGroup typeRadioGroup;
    private Button saveButton;
    private EditText nameEditText;
    private Uri fenceUri;
    private RadioButton typeRadioInclusive;

    public FenceDetailFragment() {
        super();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        contentResolver = activity.getContentResolver();

        // Query the PlacesDetails Content Provider using a Loader to find
        // the details for the selected venue.
        if (fenceUri != null) {
            getLoaderManager().initLoader(0, null, this);
        }

        // Query the Shared Preferences to find the ID of the last venue checked
        // in to.
        // SharedPreferences sp = activity.getSharedPreferences(
        // PlacesConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        // String lastCheckin =
        // sp.getString(PlacesConstants.SP_KEY_LAST_CHECKIN_ID, null);
        // if (lastCheckin != null)
        // checkedIn(lastCheckin);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preferences_fences_detail, container, false);
        nameEditText = (EditText) view.findViewById(R.id.fence_detail_name);
        latitudeEditText = (EditText) view.findViewById(R.id.detailLat);
        longitudeEditText = (EditText) view.findViewById(R.id.detailLon);
        radiusEditText = (EditText) view.findViewById(R.id.detailRadius);
        typeRadioGroup = (RadioGroup) view.findViewById(R.id.detailType);
        typeRadioInclusive = (RadioButton) view.findViewById(R.id.detailTypeInclusive);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(saveButtonOnClickListener);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.get(TmsConstants.ARGUMENTS_CONTENT_URI) != null) {
            fenceUri = Uri.parse(getArguments().getString(TmsConstants.ARGUMENTS_CONTENT_URI));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
         * Always refresh the details on resume, but don't force a refresh to
         * minimize the network usage. Forced updates are unnecessary as we
         * force an update when a venue is selected in the Place List Activity.
         */
        if (fenceUri != null)
            updatePlace(fenceUri, false);
    }

    /**
     * Start the {@link PlaceDetailsUpdateService} to refresh the details for
     * the selected venue.
     * 
     * @param reference Reference
     * @param id Unique Identifier
     * @param forceUpdate Force an update
     */
    protected void updatePlace(Uri fenceUri, boolean forceUpdate) {
        if (fenceUri != null) {
            // Start the PlaceDetailsUpdate Service to query the server for
            // details
            // on the specified venue. A "forced update" will ignore the caching
            // latency
            // rules and query the server.
            // Intent updateServiceIntent = new Intent(activity,
            // PlaceDetailsUpdateService.class);
            // updateServiceIntent.putExtra(PlacesConstants.EXTRA_KEY_REFERENCE,
            // reference);
            // updateServiceIntent.putExtra(PlacesConstants.EXTRA_KEY_ID, id);
            // updateServiceIntent.putExtra(PlacesConstants.EXTRA_KEY_FORCEREFRESH,
            // forceUpdate);
            // activity.startService(updateServiceIntent);
        }
    }

    /**
     * {@inheritDoc} Query the {@link PlaceDetailsContentProvider} for the
     * Phone, Address, Rating, Reference, and Url of the selected venue. TODO
     * Expand the projection to include any other details you are recording in
     * the Place Detail Content Provider.
     */
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                FencesContentProvider.KEY_NAME,
                FencesContentProvider.KEY_LOCATION_LAT,
                FencesContentProvider.KEY_LOCATION_LNG,
                FencesContentProvider.KEY_RADIUS,
                FencesContentProvider.KEY_ZONE_TYPE,
        };
        return new CursorLoader(activity, fenceUri, projection, null, null, null);
    }

    /**
     * {@inheritDoc} When the Loader has completed, schedule an update of the
     * Fragment UI on the main application thread.
     */
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            final String name = data.getString(data.getColumnIndex(FencesContentProvider.KEY_NAME));
            final float latitude = data.getFloat(data.getColumnIndex(KEY_LOCATION_LAT));
            final float longitude = data.getFloat(data.getColumnIndex(KEY_LOCATION_LNG));
            final int radius = data.getInt(data.getColumnIndex(FencesContentProvider.KEY_RADIUS));
            final int type = data.getInt(data.getColumnIndex(FencesContentProvider.KEY_ZONE_TYPE));

            /*
             * If we don't have a place reference passed in, we need to look it
             * up and update our details accordingly.
             */
            // if (placeReference == null) {
            // placeReference = data.getString(data
            // .getColumnIndex(PlaceDetailsContentProvider.KEY_REFERENCE));
            // updatePlace(placeReference, placeId, true);
            // }

            handler.post(new Runnable() {
                public void run() {
                    nameEditText.setText(name);
                    latitudeEditText.setText("" + latitude);
                    longitudeEditText.setText("" + longitude);
                    radiusEditText.setText("" + radius);
                    typeRadioGroup.check(type);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onLoaderReset(Loader<Cursor> loader) {
        handler.post(new Runnable() {
            public void run() {
                nameEditText.setText("");
                latitudeEditText.setText("");
                longitudeEditText.setText("");
                radiusEditText.setText("");
                typeRadioInclusive.setChecked(true);
            }
        });
    }

    protected OnClickListener saveButtonOnClickListener = new OnClickListener() {
        public void onClick(View view) {
            Log.i(TAG, "salvando cambios");
            String name = nameEditText.getText().toString().trim();
            double latitude = Double.parseDouble(latitudeEditText.getText().toString());
            double longitude = Double.parseDouble(longitudeEditText.getText().toString());
            int radius = Integer.parseInt(radiusEditText.getText().toString());
            int type = typeRadioGroup.getCheckedRadioButtonId();
            long currentTime = System.currentTimeMillis();
            addFence(name, latitude, longitude, radius, type, currentTime);
        }
    };

    /**
     * Añade o actualiza una valla a proveedor {@link FencesContentProvider}
     * usando los valores pasados. Adds theurrentTime Current time
     * 
     * @return Actualizado con exito
     */
    protected boolean addFence(String name, double latitude, double longitude, int radius,
            int type, long currentTime) {
        // Contruct the Content Values
        ContentValues values = new ContentValues();
        values.put(FencesContentProvider.KEY_NAME, name);
        values.put(FencesContentProvider.KEY_LOCATION_LAT, latitude);
        values.put(FencesContentProvider.KEY_LOCATION_LNG, longitude);
        values.put(FencesContentProvider.KEY_RADIUS, radius);
        values.put(FencesContentProvider.KEY_ZONE_TYPE, type);
        values.put(FencesContentProvider.KEY_LAST_UPDATE_TIME, currentTime);

        // Update or add the new place to the PlacesContentProvider
        boolean result = false;
        try {
            if (fenceUri != null) {
                int count = contentResolver.update(fenceUri, values, null, null);
                result = (count > 0);
                if (result) {
                    Log.i(TAG, "Updated fence with uri " + fenceUri + ".");
                }
            } else {
                fenceUri = contentResolver.insert(FencesContentProvider.CONTENT_URI, values);
                result = (fenceUri != null);
                Log.i(TAG, "New fence with uri " + fenceUri + ".");
            }
        } catch (Exception ex) {
            Log.e("PLACES", "Adding " + name + " failed.", ex);
        }

        return result;
    }
}
