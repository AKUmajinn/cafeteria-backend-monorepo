package com.cafeteria.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.cafeteria.auth.model.Usuario;
import com.cafeteria.auth.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;

@RestController 
@RequestMapping("/auth")
public class AuthController {
    @Autowired private UsuarioRepository repo;
    @Autowired private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginReq) {
        return repo.findByUsername(loginReq.getUsername())
            .filter(u -> encoder.matches(loginReq.getPassword(), u.getPassword()))
            .map(u -> {
                Map<String, String> response = new HashMap<>();
                response.put("token", "TOKEN_GENERADO_" + u.getRol());
                response.put("role", u.getRol());
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario u) {
        u.setPassword(encoder.encode(u.getPassword()));
        u.setRol("ROLE_USER");
        repo.save(u);
        return ResponseEntity.ok().build();
    }
}