package me.ronygomes.identity_fort.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken implements Serializable {

    public static enum TokenType {
        FORGET_PASSWORD, EMAIL_CONFIRMATION
    }

    private static final long serialVersionUID = 1L;

    private static final int VERIFICATION_TOKEN_EXPIRE_DURATION_IN_HOUR = 2;

    @Id
    @Column(length = 100, updatable = false, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date", nullable = false, updatable = false)
    private Date expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TokenType type;

    public VerificationToken() {
    }

    public VerificationToken(String token, User owner, TokenType type) {
        this.owner = owner;
        this.token = token;
        this.type = type;

        Calendar currentTime = Calendar.getInstance();
        currentTime.add(Calendar.HOUR, VERIFICATION_TOKEN_EXPIRE_DURATION_IN_HOUR);

        this.expiryDate = currentTime.getTime();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return getExpiryDate().before(new Date());
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EmailVerificationToken{" +
                "token='" + token + '\'' +
                ", owner=" + owner +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
