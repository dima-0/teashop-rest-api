package teashop.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement
@Entity
public class Ordering {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)

	private long id;
    
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="customer_id")
	private Customer customer;
	
    private Date orderingDate;
	
	@OneToMany(mappedBy="ordering", orphanRemoval=true)
	private List<Item> items = new ArrayList<>();

	public Ordering() {}

	public Ordering(Date orderingDate) {
		this.orderingDate = orderingDate;
	}

	public long getId() {
		return id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Date getOrderingDate() {
		return orderingDate;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setOrderingDate(Date orderingDate) {
		this.orderingDate = orderingDate;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public boolean removeItemById(long itemId) {
		return getItems().removeIf(i -> i.getId() == itemId);
	}
}
