package app.android.joshjosh.duckhuntretro.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

import app.android.joshjosh.duckhuntretro.R;
import app.android.joshjosh.duckhuntretro.common.Constantes;

public class GameActivity extends AppCompatActivity {
    //TODO
    TextView tvCounterDucks,tvTimer,tvNick;
    ImageView ivDuck;
    int counter = 0;
    int anchoPantalla;
    int altoPantalla;
    Random aleatorio;
    boolean gameOver = false;
    String id,nick;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        db = FirebaseFirestore.getInstance();
        initViewComponents();
        events();
        initPantalla();
        moveDuck();
        initCuentaAtras();


    }

    private void initCuentaAtras() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long sgsRestantes = millisUntilFinished/1000;
                tvTimer.setText(sgsRestantes + "s");
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                mostrarDialogoGameOver();
                saveResultFireStore();
            }
        }.start();


    }

    private void saveResultFireStore() {
        db.collection("users")
                .document(id)
                .update(
                        "ducks",counter
                );


    }


    private void mostrarDialogoGameOver(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Has conseguido cazar " + counter + " patos")
                .setTitle("Game Over");

        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                counter =0;
                tvCounterDucks.setText("0");
                gameOver = false;
                initCuentaAtras();
                moveDuck();

            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                Intent i = new Intent(GameActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        // 4. Mostrar el dialogo
        dialog.show();
    }

    private void initPantalla() {
        //1.Otener el tamañao de la pantalla del device
        //en el que estamos ejecutando la pp
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;

        //2. iniciamos el objeto para generar numeros aleatorios
        aleatorio = new Random();
    }

    private void initViewComponents(){
        tvCounterDucks = findViewById(R.id.textViewCounter);
        tvTimer = findViewById(R.id.textViewTimer);
        tvNick = findViewById(R.id.textViewNick);
        ivDuck = findViewById(R.id.imageViewDuck);

        changeStyle();

        //Recogemos el nickName

        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        id = extras.getString(Constantes.EXTRA_ID);
        tvNick.setText(nick);
    }
    private void changeStyle(){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"pixel.ttf");
        tvCounterDucks.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);


    }
    private void events(){
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    counter++;
                    tvCounterDucks.setText(String.valueOf(counter));
                    ivDuck.setImageResource(R.drawable.duckclicked);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivDuck.setImageResource(R.drawable.duck);
                            moveDuck();
                        }
                    }, 60);
                }

            }
        });

    }
    private void moveDuck(){
        int min = 0;
        int maximoX = anchoPantalla- ivDuck.getWidth();
        int maximoY = altoPantalla - ivDuck.getHeight();

        //Generamos dos numeros aleatorios, 1 para X y otro para Y
        int randomX = aleatorio.nextInt(((maximoX - min) + 1) + min);
        int randomY = aleatorio.nextInt(((maximoY - min) + 1) + min);

        //Use the random numbers to move the duck
        ivDuck.setX(randomX);
        ivDuck.setY(randomY);



    }
}
