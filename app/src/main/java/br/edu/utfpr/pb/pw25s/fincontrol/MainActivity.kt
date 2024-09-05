package br.edu.utfpr.pb.pw25s.fincontrol

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler
import br.edu.utfpr.pb.pw25s.fincontrol.entity.Logbook

class MainActivity : AppCompatActivity() {

    private lateinit var spEntryType: Spinner
    private lateinit var spDescriptions: Spinner
    private lateinit var etDate: EditText
    private lateinit var etValue: EditText

    private lateinit var db :DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spEntryType = findViewById(R.id.spEntryType)
        spDescriptions = findViewById(R.id.spDescriptions)
        etDate = findViewById(R.id.etDate)
        etValue = findViewById(R.id.etValue)

        db = DatabaseHandler(this)

        if( intent.getIntExtra("cod", 0) != 0 ) {
            spEntryType.setSelection(intent.getIntExtra("entryType", 0))
            spDescriptions.setSelection(intent.getIntExtra("description", 0))
            etDate.setText(intent.getStringExtra("date"))
            etValue.setText(intent.getDoubleExtra("value", 0.0).toString())
        }

        spEntryType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if( spEntryType.selectedItem.toString() == "Receita" ) {
                    spDescriptions.adapter = ArrayAdapter.createFromResource(this@MainActivity, R.array.descriptions_income, android.R.layout.simple_list_item_1)
                } else {
                    spDescriptions.adapter = ArrayAdapter.createFromResource(this@MainActivity, R.array.descriptions_outcome, android.R.layout.simple_list_item_1)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // write code to perform some action
            }
        }
    }

    fun btSaveOnClick(view: View) {
        val entryType = spEntryType.selectedItem.toString()
        val description = spDescriptions.selectedItem.toString()
        val date = etDate.text.toString()
        val value = etValue.text.toString().toDouble()

        if( intent.getIntExtra("cod", 0) != 0 ) {
            db.update(
                Logbook(
                    intent.getIntExtra("cod", 0),
                    entryType,
                    description,
                    value,
                    date
                )
            )
        } else {
            db.insert(
                Logbook(
                    null,
                    entryType,
                    description,
                    value,
                    date
                )
            )
        }

        finish()
    }

    fun ibCalendarOnClick(view: View) {
        val date = etDate.text.toString().split("/")
        val day = date[0].toInt()
        val month = date[1].toInt() - 1
        val year = date[2].toInt()

        val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
            etDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
        }, year, month, day)
        dpd.show()
    }

    fun btCancelOnClick(view: View) {
        finish()
    }


}