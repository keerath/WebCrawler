import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class TestAnchor {

	
	public static void main(String args[]) throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		MailDownloader m = new MailDownloader();
		m.findAnchors("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread?1");
	}
}
