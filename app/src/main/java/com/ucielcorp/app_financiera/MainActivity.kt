package com.ucielcorp.app_financiera

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlin.collections.ArrayList
import android.R.layout.simple_list_item_1
import android.app.Activity
import android.view.View
import android.widget.*


class MainActivity : ListActivity() {

    private var adapter: ArrayAdapter<String>? = null
    private var ArrMonedas: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**Conectando la base de datos**/

        val db = CurrencyAdapter(this)
        db.abrir()

        val allRows = db.todos()

        allRows.moveToFirst()

        ArrMonedas = ArrayList()

        for (i in 0 until allRows.count){
            ArrMonedas!!.add(allRows.getString(allRows.getColumnIndex("NOMBRE_MONEDA")))
            allRows.moveToNext()
        }

        adapter = ArrayAdapter(this, simple_list_item_1, ArrMonedas!!)

        listAdapter = adapter
    }

    /*Creando el menu*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var infrate = menuInflater
        infrate.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /*Agregando funcionalidad a nuestra app*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val intent = Intent(this, AgregarMoneda::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    /*Valores ingresado por la primera pantalla*/
    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {

        //Valor a convertir
        val valorAconvertir = findViewById<EditText>(R.id.vConvent)

        //El valor que va a devolver
        val resultado = findViewById<TextView>(R.id.vResultado)

        resultado.text = ""

        if (valorAconvertir.text.toString().isNotEmpty()) {

            val db = CurrencyAdapter(this)
            db.abrir()

            //Traigo el valor de la moneda
            val valor_cambio = db.valorMonedaPorNombre(l.getItemAtPosition(position).toString())

            //Calculo de la conversion
            resultado.text = (java.lang.Float.valueOf(valorAconvertir.text.toString()) * valor_cambio!!).toString()
        } else {

            Toast.makeText(this, resources.getString(R.string.lblAlert1), Toast.LENGTH_LONG).show()
        }

        super.onListItemClick(l, v, position, id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == 90210 && resultCode == Activity.RESULT_OK) {

            Toast.makeText(this, data.getStringExtra("NuevaMoneda") + " Agregada!", Toast.LENGTH_LONG).show()

            ArrMonedas!!.add(data.getStringExtra("NuevaMoneda").toString())
            adapter!!.notifyDataSetChanged()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}