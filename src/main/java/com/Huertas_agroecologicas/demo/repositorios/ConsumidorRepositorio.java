/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
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
public interface ConsumidorRepositorio extends JpaRepository<Consumidor,String> {
//    
     @Query("SELECT consu FROM Consumidor consu WHERE (:termino IS NULL OR CONCAT(consu.nombreConsumidor, consu.direccion, consu.dni ,  consu.email) LIKE %:termino%)")
    public List<Consumidor> search(@Param("termino") String termino);
//
//    
////    
//     @Query("SELECT consu FROM Consumidor consu WHERE consu.cosechas.idCultivo :idCultivo")
//    public List<Consumidor> consumidoresPorCultivo(@Param("idCultivo") String idCultivo);
//    
    @Query("SELECT consu FROM Comentario consu WHERE consu.huerta.idHuerta = :idHuerta ")
    public List<Consumidor> consumidoresPorHuerta(@Param("idHuerta") String idHuerta);
    
}
