package Main;
import java.io.*;
import java.text.ParseException;
import java.util.*;
// TODO: allow double-quotes to wrap arguments in the files
/**
 * A factory for creating Element objects by combining two existing ones. Also reads the files delineating the possible elements, element combinations, and how certain elements become unlocked.
 */
public final class ElementFactory {
	
	/** A map used to look up element objects based on their names. */
	private static final Map<String, Element> ELEMENT_LOOKUP;
	
	/** A set of all of the possible elements. */
	private static final Set<Element> POSSIBLE_ELEMENTS;
	
	/** A map mapping combinations of elements to the elements they create. */
	private static final Map<Combination, Element> ELEMENT_COMBINATIONS;
	
	/** A set of all of the primary elements (e.i. the ones the player starts with at the beginning of the game). */
	public static final Set<Element> PRIMARY_ELEMENTS;
	
	/** A map mapping certain elements to the number of discovered elements needed to unlock them. */
	private static final Map<Element, Integer> REQUIRED_NUMBER_DISCOVERED;
	
	/** A map mapping certain elements to which elements need to be discovered to unlock them. */
	private static final Map<Element, Set<Element>> REQUIRED_ELEMENTS_DISCOVERED;
	
	static
	{
		try {
			ELEMENT_LOOKUP = Map.copyOf(loadPossibleElements());
		} catch (ParseException e) {
			throw new ExceptionInInitializerError(e);
		}
		
		POSSIBLE_ELEMENTS = Set.copyOf(ELEMENT_LOOKUP.values());
		
		try {
			ELEMENT_COMBINATIONS = Map.copyOf(loadElementCombinations());
		} catch (ParseException e) {
			throw new ExceptionInInitializerError(e);
		}
		
		Set<Element> primaryElem = new HashSet<>();
		Map<Element, Integer> reqNumDiscovered = new HashMap<>();
		Map<Element, Set<Element>> reqElemDiscovered = new HashMap<>();
		try {
			loadUnlockModes (primaryElem, reqNumDiscovered, reqElemDiscovered);
		} catch (ParseException e) {
			throw new ExceptionInInitializerError(e);
		}
		
		PRIMARY_ELEMENTS = Set.copyOf(primaryElem);
		
		REQUIRED_NUMBER_DISCOVERED = Map.copyOf(reqNumDiscovered);
		
		REQUIRED_ELEMENTS_DISCOVERED = Map.copyOf(reqElemDiscovered);
		
		
//		System.out.println(Util.prettyPrintMap(ELEMENT_LOOKUP));
//		System.out.println(POSSIBLE_ELEMENTS);
//		System.out.println(Util.prettyPrintMap(ELEMENT_COMBINATIONS));
//		System.out.println(PRIMARY_ELEMENTS);
//		System.out.println(Util.prettyPrintMap(REQUIRED_NUMBER_DISCOVERED));
//		System.out.println(Util.prettyPrintMap(REQUIRED_ELEMENTS_DISCOVERED));
	}
	
	private static final String POSSIBLE_ELEMENTS_FILEPATH = "data/Element Data/Possible Elements.txt";
	private static final String ELEMENT_COMBINATIONS_FILEPATH = "data/Element Data/Element Combinations.txt";
	private static final String UNLOCK_MODES_FILEPATH = "data/Element Data/Unlock Modes.txt";
	
	private static final String PRIMARY_SYMBOL = "p";
	
	private ElementFactory() {}
	
	/**
	 * A pretty trivial subclass of Element so that it can be instantiated.
	 */
	private static class ConcreteElement extends Element {
		
		public ConcreteElement(String id, int order) {
			super(id, order);
		}
		
		public ConcreteElement(String id, String name, int order) {
			super(id, name, order);
		}
		
		public ConcreteElement(String id, String name, String description, int order) {
			super(id, name, description, order);
		}
		
		public ConcreteElement(String id, String name, String description, String iconFilepath, int order) {
			super(id, name, description, iconFilepath, order);
		}
		
	}
	
	/**
	 * Creates an element representing the result of combining two elements.
	 * 
	 * @param elem1 An element to combine.
	 * @param elem2 Another element to combine.
	 * @return The result of combining two elements, or null if the elements can't be combined.
	 */
	public static Element combine(Element elem1, Element elem2)
	{
		var combo = new Combination (elem1, elem2);
		return ELEMENT_COMBINATIONS.get(combo);
	}
	
	/**
	 * Gets an element given its name.
	 * 
	 * @param name The name of the element.
	 * @return The element with that name.
	 */
	public static Element getElement(String name)
	{
		return ELEMENT_LOOKUP.get(name);
	}
	
	/**
	 * Checks whether an element exists given its name.
	 * 
	 * @param name The name of the element.
	 * @return True if the element exists, false otherwise.
	 */
	public static boolean elementExists(String name)
	{
		return ELEMENT_LOOKUP.containsKey(name);
	}
	
	/**
	 * Reads the text file of all of the possible elements, and converts it into a map.
	 * 
	 * @return A map mapping the names of all of the possible elements that can be discovered to their corresponding objects.
	 * @throws ParseException 
	 */
	private static Map<String, Element> loadPossibleElements() throws ParseException
	{
		Map<String, Element> s = new HashMap<> ();
		File file = new File (POSSIBLE_ELEMENTS_FILEPATH);
		try
		{
			Scanner sc = new Scanner (file);
			int lineNum = 1;
			while (sc.hasNext())
			{
				// try to add the element to the set
				String line = sc.nextLine();
				String[] elemData;
				try
				{
					elemData = splitData(line);
				}
				catch (IllegalArgumentException e)
				{
					throw new ParseException(e.getMessage(), lineNum);
				}
				Element elem;
				if (elemData.length == 1)
					elem = new ConcreteElement(elemData[0], lineNum);
				else if (elemData.length == 2)
					elem = new ConcreteElement(elemData[0], elemData[1], lineNum);
				else if (elemData.length == 3)
					elem = new ConcreteElement(elemData[0], elemData[1], elemData[2], lineNum);
				else if (elemData.length == 4)
					elem = new ConcreteElement(elemData[0], elemData[1], elemData[2], elemData[3], lineNum);
				else if (elemData.length == 0)
					continue; // skip blank lines
				else
					throw new ParseException ("The entry at line " + lineNum + " in " + POSSIBLE_ELEMENTS_FILEPATH + " has has too many arguments.\n"
							+ "Don't forget to surround names with spaces in them with double-quotes.", lineNum);
				Element result = s.put(elemData[0], elem);
				if (result != null)
					throw new IllegalArgumentException ("The element " + elemData[0] + "is listed twice in " + POSSIBLE_ELEMENTS_FILEPATH);
				
				++lineNum;
			}
			sc.close();
			return s;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Reads a text file of all of the ways elements can combine to make new elements, and converts it into a map.
	 * 
	 * @return A map mapping combinations of elements to the elements they create.
Z	 
	 * @throws ParseException */
	private static Map<Combination, Element> loadElementCombinations() throws ParseException
	{
		Map<Combination, Element> m = new HashMap<> ();
		File file = new File (ELEMENT_COMBINATIONS_FILEPATH);
		try
		{
			Scanner sc = new Scanner (file);
			int lineNum = 1;
			while (sc.hasNext())
			{
				// parse the line
				String line = sc.nextLine();
				String[] comboData;
				try
				{
					comboData = splitData(line);
				}
				catch (IllegalArgumentException e)
				{
					throw new ParseException(e.getMessage(), lineNum);
				}
				if (comboData.length != 5)
				{
					String conditionalMsg = "";
					if (comboData.length > 5)
						conditionalMsg = ".\nDon't forget to surround names with spaces in them with double-quotes."; 
					throw new ParseException ("The entry at line " + lineNum + " in " + ELEMENT_COMBINATIONS_FILEPATH + " must be in the format element_1_id + element_2_id = element_result_id" + conditionalMsg, lineNum);
				}
				if (!comboData[1].equals("+") || !comboData[3].equals("="))
					throw new ParseException ("The entry at line " + lineNum + " in " + ELEMENT_COMBINATIONS_FILEPATH + " must be in the format element_1_id + element_2_id = element_result_id.", lineNum);
				
				String elem1Name = comboData[0];
				String elem2Name = comboData[2];
				String elemResultName = comboData[4];
				
				// try to add the combination to the map
				Element elem1 = getElement(elem1Name);
				if (elem1 == null)
					throw new IllegalArgumentException (elem1Name + " is listed in " + ELEMENT_COMBINATIONS_FILEPATH + " but not in " + POSSIBLE_ELEMENTS_FILEPATH);
				Element elem2 = getElement(elem2Name);
				if (elem2 == null)
					throw new IllegalArgumentException (elem2Name + " is listed in " + ELEMENT_COMBINATIONS_FILEPATH + " but not in " + POSSIBLE_ELEMENTS_FILEPATH);
				Element elemResult = getElement(elemResultName);
				if (elemResult == null)
					throw new IllegalArgumentException (elemResultName + " is listed in " + ELEMENT_COMBINATIONS_FILEPATH + " but not in " + POSSIBLE_ELEMENTS_FILEPATH);
				var combo = new Combination(elem1, elem2);
				Element result = m.put(combo, elemResult);
				if (result != null)
					throw new IllegalArgumentException ("The combination " + combo + " has two possible results in " + ELEMENT_COMBINATIONS_FILEPATH + ", but it should only have one");
				
				++lineNum;
			}
			sc.close();
			return m;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	/**
	 * Reads a text file of when certain elements should be unlocked, and converts that information into several sets and maps.
	 * 
	 * @param primaryElem A modifiable set to be populated with all of the primary elements.
	 * @param reqNumDiscovered A modifiable map to be populated with the number of elements that need to be discovered for certain elements to be unlocked.
	 * @param reqElemDiscovered A modifiable map to be populated with all of the elements that need to be discovered for certain elements to be unlocked.
	 * @throws ParseException 
	 */
	private static void loadUnlockModes(Set<Element> primaryElem, Map<Element, Integer> reqNumDiscovered, Map<Element, Set<Element>> reqElemDiscovered) throws ParseException
	{
		primaryElem.clear();
		reqNumDiscovered.clear();
		reqElemDiscovered.clear();
		File file = new File(UNLOCK_MODES_FILEPATH);
		try
		{
			Scanner sc = new Scanner (file);
			int lineNum = 1;
			while (sc.hasNext())
			{
				// parse the line
				String line = sc.nextLine();
				String[] unlockData;
				try
				{
					unlockData = splitData(line);
				}
				catch (IllegalArgumentException e)
				{
					throw new ParseException(e.getMessage(), lineNum);
				}
				if (unlockData.length < 3 || !unlockData[1].equals(":"))
					throw new ParseException("The entry at line " + lineNum + " in " + UNLOCK_MODES_FILEPATH + " must be in the format element_1_id : p or element_1_id : req_num_elements req_element_2_id req_element_3_id ...", lineNum);
				String elemId = unlockData[0];
				Element elem = ElementFactory.getElement(elemId);
				if (elem == null)
					throw new IllegalArgumentException (elemId + " is listed in " + UNLOCK_MODES_FILEPATH + " but not in " + ElementFactory.POSSIBLE_ELEMENTS_FILEPATH);
				var result1 = reqElemDiscovered.put(elem, new HashSet<Element>());
				var result2 = reqNumDiscovered.put(elem, 0);
				if (result1 != null || result2 != null || primaryElem.contains(elem))
					throw new IllegalArgumentException ("Two ways to unlock " + elemId + " are listed in " + UNLOCK_MODES_FILEPATH);
				
				// iterate over the requirements for unlocking the element
				if (unlockData[2].equals(PRIMARY_SYMBOL))
				{
					primaryElem.add(elem);
				}
				else
				{
					if (primaryElem.contains(elem))
					{
						String conditionalMsg = "";
						if (ElementFactory.elementExists(PRIMARY_SYMBOL))
							conditionalMsg = ".\nIf you want " + elemId + " to be unlocked after the element \"" + PRIMARY_SYMBOL + "\" is discovered, you may want to give " + PRIMARY_SYMBOL + " a different id."; 
						
						throw new IllegalArgumentException (elemId + " can't be a primary element and have an unlock mode" + conditionalMsg);
					}
					
					int i = 2;
					try
					{
						// try to parse the last element as a number
						int reqNum = Integer.parseInt(unlockData[i]);
						if (reqNum < 0)
						{
							String conditionalMsg = "";
							if (ElementFactory.elementExists(unlockData[i]))
								conditionalMsg = ".\nIf you want " + elemId + " to be unlocked after the element \"" + unlockData[i] + "\" is discovered, you may want to give " + unlockData[i] + " a different id."; 
							
							throw new IllegalArgumentException ("The number of discovered elements required to unlock " + elemId + " should be a positive integer" + conditionalMsg);
						}
						reqNumDiscovered.put(elem, reqNum);
						
						++i;
					}
					catch (NumberFormatException e)
					{ }
					
					for (; i < unlockData.length; ++i)
					{
						Element reqElem = ElementFactory.getElement(elemId);
						if (reqElem == null)
							throw new IllegalArgumentException (unlockData[i] + " is listed in " + UNLOCK_MODES_FILEPATH + " but not in " + ElementFactory.POSSIBLE_ELEMENTS_FILEPATH);
						
						// add it to the set of required elements
						reqElemDiscovered.get(elem).add(reqElem);
					}
				}
				
				++lineNum;
			}
			sc.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static String[] splitData(String line)
	{	
		List<String> elemData = new ArrayList<String>();
		
		boolean quote = false;
		String token = "";
		for (char c : line.toCharArray())
		{
			if (c == '\"')
			{
				quote = !quote;
				if (!quote || token.length() > 0) // empty strings are allowed if surrounded by quotes
				{
					elemData.add(token);
					token = "";
				}
			}
			else if (c == ' ' && !quote)
			{
				// extra whitespace is ignored
				if (token.length() > 0)
					elemData.add(token);
					
				token = "";
			}
			else
			{
				token += c;
			}
		}
		if (token.length() > 0)
			elemData.add(token);

		if (quote)
			throw new IllegalArgumentException ("Every quote should have an end-quote.");

		return elemData.toArray(String[]::new);
	}

}
