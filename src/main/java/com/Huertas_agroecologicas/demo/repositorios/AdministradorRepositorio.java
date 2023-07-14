/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface AdministradorRepositorio  extends JpaRepository<Administrador,String>{
    
}
