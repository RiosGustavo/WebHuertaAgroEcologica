/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.entiddes;

import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@AttributeOverride(name = "id", column = @Column(name = "idProductor"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Productor extends Usuario {

    private String nombreProductor;
    private String dni;
    private String direccion;

    /// con la opción cascade = CascadeType.ALL, se logrará que cuando se persista, 
    ///actualice o elimine un Productor, también se realice la misma operación en la Huerta asociada.
    
    ///Con orphanRemoval = true, cuando se elimine el Productor, se eliminará también la Huerta asociada, en caso de que no tenga otra relación.
    @OneToOne(mappedBy = "productor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Huerta huerta;

    @OneToMany(mappedBy = "productor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cultivo> cultivos;

}
