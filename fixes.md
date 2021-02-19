# Fixes & Changes Required in Order toMake a Native Image

## Background

* Security Services / Providers need to be registered at Image Build Time. This is because they require access to parts of the JVM that are not available within SVM
* When you register a new security provider, it may create a Random tat will be saved to the image heap. This will cause native image building to fail (Random's can not be saved to the image heap)
* Use rerun inititalisation at runtime. This causes nothing associated with a class to be saved to the image heap
* Use a feature to configure running the initialiser of the security provider at build time

# Fixes

Native Image  Script:

```shell
--enable-all-security-services \
--rerun-class-initialization-at-runtime=org.bouncycastle.jcajce.provider.drbg.DRBG\$Default,org.bouncycastle.jcajce.provider.drbg.DRBG\$NonceAndIV \
```
Feature:

```java
@AutomaticFeature
public class BouncyCastleFeature implements Feature {

	@Override
	public void afterRegistration(AfterRegistrationAccess access) {
		System.out.println("afterRegistration : READ ME");
		RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
		//Security.addProvider(new BouncyCastleProvider());
		EnvironmentConfigurer.configure();
	}
}
```
