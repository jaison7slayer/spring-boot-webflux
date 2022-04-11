package com.example.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.example.springboot.webflux.app.dao.ProductoDao;
import com.example.springboot.webflux.app.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
	
	@Autowired
	private ProductoDao dao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos")
		.subscribe();
		Flux.just(new Producto("Mouse inalambrico", 2.5),
				new Producto("Keyboard cable", 7.5),
				new Producto("Monitor LCD", 2.5),
				new Producto("CPU gamer", 500.5),
				new Producto("USB 3.0", 15.25),
				new Producto("Mousepad 20x10", 10.5),
				new Producto("Laptop DELL", 1000.5),
				new Producto("Charger for laptop", 50.5),
				new Producto("Disco duro externo", 80.5)				
				)
		.flatMap(producto -> {
			producto.setCreateAt(new Date());
			return dao.save(producto);
			})
		.subscribe(dato -> log.info("Insert into: " + dato.getId() + " " + dato.getNombre()));
	}

}
