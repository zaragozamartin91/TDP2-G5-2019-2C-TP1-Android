package com.g5.tdp2.myhealthapp.gateway;

/**
 * Un gateway es quien sale a obtener o buscar datos. Se usan para casos en los que es necesario obtener datos de una BBDD o de la red pero no aplica como "CASO DE USO"
 */
public interface Gateway {
    String INTERNAL_ERROR = "INTERNAL_ERROR";
    String UNKNOWN_ERROR = "UNKNOWN_ERROR";
}
