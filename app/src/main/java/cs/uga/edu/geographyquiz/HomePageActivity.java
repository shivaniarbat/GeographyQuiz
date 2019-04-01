package cs.uga.edu.geographyquiz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePageActivity extends AppCompatActivity {

    public Button newQuiz;
    public Button viewPastQuizzes;
    public String DEBUG_TAG = "HomePageActivity";

    private GeographyQuizData geographyQuizData = null;

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
               // newQuiz.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                Intent quizQuestions = new Intent(HomePageActivity.this,QuizQuestions.class);
//                startActivity(quizQuestions);

                CountryContinentNeighbourTableEntry tableEntry = new CountryContinentNeighbourTableEntry("US","What is/are continent and neightbours of India","North America","Canada,Mexico");
                Log.d( DEBUG_TAG, "Table entry going to writer task: " + tableEntry );

                // Store this new job lead in the database asynchronously,
                // without blocking the UI thread.
                new JobLeadDBWriterTask().execute( tableEntry );

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
            Toast.makeText( getApplicationContext(), "Job lead created for " + countryContinentNeighbourTableEntry.getCountryName(),
                    Toast.LENGTH_SHORT).show();


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
}
