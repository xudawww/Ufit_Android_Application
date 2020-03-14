package shadefoundry.u_fit.Adapters;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shadefoundry.u_fit.Activities.MainActivity;
import shadefoundry.u_fit.Activities.RoutineInformationActivity;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;

public class EditRoutineAdapter extends RecyclerView.Adapter<EditRoutineAdapter.ERViewHolder> {
    //this adapter fills in the list of exercises that the user has
    //in their selected routine from the routineInformationActivity
    private ArrayList<Exercise> exerciseList;
    private Context context;
    private String routineid;
    private DatabaseReference mDatabase;

    public EditRoutineAdapter(ArrayList<Exercise>exercises,String routineId){
        this.exerciseList = exercises;
        routineid = routineId;
    }

    @Override
    public ERViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        Context mCtxt = parent.getContext();
        this.context = mCtxt;
        LayoutInflater inflater = LayoutInflater.from(mCtxt);
        View view = inflater.inflate(R.layout.list_routine_exercises, parent,false);

        return new ERViewHolder(view);
    }
    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }


        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();

                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onBindViewHolder(final ERViewHolder holder,int position){
        final Exercise exercise = exerciseList.get(position);
        holder.title.setText(exercise.getTitle());
        holder.category.setText(exercise.getCategory());
        holder.reps.setText("Reps: "+exercise.getReps());
        holder.sets.setText("Sets: "+exercise.getSets());

        //TODO: fix image not appearing
        holder.thumb.setImageResource(0);
        new EditRoutineAdapter.DownLoadImageTask(holder.thumb).execute(exercise.getImageUrl());
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //todo: open exercise config
                //opens dialog that will you configure reps, sets, delete activity, etc.
                //for now it just shows you the details since custom dialogs are hard
                Log.d("-0-","---");
                final   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText editSets = new EditText(context);
                editSets.setHint("Set sets");
                layout.addView(editSets);

                final EditText editReps = new EditText(context);
                editReps.setHint("Set reps");
                layout.addView(editReps);

                builder.setView(layout);


                builder.setPositiveButton("Comfirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase= FirebaseDatabase.getInstance().getReference();
                        exercise.setSets(Long.parseLong(editSets.getText().toString()));
                        exercise.setReps(Long.parseLong(editReps.getText().toString()));
                        mDatabase.child("exercise").child(routineid).child(exercise.getExerciseID()).setValue(exercise);

                        Toast.makeText(context,"successfully updated",Toast.LENGTH_SHORT).show();
                        holder.title.setText(exercise.getTitle());
                        holder.category.setText(exercise.getCategory());
                        holder.reps.setText("Reps: "+exercise.getReps());
                        holder.sets.setText("Sets: "+exercise.getSets());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase= FirebaseDatabase.getInstance().getReference();

                        mDatabase.child("exercise").child(routineid).child(exercise.getExerciseID()).removeValue();
                        Toast.makeText(context,"successfully deleted",Toast.LENGTH_SHORT).show();
                        


                        //do nothing
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                //bring up dialog to delete item
                return true;
            }
        });
    }

    @Override
    public int getItemCount(){return exerciseList.size();}

    class ERViewHolder extends RecyclerView.ViewHolder{
        TextView title,category,reps,sets;
        ImageView thumb;
        RelativeLayout layout;

        public ERViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.txt_exerciseBrowserTitle);
            category = view.findViewById(R.id.txt_exerciseBrowserCategory);
            reps = view.findViewById(R.id.txt_routineExerciseReps);
            sets = view.findViewById(R.id.txt_routineExerciseSets);
            thumb = view.findViewById(R.id.img_exerciseBrowserThumb);
            layout = view.findViewById(R.id.layout_editRoutineExercises);
        }
    }




}
