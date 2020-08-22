package ar.com.ada.api.cursos.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.cursos.entities.*;
import ar.com.ada.api.cursos.models.request.InscripcionRequest;
import ar.com.ada.api.cursos.models.response.CursoEstudianteResponse;
import ar.com.ada.api.cursos.models.response.DocenteSimplificadoResponse;
import ar.com.ada.api.cursos.models.response.GenericResponse;
import ar.com.ada.api.cursos.services.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class EstudianteController {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    CursoService cursoService;

    @PostMapping("/api/estudiantes")
    @PreAuthorize("hasAuthority('CLAIM_userType_STAFF')")
    public ResponseEntity<GenericResponse> crearEstudiante(@RequestBody Estudiante estudiante) {

        if (estudianteService.estudianteExiste(estudiante)) {
            GenericResponse rError = new GenericResponse();
            rError.isOk = false;
            rError.message = "Este estudiante ya existe";

            return ResponseEntity.badRequest().body(rError);
        }

        estudianteService.crearEstudiante(estudiante);

        GenericResponse r = new GenericResponse();
        r.isOk = true;
        r.message = "Estudiante creada con exito";
        r.id = estudiante.getEstudianteId();

        return ResponseEntity.ok(r);

    }

    @GetMapping("/api/estudiantes/{id}")
    @PreAuthorize("hasAuthority('CLAIM_userType_STAFF') or (hasAuthority('CLAIM_userType_ESTUDIANTE') and hasAuthority('CLAIM_entityId_'+#id))")
    ResponseEntity<Estudiante> buscarPorIdEstudiante(@PathVariable Integer id) {

        Estudiante estudiante = estudianteService.buscarPorId(id);
        if (estudiante == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(estudiante);
    }

    @GetMapping("/api/estudiantes")
    @PreAuthorize("hasAuthority('CLAIM_userType_STAFF') or (hasAuthority('CLAIM_userType_ESTUDIANTE') and hasAuthority('CLAIM_entityId_'+#id))")
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        List<Estudiante> listaEstudiantes = estudianteService.listaEstudiantes();
        return ResponseEntity.ok(listaEstudiantes);
    }

    @GetMapping("/api/estudiantes/{id}/cursos")
    @PreAuthorize("hasAuthority('CLAIM_userType_STAFF') or (hasAuthority('CLAIM_userType_ESTUDIANTE') and hasAuthority('CLAIM_entityId_'+#id))")
    public ResponseEntity<List<CursoEstudianteResponse>> listaCursos(@PathVariable Integer id,
            @RequestParam(value = "disponibles", required = false) boolean disponibles) {
        List<Curso> listaCursos = new ArrayList<>();
        Estudiante estudiante = estudianteService.buscarPorId(id);
        if (disponibles) {
      
            listaCursos = cursoService.listaCursosDisponibles(estudiante);
        } else {
            listaCursos = estudiante.getCursosQueAsiste();
        }

        List<CursoEstudianteResponse> listaSimplificada = new ArrayList<>();

        for (Curso curso : listaCursos) {
            CursoEstudianteResponse nuevoCurso = new CursoEstudianteResponse();
            nuevoCurso.nombre = curso.getNombre();
            nuevoCurso.cursoId = curso.getCursoId();
            nuevoCurso.descripcion = curso.getDescripcion();
            nuevoCurso.categorias = curso.getCategorias();
            nuevoCurso.duracionHoras = curso.getDuracionHoras();

            for (Docente docente : curso.getDocentes()) {
                DocenteSimplificadoResponse dr = new DocenteSimplificadoResponse();
                dr.docenteId = docente.getDocenteId();
                dr.nombre = docente.getNombre();
                nuevoCurso.docentes.add(dr);
            }

            listaSimplificada.add(nuevoCurso);
        }

        return ResponseEntity.ok(listaSimplificada);

    }

    @PostMapping("/api/estudiantes/{id}/inscripciones")
    @PreAuthorize("hasAuthority('CLAIM_userType_ESTUDIANTE') and hasAuthority('CLAIM_entityId_'+#id)")
    public ResponseEntity<GenericResponse> inscribir(@PathVariable Integer id, @RequestBody InscripcionRequest iR) {

        Inscripcion inscripcionCreada = estudianteService.inscribir(id, iR.cursoId);

        GenericResponse gR = new GenericResponse();
        if (inscripcionCreada == null) {
            gR.isOk = false;
            gR.message = "La inscripcion no pudo ser realizada porque ya existe";
            return ResponseEntity.badRequest().body(gR);
        } else {
            gR.isOk = true;
            gR.message = "La inscripcion se realizo con exito";
            gR.id = inscripcionCreada.getInscripcionId();
            return ResponseEntity.ok(gR);
        }

    }

}