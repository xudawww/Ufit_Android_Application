package shadefoundry.u_fit.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.R;

public class    ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{
    private Context mCtxt;
    private ArrayList<Exercise> exerciseList;
    public ExerciseAdapter(Context mCtxt,ArrayList<Exercise> exerciseList){
        this.mCtxt = mCtxt;
        this.exerciseList = exerciseList;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater inflater = LayoutInflater.from(mCtxt);
        View view = inflater.inflate(R.layout.list_layout,parent,false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExerciseViewHolder holder, int position){
        final Exercise exercise = exerciseList.get(position);
        //bind view data with viewholder views
        holder.exerciseTitle.setText(exercise.getTitle());
        holder.exerciseDescription.setText(exercise.getCategory());
        new DownLoadImageTask(holder.exerciseThumbnail).execute(exercise.getImageUrl());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
                //Toast.makeText(mCtxt.getApplicationContext(),"Tutorial for "+exercise.getTitle(),Toast.LENGTH_SHORT).show();
                //TODO: replace with custom view to display tutorial thumbnail
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(exercise.getDescription())
                        .setTitle(exercise.getTitle())
                        .setPositiveButton("Done",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
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
    public int getItemCount(){
        return exerciseList.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseTitle,exerciseDescription;
        Button tutorialButton;
        ImageView exerciseThumbnail;
        RelativeLayout layout;
        //add layout here

        public ExerciseViewHolder(View itemView){
            super(itemView);
            exerciseTitle = itemView.findViewById(R.id.txt_routineTitle);
            exerciseDescription = itemView.findViewById(R.id.txt_timeFrame);
            tutorialButton = itemView.findViewById(R.id.btn_tutorial);
            exerciseThumbnail = itemView.findViewById(R.id.img_PRExerciseThumbnail);
            layout=itemView.findViewById(R.id.layout_homeListLayout);
        }
    }
}