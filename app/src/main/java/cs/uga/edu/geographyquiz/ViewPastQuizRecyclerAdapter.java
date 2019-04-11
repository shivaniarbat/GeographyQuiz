package cs.uga.edu.geographyquiz;

/**
 * @author Apurva Bansode
 * @version 1.0
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViewPastQuizRecyclerAdapter extends RecyclerView.Adapter<ViewPastQuizRecyclerAdapter.QuizResultTableEntryHolder> {
    public static final String DEBUG_TAG = "RecyclerAdapter";
    private List<QuizResultTableEntry> pastQuizzesList;

    public ViewPastQuizRecyclerAdapter( List<QuizResultTableEntry> pastQuizzesList ) {
        this.pastQuizzesList = pastQuizzesList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class QuizResultTableEntryHolder extends RecyclerView.ViewHolder {
        TextView quizId;
        TextView quizDate;
        TextView quizScore;

        public QuizResultTableEntryHolder(View itemView ) {
            super(itemView);

            quizId = (TextView) itemView.findViewById( R.id.quiz_id );
            quizDate = (TextView) itemView.findViewById( R.id.quiz_date );
            quizScore = (TextView) itemView.findViewById( R.id.quiz_score );

        }
    }
    @Override
    public ViewPastQuizRecyclerAdapter.QuizResultTableEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.adapter, null );
        return new QuizResultTableEntryHolder( view );
    }

    @Override
    public void onBindViewHolder( QuizResultTableEntryHolder holder, int position) {
        QuizResultTableEntry quizResultTableEntry = pastQuizzesList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + quizResultTableEntry );

        holder.quizId.setText("Quiz Id:" + Long.toString(quizResultTableEntry.getId()));
        holder.quizDate.setText( "Date : " + quizResultTableEntry.getDate() );
        holder.quizScore.setText( "Score :" + Long.toString(quizResultTableEntry.getScore()) );
    }

    @Override
    public int getItemCount() {
        return pastQuizzesList.size();
    }
}