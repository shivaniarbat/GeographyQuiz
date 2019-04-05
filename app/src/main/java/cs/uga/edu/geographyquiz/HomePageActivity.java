package cs.uga.edu.geographyquiz;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    public Button newQuiz;
    public Button viewPastQuizzes;
    public String DEBUG_TAG = "HomePageActivity";

    private GeographyQuizData geographyQuizData = null;
    HashMap<String, String> countryContinentHashMap = new HashMap<>();
    HashMap<String, String> countryNeighbourHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();

        /* find views by ids */
        newQuiz = findViewById(R.id.newQuiz);
        viewPastQuizzes = findViewById(R.id.viewPastQuizzes);

        geographyQuizData = new GeographyQuizData( this );

        /* on-click listerners for newQuiz and viewPastQuizzes */
        newQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* write logic to load data into table */
                // check if table is empty then insert data for the first time
                List<CountryContinentNeighbourTableEntry> entriesInTable = geographyQuizData.retrieveAllCountryEntries();
                if(entriesInTable.size() == 0) {
                    insertDataInDatabase();
                }

                /* start new activity */
                Intent quizQuestions = new Intent(HomePageActivity.this,QuizQuestions.class);
                startActivity(quizQuestions);


            }
        });

        viewPastQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewPastQuizzes.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                Intent quizQuestions = new Intent(HomePageActivity.this,ViewPastQuizzes.class);
                startActivity(quizQuestions);
            }
        });
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

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "HomePageActivity.onResume()" );
        if( geographyQuizData != null )
            geographyQuizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "HomePageActivity.onPause()" );
        if( geographyQuizData != null )
            geographyQuizData.close();
        super.onPause();
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
