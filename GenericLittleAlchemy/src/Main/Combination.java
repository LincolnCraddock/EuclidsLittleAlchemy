package Main;
import java.util.Objects;

/**
 * Represents a combination of two elements. Essentially a set of size two.
 */
public class Combination {
	
	Element element1;
	Element element2;
	
	public Combination (Element e1, Element e2)
	{
		element1 = e1;
		element2 = e2;
	}
	
	public boolean contains(Element e)
	{
		return element1.equals(e) || element2.equals(e);
	}

	@Override
	public int hashCode() {
		return element1.hashCode() + element2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Combination other = (Combination) obj;
		return (element1.id.equals(other.element1.id) && element2.id.equals(other.element2.id)) || (element1.id.equals(other.element2.id)&& element2.id.equals(other.element1.id));
		//return (Objects.equals(element1, other.element1) && Objects.equals(element2, other.element2)) || (Objects.equals(element1, other.element2) && Objects.equals(element2, other.element1));
	}

	@Override
	public String toString() {
		return "Combination [" + element1 + " + " + element2 + "]";
	}

}
