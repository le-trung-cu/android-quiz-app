package funix.prm.prm391x_quizapp_cultfx02223funixeduvn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText tvUserName = findViewById(R.id.et_user_name);
        Button logInBtn = findViewById(R.id.btn_log_in);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = tvUserName.getText().toString();

                if(userName.isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.toast_user_name,
                            Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, HomeworkActivity.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
