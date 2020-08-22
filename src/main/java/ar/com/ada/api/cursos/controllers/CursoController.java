package ar.com.ada.api.cursos.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.cursos.models.request.CursoAsigDocRequest;
import ar.com.ada.api.cursos.models.request.CursoRequest;
import ar.com.ada.api.cursos.models.response.GenericResponse;
import ar.com.ada.api.cursos.services.CursoService;
import ar.com.ada.api.cursos.services.UsuarioService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import ar.com.ada.api.cursos.entities.*;
import ar.com.ada.api.cursos.entities.Usuario.TipoUsuarioEnum;

@RestController
public class CursoController {

  @Autowired
  CursoService cursoService;

  @Autowired
  UsuarioService usuarioService;

   // @PreAuthorize("@usuarioService.buscarPorUsername(principal.getUserName()).getTipoUsuarioId().getValue() == 3") //En este caso quiero que sea STAFF(3)
  @PostMapping("/api/cursos")
  public ResponseEntity<GenericResponse> crearCurso(Principal principal, @RequestBody CursoRequest cursoReq) {

    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());

    if(usuario.getTipoUsuarioId().equals(TipoUsuarioEnum.ESTUDIANTE)) {
        //chau chau y le damos un 403: Forbidden
        //Este le avismos que hay algo, pero no lo dejamos entrar
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        //en vez de tirar un 403, tiramos un 404(Not Found) y le mentimos.
        //en este caso ni siquiera le contamos qeu hay algo ahi como para que pueda
        // seguir intentando.
        //return ResponseEntity.notFound().build();
    }

    Curso cursoCreado = cursoService.crearCurso(cursoReq.nombre, cursoReq.categoriaId, cursoReq.duracionHoras,
        cursoReq.descripcion);

    if (cursoCreado == null)
      return ResponseEntity.badRequest().build();

    GenericResponse gR = new GenericResponse();
    gR.isOk = true;
    gR.message = "Curso creado con éxito";
    gR.id = cursoCreado.getCursoId();
    return ResponseEntity.ok(gR);


  }


  @GetMapping("/api/cursos")
  @PreAuthorize("@usuarioService.buscarPorUsername(principal.getUserName()).getTipoUsuarioId().getValue() == 3") 
  public ResponseEntity<List<Curso>> listaCursos(
      @RequestParam(value = "sinDocentes", required = false) boolean sinDocentes) {
    List<Curso> listaCursos = new ArrayList<>();
    if (sinDocentes) {

      listaCursos = cursoService.listaCursosSinDocentes();
    } else {
      listaCursos = cursoService.listaCursos();
    }

    return ResponseEntity.ok(listaCursos);

  }


  @PostMapping("/api/cursos/{cursoId}/docentes")
  @PreAuthorize("@usuarioService.buscarPorUsername(principal.getUserName()).getTipoUsuarioId().getValue() == 3") 
  public ResponseEntity<GenericResponse> asignarDocente(@PathVariable Integer cursoId,
      @RequestBody CursoAsigDocRequest cADR) {
    GenericResponse gR = new GenericResponse();
    if (cursoService.asignarDocente(cursoId, cADR.docenteId)) {

      gR.isOk = true;
      gR.message = "Docente asignado con éxito al Curso";
      return ResponseEntity.ok(gR);
    }
    gR.isOk = false;
    gR.message = "El Docente no pudo ser asignado.";
    return ResponseEntity.badRequest().body(gR);

  }

}