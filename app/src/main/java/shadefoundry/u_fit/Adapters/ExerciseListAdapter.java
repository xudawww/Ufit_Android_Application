package shadefoundry.u_fit.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.R;

import static android.app.Activity.RESULT_OK;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ELViewHolder> {
    private ArrayList<Exercise> exerciseList;
    private ArrayList<Exercise> exerciseList2;
    public ExerciseListAdapter(ArrayList<Exercise> exerciseList,ArrayList<Exercise>exerciseList2){
        this.exerciseList = exerciseList;
        this.exerciseList2 = exerciseList2;
    }

    @Override
    public ELViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        Context mCtxt = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mCtxt);
        View view = inflater.inflate(R.layout.list_exercise_browser,parent,false);
        return new ELViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ELViewHolder holder,int position){
        final Exercise e = exerciseList.get(position);
        new ExerciseListAdapter .DownLoadImageTask(holder.thumb).execute(e.getImageUrl());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
                //Toast.makeText(mCtxt.getApplicationContext(),"Tutorial for "+exercise.getTitle(),Toast.LENGTH_SHORT).show();
                //TODO: replace with custom view to display tutorial thumbnail
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(e.getDescription())
                        .setTitle(e.getTitle())
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
        holder.title.setText(e.getTitle());
        holder.category.setText(e.getCategory());
        //TODO: fix image not appearing

        new ExerciseListAdapter.DownLoadImageTask(holder.thumb).execute(e.getImageUrl());
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                Log.d("ada",e.getExerciseID());
                //open dialog with customization settings to add exercise
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(e.getDescription()).setTitle(e.getTitle());
                builder.setPositiveButton("Add to Routine", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: update values before passing, only do this once there's a custom dialog
                        //return exercise to list
                        Intent intent = new Intent();
                        intent.putExtra("newExercise",e);
                        intent.putExtra("oldlist",exerciseList2);
                        ((Activity)v.getContext()).setResult(RESULT_OK,intent);
                        ((Activity)v.getContext()).finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        //do nothing since we hit cancel
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
    @Override public int getItemCount(){return exerciseList.size();}

    class ELViewHolder extends RecyclerView.ViewHolder{
        TextView title,category;
        ImageView thumb;
        RelativeLayout layout;
        public ELViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.txt_exerciseBrowserTitle);
            category=view.findViewById(R.id.txt_exerciseBrowserCategory);
            thumb = view.findViewById(R.id.img_exerciseBrowserThumb);
            layout = view.findViewById(R.id.layout_exerciseBrowserList);
        }
    }

}
