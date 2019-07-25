package app.android.joshjosh.duckhuntretro.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import app.android.joshjosh.duckhuntretro.R;
import app.android.joshjosh.duckhuntretro.common.Constantes;
import app.android.joshjosh.duckhuntretro.models.User;

public class LoginActivity extends AppCompatActivity {

    EditText etNick;
    Button btnStart,btnRanking;
    String nick;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        etNick = findViewById(R.id.editTextNick);
        btnStart = findViewById(R.id.buttonStart);
        btnRanking = findViewById(R.id.buttonRanking);

        changeStyle();
        eventListener();



    }
    private void changeStyle(){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);
        btnRanking.setTypeface(typeface);
    }

    private void eventListener(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = etNick.getText().toString();

                if(nick.isEmpty()){
                   etNick.setError("Digite un usuario");
                }else if(nick.length() < 3){
                    etNick.setError("Digite un usuario con mas de 3 caracteres");
                }
                else if(nick.length() > 20){
                    etNick.setError("Nombre muy largo");
                }
                else{
                    addNickAndStart();
                }

            }
        });

        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intento = new Intent(LoginActivity.this,RankingActivity.class);
                startActivity(intento);
            }
        });
    }

    public void addNickAndStart(){
        db.collection("users").whereEqualTo("nick",nick)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() > 0){
                            etNick.setError("El nick ya está en uso");
                        }else{
                            addNickToFireStore();
                        }
                    }
                });


    }
    private void addNickToFireStore(){
        User newUser = new User(nick,0);
        db.collection("users")
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        etNick.setText("");
                        Intent intent = new Intent(LoginActivity.this,GameActivity.class);
                        intent.putExtra(Constantes.EXTRA_NICK,nick);
                        intent.putExtra(Constantes.EXTRA_ID,documentReference.getId());
                        startActivity(intent);
                    }
                });


    }
}
