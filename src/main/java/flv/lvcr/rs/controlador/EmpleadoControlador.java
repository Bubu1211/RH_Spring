package flv.lvcr.rs.controlador;

import flv.lvcr.rs.excepcion.RecursoNoEncontradoExcepcion;
import flv.lvcr.rs.modelo.Empleado;
import flv.lvcr.rs.servicio.IEmpleadoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 *https://localhost:8080/rh-app
 */
@RestController //Indica a la fabrica de spring que es un controaldor tipo REST
@RequestMapping("rh-app") //indica la ruta a la caul se le harán peticiones
@CrossOrigin(value = "http://localhost:3000") //´permite las peticiones desde ese puerto e ip
public class EmpleadoControlador {

    //Logger para enviar mensajes a consola, usa la configuración en logback
    private static final Logger logger =
            LoggerFactory.getLogger(EmpleadoControlador.class);

    @Autowired
    private IEmpleadoServicio empleadoServicio;
    //http://localhost:8000/rh-app/empleados
    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleados(){
        var empleados = empleadoServicio.listarEmpleados();
        empleados.forEach(empleado -> logger.info(empleado.toString()));
        return empleados;
    }

    @PostMapping("/empleados")
    public Empleado guardarEmpleado(@RequestBody Empleado empleado){
        logger.info("Empleado a agregar: " + empleado);
        return empleadoServicio.guardarEmpleado(empleado);
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado>
    getEmpleadoById(@PathVariable Integer id){
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            logger.info("empleado nulo al buscar por id"+id);
            throw new RecursoNoEncontradoExcepcion("No se encontro el id= "+id);
        }
        logger.info("Empleado recuperado con el id: "+id+empleado);
        return ResponseEntity.ok(empleado);
    }

    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado>
    modificarEmpleado(@PathVariable Integer id, @RequestBody Empleado empleado){
        Empleado e = empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            throw new RecursoNoEncontradoExcepcion("El id no se encuentra para modificar");
        }
        e.setNombre(empleado.getNombre());
        e.setDepartamento(empleado.getDepartamento());
        e.setSueldo(empleado.getSueldo());
        empleadoServicio.guardarEmpleado(e);
        return ResponseEntity.ok(e);
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String, Boolean>>
    borrarEmpleado(@PathVariable Integer id){
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado == null){
            throw new RecursoNoEncontradoExcepcion("El id para eliminar no existe : "+id);
        }
        empleadoServicio.eliminarEmpleado(empleado);
        //Json {"eliminado": "true"}
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }

}
