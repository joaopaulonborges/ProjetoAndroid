package activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.melhorevento.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import adapter.EventoAdapter;
import listener.RecyclerItemClickListener;
import model.Evento;

public class AlterarEventoListagemActivity extends AppCompatActivity {

    private Boolean verificar;
    private RecyclerView recyclerViewListaEventos;
    private EditText editTextPesquisarEvento;
    private TextView textViewEventoNaoEncontrado;
    private Button buttonVoltar;
    private int dia, mes, ano;
    private DatabaseReference evento = FirebaseDatabase.getInstance().getReference("evento");
    private StorageReference imagem = FirebaseStorage.getInstance().getReference("eventos");
    private EventoAdapter eventoAdapter;
    private List<Evento> eventos = new ArrayList<>();
    private List<Evento> eventosPesquisar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_evento_listagem_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.verificar = true;
        textViewEventoNaoEncontrado = (TextView)findViewById(R.id.textViewEventoNaoEncontradoListar);
        textViewEventoNaoEncontrado.setVisibility(View.VISIBLE);
        buttonVoltar = (Button)findViewById(R.id.buttonAlterarEventosVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle parametros = intent.getExtras();
        recyclerViewListaEventos = (RecyclerView)findViewById(R.id.recyclerViewListaEventosAlterar);
        recyclerViewListaEventos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListaEventos.setHasFixedSize(true);
        eventoAdapter = new EventoAdapter(eventos, this);
        recyclerViewListaEventos.setAdapter(eventoAdapter);
        eventos.clear();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date data = new Date();
        calendar.setTime(data);
        this.dia = calendar.get(Calendar.DAY_OF_MONTH);
        this.mes = calendar.get(Calendar.MONTH)+1;
        this.ano = calendar.get(Calendar.YEAR);
        recuperarEventos(parametros.getString("email")+"-"+mes+"-"+ano);
        recyclerViewListaEventos.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                textViewEventoNaoEncontrado.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                textViewEventoNaoEncontrado.setVisibility(View.VISIBLE);
            }
        });
        editTextPesquisarEvento = (EditText)findViewById(R.id.editTextPesquisarEventoAlterar);
        editTextPesquisarEvento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesquisar();
                eventoAdapter = new EventoAdapter(eventosPesquisar, getApplicationContext());
                recyclerViewListaEventos.setAdapter(eventoAdapter);
                eventoAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerViewListaEventos.addOnItemTouchListener(
            new RecyclerItemClickListener(
                getApplicationContext()
                , recyclerViewListaEventos
                , new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_evento_listar);
                        dialog.setTitle("Alterar dados do evento");
                        dialog.setMessage("Deseja alterar os dados do evento?");
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                verificar = false;
                                Evento e;
                                int p = position;
                                if(editTextPesquisarEvento.getText().toString().equals("")) {
                                    e = eventos.get(p);
                                }
                                else {
                                    e = eventosPesquisar.get(p);
                                }
                                Intent intent = new Intent(getApplicationContext(), AlterarEventoActivity.class);
                                Bundle parametros = new Bundle();
                                parametros.putString("id", e.getId());
                                parametros.putString("titulo", e.getTitulo());
                                parametros.putString("descricao", e.getDescricao());
                                parametros.putString("logradouro", e.getLogradouro());
                                parametros.putString("numero", e.getNumero());
                                parametros.putString("bairro", e.getBairro());
                                parametros.putString("idPessoa",e.getPessoaJuridica().getId());
                                Intent i = getIntent();
                                Bundle pa = i.getExtras();
                                parametros.putString("nomePessoa",pa.getString("nome"));
                                parametros.putString("telefonePessoa",pa.getString("telefone"));
                                parametros.putString("emailPessoa",e.getPessoaJuridica().getEmail());
                                parametros.putString("senhaPessoa",pa.getString("senha"));
                                parametros.putString("cnpjPessoa",e.getPessoaJuridica().getCnpj());
                                parametros.putBoolean("publicarPessoa",e.getPessoaJuridica().getPublicar());
                                intent.putExtras(parametros);
                                startActivity(intent);
                            }
                        });
                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_evento_listar);
                        dialog.setTitle("Excluir evento");
                        dialog.setMessage("Deseja excluir o evento selecionado?");
                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Evento e;
                                int p = position;
                                if(editTextPesquisarEvento.getText().toString().equals("")) {
                                    e = eventos.get(p);
                                    evento.child(e.getId()).removeValue();
                                    StorageReference storageReference = imagem.child(e.getId()+".jpeg");
                                    storageReference.delete();
                                }
                                else {
                                    e = eventosPesquisar.get(p);
                                    evento.child(e.getId()).removeValue();
                                    StorageReference storageReference = imagem.child(e.getId()+".jpeg");
                                    storageReference.delete();
                                }
                            }
                        });
                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
            )
        );
    }

    public void recuperarEventos(String data){
        Query eventosPesquisa = evento.orderByChild("data").equalTo(data);
        eventos.clear();
        eventosPesquisa.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                if(dataSnapshot.getValue()!=null){
                    Evento e = dataSnapshot.getValue(Evento.class);
                    if(e.getDia()>=dia){
                        eventos.add(e);
                    }
                }
                eventoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Evento e = dataSnapshot.getValue(Evento.class);
                for(int i=0;i<eventos.size();i++) {
                    if(eventos.get(i).getId().equals(e.getId())){
                        eventos.remove(i);
                        break;
                    }
                }
                if(editTextPesquisarEvento.getText().toString().equals("")){
                    eventoAdapter = new EventoAdapter(eventos, getApplicationContext());
                    recyclerViewListaEventos.setAdapter(eventoAdapter);
                    eventoAdapter.notifyDataSetChanged();
                }
                else{
                    pesquisar();
                    eventoAdapter = new EventoAdapter(eventosPesquisar, getApplicationContext());
                    recyclerViewListaEventos.setAdapter(eventoAdapter);
                    eventoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pesquisar() {
        int textlength = editTextPesquisarEvento.getText().length();
        eventosPesquisar.clear();
        for (int i = 0; i < eventos.size(); i++ ) {
            if (textlength <= eventos.get(i).getTitulo().length()){
                String titulo = eventos.get(i).getTitulo().toLowerCase();
                String busca = editTextPesquisarEvento.getText().toString().toLowerCase();
                if (titulo.contains(busca)){
                    eventosPesquisar.add(eventos.get(i));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.verificar == false){
            eventos.clear();
            Intent intent = getIntent();
            Bundle parametros = intent.getExtras();
            recuperarEventos(parametros.getString("email")+"-"+mes+"-"+ano);
            verificar = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}