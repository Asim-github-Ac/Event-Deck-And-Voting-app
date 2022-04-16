package in.macrocodes.onlineauctionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button mRegBtn;
    private Button mLoginBtn;
    Spinner spinner;
    String usertype;
    String[] users={"Admin","Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegBtn = (Button) findViewById(R.id.register);
        mLoginBtn = (Button) findViewById(R.id.login);
        spinner=findViewById(R.id.simpleSpinner);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,users);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg_intent = new Intent(MainActivity.this, RegisterActivity.class);
                reg_intent.putExtra("user",usertype);
                startActivity(reg_intent);

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login_intent = new Intent(MainActivity.this, LoginActivity.class);
                login_intent.putExtra("user",usertype);
                startActivity(login_intent);

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         usertype= users[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}