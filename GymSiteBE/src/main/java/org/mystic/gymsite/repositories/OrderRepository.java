package org.mystic.gymsite.repositories;

import org.mystic.gymsite.entities.Order;
import org.mystic.gymsite.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
