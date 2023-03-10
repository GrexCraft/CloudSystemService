package net.grexcraft.cloud_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.grexcraft.cloud_service.enums.ServerState;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "server")
public class Server implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @JoinColumn(name = "fk_image_id")
    @ManyToOne
    private Image image;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ServerState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(id, server.id) && Objects.equals(image, server.image) && Objects.equals(name, server.name) && Objects.equals(address, server.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, name, address);
    }
}
