package es.uca.gestionproyectos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import es.uca.helpers.EstudianteWebServiceHelper;

public class BuscarActivity extends AppCompatActivity {
    private EditText inputBuscar;
    private Button buttonBuscar;
    private TextView textInfo;

    private String clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputBuscar = (EditText)findViewById(R.id.input_buscar);
        buttonBuscar = (Button)findViewById(R.id.button_buscar);
        textInfo = (TextView)findViewById(R.id.text_info_buscar);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clave = inputBuscar.getText().toString();
                if (!clave.equals(""))
                    new EstudianteAsyncTaskBuscar().execute();
            }
        });
    }

    private class EstudianteAsyncTaskBuscar extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            EstudianteWebServiceHelper wsHelper = null;
            try {
                wsHelper = new EstudianteWebServiceHelper("buscar", clave);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return e.toString();
            }

            return wsHelper.performGetRequest();
        }

        @Override
        protected void onPostExecute(String results) {
            if (results != null) {
                textInfo.setText(results);
            } else {
                String msg = "No se ha encontrado el estudiante con los apellidos " + clave;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            String msg = "¡Se ha cancelado la ejecución de la tarea!";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
