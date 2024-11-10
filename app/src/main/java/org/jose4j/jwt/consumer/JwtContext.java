package org.jose4j.jwt.consumer;

import java.util.List;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwx.JsonWebStructure;

/* loaded from: classes5.dex */
public class JwtContext {
    private List<JsonWebStructure> joseObjects;
    private String jwt;
    private JwtClaims jwtClaims;

    public JwtContext(JwtClaims jwtClaims, List<JsonWebStructure> joseObjects) {
        this.jwtClaims = jwtClaims;
        this.joseObjects = joseObjects;
    }

    public JwtContext(String jwt, JwtClaims jwtClaims, List<JsonWebStructure> joseObjects) {
        this.jwt = jwt;
        this.jwtClaims = jwtClaims;
        this.joseObjects = joseObjects;
    }

    public JwtClaims getJwtClaims() {
        return this.jwtClaims;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setJwtClaims(JwtClaims jwtClaims) {
        this.jwtClaims = jwtClaims;
    }

    public List<JsonWebStructure> getJoseObjects() {
        return this.joseObjects;
    }

    public String getJwt() {
        return this.jwt;
    }
}
