package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Piro.
 */
@Entity
@Table(name = "piro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Piro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "s_3_urltovideo")
    private String s3urltovideo;

    @Column(name = "created")
    private LocalDate created;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "piros")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "owner", "piros" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Piro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Piro title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Piro description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String gets3urltovideo() {
        return this.s3urltovideo;
    }

    public Piro s3urltovideo(String s3urltovideo) {
        this.sets3urltovideo(s3urltovideo);
        return this;
    }

    public void sets3urltovideo(String s3urltovideo) {
        this.s3urltovideo = s3urltovideo;
    }

    public LocalDate getCreated() {
        return this.created;
    }

    public Piro created(LocalDate created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Piro owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.removePiros(this));
        }
        if (tags != null) {
            tags.forEach(i -> i.addPiros(this));
        }
        this.tags = tags;
    }

    public Piro tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Piro addTags(Tag tag) {
        this.tags.add(tag);
        tag.getPiros().add(this);
        return this;
    }

    public Piro removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getPiros().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piro)) {
            return false;
        }
        return getId() != null && getId().equals(((Piro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Piro{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", s3urltovideo='" + gets3urltovideo() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
