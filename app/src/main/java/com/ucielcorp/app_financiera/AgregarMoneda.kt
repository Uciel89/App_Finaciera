package com.ucielcorp.app_financiera

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class AgregarMoneda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_moneda)

    }

    fun CrearLugar (v: View){

        //Llamando a los editTexs
        val nueva_moneda: EditText = findViewById(R.id.vCurrencyName)
        val moneda_valor: EditText = findViewById(R.id.vCurrencyValue)

        //Agregamos otra variable que resperesenta el nuevo texto en la base de datos
        val txtNuevaMoneda = nueva_moneda.text.toString()
        val txtMonedaValor = moneda_valor.text.toString()

        //Verificamos que aya algun valor en las dos variables anteriores
        if (txtNuevaMoneda.isNotEmpty() || txtMonedaValor.isNotEmpty()){

            //Instanciamos la base de datos en esta funcion
            val db = CurrencyAdapter(this)
            db.abrir()

            //Insertamos los nuevos valores a la tabla de datos de la base de datos
            db.insertar(txtNuevaMoneda, java.lang.Float.valueOf(txtMonedaValor))

            //Extraemos los datos, para visualizarlos
            val datos = Intent()
            datos.putExtra("NuevaMoneda", txtNuevaMoneda)

            setResult(Activity.RESULT_OK, datos)
            finish()
        } else {

            Toast.makeText(this, resources.getString(R.string.lblAlert2), Toast.LENGTH_LONG).show()

        }
    }
}