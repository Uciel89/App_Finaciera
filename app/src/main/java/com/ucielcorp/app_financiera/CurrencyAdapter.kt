package com.ucielcorp.app_financiera

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/*Generalmente, cada objeto va a tener su propio adaptador con la base de datos*/

/*En esta clase, tenemos que establecer en codigo necesario para que se pueda generar
una buena conexion entre mi base de datos y mi objeto*/

/*Esta clase tiene que realizar varias tareas:

*** Abrir la conexion con la base de datos -> OPEN.

* Va a tener que generar la cantidad de metodos que sean necesarios para que se puedan realizar
  todas las tareas que nosotros querramos:

** Select -> traer algun dato de la base de datos o tambien traer un listado(todos los datos) o con algun filtro
** Insert -> Insertar datos a la base de datos
** Update -> Actualizar datos de la base de datos
** Delete -> Eliminar datos de la base de datos

*** Close -> Cerrar la base de datos*/

class CurrencyAdapter(private val context: Context) {

    private var dbHelper: DBHelper? = null

    private var db: SQLiteDatabase? = null

    /*OPEN*/
    fun abrir() {

        //Instanciamos al DBHelper pasandole como argumento el contexto
        dbHelper = DBHelper(context)

        //.writableDatabase -> este metodo se encarga de generar la base de datos
        //Y si llega a existir la base de datos, genera una instancia de conexion de la misma

        //db -> almacenmos esto dentro de la variable db, que es la que nos permite interactuar con la base de datos
        //dentro del adaptador
        db = dbHelper!!.writableDatabase
    }

    fun monedaPorId(id: Int): String? {

        /*Basicamente, esta funcion me busca una id y nos devuele un string que seguramente se el nombre de dicho
          elemento.*/

        //.query --> este nos permite construir una consulta, la cual contiene varios varios parametros
        //- table -> el nombre de las tablas
        //- String[] colums -> un array de las columnas que me quiero traer
        //- String slection -> una seleccion que me representa la condicion de whereClouse
        //- String [] selection -> un array con las condiciones de "where"

        /*

        - groupBy -> La sentencia GROUP BY identifica una columna seleccionada para utilizarla para agrupar resultados.
        Divide los datos en grupos por los valores de la columna especificada,
        y devuelve una fila de resultados para cada grupo.


        - having -> Se utiliza "having", seguido de la condición de búsqueda,
        para seleccionar ciertas filas retornadas por la cláusula "group by".


        - ordenBy -> nos permite ordenar las filas de resultados por una o más columnas.*/


        /**
         * public Cursor query (String table, String[] columns,
         * String selection, String[] selectionArgs,
         * String groupBy, String having, String orderBy)
         */

        val c = db!!.query(

            //String[] colums -> COLUMN_CURRENCY_NAME Y COLUMN_CURRENCY_VALUE
            //Le suministro el id
            //Que me ordene los valores por ordenBy

            TABLE_NAME, arrayOf(COLUMN_CURRENCY_NAME, COLUMN_CURRENCY_VALUE),
            "id=?", arrayOf(id.toString()), null, null, COLUMN_CURRENCY_NAME
        )

        //moveToFirst() --> este metodo realiza la tarea de posicicionamiento en la fila que le indicamos
        //para poder recuperar esos datos
        if (c.moveToFirst()) {

            //getColumnIndex --> nos sirve para posicionarnos en una columna
            /*Esa comlumna en este caso seria COLUMN_CURRENCY_NAME(vale aclarar que en esta tabla de datos solo tenemos
            dos columnas -> COLUMN_CURRENCY_NAME y COLUMN_CURRENCY_VALUE)*/
            val colIndex = c.getColumnIndex(COLUMN_CURRENCY_NAME)

            //colIndex --> hace referencia a la posicion de la columna, por lo tanto cuando ejecutamos el
            //getColumnIndex, lo que nos va a devolver va a ser la ubicacion de la columna dentro de la tabla de datos

            /*Y como ultima cosa, me tae el balor posicionado en la columna (colIndex) y fila que ele indicamos (id)*/
            return c.getString(colIndex)

        } else {

            return null
        }
    }

    fun valorMonedaPorNombre(nombre_moneda: String): Float? {

        /*Esta funcion es parecida a la de monedaPorId pero con algunas diferencias, una de las mas notorias es
        * que envez de usar como argumento el id, uso el nombre de la moneda*/

        /**
         * public Cursor query (String table, String[] columns,
         * String selection, String[] selectionArgs,
         * String groupBy, String having, String orderBy)
         */

        val c = db!!.query(
            TABLE_NAME, arrayOf(COLUMN_CURRENCY_VALUE),
            //Es importante que ahora en vez de bucar por id, buscamos directamente por el nombre de dicha moneda
            //"NOMBRE_MONEDA" -> el nombre de la moneda basicamente
            "NOMBRE_MONEDA=?", arrayOf(nombre_moneda), null, null, null
        )

        if (c.moveToFirst()) {
            //Y cambiamoes el COLUMN_CURRENCY_NAME por el COLUMN_CURRENCY_VALUE
            val colIndex = c.getColumnIndex(COLUMN_CURRENCY_VALUE)

            //Me va a retornar el valor que la moneda que elegi, por ejemplo NOMBRE_MONEDA -> DOLAR, COLUMN_CURRENCY_VALUE
            //--> 70
            return c.getFloat(colIndex)
        } else {
            return null
        }
    }

    fun objectMonedaPorNombre(nombre_moneda: String): Currency {

        /**La myor diferencia entre esta funcion y las anteriores es que estoy interactuando directamente con el objeto
         Currency, en cambio en las otras funciones, pedia un nuemro o un string**/

        /*Esta funcion en si, realiza las mismas funciones que valorMonedaPorNombre, con el pequeño detalle de que
        * en vez de interactuar directamente un valor especifico, lo hago atraves del objeto
        * Currency, donde tengo almacenado todos los valores, entonces, poque hacemos estos, basicamente es uba mejor
        * forma. Es fin, es mejor manipular los objetos que un valor especifico */
        val currencyReturn = Currency()

        val c = db!!.query(
            TABLE_NAME, arrayOf(COLUMN_CURRENCY_VALUE),
            "NOMBRE_MONEDA=?", arrayOf(nombre_moneda), null, null, null
        )

        if (c.moveToFirst()) {
            val colIndex = c.getColumnIndex(COLUMN_CURRENCY_VALUE)

            currencyReturn.currency_name = nombre_moneda
            currencyReturn.currency_value = c.getFloat(colIndex)

            return currencyReturn
        } else {
            return currencyReturn
        }
    }

    fun todos(): Cursor {

        //Mediante un objeto cursor, traigo todos los elemento de mi base de datos

        /**
         * public Cursor query (String table,
         * String[] columns,
         * String selection,
         * String[] selectionArgs,
         * String groupBy,
         * String having,
         * String orderBy)
         */

        return db!!.query(
            TABLE_NAME,
            arrayOf("id", COLUMN_CURRENCY_NAME, COLUMN_CURRENCY_VALUE),
            null,
            null,
            null,
            null,
            COLUMN_CURRENCY_NAME
        )
    }

    /*INSERT*/
    fun insertar(moneda_nombre: String, moneda_valor: Float?) {

        //ContentValues() --> nos permite almacenar dichos datos, de forma "clave (key) - valor (value)"
        val valores = ContentValues()

        //COLUM_CURRENCY_NAME -> la clave es el nombre
        //moneda_nombre -> el valor en si es el nombre de la moneda que queremos agregar
        valores.put(COLUMN_CURRENCY_NAME, moneda_nombre)

        //COLUM_CURRENCY_VALUE -> La clave es el es valor
        //moneda_valor -> El valor es el valor monetario en si.
        valores.put(COLUMN_CURRENCY_VALUE, moneda_valor)

        /*Un ejemplo de estos seria, en el nombre de la moneda, dolar
          Y en el valor 70. Entonces el dato formado con esto es " 70 dolares "*/

        //.insert -> nos permite insertar los valores a la base de datos
        //TABLE_NAME -> Le tengo que decir en tabla voy a guardar el dato
        //valores -> es un objeto que es el que va a permitir ingresar todos lo valores

        db!!.insert(TABLE_NAME, null, valores)

        //Despues de tener todos lo valores (lo cual forma un elementos),
        //lo mando al db.insert, y se introduce en la base de datos
    }

    /*UPDATE*/
    fun actualizar(id: Int, moneda_nombre: String, moneda_valor: Float?) {

        /*Esta funcion lo que recive son en si, el elemento que quiero actualizar. Por eso tenemos
        id (el identificador del elemento), moneda_nombre y moneda_valor ->
        esto conforma a un elemeto dentro de la base de datos. Podemos bucar los elementos por el ID o por el nombre*/

        val valores = ContentValues()
        valores.put(COLUMN_CURRENCY_NAME, moneda_nombre)
        valores.put(COLUMN_CURRENCY_VALUE, moneda_valor)

        /**
         * public int update (String table,
         * ContentValues values,
         * String whereClause,
         * String[] whereArgs)
         */

        //.update -> nos permite actualizar los valores de un elemento
        //TABLE_NAME -> El nombre de la tabla a la cual queremos acceder para modificar algun elemento
        //valores -> tenemos de nuevo el metodo ContentValuer(), el cual contiene dichos valores

        /* whereClause -> este contiene un id = ? -> este signo de pregunta es remplazado con el arrayOf que le sigue
        el cual contiene el id que se encuentra en los parametros de la funcion, este mismo estrasformado a un string.

        Por lo tanto la actualizacion tiene el siguiente formato: update carrency whete ID = (El valor que este dentro
        del id de los parametros de la fución)*/

        db!!.update(TABLE_NAME, valores, "ID=?", arrayOf(id.toString()))
    }

    /*DELETE*/
    fun eliminar(id: Int) {
        //En esta funcion solo utlizo el ID
        //Es necesario que tenga una "whereClauses" porque si no podria eliminar todos los elementos de la base de datos

        //.delete -> nos sirve para eliminar un elemento
        db!!.delete(TABLE_NAME, "ID=?", arrayOf(id.toString()))
    }

    /*CLOSE*/
    fun cerrar() {

        //.close() -> cierra la base de datos
        dbHelper!!.close()
    }

    companion object {
        private val TABLE_NAME = "Currency"
        private val COLUMN_CURRENCY_NAME = "NOMBRE_MONEDA"
        private val COLUMN_CURRENCY_VALUE = "VALOR_MONEDA"
    }

}
