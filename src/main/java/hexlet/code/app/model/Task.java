package hexlet.code.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {


	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1)
	private String name;

	private String description;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@NotNull
	@ManyToOne
	private User author;

	@ManyToOne
	private User executor;

	@CreationTimestamp
	@Temporal(TIMESTAMP)
	private Date createdAt;
}
