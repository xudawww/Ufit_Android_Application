package shadefoundry.u_fit.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import shadefoundry.u_fit.Activities.LoginActivity;
import shadefoundry.u_fit.Activities.MainActivity;
import shadefoundry.u_fit.Activities.RoutineInformationActivity;
import shadefoundry.u_fit.Activities.RoutinelistFromCalendarActivity;
import shadefoundry.u_fit.Adapters.RoutineAdapter;
import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;

public class CalendarFragment extends Fragment {
    CalendarView cv;
    private FirebaseUser currentuser;
    private DatabaseReference mDatabase;
    private ArrayList<Routine> userRoutines;
    List<Calendar> calendars;
    private FirebaseAuth mAuth;
    int selectednumber;
    private boolean locker = false;
    ArrayList<Routine> routineList = new ArrayList<Routine>();
    @Nullable

    public void loading(){

        InternetController checker = new InternetController();

        if(checker.chechConnection(getContext())){
            if(!locker) {
                locker=true;
                Log.d("-p-", String.valueOf(routineList.size()));
                mDatabase.child("routines").child(currentuser.getUid()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            Routine a = postSnapshot.getValue(Routine.class);
                            if (!a.getid().equals(routineList.get(routineList.size() - 1).getid())) {

                                mDatabase.child("routines").child(currentuser.getUid()).orderByKey().endAt(routineList.get(routineList.size() - 1).getid()).limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
                                    ArrayList<Routine> routinelist2 = new ArrayList<Routine>();

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            Routine a = postSnapshot.getValue(Routine.class);
                                            routinelist2.add(a);
                                        }
                                        routinelist2.remove(routinelist2.size() - 1);
                                        Collections.reverse(routinelist2);
                                        for(Routine r:routinelist2){
                                            routineList.add(r);
                                            Calendar c = Calendar.getInstance();
                                            c.setTimeInMillis(r.getStarttime());
                                            int startYear = c.get(Calendar.YEAR);
                                            int startMonth = c.get(Calendar.MONTH);
                                            int startDate = c.get(Calendar.DAY_OF_MONTH);
                                            c.set(startYear,startMonth,startDate);
                                            if(!calendars.contains(c))
                                            {calendars.add(c);}
                                            String selectedDate = startDate+"/"+startMonth+"/"+startYear;




                                        }

                                        try{
                                            List<Calendar> blankcalendars = new ArrayList<>();
                                            cv.setSelectedDates(blankcalendars);
                                            cv.setSelectedDates(calendars);
                                            selectednumber = cv.getSelectedDates().size();

                                        }
                                        catch (Exception e){


                                        }


                                        Toast.makeText(getContext(), "Loading from db....",
                                                Toast.LENGTH_SHORT).show();

                                        locker = false;


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }else{


                                locker = false;

                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
        else{


            Toast.makeText(getContext(), "Please Counnext Internet",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK){


            if(resultCode == 1){

            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fragment_calendar2,null);
        routineList = (ArrayList<Routine>)getArguments().getSerializable("routineList");

        InternetController checker = new InternetController();
        mAuth=FirebaseAuth.getInstance();
        cv=(CalendarView) root.findViewById(R.id.calendarView2);
        this.currentuser = mAuth.getCurrentUser();
        List<Calendar> blankcalendars = new ArrayList<>();
        int selectnumbers = cv.getSelectedDates().size();
        Log.d("123",String.valueOf(selectnumbers));







        cv.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                List<Calendar> selectedDates = cv.getSelectedDates();
                if(selectedDates.size()<selectnumbers );
                Log.d("2", String.valueOf(eventDay.getCalendar().get(Calendar.MONTH)+1)+"/"+String.valueOf(eventDay.getCalendar().get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(eventDay.getCalendar().get(Calendar.YEAR)));

                Intent intent = new Intent(((MainActivity)getActivity()), RoutinelistFromCalendarActivity.class);
                intent.putExtra("date",String.valueOf(eventDay.getCalendar().get(Calendar.MONTH)+1)+"/"+String.valueOf(eventDay.getCalendar().get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(eventDay.getCalendar().get(Calendar.YEAR)));
                startActivityForResult(intent,1);
            }
        });
       cv.setSelectedDates(blankcalendars);


        calendars = new ArrayList<>();

        mDatabase= FirebaseDatabase.getInstance().getReference();

        cv.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {




            }
        });

       cv.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                loading();


            }
        });
        for(int i = 0;i<routineList.size();i++){
            Calendar c = Calendar.getInstance();


                    c.setTimeInMillis(routineList.get(i).getStarttime());
                    int startYear = c.get(Calendar.YEAR);
                    int startMonth = c.get(Calendar.MONTH);
                    int startDate = c.get(Calendar.DAY_OF_MONTH);
                    c.set(startYear,startMonth,startDate);
            if(!calendars.contains(c))
            {calendars.add(c);}
                    String selectedDate = startDate+"/"+startMonth+"/"+startYear;




                }

        try{

            cv.setSelectedDates(blankcalendars);
            cv.setSelectedDates(calendars);

        }
        catch (Exception e){


        }


        return  root;
    }
}
