/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
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
    
     @Query("SELECT consu FROM Usuario consu WHERE consu.email = :email")
    public Consumidor buscarPorEmail(@Param("email") String email);
    
//    
     @Query("SELECT consu FROM Consumidor consu WHERE (:termino IS NULL OR CONCAT(consu.nombreConsumidor, consu.direccion, consu.dni ,  consu.email) LIKE %:termino%)"
            + "AND (:estado IS NULL OR consu.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE consu.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN consu.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN consu.fechaAlta END DESC")
    public List<Consumidor> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);
     
    
    @Query("SELECT consu FROM Consumidor consu WHERE (:estado IS NULL OR consu.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE consu.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN consu.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN consu.fechaAlta END DESC")
    public List<Consumidor> search2(@Param("estado") String estado, @Param("orden") String orden);
    
    

    ///Spring Data JPA detectará la relación @ManyToMany entre Consumidor y Cultivo y generará automáticamente la consulta para buscar los consumidores por el id del cultivo.
//    
//    public List<Consumidor> findByCultivosId(String idCultivo);
//    
//    /// Spring Data JPA detectará la relación @ManyToMany entre Consumidor y Huerta y generará automáticamente la consulta para buscar los consumidores por el id de la Huerta.
//    public List<Consumidor> findByHuertasId(String idHuerta);
    
    //// Al utilizar los métodos de consulta generados automáticamente por Spring Data JPA, te evitas la necesidad de escribir consultas personalizadas en las anotaciones @Query 
    //y te facilita el acceso a las relaciones ManyToMany de forma más sencilla y segura.
  
     @Query("SELECT consu FROM Consumidor consu JOIN consu.cultivos cult WHERE cult.idCultivo = :idCultivo")
    public List<Consumidor> consumidoresPorCultivo(@Param("idCultivo") String idCultivo);
    
    @Query("SELECT consu FROM Consumidor consu JOIN consu.huertas h WHERE h.idHuerta = :idHuerta ")
    public List<Consumidor> consumidoresPorHuerta(@Param("idHuerta") String idHuerta);
    
    
     @Query("SELECT publicaciones FROM Consumidor consu WHERE consu.id = :id")
    public List<Publicacion> litadoPublicacionesConsumidor(@Param("id") String id); 
    
    
   
    
    
    
}
