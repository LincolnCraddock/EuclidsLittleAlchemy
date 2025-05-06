package Model;
import java.awt.*;
import java.io.File;
import java.util.*;

import javax.print.attribute.standard.Media;

import Main.*;
import View.*;

public abstract class Model {
	
	public final Set<Element> knownElements;
	private View view;
	
	public Model(View view)
	{
		knownElements = new TreeSet<Element>(ElementFactory.PRIMARY_ELEMENTS);
		this.view = view;
		
		view.setModel(this);
		view.repopulateKnownElements(knownElements);
	}
	
	/**
	 * Returns the element that representing the result of combining two elements, and updates the known
	 * elements if the resulting element is new.
	 * 
	 * @param elem1 An element to combine.
	 * @param elem2 Another element to combine.
	 * @return The result of combining two elements, or null if the elements can't be combined.
	 */
	public Element combineElements(Element elem1, Element elem2)
	{
		Element result = ElementFactory.combine(elem1, elem2);
		if (result != null)
		{
			boolean isNewElem = knownElements.add(result);
			if (isNewElem)
			{
				view.repopulateKnownElements(knownElements);
			}
			
			if (knownElements.size() >= 10 && !knownElements.contains(ElementFactory.getElement("I.Post.5")))
			{
				knownElements.add(ElementFactory.getElement("I.Post.5"));
				view.repopulateKnownElements(knownElements);
			}
			else
			{System.out.println(knownElements);}
		}
		return result; // it's the controller's job to adjust the view depending on the result of the combination
		
	}

}
