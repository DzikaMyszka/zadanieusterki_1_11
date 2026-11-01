package com.example.zgloszenieusterki;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etOpis;
    private Spinner spKategoria;
    private RadioGroup rgPriorytet;
    private CheckBox cbKontakt;
    private Button btnWyslij;
    private TextView tvPodsumowanie;
    private Button btnWyczysc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etOpis = findViewById(R.id.etOpis);
        spKategoria = findViewById(R.id.spKategoria);
        rgPriorytet = findViewById(R.id.rgPriorytet);
        cbKontakt = findViewById(R.id.cbKontakt);
        btnWyslij = findViewById(R.id.btnWyslij);
        tvPodsumowanie = findViewById(R.id.tvPodsumowanie);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.kategorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategoria.setAdapter(adapter);

        cbKontakt.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnWyslij.setEnabled(isChecked));

        btnWyslij.setOnClickListener(v -> {
            if (validateForm()) {
                pokazPotwierdzenie();
            }
        });


        btnWyczysc.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Wyczyścić formularz?")
                    .setMessage("Czy na pewno chcesz wyczyścić wszystkie pola?")
                    .setPositiveButton("Tak", (dialog, which) -> wyczyscForm())
                    .setNegativeButton("Nie", null)
                    .show();
        });
    }

    private boolean validateForm() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Podaj imię i nazwisko");
            etName.requestFocus();
            return false;
        }

        if (spKategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Wybierz kategorię", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rgPriorytet.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Wybierz priorytet", Toast.LENGTH_SHORT).show();
            return false;
        }

        String opis = etOpis.getText().toString().trim();
        if (opis.isEmpty()) {
            etOpis.setError("Opisz usterkę");
            etOpis.requestFocus();
            return false;
        }

        return true;
    }

    private void pokazPotwierdzenie() {
        new AlertDialog.Builder(this)
                .setTitle("Wysłać zgłoszenie?")
                .setMessage("Czy na pewno chcesz wysłać to zgłoszenie?")
                .setPositiveButton("Tak", (dialog, which) -> wyslij())
                .setNegativeButton("Nie", null)
                .show();
    }

    private void wyslij() {
        String name = etName.getText().toString().trim();
        String kategoria = spKategoria.getSelectedItem().toString();
        String priorytet = "brak";
        int id = rgPriorytet.getCheckedRadioButtonId();
        if (id == R.id.rbNiski) priorytet = "Niski";
        else if (id == R.id.rbSredni) priorytet = "Średni";
        else if (id == R.id.rbWysoki) priorytet = "Wysoki";

        String opis = etOpis.getText().toString().trim();

        String text = name + "\n" + kategoria + "\n" + priorytet + "\n" + opis;
        tvPodsumowanie.setText(text);

        Snackbar.make(findViewById(R.id.main), "Zgłoszenie wysłane", Snackbar.LENGTH_LONG)
                .setAction("Cofnij", v -> tvPodsumowanie.setText(""))
                .show();
    }
    private void wyczyscForm() {
        final String staryName = etName.getText().toString();
        final String staryOpis = etOpis.getText().toString();
        final int staraKategoria = spKategoria.getSelectedItemPosition();
        final int staryPriorytet = rgPriorytet.getCheckedRadioButtonId();
        final boolean staryKontakt = cbKontakt.isChecked();
        final String starePodsumowanie = tvPodsumowanie.getText().toString();

        etName.setText("");
        etName.setError(null);
        etOpis.setText("");
        etOpis.setError(null);
        spKategoria.setSelection(0);
        rgPriorytet.clearCheck();
        cbKontakt.setChecked(false);
        tvPodsumowanie.setText("");

        Snackbar.make(findViewById(R.id.main), "Formularz wyczyszczony", Snackbar.LENGTH_LONG)
                .setAction("Cofnij", v -> {
                    etName.setText(staryName);
                    etOpis.setText(staryOpis);
                    spKategoria.setSelection(staraKategoria);
                    if (staryPriorytet != -1) {
                        rgPriorytet.check(staryPriorytet);
                    }
                    cbKontakt.setChecked(staryKontakt);
                    tvPodsumowanie.setText(starePodsumowanie);
                })
                .show();
    }
}