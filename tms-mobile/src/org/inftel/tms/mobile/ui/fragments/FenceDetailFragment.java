
package org.inftel.tms.mobile.ui.fragments;

// TODO Create a richer UI to display places Details. This should include images,
// TODO ratings, reviews, other people checked in here, etc.

import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_LOCATION_LAT;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_LOCATION_LNG;
import static org.inftel.tms.mobile.util.PlatformSpecificImplementationFactory.getLastLocationFinder;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.TmsConstants;
import org.inftel.tms.mobile.contentproviders.FencesContentProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
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
import android.widget.Toast;

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

        if (fenceUri != null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            // New fence (set default values)
            Location location = getLastLocationFinder(getActivity()).getLastBestLocation(0, 0);
            if (location != null) {
                nameEditText.setText("fence");
                latitudeEditText.setText(location.getLatitude() + "");
                longitudeEditText.setText(location.getLongitude() + "");
                radiusEditText.setText("500");
                typeRadioGroup.check(typeRadioGroup.getChildAt(0).getId());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fences_detail, container, false);
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

            handler.post(new Runnable() {
                public void run() {
                    nameEditText.setText(name);
                    latitudeEditText.setText("" + latitude);
                    longitudeEditText.setText("" + longitude);
                    radiusEditText.setText("" + radius);
                    typeRadioGroup.check(typeRadioGroup.getChildAt(type).getId());
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
                typeRadioGroup.check(typeRadioGroup.getChildAt(0).getId());
            }
        });
    }

    protected OnClickListener saveButtonOnClickListener = new OnClickListener() {
        public void onClick(View view) {
            Log.i(TAG, "salvando cambios");
            String name = nameEditText.getText().toString().trim();
            Double latitude, longitude;
            try {
                latitude = Double.parseDouble(latitudeEditText.getText().toString());
                longitude = Double.parseDouble(longitudeEditText.getText().toString());
            } catch (Exception somethingGoesWrongWithParsingButDontCareICanSolveThis) {
                latitude = .0;
                longitude = .0;
            }
            Integer radius;
            try {
                radius = Integer.parseInt(radiusEditText.getText().toString());
            } catch (Exception somethingGoesWrongWithParsingButDontCareICanSolveThis) {
                radius = 0;
            }
            int type = (typeRadioInclusive.isChecked()) ? 0 : 1;
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
                    Toast.makeText(getActivity(), "Cambios guardados", Toast.LENGTH_LONG).show();
                }
            } else {
                fenceUri = contentResolver.insert(FencesContentProvider.CONTENT_URI, values);
                result = (fenceUri != null);
                Log.i(TAG, "New fence with uri " + fenceUri + ".");
                Toast.makeText(getActivity(), "Nueva valla guardada", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.e("PLACES", "Adding " + name + " failed.", ex);
        }

        return result;
    }
}
