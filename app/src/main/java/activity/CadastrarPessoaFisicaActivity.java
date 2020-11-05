package activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.app.melhorevento.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import model.PessoaFisica;
import validacoes.Validacoes;

public class CadastrarPessoaFisicaActivity extends AppCompatActivity {

    private EditText editTextNome, editTextCpf, editTextTelefone, editTextEmail, editTextSenha, editTextSenhaConfirmacao;
    private Button buttonCadastrar, buttonVoltar;
    private CheckBox checkBoxAceitarTermos;
    private ProgressBar progressBarCadastroPessoaFisica;
    private DatabaseReference pessoaFisica = FirebaseDatabase.getInstance().getReference("pessoaFisica");
    private DatabaseReference cpf = FirebaseDatabase.getInstance().getReference("cpf");
    private FirebaseAuth usuario = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_pessoa_fisica_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBarCadastroPessoaFisica = (ProgressBar)findViewById(R.id.progressBarCadastroPessoFisica);
        progressBarCadastroPessoaFisica.setVisibility(View.INVISIBLE);
        final Validacoes validacoes = new Validacoes();
        checkBoxAceitarTermos = (CheckBox)findViewById(R.id.checkBoxAceitarTermosCadastroPessoaFisica);
        checkBoxAceitarTermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxAceitarTermos.isChecked()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.logo_erro);
                    dialog.setTitle("Termos de uso");
                    dialog.setMessage("A empresa Betabyte Soluções Tecnológicas é responsável por armazenar e proteger todos os dados aqui informados. Seus dados nunca serão compartilhados com ninguém. Contudo, há uma pequena possibilidade de ocorrer perda ou exposição dos dados, mas estamos sempre trabalhando para que isso não aconteça. Ao concordar com esse termo, você concorda com o pequeno risco de perda ou exposição de seus dados.");
                    dialog.setNeutralButton("OK", null);
                    dialog.create();
                    dialog.show();
                }
            }
        });
        editTextNome = (EditText)findViewById(R.id.editTextCadastrarPessoaFisicaNome);
        editTextTelefone = (EditText)findViewById(R.id.editTextCadastrarPessoaFisicaTelefone);
        editTextCpf = (EditText)findViewById(R.id.editTextCadastrarPessoaFisicaCpf);
        editTextEmail = (EditText)findViewById(R.id.editTextCadastrarPessoaFisicaEmal);
        editTextSenha = (EditText)findViewById(R.id.editTextCadastrarPessoaFisicaSenha);
        editTextSenhaConfirmacao = (EditText)findViewById(R.id.editTextCadastrarPessoaFisicaSenhaConfirmacao);
        buttonCadastrar = (Button)findViewById(R.id.buttonCadastrarPessoaFisicaCadastrar);
        buttonVoltar = (Button)findViewById(R.id.buttonCadastrarPessoaFisicaVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario.signOut();
                finish();
            }
        });
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final View view = v;
                if(editTextNome.getText().length()<3){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.logo_erro);
                    dialog.setTitle("Falha ao criar usuário");
                    dialog.setMessage("Digite um nome com no mínimo 3(três) letras para concluir o cadastro");
                    dialog.setNeutralButton("OK",null);
                    dialog.create();
                    dialog.show();
                }
                    else if(editTextTelefone.getText().length()<10 || editTextTelefone.getText().length()>12){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Falha ao criar usuário");
                        dialog.setMessage("Digite um telefone válido para concluir o cadastro");
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }
                        else if(!validacoes.isValid(editTextCpf.getText().toString())){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                            dialog.setCancelable(false);
                            dialog.setIcon(R.drawable.logo_erro);
                            dialog.setTitle("Falha ao criar usuário");
                            dialog.setMessage("Digite um CPF válido para concluir o cadastro");
                            dialog.setNeutralButton("OK",null);
                            dialog.create();
                            dialog.show();
                        }
                            else if(editTextEmail.getText().toString().equals("")){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                dialog.setCancelable(false);
                                dialog.setIcon(R.drawable.logo_erro);
                                dialog.setTitle("Falha ao criar usuário");
                                dialog.setMessage("Digite um e-mail para concluir o cadastro");
                                dialog.setNeutralButton("OK",null);
                                dialog.create();
                                dialog.show();
                            }
                                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()){
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                    dialog.setCancelable(false);
                                    dialog.setIcon(R.drawable.logo_erro);
                                    dialog.setTitle("Falha ao criar usuário");
                                    dialog.setMessage("Digite um e-mail válido para concluir o cadastro");
                                    dialog.setNeutralButton("OK",null);
                                    dialog.create();
                                    dialog.show();
                                }
                                    else if(editTextSenha.getText().length()<8 || editTextSenhaConfirmacao.getText().length()<8){
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                        dialog.setCancelable(false);
                                        dialog.setIcon(R.drawable.logo_erro);
                                        dialog.setTitle("Falha ao criar usuário");
                                        dialog.setMessage("Digite uma senha com no mínimo de 8 caracteres");
                                        dialog.setNeutralButton("OK",null);
                                        dialog.create();
                                        dialog.show();
                                    }
                                        else if(!editTextSenha.getText().toString().equals(editTextSenhaConfirmacao.getText().toString())){
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                            dialog.setCancelable(false);
                                            dialog.setIcon(R.drawable.logo_erro);
                                            dialog.setTitle("Falha ao criar usuário");
                                            dialog.setMessage("Os campos de senha e confirmação de senha devem ser iguais");
                                            dialog.setNeutralButton("OK",null);
                                            dialog.create();
                                            dialog.show();
                                        }
                                            else if(!checkBoxAceitarTermos.isChecked()){
                                                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                                dialog.setCancelable(false);
                                                dialog.setIcon(R.drawable.logo_erro);
                                                dialog.setTitle("Falha ao criar usuário");
                                                dialog.setMessage("É necessário aceitar os termos do aplicativo para criar um usuário");
                                                dialog.setNeutralButton("OK",null);
                                                dialog.create();
                                                dialog.show();
                                            }
                else{
                    progressBarCadastroPessoaFisica.setVisibility(View.VISIBLE);
                    try{
                        usuario.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextSenha.getText().toString())
                        .addOnCompleteListener(CadastrarPessoaFisicaActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String s = pessoaFisica.push().getKey();
                                    PessoaFisica p = new PessoaFisica(s, editTextNome.getText().toString(), editTextTelefone.getText().toString(), editTextEmail.getText().toString(), editTextSenha.getText().toString(), editTextCpf.getText().toString(), false);
                                    pessoaFisica.child(s).setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            cpf.child(editTextCpf.getText().toString()).setValue(editTextEmail.getText().toString());
                                            progressBarCadastroPessoaFisica.setVisibility(View.INVISIBLE);
                                            finish();
                                        }
                                    }).addOnFailureListener(new CadastrarPessoaFisicaActivity(), new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                                            dialog.setCancelable(false);
                                            dialog.setIcon(R.drawable.logo_erro);
                                            dialog.setTitle("Falha ao criar usuário");
                                            dialog.setMessage("O CPF informado já foi utilizado para criar outro usuário. Favor informar outro CPF");
                                            dialog.setNeutralButton("OK",null);
                                            dialog.create();
                                            dialog.show();
                                            progressBarCadastroPessoaFisica.setVisibility(View.INVISIBLE);
                                            FirebaseUser firebaseUser = usuario.getCurrentUser();
                                            firebaseUser.delete();
                                        }
                                    });
                                }
                                else{
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                                    dialog.setCancelable(false);
                                    dialog.setIcon(R.drawable.logo_erro);
                                    dialog.setTitle("Falha ao criar usuário");
                                    dialog.setMessage("O e-mail informado já foi utilizado em outro cadastro");
                                    dialog.setNeutralButton("OK",null);
                                    dialog.create();
                                    dialog.show();
                                    progressBarCadastroPessoaFisica.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                    catch (Exception e){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Falha ao criar usuário");
                        dialog.setMessage("Ocorreu um erro na criação do usuário. Tente novamente mais tarde");
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        usuario.signOut();
        finish();
    }
}