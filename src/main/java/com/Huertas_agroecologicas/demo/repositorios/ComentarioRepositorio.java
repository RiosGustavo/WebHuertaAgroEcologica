/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario,String> {
    
     @Query("SELECT co FROM Comentario co WHERE co.cosecha.idCosecha = :idCosecha AND co.altaBaja = true ORDER BY co.fechaPublicacion DESC")
    public List<Comentario> comentariosPorCosecha(@Param("idCosecha") String idCosecha);
    
    @Query("SELECT co FROM Comentario co WHERE co.huerta.idHuerta = :idHuerta AND co.altaBaja = true ORDER BY co.fechaPublicacion DESC")
    public List<Comentario> comentariosPorHuerta(@Param("idHuerta") String idHuerta);
    
}