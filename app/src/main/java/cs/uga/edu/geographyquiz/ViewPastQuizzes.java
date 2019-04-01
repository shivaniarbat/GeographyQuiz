package cs.uga.edu.geographyquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewPastQuizzes extends AppCompatActivity {

    RecyclerView toDisplayQuizSummary;
    ImageButton backImageButtonToHomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_quizzes);
        getSupportActionBar().hide();

        toDisplayQuizSummary = findViewById(R.id.toDisplayQuizSummary);
        backImageButtonToHomePage = findViewById(R.id.backImageButtonToHomePage);

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
}
