/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import java.util.Date;
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
public interface HuertaRepositorio extends JpaRepository<Huerta, String> {

    @Query("SELECT hu FROM Huerta hu WHERE hu.idHuerta = :idHuerta")
    public Huerta buscarPorId(@Param("idHuerta") String idHuerta);



    @Query(value = "select * from Huerta order by fecha_alta desc", nativeQuery = true)
    List<Huerta> findAllOrderByfecha_altaDesc();

    @Query("SELECT hu FROM Huerta hu WHERE hu.fechaAlta = :fechaAlta")
    public List<Huerta> buscarPorFecha(@Param("fechaAlta") Date fechaAlta);

    @Query("SELECT hu FROM Huerta hu WHERE hu.altaBaja = true")
    public List<Huerta> buscarPorEstado();

    @Query("SELECT hu FROM Huerta hu WHERE hu.productor.id = :id")
    public List<Huerta> huertasPorProductor(@Param("id") String id);
    
     @Query("SELECT hu FROM Huerta hu WHERE hu.nombreHuerta = :nombreHuerta")
    public List<Huerta> buscarPorHuerta (@Param("nombreHuerta") String nombreHuerta);


    List<Huerta> findByAltaBajaTrue();
    
     
    @Query("SELECT hu FROM Huerta hu WHERE (:termino IS NULL OR CONCAT(hu.nombreHuerta, hu.cuerpo, hu.productor.nombreProductor) LIKE %:termino%) "
            + "AND (:estado IS NULL OR hu.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE hu.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN hu.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN hu.fechaAlta END DESC")
    public  List<Huerta> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT hu FROM Huerta hu WHERE (:estado IS NULL OR hu.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE hu.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN hu.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN hu.fechaAlta END DESC")
    public  List<Huerta> search2(@Param("estado") String estado, @Param("orden") String orden);
     
    

}
