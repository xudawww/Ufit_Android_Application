package shadefoundry.u_fit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import shadefoundry.u_fit.Adapters.RoutineAdapter;
import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.CommonControl.infinitescrolling;
import shadefoundry.u_fit.Fragments.RoutinesFragment;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;

public class RoutinelistFromCalendarActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    int starter = 5;
    private DatabaseReference mDatabase;
    private FirebaseUser currentuser;
    private boolean scrollinglocker= false;
    private ArrayList<Routine> userRoutines;
    private  RoutineAdapter adapter;
    private FirebaseAuth mAuth;
    boolean ifend = false;

    public void recursivefinder(){






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();

        intent.putExtra("backfromroutine","calendar");
        intent.putExtra("oldroutinelist",userRoutines);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRoutines = new ArrayList<Routine>();
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        this.currentuser = mAuth.getCurrentUser();
        setContentView(R.layout.activity_routinelist_from_calendar);
        recyclerView = (RecyclerView)findViewById(R.id.calendarroutinelistview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        String date = (String) getIntent().getSerializableExtra("date");
        Log.d("date",date);
        mDatabase.child("routines").child(currentuser.getUid()).limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userRoutines.clear();
                Log.d("here","here");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Routine a =  postSnapshot.getValue(Routine.class);
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(a.getStarttime());
                    if(date.equals(String.valueOf(c.get(Calendar.MONTH)+1)+"/"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(c.get(Calendar.YEAR))))
                    { userRoutines.add(a);}

                }





                Collections.reverse(userRoutines);

                final RoutineAdapter adapter = new RoutineAdapter(RoutinelistFromCalendarActivity.this, userRoutines);

                RoutinelistFromCalendarActivity.this.recyclerView.setAdapter(adapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }



        });


    }
}
