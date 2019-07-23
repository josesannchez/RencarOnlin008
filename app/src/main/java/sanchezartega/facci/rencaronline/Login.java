package sanchezartega.facci.rencaronline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sanchezartega.facci.rencaronline.Activities.Home;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Email, Contraseña;
    private Button Login, Registro;
    private String TAG;

    private TextView ayudaRapida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


            Email = (EditText) findViewById(R.id.username);
            Contraseña = (EditText) findViewById(R.id.password);
            Login = (Button) findViewById(R.id.btnlogin);
            Registro = (Button) findViewById(R.id.btnregistro2);
            mAuth = FirebaseAuth.getInstance();


            Registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Login.this, Registro.class);
                    startActivity(intent);
                }
            });
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String correo = Email.getText().toString();
                    if (isValidEmail(correo) && validarContraseña()){
                        String contraseña = Contraseña.getText().toString();
                        mAuth.signInWithEmailAndPassword(correo, contraseña)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(Login.this,"Se ingreso correctamente",Toast.LENGTH_SHORT).show();
                                            nextActivity();


                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(Login.this, "Ocurrio un error al inicio de sesion",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }else {
                        Toast.makeText(Login.this, "No se puede iniciar sesion",Toast.LENGTH_SHORT).show();
                    }

                }
            });



        }


        private boolean isValidEmail (CharSequence target){
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

        public boolean validarContraseña (){
            String contraseña;
            contraseña = Contraseña.getText().toString();
            if (contraseña.length() >=6){
                return true;
            }else return false;

        }

        @Override
        protected void onResume() {
            super.onResume();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null){
                Toast.makeText(this,"Usuario logeado",Toast.LENGTH_SHORT).show();
                nextActivity();
            }else {
            }

        }

        private void nextActivity(){
            startActivity(new Intent(Login.this, Home.class));
            finish();

        }
    }

