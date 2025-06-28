package com.codigo.ms_productos.controller;

import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.service.ProductoService;
import com.codigo.ms_productos.service.util.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @Mock
    private AuthValidator authValidator;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarProductos() {
        Producto p1 = Producto.builder().id(1L).nombre("Teclado").precio(100.0).categoria("Tecnología").build();
        Producto p2 = Producto.builder().id(2L).nombre("Mouse").precio(50.0).categoria("Tecnología").build();

        when(authValidator.tieneAcceso("fake-token")).thenReturn(true);
        when(productoService.listarProductos()).thenReturn(Arrays.asList(p1, p2));

        ResponseEntity<?> response = productoController.listar("fake-token");
        List<Producto> resultado = (List<Producto>) response.getBody();

        assertEquals(2, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNombre());
        assertEquals("Mouse", resultado.get(1).getNombre());
    }

    @Test
    public void testCrearProducto() {
        Producto nuevo = Producto.builder().nombre("Celular").precio(2000.0).categoria("Tecnología").build();
        Producto guardado = Producto.builder().id(1L).nombre("Celular").precio(2000.0).categoria("Tecnología").build();

        when(authValidator.tieneAcceso("fake-token")).thenReturn(true);
        when(productoService.crearProducto(nuevo)).thenReturn(guardado);

        ResponseEntity<?> response = productoController.crear(nuevo, "fake-token");
        Producto resultado = (Producto) response.getBody();

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Celular", resultado.getNombre());
    }

    @Test
    public void testActualizarProducto() {
        Producto actualizado = Producto.builder().id(1L).nombre("Laptop Gamer").precio(4500.5).categoria("Tecnología").build();

        when(authValidator.tieneAcceso("fake-token")).thenReturn(true);
        when(productoService.actualizarProducto(1L, actualizado)).thenReturn(actualizado);

        ResponseEntity<?> response = productoController.actualizar(1L, actualizado, "fake-token");
        Producto resultado = (Producto) response.getBody();

        assertNotNull(resultado);
        assertEquals("Laptop Gamer", resultado.getNombre());
    }

    @Test
    public void testEliminarProducto() {
        when(authValidator.tieneAcceso("fake-token")).thenReturn(true);
        doNothing().when(productoService).eliminarProducto(1L);

        ResponseEntity<?> response = productoController.eliminar(1L, "fake-token");

        assertEquals(204, response.getStatusCodeValue());
    }
}

