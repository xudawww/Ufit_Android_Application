package shadefoundry.u_fit.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;
import shadefoundry.u_fit.Activities.RoutineInformationActivity;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>{
    private Context mCtx;
    private ArrayList<Routine> routineList;
    private DatabaseReference mDatabase;
    public FirebaseUser currentuser;
    private FirebaseAuth mAuth;
    public RoutineAdapter(Context mCtxt,ArrayList<Routine> routineList){
        this.mCtx = mCtxt;
        this.routineList = routineList;
    }

    @Override
    public RoutineViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_routine,parent,false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RoutineViewHolder holder,int position){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Routine routine = routineList.get(position);

        final FirebaseUser user = this.currentuser;
        mAuth=FirebaseAuth.getInstance();
        this.currentuser = mAuth.getCurrentUser();
        //bind view data with routineHolder data
        holder.routineTitle.setText(routine.getTitle());

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(routine.getStarttime());
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(routine.getEndtime());



        holder.routineTimeFrame.setText(String.valueOf(c.get(Calendar.MONTH)+1)+"/"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(c.get(Calendar.YEAR))+" - "+String.valueOf(c2.get(Calendar.MONTH)+1)+"/"+String.valueOf(c2.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(c2.get(Calendar.YEAR)));
        //add an onclick to the routine so the user can interact with it
        holder.routineListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx.getApplicationContext(), RoutineInformationActivity.class);
                intent.putExtra("routineInfo",routine);
                intent.putExtra("fromwhat","r");
                intent.putExtra("list",routineList);
                //start activity expecting a result
                ((Activity) mCtx).startActivityForResult(intent,1);
            }
        });
        holder.routineListLayout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                //TODO: open dialog to delete
                return true;
            }
        });


        //add 1900 to year to make it show up in a readable format
        //TODO: switch from date to calendar object

    }

    @Override
            public int getItemCount(){return routineList.size();}

    class RoutineViewHolder extends RecyclerView.ViewHolder{
        TextView routineTitle,routineTimeFrame;
        RelativeLayout routineListLayout;

        public RoutineViewHolder(View itemView){
            super(itemView);
            routineTitle = itemView.findViewById(R.id.txt_routineTitle);
            routineTimeFrame = itemView.findViewById(R.id.txt_timeFrame);
            routineListLayout = itemView.findViewById(R.id.routineListLayout);
        }
    }
}
