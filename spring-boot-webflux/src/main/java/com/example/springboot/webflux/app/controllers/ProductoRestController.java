package com.example.springboot.webflux.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.webflux.app.dao.ProductoDao;
import com.example.springboot.webflux.app.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoDao dao;

	@GetMapping
	public Flux<Producto> index() {

		Flux<Producto> productos = dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			return producto;
		}).doOnNext(prod -> log.info(prod.getNombre()));

		return productos;
	}

	@GetMapping("/{id}")
	public Mono<Producto> listarPorId(@PathVariable String id) {
		// PRIMERA FORMA
		// Mono<Producto> producto = dao.findById(id);

		// SEGUNDA FORMA CON FLUX
		Flux<Producto> productos = dao.findAll();

		Mono<Producto> producto = productos
				.filter(prod -> prod.getId().equals(id))
				.next()
				.doOnNext(p -> log.info(p.getNombre()));
		return producto;
	}

}
