package org.zr.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {

    @GetMapping("/")
    public String publicPage() {
//        return "Hello, " + token.getName();
         return "index";
    }

     @GetMapping("/users")
     @PreAuthorize("hasRole('USER')")
     public String usersPage() {
         return "users";
     }

     @GetMapping("/admins")
     @PreAuthorize("hasRole('ADMIN')")
     public String adminsPage() {
         return "admins";
     }
}
