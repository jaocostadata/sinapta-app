package br.com.sinapta.ecossistema.auth;

public record LoginResponse(String token, String name, String email, Role role) {
}
