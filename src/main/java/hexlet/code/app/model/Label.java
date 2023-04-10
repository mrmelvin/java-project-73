package hexlet.code.app.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "LABELS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Label {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Size(min = 1)
	private String name;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Task> tasks;

	@CreationTimestamp
	@Temporal(TIMESTAMP)
	private Date createdAt;

	public Label(Long id) {
		this.id = id;
	}
}
