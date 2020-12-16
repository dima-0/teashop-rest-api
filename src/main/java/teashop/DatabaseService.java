package teashop;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import teashop.model.Customer;
import teashop.model.Item;
import teashop.model.Ordering;
import teashop.model.Tea;

public class DatabaseService {
	private static String PERSISTENCE_UNIT = "mariadb-localhost";
	private static EntityManagerFactory emfactory = null;
	
	public static void initEmFactory() {
		if(emfactory == null) {
			emfactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
			EntityManager em = emfactory.createEntityManager();
			fillDatabase(em);
			em.close();
		}
	}
	private static EntityManagerFactory getEmFactoryInstance() {
		if(emfactory == null) initEmFactory();
		return emfactory;
	}
	
	private static void fillDatabase(EntityManager em) {
		em.getTransaction().begin();
		List<Tea> teas = Arrays.asList(
				 new Tea("Karamell", null, 2.50, "EURO", 0.5, "Gramm", 5),
				 new Tea("Schoko", null, 3.20, "EURO", 0.75, "Gramm", 5),
				 new Tea("Orange", null, 2.30, "EURO", 0.4, "Gramm", 5),
				 new Tea("Erdbeere", null, 3, "EURO", 0.8, "Gramm", 5)
				 );
		 teas.forEach(t -> em.persist(t));
		 List<Customer> customers = Arrays.asList(
				 new Customer("Max", "Mustermann", 25, "max@web.com"),
				 new Customer("Maxi", "Musterfrau", 27, "maxi@beispiel.com"),
				 new Customer("Thomas", "Beispiel", 19, "thomas@web.com"),
				 new Customer("Tobi", "Beispielmann", 25, "tobi.beispiel@web.com")
				 );
		 customers.forEach(c -> {
			 for(int i = 0; i < 2; i++) {
				 Ordering o = new Ordering(new Date(new GregorianCalendar(
					 2017 + new Random().nextInt(3), 
					 0 + new Random().nextInt(12),
					 1 + new Random().nextInt(27)).getTimeInMillis()));
				 o.setCustomer(c);
				 c.getOrderings().add(o);
				 em.persist(o);
				 for(int j = 0; j < 2; j++) {
					 Item item = new Item(2);
					 item.setOrdering(o);
					 item.setTea(teas.get(new Random().nextInt(teas.size())));
					 em.persist(item);
					 o.getItems().add(item);
				 }
			 }
			 em.persist(c);
		 });
		 em.getTransaction().commit(); 
	}
	
	public static Tea getTeaById(long teaId) {
		EntityManager em = getEmFactoryInstance().createEntityManager();
		Tea s = em.find(Tea.class, teaId);
		em.close();
		return s;
	}
	public static List<Tea> getAllTeas(){
		EntityManager em = getEmFactoryInstance().createEntityManager();
		String statement = "SELECT t FROM Tea t";
		TypedQuery<Tea> q = em.createQuery(statement, Tea.class);
		List<Tea> teas = q.getResultList();
		em.close();
		return teas;
	}
	
	public static List<Item> getAllItemsByTeaId(long teaId){
		EntityManager em = getEmFactoryInstance().createEntityManager();
		String statement = "SELECT i FROM Item i WHERE i.tea.id = :target_tea_id";
		TypedQuery<Item> q = em.createQuery(statement, Item.class).setParameter("target_tea_id", teaId);
		List<Item> items = q.getResultList();
		em.close();
		return items;
	}
	
	public static boolean removeTeaById(long teaId) {
		EntityManager em = getEmFactoryInstance().createEntityManager();
		Optional<Tea> optTea = Optional.ofNullable(em.find(Tea.class, teaId));
		boolean removed = false;
		if(optTea.isPresent()) {
			// Get all items where teaId is referenced.
			List<Item> items = getAllItemsByTeaId(teaId);
			try {
				em.getTransaction().begin();
				items.forEach(i -> {
					i.setTea(null);
					em.merge(i);
				});
				em.remove(optTea.get());
				em.getTransaction().commit();
				removed = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		em.close();
		return removed;
	}
	
	public static long addTea(Tea nTea) {
		EntityManager em = getEmFactoryInstance().createEntityManager();
		long id = -1;
		try {
			em.getTransaction().begin();
			em.persist(nTea);
			em.getTransaction().commit();
			id = nTea.getId();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return id;
	}
	
	public static boolean updateTea(Tea tea) {
		EntityManager em = getEmFactoryInstance().createEntityManager();
		boolean updated = false;
		try {
			em.getTransaction().begin();
			em.merge(tea);
			em.getTransaction().commit();
			updated = true;
		} catch (Exception e) {
			e.printStackTrace();	
		} finally {
			em.close();
		}
		return updated;
	}
	
	public static List<Customer> getAllCustomers(){
		EntityManager em = getEmFactoryInstance().createEntityManager();
		String statement = "SELECT c FROM Customer c";
		TypedQuery<Customer> q = em.createQuery(statement, Customer.class);
		List<Customer> customers = q.getResultList();
		em.close();
		return customers;
	}
	
	public static Customer getCustomerById(long customerId) {
		EntityManager em = getEmFactoryInstance().createEntityManager();
		Customer customer = em.find(Customer.class, customerId);
		em.close();
		return customer;
	}
	
	public static boolean updateCustomer(Customer customer) {
		customer.getOrderings().forEach((o) -> {
			o.setCustomer(customer);
			o.getItems().forEach((i) -> i.setOrdering(o));
		});
		EntityManager em = getEmFactoryInstance().createEntityManager();
		boolean updated = false;
		try {
			em.getTransaction().begin();
			em.merge(customer);
			em.getTransaction().commit();
			updated = true;
		} catch (Exception e) {
			e.printStackTrace();	
		} finally {
			em.close();
		}
		return updated;
	}
	
	public static boolean removeOrderingById(long customerId, long orderingId) {
		EntityManager em = getEmFactoryInstance().createEntityManager();
		Optional<Customer> optCustomer = Optional.ofNullable(em.find(Customer.class, customerId));
		boolean removed = false;
		if(optCustomer.isPresent()) {
			Customer customer = optCustomer.get();
			removed = customer.removeOrderingById(orderingId);
			try {
				em.getTransaction().begin();
				em.merge(customer);
				em.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				removed = false;
			}
		}
		em.close();
		return removed;
	}
	
	public static long addCustomer(Customer customer) {
		// Make sure the customer does not have any orderings.
		customer.setOrderings(new ArrayList<>());
		EntityManager em = getEmFactoryInstance().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();	
		} finally {
			em.close();
		}
		return customer.getId();
	}
}
