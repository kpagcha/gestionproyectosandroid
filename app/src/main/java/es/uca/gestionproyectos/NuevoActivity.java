package es.uca.gestionproyectos;

import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.uca.helpers.EstudianteJsonHelper;
import es.uca.helpers.EstudianteWebServiceHelper;

public class NuevoActivity extends AppCompatActivity {
    private EditText inputNombre;
    private EditText inputPrimerApellido;
    private EditText inputSegundoApellido;
    private EditText inputTitulo;
    private EditText inputTutor1;
    private EditText inputTutor2;
    private RadioGroup inputGroupEstado;
    private RadioButton inputEstado;
    private RadioButton buttonEnDesarrollo;
    private RadioButton buttonPresentado;
    private TextView textFecha;
    private Button buttonFecha;
    private EditText inputCalificacion;
    private Button buttonNuevo;

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String titulo;
    private String tutor1;
    private String tutor2;
    private String estado;
    private String fecha;
    private String calificacion;

    private NotificationCompat.Builder notificacion;
    NotificationManager notificationManager;
    private static final int NOTIFICATION_ALERT_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputNombre = (EditText)findViewById(R.id.input_nombre);
        inputPrimerApellido = (EditText)findViewById(R.id.input_primer_apellido);
        inputSegundoApellido = (EditText)findViewById(R.id.input_segundo_apellido);
        inputTitulo = (EditText)findViewById(R.id.input_titulo);
        inputTutor1 = (EditText)findViewById(R.id.input_tutor1);
        inputTutor2 = (EditText)findViewById(R.id.input_tutor2);
        inputGroupEstado = (RadioGroup)findViewById(R.id.input_estado);
        buttonEnDesarrollo = (RadioButton)findViewById(R.id.button_option_desarrollo);
        buttonPresentado = (RadioButton)findViewById(R.id.button_option_presentado);
        textFecha = (TextView)findViewById(R.id.text_fecha);
        buttonFecha = (Button)findViewById(R.id.button_fecha);
        inputCalificacion = (EditText)findViewById(R.id.input_calificacion);
        buttonNuevo = (Button)findViewById(R.id.button_nuevo);

        buttonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = inputNombre.getText().toString();
                primerApellido = inputPrimerApellido.getText().toString();
                segundoApellido = inputSegundoApellido.getText().toString();
                titulo = inputTitulo.getText().toString();
                tutor1 = inputTutor1.getText().toString();
                tutor2 = inputTutor2.getText().toString();
                fecha = textFecha.getText().toString();
                calificacion = inputCalificacion.getText().toString();

                int selectedId = inputGroupEstado.getCheckedRadioButtonId();
                inputEstado = (RadioButton)findViewById(selectedId);

                estado = inputEstado.getText().toString().toLowerCase();

                new EstudianteAsyncTaskNuevo().execute();
            }
        });

        hideViews();

        buttonEnDesarrollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideViews();
            }
        });

        buttonPresentado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showViews();
            }
        });


        Intent notIntent = new Intent(NuevoActivity.this, TodosActivity.class);
        PendingIntent contIntent =
                PendingIntent.getActivity(NuevoActivity.this, 0, notIntent, 0);

        notificacion = new NotificationCompat.Builder(NuevoActivity.this)
                .setSmallIcon(R.drawable.ic_nuevo)
                .setContentTitle("Se ha creado un nuevo estudiante con éxito")
                .setTicker("¡Estudiante creado!")
                .setContentIntent(contIntent);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // Oculta la información relativa a los campos fecha de presentación y calificación
    private void hideViews() {
        TextView labelFecha = (TextView)findViewById(R.id.text_label_fecha);
        TextView labelCalificacion = (TextView)findViewById(R.id.text_label_calificacion);

        labelFecha.setVisibility(View.GONE);
        textFecha.setVisibility(View.GONE);
        buttonFecha.setVisibility(View.GONE);
        labelCalificacion.setVisibility(View.GONE);
        inputCalificacion.setVisibility(View.GONE);
    }

    // Muestra la información relativa a los campos fecha de presentación y calificación
    private void showViews() {
        TextView labelFecha = (TextView)findViewById(R.id.text_label_fecha);
        TextView labelCalificacion = (TextView)findViewById(R.id.text_label_calificacion);

        labelFecha.setVisibility(View.VISIBLE);
        textFecha.setVisibility(View.VISIBLE);
        buttonFecha.setVisibility(View.VISIBLE);
        labelCalificacion.setVisibility(View.VISIBLE);
        inputCalificacion.setVisibility(View.VISIBLE);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new es.uca.helpers.DatePicker();
        newFragment.show(getFragmentManager(), "date picker");
    }

    private class EstudianteAsyncTaskNuevo extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            EstudianteWebServiceHelper wsHelper = new EstudianteWebServiceHelper("/crear");

            JSONObject postParams = new JSONObject();
            try {
                postParams.put("nombre", nombre);
                postParams.put("primerApellido", primerApellido);
                postParams.put("segundoApellido", segundoApellido);
                postParams.put("tituloProyecto", titulo);
                postParams.put("tutor1", tutor1);
                postParams.put("tutor2", tutor2);
                postParams.put("estadoProyecto", estado);
                postParams.put("fechaPresentacionProyecto", fecha);
                postParams.put("calificacionProyecto", calificacion);
            } catch (JSONException e) {
                return e.toString();
            }

            return wsHelper.performPostRequest(postParams);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    EstudianteJsonHelper jsonHelper = new EstudianteJsonHelper();
                    HashMap<String, String> estudianteInfo = jsonHelper.getHashMap(result);

                    String msg = "Se ha añadido el estudiante " + jsonHelper.estudianteBasicInfo(estudianteInfo);
                    notificacion.setContentText(msg);

                    notificationManager.notify(NOTIFICATION_ALERT_ID, notificacion.build());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                String msg = "Hubo un error al crear un estudiante";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            String msg = "¡Se ha cancelado la ejecución de la tarea!";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
