package sanchezartega.facci.rencaronline;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import sanchezartega.facci.rencaronline.Activities.Home;
import sanchezartega.facci.rencaronline.Entidades.Usuario;

public class Registro extends AppCompatActivity  {


    ImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;

    private EditText userEmail,userPassword,userPAssword2,userName,apellido,telefono;
    private ProgressBar loadingProgress;
    private Button regBtn;

    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String email;// = userEmail.getText().toString();
    String password; //= userPassword.getText().toString();
    String password2; //= userPAssword2.getText().toString();
    String name; //= userName.getText().toString();
    String apellidos; //= apellido.getText().toString();
    String celular; //= telefono.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        userEmail = findViewById(R.id.correo);
        userPassword = findViewById(R.id.password);
        userPAssword2 = findViewById(R.id.rpassword);
        userName = findViewById(R.id.nombres);
        apellido = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);

        loadingProgress = findViewById(R.id.progressBar);
        regBtn = findViewById(R.id.btnregistro);
        loadingProgress.setVisibility(View.INVISIBLE);


        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuario");
        databaseReference2 = firebaseDatabase.getReference("Imagenes");
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                email = userEmail.getText().toString();
                password = userPassword.getText().toString();
                password2 = userPAssword2.getText().toString();
                name = userName.getText().toString();
                apellidos = apellido.getText().toString();
                celular = telefono.getText().toString();

                //if (email.isEmpty() || name.isEmpty() || apellidos.isEmpty() || celular.isEmpty() || password.isEmpty() || !password.equals(password2)) {
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Registro.this, "Porfavor ingrese su correo", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Registro.this, "Porfavor ingrese su nombre", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(apellidos)) {
                    Toast.makeText(Registro.this, "Porfavor ingrese su apellidos", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(celular)) {
                    Toast.makeText(Registro.this, "Porfavor ingrese su numero telefonico", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Registro.this, "Porfavor ingrese su clave", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password2)) {
                    Toast.makeText(Registro.this, "Porfavor repita su clave correctamente", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(password2)) {
                    Toast.makeText(Registro.this, "La clave no coincide", Toast.LENGTH_SHORT).show();
                } else {
                    CreateUserAccount(email, name, password, password2, celular,apellidos);

                }
                regBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);

            }
        });

        ImgUserPhoto = findViewById(R.id.imageView33) ;

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();


                }
                else
                {
                    openGallery();
                }





            }
        });


    }

    private void CreateUserAccount(String email, final String name, String password, String password2, String celular, String apellidos) {


        // this method create user account with specific email and password

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // user account created successfully
                            showMessage("Cuenta creada exitosamente");
                            // after we created user account we need to update his profile picture and name
                            updateUserInfo( name ,pickedImgUri,mAuth.getCurrentUser());
                        }
                        else
                        {

                            // account creation failed
                            showMessage("Falla en crea la cuenta" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });








    }


    // update user photo and name
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Perfil");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url
                        Usuario usuario = new Usuario();
                        usuario.setCorreo(userEmail.getText().toString());
                        usuario.setNombre(userName.getText().toString());
                        usuario.setApellido(apellido.getText().toString().trim());
                        usuario.setDirecion(telefono.getText().toString().trim());
                        usuario.setIdUsuario(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        usuario.setFoto(imageFilePath.toString());

                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Registro.this, "se registro correctamente",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("Registro completo");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });





            }
        });






    }

    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), Home.class);
        startActivity(homeActivity);
        finish();


    }

    // simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Registro.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(Registro.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);


        }


    }
}