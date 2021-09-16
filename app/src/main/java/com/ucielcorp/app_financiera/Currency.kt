package com.ucielcorp.app_financiera

/*Este es el objeto que vamos a estar utilizando dentro de la base de datos, en este caso
representa la moneda, ya que nuestra app es de cambio de valores de monedas*/
class Currency {

    //Este objeto en si es muy simple, este mismo solo tiene dos propiedades, el nombre y el valor
    var currency_name: String? = null
    var currency_value: Float? = null
}

//Pero sin duda se pueden crear clases mucho mas complejas como una de Usuariosm en donde hay que
//añadir muchas mas propiedades
