package org.inftel.tms.mobile.contentproviders;

import static android.text.TextUtils.isEmpty;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Content Provider and database for storing the list of fences Proveedor de contenido y base de
 * datos para almecenar las vallas del dispositivo.
 * 
 * TODO mas separacion entre Provider y datos de tabla (mirar siguiente enlace)
 * http://www.vogella.de/articles/AndroidSQLite/article.html#tutorialusecp_overview
 */
public class FencesContentProvider extends ContentProvider {

	/** The underlying database */
	private SQLiteDatabase fencesDB;

	private static final String TAG = "FencesContentProvider";
	private static final String DATABASE_NAME = "fences.db";
	private static final int DATABASE_VERSION = 1;
	private static final String FENCES_TABLE = "fences";

	// Column Names
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LOCATION_LAT = "latitude";
	public static final String KEY_LOCATION_LNG = "longitude";
	public static final String KEY_ZONE_TYPE = "inclusionzone";
	public static final String KEY_RADIUS = "radius";
	public static final String KEY_LAST_UPDATE_TIME = "lastupdatetime";

	// Zone Types
	public static final int ZONE_TYPE_INCLUSION = 0;
	public static final int ZONE_TYPE_EXCLUSION = 1;

	public static final Uri CONTENT_URI = Uri
		.parse("content://org.inftel.tms.mobile.provider.fences/fences");

	// Create the constants used to differentiate between the different URI requests.
	private static final int FENCES = 1;
	private static final int FENCES_ID = 2;

	// Allocate the UriMatcher object, where a URI ending in 'places' will
	// correspond to a request for all places, and 'places' with a trailing '/[Unique ID]' will
	// represent a single place details row.
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("org.inftel.tms.mobile.provider.fences", "fences", FENCES);
		uriMatcher.addURI("org.inftel.tms.mobile.provider.fences", "fences/*", FENCES_ID);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();

		PlacesDatabaseHelper dbHelper;
		dbHelper = new PlacesDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		try {
			fencesDB = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			fencesDB = null;
			Log.d(TAG, "Database Opening exception");
		}

		return (fencesDB == null) ? false : true;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case FENCES:
			return "vnd.android.cursor.dir/vnd.inftel.tms.fence";
		case FENCES_ID:
			return "vnd.android.cursor.item/vnd.inftel.tms.fence";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
		String sort) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(FENCES_TABLE);

		// If this is a row query, limit the result set to the passed in row.
		switch (uriMatcher.match(uri)) {
		case FENCES_ID:
			qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			break;
		}

		String orderBy = null;
		if (!TextUtils.isEmpty(sort)) {
			orderBy = sort;
		}

		// Apply the query to the underlying database.
		Cursor c = qb.query(fencesDB, projection, selection, selectionArgs, null, null, orderBy);

		// Register the contexts ContentResolver to be notified if
		// the cursor result set changes.
		c.setNotificationUri(getContext().getContentResolver(), uri);

		// Return a cursor to the query result.
		return c;
	}

	@Override
	public Uri insert(Uri _uri, ContentValues _initialValues) {
		// Insert the new row, will return the row number if successful.
		long rowID = fencesDB.insert(FENCES_TABLE, "nullhack", _initialValues);

		// Return a URI to the newly inserted row on success.
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + _uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;

		switch (uriMatcher.match(uri)) {
		case FENCES:
			count = fencesDB.delete(FENCES_TABLE, where, whereArgs);
			break;

		case FENCES_ID:
			String segment = uri.getPathSegments().get(1);
			count = fencesDB.delete(FENCES_TABLE, KEY_ID + "="
				+ segment
				+ (!TextUtils.isEmpty(where) ? " AND ("
					+ where + ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
		case FENCES:
			count = fencesDB.update(FENCES_TABLE, values, where, whereArgs);
			break;

		case FENCES_ID:
			String segment = uri.getLastPathSegment();
			count = fencesDB.update(FENCES_TABLE, values, KEY_ID + "=" + segment
				+ (!isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	// Helper class for opening, creating, and managing database version control
	private static class PlacesDatabaseHelper extends SQLiteOpenHelper {
		private static final String DATABASE_CREATE =
			"create table " + FENCES_TABLE + " ("
				+ KEY_ID + " INTEGER primary key autoincrement, "
				+ KEY_NAME + " TEXT, "
				+ KEY_LOCATION_LAT + " FLOAT, "
				+ KEY_LOCATION_LNG + " FLOAT, "
				+ KEY_ZONE_TYPE + " LONG, "
				+ KEY_RADIUS + " LONG, "
				+ KEY_LAST_UPDATE_TIME + " LONG); ";

		public PlacesDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + FENCES_TABLE);
			onCreate(db);
		}
	}
}