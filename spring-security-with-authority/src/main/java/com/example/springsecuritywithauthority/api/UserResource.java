package com.example.springsecuritywithauthority.api;

import com.example.springsecuritywithauthority.user.RegisterRequest;
import com.example.springsecuritywithauthority.user.RegisterResponse;
import com.example.springsecuritywithauthority.user.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserResource {
  private final UserServiceImpl userService;

  public UserResource(UserServiceImpl userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
    return this.userService.register(registerRequest);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/admin-login")
  public ResponseEntity<String> adminLogin() {
    return this.userService.adminAuthorityAcceptOnly();
  }

  // @Secured("USER")//work with roles
  @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
  @GetMapping("/user-login")
  public ResponseEntity<String> userLogin() {
    return this.userService.userAuthorityAcceptOnly();
  }

  // @RolesAllowed("EDITOR")//work with roles
  @PreAuthorize("hasAnyAuthority('EDITOR','ADMIN')")
  @GetMapping("/editor-login")
  public ResponseEntity<String> editorLogin() {
    return this.userService.editorAuthorityAcceptOnly();
  }

  @GetMapping("/any-of-request-login")
  public ResponseEntity<String> anyOfAuthorityLogin() {
    return this.userService.acceptsAnyAuthority();
  }
}
