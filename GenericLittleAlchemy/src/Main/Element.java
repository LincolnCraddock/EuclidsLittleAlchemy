package Main;
import java.util.Objects;

/**
 * Represents an element that the player can discover.
 * 
 * Element is an abstract class so that only the ElementFactory can provide instances of it.
 */
public abstract class Element implements Comparable<Element> {
	
	public final String id;
	public final String name;
	public final String description;
	public final String iconFilepath;
	public final int order;
	
	public Element (String id, int order)
	{
		this(id, id, order);
	}
	
	public Element (String id, String name, int order)
	{
		this(id, name, "", order);
	}
	
	public Element (String id, String name, String description, int order)
	{
		this(id, name, description, "", order);
	}
	
	public Element (String id, String name, String description, String iconFilepath, int order)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.iconFilepath = iconFilepath;
		this.order = order;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		return Objects.equals(name, other.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
	@Override
	public String toString()
	{
		return id;
	}
	
	@Override
	public int compareTo(Element elem)
	{
		return order - elem.order;
	}

}
