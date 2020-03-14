package shadefoundry.u_fit.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shadefoundry.u_fit.Adapters.ExerciseListAdapter;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;

public class ExerciseBrowserActivity extends AppCompatActivity {
    ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();
    ArrayList<Exercise> routinexelist = new ArrayList<Exercise>();
    ArrayList<Exercise> exerciseArrayList2 = new ArrayList<Exercise>();
    RecyclerView recyclerView;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_browser);

        Intent intent=getIntent();


                recyclerView=(RecyclerView)findViewById(R.id.lst_exerciseBrowserList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ExerciseBrowserActivity.this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                exerciseArrayList =(ArrayList<Exercise>)getIntent().getSerializableExtra("exerciseList");
                exerciseArrayList2 =(ArrayList<Exercise>)getIntent().getSerializableExtra("exerciseList2");
                routinexelist = (ArrayList<Exercise>)getIntent().getSerializableExtra("routinexelist");
                final ExerciseListAdapter adapter=new ExerciseListAdapter(exerciseArrayList,exerciseArrayList2);
                recyclerView.setAdapter(adapter);











    }
}
