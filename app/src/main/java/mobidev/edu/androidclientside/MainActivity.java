package mobidev.edu.androidclientside;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity {

    EditText etFirstName;
    EditText etLastName;
    EditText etIdNumber;

    TextView tvResults;

    Button buttonAddStudent;
    Button buttonViewStudent;
    Button buttonViewStudents;

    String your_IP_address = "192.168.0.171:8080"; /* Enter your IP address : port */
    String your_web_app = "AndroidServerSide"; /* Replace this with your own web app name */
    String baseUrl = "http://" + your_IP_address + "/" + your_web_app + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFirstName = (EditText) findViewById(R.id.et_firstname);
        etLastName = (EditText) findViewById(R.id.et_lastname);
        etIdNumber = (EditText) findViewById(R.id.et_idnumber);

        tvResults = (TextView) findViewById(R.id.tv_results);

        buttonAddStudent = (Button) findViewById(R.id.button_add);
        buttonViewStudent = (Button) findViewById(R.id.button_view);
        buttonViewStudents = (Button) findViewById(R.id.button_list);

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddStudentUrlHelper().execute();
            }
        });

        buttonViewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewStudentUrlHelper().execute();
            }
        });

        buttonViewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewStudentListUrlHelper().execute();
            }
        });
    }

    private class ViewStudentUrlHelper extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            // this request doesn't need to send any data to the server
            String url = baseUrl + "GetStudent";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;
            String studentResponse = "";

            try {
                // we want to geta student object in response
                response = client.newCall(request).execute();

                // a response object contains header data and the actual body content
                studentResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // the doInBackground return value will be the parameter to onPostExecute
            return studentResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonStudentObject = new JSONObject(s);
                tvResults.setText("First Name : " + jsonStudentObject.getString("firstName")
                         + "\n" + "Last Name : " + jsonStudentObject.getString("lastName")
                         + "\n" + "ID Number : " + jsonStudentObject.getInt("idNumber"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ViewStudentListUrlHelper extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            // this request doesn't need to send any data to the server

            Request request = new Request.Builder()
                    .url(baseUrl + "GetStudentList")
                    .build();

            Response response = null;
            String studentListResponse = "";

            try {
                // we want to get a list of students in response
                response = client.newCall(request).execute();

                // a response object contains header data and the actual body content
                studentListResponse = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // the doInBackground return value will be the parameter to onPostExecute
            return studentListResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonStudentList = new JSONArray(s);

                String textToDisplay = "";
                for(int i = 0; i < jsonStudentList.length(); i++) {
                    textToDisplay +=  "First Name : " + jsonStudentList.getJSONObject(i).getString("firstName")
                            + "\n" + "Last Name : " + jsonStudentList.getJSONObject(i).getString("lastName")
                            + "\n" + "ID Number : " + jsonStudentList.getJSONObject(i).getInt("idNumber")
                            + "\n\n";
                }

                tvResults.setText(textToDisplay);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddStudentUrlHelper extends AsyncTask<String, Void, String>{

        Student currentStudent = new Student();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            currentStudent.setFirstName(etFirstName.getText().toString());
            currentStudent.setLastName(etLastName.getText().toString());
            currentStudent.setIdNumber(Integer.parseInt(etIdNumber.getText().toString()));
        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            // this request needs to send data from client to server
            RequestBody postRequestBody = new FormEncodingBuilder()
                    .add("firstName", currentStudent.getFirstName())
                    .add("lastName", currentStudent.getLastName())
                    .add("idNumber", currentStudent.getIdNumber() + "")
                    .build();

            Request request = new Request.Builder()
                    .url(baseUrl + "AddStudent")
                    .post(postRequestBody)
                    .build();

            try {
                client.newCall(request).execute();
                // In this case, the client is not waiting for a response back
                // but it's better to check if the request was handled correctly or not
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }


}
