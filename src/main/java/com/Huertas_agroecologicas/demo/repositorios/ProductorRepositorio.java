/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Productor;
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
public interface ProductorRepositorio extends JpaRepository<Productor, String> {

    @Query("SELECT pro FROM Usuario pro WHERE pro.email = :email")
    public Productor buscarPorEmail(@Param("email") String email);
//
//    @Query("SELECT pro FROM Productor pro WHERE pro.nombreProductor = :nombreProductor")
//    public Productor buscarPorNombre(@Param("nombreProductor") String nombreProductor);
//
//    @Query("SELECT pro FROM Productor pro WHERE pro.dni = :dni")
//    public Productor buscarPorCuit(@Param("dni") String dni);
//
//    @Query("SELECT pro FROM Productor pro WHERE pro.direccion = :direccion")
//    public Productor buscarPorDireccion(@Param("direccion") String direccion);

    
     @Query("SELECT pro FROM Productor pro WHERE (:termino IS NULL OR CONCAT(pro.nombreProductor, pro.direccion, pro.email) LIKE %:termino%) "
            + "AND (:estado IS NULL OR pro.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE pro.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN pro.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN pro.fechaAlta END DESC")
    public List<Productor> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT pro FROM Productor pro WHERE (:estado IS NULL OR pro.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE pro.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN pro.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN pro.fechaAlta END DESC")
    public List<Productor> search2(@Param("estado") String estado, @Param("orden") String orden);

    
    
}


