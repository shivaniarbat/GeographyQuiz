package cs.uga.edu.geographyquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class QuizQuestions extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public static String DEBUG_TAG = "QuizQuestions";
    private GeographyQuizData geographyQuizData = null;
    static boolean DEBUG = false;


    public final static Integer[] imageIds = new Integer[]{
            R.drawable.figs, R.drawable.grapes, R.drawable.heirloom_tomatoes,
            R.drawable.lemons, R.drawable.lime, R.drawable.oranges,
            R.drawable.peach, R.drawable.peppers, R.drawable.zucchini
    };

    public final static String[] listOfContinents = new String[]{
            "Asia", "North America", "Europe",
            "Africa", "Oceania", "South America",
            "Australia"
    };

    public static String[] randomCountries = new String[6];
    public static String[] randomQuestions = new String[6];
    public static String[] randomCorrectNeighbours = new String[6];
    public static String[] randomCorrectContinents = new String[6];
    public static String[][] otherContinentOptions = new String[6][2];
    public static String[][] otherNeighbourOptions = new String[6][2];
    public static String[] listOfNeighbours;
    public static boolean neighbourFlag;
    public static boolean continentFlag;
    public static int[] randomContinentsId;
    public static int[] randomNeighbourId;
    public static int[] extractedNeighbourId = new int[2];
    public static ArrayList numbers;
    public static String thirdNeighbourOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        getSupportActionBar().hide();

        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), imageIds.length);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), randomCountries.length + 1);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        geographyQuizData = new GeographyQuizData( this );

        if( geographyQuizData != null )
            geographyQuizData.open();
        // call logic for loading questions
        List<CountryContinentNeighbourTableEntry> entriesInTable = geographyQuizData.retrieveAllCountryEntries();
        List<String> countryNames = geographyQuizData.retrieveAllCountryNames();

        if( geographyQuizData != null )
            geographyQuizData.close();
        /* generate random 6 numbers from range of 1 to 195 */
        int[] randomIntegers = generateRandomInteger(6,1,195);


        /* populate arrays for values of the random 6 set data generated */
        for(int i = 0;i < 6;i++){
            randomCountries[i] = entriesInTable.get(randomIntegers[i]).getCountryName();
            randomCorrectContinents[i] = entriesInTable.get(randomIntegers[i]).getContinent();
            randomQuestions[i] = entriesInTable.get(randomIntegers[i]).getQuestion();
            randomCorrectNeighbours[i] = entriesInTable.get(randomIntegers[i]).getNeighbours();

            /* select two random continents other than correct one */
            continentFlag = true;
            while (continentFlag){
                randomContinentsId = generateRandomInteger(2,0,5);
                if((listOfContinents[randomContinentsId[0]]).equals(randomCorrectContinents[i])
                || ((listOfContinents[randomContinentsId[1]]).equals(randomCorrectContinents[i]))) {
                    continentFlag = true;
                } else {
                    otherContinentOptions[i][0] = listOfContinents[randomContinentsId[0]];
                    otherContinentOptions[i][1] = listOfContinents[randomContinentsId[1]];
                    continentFlag = false;

                    if(DEBUG) {
                        System.out.println("RANDOM CONTINENT A: " + otherContinentOptions[i][0]);
                        System.out.println("RANDOM CONTINENT B: " + otherContinentOptions[i][1]);
                        System.out.println("RANDOM CONTINENT C: " + randomCorrectContinents[i]);
                        System.out.println("RANDOM CONTINENT");
                    }
                }
            }

            /* select two random neighbours other than correct one */
            neighbourFlag = true;
            listOfNeighbours = randomCorrectNeighbours[i].split(";");
            System.out.println("LIST OF NEIGHBOURS:" + Arrays.toString(listOfNeighbours));
//            while (neighbourFlag) {
//                randomNeighbourId = generateRandomInteger(2, 1, 195);
//
//                if (Arrays.asList(listOfNeighbours).contains("No Neighbour")) {
//                    neighbourFlag = true;
//                } else {
//                    if (Arrays.asList(listOfNeighbours).contains(countryNames.get(randomNeighbourId[0] - 1))
//                            || Arrays.asList(listOfNeighbours).contains(countryNames.get(randomNeighbourId[1] - 1))
//                        // || Arrays.asList(listOfNeighbours).contains("No Neighbour")
//                    ) { // if either of the neighbour is present in the randomly selected neighbours
//                        neighbourFlag = true;
//                    } else {
//
//                        otherNeighbourOptions[i][0] = countryNames.get(randomNeighbourId[0] - 1);
//                        otherNeighbourOptions[i][1] = countryNames.get(randomNeighbourId[1] - 1);
//                        neighbourFlag = false;
//                    }
//                }
//
//            }
            while (neighbourFlag){
                randomNeighbourId = generateRandomInteger(2,1,195);
                for(int idx = 0;idx < listOfNeighbours.length ; idx++ ){
                    if(listOfNeighbours[idx].equals(countryNames.get(randomNeighbourId[0] - 1))
                            || listOfNeighbours[idx].equals(countryNames.get(randomNeighbourId[1] - 1))
                          //  || listOfNeighbours[0].equals("No Neighbour")
                    ){
                        neighbourFlag = true;
                    } else {
                        if(!countryNames.get(randomNeighbourId[0] - 1).equals("No Neighbour"))
                        otherNeighbourOptions[i][0] = countryNames.get(randomNeighbourId[0] - 1);

                        if(!countryNames.get(randomNeighbourId[1] - 1).equals("No Neighbour"))
                        otherNeighbourOptions[i][1] = countryNames.get(randomNeighbourId[1] - 1);

                        if(randomCorrectNeighbours[i].equals("No Neighbour")){
                            int[] randomNumber;
                            while(true){
                                randomNeighbourId = generateRandomInteger(1,1,195);
                                if(otherNeighbourOptions[i][0].equals(countryNames.get(randomNeighbourId[0] - 1))
                                || otherNeighbourOptions[i][1].equals(countryNames.get(randomNeighbourId[0] - 1))){

                                } else {
                                    thirdNeighbourOption = countryNames.get(randomNeighbourId[0] - 1);
                                    break;
                                }
                            }
                        }
                        neighbourFlag = false;
                    }
                }

            }

        }

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
        int[] randomIntegers = new int[sizeOfArray];
        numbers = new ArrayList();
        for(int i = startIndex ;i <= lastIndex ; i++){
            numbers.add(i);
        }
        // shuffle numbers
        Collections.shuffle(numbers);

        for(int i = 0;i < sizeOfArray;i++){
            randomIntegers[i] = (int) numbers.get(i+1);
            if(DEBUG)
                System.out.println("RANDOM DATA: " + randomIntegers[i]);
        }
        return randomIntegers;
    }

//    public void loadView(ImageView imageView, int resId, TextView textView, String description) {
//        imageView.setImageResource(resId);
//        textView.setText(description);
//    }

    public void loadView(TextView continentQuestionTextView, String genericQuestion, TextView continentOptionATEXTTextView, String optionA,
                         TextView continentOptionBTEXTTextView, String optionB, TextView continentOptionCTEXTTextView, String optionC,
                         TextView neighbourOptionA, String neighbourOptionAText, TextView neighbourOptionB, String neighbourOptionBText,
                         TextView neighbourOptionC, String neighbourOptionCText, TextView swipeQuestionsStatusTextView, String status
                         ){
        continentQuestionTextView.setText(genericQuestion);
        continentOptionATEXTTextView.setText(optionA);
        continentOptionBTEXTTextView.setText(optionB);
        continentOptionCTEXTTextView.setText(optionC);
        neighbourOptionA.setText(neighbourOptionAText);
        neighbourOptionB.setText(neighbourOptionBText);
        neighbourOptionC.setText(neighbourOptionCText);
        swipeQuestionsStatusTextView.setText(status + " of 6");
    }

    public void onClickButtonGoToMainPage(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(QuizQuestions.this,HomePageActivity.class);
                QuizQuestions.this.startActivity(homeIntent);
                QuizQuestions.this.finish();
            }
        });
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
        private RadioButton continentOptionB;
        private RadioButton continentOptionC;
        private RadioButton neighbourOptionA;
        private RadioButton neighbourOptionB;
        private RadioButton neighbourOptionC;
        private ImageView resultsImage;
        private TextView titleTextView9;
        private RadioButton neighbourOptionD;
        private TextView neighbourQuestionTextView;
        private TextView continentOptionATEXTTextView;
        private TextView continentOptionBTEXTTextView;
        private TextView continentOptionCTEXTTextView;
        private TextView neighbourOptionATEXTTextView;
        private TextView neighbourOptionBTEXTTextView;
        private TextView neighbourOptionCTEXTTextView;
        private TextView neighbourOptionDTEXTTextView;
        private TextView swipeQuestionsStatusTextView;
        private TextView swipeInstructionsTextView;
        private TextView quizInfo2TextView;
        private Button goToMainPage;
        private LinearLayout question1LinearLayout;
        private ScrollView questionsFragmentScrollView;

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
            continentOptionB = rootView.findViewById(R.id.continentOptionB);
            continentOptionC = rootView.findViewById(R.id.continentOptionC);

            neighbourOptionA = rootView.findViewById(R.id.neighbourOptionA);
            neighbourOptionB = rootView.findViewById(R.id.neighbourOptionB);
            neighbourOptionC = rootView.findViewById(R.id.neighbourOptionC);

            /* for results page */
            resultsImage = rootView.findViewById(R.id.imageView2);
            titleTextView9 = rootView.findViewById(R.id.textView9);
            neighbourOptionD = rootView.findViewById(R.id.neighbourOptionD);
            neighbourQuestionTextView = rootView.findViewById(R.id.neighbourQuestionTextView);
            continentOptionATEXTTextView = rootView.findViewById(R.id.continentOptionATEXTTextView);
            continentOptionBTEXTTextView = rootView.findViewById(R.id.continentOptionBTEXTTextView);
            continentOptionCTEXTTextView = rootView.findViewById(R.id.continentOptionCTEXTTextView);

            neighbourOptionATEXTTextView = rootView.findViewById(R.id.neighbourOptionATEXTTextView);
            neighbourOptionBTEXTTextView = rootView.findViewById(R.id.neighbourOptionBTEXTTextView);
            neighbourOptionCTEXTTextView = rootView.findViewById(R.id.neighbourOptionCTEXTTextView);
            neighbourOptionDTEXTTextView = rootView.findViewById(R.id.neighbourOptionDTEXTTextView);

            swipeQuestionsStatusTextView = rootView.findViewById(R.id.swipeQuestionsStatusTextView);
            swipeInstructionsTextView = rootView.findViewById(R.id.swipeInstructionsTextView);
            quizInfo2TextView = rootView.findViewById(R.id.quizInfo2TextView);
            goToMainPage = rootView.findViewById(R.id.goToMainPage);
            question1LinearLayout = rootView.findViewById(R.id.question1LinearLayout);
            questionsFragmentScrollView = rootView.findViewById(R.id.questionsFragmentScrollView);


            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (QuizQuestions.class.isInstance(getActivity())) {

                if(mImageNum < 7) {

                    final String genericQuestion = randomQuestions[mImageNum - 1];
                    goToMainPage.setVisibility(View.GONE);

                    /* randomize continents option */
                    ArrayList continentsOptions = new ArrayList();
                    continentsOptions.add(otherContinentOptions[mImageNum - 1][0]);
                    continentsOptions.add(otherContinentOptions[mImageNum - 1][1]);
                    continentsOptions.add(randomCorrectContinents[mImageNum - 1]);

                    Collections.shuffle(continentsOptions); // randomize content

                    /* randomize neighbours option */
                    ArrayList neighboursOptions = new ArrayList();
                    neighboursOptions.add(otherNeighbourOptions[mImageNum - 1][0]);
                    neighboursOptions.add(otherNeighbourOptions[mImageNum - 1][1]);

                    if (randomCorrectNeighbours[mImageNum - 1].trim().equals("No Neighbour")) {
                        neighboursOptions.add(thirdNeighbourOption);
                    } else {
                        ArrayList correctNeighbours = new ArrayList();
                        for (int j = 0; j < listOfNeighbours.length; j++) {
                            correctNeighbours.add(listOfNeighbours[j]);
                        }
                        Collections.shuffle(correctNeighbours);
                        neighboursOptions.add(correctNeighbours.get(0));
                    }

                    Collections.shuffle(neighboursOptions); // randomize content

                    //swipeQuestionsStatusTextView.setText(mImageNum - 1);
                    int statusValue = mImageNum;

                    if (DEBUG)
                        Log.e(DEBUG_TAG, "Fragment.onActivityCreated random questions:" + randomQuestions[mImageNum - 1]);

                    ((QuizQuestions) getActivity()).loadView(continentQuestionTextView, genericQuestion, continentOptionA, continentsOptions.get(0).toString(),
                            continentOptionB, continentsOptions.get(1).toString(), continentOptionC, continentsOptions.get(2).toString(),
                            neighbourOptionA, neighboursOptions.get(0).toString(), neighbourOptionB, neighboursOptions.get(1).toString(),
                            neighbourOptionC, neighboursOptions.get(2).toString(), swipeQuestionsStatusTextView, Integer.toString(statusValue)
                    );
                } else {


//                    continentQuestionTextView.setVisibility(View.GONE);
//                    continentQuestionTextView.setText("Results");
//                    continentQuestionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80f);
//                    continentQuestionTextView.setTextColor(getResources().getColor(R.color.colorAccent));
//                    continentQuestionTextView.setLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

//                    resultsImage.setImageResource(R.drawable.trophy);
//                    titleTextView9.setText(" ");
//                    titleTextView9.setVisibility(View.GONE);
//
//                    continentOptionA.setVisibility(View.GONE);
//                    continentOptionB.setVisibility(View.GONE);
//                    continentOptionC.setVisibility(View.GONE);
//
//                    neighbourOptionA.setVisibility(View.GONE);
//                    neighbourOptionB.setVisibility(View.GONE);
//                    neighbourOptionC.setVisibility(View.GONE);
//                    neighbourOptionD.setVisibility(View.GONE);
//
//                    continentOptionATEXTTextView.setVisibility(View.GONE);
//                    continentOptionBTEXTTextView.setVisibility(View.GONE);
//                    continentOptionCTEXTTextView.setVisibility(View.GONE);
//
//                    neighbourOptionATEXTTextView.setVisibility(View.GONE);
//                    neighbourOptionBTEXTTextView.setVisibility(View.GONE);
//                    neighbourOptionCTEXTTextView.setVisibility(View.GONE);
//                    neighbourOptionDTEXTTextView.setVisibility(View.GONE);
//
//                    swipeQuestionsStatusTextView.setVisibility(View.GONE);
//                    swipeInstructionsTextView.setVisibility(View.GONE);
//                    quizInfo2TextView.setVisibility(View.GONE);
//
//                    neighbourQuestionTextView.setText("DISPLAY SCORES HERE"); // this will be used to display the scores
//                    neighbourQuestionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,80f);

                    questionsFragmentScrollView.setVisibility(View.GONE);

                    ImageView trophy = new ImageView(getContext());
                    trophy.setImageResource(R.drawable.trophy);

                    TextView resutlsText = new TextView(getContext());
                    resutlsText.setText("!!!Congratulations!!!");
                    resutlsText.setAlpha(1);
                    resutlsText.setTypeface(resutlsText.getTypeface(),Typeface.BOLD_ITALIC);
                    resutlsText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    resutlsText.setGravity(Gravity.CENTER);
                    resutlsText.setTextColor(getResources().getColor(R.color.colorAccent));
                    resutlsText.setTextSize(45);

                    TextView scoreValue = new TextView(getContext());
                    LinearLayout.LayoutParams scoreValueLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    scoreValue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    scoreValue.setTextColor(getResources().getColor(R.color.colorSomethingYellow));
                    scoreValue.setText("5/6");
                    scoreValue.setTypeface(scoreValue.getTypeface(),Typeface.ITALIC);
                    scoreValue.setLayoutParams(scoreValueLayoutParams);
                    //scoreValue.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));
                   // scoreValue.setPadding(0,100,0,0);
                    scoreValue.setTextSize(100);

                    Button goToMainPage = new Button(getContext());
                    LinearLayout.LayoutParams buttonParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    buttonParameters.setMargins(0,200,0,0);
                    goToMainPage.setLayoutParams(buttonParameters);
                    goToMainPage.setTextColor(getResources().getColor(R.color.colorWhite));
                    goToMainPage.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                    goToMainPage.setText("Go To Main Page");

                    LinearLayout linearLayoutResultsPage = new LinearLayout(getContext());
                    linearLayoutResultsPage.setOrientation(LinearLayout.VERTICAL);
                    //linearLayoutResultsPage.setBackgroundColor(getResources().getColor(R.color.blueEnough));
                    //linearLayoutResultsPage.setAlpha((float) 0.9);
                    linearLayoutResultsPage.setPadding(15,100,15,50);
                    linearLayoutResultsPage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayoutResultsPage.setGravity(Gravity.CENTER);

                    linearLayoutResultsPage.addView(trophy);
                    linearLayoutResultsPage.addView(resutlsText);
                    linearLayoutResultsPage.addView(scoreValue);
                    linearLayoutResultsPage.addView(goToMainPage);

                    FrameLayout frameLayout = getView().findViewById(R.id.fragmentXML);
                    frameLayout.setBackgroundResource(R.drawable.map);
                    //frameLayout.setAlpha((float)0.7);
                    frameLayout.addView(linearLayoutResultsPage);

                    ((QuizQuestions) getActivity()).onClickButtonGoToMainPage(goToMainPage);

//                    TextView resutlsText = new TextView(getContext());
//                    resutlsText.setText("Results");
//                    resutlsText.setTextColor(getResources().getColor(R.color.colorWhite));
//                    resutlsText.setTextSize(80);
//                    resutlsText.setVisibility(View.VISIBLE);
//
//                    /* add a linear layout in scroll view to add buttons */
//
//                    ScrollView scroll = new ScrollView(getContext());
//                    scroll.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    scroll.setAlpha((float)0.7);
//                    scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                            LinearLayout.LayoutParams.MATCH_PARENT));
//                    scroll.addView(resutlsText);
//
//                    getActivity().addContentView(scroll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

                }
            }
        }
    }
}
