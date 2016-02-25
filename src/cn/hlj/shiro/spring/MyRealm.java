package cn.hlj.shiro.spring;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class MyRealm extends AuthorizingRealm {

	/**
	 * @return 通常使用 SimpleAuthorizationInfo 作为返回值的实现类
	 * @param principals: 可以获取用户的登录信息. 即可以获取 doGetAuthenticationInfo 方法的返回值的 principal
	 */
	// 进行授权的方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("doGetAuthorizationInfo : " + principals);

		// 获取用户的登录信息
		Object principal = principals.getPrimaryPrincipal();

		System.out.println("依据 principal: " + principal + " 来获取当前用户所具有的权限. ");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		info.addRole("user");
		if ("admin".equals(principal)) {
			info.addRole("admin");
		}
		return info;
	}

	// 进行认证的方法
	/**
	 * @return: 主要使用 SimpleAuthenticationInfo 作为实现类
	 * @param token:
	 *            即为 handler 中调用 Subject 的 login 方法传入的 UsernamePasswordToken
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo : " + token.hashCode());

		// 1. 进行强制的类型转换
		UsernamePasswordToken upt = (UsernamePasswordToken) token;

		// 2. 获取用户名
		String username = upt.getUsername();
		if ("uaeUser".equals(username)) {
			throw new UnknownAccountException();
		}
		if ("laeUser".equals(username)) {
			throw new LockedAccountException();
		}

		// 3. 依据用户名从数据库中获取用户信息
		System.out.println("依据用户名 ： " + username + "获取用户信息。");

		// 4. 创建 AuthenticationInfo 实例
		// principal : 登录信息。也可以是对象类型
		Object principal = username;
		// credentials: 凭证. 即第 3 步从数据库中获取的用户的密码
		String credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
		// realmName: 当前 Realm 的 name. 可以直接调用 getName() 方法完成
		String realmName = getName();
		// credentialsSalt: 密码加密时的盐. 为 ByteSource 类型
		ByteSource credentialsSalt = ByteSource.Util.bytes("admin");
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, credentialsSalt,
				realmName);

		return info;
	}

	public static void main(String[] args) {
		String hashAlgorithmName = "MD5";
		String credentials = "123456";
		ByteSource salt = ByteSource.Util.bytes("admin");
		int hashIterations = 1024;

		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		System.out.println(result);// 038bdaf98f2037b31f1e75b5b4c9b26e
	}

}
