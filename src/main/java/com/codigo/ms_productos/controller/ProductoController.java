package com.codigo.ms_productos.controller;

import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.service.ProductoService;
import com.codigo.ms_productos.service.util.AuthValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final AuthValidator authValidator;

    public ProductoController(ProductoService productoService, AuthValidator authValidator) {
        this.productoService = productoService;
        this.authValidator = authValidator;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto,
                                   @RequestHeader("Authorization") String token) {
        if (!authValidator.tieneAcceso(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader("Authorization") String token) {
        if (!authValidator.tieneAcceso(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Producto producto,
                                        @RequestHeader("Authorization") String token) {
        if (!authValidator.tieneAcceso(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                      @RequestHeader("Authorization") String token) {
        if (!authValidator.tieneAcceso(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
