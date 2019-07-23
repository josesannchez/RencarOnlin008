package sanchezartega.facci.rencaronline.FRAG;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import sanchezartega.facci.rencaronline.Entidades.Usuario;
import sanchezartega.facci.rencaronline.R;


public class Perfil extends Fragment  implements View.OnClickListener {
    private EditText nombres, apellidos, direccion, correo;
    private CircleImageView perfil;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usuario, databaseReference;
    private String userId, contra, vericontra, foto;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnEditar, btnGuardar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nombres = (EditText)view.findViewById(R.id.txtNombresP);
        apellidos = (EditText)view.findViewById(R.id.txtApellidoP);
        direccion = (EditText)view.findViewById(R.id.txtDireccionP);
        correo = (EditText)view.findViewById(R.id.txtCorreoP);
        perfil = (CircleImageView)view.findViewById(R.id.CirculoImagenP);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
        usuario = firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getReference();

        Perfil();

        btnEditar = (Button) view.findViewById(R.id.btnEditPerfil);
        btnGuardar = (Button) view.findViewById(R.id.btnGuardarPerfil);

        btnEditar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

        return view;
    }

    private void Perfil() {

        usuario.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String key = usuario.child("Usuario").child(userId).getKey();
                    String key2 = snapshot.getKey();
                    if (key.equals(key2)){
                        Usuario usuario_perfil = snapshot.getValue(Usuario.class);
                        nombres.setText(usuario_perfil.getNombre());
                        apellidos.setText(usuario_perfil.getApellido());
                        direccion.setText(usuario_perfil.getDirecion());
                        correo.setText(usuario_perfil.getCorreo());
                        foto = usuario_perfil.getFoto();
                        Picasso.with(getContext()).load(usuario_perfil.getFoto()).into(perfil);
                    }else {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEditPerfil:
                nombres.setEnabled(true);
                apellidos.setEnabled(true);
                direccion.setEnabled(true);
                btnGuardar.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnGuardarPerfil:
                Usuario usuario1 = new Usuario();
                usuario1.setNombre(nombres.getText().toString().trim());
                usuario1.setApellido(apellidos.getText().toString().trim());
                usuario1.setDirecion(direccion.getText().toString().trim());
                usuario1.setCorreo(correo.getText().toString().trim());
                usuario1.setFoto(foto.trim());
                databaseReference.child("Usuario").child(userId).setValue(usuario1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "PERFIL ACTUALIZADO", Toast.LENGTH_SHORT).show();
                                nombres.setEnabled(false);
                                apellidos.setEnabled(false);
                                direccion.setEnabled(false);
                                btnGuardar.setVisibility(View.INVISIBLE);
                                btnEditar.setVisibility(View.VISIBLE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                        nombres.setEnabled(false);
                        apellidos.setEnabled(false);
                        direccion.setEnabled(false);
                        btnGuardar.setVisibility(View.INVISIBLE);
                        btnEditar.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }
}
