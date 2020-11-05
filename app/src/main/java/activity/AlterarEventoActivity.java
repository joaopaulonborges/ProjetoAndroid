package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.app.melhorevento.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import model.Evento;
import model.PessoaJuridica;

public class AlterarEventoActivity extends AppCompatActivity {

    private RadioGroup radioGroupTipoEvento;
    private RadioButton radioButtonTipo1, radioButtonTipo2, radioButtonTipo3;
    private EditText nomeEvento, descricaoEvento, logradouroEvento, numeroEvento, bairroEvento;
    private TextView textViewImagemEvento;
    private Button buttonAlterar, buttonVoltar, buttonSelecionarImagem;
    private Spinner spinnerEstado, spinnerMunicipio, spinnerHora, spinnerMinutos;
    private CalendarView calendarView;
    private Integer dia, mes, ano;
    private Bitmap imagemEvento;
    private DatabaseReference evento = FirebaseDatabase.getInstance().getReference("evento");
    private StorageReference imagem = FirebaseStorage.getInstance().getReference("eventos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_evento_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imagemEvento = null;
        Intent intent = getIntent();
        final Bundle parametros = intent.getExtras();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date data = new Date();
        calendar.setTime(data);
        calendarView = (CalendarView)findViewById(R.id.calendarViewDataEventoAlterar);
        calendarView.setMinDate(calendar.getTimeInMillis());
        this.dia = calendar.get(Calendar.DAY_OF_MONTH);
        this.mes = calendar.get(Calendar.MONTH)+1;
        this.ano = calendar.get(Calendar.YEAR);
        nomeEvento = (EditText)findViewById(R.id.editTextNomeEventoAlterar);
        descricaoEvento = (EditText)findViewById(R.id.editTextDescricaoEventoAlterar);
        logradouroEvento = (EditText)findViewById(R.id.editTextLogradouroEventoAlterar);
        numeroEvento = (EditText)findViewById(R.id.editTextNumeroEventoAlterar);
        bairroEvento = (EditText)findViewById(R.id.editTextBairroEventoAlterar);
        nomeEvento.setText(parametros.getString("titulo"));
        descricaoEvento.setText(parametros.getString("descricao"));
        logradouroEvento.setText(parametros.getString("logradouro"));
        numeroEvento.setText(parametros.getString("numero"));
        bairroEvento.setText(parametros.getString("bairro"));
        radioGroupTipoEvento = (RadioGroup)findViewById(R.id.radioGroupTipoEventoAlterar);
        radioButtonTipo1 = (RadioButton)findViewById(R.id.radioButtonTipo1Alterar);
        radioButtonTipo1.setChecked(true);
        radioButtonTipo2 = (RadioButton)findViewById(R.id.radioButtonTipo2Alterar);
        radioButtonTipo2.setChecked(false);
        radioButtonTipo3 = (RadioButton)findViewById(R.id.radioButtonTipo3Alterar);
        radioButtonTipo3.setChecked(false);
        spinnerEstado = (Spinner)findViewById(R.id.spinnerEstadoAlterar);
        spinnerMunicipio = (Spinner)findViewById(R.id.spinnerMunicipioAlterar);
        spinnerHora = (Spinner)findViewById(R.id.spinnerHoraAlterar);
        spinnerMinutos = (Spinner)findViewById(R.id.spinnerMinutosAlterar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dia = dayOfMonth;
                mes = month+1;
                ano = year;
            }
        });
        buttonAlterar = (Button)findViewById(R.id.buttonAlterarEvento);
        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nomeEvento.getText().length()<3 || nomeEvento.getText().length()>75){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setCancelable(false);
                    dialog.setIcon(R.drawable.logo_erro);
                    dialog.setTitle("Falha ao criar evento");
                    dialog.setMessage("Digite um nome para o evento contendo no mínimo 3(três) caracteres e máximo de 70(setenta) caracteres. Quantidade atual de caracteres atual: "+nomeEvento.getText().length());
                    dialog.setNeutralButton("OK",null);
                    dialog.create();
                    dialog.show();
                }
                    else if(descricaoEvento.getText().length()<3 || descricaoEvento.getText().length()>500){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Falha ao criar evento");
                        dialog.setMessage("Digite uma descrição para o evento contendo no mínimo de 3(três) caracteres e máximo de 500(quinhentos) caracteres. Quantidade atual de caracteres atual: "+descricaoEvento.getText().length());
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }
                        else if(logradouroEvento.getText().length()<3 || logradouroEvento.getText().length()>100){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                            dialog.setCancelable(false);
                            dialog.setIcon(R.drawable.logo_erro);
                            dialog.setTitle("Falha ao criar evento");
                            dialog.setMessage("Digite um logradouro para o evento contendo no mínimo 3(três) caracteres e máximo de 100(cem) caracretes. Quantidade atual de caracteres atual: "+logradouroEvento.getText().length());
                            dialog.setNeutralButton("OK",null);
                            dialog.create();
                            dialog.show();
                        }
                            else if(bairroEvento.getText().length()<3 || bairroEvento.getText().length()>80){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                dialog.setCancelable(false);
                                dialog.setIcon(R.drawable.logo_erro);
                                dialog.setTitle("Falha ao criar evento");
                                dialog.setMessage("Digite um bairro para o evento contendo no mínimo 3(três) caracteres e máximp de 80(oitenta) caracteres. Quantidade atual de caracteres atual: "+bairroEvento.getText().length());
                                dialog.setNeutralButton("OK",null);
                                dialog.create();
                                dialog.show();
                            }
                                else if(imagemEvento == null){
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                                    dialog.setCancelable(false);
                                    dialog.setIcon(R.drawable.logo_erro);
                                    dialog.setTitle("Falha ao criar evento");
                                    dialog.setMessage("Selecione uma imagem para o evento");
                                    dialog.setNeutralButton("OK",null);
                                    dialog.create();
                                    dialog.show();
                                }
                else{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagemEvento.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                    byte[] dadosImagem = baos.toByteArray();
                    long tamanho = dadosImagem.length / 1024;
                    if(tamanho>2048){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Falha ao criar evento");
                        dialog.setMessage("A imagem do evento não pode ter mais que 2 Mebabytes");
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }
                    else {
                        int tipo;
                        String estado, cidade, hora, minutos, numero;
                        if (radioButtonTipo1.isChecked()) {
                            tipo = 1;
                        } else if (radioButtonTipo2.isChecked()) {
                            tipo = 2;
                        } else if (radioButtonTipo3.isChecked()) {
                            tipo = 3;
                        } else {
                            tipo = 0;
                        }
                        if (numeroEvento.getText().toString().equals("")) {
                            numero = "S/N";
                        } else {
                            numero = numeroEvento.getText().toString();
                        }
                        cidade = spinnerMunicipio.getSelectedItem().toString();
                        estado = spinnerEstado.getSelectedItem().toString();
                        hora = spinnerHora.getSelectedItem().toString();
                        minutos = spinnerMinutos.getSelectedItem().toString();
                        StorageReference salvarImagem = imagem.child(parametros.getString("id")+".jpeg");
                        UploadTask uploadTask = salvarImagem.putBytes(dadosImagem);
                        PessoaJuridica pj = new PessoaJuridica(parametros.getString("idPessoa")
                                , parametros.getString("nomePessoa")
                                , parametros.getString("telefonePessoa")
                                , parametros.getString("emailPessoa")
                                , parametros.getString("senhaPessoa")
                                , parametros.getString("cnpjPessoa")
                                , parametros.getBoolean("publicarPessoa"));
                        Evento e = new Evento(parametros.getString("id"), nomeEvento.getText().toString(), descricaoEvento.getText().toString()
                                , logradouroEvento.getText().toString(), numero
                                , bairroEvento.getText().toString(), cidade, estado, hora, minutos, dia, mes, ano, tipo, pj
                                , Integer.toString(dia) + "-" + Integer.toString(mes) + "-" + Integer.toString(ano) + "-" + Integer.toString(tipo) + "-" + cidade + "-" + estado
                                , parametros.getString("emailPessoa") + "-" + mes + "-" + ano);
                        evento.child(parametros.getString("id")).setValue(e);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_erro);
                        dialog.setTitle("Evento alterado");
                        dialog.setMessage("Evento alterado com sucesso. Deseja fazer outras alterações?");
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
            }
        });
        buttonVoltar = (Button)findViewById(R.id.buttonAlteraEventoVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewImagemEvento = (TextView)findViewById(R.id.textViewImagemEventoAlterar);
        buttonSelecionarImagem = (Button)findViewById(R.id.buttonImagemEventoAlterar);
        buttonSelecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 1);
            }
        });
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources r = getResources();
                if(spinnerEstado.getSelectedItem().toString().equals("AC")){
                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AC, android.R.layout.simple_spinner_item);
                    spinnerMunicipio.setAdapter(adapter);
                }
                    else if(spinnerEstado.getSelectedItem().toString().equals("AL")){
                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AL, android.R.layout.simple_spinner_item);
                        spinnerMunicipio.setAdapter(adapter);
                    }
                        else if(spinnerEstado.getSelectedItem().toString().equals("AM")){
                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AM, android.R.layout.simple_spinner_item);
                            spinnerMunicipio.setAdapter(adapter);
                        }
                            else if(spinnerEstado.getSelectedItem().toString().equals("AP")){
                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AP, android.R.layout.simple_spinner_item);
                                spinnerMunicipio.setAdapter(adapter);
                            }
                                else if(spinnerEstado.getSelectedItem().toString().equals("BA")){
                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.BA, android.R.layout.simple_spinner_item);
                                    spinnerMunicipio.setAdapter(adapter);
                                }
                                    else if(spinnerEstado.getSelectedItem().toString().equals("CE")){
                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.CE, android.R.layout.simple_spinner_item);
                                        spinnerMunicipio.setAdapter(adapter);
                                    }
                                        else if(spinnerEstado.getSelectedItem().toString().equals("DF")){
                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.DF, android.R.layout.simple_spinner_item);
                                            spinnerMunicipio.setAdapter(adapter);
                                        }
                                            else if(spinnerEstado.getSelectedItem().toString().equals("ES")){
                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ES, android.R.layout.simple_spinner_item);
                                                spinnerMunicipio.setAdapter(adapter);
                                            }
                                                else if(spinnerEstado.getSelectedItem().toString().equals("GO")){
                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.GO, android.R.layout.simple_spinner_item);
                                                    spinnerMunicipio.setAdapter(adapter);
                                                }
                                                    else if(spinnerEstado.getSelectedItem().toString().equals("MA")){
                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MA, android.R.layout.simple_spinner_item);
                                                        spinnerMunicipio.setAdapter(adapter);
                                                    }
                                                        else if(spinnerEstado.getSelectedItem().toString().equals("MG")){
                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MG, android.R.layout.simple_spinner_item);
                                                            spinnerMunicipio.setAdapter(adapter);
                                                        }
                                                            else if(spinnerEstado.getSelectedItem().toString().equals("MS")){
                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MS, android.R.layout.simple_spinner_item);
                                                                spinnerMunicipio.setAdapter(adapter);
                                                            }
                                                                else if(spinnerEstado.getSelectedItem().toString().equals("MT")){
                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MT, android.R.layout.simple_spinner_item);
                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                }
                                                                    else if(spinnerEstado.getSelectedItem().toString().equals("PA")){
                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PA, android.R.layout.simple_spinner_item);
                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                    }
                                                                        else if(spinnerEstado.getSelectedItem().toString().equals("PB")){
                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PB, android.R.layout.simple_spinner_item);
                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                        }
                                                                            else if(spinnerEstado.getSelectedItem().toString().equals("PE")){
                                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PE, android.R.layout.simple_spinner_item);
                                                                                spinnerMunicipio.setAdapter(adapter);
                                                                            }
                                                                                else if(spinnerEstado.getSelectedItem().toString().equals("PI")){
                                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PI, android.R.layout.simple_spinner_item);
                                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                                }
                                                                                    else if(spinnerEstado.getSelectedItem().toString().equals("PR")){
                                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PR, android.R.layout.simple_spinner_item);
                                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                                    }
                                                                                        else if(spinnerEstado.getSelectedItem().toString().equals("RJ")){
                                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RJ, android.R.layout.simple_spinner_item);
                                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                                        }
                                                                                            else if(spinnerEstado.getSelectedItem().toString().equals("RN")){
                                                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RN, android.R.layout.simple_spinner_item);
                                                                                                spinnerMunicipio.setAdapter(adapter);
                                                                                            }
                                                                                                else if(spinnerEstado.getSelectedItem().toString().equals("RO")){
                                                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RO, android.R.layout.simple_spinner_item);
                                                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                                                }
                                                                                                    else if(spinnerEstado.getSelectedItem().toString().equals("RR")){
                                                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RR, android.R.layout.simple_spinner_item);
                                                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                                                    }
                                                                                                        else if(spinnerEstado.getSelectedItem().toString().equals("RS")){
                                                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RS, android.R.layout.simple_spinner_item);
                                                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                                                        }
                                                                                                            else if(spinnerEstado.getSelectedItem().toString().equals("SC")){
                                                                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.SC, android.R.layout.simple_spinner_item);
                                                                                                                spinnerMunicipio.setAdapter(adapter);
                                                                                                            }
                                                                                                                else if(spinnerEstado.getSelectedItem().toString().equals("SE")){
                                                                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.SE, android.R.layout.simple_spinner_item);
                                                                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                                                                }
                                                                                                                    else if(spinnerEstado.getSelectedItem().toString().equals("SP")){
                                                                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.SP, android.R.layout.simple_spinner_item);
                                                                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                                                                    }
                                                                                                                        else if(spinnerEstado.getSelectedItem().toString().equals("TO")){
                                                                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TO, android.R.layout.simple_spinner_item);
                                                                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                                                                        }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri localImagemSelecionada = data.getData();
            try {
                imagemEvento = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
            } catch (IOException e){
                AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                dialog.setCancelable(false);
                dialog.setIcon(R.drawable.logo_erro);
                dialog.setTitle("Erro ao selecionar imagem");
                dialog.setMessage("Não foi possível selecionar a imagem");
                dialog.setNeutralButton("OK", null);
                dialog.create();
                dialog.show();
            }
            textViewImagemEvento.setText("Imagem selecionada");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}