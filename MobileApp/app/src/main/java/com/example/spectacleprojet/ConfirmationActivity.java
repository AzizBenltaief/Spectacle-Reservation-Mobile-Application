package com.example.spectacleprojet;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;

public class ConfirmationActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CardView cardSuccess;
    private LottieAnimationView lottieAnimation;
    private Button btnDone;
    private TextView reservationDetailsTextView;
    private boolean animationCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        progressBar = findViewById(R.id.progressBar);
        cardSuccess = findViewById(R.id.cardSuccess);
        lottieAnimation = findViewById(R.id.lottieAnimation);
        btnDone = findViewById(R.id.btnDone);
        reservationDetailsTextView = findViewById(R.id.reservation_details);

        Intent intent = getIntent();
        String spectacleTitre = intent.getStringExtra("spectacleTitre");
        String date = intent.getStringExtra("date");
        String heure = intent.getStringExtra("heure");
        int numberOfPlaces = intent.getIntExtra("numberOfPlaces", 0);
        String categorieBillet = intent.getStringExtra("categorieBillet");
        float prix = intent.getFloatExtra("prix", 0.0f);
        int billetId = intent.getIntExtra("billetId", -1);

        String details = "Spectacle : " + (spectacleTitre != null ? spectacleTitre : "N/A") + "\n" +
                "Date et heure : " + (date != null ? date : "N/A") + " " + (heure != null ? heure : "") + "\n" +
                "Nombre de places : " + numberOfPlaces + "\n" +
                "Catégorie de billet : " + (categorieBillet != null ? categorieBillet : "N/A") + "\n" +
                "Prix total : " + String.format("%.2f", prix) + " TND\n" +
                "ID du billet : " + (billetId != -1 ? billetId : "N/A") + "\n\n" +
                "Un email de confirmation a été envoyé à votre adresse email.";
        reservationDetailsTextView.setText(details);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            cardSuccess.setVisibility(View.VISIBLE);
            lottieAnimation.playAnimation();

            lottieAnimation.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animationCompleted = true;
                    new Handler().postDelayed(() -> {
                        navigateToDetails();
                    }, 1000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    navigateToDetails();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }, 3000);

        btnDone.setOnClickListener(v -> {
            if (!animationCompleted) {
                lottieAnimation.cancelAnimation();
            }
            navigateToDetails();
        });
    }

    private void navigateToDetails() {
        Intent intent = new Intent(ConfirmationActivity.this, SpectacleDetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}