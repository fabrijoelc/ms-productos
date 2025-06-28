package com.codigo.ms_productos.service.impl;

import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.repository.ProductoRepository;
import com.codigo.ms_productos.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        existente.setCategoria(producto.getCategoria());
        return productoRepository.save(existente);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}


