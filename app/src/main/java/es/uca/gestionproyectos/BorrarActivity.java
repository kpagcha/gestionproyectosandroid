package es.uca.gestionproyectos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import es.uca.helpers.EstudianteWebServiceHelper;

public class BorrarActivity extends AppCompatActivity {
    private EditText inputBorrar;
    private Button buttonBorrar;

    private String clave;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputBorrar = (EditText)findViewById(R.id.input_borrar);
        buttonBorrar = (Button)findViewById(R.id.button_borrar);

        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                clave = inputBorrar.getText().toString();
                if (!clave.equals(""))
                    new EstudianteAsyncTaskBorrar().execute();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private class EstudianteAsyncTaskBorrar extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            EstudianteWebServiceHelper wsHelper = null;
            try {
                wsHelper = new EstudianteWebServiceHelper("eliminar", clave);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return e.toString();
            }

            return wsHelper.performDeleteRequest();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && result.equals("true")) {
                Snackbar snackbar =
                        Snackbar.make(view, "Estudiante eliminado", Snackbar.LENGTH_LONG);
                TextView textView =
                        (TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                snackbar.show();
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
