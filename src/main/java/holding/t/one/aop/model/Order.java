package holding.t.one.aop.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    private String description;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
