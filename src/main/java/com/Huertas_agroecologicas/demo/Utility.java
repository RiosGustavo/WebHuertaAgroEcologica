/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author User
 */
public class Utility {
    
    public static String getSiteURL(HttpServletRequest request) {

        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");

    }
    
}
