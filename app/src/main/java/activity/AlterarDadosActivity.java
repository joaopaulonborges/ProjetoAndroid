package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.app.melhorevento.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import model.PessoaFisica;
import model.PessoaJuridica;

public class AlterarDadosActivity extends AppCompatActivity {

    private EditText editTextNome, editTextTelefone, editTextSenha, editTextSenhaConfirmacao;
    private Button buttonAlterar, buttonVoltar;
    private DatabaseReference pessoaFisica = FirebaseDatabase.getInstance().getReference("pessoaFisica");
    private DatabaseReference pessoaJuridica = FirebaseDatabase.getInstance().getReference("pessoaJuridica");
    private PessoaFisica pf;
    private PessoaJuridica pj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_dados_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pf = null;
        pj = null;
        Intent intent = getIntent();
        Bundle parametros = intent.getExtras();
        editTextNome = (EditText)findViewById(R.id.editAlterarDadosCadastraisNome);
        editTextTelefone = (EditText)findViewById(R.id.editTextAlterarDadosCadastraisTelefone);
        editTextSenha = (EditText)findViewById(R.id.editTextAlterarDadosCadastraisSenha);
        editTextSenhaConfirmacao = (EditText)findViewById(R.id.editTextAlterarDadosCadastraisSenhaConfirmacao);
        if (parametros!=null) {
            if (parametros.getBoolean("publicar")){
                pj = new PessoaJuridica(parametros.getString("id"), parametros.getString("nome"), parametros.getString("telefone"), parametros.getString("email"), parametros.getString("senha"), parametros.getString("cnpj"), parametros.getBoolean("publicar"));
                editTextNome.setText(pj.getNome());
                editTextTelefone.setText(pj.getTelefone());
                editTextSenha.setText(pj.getSenha());
                editTextSenhaConfirmacao.setText(pj.getSenha());
                editTextNome.setHint("Digite o nome da empresa");
            } else{
                pf = new PessoaFisica(parametros.getString("id"), parametros.getString("nome"), parametros.getString("telefone"), parametros.getString("email"), parametros.getString("senha"), parametros.getString("cpf"), parametros.getBoolean("publicar"));
                editTextNome.setText(pf.getNome());
                editTextTelefone.setText(pf.getTelefone());
                editTextSenha.setText(pf.getSenha());
                editTextSenhaConfirmacao.setText(pf.getSenha());
                editTextNome.setHint("Digite o nome do usuário");
            }
        }
        buttonVoltar = (Button)findViewById(R.id.buttonAlterarDadosCadastraisVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonAlterar = (Button)findViewById(R.id.buttonAlterarDadosCadastraisAlterar);
        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(editTextNome.getText().toString().length()<3){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.logo_erro);
                    dialog.setTitle("Falha ao alterar usuário");
                    dialog.setMessage("Digite um nome com no mínimo 3(três) letras para concluir a alteração");
                    dialog.setNeutralButton("OK",null);
                    dialog.create();
                    dialog.show();
                }
                    else if(editTextTelefone.getText().length()<10 || editTextTelefone.getText().length()>12){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Falha ao alterar usuário");
                        dialog.setMessage("Digite um telefone válido para concluir a alteração");
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }
                        else if(editTextSenha.getText().toString().length()<8 || editTextSenhaConfirmacao.getText().toString().length()<8){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                            dialog.setCancelable(false);
                            dialog.setIcon(R.drawable.logo_erro);
                            dialog.setTitle("Falha ao alterar usuário");
                            dialog.setMessage("Digite uma senha com no mínimo de 8 caracteres");
                            dialog.setNeutralButton("OK",null);
                            dialog.create();
                            dialog.show();
                        }
                            else if(!editTextSenha.getText().toString().equals(editTextSenhaConfirmacao.getText().toString())){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                dialog.setCancelable(false);
                                dialog.setIcon(R.drawable.logo_erro);
                                dialog.setTitle("Falha ao alterar usuário");
                                dialog.setMessage("Os campos de senha e confirmação de senha devem ser iguais");
                                dialog.setNeutralButton("OK",null);
                                dialog.create();
                                dialog.show();
                        }
                else{
                    if(pj!=null){
                        PessoaJuridica p = new PessoaJuridica(pj.getId()
                            ,editTextNome.getText().toString()
                            ,editTextTelefone.getText().toString()
                            ,pj.getEmail()
                            ,editTextSenha.getText().toString()
                            ,pj.getCnpj()
                            ,pj.getPublicar());
                        pessoaJuridica.child(pj.getId()).setValue(p);
                    }
                    else{
                        PessoaFisica p = new PessoaFisica(pf.getId()
                                ,editTextNome.getText().toString()
                                ,editTextTelefone.getText().toString()
                                ,pf.getEmail()
                                ,editTextSenha.getText().toString()
                                ,pf.getCpf()
                                ,pf.getPublicar());
                        pessoaFisica.child(pf.getId()).setValue(p);
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.logo_erro);
                    dialog.setTitle("Usuário alterado");
                    dialog.setMessage("Os dados do usuário foram alterados com sucesso. Deseja fazer outras alterações?");
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){

                        }
                    });
                    dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            finish();
                        }
                    });
                    dialog.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}