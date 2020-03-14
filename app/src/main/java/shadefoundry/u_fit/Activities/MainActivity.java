package shadefoundry.u_fit.Activities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import 	androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.renderscript.Sampler;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import shadefoundry.u_fit.CommonControl.InternetListener;
import shadefoundry.u_fit.Fragments.CalendarFragment;
import shadefoundry.u_fit.Fragments.ExerciseFragment;
import shadefoundry.u_fit.Fragments.PersonalRecordFragment;
import shadefoundry.u_fit.Fragments.RoutinesFragment;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.Objects.User;
import shadefoundry.u_fit.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public FirebaseUser currentuser;
    User user;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    private DatabaseReference mDatabase;
    private  String fromwhere="";
    ArrayList<String> exerlist;
    ArrayList<String> routinelist;
    private FirebaseAuth mAuth;
     ArrayList<Routine> userRoutines;
    ArrayList<Routine> userRoutines2;
    ArrayList<Exercise> userExercises;
    ArrayList<Exercise> userExercises2;
    public boolean firstload=true;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = InternetListener.getConnectivityStatus(context);
            if(!status.equals("No Internet")){

               if(fromwhere.equals(""))
               {getalldata();}



            }
            else{
                Toast.makeText(MainActivity.this, "You are offline, some data might no be loaded.",
                        Toast.LENGTH_SHORT).show();
                fromwhere="";

            }
        }



    };

    public Fragment  storeAndReturnFragment ( int fragmentHolderLayoutId, Fragment fragment, Context context, String tag ) {


        FragmentManager manager = ( (AppCompatActivity) context ).getSupportFragmentManager ();
        FragmentTransaction ft = manager.beginTransaction ();

        if (manager.findFragmentByTag ( tag ) == null) { // No fragment in backStack with same tag..
            ft.add ( fragmentHolderLayoutId, fragment, tag );
            ft.addToBackStack ( tag );
            ft.commit ();
            return fragment;
        }

        return getSupportFragmentManager().findFragmentByTag(tag);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.routine_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         Log.d("9099",String.valueOf(item.getItemId()));

        switch (item.getItemId()) {
            case R.id.logout:
                // todo: goto back activity from here
                SharedPreferences prefs = getSharedPreferences("ufituser", MODE_PRIVATE);
                prefs.edit().remove("user").commit();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                startActivity(intent);
                Log.d("1111","here");

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        CountDownLatch done = new CountDownLatch(1);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        userExercises2 = new ArrayList<Exercise>();
        userExercises=new ArrayList<Exercise>();
        userRoutines = new ArrayList<Routine>();
        userRoutines2 = new ArrayList<Routine>();
        mAuth=FirebaseAuth.getInstance();
        this.currentuser = mAuth.getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("ufituser", MODE_PRIVATE);
        prefs.edit().putString("user",currentuser.getUid()).commit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = getDialogProgressBar().create();


       firstload = false;
}

    public void getalldata(){
        userExercises.clear();
        userRoutines.clear();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(myToolbar);



        progressDialog.show();
        ArrayList<String> exenames = new ArrayList<String>();
        exenames.add("e1");
        exenames.add("e2");
        exenames.add("e3");
        for(int i = 0; i < exenames.size(); i++) {
            mDatabase.child("exercise").child(exenames.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Exercise a = dataSnapshot.getValue(Exercise.class);
                    a.setExerciseID(dataSnapshot.child("exerciseID").getValue().toString());
                    Log.d("321",a.getExerciseID());
                    userExercises.add(a);
                    if (userExercises.size() == 3) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("exerciseList", userExercises);
                        ExerciseFragment h = new ExerciseFragment();
                        h.setArguments(bundle);
                        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                        navigation.setOnNavigationItemSelectedListener(MainActivity.this);
                        //load to home fragment by default
                        loadFragment(h); }

                    if(fromwhere.equals(""))
                    {   mDatabase.child("routines").child(currentuser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mDatabase.child("routines").child(currentuser.getUid()).limitToLast(5).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    userRoutines.clear();
                                    userRoutines2.clear();
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                        Routine a =  postSnapshot.getValue(Routine.class);

                                        userRoutines.add(a);
                                        userRoutines2.add(a);
                                    }
                                    Collections.reverse(userRoutines);
                                    Collections.reverse(userRoutines2);

                                    mDatabase.child("exercise_d").child(currentuser.getUid()).limitToLast(5).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            userExercises2.clear();

                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                Exercise exe = postSnapshot.getValue(Exercise.class);
                                                String b = postSnapshot.child("title").getValue().toString();
                                                userExercises2.add(exe);
                                                Log.d("jiji",b);
                                            }
                                            Collections.reverse( userExercises2);
                                            progressDialog.dismiss();


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

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            }
            );
        }
}




    //make sure you call the second fragment in the list for compatibility
    private boolean loadFragment(Fragment fragment){
        if(fragment !=null){
            //replace the fragment
            if(!isFinishing()) {

              FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,fragment);
                ft.commitAllowingStateLoss();







            return true;}
        }
        return false;
    }

    //turn off the back button so we can't accidentally log out

    public void onBackPressed() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    //called when we select a bottom navigation item

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(networkChangeReceiver, intentFilter);


    }
    public static void backupFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        // Make sure the current transaction finishes first
        fragmentManager.executePendingTransactions();

        // If there is no fragment yet with this tag...
        if (fragmentManager.findFragmentByTag(tag) == null) {
            // Add it
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragment, tag);
            transaction.commit();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        final Bundle bundle = new Bundle();
        String tag="";

        switch(item.getItemId()){
            case R.id.navigation_home:
                if (!(fragment instanceof ExerciseFragment)) {
                    //pass list to fragment to populate the recyclerView
                    bundle.putSerializable("exerciseList", userExercises);
                    fragment = new ExerciseFragment();
                    fragment.setArguments(bundle);
                    tag="home"; }
                break;
            case R.id.navigation_calendar:
                bundle.putSerializable("routineList",userRoutines2);
                fragment = new CalendarFragment();
                fragment.setArguments(bundle);
                tag="calendar";
                break;
            case R.id.navigation_routines:
//                for(int i=0;i<userRoutines.size()-1;i++){
//                       final int i3 = i;
//                    userRoutines.get(i).setExerciseList(new ArrayList<Exercise>());
//                    for(int i1=0;i1<userRoutines.get(i).getExerciseidList().size()-1;i1++){
//                        final int i4 = i1;
//                        mDatabase = FirebaseDatabase.getInstance().getReference().child("exercises").child(userRoutines.get(i).getExerciseidList().get(i1));
//                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() { //attach listener
//
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
//                                Exercise a= dataSnapshot.getValue(Exercise.class);
//                                userRoutines.get(i3).addExerciseToList(a);
//                                if(userRoutines.get(i4).getExerciseList().get(userRoutines.get(i3).getExerciseidList().size()-1).getId().equals(a.getId())&& userRoutines.get(userRoutines.size()-1).getRoutineId().equals(userRoutines.get(i3).getRoutineId())){
//
//
//
//
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
//
//                            }
//                        });
//                    }
//
//
//
//
//
//                }

                bundle.putSerializable("routineList",userRoutines);
                fragment = new RoutinesFragment();
                fragment.setArguments(bundle);
                tag="routine";

                break;
            case R.id.navigation_personalRecord:
                //TODO: Reimplement personalRecords page when we have an api
                //we should actually be pulling a completely different array for this part
                //but I didn't think of that and it kinda needs to be done on the api
                //so for now we just pass the exercise list and display different values
                //from what we show in the home screen.
                bundle.putSerializable("personalRecords",userExercises2);
                fragment = new PersonalRecordFragment();
                fragment.setArguments(bundle);
                tag="record";
                break;
        }
        return  loadFragment(this.storeAndReturnFragment(R.id.fragment_container,fragment,this,tag));
    }
//
//    public void toggleCalendarView(View view) {
//        Switch calendarToggle = (Switch) findViewById(R.id.sw_calendarViewToggle);
//        ConstraintLayout calendarLayout = (ConstraintLayout) findViewById(R.id.layout_calendarView);
//
//        String toggleText = calendarToggle.getText().toString();
//        if(calendarToggle.isChecked()){
//            //if calendar view is on, set it to week view
//            calendarLayout.setVisibility(View.INVISIBLE);
//
//            calendarToggle.setText("Week View");
//        }
//        else{
//            //otherwise we set it to calendar view
//            calendarLayout.setVisibility(View.VISIBLE);
//
//            calendarToggle.setText("Calendar View");
//        }
//    }
public AlertDialog.Builder getDialogProgressBar() {

    if (builder == null) {
        builder = new AlertDialog.Builder(this);



        final ProgressBar progressBar = new ProgressBar(this);
        ConstraintLayout.LayoutParams lp = new    ConstraintLayout.LayoutParams(
                20,
               20);
        progressBar.setLayoutParams(lp);
        builder.setView(progressBar);
    }
    return builder;
}
    public void addRoutine(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Routine Title");
        final EditText newRoutineTitle = new EditText(this);
        newRoutineTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(newRoutineTitle);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //routine created, add it to list, tell the recyclerView to update its shit
                ArrayList<Exercise> emptyList = new ArrayList<Exercise>();
                final Date startDate = new Date();
                Routine emptyRoutine = new Routine("blank",startDate.getTime(),startDate.getTime(),false,false,emptyList,newRoutineTitle.getText().toString());
                //add the empty routine
                userRoutines.add(emptyRoutine);
                //update the main user object to maintain parity



                Intent intent = new Intent(MainActivity.this, RoutineInformationActivity.class);
                intent.putExtra("routineInfo",emptyRoutine);
                intent.putExtra("list",userRoutines);
                //start activity expecting a result
                startActivityForResult(intent,1);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onActivityResult(int reqCode,int resCode,Intent data){
        super.onActivityResult(reqCode,resCode,data);
        if(reqCode==1){
            if(resCode==RESULT_OK){

                Routine updatedRoutine;
                //get new routine
                updatedRoutine = (Routine) data.getSerializableExtra("updatedRoutine");
                this.fromwhere = (String) data.getSerializableExtra("backfromroutine");
                this.userRoutines = (ArrayList)data.getSerializableExtra("oldroutinelist");
                Log.d("returnone1",updatedRoutine.getid());
                if(this.fromwhere.equals("backfromroutine"))
                {for(int i=0;i<userRoutines.size();i++){
                    Routine tempRoutine = userRoutines.get(i);
                    //if routine is an update
                    if(tempRoutine.getid().equals(updatedRoutine.getid())){
                        //replace the old object
                        userRoutines.set(i,updatedRoutine);
                        //update the user's list
//                        user.setRoutines(userRoutines);
                        Log.d("returnone1","1");

                    }


                }
                    Fragment fragment = null;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("routineList",userRoutines);
                    fragment = new RoutinesFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                }
                else if(this.fromwhere.equals("delete")){


                    for(int i=0;i<userRoutines.size();i++){
                        Routine tempRoutine = userRoutines.get(i);
                        //if routine is an update
                        if(tempRoutine.getid().equals(updatedRoutine.getid())){
                            //replace the old object
                            userRoutines.remove(i);
                            //update the user's list
//                        user.setRoutines(userRoutines);
                            Log.d("returnone1","1");

                        }


                    }
                    Fragment fragment = null;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("routineList",userRoutines);
                    fragment = new RoutinesFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);


                }
                else if(this.fromwhere.equals("calendar")){
                    Log.d("here","here111");
                    Fragment fragment = null;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("routineList",userRoutines2);
                    fragment = new RoutinesFragment();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);



                }

                //reload the fragment

            }
        }
    }
}
