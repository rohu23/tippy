package com.rtee.tippy

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : ComponentActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        tvTipDescription = findViewById(R.id.tvTipDescription)
        updateTipDescription(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChange $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14->"Acceptable"
            in 15..19->"Good"
            in 20..24-> "Great"
            else->"Amazing"
        }
        tvTipDescription.text= tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/ seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
                    ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()){
            tvTipAmount.text=""
            tvTotalAmount.text=""
            return
        }
        //1.Get values from base amount and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        //2.Calculate the tip and total
        val tipAmount= baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        //3.Update UI
        tvTipAmount.text = "%.2f".format( tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)

    }
}

