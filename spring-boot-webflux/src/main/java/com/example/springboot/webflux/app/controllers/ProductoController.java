package com.example.springboot.webflux.app.controllers;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.example.springboot.webflux.app.SpringBootWebfluxApplication;
import com.example.springboot.webflux.app.dao.ProductoDao;
import com.example.springboot.webflux.app.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoDao dao;
	
	@GetMapping({"", "/", "/listar"})
	public String listar(Model model) {
		
		Flux<Producto> productos = dao.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				});
		
		productos.subscribe(prod -> log.info(prod.getNombre()));
		
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "SE ENVIA EL DATO DE LOS PRODUCTOS CON WEBFLUX!");
		return "listar";
	}
	
	@GetMapping("/listar-datadriven")
	public String listarDataDriven(Model model) {
		
		Flux<Producto> productos = dao.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				}).delayElements(Duration.ofSeconds(1));
		
		productos.subscribe(prod -> log.info(prod.getNombre()));
		
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 1));
		model.addAttribute("titulo", "LISTA LOS PRODUCTOS CON WEBFLUX! Y DELAY");
		return "listar";
	}
	
	@GetMapping("/listar-full")
	public String listarFull(Model model) {
		
		Flux<Producto> productos = dao.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.repeat(6000);
		
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "SE ENVIA EL DATO DE LOS PRODUCTOS CON WEBFLUX!");
		return "listar";
	}
	
	@GetMapping("/listar-chunked")
	public String listarChunked(Model model) {
		
		Flux<Producto> productos = dao.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.repeat(6000);
		
		model.addAttribute("productos", productos);
		model.addAttribute("titulo", "SE ENVIA EL DATO DE LOS PRODUCTOS CON WEBFLUX! LISTAR CHUNKED");
		return "listar-chunked";
	}

}
