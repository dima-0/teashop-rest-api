package teashop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Tea {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(length = 40)
	private String name;
	@Column
	private String description;
	@Column
	private double price;
	@Column
	private String currency;
	@Column
	private double weight;
	@Column
	private String weightUnit;
	@Column
	private int stock = 0;

	public Tea() {
	}

	public Tea(String name, String description, double price, String currency, double weight, String weightUnit,
			int stock) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.currency = currency;
		this.weight = weight;
		this.weightUnit = weightUnit;
		this.stock = stock;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public String getCurrency() {
		return currency;
	}

	public double getWeight() {
		return weight;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public int getStock() {
		return stock;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
