package polymorphism;

public class BeanFactory {
	
	public Object getBean(String beanName) {
		if(beanName.equals("samsung") || beanName.equals("Samsung")) {
			return new SamsungTV();
		}
		else if(beanName.equals("Lg") || beanName.equals("lg") || beanName.equals("LG")) {
			return new LgTV();
		}
		return null;
	}

}
