package polymorphism;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class TVUser {
	
	public static void main(String[] args) {
//		BeanFactory factory = new BeanFactory();
		// run as > run configurations... > arguments > lg or samsung
		// 클라이언트 소스를 수정하지 않고도 BeanFactory를 통해 실행되는 객체를 변경할 수 있음.
//		TV tv = (TV)factory.getBean(args[0]);
//		tv.powerOn();
//		tv.volumUp();
//		tv.volumDown();
//		tv.powerOff();
		
		// 1. Spring 컨테이너 구동.
		AbstractApplicationContext factory = new GenericXmlApplicationContext("applicationContext.xml");
		
		// 2. Spring 컨테이너로부터 필요한 객체를 요청(Lookup)한다.
		TV tv = (TV)factory.getBean("tv");
		tv.powerOn();
		tv.volumUp();
		tv.volumDown();
		tv.powerOff();
		
		// 3. Spring 컨테이너를 종료한다.
		factory.close();
		
		
	}

}
