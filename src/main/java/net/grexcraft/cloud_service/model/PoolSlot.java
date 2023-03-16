package net.grexcraft.cloud_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pool_slot")
public class PoolSlot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(nullable = false, name = "name") // like gungame1; lobby; bauserver
    private String name;

    @JoinColumn(nullable = false, name = "fk_pool_id")
    @ManyToOne
    private Pool pool;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "poolSlot")
    private Server server;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoolSlot poolSlot = (PoolSlot) o;
        return Objects.equals(id, poolSlot.id) && Objects.equals(name, poolSlot.name) && Objects.equals(pool, poolSlot.pool) && Objects.equals(server, poolSlot.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pool, server);
    }
}
