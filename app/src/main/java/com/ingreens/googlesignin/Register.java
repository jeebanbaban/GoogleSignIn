package com.ingreens.googlesignin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Register extends AppCompatActivity {
    EditText nickname;
    Button register;
    DbInterface dbInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nickname=findViewById(R.id.etNickname);
        register=findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String nicknamee= nickname.getText().toString();
                RegistrationModel registrationModel=new RegistrationModel();
                registrationModel.setNickname(nicknamee);

                startActivity(new Intent(Register.this,MainActivity.class));
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                System.out.println(""+registrationModel.getNickname());
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");


            }
        });
    }
}
