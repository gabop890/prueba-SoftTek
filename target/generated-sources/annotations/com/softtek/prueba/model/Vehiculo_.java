package com.softtek.prueba.model;

import com.softtek.prueba.model.Manejo;
import com.softtek.prueba.model.Proveedor;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-13T14:33:09")
@StaticMetamodel(Vehiculo.class)
public class Vehiculo_ { 

    public static volatile SingularAttribute<Vehiculo, String> marca;
    public static volatile SingularAttribute<Vehiculo, String> estado;
    public static volatile ListAttribute<Vehiculo, Manejo> manejoList;
    public static volatile SingularAttribute<Vehiculo, Proveedor> proveedor;
    public static volatile SingularAttribute<Vehiculo, Integer> modelo;
    public static volatile SingularAttribute<Vehiculo, String> placa;

}