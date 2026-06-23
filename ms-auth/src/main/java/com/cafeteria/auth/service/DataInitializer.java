package com.cafeteria.auth.service;

import com.cafeteria.auth.model.Usuario;
import com.cafeteria.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== VERIFICANDO USUARIO ADMINISTRADOR ===");
        

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            
         
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            
       
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");

         
            usuarioRepository.save(admin);
            System.out.println("✅ ÉXITO: Usuario administrador por defecto creado (admin / admin123).");
        } else {
            System.out.println("ℹ️ El usuario 'admin' ya existe. No se realizaron cambios.");
        }
        System.out.println("=========================================");
    }
}