package shadefoundry.u_fit.Activities;

import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText username;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser  a = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getSharedPreferences("ufituser", MODE_PRIVATE);
        String name = prefs.getString("user", "");
        if(prefs.contains("user"))
            {login();}
        Log.d("uuid",name);
        username   = (EditText)findViewById(R.id.signupuname);
        password   = (EditText)findViewById(R.id.signupupass);


    }

    public void signup(View view){

        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
    public void login(){


//        User u = new User(1, "mohara", "key", "Miguel", "O'Hara", "mohara@outlook.com", routineArrayList);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //pass the user object to the main activity
//        intent.putExtra("user", u);
        startActivity(intent);
    }
    public AlertDialog.Builder getDialogProgressBar() {

        if (builder == null) {
            builder = new AlertDialog.Builder(this);



            final ProgressBar progressBar = new ProgressBar(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    30,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }
    public void loginButtonClicked(View view) {
        mAuth = FirebaseAuth.getInstance();
            if(TextUtils.isEmpty(username.getText().toString())||TextUtils.isEmpty(password.getText().toString())){
               Toast.makeText(LoginActivity.this, "Please enter username and password",
                    Toast.LENGTH_SHORT).show();
            }
            else {

                InternetController internet = new InternetController();
                if(internet.chechConnection(this))
                {

                    mAuth.signInWithEmailAndPassword(this.username.getText().toString(), this.password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor editor = getSharedPreferences("ufituser", MODE_PRIVATE).edit();
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                editor.putString("user",user.getUid());
                                editor.commit();

                                login();
                            }
                            else  {

                                Toast.makeText(LoginActivity.this, "Wrong username or password.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("12121212","00000");

                        }
                    });


                }else{
                    Toast.makeText(LoginActivity.this, "Please connect internet",
                            Toast.LENGTH_SHORT).show();




                }}




    }
}
