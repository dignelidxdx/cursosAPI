package ar.com.ada.api.cursos.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.cursos.entities.Estudiante;
import ar.com.ada.api.cursos.entities.Inscripcion;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    @Query("select CASE WHEN  count(e) > 0 THEN true ELSE false END from Estudiante e where e.paisId=:pais and e.tipoDocumentoId=:tipoDocuEnum and e.documento=:documento")
    boolean existsEstudiante(Integer pais, Integer tipoDocuEnum, String documento);

    @Query("select e from Estudiante e where e.paisId=:pais and e.tipoDocumentoId=:tipoDocuEnum and e.documento=:documento")
    Estudiante buscarEstudiantePorDocu(Integer pais, Integer tipoDocuEnum, String documento);

    @Query("select i from Inscripcion i where i.curso.cursoId=:cursoId and i.usuario.usuarioId=:usuarioId")
    Inscripcion buscarInscripcionSiExiste(Integer cursoId, Integer usuarioId);

    @Query("select CASE WHEN  count(i) > 0 THEN true ELSE false END from Inscripcion i where i.curso.cursoId=:cursoId and i.usuario.estudiante.estudianteId=:estudianteId")
    boolean existsInscripcion(Integer cursoId, Integer estudianteId);

}