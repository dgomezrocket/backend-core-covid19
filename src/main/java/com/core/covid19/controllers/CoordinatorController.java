package com.core.covid19.controllers;

import com.core.covid19.models.entities.Person;
import com.core.covid19.models.requests.DoctorRequest;
import com.core.covid19.models.responses.PersonResponse;
import com.core.covid19.services.AccountService;
import com.core.covid19.services.CoordinatorService;
import com.core.covid19.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coordinators")
public class CoordinatorController {

    @Autowired
    private CoordinatorService service;

    @Autowired
    private PersonService personService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<PersonResponse> getCoordinators() {
        return service.getCoordinators();
    }

    @PostMapping
    public void update(@RequestBody DoctorRequest data) {
        Person p = new Person(data);
        personService.modify(data.getEmail(), p, data.getRoles());
    }

    @PostMapping("/new")
    public ResponseEntity<?> insert(@RequestBody DoctorRequest data) {
        try {
            // Validar campos requeridos
            if (data.getEmail() == null || data.getEmail().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El email es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (data.getPassword() == null || data.getPassword().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La contraseña es requerida");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (data.getDocument() == null || data.getDocument().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El documento es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (data.getName() == null || data.getName().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El nombre es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (data.getLastname() == null || data.getLastname().trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El apellido es requerido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            accountService.insertCoordinador(data);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Coordinador creado exitosamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage() != null ? e.getMessage() : "Error al crear coordinador");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}