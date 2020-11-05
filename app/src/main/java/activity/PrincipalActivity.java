package activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import com.app.melhorevento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import model.PessoaFisica;
import model.PessoaJuridica;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView textViewNomePerfil, textViewEmailPerfil;
    private PessoaFisica pf;
    private PessoaJuridica pj;
    private View v;
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private DatabaseReference pessoaFisica = FirebaseDatabase.getInstance().getReference("pessoaFisica");
    private DatabaseReference pessoaJuridica = FirebaseDatabase.getInstance().getReference("pessoaJuridica");

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        Intent intent = getIntent();
        Bundle parametros = intent.getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if(!parametros.getBoolean("publicar")){
            navigationView.getMenu().findItem(R.id.nav_cadastrar_evento).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_listar_evento).setVisible(false);
        }
        this.v = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        textViewNomePerfil = (TextView)v.findViewById(R.id.textViewNomePerfil);
        textViewEmailPerfil = (TextView)v.findViewById(R.id.textViewEmailPerfil);
        pf = null;
        pj = null;
        if (parametros!=null){
            if(parametros.getBoolean("publicar")){
                pj = new PessoaJuridica(parametros.getString("id"), parametros.getString("nome"), parametros.getString("telefone"), parametros.getString("email"), parametros.getString("senha"), parametros.getString("cnpj"), parametros.getBoolean("publicar"));
                textViewNomePerfil.setText(pj.getNome());
                textViewEmailPerfil.setText(pj.getEmail());
                procurarPessoaJuridica(parametros.getString("email"));
            }
            else{
                pf = new PessoaFisica(parametros.getString("id"), parametros.getString("nome"), parametros.getString("telefone"), parametros.getString("email"), parametros.getString("senha"), parametros.getString("cpf"), parametros.getBoolean("publicar"));
                textViewNomePerfil.setText(pf.getNome());
                textViewEmailPerfil.setText(pf.getEmail());
                procurarPessoaFisica(parametros.getString("email"));
            }
        }
        else{
            textViewNomePerfil.setText("Usuário Anônimo");
            textViewEmailPerfil.setText("anonimo@email.com");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(pj!=null){
            textViewNomePerfil.setText(pj.getNome());
        }
        else{
            textViewNomePerfil.setText(pf.getNome());
        }
    }

    public void procurarPessoaFisica(String email){
        Query pessoaFisicaQuery = pessoaFisica.orderByChild("email").equalTo(email);
        pessoaFisicaQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    PessoaFisica p = dataSnapshot.getValue(PessoaFisica.class);
                    pf.setNome(p.getNome());
                    pf.setTelefone(p.getTelefone());
                    pf.setSenha(p.getSenha());
                }
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

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue()!=null) {
                    PessoaJuridica p = dataSnapshot.getValue(PessoaJuridica.class);
                    pj.setNome(p.getNome());
                    pj.setTelefone(p.getTelefone());
                    pj.setSenha(p.getSenha());
                }
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

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.menu_sobre){
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_buscar_evento){
            Intent intent = new Intent(this, BuscarEventoActivity.class);
            startActivity(intent);
        }
            else if(id == R.id.nav_cadastrar_evento){
                Intent intent = new Intent(getApplicationContext(), CadastrarEventoActivity.class);
                Bundle parametros = new Bundle();
                parametros.putString("id", pj.getId());
                parametros.putString("nome", pj.getNome());
                parametros.putString("telefone", pj.getTelefone());
                parametros.putString("email", pj.getEmail());
                parametros.putString("senha", pj.getSenha());
                parametros.putString("cnpj", pj.getCnpj());
                parametros.putBoolean("publicar", pj.getPublicar());
                intent.putExtras(parametros);
                startActivity(intent);
            }
                else if(id == R.id.nav_listar_evento){
                    Intent intent = new Intent(this, AlterarEventoListagemActivity.class);
                    Bundle parametros = new Bundle();
                    parametros.putString("email", pj.getEmail());
                    parametros.putString("nome", pj.getNome());
                    parametros.putString("telefone", pj.getTelefone());
                    parametros.putString("senha", pj.getSenha());
                    intent.putExtras(parametros);
                    startActivity(intent);
                }
                    else if(id == R.id.nav_alterar_dados){
                        Intent intent = new Intent(this, AlterarDadosActivity.class);
                        Bundle parametros = new Bundle();
                        if(pj!=null){
                            parametros.putString("id", pj.getId());
                            parametros.putString("nome", pj.getNome());
                            parametros.putString("telefone", pj.getTelefone());
                            parametros.putString("email", pj.getEmail());
                            parametros.putString("senha", pj.getSenha());
                            parametros.putString("cnpj", pj.getCnpj());
                            parametros.putBoolean("publicar", pj.getPublicar());
                            intent.putExtras(parametros);
                        }
                        else{
                            parametros.putString("id", pf.getId());
                            parametros.putString("nome", pf.getNome());
                            parametros.putString("telefone", pf.getTelefone());
                            parametros.putString("email", pf.getEmail());
                            parametros.putString("senha", pf.getSenha());
                            parametros.putString("cpf", pf.getCpf());
                            parametros.putBoolean("publicar", pf.getPublicar());
                            intent.putExtras(parametros);
                        }
                        startActivity(intent);
                    }
                        else if (id == R.id.nav_sair){
                            usuario.signOut();
                            finish();
                        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}