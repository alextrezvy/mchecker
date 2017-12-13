package my.app.mchecker;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A console application which is able to check the state of other daemons.
 * Returns 0 if OK, or the number of broken daemon otherwise.  
 */
public class MChecker {
	private static final Logger log = Logger.getLogger(MChecker.class.getName());

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/context.xml");
		Map<String, Checker> beans = ctx.getBeansOfType(Checker.class);
		int error = 0;
		int count = 0;
		for (Map.Entry<String, Checker> entry : beans.entrySet()) {
			log.log(Level.INFO, "Start to check {0}", entry.getKey());
			count++;
			if (!entry.getValue().check()) {
				log.log(Level.INFO, "Check failed at checker {0}", count);
				error = count;
				break;
			}
		}
		log.log(Level.INFO, "Exiting with error code: {0}", error);
		System.exit(error);
	}

}
