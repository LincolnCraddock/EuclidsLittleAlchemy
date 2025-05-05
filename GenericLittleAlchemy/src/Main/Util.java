package Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Util {

	public static <K, V> String prettyPrintMap(Map<K, V> map) {
	    StringBuilder sb = new StringBuilder();

	    for (Iterator<Map.Entry<K, V>> it = map.entrySet().iterator(); it.hasNext();) {
	        Map.Entry<K, V> entry = it.next();
	        sb.append(entry.getKey()).append("\n");

	        V value = entry.getValue();

	        if (value == null) {
	            sb.append(" └──── null\n");
	        } else if (value instanceof Collection<?> collection) {
	            List<?> valueList = new ArrayList<>(collection);
	            if (valueList.isEmpty()) {
	                sb.append(" └──── [])\n");
	            } else {
	                for (int i = 0; i < valueList.size(); i++) {
	                    String prefix = (i == valueList.size() - 1) ? "└──── " : "├──── ";
	                    sb.append("    ").append(prefix).append(valueList.get(i)).append("\n");
	                }
	            }
	        } else {
	            sb.append(" └──── ").append(value).append("\n");
	        }

	        if (it.hasNext()) {
	            sb.append("\n");
	        }
	    }

	    return sb.toString();
	}



	
}
