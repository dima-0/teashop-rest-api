package teashop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column
	private String firstname;
	@Column
	private String secondname;
	@Column
	private int age;
	@Column
	private String email;
	
	@OneToMany(mappedBy="customer", orphanRemoval=true, fetch=FetchType.EAGER)
	private List<Ordering> orderings = new ArrayList<>();
	
	public Customer() {}
	
	public Customer(String firstname, String secondname, int age, String email) {
		this.firstname = firstname;
		this.secondname = secondname;
		this.age = age;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getSecondname() {
		return secondname;
	}

	public int getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}

	public List<Ordering> getOrderings() {
		return orderings;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOrderings(List<Ordering> orderings) {
		this.orderings = orderings;
	}
	
	public boolean removeOrderingById(long orderingId) {
		return getOrderings().removeIf(o -> o.getId() == orderingId);
	}
}
