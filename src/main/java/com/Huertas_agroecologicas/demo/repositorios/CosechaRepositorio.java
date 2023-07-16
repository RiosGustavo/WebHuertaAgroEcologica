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
public interface CosechaRepositorio extends JpaRepository<Cosecha, String> {

    @Query("SELECT co FROM Cosecha co WHERE co.nombreCosecha LIKE %:nombreCosecha%")
    public List<Cosecha> buscarPorNombreCosecha(@Param("nombreCosecha") String nombreCosecha);

    @Query("SELECT co FROM Cosecha co WHERE co.altaBaja = true")
    public List<Cosecha> buscarPorEstado();
    
     @Query(value = "select * from Cosecha order by fecha_alta desc", nativeQuery = true)
    List<Cosecha> findAllOrderByfecha_altaDesc();

    @Query("SELECT co FROM Cosecha co WHERE co.fechaAlta = :fechaAlta")
    public List<Cosecha> buscarPorFecha(@Param("fechaAlta") Date fechaAlta);
    
     @Query("SELECT co FROM Cosecha co WHERE co.precio = :precio")
    public List<Cosecha> buscarPorPrecio(@Param("precio") Integer precio);
    
     @Query("SELECT co FROM Cosecha co WHERE co.stock =  :stock")
    public List<Cosecha> buscarPorStock(@Param("stock") Integer stock);
//    
//     @Query("SELECT co FROM Cosecha co WHERE co.huertas.nombrehuerta =  :nombrehuerta")
//    public List<Cosecha> buscarPorHuertas(@Param("nombrehuerta") String nombrehuerta);
    
    

}
