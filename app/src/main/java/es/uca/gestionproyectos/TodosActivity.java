package es.uca.gestionproyectos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import es.uca.helpers.EstudianteJsonHelper;
import es.uca.helpers.EstudianteWebServiceHelper;

public class TodosActivity extends AppCompatActivity {

    private EstudianteJsonHelper jsonHelper;
    private String todosJsonString;

    private ListView listTodos;
    private ArrayAdapter<String> adapter;

    private Button buttonPresentados;
    private Button buttonEnDesarrollo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listTodos = (ListView)findViewById(R.id.list_todos);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        buttonPresentados = (Button)findViewById(R.id.button_presentados);
        buttonEnDesarrollo = (Button)findViewById(R.id.button_desarrollo);

        buttonPresentados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonHelper != null) {
                    adapter.clear();
                    adapter = jsonHelper.buildPresentadosArrayAdapter(adapter);

                    setListAdapter(adapter);
                }
            }
        });

        buttonEnDesarrollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonHelper != null) {
                    adapter.clear();
                    adapter = jsonHelper.buildEnDesarrolloArrayAdapter(adapter);

                    setListAdapter(adapter);
                }
            }
        });

        new EstudianteAsyncTaskTodos().execute();
    }

    private void setListAdapter(ArrayAdapter<String> adapter) {
        listTodos.setAdapter(adapter);
    }

    private class EstudianteAsyncTaskTodos extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            EstudianteWebServiceHelper wsHelper = new EstudianteWebServiceHelper("/");

            todosJsonString = wsHelper.performGetRequest();
            return todosJsonString;
        }

         @Override
        protected void onPostExecute(String results) {
             if (results != null) {
                 jsonHelper = new EstudianteJsonHelper();
                 adapter = jsonHelper.buildTodosArrayAdapter(adapter, todosJsonString);

                 setListAdapter(adapter);
             }
         }

        @Override
        protected void onCancelled() {
            String msg = "¡Se ha cancelado la ejecución de la tarea!";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
