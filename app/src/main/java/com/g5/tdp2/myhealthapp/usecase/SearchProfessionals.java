package com.g5.tdp2.myhealthapp.usecase;

import com.g5.tdp2.myhealthapp.entity.Professional;
import com.g5.tdp2.myhealthapp.entity.ProfessionalSearchForm;
import com.g5.tdp2.myhealthapp.entity.ProfessionalWdistForm;

import java.util.List;
import java.util.function.Consumer;

/**
 * Caso de uso de busqueda de profesionales
 */
public interface SearchProfessionals extends Usecase {
    /**
     * Dispara una busqueda de profesionales
     *
     * @param form         Formulario de busqueda
     * @param succCallback Accion a realizar con los resultados
     * @param errCallback  Accion a realizar ante un error
     * @throws SearchProvidersException Si ocurre un error al realizar la busqueda
     */
    void searchProfessionals(
            ProfessionalSearchForm form,
            Consumer<List<Professional>> succCallback,
            Consumer<Exception> errCallback
    ) throws SearchProvidersException;

    /**
     * Busca profesionales cercanos
     *
     * @param form         Formulario de busqueda
     * @param succCallback Accion a realizar con los resultados
     * @param errCallback  Accion a realizar ante un error
     * @throws SearchProvidersException Si ocurre un error al realizar la busqueda
     */
    void searchProfessionals(
            ProfessionalWdistForm form,
            Consumer<List<Professional>> succCallback,
            Consumer<Exception> errCallback) throws SearchProvidersException;
}
