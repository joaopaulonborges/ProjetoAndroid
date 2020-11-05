package activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.app.melhorevento.R;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import permissoes.Permissoes;

public class BuscarEventoActivity extends AppCompatActivity {

    private RadioGroup radioGroupCidade, radioGroupTipoEvento;
    private RadioButton radioButtonCidadeAtual, radioButtonOutraCidade, radioButtonTipo1, radioButtonTipo2, radioButtonTipo3;
    private Button buttonBuscar, buttonVoltar;
    private Spinner spinnerEstado, spinnerMunicipio;
    private CalendarView calendarView;
    private LocationManager locationManager;
    private Location location;
    private double longitude = 0, latitude = 0;
    private int dia, mes, ano, aux;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_evento_activity);
        this.location = null;
        Permissoes.validarPermissoes(permissoes, this, 1);
        calendarView = (CalendarView) findViewById(R.id.calendarViewDataEventoBuscar);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendarView.setMinDate(calendar.getTimeInMillis());
        Date data = new Date();
        calendar.setTime(data);
        this.dia = calendar.get(Calendar.DAY_OF_MONTH);
        this.mes = calendar.get(Calendar.MONTH) + 1;
        this.ano = calendar.get(Calendar.YEAR);
        spinnerEstado = (Spinner) findViewById(R.id.spinnerEstado);
        spinnerEstado.setVisibility(View.INVISIBLE);
        spinnerMunicipio = (Spinner) findViewById(R.id.spinnerMunicipio);
        spinnerMunicipio.setVisibility(View.INVISIBLE);
        buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        radioGroupTipoEvento = (RadioGroup) findViewById(R.id.radioGroupTipoEventoBuscar);
        radioButtonTipo1 = (RadioButton) findViewById(R.id.radioButtonTipo1Buscar);
        radioButtonTipo1.setChecked(true);
        radioButtonTipo2 = (RadioButton) findViewById(R.id.radioButtonTipo2Buscar);
        radioButtonTipo2.setChecked(false);
        radioButtonTipo3 = (RadioButton) findViewById(R.id.radioButtonTipo3Buscar);
        radioButtonTipo3.setChecked(false);
        radioGroupCidade = (RadioGroup) findViewById(R.id.radioGroupCidade);
        radioButtonCidadeAtual = (RadioButton) findViewById(R.id.radioButtonCidadeAtual);
        radioButtonCidadeAtual.setChecked(true);
        radioButtonOutraCidade = (RadioButton) findViewById(R.id.radioButtonOutraCidade);
        radioButtonOutraCidade.setChecked(false);
        radioButtonOutraCidade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioButtonOutraCidade.isChecked()) {
                    spinnerEstado.setVisibility(View.VISIBLE);
                    spinnerMunicipio.setVisibility(View.VISIBLE);
                } else {
                    spinnerEstado.setVisibility(View.INVISIBLE);
                    spinnerMunicipio.setVisibility(View.INVISIBLE);
                }
            }
        });
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Resources r = getResources();
                if (spinnerEstado.getSelectedItem().toString().equals("AC")) {
                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AC, android.R.layout.simple_spinner_item);
                    spinnerMunicipio.setAdapter(adapter);
                }
                    else if (spinnerEstado.getSelectedItem().toString().equals("AL")) {
                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AL, android.R.layout.simple_spinner_item);
                        spinnerMunicipio.setAdapter(adapter);
                    }
                        else if (spinnerEstado.getSelectedItem().toString().equals("AM")) {
                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AM, android.R.layout.simple_spinner_item);
                            spinnerMunicipio.setAdapter(adapter);
                        }
                            else if (spinnerEstado.getSelectedItem().toString().equals("AP")) {
                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.AP, android.R.layout.simple_spinner_item);
                                spinnerMunicipio.setAdapter(adapter);
                            }
                                else if (spinnerEstado.getSelectedItem().toString().equals("BA")) {
                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.BA, android.R.layout.simple_spinner_item);
                                    spinnerMunicipio.setAdapter(adapter);
                                }
                                    else if (spinnerEstado.getSelectedItem().toString().equals("CE")) {
                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.CE, android.R.layout.simple_spinner_item);
                                        spinnerMunicipio.setAdapter(adapter);
                                    }
                                        else if (spinnerEstado.getSelectedItem().toString().equals("DF")) {
                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.DF, android.R.layout.simple_spinner_item);
                                            spinnerMunicipio.setAdapter(adapter);
                                        }
                                            else if (spinnerEstado.getSelectedItem().toString().equals("ES")) {
                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ES, android.R.layout.simple_spinner_item);
                                                spinnerMunicipio.setAdapter(adapter);
                                            }
                                                else if (spinnerEstado.getSelectedItem().toString().equals("GO")) {
                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.GO, android.R.layout.simple_spinner_item);
                                                    spinnerMunicipio.setAdapter(adapter);
                                                }
                                                    else if (spinnerEstado.getSelectedItem().toString().equals("MA")) {
                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MA, android.R.layout.simple_spinner_item);
                                                        spinnerMunicipio.setAdapter(adapter);
                                                    }
                                                        else if (spinnerEstado.getSelectedItem().toString().equals("MG")) {
                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MG, android.R.layout.simple_spinner_item);
                                                            spinnerMunicipio.setAdapter(adapter);
                                                        }
                                                            else if (spinnerEstado.getSelectedItem().toString().equals("MS")) {
                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MS, android.R.layout.simple_spinner_item);
                                                                spinnerMunicipio.setAdapter(adapter);
                                                            }
                                                                else if (spinnerEstado.getSelectedItem().toString().equals("MT")) {
                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.MT, android.R.layout.simple_spinner_item);
                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                }
                                                                    else if (spinnerEstado.getSelectedItem().toString().equals("PA")) {
                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PA, android.R.layout.simple_spinner_item);
                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                    }
                                                                        else if (spinnerEstado.getSelectedItem().toString().equals("PB")) {
                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PB, android.R.layout.simple_spinner_item);
                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                        }
                                                                            else if (spinnerEstado.getSelectedItem().toString().equals("PE")) {
                                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PE, android.R.layout.simple_spinner_item);
                                                                                spinnerMunicipio.setAdapter(adapter);
                                                                            }
                                                                                else if (spinnerEstado.getSelectedItem().toString().equals("PI")) {
                                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PI, android.R.layout.simple_spinner_item);
                                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                                }
                                                                                    else if (spinnerEstado.getSelectedItem().toString().equals("PR")) {
                                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.PR, android.R.layout.simple_spinner_item);
                                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                                    }
                                                                                        else if (spinnerEstado.getSelectedItem().toString().equals("RJ")) {
                                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RJ, android.R.layout.simple_spinner_item);
                                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                                        }
                                                                                            else if (spinnerEstado.getSelectedItem().toString().equals("RN")) {
                                                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RN, android.R.layout.simple_spinner_item);
                                                                                                spinnerMunicipio.setAdapter(adapter);
                                                                                            }
                                                                                                else if (spinnerEstado.getSelectedItem().toString().equals("RO")) {
                                                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RO, android.R.layout.simple_spinner_item);
                                                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                                                }
                                                                                                    else if (spinnerEstado.getSelectedItem().toString().equals("RR")) {
                                                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RR, android.R.layout.simple_spinner_item);
                                                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                                                    }
                                                                                                        else if (spinnerEstado.getSelectedItem().toString().equals("RS")) {
                                                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.RS, android.R.layout.simple_spinner_item);
                                                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                                                        }
                                                                                                            else if (spinnerEstado.getSelectedItem().toString().equals("SC")) {
                                                                                                                ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.SC, android.R.layout.simple_spinner_item);
                                                                                                                spinnerMunicipio.setAdapter(adapter);
                                                                                                            }
                                                                                                                else if (spinnerEstado.getSelectedItem().toString().equals("SE")) {
                                                                                                                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.SE, android.R.layout.simple_spinner_item);
                                                                                                                    spinnerMunicipio.setAdapter(adapter);
                                                                                                                }
                                                                                                                    else if (spinnerEstado.getSelectedItem().toString().equals("SP")) {
                                                                                                                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.SP, android.R.layout.simple_spinner_item);
                                                                                                                        spinnerMunicipio.setAdapter(adapter);
                                                                                                                    }
                                                                                                                        else if (spinnerEstado.getSelectedItem().toString().equals("TO")) {
                                                                                                                            ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.TO, android.R.layout.simple_spinner_item);
                                                                                                                            spinnerMunicipio.setAdapter(adapter);
                                                                                                                        }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dia = dayOfMonth;
                mes = month + 1;
                ano = year;
            }
        });
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(radioButtonCidadeAtual.isChecked()){
                    verificarGps();
                    procurarEvento();
                }
                else{
                    String cidade = spinnerMunicipio.getSelectedItem().toString();
                    String estado = spinnerEstado.getSelectedItem().toString();
                    int tipo = 0;
                    if (radioButtonTipo1.isChecked()) {
                        tipo = 1;
                    } else if (radioButtonTipo2.isChecked()) {
                        tipo = 2;
                    } else if (radioButtonTipo3.isChecked()) {
                        tipo = 3;
                    }
                    Intent intent = new Intent(getApplicationContext(), ExibirEventosActivity.class);
                    Bundle parametros = new Bundle();
                    parametros.putString("localizacao", Integer.toString(dia) + "-" + Integer.toString(mes) + "-" + Integer.toString(ano) + "-" + Integer.toString(tipo) + "-" + cidade + "-" + estado);
                    intent.putExtras(parametros);
                    startActivity(intent);
                }
            }
        });
        buttonVoltar = (Button) findViewById(R.id.buttonVoltarBuscarEvento);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
            ativarGps();
        }
        else{
            atualizarGps();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_GRANTED){

            }
            else{
                alertaPermissaoLocalizacao();
            }
        }
    }

    public void verificarGps(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else{
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.logo_erro);
            dialog.setTitle("Falha ao buscar evento");
            dialog.setMessage("Para utilizar o app é necessário aceitar as permissões");
            dialog.setNeutralButton("OK", null);
            dialog.create();
            dialog.show();
        }
    }

    public void atualizarGps(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, new LocationListener(){
                @Override
                public void onLocationChanged(Location location){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, new LocationListener(){
                @Override
                public void onLocationChanged(Location location){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }
    }

    public void procurarEvento(){
        Geocoder g = new Geocoder(this);
        try{
            String cidade = "";
            String estado = "";
            List<Address> lista = g.getFromLocation(latitude, longitude, 1);
            cidade = lista.get(0).getSubAdminArea();
            estado = lista.get(0).getAdminArea();
            cidade = cidade.replace("'", " ");
            if (estado.equals("Acre")){
                estado = "AC";
            }
                else if (estado.equals("Alagoas")) {
                    estado = "AL";
                }
                    else if (estado.equals("Amazonas")) {
                        estado = "AM";
                    }
                        else if (estado.equals("Amapá")) {
                            estado = "AP";
                        }
                            else if (estado.equals("Bahia")) {
                                estado = "BA";
                            }
                                else if (estado.equals("Ceará")) {
                                    estado = "CE";
                                }
                                    else if (estado.equals("Distrito Federal")) {
                                        estado = "DF";
                                    }
                                        else if (estado.equals("Espírito Santo")) {
                                            estado = "ES";
                                        }
                                            else if (estado.equals("Goiás")) {
                                                estado = "GO";
                                            }
                                                else if (estado.equals("Maranhão")) {
                                                    estado = "MA";
                                                }
                                                    else if (estado.equals("Minas Gerais")) {
                                                        estado = "MG";
                                                    }
                                                        else if (estado.equals("Mato Grosso")) {
                                                            estado = "MT";
                                                        }
                                                            else if (estado.equals("Mato Grosso do Sul")) {
                                                                estado = "MS";
                                                            }
                                                                else if (estado.equals("Pará")) {
                                                                    estado = "PA";
                                                                }
                                                                    else if (estado.equals("Paraíba")) {
                                                                        estado = "PB";
                                                                    }
                                                                        else if (estado.equals("Pernambuco")) {
                                                                            estado = "PE";
                                                                        }
                                                                            else if (estado.equals("Piauí")) {
                                                                                estado = "PI";
                                                                            }
                                                                                else if (estado.equals("Paraná")) {
                                                                                    estado = "PR";
                                                                                }
                                                                                    else if (estado.equals("Rio de Janeiro")) {
                                                                                        estado = "RJ";
                                                                                    }
                                                                                        else if (estado.equals("Rio Grande do Norte")) {
                                                                                            estado = "RN";
                                                                                        }
                                                                                            else if (estado.equals("Rondônia")) {
                                                                                                estado = "RO";
                                                                                            }
                                                                                                else if (estado.equals("Roraima")) {
                                                                                                    estado = "RR";
                                                                                                }
                                                                                                    else if (estado.equals("Rio Grande do Sul")) {
                                                                                                        estado = "RS";
                                                                                                    }
                                                                                                        else if (estado.equals("Santa Catarina")) {
                                                                                                            estado = "SC";
                                                                                                        }
                                                                                                            else if (estado.equals("Sergipe")) {
                                                                                                                estado = "SE";
                                                                                                            }
                                                                                                                else if (estado.equals("São Paulo")) {
                                                                                                                    estado = "SP";
                                                                                                                }
                                                                                                                    else if (estado.equals("Tocantins")) {
                                                                                                                        estado = "TO";
                                                                                                                    }
            int tipo = 0;
            if (radioButtonTipo1.isChecked()) {
                tipo = 1;
            } else if (radioButtonTipo2.isChecked()) {
                tipo = 2;
            } else if (radioButtonTipo3.isChecked()) {
                tipo = 3;
            }
            Intent intent = new Intent(getApplicationContext(), ExibirEventosActivity.class);
            Bundle parametros = new Bundle();
            parametros.putString("localizacao", Integer.toString(dia) + "-" + Integer.toString(mes) + "-" + Integer.toString(ano) + "-" + Integer.toString(tipo) + "-" + cidade + "-" + estado);
            intent.putExtras(parametros);
            startActivity(intent);
        }
        catch (Exception e){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.logo_erro);
            dialog.setTitle("Não foi possível buscar os eventos");
            dialog.setMessage("Falha ao encontrar sua localização. Tente novamente ou procure a cidade manualmente");
            dialog.setNeutralButton("OK", null);
            dialog.create();
            dialog.show();
        }
    }

    public void ativarGps(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Seu GPS está desativado, é necessário ativa-lo para buscar por eventos.")
        .setCancelable(false)
        .setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                finish();
            }
        })
        .setNegativeButton("Não", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void alertaPermissaoLocalizacao(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.logo_erro);
        dialog.setTitle("Permissão negada");
        dialog.setMessage("Para utilizar o app é necessário aceitar as permissões");
        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}