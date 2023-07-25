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
public interface CultivoRepositorio extends JpaRepository<Cultivo, String> {

    @Query("SELECT co FROM Cultivo co WHERE co.nombreCultivo LIKE %:nombreCultivo%")
    public List<Cultivo> buscarPorNombreCultivo(@Param("nombreCultivo") String nombreCultivo);

    @Query("SELECT cu FROM Cultivo cu  JOIN cu.productor pro WHERE pro.id = :id")
    public List<Cultivo> buscarCultivoPorProductor(@Param("id") String id);
    
    
    ///Con esta corrección, la consulta realiza un JOIN con la lista de consumidores de Cultivo (cu.consumidores) 
    /// y filtra los cultivos que están asociados al Consumidor con el id especificado.

    @Query("SELECT cu FROM Cultivo cu  JOIN cu.consumidores WHERE consu.id = :id")
    public List<Cultivo> buscarCultivoPorConsumior(@Param("id") String id);

    @Query("SELECT co FROM Cultivo co WHERE co.altaBaja = true")
    public List<Cultivo> buscarPorEstado();

    @Query(value = "select * from Cultivo order by fecha_alta desc", nativeQuery = true)
    List<Cultivo> findAllOrderByfecha_altaDesc();

    @Query("SELECT co FROM Cultivo co WHERE co.fechaAlta = :fechaAlta")
    public List<Cultivo> buscarPorFecha(@Param("fechaAlta") Date fechaAlta);

    @Query("SELECT co FROM Cultivo co WHERE co.precio = :precio")
    public List<Cultivo> buscarPorPrecio(@Param("precio") Integer precio);

    @Query("SELECT co FROM Cultivo co WHERE co.stock =  :stock")
    public List<Cultivo> buscarPorStock(@Param("stock") Integer stock);

    @Query("SELECT c FROM Cultivo c JOIN c.huertas h WHERE h.idHuerta = :idHuerta")
    public List<Cultivo> buscarPorHuertas(@Param("idHuerta") String idHuerta);

    @Query("SELECT hu FROM Huerta hu JOIN hu.cultivos c WHERE  c.idCultivo :idCultivo ")
    public List<Huerta> buscarHuertasPorCultivo(@Param("idCultivo") String idCultivo);
//    

    @Query("SELECT co FROM Cultivo co WHERE (:termino IS NULL OR CONCAT(co.nombreCultivo, co.descripcion, co.precio, co.stock) LIKE %:termino%) "
            + "AND (:estado IS NULL OR co.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE co.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN co.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN co.fechaAlta END DESC")
    public List<Cultivo> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT co FROM Cultivo co WHERE (:estado IS NULL OR co.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE co.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN co.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN co.fechaAlta END DESC")
    public List<Cultivo> search2(@Param("estado") String estado, @Param("orden") String orden);

    List<Cultivo> findByAltaBajaTrue();

}
