package cz.tatarin.android.ds18b20calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private RadioButton rab9Bit;
    private RadioButton rab12Bit;

    private EditText etxHighByte;
    private EditText etxLowByte;

    private TextView txvResult;

    private enum UNIT{
        DEGREES_OF_CELSIUS,
        DEGREES_OF_FAHRENHEITS
    };

    private enum SIGN{
        PLUS,
        MINUS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rab9Bit = findViewById(R.id.activity_main_rab_9bit);
        rab12Bit = findViewById(R.id.activity_main_rab_12bit);

        etxHighByte = findViewById(R.id.activity_main_etx_high_byte);
        etxLowByte = findViewById(R.id.activity_main_etx_low_byte);
        etxHighByte.setText("2");
        etxLowByte.setText("105");

        /*txvResult.setText(String.valueOf(computeTemperature(
                Integer.parseInt(etxHighByte.getText().toString()),
                Integer.parseInt(etxLowByte.getText().toString()),
                UNIT.DEGREES_OF_CELSIUS)));
*/
        etxHighByte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txvResult.setText(String.valueOf(computeTemperature(
                        Integer.parseInt(etxHighByte.getText().toString()),
                        Integer.parseInt(etxLowByte.getText().toString()),
                        UNIT.DEGREES_OF_CELSIUS)));
            }
        });

        etxLowByte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txvResult.setText(String.valueOf(computeTemperature(
                        Integer.parseInt(etxHighByte.getText().toString()),
                        Integer.parseInt(etxLowByte.getText().toString()),
                        UNIT.DEGREES_OF_CELSIUS)));
            }
        });

        txvResult = findViewById(R.id.activity_main_txv_result);
        txvResult.setText(String.valueOf(computeTemperature(60,98,UNIT.DEGREES_OF_CELSIUS)));
    }

    private float computeTemperature(int highByte, int lowByte, UNIT unit){

        float computedTemperature = 0;
        SIGN sign;
        if(BigInteger.valueOf(highByte).testBit(3)){
            sign = SIGN.MINUS;
        } else {
            sign = SIGN.PLUS;
        }

        for (int bit = 2; bit >= 0; bit--){
            if(BigInteger.valueOf(highByte).testBit(bit)){
                if(sign == SIGN.PLUS){
                    computedTemperature += Math.pow(2, 4 + bit);
                } else {
                    computedTemperature -= Math.pow(2, 4 + bit);
                }
            }
        }

        for (int bit = 7; bit >= 0; bit--){
            if(BigInteger.valueOf(lowByte).testBit(bit)){
                if(sign == SIGN.PLUS){
                    computedTemperature += Math.pow(2, -4 + bit);
                } else {
                    computedTemperature -= Math.pow(2, -4 + bit);
                }
            }
        }

        if (unit == UNIT.DEGREES_OF_CELSIUS){
            return computedTemperature;
        } else {
            return computedTemperature * 1.8f + 32;
        }
    }
}