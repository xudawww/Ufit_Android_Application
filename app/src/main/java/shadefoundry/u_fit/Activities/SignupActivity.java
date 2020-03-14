package shadefoundry.u_fit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import shadefoundry.u_fit.CommonControl.InternetController;
import shadefoundry.u_fit.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import shadefoundry.u_fit.Objects.Exercise;
import shadefoundry.u_fit.Objects.Routine;
import shadefoundry.u_fit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText signupuname;
    private EditText signuppass;
    private EditText signuppasscom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupuname = (EditText)findViewById(R.id.signupuname);
        signuppass = (EditText)findViewById(R.id.signuppass);
        signuppasscom = (EditText)findViewById(R.id.signuppasscom);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signup(View view){
         InternetController internet = new InternetController();

        if(TextUtils.isEmpty(signupuname.getText().toString())||TextUtils.isEmpty(signuppass.getText().toString())||TextUtils.isEmpty(signuppasscom.getText().toString())){
             if(!signuppass.getText().toString().equals(signuppasscom.getText().toString())){

                 Toast.makeText(this, "Password and comfimed password is different!.",
                         Toast.LENGTH_SHORT).show();

             }else {

                 Toast.makeText(this, "Please filled all information",
                         Toast.LENGTH_SHORT).show();
             }

        }
        else {
            if (internet.chechConnection(this)) {
                mAuth.createUserWithEmailAndPassword(signupuname.getText().toString(), signuppass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    mAuth.signInWithEmailAndPassword(signupuname.getText().toString(), signuppass.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                SharedPreferences.Editor editor = getSharedPreferences("ufituser", MODE_PRIVATE).edit();
                                                // Sign in success, update UI with the signed-in user's information
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                editor.putString("user", user.getUid());
                                                editor.commit();
                                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                //pass the user object to the main activity
//        intent.putExtra("user", u);
                                                startActivity(intent);


                                            } else {

                                                Toast.makeText(SignupActivity.this, "Wrong username or password.",
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                            // ...
                                        }

                                    });


                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });


            }
            else{
                Toast.makeText(SignupActivity.this, "Please Connect Internet!",
                        Toast.LENGTH_SHORT).show();


            }
        }
    }
}