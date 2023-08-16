package me.ronygomes.identity_fort.command;

import javax.validation.constraints.Email;
import java.io.Serializable;

public class EmailCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email(message = "{error.invalid.email}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
