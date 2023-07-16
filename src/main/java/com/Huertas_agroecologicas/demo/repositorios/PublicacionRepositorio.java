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
//    /// falta implemntar el la siguiente linea en el servicio
    @Query("SELECT pu FROM Publicacion pu WHERE pu.huerta.idHuerta = :idHuerta AND pu.altaBaja = true")
    public List<Publicacion> publicacionesActivasPorHuerta (@Param("idHuerta") String idHuerta  );
    
     // falta implemntar el la siguiente linea en el servicio
     @Query("SELECT pu FROM Publicacion pu WHERE pu.huerta.nombrehuerta = :nombrehuerta")
    public List<Publicacion> buscarPorHuerta (@Param("nombrehuerta") String nombrehuerta );
    
    
    @Query("SELECT pu FROM Publicacion pu WHERE pu.cosecha.idCosecha = idCosecha")
     public List<Publicacion> buscarPorCosecha (@Param("idCosecha") String idCosecha  );
    
    
    
}
