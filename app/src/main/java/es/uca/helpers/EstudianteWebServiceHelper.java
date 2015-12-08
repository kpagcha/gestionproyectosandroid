package es.uca.helpers;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class EstudianteWebServiceHelper {
    private String host = "10.0.2.2";
    //private String host = "192.168.1.33";
    private String puerto = "8080";
    private String proyecto = "GestionProyectos";
    private String rutaServicio = "api/estudiantes";
    private String rutaMetodo;
    private String ruta;

    private void buildRuta() {
        if (rutaMetodo.indexOf("/") == 0)
            rutaMetodo = rutaMetodo.substring(1);
        ruta = "http://" + host + ":" + puerto + "/" + proyecto + "/" + rutaServicio + "/" + rutaMetodo;
    }

    public EstudianteWebServiceHelper(String rutaMetodo) {
        this.rutaMetodo = rutaMetodo;
        buildRuta();
    }

    public EstudianteWebServiceHelper(String rutaMetodo, String rutaParams)
        throws UnsupportedEncodingException {

        this.rutaMetodo = rutaMetodo + "/" + URLEncoder.encode(rutaParams, "UTF-8").replace("+", "%20");
        buildRuta();
    }

    public String performGetRequest() {
        StringBuffer sb = new StringBuffer();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(ruta);
            connection = (HttpURLConnection)url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());

            BufferedReader rd = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            if (connection != null) connection.disconnect();
        }
        String result = sb.toString();
        if (result.equals(""))
            return null;
        return result;
    }

    public String performPostRequest(JSONObject params) {
        StringBuffer sb = new StringBuffer();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(ruta);

            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");

            OutputStream os = connection.getOutputStream();
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            wr.write(params.toString());

            wr.flush();
            wr.close();
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            if (connection != null) connection.disconnect();
        }
        String result = sb.toString();
        if (result.equals(""))
            return null;
        return result;
    }

    public String performDeleteRequest() {
        StringBuffer sb = new StringBuffer();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(ruta);

            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            if (connection != null) connection.disconnect();
        }
        String result = sb.toString();
        if (result.equals(""))
            return null;
        return result;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        buildRuta();
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
        buildRuta();
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
        buildRuta();
    }

    public String getRutaServicio() {
        return rutaServicio;
    }

    public void setRutaServicio(String rutaServicio) {
        this.rutaServicio = rutaServicio;
        buildRuta();
    }

    public String getRutaMetodo() {
        return rutaMetodo;
    }

    public void setRutaMetodo(String rutaMetodo) {
        this.rutaMetodo = rutaMetodo;
        buildRuta();
    }

    public String getRuta() {
        return ruta;
    }
}
