package cs.uga.edu.geographyquiz;

/**
 * @author Shivani Arbat; Apurva Bansode
 * @version 1.0
 */

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ViewPastQuizzes extends AppCompatActivity {
    public static final String DEBUG_TAG = "ViewPastQuizzes";

    private RecyclerView toDisplayQuizSummary;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private ImageButton backImageButtonToHomePage;

    private GeographyQuizData geographyQuizData = null;
    private List<QuizResultTableEntry> pastQuizzesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d( DEBUG_TAG, "ViewPastQuizzes.onCreate()" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_quizzes);
        getSupportActionBar().hide();

        toDisplayQuizSummary = findViewById(R.id.toDisplayQuizSummary);
        backImageButtonToHomePage = findViewById(R.id.backImageButtonToHomePage);

        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this );
        toDisplayQuizSummary.setLayoutManager( layoutManager );

        // Create a JobLeadsData instance, since we will need to save a new JobLead to the dn.
        // Note that even though more activites may create their own instances of the JobLeadsData
        // class, we will be using a single instance of the JobLeadsDBHelper object, since
        // that class is a singleton class.
        geographyQuizData = new GeographyQuizData( this );

        // Execute the retrieval of the job leads in an asynchronous way,
        // without blocking the UI thread.
        new QuizResultDBReaderTask().execute();
        /* on click listener for image button */
        backImageButtonToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backImageButtonToHomePage.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                ViewPastQuizzes.super.onBackPressed();
            }
        });

        /* write code to display quiz summary here  - RECYCLER */
    }
    // This is an AsyncTask class (it extends AsyncTask) to perform DB reading of job leads, asynchronously.
    private class QuizResultDBReaderTask extends AsyncTask<Void, Void, List<QuizResultTableEntry>>{

        // This method will run as a background process to read from db.
        @Override
        protected List<QuizResultTableEntry> doInBackground(Void... params) {
            geographyQuizData.open();
            pastQuizzesList = geographyQuizData.retrieveAllPastQuizzes();
            geographyQuizData.close();
            Log.d( DEBUG_TAG, "JobLeadDBReaderTask: Job leads retrieved from ViewPastQuizzes.java: " + pastQuizzesList.size() );

            return pastQuizzesList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        @Override
        protected void onPostExecute( List<QuizResultTableEntry> pastQuizzesList ) {
            super.onPostExecute(pastQuizzesList);
            recyclerAdapter = new ViewPastQuizRecyclerAdapter( pastQuizzesList );
            toDisplayQuizSummary.setAdapter( recyclerAdapter );
        }
    }
}
