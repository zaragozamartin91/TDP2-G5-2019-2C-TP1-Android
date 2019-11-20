package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Sanatorium;
import com.g5.tdp2.myhealthapp.entity.SanatoriumWdistForm;

import java.util.List;
import java.util.function.Consumer;

/**
 * Caso de uso de busqueda de sanatorios
 */
public interface SearchSanatoriums extends Usecase {

    /**
     * Busca sanatorios cercanos
     *
     * @param form         Formulario de busqueda
     * @param succCallback Accion a realizar con los resultados
     * @param errCallback  Accion a realizar ante un error
     * @throws SearchProvidersException Si ocurre un error al realizar la busqueda
     */
    void searchSanatoriums(
            SanatoriumWdistForm form,
            Consumer<List<Sanatorium>> succCallback,
            Consumer<Exception> errCallback) throws SearchProvidersException;
}
