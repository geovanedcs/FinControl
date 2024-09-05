package br.edu.utfpr.pb.pw25s.fincontrol

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.pb.pw25s.fincontrol.adapter.LogElementAdapter
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            TODO("Not yet implemented")
        }

        db = DatabaseHandler(this)
    }

    override fun onStart() {
        super.onStart()
        val entries = db.listCursor()
        val adapter = LogElementAdapter(this, entries)
        lvEntry.adapter = adapter
    }


}