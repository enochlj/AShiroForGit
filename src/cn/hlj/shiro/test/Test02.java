package cn.hlj.shiro.test;

import org.junit.Test;

public class Test02 {

	@Test
	public void testA() {

		System.out.println("HELLO testA");

		System.out.println("HELLO testA - 11:18 FOR PUSH");

	}

	@Test
	public void testB() {
		System.out.println("HELLO testB");

		System.out.println("HELLO testB - 13:53 FOR PUSH");
		
		System.out.println("HELLO testB - 14:11 FOR PULL");
		
		System.out.println("HELLO testB - 14:12 FOR PUSH");
	}

}
