package net.grexcraft.cloud.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pool")
public class Pool implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "max")
    private int max;

    @Column(name = "min")
    private int min;

//    @JoinColumn(name = "fk_image_default_id")
//    @ManyToOne
//    private Image defaultImage;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "pool")
    private Set<PoolSlot> slots;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, mappedBy = "pool")
    private Set<Server> servers;

    @Column(name = "fallback")
    private int fallback;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pool pool = (Pool) o;
        return max == pool.max && min == pool.min && fallback == pool.fallback && Objects.equals(id, pool.id) && Objects.equals(name, pool.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, max, min, fallback);
    }
}
