package shadefoundry.u_fit.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import shadefoundry.u_fit.Adapters.EditRoutineAdapter;
import shadefoundry.u_fit.Adapters.RoutineAdapter;
import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.CommonControl.InternetListener;
import shadefoundry.u_fit.CommonControl.infinitescrolling;
import shadefoundry.u_fit.Fragments.ExerciseFragment;
import shadefoundry.u_fit.Fragments.RoutinesFragment;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.Objects.User;
import shadefoundry.u_fit.R;

public class RoutineInformationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    public FirebaseUser currentuser;
    public String fromwhat;
    Routine routine;
    boolean firstload =true;
    private FirebaseAuth mAuth;
    ArrayList<String> exerlist ;
    EditText txtEndDate,txtRoutineTitle,txtStartDate;
    private boolean ifend=false;
    Switch swPrimary,swOnCalendar;
    RecyclerView recyclerView;
    ArrayList<Exercise> userExercises;
    ArrayList<Exercise> ex1;
    private boolean scrollinglocker = false;
    ArrayList<Routine> userRoutines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        this.currentuser = mAuth.getCurrentUser();
        setContentView(R.layout.activity_routine_information);
        ex1 = new ArrayList<Exercise>();
        exerlist = new  ArrayList<String>();
        userExercises=new ArrayList<Exercise>();
        userRoutines=new ArrayList<Routine>();
        //get the routine object
        Intent intent = getIntent();
        routine = (Routine) intent.getSerializableExtra("routineInfo");
        fromwhat = (String) intent.getSerializableExtra("fromwhat");
        userRoutines =(ArrayList<Routine>) intent.getSerializableExtra("list");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        //populate routine exercise list
        recyclerView = (RecyclerView)findViewById(R.id.lst_RoutineExercises);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //intialize our view objects
        txtRoutineTitle = (EditText)findViewById(R.id.txt_routineTitle);
        swPrimary = (Switch)findViewById(R.id.sw_isPrimaryWorkout);
        swOnCalendar = (Switch)findViewById(R.id.sw_isOnCalendar);
        txtStartDate = (EditText) findViewById(R.id.txt_startDate);
        txtEndDate = (EditText)findViewById(R.id.txt_endDate);
        //populate routine information from routine object
            txtRoutineTitle.setText(routine.getTitle());
            if(routine.isIsprimary()){swPrimary.setChecked(true);}
            if(routine.isOnCalendar()){swOnCalendar.setChecked(true);}
            Calendar c = Calendar.getInstance();
        c.setTimeInMillis(routine.getStarttime());
        txtStartDate.setText(String.valueOf(c.get(Calendar.MONTH)+1)+"/"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(c.get(Calendar.YEAR)));
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(routine.getEndtime());
        txtEndDate.setText(String.valueOf(c2.get(Calendar.MONTH)+1)+"/"+String.valueOf(c2.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(c2.get(Calendar.YEAR)));


        mDatabase.child("exercise").child(routine.getid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDatabase.child("exercise").child(routine.getid()).limitToLast(5).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ex1.clear();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            Exercise e =  postSnapshot.getValue(Exercise .class);
                            Log.d("e2222", e.getExerciseID());
                            ex1.add(e);

                            //Toast.makeText(mCtx.getApplicationContext(),"It Works!",Toast.LENGTH_SHORT).show();
                            //pass routine to new activity and open that shit
                        }
                        Log.d("e2222", String.valueOf(ex1.size()));
                        Collections.reverse(ex1 );
                        final EditRoutineAdapter adapter = new  EditRoutineAdapter( ex1,routine.getid());
                        recyclerView.removeAllViews();
                        recyclerView.setAdapter(adapter);
                        recyclerView.addOnScrollListener(new infinitescrolling() {

                            @Override
                            public void onScrolledToEnd() {
                                InternetController internet = new InternetController();
                                if(internet.chechConnection(RoutineInformationActivity.this)){
                                    Log.d("pop",String.valueOf(ex1.get(ex1.size()-1).getReps()));
                                    if(!scrollinglocker){
                                        scrollinglocker=true;
                                        mDatabase.child("exercise").child(routine.getid()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                                                    Exercise a =  postSnapshot.getValue(Exercise.class);
                                                    if(!a.getExerciseID().equals(ex1.get(ex1.size()-1).getExerciseID())){

                                                        mDatabase.child("exercise").child(routine.getid()).orderByKey().endAt(ex1.get(ex1.size()-1).getExerciseID()).limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            ArrayList<Exercise> exerciselist2 = new ArrayList<Exercise>();

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                exerciselist2.clear();
                                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                                    Exercise a =  postSnapshot.getValue(Exercise.class);
                                                                    exerciselist2.add(a);
                                                                }
                                                                if(exerciselist2.size()>0){
                                                                    exerciselist2.remove(exerciselist2.size()-1);
                                                                    Collections.reverse(exerciselist2);
                                                                    for(Exercise e :exerciselist2){
                                                                        ex1.add(e);
                                                                    }}
                                                                final EditRoutineAdapter adapter = new EditRoutineAdapter( ex1,routine.getid());
                                                                recyclerView.setAdapter(adapter);
                                                                recyclerView.scrollToPosition(ex1.size()-3);
                                                                Toast.makeText(RoutineInformationActivity.this, String.valueOf(ex1.size()),
                                                                        Toast.LENGTH_SHORT).show();
                                                                scrollinglocker=false;}
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });}}
                                else{

                                    Toast.makeText(RoutineInformationActivity.this, "Please Connect Internet!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                        scrollinglocker = false;



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }



                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });




    }

    //turn off the back button so we can't accidentally break the routineList
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("updatedRoutine",routine);
        intent.putExtra("backfromroutine","backfromroutine");
        intent.putExtra("oldroutinelist",userRoutines);
        setResult(RESULT_OK,intent);
        finish();

    }
    public void delete(View view){
        InternetController internet = new InternetController();
        if(internet.chechConnection(this)) {
            if (!routine.getid().equals("blank")) {


                mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).removeValue();

                mDatabase.child("exercises").child(routine.getid()).removeValue();
                Toast.makeText(RoutineInformationActivity.this, "Already removed!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("updatedRoutine",routine);
                intent.putExtra("backfromroutine","delete");
                intent.putExtra("oldroutinelist",userRoutines);
                setResult(RESULT_OK,intent);
                finish();


            }

        }
        else{Toast.makeText(RoutineInformationActivity.this, "Please connect with internet!", Toast.LENGTH_SHORT).show();}

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        exerlist.clear();
        userExercises.clear();

    }

    public void setRoutineStartDate(View view) {
        //set calendar to current date
        final Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        txtStartDate = (EditText)findViewById(R.id.txt_startDate);
        //open first datepicker dialog
        DatePickerDialog dialog1 = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //set the date values on the view
                        startDate.setYear(year);
                        startDate.setMonth(month+1);
                        startDate.setDate(day);
                        txtStartDate.setText(month+1+"/"+day+"/"+year);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        try{
                            Date date = sdf.parse(month+"/"+day+"/"+year);
                            routine.setEndtime(date.getTime());
                        }
                        catch (Exception e){}
                    }
                },year,month,dayOfMonth);
        dialog1.show();
    }

    public void setRoutineEndDate(View view) {
        final Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        txtEndDate = (EditText)findViewById(R.id.txt_endDate);
        DatePickerDialog dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endDate.setYear(year);
                endDate.setMonth(month+1);
                endDate.setDate(day);
                txtEndDate.setText(month+1+"/"+day+"/"+year);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                try{
                Date date = sdf.parse(month+"/"+day+"/"+year);
                routine.setEndtime(date.getTime());
                }
                catch (Exception e){}

            }
        },year,month,dayOfMonth);
        dialog2.show();
    }

    public void saveRoutine(View view) {
        InternetController internet = new InternetController();
        if(internet.chechConnection(this)){
        scrollinglocker = false;
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date startdate = formatter2.parse(txtStartDate.getText().toString());
            Date enddate = formatter2.parse(txtEndDate.getText().toString());
            if(startdate.compareTo(enddate)>0){


                Toast.makeText(this, "Please dont set the start date larger than end date",
                        Toast.LENGTH_SHORT).show();



            }
            else{
//set routine title since im not doing it anywhere else
                routine.setTitle(txtRoutineTitle.getText().toString());
                SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date d = f.parse(txtStartDate.getText().toString());
                    Date d1 = f.parse(txtEndDate.getText().toString());
                    long milliseconds = d.getTime();
                    long milliseconds2 = d1.getTime();
                    routine.setStarttime(milliseconds);
                    routine.setEndtime(milliseconds2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(routine.getid().equals("blank")) {
                    String key2 = mDatabase.child("routines").child(currentuser.getUid()).push().getKey();
                    routine.setRoutineId(key2);
                    for (Exercise e : ex1) {
                        String key = mDatabase.child("exercise").child(key2).push().getKey();
                        e.setExerciseID(key);
                        mDatabase.child("exercise").child(key2).child(key).setValue(e);

//            mDatabase.child("exercise").child(routine.getid()).child(key).child("category").setValue(e.getCategory());
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("exerciseID").setValue(key);
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("title").setValue(e.getTitle());
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("description").setValue(e.getDescription());
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("highScore").setValue(e.getHighscore());
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("imageUrl").setValue(e.getImageUrl());
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("isPrimary").setValue(false);
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("onCalendar").setValue(true);
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("reps").setValue(e.getReps());
//            mDatabase.child("exercise").child(routine.getid()).child(key).child("sets").setValue(e.getSets());

                    }
                    long milliseconds;

                    Date today = Calendar.getInstance().getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    long millis = new java.util.Date().getTime();
                    mDatabase.child("routines").child(currentuser.getUid()).child(key2).child("endtime").setValue(millis);
                    mDatabase.child("routines").child(currentuser.getUid()).child(key2).child("id").setValue(key2);
                    mDatabase.child("routines").child(currentuser.getUid()).child(key2).child("isprimary").setValue(routine.isIsprimary());
                    mDatabase.child("routines").child(currentuser.getUid()).child(key2).child("oncalendar").setValue(routine.isOnCalendar());
                    mDatabase.child("routines").child(currentuser.getUid()).child(key2).child("starttime").setValue(millis);
                    mDatabase.child("routines").child(currentuser.getUid()).child(key2).child("title").setValue(routine.getTitle());

                }
                else{
                    Date millis = new Date();

                    mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).child("endtime").setValue(routine.getEndtime());
                    mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).child("id").setValue(routine.getid());
                    mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).child("isprimary").setValue(routine.isIsprimary());
                    mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).child("oncalendar").setValue(routine.isOnCalendar());
                    mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).child("starttime").setValue(routine.getStarttime());
                    mDatabase.child("routines").child(currentuser.getUid()).child(routine.getid()).child("title").setValue(routine.getTitle());

                    for (Exercise e : ex1) {

                        Log.d("here",e.getExerciseID());
                        if (e.getExerciseID().length() <= 3) {
                            String key = mDatabase.child("exercise").child(routine.getid()).push().getKey();
                            e.setExerciseID(key);
                            Log.d("here","here2");
                            mDatabase.child("exercise").child(routine.getid()).child(key).setValue(e);

                        }

                    }
                }
                Toast.makeText(this, "Upload successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("updatedRoutine",routine);
                intent.putExtra("backfromroutine","backfromroutine");
                intent.putExtra("oldroutinelist",userRoutines);
                setResult(RESULT_OK,intent);
                finish();


            }
        }catch (Exception e){ }
        int com = Long.compare(routine.getStarttime(),routine.getEndtime());

        }
    else{
            Toast.makeText(this, "Please connect with Internet", Toast.LENGTH_SHORT).show();

        }}


    public void openExerciseBrowser(View view) {
        //this will open another activity for result, who needs efficiency?
        //temporary exercise list is going to be a single item while we don't have an api.
        //exercises taken from https://wger.de/en/exercise/overview/ for now

        //pull list of exercises from api
        //send them to the exercise list
        ArrayList<String> exenames = new ArrayList<String>();
        exenames.add("e1");
        exenames.add("e2");
        exenames.add("e3");
        for(int i = 0; i < exenames.size(); i++) {
            Log.d("123",exenames.get(i));
            mDatabase.child("exercise").child(exenames.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Exercise a = dataSnapshot.getValue(Exercise.class);
                    a.setExerciseID(dataSnapshot.child("exerciseID").getValue().toString());
                    Log.d("eid",a.getExerciseID());
                    userExercises.add(a);
                    if (userExercises.size() == 3) {
                        Intent intent = new Intent(RoutineInformationActivity.this, ExerciseBrowserActivity.class);
                        intent.putExtra("exerciseList", userExercises);
                        intent.putExtra("exerciseList2", ex1);
                        intent.putExtra("routinefromact", routine);
                        startActivityForResult(intent, 1);

                    }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }
            );
        }

        //open activity for result
    }

    //when we return from the exercise browser
    public void onActivityResult(int reqCode,int resCode,Intent data){
        super.onActivityResult(reqCode,resCode,data);
        if(reqCode==1){
            if(resCode==RESULT_OK){
                //TODO: update routine list with new object
                //get the new exercise
                scrollinglocker = false;
                Exercise e = (Exercise) data.getSerializableExtra("newExercise");
                ArrayList<Exercise> oldexerciselist = (ArrayList<Exercise> ) data.getSerializableExtra("oldlist");
                Log.d("back", String.valueOf(oldexerciselist.size()));
                oldexerciselist.add(0,e);
                EditRoutineAdapter mAdapter = new EditRoutineAdapter(oldexerciselist,routine.getid());
                ex1 = oldexerciselist;
                recyclerView = findViewById(R.id.lst_RoutineExercises);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setIsPrimary(View view) {
        if(swPrimary.isChecked()){
            routine.setIsprimary(true);
        }else{
            routine.setIsprimary(false);
        }
    }

    public void setIsOnCalendar(View view) {
        if(swOnCalendar.isChecked()){
            routine.setOnCalendar(true);
        }else{
            routine.setOnCalendar(false);
        }
    }
}
