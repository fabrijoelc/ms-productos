package com.codigo.ms_productos.serviceImpl;

import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        Producto producto = Producto.builder()
                .nombre("Tablet")
                .precio(999.99)
                .categoria("Tecnología")
                .build();

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.crearProducto(producto);

        assertNotNull(resultado); // Asegúrate que no sea null
        assertEquals("Tablet", resultado.getNombre());
        assertEquals(999.99, resultado.getPrecio());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }


    @Test
    void testListarProductos() {
        Producto producto1 = Producto.builder().nombre("A").precio(1.0).categoria("X").build();
        Producto producto2 = Producto.builder().nombre("B").precio(2.0).categoria("Y").build();
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        List<Producto> productos = productoService.listarProductos();

        assertEquals(2, productos.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testActualizarProducto() {
        Producto existente = Producto.builder()
                .id(1L)
                .nombre("Mouse")
                .precio(50.0)
                .categoria("Tecnología")
                .build();

        Producto actualizado = Producto.builder()
                .nombre("Mouse Gamer")
                .precio(70.0)
                .categoria("Tecnología")
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(productoRepository.save(any(Producto.class))).thenReturn(existente);

        Producto result = productoService.actualizarProducto(1L, actualizado);

        assertEquals("Mouse Gamer", result.getNombre());
        assertEquals(70.0, result.getPrecio());
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(existente);
    }

    @Test
    void testEliminarProducto() {
        Long id = 1L;
        doNothing().when(productoRepository).deleteById(id);
        productoService.eliminarProducto(id);
        verify(productoRepository).deleteById(id);
    }
}
