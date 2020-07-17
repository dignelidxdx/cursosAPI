package ar.com.ada.api.cursos.entities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inscripcion_id")
    private Integer inscripcionId;
    @ManyToOne // ManyToOne lleva JoinColumn
    @JoinColumn(name = "curso_id", referencedColumnName = "curso_id")
    private Curso curso;
    private Date fecha;
    // el @column va sobre tipos de datos Integer, String, Date, primitivos o enum.
    // No sobre otros objetos.
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    private Usuario usuario;
    @Column(name = "estado_inscripcion_id")
    private EstadoInscripcionEnum estadoInscripcionId;

    public enum EstadoInscripcionEnum {
        INACTIVO(0), ACTIVO(1);

        private final Integer value;

        // NOTE: Enum constructor tiene que estar en privado
        private EstadoInscripcionEnum(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public static EstadoInscripcionEnum parse(Integer id) {
            EstadoInscripcionEnum status = null; // Default
            for (EstadoInscripcionEnum item : EstadoInscripcionEnum.values()) {
                if (item.getValue().equals(id)) {
                    status = item;
                    break;
                }
            }
            return status;
        }
    }

}