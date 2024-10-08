package br.edu.utfpr.pb.pw25s.fincontrol.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import br.edu.utfpr.pb.pw25s.fincontrol.MainActivity
import br.edu.utfpr.pb.pw25s.fincontrol.R
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler
import br.edu.utfpr.pb.pw25s.fincontrol.entity.Logbook
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler.Companion.ID
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler.Companion.TYPE
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler.Companion.DESCRIPTION
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler.Companion.DATE
import br.edu.utfpr.pb.pw25s.fincontrol.database.DatabaseHandler.Companion.VALUE
import java.text.NumberFormat
import java.util.Locale


class LogElementAdapter(val context: Context, val cursor: Cursor) : BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)

        return Logbook(
            cursor.getInt(ID),
            cursor.getString(TYPE),
            cursor.getString(DESCRIPTION),
            cursor.getDouble(VALUE),
            cursor.getString(DATE)
        )
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)

        return cursor.getInt(ID).toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.entry_element, null)

        val tvDescriptionView = view.findViewById<TextView>(R.id.tvDescriptionView)
        val tvValueView = view.findViewById<TextView>(R.id.tvValueView)
        val tvDateView = view.findViewById<TextView>(R.id.tvDateView)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val ibEdit = view.findViewById<ImageView>(R.id.ibEdit)

        cursor.moveToPosition(position)

        tvDescriptionView.setText((cursor.getString(DESCRIPTION)))
        tvValueView.setText(formatToBRL(cursor.getDouble(VALUE)))
        tvDateView.setText(cursor.getString(DATE))
        if(cursor.getString(TYPE) == "Receita"){
            val sgv = when (cursor.getString(DESCRIPTION)){
                "Doação/Presente" -> R.drawable.donation
                "Salário" -> R.drawable.salary
                else -> R.drawable.extras
            }
            imageView.setImageResource(sgv)
        } else {
            val sgv = when (cursor.getString(DESCRIPTION)){
                "Alimentação" -> R.drawable.food
                "Entretenimento" -> R.drawable.entertainment
                "Moradia" -> R.drawable.home
                "Saúde" -> R.drawable.health
                else -> R.drawable.transport
            }
            imageView.setImageResource(sgv)
        }

        imageView.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)

            cursor.moveToPosition(position)

            intent.putExtra("cod", cursor.getInt(ID))
            intent.putExtra("type", cursor.getString(TYPE))
            intent.putExtra("description", cursor.getString(DESCRIPTION))
            intent.putExtra("value", cursor.getDouble(VALUE))
            intent.putExtra("date", cursor.getString(DATE))
            context.startActivity(intent)
        }

        ibEdit.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)

            cursor.moveToPosition(position)

            intent.putExtra("cod", cursor.getInt(ID))
            intent.putExtra("type", if(cursor.getString(TYPE) == "Receita") 0 else 1)
            intent.putExtra("description", descriptionToIndex(cursor.getString(DESCRIPTION)))
            intent.putExtra("value", cursor.getDouble(VALUE))
            intent.putExtra("date", cursor.getString(DATE))
            context.startActivity(intent)
        }

        return view
    }

    fun formatToBRL (value: Double): String {
        val locale = Locale("pt", "BR")
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        return currencyFormat.format(value)
    }

    private fun descriptionToIndex(description: String): Int {
        if(cursor.getString(TYPE) == "Receita") {
            return when (description) {
                "Doação/Presente" -> 0
                "Extras" -> 1
                else -> 2
            }
        }else{
            return when (description) {
                "Alimentação" -> 0
                "Entretenimento" -> 1
                "Moradia" -> 2
                "Saúde" -> 3
                else -> 4
            }
        }

    }


}