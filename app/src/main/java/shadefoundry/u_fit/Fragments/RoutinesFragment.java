package shadefoundry.u_fit.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import shadefoundry.u_fit.Activities.ExerciseBrowserActivity;
import shadefoundry.u_fit.Activities.LoginActivity;
import shadefoundry.u_fit.Activities.RoutineInformationActivity;
import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.CommonControl.infinitescrolling;
import shadefoundry.u_fit.CommonControl.infinitescrolling;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.R;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.Adapters.RoutineAdapter;

public class RoutinesFragment extends Fragment {
    private boolean ifend=false;
    ArrayList<String> exerlist;
    public FirebaseUser currentuser;
    ArrayList<Routine> routineList = new ArrayList<Routine>();
    private DatabaseReference mDatabase;
    ArrayList<Routine> userRoutines;
    ArrayList<Exercise> userExercises;
    private boolean scrollinglocker =false;
    private FirebaseAuth mAuth;
     RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_routines,container,false);
        final FragmentActivity c = getActivity();
        mAuth=FirebaseAuth.getInstance();
        exerlist = new ArrayList<String>();
        userExercises = new ArrayList<Exercise>();
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.lst_routineList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(layoutManager);
        this.currentuser = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        new Thread(new Runnable(){
            @Override
            public void run(){
                routineList = (ArrayList<Routine>)getArguments().getSerializable("routineList");



                scrollinglocker =false;


                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//
                        final RoutineAdapter adapter = new RoutineAdapter(getActivity(), routineList);

                        RoutinesFragment.this.recyclerView.setAdapter(adapter);
                        if(routineList.size()>0)
                        {recyclerView.addOnScrollListener(new infinitescrolling() {

                            Routine routine = routineList.get(0);

                            @Override
                            public void onScrolledToEnd() {
                                InternetController internet = new InternetController();
                                if(internet.chechConnection(getContext())){
                                    if( !scrollinglocker)
                                    {   scrollinglocker=true;
                                        mDatabase.child("routines").child(currentuser.getUid()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                                            Routine a =  postSnapshot.getValue(Routine.class);
                                            if(!a.getid().equals(routineList.get(routineList.size()-1).getid())){

                                                mDatabase.child("routines").child(currentuser.getUid()).orderByKey().endAt(routineList.get(routineList.size()-1).getid()).limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    ArrayList<Routine> routinelist2 = new ArrayList<Routine>();

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                                                            Routine a =  postSnapshot.getValue(Routine.class);


                                                            routinelist2.add(a);





                                                        }
                                                       if(routinelist2.size()>0) {
                                                           routinelist2.remove(routinelist2.size() - 1);
                                                           Collections.reverse(routinelist2);
                                                           for (Routine routine : routinelist2) {

                                                               routineList.add(routine);

                                                           }

                                                       }


                                                        final RoutineAdapter adapter = new RoutineAdapter(getActivity(), routineList);

                                                        RoutinesFragment.this.recyclerView.setAdapter(adapter);
                                                        Toast.makeText(getContext(), String.valueOf(routineList.size()),
                                                                Toast.LENGTH_SHORT).show();
                                                        recyclerView.scrollToPosition(routineList.size()-3);

                                                        scrollinglocker =false;


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });



                                            }

                                    else{


                                          ifend = true;
                                           }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });}}
                                else{
                                    if(!ifend) {
                                        RoutinesFragment.this.recyclerView.setAdapter(adapter);
                                        Toast.makeText(getContext(), "Please Connect Internet!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                               Log.d("345",routineList.get(routineList.size()-1).getid());


                            }
                        });

                    }
                    }
                });
            }
        }).start();

        return rootView;
    }




}
