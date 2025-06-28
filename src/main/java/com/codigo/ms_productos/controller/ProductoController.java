package com.codigo.ms_productos.controller;

import com.codigo.ms_productos.entity.Producto;
import com.codigo.ms_productos.service.ProductoService;
import com.codigo.ms_productos.service.util.AuthValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("POST /productos - Token recibido: " + token);
        if (!authValidator.tieneAcceso(token)) {
            System.out.println("POST /productos - Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader("Authorization") String token) {
        System.out.println("GET /productos - Token recibido: " + token);
        if (!authValidator.tieneAcceso(token)) {
            System.out.println("GET /productos - Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        System.out.println("GET /productos - Acceso concedido.");
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Producto producto,
                                        @RequestHeader("Authorization") String token) {
        System.out.println("PUT /productos/" + id + " - Token recibido: " + token);
        if (!authValidator.tieneAcceso(token)) {
            System.out.println("PUT /productos - Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                      @RequestHeader("Authorization") String token) {
        System.out.println("DELETE /productos/" + id + " - Token recibido: " + token);
        if (!authValidator.tieneAcceso(token)) {
            System.out.println("DELETE /productos - Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}


