package org.mystic.gymsite.repositories;

import org.mystic.gymsite.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
