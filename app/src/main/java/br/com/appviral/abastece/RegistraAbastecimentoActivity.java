package br.com.appviral.abastece;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.appviral.abastece.Entidade.Abastecimento;
import br.com.appviral.abastece.Persistencia.AbastecimentoDAO;

public class RegistraAbastecimentoActivity extends AppCompatActivity implements OnFocusChangeListener {
    String operacao;
    Calendar data;
    EditText etQtde_litros, etVlr_litro, etVlr_tota, etData;
    RadioButton rbGasolina, rbAlcool, rbDiesel;
    SimpleDateFormat sdf;
    Abastecimento umAbastecimento = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraabastecimento);
        operacao = getIntent().getStringExtra("OPERACAO");
        data = Calendar.getInstance();

        etQtde_litros = (EditText) findViewById(R.id.etQtde_litros);
        etVlr_litro = (EditText) findViewById(R.id.etVlr_litros);
        etVlr_tota = (EditText) findViewById(R.id.etVlr_total);
        etData = (EditText) findViewById(R.id.etData);
        rbGasolina = (RadioButton) findViewById(R.id.rbGasolina);
        rbAlcool = (RadioButton) findViewById(R.id.rbAlcool);
        rbDiesel = (RadioButton) findViewById(R.id.rbDiesel);

        etQtde_litros.setOnFocusChangeListener(this);
        etVlr_litro.setOnFocusChangeListener(this);
        etVlr_tota.setOnFocusChangeListener(this);

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        preparaUmAbastecimetno();
    }

    private void preparaUmAbastecimetno(){
        etVlr_litro.setText("");
        etVlr_tota.setText("");
        etQtde_litros.setText("");
        etData.setText(sdf.format(data.getTime()));
        etQtde_litros.requestFocus();
        umAbastecimento = new Abastecimento();
        umAbastecimento.combustivel = Abastecimento.tipo_combustivel.gasolina;
        umAbastecimento.data = etData.getText().toString();
    }

    public void onRadioButtonClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rbGasolina:
                umAbastecimento.combustivel = Abastecimento.tipo_combustivel.gasolina;
                break;
            case R.id.rbAlcool:
                umAbastecimento.combustivel = Abastecimento.tipo_combustivel.alcool;
                break;
            case R.id.rbDiesel:
                umAbastecimento.combustivel = Abastecimento.tipo_combustivel.diesel;
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            umAbastecimento.calculaTerceiro(
                    etQtde_litros.getText().toString(),
                    etVlr_litro.getText().toString(),
                    etVlr_tota.getText().toString());
            if (umAbastecimento.isCalculou) {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(2);
                etQtde_litros.setText(nf.format(umAbastecimento.qtde_litros));
                etVlr_litro.setText(nf.format(umAbastecimento.vlr_litro));
                etVlr_tota.setText(nf.format(umAbastecimento.vlr_total));
            }
        }
    }

    public void pegaData(View view) {
        DatePickerDialog toDatePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        data.set(year, monthOfYear, dayOfMonth);
                        etData.setText(sdf.format(data.getTime()));
                        umAbastecimento.data = etData.getText().toString();
                    }
                },
                data.get(Calendar.YEAR),
                data.get(Calendar.MONTH),
                data.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();

    }

    public void salva(View view) {
        AbastecimentoDAO abastecimentoDAO = new AbastecimentoDAO(this);
        switch (operacao) {
            case "inserir":
                if (abastecimentoDAO.inserir(umAbastecimento) > 0) {
                    Toast.makeText(this,"Salvo!!!", Toast.LENGTH_SHORT).show();
                    preparaUmAbastecimetno();
                }
                break;
            case "alterar":
                break;
        }
    }


}