package com.appclass.myorganizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appclass.myorganizze.R;
import com.appclass.myorganizze.config.ConfiguracaoFirebase;
import com.appclass.myorganizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail    = findViewById(R.id.editTextEmail);
        campoSenha    = findViewById(R.id.editTextSenha);
        botaoEntrar   = findViewById(R.id.buttonEntrar);


        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if ( !textoEmail.isEmpty() ){
                    if ( !textoSenha.isEmpty() ){

                        usuario = new Usuario();
                        usuario.setEmail( textoEmail );
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha() ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    abrirTelaPrincipal();
                    //Toast.makeText(LoginActivity.this, "abriu porra", Toast.LENGTH_SHORT).show();

                }else {

                    String excessao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e){
                        excessao = "Usuário não cadastrado.";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excessao = "E-mail e/ou senha inválidos.";
                    }catch ( Exception e){
                        excessao = "Erro ao fazer login: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, excessao, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void abrirTelaPrincipal(){

        startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));

    }

}