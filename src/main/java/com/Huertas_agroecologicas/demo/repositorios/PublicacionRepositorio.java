/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

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
public interface PublicacionRepositorio extends JpaRepository<Publicacion, String> {

    @Query("SELECT pu FROM Publicacion pu WHERE pu.altaBaja = true")
    public List<Publicacion> listadoPublicacionesActivas();
//

    @Query("SELECT pu FROM Publicacion pu WHERE pu.titulo LIKE %:titulo%")
    public List<Publicacion> buscarPorTitulo(@Param("titulo") String titulo);
//

    @Query(value = "SELECT * FROM Publicacion order by fecha_alta desc", nativeQuery = true)
    //// esta linea esta implementada en el servicio de publicacion y aca se llama
    List<Publicacion> findAllOrderByfecha_altaDesc();
//


    @Query("SELECT pu FROM Publicacion pu WHERE pu.huerta.idHuerta = :idHuerta AND pu.altaBaja = true")
    public List<Publicacion> publicacionesActivasPorHuerta(@Param("idHuerta") String idHuerta);

    @Query("SELECT pu FROM Publicacion pu WHERE pu.cultivo.idCultivo = :idCultivo AND pu.altaBaja = true")
    public List<Publicacion> publicacionesActivasPorCultivo(@Param("idCultivo") String idCultivo);

    // falta implemntar el la siguiente linea en el servicio
    @Query("SELECT pu FROM Publicacion pu WHERE pu.huerta.idHuerta = :idHuerta")
    public List<Publicacion> buscarPorHuerta(@Param("idHuerta") String idHuerta);

    @Query("SELECT pu FROM Publicacion pu WHERE pu.cultivo.idCultivo =:idCultivo")
    public List<Publicacion> buscarPorCultivo(@Param("idCultivo") String idCultivo);

    //////////////////////////////////////
    @Query("SELECT hu FROM Publicacion hu WHERE (:termino IS NULL OR CONCAT(hu.titulo, hu.descripcion) LIKE %:termino%) "
            + "AND (:estado IS NULL OR hu.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE hu.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN hu.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN hu.fechaAlta END DESC")
    public List<Publicacion> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT hu FROM Publicacion hu WHERE (:estado IS NULL OR hu.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE hu.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN hu.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN hu.fechaAlta END DESC")
    public List<Publicacion> search2(@Param("estado") String estado, @Param("orden") String orden);
    //////////////////////////
    
    @Query("SELECT co FROM Publicacion co WHERE (:termino IS NULL OR CONCAT(co.titulo, co.descripcion) LIKE %:termino%) "
            + "AND (:estado IS NULL OR co.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE co.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN co.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN co.fechaAlta END DESC")
    public List<Publicacion> search3(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

}
