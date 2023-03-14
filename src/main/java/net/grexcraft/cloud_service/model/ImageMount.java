package net.grexcraft.cloud_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image_mount")
public class ImageMount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(name = "path_local")
    private String pathLocal;

    @Column(name = "path_container")
    private String pathContainer;

    @ManyToOne
    @JoinColumn(name = "fk_image_id")
    private Image image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageMount that = (ImageMount) o;
        return Objects.equals(id, that.id) && Objects.equals(pathLocal, that.pathLocal) && Objects.equals(pathContainer, that.pathContainer) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pathLocal, pathContainer, image);
    }
}