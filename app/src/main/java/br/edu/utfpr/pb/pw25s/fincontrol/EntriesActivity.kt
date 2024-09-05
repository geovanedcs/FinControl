package br.edu.utfpr.pb.pw25s.fincontrol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.pb.pw25s.fincontrol.adapter.LogElementAdapter
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.util.Locale

class EntriesActivity : AppCompatActivity() {

    private lateinit var lvEntry: ListView
    private lateinit var btAddEntry: FloatingActionButton
    private lateinit var fabShowBalance: FloatingActionButton

    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        lvEntry = findViewById(R.id.lvEntries)
        btAddEntry = findViewById(R.id.btAddEntry)
        fabShowBalance = findViewById(R.id.fabShowBalance)

        btAddEntry.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fabShowBalance.setOnClickListener {
            var income = 0.0
            var expenses = 0.0
            db.list().forEach {
                if (it.type == "Receita") {
                    income += it.value
                } else {
                    expenses += it.value
                }
            }
            val balance = income - expenses

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Saldo")
            builder.setMessage("Receitas: ${formatToBRL(income)}\nDespesas: ${formatToBRL(expenses)}" +
                    "\nSaldo: ${formatToBRL(balance)}")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        db = DatabaseHandler(this)
    }

    fun formatToBRL (value: Double): String {
        val locale = Locale("pt", "BR")
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        return currencyFormat.format(value)
    }

    override fun onStart() {
        super.onStart()
        val entries = db.listCursor()
        val adapter = LogElementAdapter(this, entries)
        lvEntry.adapter = adapter
    }


}