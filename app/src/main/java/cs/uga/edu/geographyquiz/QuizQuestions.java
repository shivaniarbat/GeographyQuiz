package cs.uga.edu.geographyquiz;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QuizQuestions extends AppCompatActivity implements QuizQuestionsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        getSupportActionBar().hide();

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        QuizQuestionsFragment quizQuestionsFragment = new QuizQuestionsFragment();

        ft.add(R.id.fragment_placeholder, new QuizQuestionsFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // *************************** important method to define fragment interactions *******************************
        // *************************** important method to define fragment interactions *******************************
        // *************************** important method to define fragment interactions *******************************
        // *************************** important method to define fragment interactions *******************************
        // *************************** important method to define fragment interactions *******************************
        // *************************** important method to define fragment interactions *******************************
        // *************************** important method to define fragment interactions *******************************

        System.out.println("Something done correctly");
    }
}
