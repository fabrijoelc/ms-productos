package com.codigo.ms_productos.service;


import com.codigo.ms_productos.entity.Producto;

import java.util.List;

public interface ProductoService {
    Producto crearProducto(Producto producto);
    List<Producto> listarProductos();
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
}
