package shadefoundry.u_fit.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.R;

public class PersonalRecordAdapter extends RecyclerView.Adapter<PersonalRecordAdapter.PRViewHolder> {
    private Context mCtxt;
    private ArrayList<Exercise> personalRecordList;
    public PersonalRecordAdapter(Context mCtxt,ArrayList<Exercise> personalRecords){
        this.mCtxt = mCtxt;
        this.personalRecordList = personalRecords;
    }

    @Override
    public PRViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater inflater = LayoutInflater.from(mCtxt);
        View view = inflater.inflate(R.layout.list_personal_record,parent,false);
        return new PRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PRViewHolder holder,int position){
        final Exercise exercise = personalRecordList.get(position);
        holder.exerciseTitle.setText(exercise.getTitle());
        Log.d("12222222",String.valueOf(exercise.getLastPerformDate()));
        java.util.Date time=new java.util.Date((long)exercise.getLastPerformDate()*1000);
        holder.lastPerformed.setText("Last Performed: "+time.toString());
        holder.personalRecordScore.setText("Best Score: "+exercise.getHighscore());
        //holder.thumbnail.setImageDrawable(mCtxt.getResources().getDrawable(exercise.getImage()));
    }

    @Override
    public int getItemCount(){return personalRecordList.size();}

    class PRViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseTitle,lastPerformed,personalRecordScore;
        //ImageView thumbnail;
        public PRViewHolder(View itemView){
            super(itemView);
            exerciseTitle = itemView.findViewById(R.id.txt_PRExerciseTitle);
            lastPerformed = itemView.findViewById(R.id.txt_lastPerformed);
            personalRecordScore = itemView.findViewById(R.id.txt_personalRecord);
            //thumbnail = itemView.findViewById(R.id.img_PRExerciseThumbnail);
        }
    }
}
