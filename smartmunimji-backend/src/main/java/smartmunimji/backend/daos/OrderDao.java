package smartmunimji.backend.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartmunimji.backend.entities.Order;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderIdAndDateOfPurchase(String orderId, LocalDate dateOfPurchase);
}