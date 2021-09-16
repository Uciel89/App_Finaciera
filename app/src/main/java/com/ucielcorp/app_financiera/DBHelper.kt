package com.ucielcorp.app_financiera

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**En esta clase, basicamente generamos la base de datos, aca la modelamos por asi decirlo**/

/*El nombre DBHelper puede variar, pero lo que me indica principalmente, es que esta clase va a realizar
//un soporte.

SQLiteOpenHelper --> esta metodo requiere 3 valores:
_ Un contexto
_ Un nombre de la base de datos
_ Un valore generico
_ Y la version de la base de datos

Estos valores son parte del constructor*/

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

/*El metodo onCreate() es el encargado de generar la base de datos. Este mismo tiene una particularidad.
Puede generar la base de datos, pero si ya con aterioridad se genero, este metodo no se va a ejecutar ->
Por lo tanto esta puede Crear en si mismo la base datos o si no ya esta creada para a un modo de
verificación*/

override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
    sqLiteDatabase.execSQL(

        //TABLE_NAME --> el nombre de tabla propiamente dicho
        //CREATE TABLE --> Es una sentencia que indica que vamos a crear una tabla de valores
        //Y por utlimo, definimos los campos que va a contener esa tabla: ID, NOMBRE_MONEDA, VALOR_MONEDA

        "CREATE TABLE " + TABLE_NAME +
        " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE_MONEDA TEXT, VALOR_MONEDA DECIMAL)"
    )
}

/*Este metodo se utiliza cuando realizamos una modificacion directa en la base de datos, en el onCreate,
el usuario no la va poder visualizar, por asi decirlo, por lo tanto, en este sector tenemos que
ir actualizando la base de datos, en simultaneo al momento en que modificamos la base de datos. Este
nos permite, volver a generar la base de datos, para que aparezcan nuestras modificaciones*/

override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i:Int, i1:Int) {

}

companion object {

        //Nombre de la base de datos, con la extencion .db, muy importante
        private val DB_NAME = "exchange.db"

        //Version de la base de datos
        private val DB_VERSION = 1

        //El nombre de la tabla de datos que vamos a usar dentro de la base de datos
        private val TABLE_NAME = "Currency"
    }
}