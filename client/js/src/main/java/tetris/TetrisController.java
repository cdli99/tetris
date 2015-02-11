package tetris;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TetrisController {

    @RequestMapping("/")
    @ResponseBody
    public String helloWorld() {
	return "hello world";
    }

    @RequestMapping("/info")
    @ResponseBody
    public String info() {
	return String.format("<pre>%s\n\n%s</pre>", "The System properties:",
		getOSDetail());
    }

    private static String getOSDetail() {
	StringBuilder sb = new StringBuilder();
	Object[] keySet = System.getProperties().keySet().toArray();
	Arrays.sort(keySet);
	for (Object key : keySet) {
	    sb.append(key);
	    sb.append(" : ");
	    sb.append(System.getProperty((String) key));
	    sb.append("\n");
	}
	return sb.toString();
    }
}
