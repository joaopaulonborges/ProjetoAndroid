package activity;

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
import java.util.ArrayList;
import java.util.List;
import adapter.EventoAdapter;
import listener.RecyclerItemClickListener;
import model.Evento;

public class ExibirEventosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListaEventos;
    private EditText editTextPesquisarEvento;
    private TextView textViewEventoNaoEncontrado;
    private Button buttonVoltar;
    private DatabaseReference evento = FirebaseDatabase.getInstance().getReference("evento");
    private EventoAdapter eventoAdapter;
    private List<Evento> eventos = new ArrayList<>();
    private List<Evento> eventosPesquisar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exibir_eventos_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        textViewEventoNaoEncontrado = (TextView)findViewById(R.id.textViewEventoNaoEncontradoBucas);
        textViewEventoNaoEncontrado.setVisibility(View.VISIBLE);
        buttonVoltar = (Button)findViewById(R.id.buttonExibirEventosVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle parametros = intent.getExtras();
        recyclerViewListaEventos = (RecyclerView)findViewById(R.id.recyclerViewListaEventos);
        recyclerViewListaEventos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListaEventos.setHasFixedSize(true);
        eventoAdapter = new EventoAdapter(eventos, this);
        recyclerViewListaEventos.setAdapter(eventoAdapter);
        eventos.clear();
        recuperarEventos(parametros.getString("localizacao"));
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
        recyclerViewListaEventos.addOnItemTouchListener(
            new RecyclerItemClickListener(
                getApplicationContext()
                , recyclerViewListaEventos
                , new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Evento e = eventos.get(position);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.logo_evento_listar);
                        dialog.setTitle("Endereço do evento");
                        String s = "";
                        s+="Logradouro: "+e.getLogradouro()+"\n";
                        s+="Número: "+e.getNumero()+"\n";
                        s+="Bairro: "+e.getBairro()+"\n";
                        s+="Cidade: "+e.getCidade()+"\n";
                        s+="Estado: "+e.getEstado();
                        dialog.setMessage(s);
                        dialog.setNeutralButton("OK",null);
                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
            )
        );
        editTextPesquisarEvento = (EditText)findViewById(R.id.editTextPesquisarEvento);
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
    }

    public void recuperarEventos(String localizacao){
        Query eventosPesquisa = evento.orderByChild("localizacao").equalTo(localizacao);
        eventosPesquisa.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                if(dataSnapshot.getValue()!=null){
                    eventos.add(dataSnapshot.getValue(Evento.class));
                }
                eventoAdapter.notifyDataSetChanged();
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

    public void pesquisar(){
        int textlength = editTextPesquisarEvento.getText().length();
        eventosPesquisar.clear();
        for (int i = 0; i < eventos.size(); i++ ){
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}