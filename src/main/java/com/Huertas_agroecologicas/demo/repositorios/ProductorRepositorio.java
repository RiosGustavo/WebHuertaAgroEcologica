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

    @Query("SELECT pro FROM Productor pro WHERE pro.nombreProductor = :nombreProductor")
    public Productor buscarPorNombre(@Param("nombreProductor") String nombreProductor);

    @Query("SELECT pro FROM Productor pro WHERE pro.dni = :dni")
    public Productor buscarPorCuit(@Param("dni") String dni);

    @Query("SELECT pro FROM Productor pro WHERE pro.direccion = :direccion")
    public Productor buscarPorDireccion(@Param("direccion") String direccion);
    
     //// FALTA IMPLENTAR EN EL SERVICIO DE Productor
//     @Query("SELECT pro FROM Productor pro WHERE ((:termino IS NULL OR LOWER(pro.nombreProductor) LIKE %:termino%) OR "
//            + "(:termino IS NULL OR pro.fechaAlta LIKE %:termino%) OR "
//            + "(:termino IS NULL OR pro.rubro LIKE %:termino%) OR "
//            + "(:termino IS NULL OR pro.cuit LIKE %:termino%) OR "
//            + "(:termino IS NULL OR pro.email LIKE %:termino%) OR "
//            + "(:termino IS NULL OR LOWER(pro.id) LIKE %:termino%))")
//    List<Productor> buscarProductorPorTermino(@Param("termino") String termino);
    
    
}


