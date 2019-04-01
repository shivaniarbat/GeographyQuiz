package cs.uga.edu.geographyquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GeographyQuizData {

    public static final String DEBUG_TAG = "GeographyQuizData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper geographyQuizDBHelper;
    private static final String[] allColumns = {
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_QUESTION,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS
    };

    public GeographyQuizData( Context context ){
        this.geographyQuizDBHelper = GeographyQuizDBHelper.getInstance(context);
    }

    /* Open the database */
    public void open(){
        db = geographyQuizDBHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "GeographyQuizData: db open" );
    }

    /* Close the database */
    public void close(){
        if( geographyQuizDBHelper != null ) {
            geographyQuizDBHelper.close();
            Log.d(DEBUG_TAG, "GeographyQuizData: db closed");
        }
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<CountryContinentNeighbourTableEntry> retrieveAllCountryEntries() {
        ArrayList<CountryContinentNeighbourTableEntry> countryContinentNeighbourTableEntries = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( GeographyQuizDBHelper.TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR, allColumns,
                    null, null, null, null, null );
            // collect all job leads into a List
            if( cursor.getCount() > 0 ) {
                while( cursor.moveToNext() ) {
                    long id = cursor.getLong( cursor.getColumnIndex( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC ) );
                    String countryName = cursor.getString( cursor.getColumnIndex( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY ) );
                    String question = cursor.getString( cursor.getColumnIndex( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_QUESTION ) );
                    String continent = cursor.getString( cursor.getColumnIndex( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT ) );
                    String neighbours = cursor.getString( cursor.getColumnIndex( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS ) );
                    CountryContinentNeighbourTableEntry countryContinentNeighbourTableEntry = new CountryContinentNeighbourTableEntry( countryName, question, continent, neighbours );
                    countryContinentNeighbourTableEntry.setId( id );
                    countryContinentNeighbourTableEntries.add( countryContinentNeighbourTableEntry );
                    Log.d( DEBUG_TAG, "Retrieved CountryContinentNeighbourTableEntry: " + countryContinentNeighbourTableEntry );
                }
            }
            Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        return countryContinentNeighbourTableEntries;
    }

    // Store a new job lead in the database.
    public CountryContinentNeighbourTableEntry storeCountryContinentQuestionNeighbour( CountryContinentNeighbourTableEntry countryContinentNeighbourTableEntry ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the JobLead argument.
        // This is how we are providing persistence to a JobLead (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY, countryContinentNeighbourTableEntry.getCountryName());
        values.put( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_QUESTION, countryContinentNeighbourTableEntry.getQuestion() );
        values.put( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT, countryContinentNeighbourTableEntry.getContinent() );
        values.put( GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS, countryContinentNeighbourTableEntry.getNeighbours() );

        Log.d( DEBUG_TAG, "Stored new job lead with before ID ");

        // Insert the new row into the database table;  the id (primary key) will be
        // automatically generated by the database system
        long id = db.insert( GeographyQuizDBHelper.TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR,null, values );

        // store the id in the JobLead instance, as it is now persistent
        countryContinentNeighbourTableEntry.setId( id );

        Log.d( DEBUG_TAG, "Stored new job lead with id: " + String.valueOf( countryContinentNeighbourTableEntry.getId() ) );

        return countryContinentNeighbourTableEntry;
    }
}
