package shadefoundry.u_fit.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Adapters.ExerciseAdapter;
import shadefoundry.u_fit.R;

public class ExerciseFragment extends Fragment {

    ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //declare the view so we can mess with it
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        final FragmentActivity c = getActivity();

        //exerciseList = new ArrayList<Exercise>();
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.lst_workoutSchedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        //make sure the view itself has a fixed size (not the number of elements)
        recyclerView.setHasFixedSize(true);
        //can be used with a different layout manager
        recyclerView.setLayoutManager(layoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                exerciseList = (ArrayList<Exercise>)getArguments().getSerializable("exerciseList");
                final ExerciseAdapter adapter = new ExerciseAdapter(c, exerciseList);
                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        return rootView;
    }
}
