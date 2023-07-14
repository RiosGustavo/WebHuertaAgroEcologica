/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.entiddes;

import com.Huertas_agroecologicas.demo.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;


/**
 *
 * @author User
 */
@Entity

public class Administrador extends Usuario{

    public Administrador() {
    }

    public Administrador(String id, String email, String password, Rol roles, Boolean altaBaja, Imagen imagen, Date fechaAlta) {
        super(id, email, password, roles, altaBaja, imagen, fechaAlta);
    }
    
    
    
    
    
    
    
    
}
