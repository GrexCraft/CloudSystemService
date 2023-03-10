package net.grexcraft.cloud_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image_mount")
public class ImageMount {
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
}
