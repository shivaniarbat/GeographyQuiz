package cs.uga.edu.geographyquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GeographyQuizData {

    public static final String DEBUG_TAG = "GeographyQuizData";

    // this is a reference to our database; it is used later to run SQL commands
    private static SQLiteDatabase db;
    private SQLiteOpenHelper geographyQuizDBHelper;
    private static final String[] allColumns = {
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_QUESTION,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT,
            GeographyQuizDBHelper.COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS,
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

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<String> retrieveAllCountryNames() {
        ArrayList<String> countryEntries = new ArrayList<>();
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
                    countryEntries.add( countryContinentNeighbourTableEntry.getCountryName() );
                    //Log.d( DEBUG_TAG, "Retrieved CountryContinentNeighbourTableEntry: " + countryContinentNeighbourTableEntry );
                }
            }
            //Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
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
        return countryEntries;
    }

    // Store a new job lead in the database.
    public static QuizResultTableEntry storequizEntry( QuizResultTableEntry quizResultTableEntry ) {
        boolean DEBUG = true;
        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the JobLead argument.
        // This is how we are providing persistence to a JobLead (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        /* add this data into database in quiz_results table */

        ContentValues values = new ContentValues();
        values.put(GeographyQuizDBHelper.QUIZ_QUESTION_ID1, quizResultTableEntry.getQ1());
        values.put(GeographyQuizDBHelper.QUIZ_QUESTION_ID2, quizResultTableEntry.getQ2());
        values.put(GeographyQuizDBHelper.QUIZ_QUESTION_ID3, quizResultTableEntry.getQ3());
        values.put(GeographyQuizDBHelper.QUIZ_QUESTION_ID4, quizResultTableEntry.getQ4());
        values.put(GeographyQuizDBHelper.QUIZ_QUESTION_ID5, quizResultTableEntry.getQ5());
        values.put(GeographyQuizDBHelper.QUIZ_QUESTION_ID6, quizResultTableEntry.getQ6());
        values.put(GeographyQuizDBHelper.QUIZ_DATE, Calendar.getInstance().getTime().toString());
        values.put(GeographyQuizDBHelper.QUIZ_SCORE, quizResultTableEntry.getScore());


        if(DEBUG) {
            System.out.println("QUIZ ENTRY: Q1:" + quizResultTableEntry.getQ1() + ";");
            System.out.println("QUIZ ENTRY: Q2:" + quizResultTableEntry.getQ2()+ ";");
            System.out.println("QUIZ ENTRY: Q3:" + quizResultTableEntry.getQ3()+ ";");
            System.out.println("QUIZ ENTRY: Q4:" + quizResultTableEntry.getQ4()+ ";");
            System.out.println("QUIZ ENTRY: Q5:" + quizResultTableEntry.getQ5()+ ";");
            System.out.println("QUIZ ENTRY: Q6:" + quizResultTableEntry.getQ6()+ ";");
            System.out.println("QUIZ ENTRY SCORE:" + quizResultTableEntry.getScore() + ";");
        }


        long id = db.insert( GeographyQuizDBHelper.TABLE_QUIZ_RESULT,null, values );

        quizResultTableEntry.setId(id);

        Log.d( DEBUG_TAG, "Stored new job quiz entry with id: " + String.valueOf( quizResultTableEntry.getId() ) );

        return quizResultTableEntry;
    }


}
