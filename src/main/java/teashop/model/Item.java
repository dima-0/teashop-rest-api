package teashop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ordering_id")
	private Ordering ordering;
	
	@ManyToOne
	@JoinColumn(name="tea_id")
	private Tea tea;
	
	@Column
	private int quantity;
	
	
	public Item() {}
	
	public Item(int quantity) {
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public Ordering getOrdering() {
		return ordering;
	}

	public Tea getTea() {
		return tea;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrdering(Ordering ordering) {
		this.ordering = ordering;
	}

	public void setTea(Tea tea) {
		this.tea = tea;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
