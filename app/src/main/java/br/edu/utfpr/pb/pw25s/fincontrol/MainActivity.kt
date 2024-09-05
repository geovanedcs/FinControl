package br.edu.utfpr.pb.pw25s.fincontrol

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler
import br.edu.utfpr.pb.pw25s.fincontrol.entity.Logbook
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var spEntryType: Spinner
    private lateinit var spDescriptions: Spinner
    private lateinit var etDate: EditText
    private lateinit var etValue: EditText
    private lateinit var fabDelete: FloatingActionButton

    private lateinit var db :DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spEntryType = findViewById(R.id.spEntryType)
        spDescriptions = findViewById(R.id.spDescriptions)
        etDate = findViewById(R.id.etDate)
        etValue = findViewById(R.id.etValue)
        fabDelete = findViewById(R.id.fabDelete)

        db = DatabaseHandler(this)

        fabDelete.visibility = if( intent.getIntExtra("cod", 0) != 0 ) View.VISIBLE else View.GONE

        if( intent.getIntExtra("cod", 0) != 0 ) {
            spEntryType.setSelection(intent.getIntExtra("type", 0))
            spDescriptions.setSelection(intent.getIntExtra("description", 0))
            etDate.setText(intent.getStringExtra("date"))
            etValue.setText(intent.getDoubleExtra("value", 0.0).toString())
        }

        spEntryType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if( spEntryType.selectedItem.toString() == "Receita" ) {
                    spDescriptions.adapter = ArrayAdapter.createFromResource(this@MainActivity, R.array.descriptions_income, android.R.layout.simple_list_item_1)
                } else {
                    spDescriptions.adapter = ArrayAdapter.createFromResource(this@MainActivity, R.array.descriptions_expenses, android.R.layout.simple_list_item_1)
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
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            etDate.setText("$dayOfMonth/${monthOfYear+1}/$year")
        }, year, month, day)

        dpd.show()
    }

    fun btCancelOnClick(view: View) {
        finish()
    }

    fun fabDeleteOnClick(view: View) {
        db.delete(intent.getIntExtra("cod", 0))
        finish()
    }


}