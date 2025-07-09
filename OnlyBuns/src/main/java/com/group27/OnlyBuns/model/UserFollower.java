package com.group27.OnlyBuns.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_followers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}))
public class UserFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "id", nullable = false)
    private User follower;  // korisnik koji prati

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", referencedColumnName = "id", nullable = false)
    private User followee;  // korisnik koji je praćen

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }

    public User getFollowee() { return followee; }
    public void setFollowee(User followee) { this.followee = followee; }

    // Override equals i hashCode - važno za kolekcije i da se izbegnu duplikati
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollower)) return false;
        UserFollower that = (UserFollower) o;
        return Objects.equals(follower, that.follower) &&
                Objects.equals(followee, that.followee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, followee);
    }
}
