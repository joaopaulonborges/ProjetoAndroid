package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.app.melhorevento.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import model.PessoaFisica;
import model.PessoaJuridica;

public class MainActivity extends AppCompatActivity {

    private boolean verificar;
    private EditText editTextEmail, editTextSenha;
    private TextView textViewEsqueciASenha;
    private Button buttonLogar, buttonCadastrar;
    private ProgressBar progressBarLogin;
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private DatabaseReference pessoaFisica = FirebaseDatabase.getInstance().getReference("pessoaFisica");
    private DatabaseReference pessoaJuridica = FirebaseDatabase.getInstance().getReference("pessoaJuridica");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo()==null || !manager.getActiveNetworkInfo().isConnected()){
            ativarInternet();
        }
        this.verificar = true;
        if(usuario.getCurrentUser()!=null){
            verificar = false;
            procurarPessoaFisica(usuario.getCurrentUser().getEmail());
            procurarPessoaJuridica(usuario.getCurrentUser().getEmail());
        }
        else{
            setContentView(R.layout.main_activity);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            textViewEsqueciASenha = (TextView)findViewById(R.id.textViewEsqueciASenha);
            textViewEsqueciASenha.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.logo_erro);
                    dialog.setTitle("Esqueci o e-mail / senha");
                    dialog.setMessage("Para recuperar seus dados de login é necessário enviar um e-mail para betabytesolucoestecnologicas@gmail.com, informando seu e-mail e o CPF / CNPJ. Se os dados não forem informados corretamente, não será possível recuperar seu login.");
                    dialog.setNeutralButton("OK",null);
                    dialog.create();
                    dialog.show();
                }
            });
            progressBarLogin = (ProgressBar)findViewById(R.id.progressBarLogin);
            progressBarLogin.setVisibility(View.INVISIBLE);
            editTextEmail = (EditText)findViewById(R.id.editTextEmail);
            editTextSenha = (EditText)findViewById(R.id.editTextSenha);
            buttonLogar = (Button)findViewById(R.id.buttonLogar);
            buttonCadastrar = (Button)findViewById(R.id.buttonCadastrar);
            buttonLogar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = v;
                    if(editTextEmail.getText().toString().equals("") || editTextSenha.getText().toString().equals("")){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Falha ao logar");
                        dialog.setMessage("E-mail e senha devem ser preenchidos");
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }
                    else{
                        usuario.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextSenha.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    verificar = false;
                                    progressBarLogin.setVisibility(View.VISIBLE);
                                    procurarPessoaFisica(usuario.getCurrentUser().getEmail());
                                    procurarPessoaJuridica(usuario.getCurrentUser().getEmail());
                                }
                                else{
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                                    dialog.setCancelable(false);
                                    dialog.setIcon(R.drawable.logo_erro);
                                    dialog.setTitle("Falha ao logar");
                                    dialog.setMessage("E-mail ou senha incorretos");
                                    dialog.setNeutralButton("OK",null);
                                    dialog.create();
                                    dialog.show();
                                }
                            }
                        });
                    }
                }
            });
            buttonCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setIcon(R.drawable.logo_cadastro_usuario);
                    dialog.setTitle("Criar conta");
                    dialog.setMessage("Escolha o tipo de usuário que deseja criar:");
                    dialog.setPositiveButton("Pessoa Física", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            verificar = true;
                            Intent intent = new Intent(getApplicationContext(), CadastrarPessoaFisicaActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.setNegativeButton("Pessoa Jurídica", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            verificar = true;
                            Intent intent = new Intent(getApplicationContext(), CadastrarPessoaJuridicaActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.create();
                    dialog.show();
                }
            });
        }
    }

    public void ativarInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Não foi encontrado conexão com a internet, é necessário ter conexão para utilizar o aplicativo.")
        .setCancelable(false)
        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(usuario.getCurrentUser()!=null && verificar == false){
            finish();
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void procurarPessoaFisica(String email){
        Query pessoaFisicaQuery = pessoaFisica.orderByChild("email").equalTo(email);
        pessoaFisicaQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    PessoaFisica p = dataSnapshot.getValue(PessoaFisica.class);
                    logarPessoaFisica(p);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void procurarPessoaJuridica(String email){
        Query pessoaJuridicaQuery = pessoaJuridica.orderByChild("email").equalTo(email);
        pessoaJuridicaQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue()!=null) {
                    PessoaJuridica p = dataSnapshot.getValue(PessoaJuridica.class);
                    logarPessoaJuridica(p);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void logarPessoaFisica(PessoaFisica p){
        Intent intent = new Intent(this, PrincipalActivity.class);
        Bundle parametros = new Bundle();
        parametros.putString("id", p.getId());
        parametros.putString("nome", p.getNome());
        parametros.putString("telefone", p.getTelefone());
        parametros.putString("email", p.getEmail());
        parametros.putString("senha", p.getSenha());
        parametros.putString("cpf", p.getCpf());
        parametros.putBoolean("publicar", p.getPublicar());
        intent.putExtras(parametros);
        if(progressBarLogin!=null) {
            progressBarLogin.setVisibility(View.INVISIBLE);
        }
        startActivity(intent);
    }

    public void logarPessoaJuridica(PessoaJuridica p){
        Intent intent = new Intent(this, PrincipalActivity.class);
        Bundle parametros = new Bundle();
        parametros.putString("id", p.getId());
        parametros.putString("nome", p.getNome());
        parametros.putString("telefone", p.getTelefone());
        parametros.putString("email", p.getEmail());
        parametros.putString("senha", p.getSenha());
        parametros.putString("cnpj", p.getCnpj());
        parametros.putBoolean("publicar", p.getPublicar());
        intent.putExtras(parametros);
        if(progressBarLogin!=null){
            progressBarLogin.setVisibility(View.INVISIBLE);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}