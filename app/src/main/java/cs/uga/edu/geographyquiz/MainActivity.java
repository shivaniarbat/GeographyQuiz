package cs.uga.edu.geographyquiz;

/**
 * This is splash screen activity class.
 * @author Shivani Arbat
 * @version 1.0
 */

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected String DEBUG_TAG = "MainActivity";
    private static int SPLASH_TIME_OUT = 4000;
    private GeographyQuizData geographyQuizData = null;
    HashMap<String, String> countryContinentHashMap = new HashMap<>();
    HashMap<String, String> countryNeighbourHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        geographyQuizData = new GeographyQuizData( this );

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this,HomePageActivity.class);
                MainActivity.this.startActivity(homeIntent);
                MainActivity.this.finish();
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();

        geographyQuizData.open();
        List<CountryContinentNeighbourTableEntry> entriesInTable = geographyQuizData.retrieveAllCountryEntries();
        if(entriesInTable.size() == 0) {
            /* tested with pre-loaded database */
            insertDataInDatabase();
        }
    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a job lead, asynchronously.
    private class JobLeadDBWriterTask extends AsyncTask<CountryContinentNeighbourTableEntry, Void, CountryContinentNeighbourTableEntry> {

        // This method will run as a background process to write into db.
        @Override
        protected CountryContinentNeighbourTableEntry doInBackground( CountryContinentNeighbourTableEntry... countryContinentNeighbourTableEntry ) {
            geographyQuizData.storeCountryContinentQuestionNeighbour( countryContinentNeighbourTableEntry[0]);
            return countryContinentNeighbourTableEntry[0];
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.
        @Override
        protected void onPostExecute( CountryContinentNeighbourTableEntry countryContinentNeighbourTableEntry ) {
            super.onPostExecute( countryContinentNeighbourTableEntry );

            // Show a quick confirmation
            /* Toast.makeText( getApplicationContext(), "Entry created for " + countryContinentNeighbourTableEntry.getCountryName(),
                    Toast.LENGTH_SHORT).show(); */


            Log.d( DEBUG_TAG, "Country saved: " + countryContinentNeighbourTableEntry );
        }
    }

    protected void insertDataInDatabase(){
        Resources res = getResources();
        InputStream countryContinentInputStream = res.openRawResource(R.raw.country_continent);
        InputStream countryNeighbourInputStream = res.openRawResource(R.raw.country_neighbors);
        // read the CSV data
        CSVReader countryContinentReader = new CSVReader(new InputStreamReader(countryContinentInputStream));
        CSVReader countryNeighbourReader = new CSVReader(new InputStreamReader(countryNeighbourInputStream));
        String[] nextLine;

        try {
            /* populate hashmap for country continent data */
            while ((nextLine = countryContinentReader.readNext()) != null) {
                Log.e("The Result is", nextLine[1]);
                countryContinentHashMap.put(nextLine[0], nextLine[1]);
            }

            /* populate hashmap for country neighbour data */
            while ((nextLine = countryNeighbourReader.readNext()) != null) {
                String newValue = "";
                boolean firstNeighbour = true;

                for(int i = 1; i < nextLine.length; i++){
                    if(firstNeighbour){
                        newValue = nextLine[i];
                        firstNeighbour = false;
                    } else {
                        if(!nextLine[i].isEmpty()){
                            newValue = newValue + ";" + nextLine[i];
                        }
                    }
                }

                if(newValue.isEmpty()){
                    newValue = "No Neighbour";
                }

                countryNeighbourHashMap.put(nextLine[0],newValue);
                System.out.println("ENtry DONE: "  + nextLine[0] + newValue);
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("here");
        }

        /* update table from the hashmaps */
        /* get country names from hashmap countryContinentHashMap */

        /* country name ; question ; continent ; neoghbour */
        /* new CountryContinentNeighbourTableEntry("US","What is/are continent and neightbours of India","North America","Canada,Mexico"); */

        for(String countryName :countryContinentHashMap.keySet()){
            new JobLeadDBWriterTask().execute( new CountryContinentNeighbourTableEntry(countryName,"Select CONTINENT & NEIGHBOUR for " + countryName + " from below.",countryContinentHashMap.get(countryName),countryNeighbourHashMap.get(countryName)) );
        }
    }
}
