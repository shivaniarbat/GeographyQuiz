package cs.uga.edu.geographyquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public static String DEBUG_TAG = "QuizQuestions";
    private GeographyQuizData geographyQuizData = null;


    public final static Integer[] imageIds = new Integer[]{
            R.drawable.figs, R.drawable.grapes, R.drawable.heirloom_tomatoes,
            R.drawable.lemons, R.drawable.lime, R.drawable.oranges,
            R.drawable.peach, R.drawable.peppers, R.drawable.zucchini
    };

    public final static String[] imageDescriptions = new String[]{
            "Figs", "Grapes", "Heirloom Tomatoes",
            "Lemons", "Lime", "Oranges",
            "Peach", "Peppers", "Zucchini"
    };

    public String[] randomCountries = new String[6];
    public static String[] randomQuestions = new String[6];
    public String[] randomNeighbours = new String[6];
    public String[] randomContinents = new String[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        getSupportActionBar().hide();

        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), imageIds.length);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), randomCountries.length);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        geographyQuizData = new GeographyQuizData( this );

        if( geographyQuizData != null )
            geographyQuizData.open();
        // call logic for loading questions
        List<CountryContinentNeighbourTableEntry> entriesInTable = geographyQuizData.retrieveAllCountryEntries();

        if( geographyQuizData != null )
            geographyQuizData.close();
        /* generate random 6 numbers from range of 1 to 195 */
        int[] randomIntergers = new int[6];
        randomIntergers = generateRandomInteger(6,1,195);

        /* populate arrays for values of the random data generated */
        for(int i = 0;i < 6;i++){
            randomCountries[i] = entriesInTable.get(i).getCountryName();
            randomContinents[i] = entriesInTable.get(i).getContinent();
            randomQuestions[i] = entriesInTable.get(i).getQuestion();
            randomNeighbours[i] = entriesInTable.get(i).getNeighbours();
        }

        //  call logic for randomizing the data

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });

    }

    public int[] generateRandomInteger(int sizeOfArray, int startIndex, int lastIndex){
        int[] randomIntegers = new int[6];
        ArrayList numbers = new ArrayList();
        for(int i = startIndex ;i <= lastIndex ; i++){
            numbers.add(i);
        }
        // shuffle numbers
        Collections.shuffle(numbers);

        for(int i = 0;i < 6;i++){
            randomIntegers[i] = (int) numbers.get(i+1);
            System.out.println("RANDOM DATA: " + randomIntegers[i]);
        }
        return randomIntegers;
    }

    public void loadView(ImageView imageView, int resId, TextView textView, String description) {
        imageView.setImageResource(resId);
        textView.setText(description);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final int mSize;

        public SectionsPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.mSize = size;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int imageNum = position + 1;
            return String.valueOf("Image " + imageNum);
        }

        public String returnQuestion(){
            return randomQuestions[0];
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int mImageNum;
        private TextView continentQuestionTextView;
        private RadioButton continentOptionA;
        private ImageView mImageView;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mImageNum = getArguments().getInt(ARG_SECTION_NUMBER);
            } else {
                mImageNum = -1;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz_questions, container, false);


            continentQuestionTextView = (TextView) rootView.findViewById(R.id.continentQuestionTextView);
            continentOptionA = rootView.findViewById(R.id.continentOptionA);
            mImageView = (ImageView) rootView.findViewById(R.id.imageView2);



            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (QuizQuestions.class.isInstance(getActivity())) {
                final int resId = QuizQuestions.imageIds[mImageNum - 1];
                //final String description = imageDescriptions[mImageNum - 1];
                final String description = randomQuestions[mImageNum - 1];

                Log.e(DEBUG_TAG,"Fragment.onActivityCreated randomquestions:" + randomQuestions[mImageNum - 1]);
                /* add a fragment with a view model */

                ((QuizQuestions) getActivity()).loadView(mImageView, resId, continentOptionA, description);
            }
        }


//        @Override
//        public void onResume() {
//            Log.d( DEBUG_TAG, "QuizQuestions.onResume()" );
//            if( geographyQuizData != null )
//                geographyQuizData.open();
//            super.onResume();
//        }
//
//        @Override
//        public void onPause() {
//            Log.d( DEBUG_TAG, "QuizQuestions.onPause()" );
//            if( geographyQuizData != null )
//                geographyQuizData.close();
//            super.onPause();
//        }
    }
}
