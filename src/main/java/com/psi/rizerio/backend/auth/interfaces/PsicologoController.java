package com.psi.rizerio.backend.auth.interfaces;

import com.psi.rizerio.backend.auth.domain.Role;
import com.psi.rizerio.backend.auth.domain.User;
import com.psi.rizerio.backend.auth.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/psicologos")
@RequiredArgsConstructor
public class PsicologoController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> getAllPsicologos() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.PSYCHOLOGIST)
                .toList());
    }

    /**
     * Retorna um usuário por ID.
     * Usado pelo frontend em administracao.jsx e getPsicologoPorId().
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getPsicologoById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna o perfil do usuário autenticado atual.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createPsicologo(@RequestBody User request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword() != null && !request.getPassword().isBlank() ? request.getPassword() : "senha123"
                ))
                .role(Role.PSYCHOLOGIST)
                .crp(request.getCrp())
                .telefone(request.getTelefone())
                .photo(request.getPhoto())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updatePsicologo(@PathVariable UUID id, @RequestBody User request) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setCrp(request.getCrp());
        user.setTelefone(request.getTelefone());
        if (request.getPhoto() != null) {
            user.setPhoto(request.getPhoto());
        }
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        User user = userRepository.findById(id).orElseThrow();
        String senhaAtual = request.get("senha");
        String novaSenha = request.get("novaSenha");

        if (senhaAtual == null || novaSenha == null || !passwordEncoder.matches(senhaAtual, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        user.setPassword(passwordEncoder.encode(novaSenha));
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
