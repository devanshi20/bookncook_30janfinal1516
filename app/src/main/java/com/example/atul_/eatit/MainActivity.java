package com.example.atul_.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atul_.eatit.Common.Common;
import com.example.atul_.eatit.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.atul_.eatit.R.id.ckbRemember;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn ,btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
     //   Typeface face = Typeface.createFromAsset(getAssets(), "font/Nabila.ttf");

        Paper.init(this);



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);
            }
        });

        String user=Paper.book().read(Common.USER_KEY);
        String pwd=Paper.book().read(Common.PWD_KEY);

        if (user !=null && pwd!=null){

            if (!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }

    }

    private void login(final String phone, final String pwd) {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {






            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait");
            mDialog.show();


            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phone).exists()) {
                        mDialog.dismiss();
                        User user = dataSnapshot.child(phone).getValue(User.class);


                        if (user.getPassword().equals(pwd)) {
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(MainActivity.this, "User does not exist.", Toast.LENGTH_SHORT).show();
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this,"Please check your connection",Toast.LENGTH_SHORT).show();
            return;
        }


    }


}
