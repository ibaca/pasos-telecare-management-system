
package org.inftel.tms.mobile.ui.fragments;

import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_ID;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_LOCATION_LAT;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_LOCATION_LNG;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_NAME;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_RADIUS;
import static org.inftel.tms.mobile.contentproviders.FencesContentProvider.KEY_ZONE_TYPE;

import org.inftel.tms.mobile.R;
import org.inftel.tms.mobile.contentproviders.FencesContentProvider;
import org.inftel.tms.mobile.ui.FencesActivity;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

/**
 * Fragmento UI para mostra lista de fences del dispositivo.
 */
public class FenceListFragment extends ListFragment implements LoaderCallbacks<Cursor> {

    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;

    protected Cursor cursor = null;
    protected SimpleCursorAdapter adapter;
    protected FencesActivity activity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // this is very important in order to save the state across screen
        // configuration changes
        setRetainInstance(true);

        activity = (FencesActivity) getActivity();

        adapter = new SimpleCursorAdapter(
                activity,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[] {
                        FencesContentProvider.KEY_NAME
                },
                new int[] {
                        android.R.id.text1
                },
                0);
        // Allocate the adapter to the List displayed within this fragment.
        setListAdapter(adapter);

        // Enable context menu
        registerForContextMenu(getListView());

        // Populate the adapter / list using a Cursor Loader.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long theid) {
        super.onListItemClick(l, v, position, theid);

        // Find the ID and Reference of the selected fence.
        Cursor c = adapter.getCursor();
        c.moveToPosition(position);

        Uri fenceUri = ContentUris.withAppendedId(FencesContentProvider.CONTENT_URI,
                c.getLong(c.getColumnIndex(FencesContentProvider.KEY_ID)));

        // Request the parent Activity display the venue detail UI.
        activity.selectDetail(fenceUri.toString());
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {
                KEY_ID, KEY_NAME, KEY_ZONE_TYPE,
                KEY_LOCATION_LAT, KEY_LOCATION_LNG, KEY_RADIUS
        };

        return new CursorLoader(activity, FencesContentProvider.CONTENT_URI, projection,
                null, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        Log.d("FencesList", "context menu created");
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("FencesList", "context menu selected");
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case EDIT_ID:
                onListItemClick(getListView(), getView(), info.position, info.id);
                return true;
            case DELETE_ID:
                Uri uri = ContentUris.withAppendedId(FencesContentProvider.CONTENT_URI, +info.id);
                getActivity().getContentResolver().delete(uri, null, null);
                return true;
        }
        return super.onContextItemSelected(item);
    }

}
