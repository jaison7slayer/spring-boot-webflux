package com.example.springboot.webflux.app.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.springboot.webflux.app.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

}
