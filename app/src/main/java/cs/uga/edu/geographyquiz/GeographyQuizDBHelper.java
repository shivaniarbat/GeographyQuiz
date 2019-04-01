package cs.uga.edu.geographyquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GeographyQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "GeographyQuizDBHelper";
    private static final String DB_NAME = "geographyquiz.db";
    private static final int DB_VERSION = 1;

    /* table creation information */
    public static final String TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR = "country_continent_neighbour";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC = "_id";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY = "country";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_QUESTION = "question";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT = "continent";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS = "neighbours";

    /* A Create table SQL statement to create a table for job leads.
     Note that _id is an auto increment primary key, i.e. the database will
     automatically generate unique id values as keys. */
    private static final String CREATE_COUNTRY_CONTINENT_NEIGHBOUR =
            "create table " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR + " ("
                    + COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY + " TEXT, "
                    + COUNTRY_CONTINENT_NEIGHBOUR_QUESTION + " TEXT, "
                    + COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT + " TEXT, "
                    + COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS + " TEXT"
                    + ")";


    /* This is a reference to the only instance for the helper. */
    private static GeographyQuizDBHelper helperInstance;

    private GeographyQuizDBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    /* Access method to the single instance of the class */
    public static synchronized GeographyQuizDBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new GeographyQuizDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    // We must override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( CREATE_COUNTRY_CONTINENT_NEIGHBOUR );
        Log.d( DEBUG_TAG, "Table " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR + " created" );
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR + " upgraded" );
    }
}
