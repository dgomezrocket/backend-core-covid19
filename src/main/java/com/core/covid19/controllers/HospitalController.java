package com.core.covid19.controllers;

import com.core.covid19.models.entities.Hospital;
import com.core.covid19.models.requests.HospitalRequest;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.core.covid19.authentication.util.JwtUtil;
import com.core.covid19.models.responses.HospitalsResponse;
import com.core.covid19.services.HospitalService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {

	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping
	public List<Hospital> getAll(@RequestParam(value = "idDoctor", required = false) Integer idDoctor) {
		if (idDoctor != null)
			return hospitalService.getHospitalsByDoctor(idDoctor);
		return hospitalService.getAll();
	}

	@GetMapping("/{id}")
	public Hospital get(@PathVariable("id") Integer id) {
		return hospitalService.get(id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Integer id) {
		hospitalService.delete(id);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody HospitalRequest data) {
		try {
			hospitalService.save(data);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Hospital guardado exitosamente");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> error = new HashMap<>();
			error.put("error", "Error interno: " + e.getMessage());
			error.put("type", e.getClass().getName());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping(value="/my")
	public HospitalsResponse get(@RequestHeader("Authorization") String authorization){
		return hospitalService.getTenCloser(jwtUtil.getEmailFromJwtToken(authorization));
	}

	@PostMapping(value="/cargar")
	public void cargar(@RequestParam("file") MultipartFile file) throws IOException, InvalidFormatException {
		hospitalService.cargar(file);
	}
}