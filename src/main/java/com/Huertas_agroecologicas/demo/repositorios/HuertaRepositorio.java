/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Cosecha;
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

    @Query("SELECT hu FROM Huerta hu WHERE hu.titulo LIKE %:titulo%")
    public List<Huerta> buscarPorTitulo(@Param("titulo") String titulo);

    @Query(value = "select * from Huerta order by fecha_alta desc", nativeQuery = true)
    List<Huerta> findAllOrderByfecha_altaDesc();

    @Query("SELECT hu FROM Huerta hu WHERE hu.fechaAlta = :fechaAlta")
    public List<Huerta> buscarPorFecha(@Param("fechaAlta") Date fechaAlta);

    @Query("SELECT hu FROM Huerta hu WHERE hu.altaBaja = true")
    public List<Huerta> buscarPorEstado();

    @Query("SELECT hu FROM Huerta hu WHERE hu.productor.id = :id")
    public List<Huerta> huertasPorProductor(@Param("id") String id);


    @Query("SELECT hu FROM Huerta hu WHERE hu.cosecha.nombreCosecha = :nombreCosecha")
    public List<Huerta> cosechasPorHuerta(@Param("nombreCosecha") String nombreCosecha);

    @Query("SELECT hu FROM Huerta hu WHERE hu.nombreProductor = :nombreProductor")
    public List<Huerta> buscarPorNombreProductor(@Param("nombreProductor") String nombreProductor);

    List<Huerta> findByAltaBajaTrue();

}
