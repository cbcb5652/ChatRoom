package cat.function;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class test {
	public static void main(String[] args) throws UnknownHostException {
		System.out.println(InetAddress.getByName("LAPTOP-I838OGBK").getHostAddress());
	}
}
