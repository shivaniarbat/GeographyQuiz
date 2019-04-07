package cs.uga.edu.geographyquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class GeographyQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "GeographyQuizDBHelper";
    private static final String DB_NAME = "geographyquiz.db";
    private static final int DB_VERSION = 1;

    /* table creation information  foe question table*/
    public static final String TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR = "country_continent_neighbour";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC = "_id";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_COUNTRY = "country";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_QUESTION = "question";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_CONTINENT = "continent";
    public static final String COUNTRY_CONTINENT_NEIGHBOUR_NEIGHBOURS = "neighbours";

    /*table creation information for result table */
    public static final String TABLE_QUIZ_RESULT = "quiz_result";
    public static final String QUIZ_RESULT_ID ="_quiz_id";
    public static final String QUIZ_DATE = "quiz_date";
    public static final String QUIZ_QUESTION_ID1 = "q1";
    public static final String QUIZ_QUESTION_ID2 = "q2";
    public static final String QUIZ_QUESTION_ID3 = "q3";
    public static final String QUIZ_QUESTION_ID4 = "q4";
    public static final String QUIZ_QUESTION_ID5 = "q5";
    public static final String QUIZ_QUESTION_ID6 = "q6";
    public static final String QUIZ_SCORE = "score";


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



    private static final String CREATE_QUIZ_RESULT =
            "create table " + TABLE_QUIZ_RESULT + " ("
                    + QUIZ_RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZ_DATE + " TEXT, "
                    + QUIZ_QUESTION_ID1 + " INTEGER NOT NULL, "
                    + QUIZ_QUESTION_ID2 + " INTEGER NOT NULL, "
                    + QUIZ_QUESTION_ID3 + " INTEGER NOT NULL, "
                    + QUIZ_QUESTION_ID4 + " INTEGER NOT NULL, "
                    + QUIZ_QUESTION_ID5 + " INTEGER NOT NULL, "
                    + QUIZ_QUESTION_ID6 + " INTEGER NOT NULL, "
                    + QUIZ_SCORE + " INTEGER NOT NULL,"
                    + " FOREIGN KEY ("+ QUIZ_QUESTION_ID1 +") REFERENCES "+ TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR+"("+COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC +"),"
                    + " FOREIGN KEY ("+ QUIZ_QUESTION_ID2 +") REFERENCES "+ TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR+"("+COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC +"),"
                    + " FOREIGN KEY ("+ QUIZ_QUESTION_ID3 +") REFERENCES "+ TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR+"("+COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC +"),"
                    + " FOREIGN KEY ("+ QUIZ_QUESTION_ID4 +") REFERENCES "+ TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR+"("+COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC +"),"
                    + " FOREIGN KEY ("+ QUIZ_QUESTION_ID5 +") REFERENCES "+ TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR+"("+COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC +"),"
                    + " FOREIGN KEY ("+ QUIZ_QUESTION_ID6 +") REFERENCES "+ TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR+"("+COUNTRY_CONTINENT_NEIGHBOUR_AUTOINC +"));";


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
        System.out.println("onCreate called");
        System.out.println(CREATE_COUNTRY_CONTINENT_NEIGHBOUR);
        System.out.println(CREATE_QUIZ_RESULT);
        db.execSQL( CREATE_COUNTRY_CONTINENT_NEIGHBOUR );
        Log.d( DEBUG_TAG, "Table " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR + " created" );

        db.execSQL(CREATE_QUIZ_RESULT);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZ_RESULT + "created");
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_PARENT_COUNTRY_CONTINENT_NEIGHBOUR + " upgraded" );

        db.execSQL("drop table if exists " + TABLE_QUIZ_RESULT );
        onCreate(db);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZ_RESULT + "upgraded");
    }
}
