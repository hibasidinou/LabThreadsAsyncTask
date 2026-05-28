package com.example.labthreadsasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Références UI
    private TextView statusLabel;
    private ProgressBar loadingBar;
    private ImageView imageDisplay;

    // Handler pour repasser sur le thread principal
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        statusLabel   = findViewById(R.id.txtStatus);
        loadingBar    = findViewById(R.id.progressBar);
        imageDisplay  = findViewById(R.id.img);

        Button btnThread = findViewById(R.id.btnLoadThread);
        Button btnAsync  = findViewById(R.id.btnCalcAsync);
        Button btnToast  = findViewById(R.id.btnToast);

        // Handler lié au thread UI
        uiHandler = new Handler(Looper.getMainLooper());

        // --- Bouton Toast : réponse immédiate garantie ---
        btnToast.setOnClickListener(v ->
                Toast.makeText(this, "✅ UI toujours réactive !", Toast.LENGTH_SHORT).show()
        );

        // --- Bouton Thread : chargement image en arrière-plan ---
        btnThread.setOnClickListener(v -> demarrerChargementImage());

        // --- Bouton AsyncTask : calcul intensif avec progression ---
        btnAsync.setOnClickListener(v -> new TacheCalculIntensif().execute());
    }

    // =========================================================
    //  PARTIE 1 — Thread manuel
    // =========================================================
    private void demarrerChargementImage() {

        // Affichage initial (sur UI thread)
        loadingBar.setVisibility(View.VISIBLE);
        loadingBar.setProgress(0);
        statusLabel.setText("⏳ Chargement en cours (Thread)...");

        new Thread(() -> {

            // Simulation du délai réseau
            try {
                Thread.sleep(1200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            // Décodage de l'image depuis les ressources
            // ic_launcher_background est un vrai drawable (contrairement à ic_launcher
            // qui est un adaptive icon non décodable en Bitmap sur Android 8+)
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.monimage);

            // Mise à jour de l'UI → obligatoirement via Handler
            uiHandler.post(() -> {
                if (bmp != null) {
                    imageDisplay.setImageBitmap(bmp);
                    statusLabel.setText("✅ Image chargée avec succès (Thread)");
                } else {
                    // Fallback : afficher l'icône directement via setImageResource
                    imageDisplay.setImageResource(R.mipmap.ic_launcher);
                    statusLabel.setText("✅ Image affichée (fallback)");
                }
                loadingBar.setVisibility(View.INVISIBLE);
            });

        }).start();
    }

    // =========================================================
    //  PARTIE 2 — AsyncTask
    // =========================================================
    private class TacheCalculIntensif extends AsyncTask<Void, Integer, Long> {

        // Étape 1 : préparation de l'UI avant le calcul
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            loadingBar.setProgress(0);
            statusLabel.setText("⚙️ Calcul intensif lancé (AsyncTask)...");
        }

        // Étape 2 : calcul lourd sur le worker thread
        @Override
        protected Long doInBackground(Void... params) {
            long total = 0;

            for (int step = 1; step <= 100; step++) {
                // Boucle interne simulant un traitement lourd
                for (int j = 0; j < 200000; j++) {
                    total += (step * j) % 7;
                }
                // Signaler la progression à onProgressUpdate
                publishProgress(step);
            }

            return total;
        }

        // Étape 3 : mise à jour de la ProgressBar
        @Override
        protected void onProgressUpdate(Integer... progress) {
            loadingBar.setProgress(progress[0]);
        }

        // Étape 4 : affichage du résultat final
        @Override
        protected void onPostExecute(Long finalResult) {
            loadingBar.setVisibility(View.INVISIBLE);
            statusLabel.setText("🎉 Calcul terminé ! Résultat = " + finalResult);
        }
    }
}