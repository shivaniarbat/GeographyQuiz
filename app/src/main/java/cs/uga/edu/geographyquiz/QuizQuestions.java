package cs.uga.edu.geographyquiz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class QuizQuestions extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public static String DEBUG_TAG = "QuizQuestions";
    private GeographyQuizData geographyQuizData = null;
    static boolean DEBUG = false;


    public final static String[] listOfContinents = new String[]{
            "Asia", "North America", "Europe",
            "Africa", "Oceania", "South America",
            "Australia"
    };

    public static String[] randomCountries = new String[6];
    public static String[] randomQuestions = new String[6];
    public static String[] randomCorrectNeighbours = new String[6];
    public static String[] randomCorrectContinents = new String[6];
    public static long[] randomCountriesID = new long[6];
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
    public static int quizScore = 0;
    public static int[] saveInstanceStateRandomSelections = new int[6];
    public static ArrayList<String> listofNeighboursList = new ArrayList<>();
    public static int[] randomIntegers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        getSupportActionBar().hide();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), randomCountries.length + 2);
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



        if(savedInstanceState != null){
            saveInstanceStateRandomSelections = savedInstanceState.getIntArray("saveInstanceStateRandomSelections");
            System.out.println("RANDOM SELECTIONS:" + saveInstanceStateRandomSelections.toString());
            for(int i = 0;i < saveInstanceStateRandomSelections.length;i++){
                System.out.println("RANDOM SELECTIONS values:" + saveInstanceStateRandomSelections[i]);
            }
        } else {
            randomIntegers = generateRandomInteger(6,1,195);
            saveInstanceStateRandomSelections = randomIntegers;
            /* populate arrays for values of the random 6 set data generated */
            for(int i = 0;i < 6;i++){
                randomCountries[i] = entriesInTable.get(randomIntegers[i]).getCountryName();
                randomCorrectContinents[i] = entriesInTable.get(randomIntegers[i]).getContinent();
                randomQuestions[i] = entriesInTable.get(randomIntegers[i]).getQuestion();
                randomCorrectNeighbours[i] = entriesInTable.get(randomIntegers[i]).getNeighbours();
                randomCountriesID[i] = entriesInTable.get(randomIntegers[i]).getId();

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
                for(int idx = 0 ;idx < listOfNeighbours.length; idx++){
                    listofNeighboursList.add(listOfNeighbours[idx]);
                }

                System.out.println("LIST OF NEIGHBOURS:" + Arrays.toString(listOfNeighbours));

           /* while (neighbourFlag){
                randomNeighbourId = generateRandomInteger(2,1,195);
                for(int idx = 0;idx < listOfNeighbours.length ; idx++ ){
                    if(listOfNeighbours[idx].equals(countryNames.get(randomNeighbourId[0] - 1))
                            || listOfNeighbours[idx].equals(countryNames.get(randomNeighbourId[1] - 1))
                          //  || listOfNeighbours[0].equals("No Neighbour")
                    ){
                        neighbourFlag = true;
                    } else {
//                        if(!countryNames.get(randomNeighbourId[0] - 1).equals("No Neighbour"))
                        if(!listOfNeighbours[idx].equals("No Neighbour"))
                        otherNeighbourOptions[i][0] = countryNames.get(randomNeighbourId[0] - 1);

//                        if(!countryNames.get(randomNeighbourId[1] - 1).equals("No Neighbour"))
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

            }*/

                while(neighbourFlag){
                    randomNeighbourId = generateRandomInteger(2,1,195);
                    if(listofNeighboursList.contains(countryNames.get(randomNeighbourId[0] - 1))
                            && listofNeighboursList.contains(countryNames.get(randomNeighbourId[1] - 1))
                    ){
                        neighbourFlag= true;
                    } else {
                        otherNeighbourOptions[i][0] = countryNames.get(randomNeighbourId[0] - 1);
                        otherNeighbourOptions[i][1] = countryNames.get(randomNeighbourId[1] - 1);

                        if(randomCorrectNeighbours[i].trim().equals("No Neighbour")){
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

        /*mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), randomCountries.length + 2);
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
*/
        /* generate random 6 numbers from range of 1 to 195 */
//        randomIntegers = generateRandomInteger(6,1,195);

//        saveInstanceStateRandomSelections = randomIntegers;

       /* *//* populate arrays for values of the random 6 set data generated *//*
        for(int i = 0;i < 6;i++){
            randomCountries[i] = entriesInTable.get(randomIntegers[i]).getCountryName();
            randomCorrectContinents[i] = entriesInTable.get(randomIntegers[i]).getContinent();
            randomQuestions[i] = entriesInTable.get(randomIntegers[i]).getQuestion();
            randomCorrectNeighbours[i] = entriesInTable.get(randomIntegers[i]).getNeighbours();
            randomCountriesID[i] = entriesInTable.get(randomIntegers[i]).getId();

            *//* select two random continents other than correct one *//*
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

            *//* select two random neighbours other than correct one *//*
            neighbourFlag = true;
            listOfNeighbours = randomCorrectNeighbours[i].split(";");
            for(int idx = 0 ;idx < listOfNeighbours.length; idx++){
                listofNeighboursList.add(listOfNeighbours[idx]);
            }

            System.out.println("LIST OF NEIGHBOURS:" + Arrays.toString(listOfNeighbours));

           *//* while (neighbourFlag){
                randomNeighbourId = generateRandomInteger(2,1,195);
                for(int idx = 0;idx < listOfNeighbours.length ; idx++ ){
                    if(listOfNeighbours[idx].equals(countryNames.get(randomNeighbourId[0] - 1))
                            || listOfNeighbours[idx].equals(countryNames.get(randomNeighbourId[1] - 1))
                          //  || listOfNeighbours[0].equals("No Neighbour")
                    ){
                        neighbourFlag = true;
                    } else {
//                        if(!countryNames.get(randomNeighbourId[0] - 1).equals("No Neighbour"))
                        if(!listOfNeighbours[idx].equals("No Neighbour"))
                        otherNeighbourOptions[i][0] = countryNames.get(randomNeighbourId[0] - 1);

//                        if(!countryNames.get(randomNeighbourId[1] - 1).equals("No Neighbour"))
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

            }*//*

           while(neighbourFlag){
               randomNeighbourId = generateRandomInteger(2,1,195);
               if(listofNeighboursList.contains(countryNames.get(randomNeighbourId[0] - 1))
                       && listofNeighboursList.contains(countryNames.get(randomNeighbourId[1] - 1))
               ){
                 neighbourFlag= true;
               } else {
                   otherNeighbourOptions[i][0] = countryNames.get(randomNeighbourId[0] - 1);
                   otherNeighbourOptions[i][1] = countryNames.get(randomNeighbourId[1] - 1);

                   if(randomCorrectNeighbours[i].trim().equals("No Neighbour")){
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

        }*/

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("saveInstanceStateRandomSelections",saveInstanceStateRandomSelections);
        Log.e(DEBUG_TAG,"QuizQuestions.onSaveInstanceState() :" + saveInstanceStateRandomSelections);
    }


    @Override
    public void onResume() {
        Log.d( DEBUG_TAG, "QuizQuestions.onResume()" );
        if( geographyQuizData != null )
            geographyQuizData.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d( DEBUG_TAG, "QuizQuestions.onPause()" );
        if( geographyQuizData != null )
            geographyQuizData.close();
        super.onPause();
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
        private RadioButton neighbourOptionD;
        private TextView swipeQuestionsStatusTextView;
        private Button goToMainPage;
        private ScrollView questionsFragmentScrollView;
        private RadioGroup continentRadioGroup;
        private RadioGroup neighbourRadioGroup;
        public static int quizScore = 0;
        public static String correctContinentAnswer;
        public static String correctNeighbourAnswer;
        public static QuizResultTableEntry quizQuestionToLoadToDB = new QuizResultTableEntry();
        public static GeographyQuizData geographyQuizData = null;


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
            geographyQuizData = new GeographyQuizData(getContext());
            if (getArguments() != null) {
                mImageNum = getArguments().getInt(ARG_SECTION_NUMBER);
            } else {
                mImageNum = -1;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz_questions, container, false);


            continentQuestionTextView = (TextView) rootView.findViewById(R.id.continentQuestionTextView);
            continentOptionA = rootView.findViewById(R.id.continentOptionA);
            continentOptionB = rootView.findViewById(R.id.continentOptionB);
            continentOptionC = rootView.findViewById(R.id.continentOptionC);

            neighbourOptionA = rootView.findViewById(R.id.neighbourOptionA);
            neighbourOptionB = rootView.findViewById(R.id.neighbourOptionB);
            neighbourOptionC = rootView.findViewById(R.id.neighbourOptionC);
            neighbourOptionD = rootView.findViewById(R.id.neighbourOptionD);

            swipeQuestionsStatusTextView = rootView.findViewById(R.id.swipeQuestionsStatusTextView);
            goToMainPage = rootView.findViewById(R.id.goToMainPage);
            questionsFragmentScrollView = rootView.findViewById(R.id.questionsFragmentScrollView);

            continentRadioGroup = rootView.findViewById(R.id.continentRadioGroup);
            neighbourRadioGroup = rootView.findViewById(R.id.neighbourRadioGroup);

            /* add listeners on radio button to check for the option */
            continentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch ((checkedId)) {
                        case -1:
                            Log.e(DEBUG_TAG, "continents choices are cleared");
                            break;
                        case R.id.continentOptionA:
                            if (continentOptionA.getText().toString().trim().equals(randomCorrectContinents[mImageNum - 1])) {
                                System.out.println("CORRECT ANSWER CONTINENT SELECTED : " + continentOptionA.getText());
                                quizScore++;
                            }
                            break;
                        case R.id.continentOptionB:
                            if (continentOptionB.getText().toString().trim().equals(randomCorrectContinents[mImageNum - 1])) {
                                System.out.println("CORRECT ANSWER CONTINENT SELECTED : " + continentOptionB.getText());
                                quizScore++;
                            }
                            break;
                        case R.id.continentOptionC:
                            if (continentOptionC.getText().toString().trim().equals(randomCorrectContinents[mImageNum - 1])) {
                                System.out.println("CORRECT ANSWER CONTINENT SELECTED : " + continentOptionC.getText());
                                quizScore++;
                            }
                            break;
                        default:
                            Log.e(DEBUG_TAG, "Default option for continent");
                            break;
                    }

                }
            });

            /* add listeners on neighbours radio button to check for the option */
            neighbourRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    listOfNeighbours = randomCorrectNeighbours[mImageNum - 1].split(";");
                    ArrayList correctNeighboursInListerner = new ArrayList();
                    for (int j = 0; j < listOfNeighbours.length; j++) {
                        correctNeighboursInListerner.add(listOfNeighbours[j]);
                    }

                    System.out.println("CORRECT ANSWER LIST FOR NEIGHBOURS TO CHECK THROUGH:" + correctNeighboursInListerner.toString());
                    switch ((checkedId)) {
                        case -1:
                            Log.e(DEBUG_TAG, "neighbours choices are cleared");
                            break;
                        case R.id.neighbourOptionA:
                            if (correctNeighboursInListerner.contains(neighbourOptionA.getText().toString().trim())) {
                                System.out.println("CORRECT ANSWER NEIGHBOUR SELECTED : " + neighbourOptionA.getText());
                                quizScore++;
                            }
                            break;
                        case R.id.neighbourOptionB:
                            if (correctNeighboursInListerner.contains(neighbourOptionB.getText().toString().trim())) {
                                System.out.println("CORRECT ANSWER NEIGHBOUR SELECTED : " + neighbourOptionB.getText());
                                quizScore++;
                            }
                            break;
                        case R.id.neighbourOptionC:
                            if (correctNeighboursInListerner.contains(neighbourOptionC.getText().toString().trim())) {
                                System.out.println("CORRECT ANSWER NEIGHBOUR SELECTED : " + neighbourOptionC.getText());
                                quizScore++;
                            }
                            break;
                        case R.id.neighbourOptionD:
                            if (correctNeighboursInListerner.contains(neighbourOptionD.getText().toString().trim())) {
                                System.out.println("CORRECT ANSWER NEIGHBOUR SELECTED : " + neighbourOptionD.getText());
                                quizScore++;
                            }
                            break;
                        default:
                            Log.e(DEBUG_TAG, "Default option for neighbour");
                            break;
                    }
                }
            });

            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
//            quizScore = 0;

            if(savedInstanceState != null){
                saveInstanceStateRandomSelections = savedInstanceState.getIntArray("saveInstanceStateRandomSelections");
            }

            if (QuizQuestions.class.isInstance(getActivity())) {

                if (mImageNum < 7) {

                    final String genericQuestion = randomQuestions[mImageNum - 1];
                    goToMainPage.setVisibility(View.GONE);

                    /* randomize continents option */
                    ArrayList continentsOptions = new ArrayList();
                    continentsOptions.add(otherContinentOptions[mImageNum - 1][0]);
                    continentsOptions.add(otherContinentOptions[mImageNum - 1][1]);
                    continentsOptions.add(randomCorrectContinents[mImageNum - 1]);
                    correctContinentAnswer = randomCorrectContinents[mImageNum - 1];

                    if (DEBUG) {
                        System.out.println("CORRECT CONTINENT ANSWER for ::::::" + mImageNum + " is " + correctContinentAnswer);
                        System.out.println("OTHER OPTION CONTINENT for " + mImageNum + " is " + otherContinentOptions[mImageNum - 1][0]);
                        System.out.println("OTHER OPTION CONTINENT for " + mImageNum + " is " + otherContinentOptions[mImageNum - 1][1]);
                    }

                    Collections.shuffle(continentsOptions); // randomize content

                    /* randomize neighbours option */
                    ArrayList neighboursOptions = new ArrayList();
                    neighboursOptions.add(otherNeighbourOptions[mImageNum - 1][0]);
                    neighboursOptions.add(otherNeighbourOptions[mImageNum - 1][1]);
                    correctNeighbourAnswer = randomCorrectNeighbours[mImageNum - 1];

                    if (randomCorrectNeighbours[mImageNum - 1].trim().equals("No Neighbour")) {
                        neighboursOptions.add(thirdNeighbourOption);
                    } else {
                        listOfNeighbours = randomCorrectNeighbours[mImageNum - 1].split(";");
                        ArrayList correctNeighbours = new ArrayList();
                        for (int j = 0; j < listOfNeighbours.length; j++) {
                            correctNeighbours.add(listOfNeighbours[j]);
                            System.out.print(listOfNeighbours[j] + "  ");
                        }
                        Collections.shuffle(correctNeighbours);
                        neighboursOptions.add(correctNeighbours.get(0));
                        System.out.println("CORRECT NEIGHBOUR:" + correctNeighbours.get(0));
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


                    switch (mImageNum - 1) {
                        case 0:
                            quizQuestionToLoadToDB.setQ1(randomCountriesID[mImageNum - 1]);
                            break;
                        case 1:
                            quizQuestionToLoadToDB.setQ2(randomCountriesID[mImageNum - 1]);
                            break;
                        case 2:
                            quizQuestionToLoadToDB.setQ3(randomCountriesID[mImageNum - 1]);
                            break;
                        case 3:
                            quizQuestionToLoadToDB.setQ4(randomCountriesID[mImageNum - 1]);
                            break;
                        case 4:
                            quizQuestionToLoadToDB.setQ5(randomCountriesID[mImageNum - 1]);
                            break;
                        case 5:
                            quizQuestionToLoadToDB.setQ6(randomCountriesID[mImageNum - 1]);
                            break;
                    }

                } else {

                    if (mImageNum == 7) {
                        questionsFragmentScrollView.setVisibility(View.GONE);

                        FrameLayout frameLayout = getView().findViewById(R.id.fragmentXML);
                        frameLayout.setBackgroundResource(R.drawable.map);

                        quizQuestionToLoadToDB.setScore(quizScore);

//                        geographyQuizData.open();

                        /*new QuizResultDBWriterTask().execute(new QuizResultTableEntry(Calendar.getInstance().getTime().toString(),
                                randomCountriesID[0], randomCountriesID[1], randomCountriesID[2],
                                randomCountriesID[3], randomCountriesID[4], randomCountriesID[5],
                                quizScore
                        ));*/

//                        geographyQuizData.close();
                        //frameLayout.addView();

                    } else if (mImageNum > 7) {

                        questionsFragmentScrollView.setVisibility(View.GONE);

                        new QuizResultDBWriterTask().execute(new QuizResultTableEntry(Calendar.getInstance().getTime().toString(),
                                randomCountriesID[0], randomCountriesID[1], randomCountriesID[2],
                                randomCountriesID[3], randomCountriesID[4], randomCountriesID[5],
                                quizScore
                        ));

                        ImageView trophy = new ImageView(getContext());
                        trophy.setImageResource(R.drawable.trophy);

                        TextView resutlsText = new TextView(getContext());
                        resutlsText.setText("!!!Congratulations!!!");
                        resutlsText.setAlpha(1);
                        resutlsText.setTypeface(resutlsText.getTypeface(), Typeface.BOLD_ITALIC);
                        resutlsText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        resutlsText.setGravity(Gravity.CENTER);
                        resutlsText.setTextColor(getResources().getColor(R.color.colorAccent));
                        resutlsText.setTextSize(45);

                        TextView scoreValue = new TextView(getContext());
                        LinearLayout.LayoutParams scoreValueLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        scoreValue.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        scoreValue.setTextColor(getResources().getColor(R.color.colorSomethingYellow));
                        scoreValue.setText(Integer.toString(quizScore) + " / 12");
                        scoreValue.setTypeface(scoreValue.getTypeface(), Typeface.ITALIC);
                        scoreValue.setLayoutParams(scoreValueLayoutParams);
                        scoreValue.setTextSize(100);

                        Button goToMainPage = new Button(getContext());
                        LinearLayout.LayoutParams buttonParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        buttonParameters.setMargins(0, 200, 0, 0);
                        goToMainPage.setLayoutParams(buttonParameters);
                        goToMainPage.setTextColor(getResources().getColor(R.color.colorWhite));
                        goToMainPage.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                        goToMainPage.setText("Go To Main Page");

                        LinearLayout linearLayoutResultsPage = new LinearLayout(getContext());
                        linearLayoutResultsPage.setOrientation(LinearLayout.VERTICAL);
                        linearLayoutResultsPage.setPadding(15, 100, 15, 50);
                        linearLayoutResultsPage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        linearLayoutResultsPage.setGravity(Gravity.CENTER);

                        linearLayoutResultsPage.addView(trophy);
                        linearLayoutResultsPage.addView(resutlsText);
                        linearLayoutResultsPage.addView(scoreValue);
                        linearLayoutResultsPage.addView(goToMainPage);

                        FrameLayout frameLayout = new FrameLayout(getContext());//getView().findViewById(R.id.fragmentXML);
                        frameLayout.setBackgroundResource(R.drawable.map);
                        frameLayout.addView(linearLayoutResultsPage);

                        ((QuizQuestions) getActivity()).onClickButtonGoToMainPage(goToMainPage);

                        ((QuizQuestions) getActivity()).addContentView(frameLayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT));
                        quizScore = 0;
                    }

                }
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


        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putIntArray("saveInstanceStateRandomSelections",saveInstanceStateRandomSelections);
        }
    }

    private static class QuizResultDBWriterTask extends AsyncTask<QuizResultTableEntry, Void, QuizResultTableEntry> {

        // This method will run as a background process to write into db.
        @Override
        protected QuizResultTableEntry doInBackground(QuizResultTableEntry... quizResultTableEntry) {
            GeographyQuizData.storequizEntry(quizResultTableEntry[0]);
            return quizResultTableEntry[0];
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.
        @Override
        protected void onPostExecute(QuizResultTableEntry quizResultTableEntry) {
            super.onPostExecute(quizResultTableEntry);

            Log.d(DEBUG_TAG, "Quiz Entry saved: " + quizResultTableEntry);
        }
    }
}
