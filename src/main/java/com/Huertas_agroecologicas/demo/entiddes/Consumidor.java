/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.entiddes;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author User
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Consumidor extends Usuario {

    private String nombreConsumidor;
    private String dni;
    private String direccion;

    @OneToMany(mappedBy = "consumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Publicacion> publicaciones;
    
    
    
    @ManyToMany
    @JoinTable(name = "consumidor_Cultivo",
            joinColumns = @JoinColumn(name = "idConsumidor"),
            inverseJoinColumns = @JoinColumn(name = "idCultivo"))
    private List<Cultivo> cultivos;
    
    
    @ManyToMany
     @JoinTable(name = "consumidor_huerta",
            joinColumns = @JoinColumn(name = "idConsumidor"),
            inverseJoinColumns = @JoinColumn(name = "idHuerta"))
    private List<Huerta> huertas;
    
        
    
    //// hemos utilizado las anotaciones @ManyToMany, @JoinTable, @JoinColumn, @Entity e @Id para establecer 
    /// la relación Many-to-Many entre Consumidor, Huerta y Cultivo. La tabla de unión entre Consumidor y Huerta se llama consumidor_huerta, 
    ///y la tabla de unión entre Consumidor y Cultivo se llama consumidor_cultivo.

}
