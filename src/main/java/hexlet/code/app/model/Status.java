package hexlet.code.app.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Getter
@Setter
@Table(name = "STATUSES")
@NoArgsConstructor
@AllArgsConstructor
public class Status {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Size(min = 1)
	private String name;

	@OneToOne(mappedBy = "status")
	private Task task;

	@CreationTimestamp
	@Temporal(TIMESTAMP)
	private Date createdAt;
}
