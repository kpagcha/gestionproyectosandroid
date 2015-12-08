package es.uca.helpers;

import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EstudianteJsonHelper {

    private JSONArray estudiantesJsonArray;

    public ArrayAdapter<String> buildTodosArrayAdapter(ArrayAdapter<String> adapter, String src) {
        try {
            estudiantesJsonArray = new JSONArray(src);
            int n = estudiantesJsonArray.length();

            for (int i = 0; i < n; i++)
                adapter.add(estudianteItemString(i));
        } catch (JSONException e) {
            adapter.add(e.toString());
            e.printStackTrace();
        }
        return adapter;
    }

    public ArrayAdapter<String> buildPresentadosArrayAdapter(ArrayAdapter<String> adapter) {
        try {
            int n = estudiantesJsonArray.length();

            for (int i = 0; i < n; i++) {
                JSONObject jsonObject = estudiantesJsonArray.getJSONObject(i);
                String estadoProyecto = jsonObject.getString("estadoProyecto");

                if (estadoProyecto.equalsIgnoreCase("presentado")) {
                    String fecha = jsonObject.getString("fechaPresentacionProyecto");
                    adapter.add(estudianteItemString(i) + ". Proyecto presentado el " + fecha);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return adapter;
    }

    public ArrayAdapter<String> buildEnDesarrolloArrayAdapter(ArrayAdapter<String> adapter) {
        try {
            int n = estudiantesJsonArray.length();

            for (int i = 0; i < n; i++) {
                JSONObject jsonObject = estudiantesJsonArray.getJSONObject(i);
                String estadoProyecto = jsonObject.getString("estadoProyecto");

                if (estadoProyecto.equalsIgnoreCase("en desarrollo")) {
                    String titulo = jsonObject.getString("tituloProyecto");
                    adapter.add(estudianteItemString(i) + ". Proyecto: " + titulo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return adapter;
    }

    private String estudianteItemString(int i) throws JSONException {
        String primerApellido = estudiantesJsonArray.getJSONObject(i).getString("primerApellido");
        String segundoApellido = estudiantesJsonArray.getJSONObject(i).isNull("segundoApellido") ?
                null : estudiantesJsonArray.getJSONObject(i).getString("segundoApellido");
        String nombre = estudiantesJsonArray.getJSONObject(i).getString("nombre");

        String item = primerApellido;
        if (segundoApellido != null && !segundoApellido.equals(""))
            item += " " + segundoApellido;
        item += ", " + nombre;

        return item;
    }

    public HashMap<String, String> getHashMap(String jsonString) throws JSONException {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        JSONObject jsonObject = new JSONObject(jsonString);
        for (int i = 0; i < jsonObject.names().length(); i++) {
            String key = jsonObject.names().getString(i);
            hashMap.put(key, jsonObject.get(key).toString());
        }

        return hashMap;
    }

    public String estudianteBasicInfo(HashMap<String, String> hashMap) {
        String s = hashMap.get("primerApellido");
        String segundoApellido = hashMap.get("segundoApellido");
        if (segundoApellido != null && !segundoApellido.isEmpty())
            s += " " + segundoApellido;
        s += ", " + hashMap.get("nombre");

        return s;
    }
}
