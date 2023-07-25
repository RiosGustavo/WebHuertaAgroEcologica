/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author User
 */
@Controller
public class ErrorControlador implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderPaginaError(HttpServletRequest httpRequest) {

        ModelAndView paginaError = new ModelAndView("error");

        String errorMsg = "";

        int httpCodigoError = getCodigoError(httpRequest);

        switch (httpCodigoError) {

            case 400 ->  {
                errorMsg = "El recurso solicitado no existe.";
            }

            case 401 ->  {
                errorMsg = "No se encuentra autorizado.";
            }

            case 403 ->  {
                errorMsg = "No tiene permisos para acceder a este recurso.";
            }

            case 404 ->  {
                errorMsg = "El recurso solicitado no fue encontrado.";
            }

            case 500 ->  {
                errorMsg = "Se produjo un error interno.";
            }

        }
        paginaError.addObject("codigo", httpCodigoError);
        paginaError.addObject("mensaje", errorMsg);
        return paginaError;

    }
    
    private int getCodigoError (HttpServletRequest httpRequest){
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }
    
    public String getErrorPath(){
        return "/error.html";
    }

}
