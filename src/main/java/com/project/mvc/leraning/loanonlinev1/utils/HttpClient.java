package com.project.mvc.leraning.loanonlinev1.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpClient {
    /*
    public static String getdata(String Url, String SecU, String SecP) throws Exception {
        String url = Url;

        //url="http://localhost:26226/api/openacct";
        // url="https://localhost:7064/api";

        URL obj = new URL(url);

        //===============================
        //System.out.print("\n Step trust side!");

        TrustCert trc = new TrustCert();
        trc.trustAllCertificates();

        //System.out.print("\n Open url!");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //System.out.print("\n Add Head for GET!");
        // Setting basic post request
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);


       //DATE: 2023 CODE NEW UPDATE  FOR => convert to FromBase64String
        String username = SecU; // Replace SecU with your actual username variable
        String password = SecP;
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        con.setRequestMethod("GET");
        //con.setRequestProperty("Authorization", "Basic " + SecU + ":" + SecP);
        con.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        System.out.println("Secu: "+SecU + "SecP: "+SecP);
        //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        //con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        con.setRequestProperty("Connection", "Keep-Alive");


        int responseCode = con.getResponseCode();
        // BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        String aa = response.toString().replaceAll("^\"|\"$", "").replace("\\", "");
        String Mjson = aa.replace("[\"[", "[");
        String Myjson = Mjson.replace("]\"]", "]");
        //System.out.print("GET:"+Myjson);
        return Myjson;

    }
     */

    public static String getData(String Url, String SecU, String SecP, String arrayName) throws Exception {
        String url = Url;

        URL obj = new URL(url);

        TrustCert trc = new TrustCert();
        trc.trustAllCertificates();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);

        String username = SecU;
        String password = SecP;
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        System.out.println(encodedCredentials);

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Connection", "Keep-Alive");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        String jsonResponse = response.toString();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONArray jsonArray = jsonObject.getJSONArray(arrayName);

            return jsonArray.toString();
        } catch (JSONException e) {
            System.out.println("Error parsing JSON response: " + e.getMessage());
            return null;
        }
    }

    public String PostData(String Url, String postJsonData, String SecU, String SecP) throws Exception {
        String url = Url;
        URL obj = new URL(url);

        // Trust all certificates
        TrustCert trc = new TrustCert();
        trc.trustAllCertificates();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        byte[] postDataBytes = postJsonData.getBytes(StandardCharsets.UTF_8);

        String username = SecU;
        String password = SecP;
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());//convert to base64

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + encodedCredentials);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        con.setRequestProperty("Connection", "Keep-Alive");

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8);
        outputStreamWriter.write(postJsonData);
        outputStreamWriter.flush();

        int responseCode = con.getResponseCode();
        System.out.print("\n Response Code=");
        System.out.print(responseCode);

        System.out.print("\nSending 'POST' request to URL : " + url);
        System.out.print("\nPost Data     : " + postJsonData);
        System.out.print("\nResponse Code : " + responseCode + "\n");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        return response.toString();
    }
}