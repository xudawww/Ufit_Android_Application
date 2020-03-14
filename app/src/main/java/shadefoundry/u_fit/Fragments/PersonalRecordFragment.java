package shadefoundry.u_fit.Fragments;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import shadefoundry.u_fit.Adapters.RoutineAdapter;
import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.CommonControl.infinitescrolling;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Adapters.PersonalRecordAdapter;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;

public class PersonalRecordFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private boolean scrollinglocker=false;
    public FirebaseUser currentuser;
    ArrayList<Exercise> personalRecordList = new ArrayList<Exercise>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_record,container,false);
        final FragmentActivity c= getActivity();
        mAuth=FirebaseAuth.getInstance();
        this.currentuser = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.lst_personalRecords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {




                personalRecordList = (ArrayList<Exercise>)getArguments().getSerializable("personalRecords");
                final PersonalRecordAdapter adapter = new PersonalRecordAdapter(c,personalRecordList);
                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        recyclerView.setAdapter(adapter);

                        final PersonalRecordAdapter adapter = new PersonalRecordAdapter  (getActivity(),  personalRecordList);

                        recyclerView.setAdapter(adapter);
                        if(personalRecordList.size()>0)
                        {recyclerView.addOnScrollListener(new infinitescrolling() {

                           Exercise routine = personalRecordList.get(0);

                            @Override
                            public void onScrolledToEnd() {
                                InternetController internet = new InternetController();
                                if(internet.chechConnection(getContext())){
                                    if( !scrollinglocker)
                                    {   scrollinglocker=true;
                                        mDatabase.child("exercise_d").child(currentuser.getUid()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                                                    Exercise a =  postSnapshot.getValue(Exercise.class);
                                                    if(!a.getExerciseID().equals(personalRecordList.get(personalRecordList.size()-1).getExerciseID())){

                                                        mDatabase.child("exercise_d").child(currentuser.getUid()).orderByKey().endAt(personalRecordList.get(personalRecordList.size()-1).getExerciseID()).limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            ArrayList< Exercise> exercieslist2 = new ArrayList< Exercise>();

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                                                                    Exercise a =  postSnapshot.getValue(Exercise.class);


                                                                    exercieslist2.add(a);





                                                                }

                                                                exercieslist2.remove(exercieslist2.size()-1);
                                                                Collections.reverse(exercieslist2);
                                                                for(Exercise routine :exercieslist2){

                                                                   personalRecordList.add(routine);

                                                                }




                                                                final PersonalRecordAdapter adapter = new  PersonalRecordAdapter(getActivity(), personalRecordList);

                                                                recyclerView.setAdapter(adapter);
                                                                Toast.makeText(getContext(), String.valueOf(personalRecordList.size()),
                                                                        Toast.LENGTH_SHORT).show();

                                                                scrollinglocker =false;


                                                            }

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
                                    recyclerView.setAdapter(adapter);
                                    Toast.makeText(getContext(), "Please Connect Internet!",
                                            Toast.LENGTH_SHORT).show();


                                }



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